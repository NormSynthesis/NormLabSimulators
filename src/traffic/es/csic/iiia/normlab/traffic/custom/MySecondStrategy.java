package es.csic.iiia.normlab.traffic.custom;

import es.csic.iiia.normlab.traffic.car.CarAction;
import es.csic.iiia.nsm.agent.language.SetOfPredicatesWithTerms;
import es.csic.iiia.nsm.net.norm.NetworkNodeState;
import es.csic.iiia.nsm.net.norm.NormativeNetwork;
import es.csic.iiia.nsm.norm.Norm;
import es.csic.iiia.nsm.norm.NormModality;
import es.csic.iiia.nsm.norm.NormativeSystem;

/**
 * My second strategy
 */
public class MySecondStrategy implements es.csic.iiia.nsm.strategy.NormSynthesisStrategy {

	/* The normative network, a data structure to keep track of norms */
	private NormativeNetwork normativeNetwork;
	
	/**
	 * Constructor of the strategy
	 * 
	 * @param nsm the norm synthesis machine
	 */
	public MySecondStrategy(es.csic.iiia.nsm.NormSynthesisMachine nsm) {
		this.normativeNetwork = nsm.getNormativeNetwork();
		
		this.createNormativeSystem(); // Create a default normative system
	}
	
	@Override
  public NormativeSystem execute() {
		return normativeNetwork.getNormativeSystem();
  }

	/**
	 * Creates a normative system to give way to the cars on the left
	 */
	private void createNormativeSystem() {
		/* Create norm preconditions */
		SetOfPredicatesWithTerms n1Precondition = new SetOfPredicatesWithTerms();
		n1Precondition.add("l", ">");
		n1Precondition.add("f", "*");
		n1Precondition.add("r", "*");
		
		/* Create norms */
		Norm n1 = new Norm(n1Precondition, NormModality.Prohibition, CarAction.Go);

		/* Add the norms to the normative network and activate them */
		this.normativeNetwork.add(n1);
		normativeNetwork.setState(n1, NetworkNodeState.ACTIVE);
	}
}

