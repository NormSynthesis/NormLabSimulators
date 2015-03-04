package es.csic.iiia.normlab.onlinecomm.nsm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.util.ContextUtils;
import es.csic.iiia.normlab.onlinecomm.agents.CommunityAgent;
import es.csic.iiia.normlab.onlinecomm.agents.norms.CommunityAgentReasoner;
import es.csic.iiia.normlab.onlinecomm.agents.norms.CommunityFactFactory;
import es.csic.iiia.normlab.onlinecomm.agents.norms.CommunityNormEngine;
import es.csic.iiia.normlab.onlinecomm.context.ContextData;
import es.csic.iiia.normlab.onlinecomm.metrics.CommunityMetricsManager;
import es.csic.iiia.normlab.onlinecomm.nsm.agent.CommunityAgentAction;
import es.csic.iiia.nsm.IncorrectSetupException;
import es.csic.iiia.nsm.NormSynthesisMachine;
import es.csic.iiia.nsm.NormSynthesisMachine.NormGeneralisationMode;
import es.csic.iiia.nsm.agent.language.PredicatesDomains;
import es.csic.iiia.nsm.agent.language.SetOfPredicatesWithTerms;
import es.csic.iiia.nsm.agent.language.TaxonomyOfTerms;
import es.csic.iiia.nsm.config.DomainFunctions;
import es.csic.iiia.nsm.config.NormSynthesisSettings;
import es.csic.iiia.nsm.norm.Norm;
import es.csic.iiia.nsm.norm.NormModality;
import es.csic.iiia.nsm.norm.NormativeSystem;
import es.csic.iiia.nsm.perception.Sensor;
import es.csic.iiia.nsm.strategy.NormSynthesisStrategy;

/**
 * 
 * @author "Javier Morales (jmorales@iiia.csic.es)"
 * 
 */
public class CommunityNormSynthesisAgent {

	// ---------------------------------------------------------------------------
	// Static attributes
	// ---------------------------------------------------------------------------

	private static CommunityFactFactory factFactory;
	private static CommunityNormEngine nsNormEngine;
	private static CommunityAgentReasoner agentsReasoner;

	// ---------------------------------------------------------------------------
	// Attributes
	// ---------------------------------------------------------------------------

	private ContextData contextData;
	
	private NormSynthesisMachine nsm;
	private NormSynthesisSettings nsmSettings;
	private DomainFunctions dmFunctions;
	private NormativeSystem normativeSystem;
	private List<Norm> addedNorms;
	private List<Norm> removedNorms;

	private List<CommunityAgent> agents;
	private CommunityMetricsManager metricsManager;
	private PredicatesDomains predDomains;

	// ---------------------------------------------------------------------------
	// Methods
	// ---------------------------------------------------------------------------

	/**
	 * 
	 */
	public CommunityNormSynthesisAgent(Sensor watcher,
	    ContextData context, long randomSeed, boolean withGui) {

		this.contextData = context;
		this.normativeSystem = new NormativeSystem();
		this.addedNorms = new ArrayList<Norm>();
		this.removedNorms = new ArrayList<Norm>();
		this.nsmSettings = new CommunityNormSynthesisSettings();
		
		/* Create predicates domains */
		this.predDomains = this.createPredicatesDomains();

		/* Create class to generate facts that describe the on-line community
		 * scenario */
		factFactory = new CommunityFactFactory(predDomains);
		
		/* Create online community domain functions */
		this.dmFunctions = new CommunityDomainFunctions(factFactory);
		
		/* Create norm engine to reason about norms */
		nsNormEngine = new CommunityNormEngine(this.dmFunctions, predDomains);

		/* Create norm synthesis machine */
		this.nsm = new NormSynthesisMachine(nsmSettings, predDomains, dmFunctions,
		    withGui, randomSeed);

		/* Add sensors to the monitor of the norm synthesis machine */
		this.nsm.addSensor(watcher);

		agentsReasoner = new CommunityAgentReasoner(predDomains,contextData);
		
		/* Finally, set norm synthesis strategy (SIMON in this case) */

//		/* Generate pool of norms */
//		// List<Norm> poolOfNorms = this.generateDefaultPoolOfNorms();
//		List<Norm> poolOfNorms = new ArrayList<Norm>();
//
//		/* Set norm synthesis strategy */
//		// this.nsm.useIRONNormSynthesisStrategy();
//		this.nsm.useSIMONNormSynthesisStrategy(NormGeneralisationMode.Deep, 1,
//		    poolOfNorms);
//
//		// this.nsm.useXSIMONNormSynthesisStrategy(SIMONGeneralisationMode.Deep, 1);

//		/* Create metrics */
//		this.metricsManager = new CommunityMetricsManager(context, nsm);
//		this.nsm.useNormSynthesisMetrics(metricsManager);

		
		/* 3. Set the norm synthesis strategy */
		this.setNormSynthesisStrategy();
		
		/* CBR frame */
		if (withGui) {
			// CBRFrame cbrFrame = new CBRFrame(nsm);
			// cbrFrame.setVisible(true);
		}
	}

