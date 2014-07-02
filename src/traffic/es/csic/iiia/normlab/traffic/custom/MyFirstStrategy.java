package es.csic.iiia.normlab.traffic.custom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.csic.iiia.nsm.config.Goal;
import es.csic.iiia.nsm.net.norm.NormativeNetwork;
import es.csic.iiia.nsm.norm.NormativeSystem;
import es.csic.iiia.nsm.norm.generation.Conflict;

/**
 * 
 */
public class MyFirstStrategy implements es.csic.iiia.nsm.strategy.NormSynthesisStrategy {

	private Map<Goal, List<Conflict>> conflicts; // to save conflicts
	private NormativeNetwork normativeNetwork;
	
	/**
	 * 
	 * @param nsm
	 */
	public MyFirstStrategy(es.csic.iiia.nsm.NormSynthesisMachine nsm) {
		this.conflicts = new HashMap<Goal, List<Conflict>>();
		
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
  public Map<Goal, List<Conflict>> getNonRegulatedConflictsThisTick() {
	  return conflicts;
  }
}

