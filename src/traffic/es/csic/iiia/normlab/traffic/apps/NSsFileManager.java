package es.csic.iiia.normlab.traffic.apps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.csic.iiia.normlab.traffic.car.CarAction;
import es.csic.iiia.normlab.traffic.factory.CarContextFactory;
import es.csic.iiia.normlab.traffic.normsynthesis.TrafficDomainFunctions;
import es.csic.iiia.normlab.traffic.utils.StringList;
import es.csic.iiia.nsm.agent.AgentAction;
import es.csic.iiia.nsm.agent.language.PredicatesDomains;
import es.csic.iiia.nsm.agent.language.SetOfPredicatesWithTerms;
import es.csic.iiia.nsm.config.DomainFunctions;
import es.csic.iiia.nsm.norm.Norm;
import es.csic.iiia.nsm.norm.NormModality;
import es.csic.iiia.nsm.norm.refinement.iron.GeneralisationReasoner;

/**
 * Norms file manager
 * 
 * @author Javier Morales (jmoralesmat@gmail.com)
 *
 */
public class NSsFileManager {

	//-----------------------------------------------------------------
	// Attributes
	//-----------------------------------------------------------------

	private HashMap<StringList,Integer> stringNormativeSystems;
	private List<NormativeSystem> normativeSystems;
	private File normativeSystemsFile;
	
	private GeneralisationReasoner genReasoner;
	private DomainFunctions dmFunctions;
	
	private HashMap<StringList,Float> effNormSets, necNormSets;
	private HashMap<StringList,Integer> covNormSets, matchesNormSets;
	
	//-----------------------------------------------------------------
	// Methods
	//-----------------------------------------------------------------

	/**
	 * Constructor
	 */
	public NSsFileManager(PredicatesDomains predDomains,
			CarContextFactory carContextFactory) {
		
		this.dmFunctions = new TrafficDomainFunctions(predDomains, carContextFactory);
		this.genReasoner = new GeneralisationReasoner(predDomains, dmFunctions);
		
		this.covNormSets = new HashMap<StringList, Integer>();
		this.matchesNormSets = new HashMap<StringList, Integer>();
		this.effNormSets = new HashMap<StringList, Float>();
		this.necNormSets = new HashMap<StringList, Float>();
	}

	/**
	 * Loads the norms file
	 * @return 
	 */
	public List<NormativeSystem> load(String normativeSystemsFilename) {
		this.stringNormativeSystems = new HashMap<StringList, Integer>();
		this.normativeSystems = new ArrayList<NormativeSystem>();
		this.normativeSystemsFile = new File(normativeSystemsFilename);
		
		// Load files into hash maps
		return this.loadNormsFile(normativeSystemsFile, stringNormativeSystems);
	}

	/**
	 * 
	 * @return
	 */
	public List<NormativeSystem> getNormativeSystems() {
		return this.normativeSystems;
	}
	
	//--------------------------------------------------------------------------------
	// Private methods
	//--------------------------------------------------------------------------------
	
