package es.csic.iiia.normlab.traffic.custom;

import java.util.List;

import es.csic.iiia.normlab.traffic.car.CarAction;
import es.csic.iiia.nsm.agent.language.SetOfPredicatesWithTerms;
import es.csic.iiia.nsm.config.Goal;
import es.csic.iiia.nsm.net.norm.NetworkNodeState;
import es.csic.iiia.nsm.net.norm.NormativeNetwork;
import es.csic.iiia.nsm.norm.Norm;
import es.csic.iiia.nsm.norm.NormModality;
import es.csic.iiia.nsm.norm.NormativeSystem;

/**
 * 
 */
public class MySecondStrategy implements es.csic.iiia.nsm.strategy.NormSynthesisStrategy {

	private NormativeNetwork normativeNetwork;
	
	/**
	 * 
	 * @param nsm
	 */
	public MySecondStrategy(es.csic.iiia.nsm.NormSynthesisMachine nsm) {
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
		
		/* Create norm preconditions */
		SetOfPredicatesWithTerms n1Precondition = new SetOfPredicatesWithTerms();
		n1Precondition.add("l", ">");
		n1Precondition.add("f", "*");
		n1Precondition.add("r", "*");
		
		/* Create norms */
		Norm n1 = new Norm(n1Precondition, 
				NormModality.Prohibition, CarAction.Go);

		/* Add the norms to the normative network and activate them */
		this.normativeNetwork.add(n1);
		normativeNetwork.setState(n1, NetworkNodeState.ACTIVE);
	}
	
	/**
	 * 
	 */
	@Override
  public boolean hasNonRegulatedConflictsThisTick() {
	  return false;
  }

	@Override
  public void newNonRegulatedConflictsSolvedThisTick() {
	  // TODO Auto-generated method stub
	  
  }
}

