package es.csic.iiia.normlab.traffic.custom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.csic.iiia.normlab.traffic.car.CarAction;
import es.csic.iiia.nsm.agent.language.SetOfPredicatesWithTerms;
import es.csic.iiia.nsm.config.Goal;
import es.csic.iiia.nsm.net.norm.NormativeNetwork;
import es.csic.iiia.nsm.norm.Norm;
import es.csic.iiia.nsm.norm.NormModality;
import es.csic.iiia.nsm.norm.NormativeSystem;
import es.csic.iiia.nsm.norm.generation.Conflict;

/**
 * 
 */
public class MySecondStrategy implements es.csic.iiia.nsm.strategy.NormSynthesisStrategy {

	private List<Goal> goals;
	private Map<Goal, List<Conflict>> conflicts; // to save conflicts
	private NormativeNetwork normativeNetwork;
	
	/**
	 * 
	 * @param nsm
	 */
	public MySecondStrategy(es.csic.iiia.nsm.NormSynthesisMachine nsm) {
		this.conflicts = new HashMap<Goal, List<Conflict>>();
		
		/* Get norm synthesis elements */
		this.goals = nsm.getNormSynthesisSettings().getSystemGoals();
		this.normativeNetwork = nsm.getNormativeNetwork();
		
		this.createNormativeSystem();
	}
	
	/**
	 * 
	 */
	@Override
  public NormativeSystem execute() {
		return normativeNetwork.getNormativeSystem();
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
		n1Precondition.add("l", ">");
		n1Precondition.add("f", "*");
		n1Precondition.add("r", "*");
		
		/* Create norms */
		Norm n1 = new Norm(n1Precondition, 
				NormModality.Prohibition, CarAction.Go, goalAvoidCollisions);

		/* Add the norms to the normative network and activate them */
		this.normativeNetwork.add(n1);
		this.normativeNetwork.activate(n1);
	}
	
	/**
	 * 
	 */
	@Override
  public Map<Goal, List<Conflict>> getNonRegulatedConflictsThisTick() {
	  return conflicts;
  }
}

