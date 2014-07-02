package es.csic.iiia.normlab.traffic.examples.ex4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.csic.iiia.nsm.NormSynthesisMachine;
import es.csic.iiia.nsm.agent.language.PredicatesDomains;
import es.csic.iiia.nsm.config.DomainFunctions;
import es.csic.iiia.nsm.config.Goal;
import es.csic.iiia.nsm.config.NormSynthesisSettings;
import es.csic.iiia.nsm.net.norm.NormativeNetwork;
import es.csic.iiia.nsm.norm.Norm;
import es.csic.iiia.nsm.norm.NormativeSystem;
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
public class TrafficNSExample4_NSStrategy implements NormSynthesisStrategy {



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
	protected TrafficNSExample4_NSOperators operators;
	protected List<ViewTransition> viewTransitions;

	protected Map<Goal,List<Conflict>> conflicts;
	protected List<Norm> createdNorms;
	protected List<Norm> activatedNorms;
	protected List<Norm> normAdditions;
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
	public TrafficNSExample4_NSStrategy(NormSynthesisMachine nsm) {

		this.nsm = nsm;
		this.nsmSettings = nsm.getNormSynthesisSettings();
		this.dmFunctions = nsm.getDomainFunctions();
		this.predicatesDomains = this.nsm.getPredicatesDomains();
		this.normativeNetwork = nsm.getNormativeNetwork();
		this.monitor = nsm.getMonitor();

		this.normReasoner = new NormReasoner(this.nsmSettings.getSystemGoals(), 
				this.predicatesDomains, this.dmFunctions);

		this.operators = new TrafficNSExample4_NSOperators(this, normReasoner, nsm);
		this.conflicts = new HashMap<Goal, List<Conflict>>();

		this.viewTransitions = new ArrayList<ViewTransition>();
		this.createdNorms = new ArrayList<Norm>();
		this.activatedNorms = new ArrayList<Norm>();
		this.normAdditions = new ArrayList<Norm>();
		this.normDeactivations = new ArrayList<Norm>();

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

		/*-------------------
		 *  Norm generation
		 *-------------------*/

		this.normGeneration();
		
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
				goalConflicts.addAll(dmFunctions.getNonRegulatedConflicts(goal, vTrans));
			}  	
			conflicts.put(goal, goalConflicts);
		}
		return conflicts;
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
	public Map<Goal, List<Conflict>> getNonRegulatedConflictsThisTick() {
		return this.conflicts;
	}
}