	/**
	 * @throws IncorrectSetupException
	 * 
	 */
	public void step() throws IncorrectSetupException {
		double startTime, finishTime, compTime;
		NormativeSystem newNormativeSystem;
		
		this.addedNorms.clear();
		this.removedNorms.clear();
		
		double tick = RunEnvironment.getInstance().
				getCurrentSchedule().getTickCount();
		
		if (this.agents == null) {
			this.agents = this.getAgents();
		}

		/* Save time just before executing the strategy */
		startTime = System.nanoTime();
		
		/* Execute strategy and obtain new normative system */
		newNormativeSystem = nsm.executeStrategy(tick);
		
		/* Compute elapsed time during the strategy execution */
		finishTime = System.nanoTime();
		compTime = (finishTime - startTime) / 1000000;
		
		this.metricsManager.addNewComputationTime(compTime);
		
		
		
		
		/* Check norm additions to the normative system */ 
		for(Norm norm : newNormativeSystem) {
			if(!normativeSystem.contains(norm)) {
				this.normativeSystem.add(norm);
				this.addedNorms.add(norm);
				
				nsNormEngine.addNorm(norm);
				agentsReasoner.addNorm(norm);
			}
		}

		// Check norm removals
		for(Norm norm : normativeSystem) {
			if(!newNormativeSystem.contains(norm)) {
				this.removedNorms.add(norm);
			}
		}	

		// Remove norms from normative systems
		for(Norm norm : removedNorms) {
			this.normativeSystem.remove(norm);
			agentsReasoner.removeNorm(norm);
		}
		
		if(tick == 5000) {
			System.out.println(this.normativeSystem);
		}
	}

	/**
	 * Getter of Agents to get a ArrayList of agents.
	 * 
	 * @return ArrayList of agents.
	 */
	private List<CommunityAgent> getAgents() {
		List<CommunityAgent> agents = new ArrayList<CommunityAgent>();
		Context context = ContextUtils.getContext(this);
		Iterator iElements = context.iterator();

		while (iElements.hasNext()) {
			Object o = iElements.next();
			if (o instanceof CommunityAgent) {
				agents.add((CommunityAgent) o);
			}
		}
		return agents;
	}

	/**
	 * Sets the norm synthesis strategy
	 */
	protected void setNormSynthesisStrategy() {
		NormGeneralisationMode genMode = nsmSettings.getNormGeneralisationMode();
		int genStep = nsmSettings.getNormGeneralisationStep();

		NormSynthesisStrategy.Option option = null;

		/* Finally, set norm synthesis strategy */
		switch(CommunityNormSynthesisSettings.NORM_SYNTHESIS_STRATEGY) {

		case 0:			this.setCustomNormSynthesisStrategy();						break;
		case 1:			option = NormSynthesisStrategy.Option.IRON;				break;
		case 2:			option = NormSynthesisStrategy.Option.SIMON;			break;
		case 3:			option = NormSynthesisStrategy.Option.SIMONPlus;	break;
		case 4:			option = NormSynthesisStrategy.Option.LION;				break;
		}

		this.metricsManager = new CommunityMetricsManager(contextData, nsm);
		this.nsm.setup(option, genMode, genStep, metricsManager, null, null);
	}
	
	/**
	 * Sets a custom norm synthesis strategy
	 */
	protected void setCustomNormSynthesisStrategy() {
		
	}
	