	/**
	 * Loads the norms from the norms file
	 * @return 
	 */
	private List<NormativeSystem> loadNormsFile(File file, HashMap<StringList, Integer> nsMap) {
		
		StringList normSet = null;
		String norm = null;
		String line = null;
		int numApps = 0, covNormSet = 0;
		float effNormSet = 0f, necNormSet = 0f;
		
		int id = 0;
		
		try
		{
			BufferedReader input = new BufferedReader(new FileReader(file));
			while ((line = input.readLine()) != null)
			{
				switch(line.charAt(0)) {

				// Indicates new norm set
				case 'S':
					id++;
					normSet = new StringList();
					break;

				case 'N':
					norm = input.readLine();
					normSet.add(norm);
					break;
					
				// Line of the number of appearances 
				case 'A':
					numApps = Integer.valueOf(input.readLine());
					matchesNormSets.put(normSet, numApps);
					stringNormativeSystems.put(normSet, id);
					break;
					
				case 'F':
					effNormSet = Float.valueOf(input.readLine());
					effNormSets.put(normSet, effNormSet);
					necNormSet = Float.valueOf(input.readLine());
					necNormSets.put(normSet, necNormSet);
					covNormSet = Integer.valueOf(input.readLine());
					covNormSets.put(normSet, covNormSet);
					break;	
				}
			}
			input.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		/* Generate REAL normative systems */
		return this.generateNormativeSystems();
	}
	
	/**
	 * 
	 * @return 
	 * @return
	 */
	private List<NormativeSystem> generateNormativeSystems() {
		
		// For each normative system
		for(StringList list : this.stringNormativeSystems.keySet()) {
			int id = stringNormativeSystems.get(list);
			NormativeSystem ns = new NormativeSystem(id);
						
			int normId = 0;
			
			// For each norm in the normative system
			for(String nDesc : list) {
				
				String leftPred = String.valueOf(nDesc.charAt(3));
				String fronPred = String.valueOf(nDesc.charAt(8));
				String rightPred = String.valueOf(nDesc.charAt(13));
				SetOfPredicatesWithTerms precond = new SetOfPredicatesWithTerms();
				
				precond.add("l", leftPred);
				precond.add("f", fronPred);
				precond.add("r", rightPred);
				
				Norm norm = new Norm(precond, NormModality.Prohibition, CarAction.Go);
				norm.setId(++normId);
				ns.add(norm);
			}
			
			int simplicity = this.getSimplicity(ns);
			NormativeSystem representedNS = this.getRepresentedNormativeSystem(ns);
			
			normId = 0;
			for(Norm norm : representedNS) {
				norm.setId(++normId);
			}
			
			representedNS.setSimplicity(simplicity);
			representedNS.setNumGenNorms(list.size());
			representedNS.setMatches(this.matchesNormSets.get(list));
			representedNS.setEffectiveness(this.effNormSets.get(list));
			representedNS.setNecessity(this.necNormSets.get(list));
			
			this.normativeSystems.add(representedNS);
		}
		return this.normativeSystems;
	}

	/**
	 * 
	 * @param ns
	 * @return
	 */
  private int getSimplicity(NormativeSystem ns) {
  	int numSpecPreds = 0;
	  for(Norm norm : ns) {
	  	SetOfPredicatesWithTerms precond = norm.getPrecondition();
	  	for(String pred : precond.getPredicates()) {
	  		for(String term : precond.getTerms(pred)) {
	  			if(!term.equals("*")) { 
	  				numSpecPreds++;
	  			}
	  		}
	  	}
	  }
	  return numSpecPreds;
  }

	/**
   * 
   * @return
   */
  private NormativeSystem getRepresentedNormativeSystem(NormativeSystem ns) {
  	NormativeSystem representedNS = new NormativeSystem(ns.getId());
  	for(Norm norm : ns) {
  		this.computeRepresentedNorms(representedNS, norm);
  	}
  	return representedNS; 
  }
  
  /**
   * 
   * @param norm
   * @return
   */
  private void computeRepresentedNorms(List<Norm> representedNorms, Norm norm) {
  	NormModality mod = norm.getModality();
		AgentAction action = norm.getAction();
		List<SetOfPredicatesWithTerms> chPreconds = this.genReasoner.
				getChildContexts(norm.getPrecondition());
		
		if(!norm.getPrecondition().toString().contains("*")) {
			boolean exists = false;
			for(Norm represented : representedNorms) {
				if(represented.equals(norm)) {
					exists = true;
				}
			}
			if(!exists) {
				representedNorms.add(norm);
				return;
			}
		}
		else {
			List<Norm> children = new ArrayList<Norm>();
			for(SetOfPredicatesWithTerms chPrecond : chPreconds) {
				Norm chNorm = new Norm(chPrecond, mod, action);
				children.add(chNorm);
			}
			
			for(Norm child : children) {
				this.computeRepresentedNorms(representedNorms, child);
			}
		}
  }
}
