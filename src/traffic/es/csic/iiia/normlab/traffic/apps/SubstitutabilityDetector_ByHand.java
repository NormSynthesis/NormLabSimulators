package es.csic.iiia.normlab.traffic.apps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.csic.iiia.normlab.traffic.car.CarAction;
import es.csic.iiia.nsm.agent.language.SetOfPredicatesWithTerms;
import es.csic.iiia.nsm.norm.Norm;
import es.csic.iiia.nsm.norm.NormModality;
import es.csic.iiia.nsm.norm.NormativeSystem;

/**
 * 
 * @author "Javier Morales (jmorales@iiia.csic.es)"
 *
 */
public class SubstitutabilityDetector_ByHand {
	private HashMap<Norm,List<Norm>> subsRelationships;

	/**
	 * 
	 */
	public SubstitutabilityDetector_ByHand() {
		this.subsRelationships = new HashMap<Norm,List<Norm>>();
		this.createListOfSubstitutableNorms();
	}

	/**
	 * 
	 * @param ns
	 * @return
	 */
	public List<Norm> getSubstitutableNorms(NormativeSystem ns) {
		List<Norm> substitutable = new ArrayList<Norm>();

		for(Norm nA : this.subsRelationships.keySet()) {
			for(Norm nB: this.subsRelationships.get(nA)) {
				if(this.contains(ns,nA) && this.contains(ns,nB)){
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
	 */
	private void createListOfSubstitutableNorms() {
		SetOfPredicatesWithTerms precond = new SetOfPredicatesWithTerms();
		precond.add("l", ">");
		precond.add("f", "-");
		precond.add("r", "-");
		Norm n1 = new Norm(precond, NormModality.Prohibition, CarAction.Go);
		n1.setId(1);

		precond = new SetOfPredicatesWithTerms();
		precond.add("l", "-");
		precond.add("f", "-");
		precond.add("r", "<");
		Norm n2 = new Norm(precond, NormModality.Prohibition, CarAction.Go);
		n2.setId(2);

		precond = new SetOfPredicatesWithTerms();
		precond.add("l", "<");
		precond.add("f", "-");
		precond.add("r", "<");
		Norm n3 = new Norm(precond, NormModality.Prohibition, CarAction.Go);
		n3.setId(3);

		precond = new SetOfPredicatesWithTerms();
		precond.add("l", ">");
		precond.add("f", "-");
		precond.add("r", ">");
		Norm n4 = new Norm(precond, NormModality.Prohibition, CarAction.Go);
		n4.setId(4);

		precond = new SetOfPredicatesWithTerms();
		precond.add("l", ">");
		precond.add("f", "^");
		precond.add("r", "-");
		Norm n5 = new Norm(precond, NormModality.Prohibition, CarAction.Go);
		n5.setId(5);

		precond = new SetOfPredicatesWithTerms();
		precond.add("l", "-");
		precond.add("f", "<");
		precond.add("r", "<");
		Norm n6 = new Norm(precond, NormModality.Prohibition, CarAction.Go);
		n6.setId(6);

		precond = new SetOfPredicatesWithTerms();
		precond.add("l", "-");
		precond.add("f", "^");
		precond.add("r", "<");
		Norm n7 = new Norm(precond, NormModality.Prohibition, CarAction.Go);
		n7.setId(7);

		precond = new SetOfPredicatesWithTerms();
		precond.add("l", ">");
		precond.add("f", ">");
		precond.add("r", "-");
		Norm n8 = new Norm(precond, NormModality.Prohibition, CarAction.Go);
		n8.setId(8);

		precond = new SetOfPredicatesWithTerms();
		precond.add("l", ">");
		precond.add("f", "^");
		precond.add("r", ">");
		Norm n9 = new Norm(precond, NormModality.Prohibition, CarAction.Go);
		n9.setId(9);
		
		precond = new SetOfPredicatesWithTerms();
		precond.add("l", "<");
		precond.add("f", "^");
		precond.add("r", "<");
		Norm n10 = new Norm(precond, NormModality.Prohibition, CarAction.Go);
		n10.setId(10);
		
		this.addSubstitutability(n1, n2);		// >-- | --<
		this.addSubstitutability(n1, n3);		// >-- | <-<
		this.addSubstitutability(n4, n2);		// >-> | --<
		this.addSubstitutability(n4, n3);		// >-> | <-<
		this.addSubstitutability(n5, n6);		// >^- | -<<
		this.addSubstitutability(n7, n8);		// -^< | >>-
		this.addSubstitutability(n9, n6);		// >^> | -<<
		this.addSubstitutability(n10, n8);	// <^< | >>-
		
		System.out.println(this.subsRelationships);
	}

	/**
	 * 
	 * @param n1
	 * @param n2
	 */
	private void addSubstitutability(Norm n1, Norm n2) {
		Norm nA, nB;

		if(n1.getId() < n2.getId()) {
			nA = n1;
			nB = n2;
		}
		else {
			nA = n2;
			nB = n1;
		}

		if(!this.subsRelationships.containsKey(nA)) {
			this.subsRelationships.put(nA, new ArrayList<Norm>());
		}
		this.subsRelationships.get(nA).add(nB);
	}

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