	/**
	 * Creates the predicate and their domains for the traffic scenario
	 */
	private PredicatesDomains createPredicatesDomains() {
		PredicatesDomains predicatesDomains = new PredicatesDomains();
		
		/* Predicate "section" domain*/
		TaxonomyOfTerms secPredTaxonomy = new TaxonomyOfTerms("sec");
		secPredTaxonomy.addTerm("*");
		secPredTaxonomy.addTerm("1");
		secPredTaxonomy.addTerm("2");
		secPredTaxonomy.addTerm("3");
		
		secPredTaxonomy.addRelationship("1", "*");
		secPredTaxonomy.addRelationship("2", "*");
		secPredTaxonomy.addRelationship("3", "*");
		
		/* Predicate "content" domain*/
		TaxonomyOfTerms cntTypePredTaxonomy = new TaxonomyOfTerms("cnt");
		cntTypePredTaxonomy.addTerm("correct");
		cntTypePredTaxonomy.addTerm("spam");
		cntTypePredTaxonomy.addTerm("porn");
		cntTypePredTaxonomy.addTerm("violent");
		cntTypePredTaxonomy.addTerm("insult");
		
		cntTypePredTaxonomy.addRelationship("correct", "*");
		cntTypePredTaxonomy.addRelationship("spam", "*");
		cntTypePredTaxonomy.addRelationship("porn", "*");
		cntTypePredTaxonomy.addRelationship("violent", "*");
		cntTypePredTaxonomy.addRelationship("insult", "*");
		
		/* Predicate "user" domain*/		
		if(CommunityNormSynthesisSettings.NORMS_WITH_USER_ID) {
			TaxonomyOfTerms usrPredTaxonomy = new TaxonomyOfTerms("usr");
			usrPredTaxonomy.addTerm("*");
		
			/* Add users */
			int numAgents = CommunityNormSynthesisSettings.SIM_NUM_AGENTS;
			for (int numAgent = 0 ; numAgent <= numAgents; numAgent++){
				usrPredTaxonomy.addTerm("" + numAgent);
				usrPredTaxonomy.addRelationship("" + numAgent, "*");
			}
			predicatesDomains.addPredicateDomain("usr", usrPredTaxonomy);
		}
		
		predicatesDomains.addPredicateDomain("sec", secPredTaxonomy);
		predicatesDomains.addPredicateDomain("cnt", cntTypePredTaxonomy);
		
		return predicatesDomains;
	}
	
	/**
	 * 
	 * @return
	 */
	private List<Norm> generateDefaultPoolOfNorms() {
		List<Norm> norms = new ArrayList<Norm>();

		SetOfPredicatesWithTerms n1Precondition = new SetOfPredicatesWithTerms();
		n1Precondition.add("usr", "*");
		n1Precondition.add("sec", "*");
		n1Precondition.add("cnt", "spam");

		SetOfPredicatesWithTerms n2Precondition = new SetOfPredicatesWithTerms();
		n2Precondition.add("usr", "1");
		n2Precondition.add("sec", "1");
		n2Precondition.add("cnt", "spam");

		SetOfPredicatesWithTerms n3Precondition = new SetOfPredicatesWithTerms();
		n3Precondition.add("usr", "1");
		n3Precondition.add("sec", "*");
		n3Precondition.add("cnt", "spam");

		/* Create norm */
		Norm n1 = new Norm(n1Precondition, NormModality.Prohibition,
		    CommunityAgentAction.Upload);

		Norm n2 = new Norm(n2Precondition, NormModality.Prohibition,
		    CommunityAgentAction.Upload);

		Norm n3 = new Norm(n3Precondition, NormModality.Prohibition,
		    CommunityAgentAction.Upload);

		norms.add(n1);
		norms.add(n2);
		norms.add(n3);

		return norms;
	}

	/**
	 * 
	 * @return
	 */
	public CommunityMetricsManager getMetricsManager() {
		return this.metricsManager;
	}

	/**
	 * 
	 * @return
	 */
	public List<Norm> getAddedNorms() {
		return this.addedNorms;
	}

	/**
	 * 
	 * @return
	 */
	public List<Norm> getRemovedNorms() {
		return this.removedNorms;
	}

	/**
	 * 
	 */
	public NormativeSystem getNormativeSystem() {
		return normativeSystem;
	}

	/**
	 * 
	 * @return
	 */
	public NormSynthesisMachine getNormSynthesisMachine() {
		return this.nsm;
	}

	/**
	 * 
	 * @return
	 */
	public static CommunityFactFactory getFactFactory() {
		return factFactory;
	}

	/**
	 * 
	 * @return
	 */
	public static CommunityNormEngine getNormEngine() {
		return nsNormEngine;
	}

	/**
	 * 
	 * @return
	 */
	public static CommunityAgentReasoner getAgentsReasoner() {
		return agentsReasoner;
	}
	
	/**
	 * 
	 * @return
	 */
	public DomainFunctions getDomainFunctions() {
		return this.dmFunctions;
	}
	
	/**
	 * 
	 * @return
	 */
	public PredicatesDomains getPredicatesDomains() {
		return this.predDomains;
	}
}
