package es.csic.iiia.normlab.traffic.apps;

import java.util.ArrayList;
import java.util.List;

import es.csic.iiia.normlab.traffic.factory.CarContextFactory;
import es.csic.iiia.normlab.traffic.factory.TrafficFactFactory;
import es.csic.iiia.nsm.agent.language.PredicatesDomains;
import es.csic.iiia.nsm.norm.Norm;

/**
 * 
 * @author "Javier Morales (jmorales@iiia.csic.es)"
 *
 */
public class SubstitutabilityDetector {

	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------

	private SubstitutableNormsTester subsTester;

	//---------------------------------------------------------------------------
	// Public methods
	//---------------------------------------------------------------------------

	/**
	 * 
	 */
	public SubstitutabilityDetector(PredicatesDomains predDomains, 
			CarContextFactory carContextFactory,
			TrafficFactFactory factFactory) {
		
		this.subsTester = new SubstitutableNormsTester(predDomains, carContextFactory,
				factFactory);
	}

	/**
	 * 
	 * @param ns
	 * @return
	 */
	public List<Norm> getSubstitutableNorms(NormativeSystem ns) {
		List<Norm> substitutable = new ArrayList<Norm>();

		for(int i=0; i<ns.size(); i++) {
			Norm nA = ns.get(i);
			for(int j=i+1; j<ns.size(); j++) {
				Norm nB = ns.get(j);

				// Check substitutability
				if(this.subsTester.areSubstitutable(nA, nB, ns)) {
					if(!this.contains(substitutable, nA)) {
						substitutable.add(nA);	
					}
					if(!this.contains(substitutable, nB)) {
						substitutable.add(nB);
					}	
				}
			}
		}
		return substitutable;
	}

	//-------------------------------------------------------------------------
	// Private methods
	//-------------------------------------------------------------------------

	/**
	 * 
	 * @param ns
	 * @param nA
	 * @return
	 */
	private boolean contains(List<Norm> ns, Norm nA) {
		for(Norm nsNorm : ns) {
			if(nsNorm.equals(nA)) {
				return true;
			}
		}
		return false;
	}
}
