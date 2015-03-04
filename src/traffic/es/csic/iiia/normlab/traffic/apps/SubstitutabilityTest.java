package es.csic.iiia.normlab.traffic.apps;

import java.util.List;

import es.csic.iiia.normlab.traffic.factory.CarContextFactory;
import es.csic.iiia.normlab.traffic.factory.TrafficFactFactory;
import es.csic.iiia.normlab.traffic.normsynthesis.TrafficNormSynthesisSettings;
import es.csic.iiia.nsm.agent.language.PredicatesDomains;
import es.csic.iiia.nsm.agent.language.TaxonomyOfTerms;
import es.csic.iiia.nsm.norm.Norm;

/**
 * 
 * @author "Javier Morales (jmorales@iiia.csic.es)"
 *
 */
public class SubstitutabilityTest {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		PredicatesDomains predDomains = createPredicatesDomains();
		TrafficFactFactory factFactory = new TrafficFactFactory(predDomains);
		CarContextFactory carContextFactory = new CarContextFactory(factFactory);
		
		NSsFileManager nsFileManager = new NSsFileManager(predDomains, carContextFactory);
		SubstitutabilityDetector subsDetector = new SubstitutabilityDetector(predDomains,
				carContextFactory, factFactory);
		
		String drSimonNSFilename = TrafficNormSynthesisSettings.SIM_DATA_PATH + 
				"/LION" + TrafficNormSynthesisSettings.SIM_FINAL_NORMSETS_FILE;
		String simonNSFilename = TrafficNormSynthesisSettings.SIM_DATA_PATH + 
				"/SIMON" + TrafficNormSynthesisSettings.SIM_FINAL_NORMSETS_FILE;
		
		List<NormativeSystem> drSimonNSs = nsFileManager.load(drSimonNSFilename);
		List<NormativeSystem> simonNSs = nsFileManager.load(simonNSFilename);
		
//		System.out.println("NSId Matches SubsNorms Represented");
//		for(NormativeSystem drSimonNS : drSimonNSs) {
//			List<Norm> drSimonSubsNorms = subsDetector.getSubstitutableNorms(drSimonNS);
//			int drSimonNumSubs = drSimonSubsNorms.size();
//			int lionMatches = drSimonNS.getMatches();
//			int numRepresented = drSimonNS.size();
//			
//			System.out.println("NS" + drSimonNS.getId() + " " + lionMatches + " " +
//			drSimonNumSubs + " " + numRepresented);
//		}
//		
//		System.out.println("NSId Matches SubsNorms Represented");
//		for(NormativeSystem simonNS : simonNSs) {
//			List<Norm> simonSubsNorms = subsDetector.getSubstitutableNorms(simonNS);
//			int simonNumSubs = simonSubsNorms.size();
//			int simonMatches = simonNS.getMatches();
//			int numRepresented = simonNS.size();
//
//			System.out.println("NS" + simonNS.getId() + " " + simonMatches + " " +
//					simonNumSubs + " " + numRepresented);
//		}
		
		System.out.println("LIONNSId Matches Minimality Simplicity Liberality Substitutability Eff Nec "
				+ "SIMONNSId Matches Minimality Simplicity Liberality Substitutability Eff Nec ");
		
		for(NormativeSystem drSimonNS : drSimonNSs) {
			List<Norm> drSimonSubsNorms = subsDetector.getSubstitutableNorms(drSimonNS);
			int drSimonNumSubs = drSimonSubsNorms.size();
			int lionMatches = drSimonNS.getMatches();
			float lionEff = drSimonNS.getEffectiveness();
			float lionNec = drSimonNS.getNecessity();
			int lionNumGenNorms = drSimonNS.getNumGenNorms();
			int lionNumRepresented = drSimonNS.size();
			int lionSimplicity = drSimonNS.getSimplicity();
			
			// TODO: Subs norms que ahora cuente numero de RELACIONES 
			// de substitutabilidad, en lugar de numero de normas que tienen relaciones
			// de substitutabilidad
			
			for(NormativeSystem simonNS : simonNSs) {
				List<Norm> simonSubsNorms = subsDetector.getSubstitutableNorms(simonNS);
				int simonNumSubs = simonSubsNorms.size();
				int simonMatches = simonNS.getMatches();
				float simonEff = simonNS.getEffectiveness();
				float simonNec = simonNS.getNecessity();
				int simonNumGenNorms = simonNS.getNumGenNorms();
				int simonNumRepresented = simonNS.size();
				int simonSimplicity = simonNS.getSimplicity();
				
				float subsSavings = 100 * (simonNumSubs-drSimonNumSubs) / simonNumSubs;
				float effSavings = 100 * (lionEff-simonEff) / lionEff;
				float necSavings = 100 * (lionNec-simonNec) / lionNec;

				if(!drSimonNS.isSubsetOf(simonNS) && simonNS.getId() == 5) {
					System.out.println(
							
							"NS" + drSimonNS.getId() + " " +
							lionMatches + " " +
							lionNumGenNorms + " " + 
							lionSimplicity + " " +
							lionNumRepresented + " " +
							drSimonNumSubs + " " + 
							lionEff + " " +
							lionNec + 
							
							" NS" + simonNS.getId() + " " + 
							simonMatches + " " +
							simonNumGenNorms + " " +
							simonSimplicity + " " +
							simonNumRepresented + " " + 
							simonNumSubs + " " +
							simonEff + " " +
							simonNec + " " +
							
							subsSavings + " " + effSavings + " " + necSavings );
				}
			}
		}
	}
	
	/**
	 * Creates the agent language. That is, the predicates, and their domains 
	 */
	private static PredicatesDomains createPredicatesDomains() {
		PredicatesDomains predDomains;
		
		/* Predicate "left" domain */
		TaxonomyOfTerms leftPredTaxonomy = new TaxonomyOfTerms("l");
		leftPredTaxonomy.addTerm("*");
		leftPredTaxonomy.addTerm("<");
		leftPredTaxonomy.addTerm(">");
		leftPredTaxonomy.addTerm("-");
		leftPredTaxonomy.addRelationship("<", "*");
		leftPredTaxonomy.addRelationship(">", "*");
		leftPredTaxonomy.addRelationship("-", "*");

		/* Predicate "front" domain*/
		TaxonomyOfTerms frontPredTaxonomy = new TaxonomyOfTerms("f", leftPredTaxonomy);
		frontPredTaxonomy.addTerm("^");
		frontPredTaxonomy.addRelationship("^", "*");

		/* Predicate "right" domain*/
		TaxonomyOfTerms rightPredTaxonomy = new TaxonomyOfTerms("r", leftPredTaxonomy);
		rightPredTaxonomy.addTerm("w");
		rightPredTaxonomy.addRelationship("w", "*");

		predDomains = new PredicatesDomains();
		predDomains.addPredicateDomain("l", leftPredTaxonomy);
		predDomains.addPredicateDomain("f", frontPredTaxonomy);
		predDomains.addPredicateDomain("r", rightPredTaxonomy);
		
		return predDomains;
	}
}
