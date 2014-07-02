package es.csic.iiia.normlab.traffic.examples.ex3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.csic.iiia.normlab.traffic.car.CarAction;
import es.csic.iiia.nsm.NormSynthesisMachine;
import es.csic.iiia.nsm.agent.language.SetOfPredicatesWithTerms;
import es.csic.iiia.nsm.config.Goal;
import es.csic.iiia.nsm.net.norm.NormativeNetwork;
import es.csic.iiia.nsm.norm.Norm;
import es.csic.iiia.nsm.norm.NormModality;
import es.csic.iiia.nsm.norm.NormativeSystem;
import es.csic.iiia.nsm.norm.generation.Conflict;
import es.csic.iiia.nsm.strategy.NormSynthesisStrategy;

/**
 * 
 * @author "Javier Morales (jmorales@iiia.csic.es)"
 *
 */
public class TrafficNSExample3_NSStrategy implements NormSynthesisStrategy{

	private List<Goal> goals;
	private Map<Goal, List<Conflict>> conflicts;
	private NormativeNetwork normativeNetwork;
	
	/**
	 * 
	 * @param nsm
	 */
	public TrafficNSExample3_NSStrategy(NormSynthesisMachine nsm) {
		this.goals = nsm.getNormSynthesisSettings().getSystemGoals();
		this.conflicts = new HashMap<Goal, List<Conflict>>();
		this.normativeNetwork = nsm.getNormativeNetwork();
		
		/* Create the normative system */
		this.createNormativeSystem();
	}
	
	/**
	 * A basic norm synthesis strategy that always outputs an
	 * empty normative system
	 * 
	 * @return an empty normative system
	 */
	@Override
  public NormativeSystem execute() {
		return this.normativeNetwork.getNormativeSystem();
  }
	
	/**
	 * Creates a normative system that allows all possible collisions in the
	 * road traffic scenario
	 */
	private void createNormativeSystem() {

		/* Get system goal (to avoid collisions) */
		Goal goalAvoidCollisions = goals.get(0);
		
		/* Create norm preconditions */
		SetOfPredicatesWithTerms n1Precondition = new SetOfPredicatesWithTerms();
		n1Precondition.add("l", "*");
		n1Precondition.add("f", "^");
		n1Precondition.add("r", "*");
		
		SetOfPredicatesWithTerms n2Precondition = new SetOfPredicatesWithTerms();
		n2Precondition.add("l", ">");
		n2Precondition.add("f", "-");
		n2Precondition.add("r", "*");
		
		SetOfPredicatesWithTerms n3Precondition = new SetOfPredicatesWithTerms();
		n3Precondition.add("l", "<");
		n3Precondition.add("f", "<");
		n3Precondition.add("r", "*");

		
		/* Create norms */
		Norm n1 = new Norm(n1Precondition, 
				NormModality.Prohibition, CarAction.Go, goalAvoidCollisions);

		Norm n2 = new Norm(n2Precondition, 
				NormModality.Prohibition, CarAction.Go, goalAvoidCollisions);

		Norm n3 = new Norm(n3Precondition, 
				NormModality.Prohibition, CarAction.Go, goalAvoidCollisions);

		/* Add the norms to the normative network and activate them */
		this.normativeNetwork.add(n1);
		this.normativeNetwork.add(n2);
		this.normativeNetwork.add(n3);
		
		this.normativeNetwork.activate(n1);
		this.normativeNetwork.activate(n2);
		this.normativeNetwork.activate(n3);
	}
	
	/**
	 * 
	 */
	@Override
  public Map<Goal, List<Conflict>> getNonRegulatedConflictsThisTick() {
		return this.conflicts;
  }
}
