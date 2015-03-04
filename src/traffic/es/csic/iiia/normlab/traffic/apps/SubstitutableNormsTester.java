package es.csic.iiia.normlab.traffic.apps;

import es.csic.iiia.normlab.traffic.factory.CarContextFactory;
import es.csic.iiia.normlab.traffic.factory.TrafficFactFactory;
import es.csic.iiia.nsm.agent.language.PredicatesDomains;
import es.csic.iiia.nsm.agent.language.SetOfPredicatesWithTerms;
import es.csic.iiia.nsm.norm.Norm;
import es.csic.iiia.nsm.norm.evaluation.NormCompliance;

/**
 * 
 * @author "Javier Morales (jmorales@iiia.csic.es)"
 *
 */
public class SubstitutableNormsTester {

	//---------------------------------------------------------------------------
	// Attributes 
	//---------------------------------------------------------------------------
	
	private SubstitutabilityMap map;
	private PredicatesDomains predDomains;
	
	//---------------------------------------------------------------------------
	// Methods 
	//---------------------------------------------------------------------------
	
	/**
	 * 
	 */
	public SubstitutableNormsTester(PredicatesDomains predDomains, 
			CarContextFactory carContextFactory,
			TrafficFactFactory factFactory) {
		
		this.map = new SubstitutabilityMap(3, 3, predDomains,
				carContextFactory, factFactory);
	}
	
	/**
	 * 
	 * @param nA
	 * @param nB
	 * @return
	 */
	public boolean areSubstitutable(Norm nA, Norm nB, NormativeSystem ns) {
		SetOfPredicatesWithTerms nAPrecond = nA.getPrecondition();
		SetOfPredicatesWithTerms nBPrecond = nB.getPrecondition();
		Norm leftToRightNorm = null;
		Norm bottomToTopNorm = null;
		boolean FFConflict = false;
		boolean FIConflict = false;
		boolean IFConflict = false;
		boolean IIConflict = false;
		
		if(nAPrecond.toString().charAt(2) == '>') {
			leftToRightNorm = nA;
		}
		if(nAPrecond.toString().charAt(12) == '<') {
			bottomToTopNorm = nA;
		}
		
		if(nBPrecond.toString().charAt(2) == '>') {
			leftToRightNorm = nB;
		}
		if(nBPrecond.toString().charAt(12) == '<') {
			bottomToTopNorm = nB;
		}
		
		if(leftToRightNorm == null || bottomToTopNorm == null) {
			return false;
		}
		
		try {
			FFConflict = this.leadsToConflict(leftToRightNorm, NormCompliance.FULFILMENT,
					bottomToTopNorm, NormCompliance.FULFILMENT, ns);
			FIConflict = this.leadsToConflict(leftToRightNorm, NormCompliance.FULFILMENT,
					bottomToTopNorm, NormCompliance.INFRINGEMENT, ns);
			IFConflict = this.leadsToConflict(leftToRightNorm, NormCompliance.INFRINGEMENT,
					bottomToTopNorm, NormCompliance.FULFILMENT, ns);
			IIConflict = this.leadsToConflict(leftToRightNorm, NormCompliance.INFRINGEMENT,
					bottomToTopNorm, NormCompliance.INFRINGEMENT, ns);
		} 
		catch (Exception e) {
			return false;
		}
		
		return !FFConflict && !FIConflict && !IFConflict && IIConflict;			 
	}

	/**
	 * 
	 * @param nACompliance
	 * @param nBCompliance
	 * @return
	 */
	private boolean leadsToConflict(Norm leftToRightNorm, NormCompliance leftCompliance,
			Norm bottomToTopNorm, NormCompliance bottomCompliance,
			NormativeSystem ns) throws Exception {
		
		this.map.clear();
		this.map.addNorms(leftToRightNorm, bottomToTopNorm, ns);
		
		/* */
		if(!this.map.isConsistent()) {
			throw new Exception("Inconsistent map");
		}		
		
		this.map.moveCars(leftCompliance, bottomCompliance);
		return this.map.hasCollisions();		
	}

}
