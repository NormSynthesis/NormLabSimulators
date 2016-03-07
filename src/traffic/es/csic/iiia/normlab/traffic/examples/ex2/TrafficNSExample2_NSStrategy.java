package es.csic.iiia.normlab.traffic.examples.ex2;

import java.util.List;

import es.csic.iiia.normlab.traffic.car.CarAction;
import es.csic.iiia.nsm.NormSynthesisMachine;
import es.csic.iiia.nsm.agent.language.SetOfPredicatesWithTerms;
import es.csic.iiia.nsm.config.Goal;
import es.csic.iiia.nsm.net.norm.NetworkNodeState;
import es.csic.iiia.nsm.net.norm.NormativeNetwork;
import es.csic.iiia.nsm.norm.Norm;
import es.csic.iiia.nsm.norm.NormModality;
import es.csic.iiia.nsm.norm.NormativeSystem;
import es.csic.iiia.nsm.strategy.NormSynthesisStrategy;

/**
 * 
 * @author "Javier Morales (jmorales@iiia.csic.es)"
 *
 */
public class TrafficNSExample2_NSStrategy implements NormSynthesisStrategy{

	private NormativeNetwork normativeNetwork;
	
	/**
	 * 
	 * @param nsm
	 */
	public TrafficNSExample2_NSStrategy(NormSynthesisMachine nsm) {
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
	 * Creates a normative system with a few core norms that are 
	 * highly effective and necessary
	 */
	private void createNormativeSystem() {

		/* Create norms */
		SetOfPredicatesWithTerms n1Precondition = new SetOfPredicatesWithTerms();
		n1Precondition.add("l", ">");
		n1Precondition.add("f", "*");
		n1Precondition.add("r", "*");
		
		/* Create norm */
		Norm n1 = new Norm(n1Precondition, 
				NormModality.Prohibition, CarAction.Go);
		
		/* Add the norm to the normative network and activate it */
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
