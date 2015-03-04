package es.csic.iiia.normlab.traffic.custom;

import es.csic.iiia.nsm.net.norm.NormativeNetwork;
import es.csic.iiia.nsm.norm.NormativeSystem;

/**
 * 
 */
public class MyFirstStrategy implements es.csic.iiia.nsm.strategy.NormSynthesisStrategy {

	/* The normative network, a data structure to keep track of norms */
	private NormativeNetwork normativeNetwork;
	
	/**
	 * Constructor of the strategy
	 * 
	 * @param nsm
	 */
	public MyFirstStrategy(es.csic.iiia.nsm.NormSynthesisMachine nsm) {
		
		/* Get normative network */
		this.normativeNetwork = nsm.getNormativeNetwork();
	}
	
	/**
	 * Executes your strategy
	 */
	@Override
  public NormativeSystem execute() {
		return normativeNetwork.getNormativeSystem();
  }
}

