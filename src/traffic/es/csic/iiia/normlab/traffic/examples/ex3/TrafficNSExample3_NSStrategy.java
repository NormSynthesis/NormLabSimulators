package es.csic.iiia.normlab.traffic.examples.ex3;

import es.csic.iiia.nsm.NormSynthesisMachine;
import es.csic.iiia.nsm.net.norm.NormativeNetwork;
import es.csic.iiia.nsm.norm.NormativeSystem;
import es.csic.iiia.nsm.strategy.NormSynthesisStrategy;

/**
 * 
 * @author "Javier Morales (jmorales@iiia.csic.es)"
 *
 */
public class TrafficNSExample3_NSStrategy implements NormSynthesisStrategy {
	
	/* The normative network, a structure to keep track of synthesised norms */
	private NormativeNetwork normativeNetwork;
	
	/**
	 * 
	 * @param nsm
	 */
	public TrafficNSExample3_NSStrategy(NormSynthesisMachine nsm) {
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
}
