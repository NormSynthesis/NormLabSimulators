package es.csic.iiia.normlab.traffic.examples.ex1;

import es.csic.iiia.nsm.NormSynthesisMachine;
import es.csic.iiia.nsm.net.norm.NormativeNetwork;
import es.csic.iiia.nsm.norm.NormativeSystem;
import es.csic.iiia.nsm.strategy.NormSynthesisStrategy;

/**
 * 
 * @author "Javier Morales (jmorales@iiia.csic.es)"
 *
 */
public class TrafficNSExample1_NSStrategy implements NormSynthesisStrategy{


	private NormativeNetwork normativeNetwork;
	
	/**
	 * Constructor of the first norm synthesis example strategy  
	 * 
	 * @param nsm
	 */
	public TrafficNSExample1_NSStrategy(NormSynthesisMachine nsm) {
		this.normativeNetwork = nsm.getNormativeNetwork();
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
