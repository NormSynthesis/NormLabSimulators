package es.csic.iiia.normlab.onlinecomm.nsm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.util.ContextUtils;
import es.csic.iiia.normlab.onlinecomm.agents.CommunityAgent;
import es.csic.iiia.normlab.onlinecomm.agents.norms.CommunityFactFactory;
import es.csic.iiia.normlab.onlinecomm.agents.norms.CommunityNormEngine;
import es.csic.iiia.normlab.onlinecomm.context.ContextData;
import es.csic.iiia.normlab.onlinecomm.nsm.perception.CommunityWatcher;
import es.csic.iiia.nsm.IncorrectSetupException;
import es.csic.iiia.nsm.NormSynthesisMachine;
import es.csic.iiia.nsm.NormSynthesisMachine.NormGeneralisationMode;
import es.csic.iiia.nsm.agent.language.PredicatesDomains;
import es.csic.iiia.nsm.config.DomainFunctions;
import es.csic.iiia.nsm.config.NormSynthesisSettings;
import es.csic.iiia.nsm.metrics.NormSynthesisMetrics;
import es.csic.iiia.nsm.norm.Norm;
import es.csic.iiia.nsm.norm.NormativeSystem;

/**
 * 
 * @author "Javier Morales (jmorales@iiia.csic.es)"
 *
 */
public class CommunityNormSynthesisAgent {

	//---------------------------------------------------------------------------
	// Static attributes
	//---------------------------------------------------------------------------
	
	private static CommunityFactFactory factFactory;
	private static CommunityNormEngine normEngine;
	
	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------
	
	private NormSynthesisMachine nsm;
	private NormSynthesisSettings nsmSettings;
	private DomainFunctions dmFunctions;
	private NormativeSystem normativeSystem;
	private List<Norm> addedNorms;
	private List<Norm> removedNorms;
	
	private List<CommunityAgent> agents;


	//---------------------------------------------------------------------------
	// Methods
	//---------------------------------------------------------------------------
		
	/**
	 * 
	 */
	public CommunityNormSynthesisAgent(CommunityWatcher watcher,
			ContextData context, PredicatesDomains predDomains) {
		
		boolean isGui = !RunEnvironment.getInstance().isBatch();
		
		this.normativeSystem = new NormativeSystem();
		this.addedNorms = new ArrayList<Norm>();
		this.removedNorms = new ArrayList<Norm>();
		this.nsmSettings = new CommunityNormSynthesisSettings();
		this.dmFunctions = new CommunityDomainFunctions();
		
		/* Create norm engine to reason about norms */
		normEngine = new CommunityNormEngine(this.dmFunctions, predDomains);
		
		/* Create class to generate facts that describe the
		 * on-line community scenario */
		factFactory = new CommunityFactFactory(predDomains);
		
		
		/* Create norm synthesis machine */
		this.nsm = new NormSynthesisMachine(nsmSettings, predDomains, 
				dmFunctions, isGui);
		
		/* Setup norm synthesis machine (utility function, omega function */
		this.nsm.useDefaultOmegaFunction();
		
		/* Add sensors to the monitor of the norm synthesis machine */
		this.nsm.addSensor(watcher);	
		
		/* Finally, set norm synthesis strategy (SIMON in this case) */
		
//		this.nsm.useIRONNormSynthesisStrategy();
		this.nsm.useSIMONNormSynthesisStrategy(NormGeneralisationMode.Deep, 1);
//		this.nsm.useXSIMONNormSynthesisStrategy(SIMONGeneralisationMode.Deep, 1);
		
//		TrafficExampleStrategy1 strategy = new TrafficExampleStrategy1(nsm);
//		this.nsm.useStrategy(strategy);
		
		
		/* CBR frame */
		if(isGui){
//			CBRFrame cbrFrame = new CBRFrame(nsm);
//			cbrFrame.setVisible(true);
		}
	}
	
	/**
	 * @throws IncorrectSetupException 
	 * 
	 */
	public void step() throws IncorrectSetupException {
		this.addedNorms.clear();
		this.removedNorms.clear();
		
		if(this.agents == null) {
			this.agents = this.getAgents();
		}
		
		/* Execute strategy and obtain new normative system */
		NormativeSystem newNormativeSystem = nsm.executeStrategy();
		
		/* Check norm additions to the normative system */ 
		for(Norm norm : newNormativeSystem) {
			if(!normativeSystem.contains(norm)) {
				this.normativeSystem.add(norm);
				this.addedNorms.add(norm);
				normEngine.addNorm(norm);
//				
//				this.applicabilityFunction.addNorm(norm);
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
			//this.applicabilityFunction.removeNorm(norm); // TODO: We want to evaluate ALL norms
		}
	}
	
	/**
	 * Getter of Agents to get a ArrayList of agents.
	 * 
	 * @return ArrayList of agents.
	 */
	private List<CommunityAgent> getAgents(){
		List<CommunityAgent> agents = new ArrayList<CommunityAgent>();
		Context context = ContextUtils.getContext(this);
		Iterator iElements = context.iterator();

		while(iElements.hasNext()){
			Object o = iElements.next();
			if(o instanceof CommunityAgent){
				agents.add((CommunityAgent) o);
			}	
		}
		return agents;
	}
	
	/**
	 * 
	 * @return
	 */
	public NormSynthesisMetrics getMetrics() {
		return this.nsm.getNormSynthesisMetrics();
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
		return normEngine;
	}
	
	/**
	 * 
	 * @return
	 */
	public DomainFunctions getDomainFunctions() {
		return this.dmFunctions;
	}
}
