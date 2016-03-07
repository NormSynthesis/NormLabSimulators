package es.csic.iiia.normlab.traffic.custom;

import es.csic.iiia.nsm.net.norm.NormativeNetwork;
import es.csic.iiia.nsm.norm.NormativeSystem;

/**
 * 
 */
public class MyFirstStrategy implements es.csic.iiia.nsm.strategy.NormSynthesisStrategy {

	private NormativeNetwork normativeNetwork;
	
	/**
	 * 
	 * @param nsm
	 */
	public MyFirstStrategy(es.csic.iiia.nsm.NormSynthesisMachine nsm) {
		
		/* Get norm synthesis elements */
		this.normativeNetwork = nsm.getNormativeNetwork();
	}
	
	/**
	 * 
	 */
	@Override
  public NormativeSystem execute() {
		return normativeNetwork.getNormativeSystem();
  }

	/**
	 * 
	 */
	@Override
  public boolean hasNonRegulatedConflictsThisTick() {
	  return false;
  }

	@Override
  public void newNonRegulatedConflictsSolvedThisTick() {}
	
}

