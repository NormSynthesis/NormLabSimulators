package es.csic.iiia.normlab.traffic.examples.ex5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.csic.iiia.nsm.NormSynthesisMachine;
import es.csic.iiia.nsm.agent.language.PredicatesDomains;
import es.csic.iiia.nsm.config.Dimension;
import es.csic.iiia.nsm.config.DomainFunctions;
import es.csic.iiia.nsm.config.Goal;
import es.csic.iiia.nsm.config.NormSynthesisSettings;
import es.csic.iiia.nsm.net.norm.NormativeNetwork;
import es.csic.iiia.nsm.norm.Norm;
import es.csic.iiia.nsm.norm.NormativeSystem;
import es.csic.iiia.nsm.norm.evaluation.NormComplianceOutcomes;
import es.csic.iiia.nsm.norm.evaluation.NormsApplicableInView;
import es.csic.iiia.nsm.norm.generation.Conflict;
import es.csic.iiia.nsm.norm.reasoning.NormReasoner;
import es.csic.iiia.nsm.perception.Monitor;
import es.csic.iiia.nsm.perception.ViewTransition;
import es.csic.iiia.nsm.strategy.NormSynthesisStrategy;

/**
 * The SIMON norm synthesis strategy
 * 
 * @author "Javier Morales (jmorales@iiia.csic.es)"
 */
public class TrafficNSExample5_NSStrategy implements NormSynthesisStrategy {

	/**
	 * Generalisation mode of SIMON. Mode <tt>Shallow<tt> sets SIMON to perform
	 * shallow generalisations, while mode <tt>Deep<tt> sets SIMON to perform
	 * deep generalisations
	 * 
	 * @author "Javier Morales (jmorales@iiia.csic.es)"
	 */
	public enum GeneralisationMode {
		Deep, Shallow;
	}

	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------

	protected NormSynthesisMachine nsm;
	protected NormSynthesisSettings nsmSettings; 
	protected NormReasoner normReasoner;
	protected NormativeNetwork normativeNetwork;

	protected DomainFunctions dmFunctions;
	protected PredicatesDomains predicatesDomains;
	protected Monitor monitor;

	protected TrafficNSExample5_UtilityFunction utilityFunction;	
	protected TrafficNSExample5_NSOperators operators;

	protected Map<Goal,List<Conflict>> conflicts;
	protected Map<Goal,Map<ViewTransition, NormComplianceOutcomes>> normCompliance;
	protected Map<ViewTransition, NormsApplicableInView> normApplicability;
	protected List<ViewTransition> viewTransitions;

	protected List<Norm> createdNorms;
	protected List<Norm> activatedNorms;
	protected List<Norm> 	normAdditions;
	protected List<Norm> normDeactivations;

	//---------------------------------------------------------------------------
	// Methods
	//---------------------------------------------------------------------------

	/**
	 * Constructor
	 * 
	 * @param 	nsm the norm synthesis machine
	 * @param 	genMode the SIMON generalisation mode
	 */
	public TrafficNSExample5_NSStrategy(NormSynthesisMachine nsm) {

		this.nsm = nsm;

		this.nsmSettings = nsm.getNormSynthesisSettings();
		this.dmFunctions = nsm.getDomainFunctions();
		this.predicatesDomains = this.nsm.getPredicatesDomains();
		this.normativeNetwork = nsm.getNormativeNetwork();
		this.monitor = nsm.getMonitor();

		this.normReasoner = new NormReasoner(this.nsmSettings.getSystemGoals(), 
				this.predicatesDomains, this.dmFunctions);

		this.operators = new TrafficNSExample5_NSOperators(this, normReasoner, nsm);
		this.utilityFunction = new TrafficNSExample5_UtilityFunction();
		this.conflicts = new HashMap<Goal, List<Conflict>>();

		this.normApplicability = new HashMap<ViewTransition, 
				NormsApplicableInView>();

		this.normCompliance = new HashMap<Goal,
				Map<ViewTransition,NormComplianceOutcomes>>();

		this.createdNorms = new ArrayList<Norm>();
		this.activatedNorms = new ArrayList<Norm>();
		this.normAdditions = new ArrayList<Norm>();
		this.normDeactivations = new ArrayList<Norm>();


		for(Goal goal : nsmSettings.getSystemGoals()) {
			this.normCompliance.put(goal, new HashMap<ViewTransition,
					NormComplianceOutcomes>());
		}
	}

	/**
	 * Executes IRON's strategy and outputs the resulting normative system.
	 * The norm synthesis cycle consists in three steps:
	 * <ol>
	 * <li> Norm generation. Generates norms for each detected conflict.
	 * 			Generated norms are aimed to avoid detected conflicts in the future;
	 * <li> Norm evaluation. Evaluates norms in terms of their effectiveness
	 * 			and necessity, based on the outcome of their compliances and 
	 * 			infringements, respectively; and
	 * <li> Norm refinement. Generalises norms which utilities are over
	 * 			generalisation thresholds, and specialises norms which utilities
	 * 			are under specialisation thresholds. Norm generalisations can be
	 * 			performed in Shallow or Deep mode.
	 * 
	 * @return the normative system resulting from the norm synthesis cycle
	 */
	public NormativeSystem execute() {	
		this.normAdditions.clear();
		this.normDeactivations.clear();
		this.createdNorms.clear();
		this.activatedNorms.clear();

		this.normGeneration();
		
		this.normEvaluation();

		/* Return the current normative system */
		return normativeNetwork.getNormativeSystem();
	}

	//---------------------------------------------------------------------------
	// Private methods
	//---------------------------------------------------------------------------

	/**
	 * Executes the norm generation phase
	 */
	private void normGeneration() {

		/* Obtain monitor perceptions */
		viewTransitions = new ArrayList<ViewTransition>();
		obtainPerceptions(viewTransitions);

		/* Conflict detection */
		conflicts = conflictDetection(viewTransitions);

		/* Norm generation */
		for(Goal goal : conflicts.keySet()) {
			for(Conflict conflict : conflicts.get(goal)) {
				operators.create(conflict, goal);
			}	
		}
	}

	/**
	 * Executes the norm evaluation phase
	 */
	private void normEvaluation() {

		/* Compute norm applicability */
		this.normApplicability = this.normApplicability(viewTransitions);

		/* Detect norm applicability and compliance */
		this.normCompliance(this.normApplicability);

		/* Update utilities and performances */
		this.updateUtilitiesAndPerformances(this.normCompliance);
	}

	/**
	 * Calls scenario monitors to perceive agents interactions
	 * 
	 * @return a {@code List} of the monitor perceptions, where each perception
	 *  				is a view transition from t-1 to t
	 */
	private void obtainPerceptions(List<ViewTransition> viewTransitions) {
		this.monitor.getPerceptions(viewTransitions);
	}

	/**
	 * Given a list of view transitions (from t-1 to t), this method
	 * returns a list of conflicts with respect to each goal of the system
	 * 
	 * @param viewTransitions the list of perceptions of each sensor
	 */
	protected Map<Goal, List<Conflict>> conflictDetection(
			List<ViewTransition> viewTransitions) {
		this.conflicts.clear();

		/* Conflict detection is computed in terms of a goal */
		for(Goal goal : this.nsmSettings.getSystemGoals())		{
			List<Conflict> goalConflicts = new ArrayList<Conflict>();

			for(ViewTransition vTrans : viewTransitions) {
				goalConflicts.addAll(dmFunctions.getConflicts(goal, vTrans));
			}  	
			conflicts.put(goal, goalConflicts);
		}
		return conflicts;
	}

	/**
	 * Computes the norm applicability perceived by each sensor.
	 * (recall that each view transition is perceived by a particular sensor)
	 * 
	 * @param vTransitions the list of perceptions of each sensor
	 * @return a map containing the norms that are applicable to each
	 * agent in each view transition
	 */
	protected Map<ViewTransition, NormsApplicableInView> normApplicability(
			List<ViewTransition> vTransitions)	{

		/* Clear norm applicability from previous tick */
		this.normApplicability.clear();

		/* Get applicable norms of each viewTransition (of each sensor) */
		for(ViewTransition vTrans : vTransitions) {
			NormsApplicableInView normApplicability;
			normApplicability = this.normReasoner.getNormsApplicable(vTrans);
			this.normApplicability.put(vTrans, normApplicability);
		}
		return this.normApplicability;
	}

	/**
	 * Computes norms' compliance based on the norms that were applicable 
	 * to each agent in the previous time step (norm applicability)
	 * and the actions that agents performed in the transition from the 
	 * previous to the current time step
	 * 
	 * @param normApplicability norms that were applicable in the previous tick
	 */
	protected void normCompliance(Map<ViewTransition,
			NormsApplicableInView> normApplicability) {

		/* Check norm compliance in the view in terms of each system goal */
		for(Goal goal : this.nsmSettings.getSystemGoals()) {

			/* Clear norm compliance of previous tick */
			this.normCompliance.get(goal).clear();

			/* Evaluate norm compliance and conflicts in each 
			 * view transition with respect to each system goal */
			for(ViewTransition vTrans : normApplicability.keySet()) {
				NormsApplicableInView vNormAppl = normApplicability.get(vTrans);

				/* If there is no applicable norm in the view, continue */
				if(vNormAppl.isEmpty()) {
					continue;
				}
				NormComplianceOutcomes nCompliance = this.normReasoner.
						checkNormComplianceAndOutcomes(vNormAppl, goal);

				this.normCompliance.get(goal).put(vTrans, nCompliance);
			}
		}
	}

	/**
	 * Updates norm utilities and performances based on
	 * their norm compliance in the current time step
	 * 
	 * @param normCompliance the norm compliance in the current time step
	 */
	protected void	updateUtilitiesAndPerformances(
			Map<Goal, Map<ViewTransition,NormComplianceOutcomes>> normCompliance) {

		for(Goal goal : this.nsmSettings.getSystemGoals()) {
			for(ViewTransition vTrans : normCompliance.get(goal).keySet()) {
				for(Dimension dim : this.nsm.getNormEvaluationDimensions())	{
					this.utilityFunction.evaluate(dim, goal, 
							normCompliance.get(goal).get(vTrans), normativeNetwork);	
				}
			}
		}
	}

	/**
	 * 
	 * @param norm
	 */
	public void normCreated(Norm norm) {
		this.createdNorms.add(norm);
		this.normAdditions.add(norm);
	}

	/**
	 * 
	 * @param norm
	 */
	public void normActivated(Norm norm) {
		this.activatedNorms.add(norm);
		this.normAdditions.add(norm);
	}

	/**
	 * 
	 * @param norm
	 */
	public void normDeactivated(Norm norm) {
		this.normDeactivations.add(norm);
	}

	/**
	 * 
	 * @return
	 */
	public List<Norm> getNewAdditionsToNormativeSystem() {
		return this.normAdditions;
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasNonRegulatedConflictsThisTick() {
		return false;
	}

	@Override
  public void newNonRegulatedConflictsSolvedThisTick() {
	  // TODO Auto-generated method stub
	  
  }
}