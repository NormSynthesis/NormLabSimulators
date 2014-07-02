package es.csic.iiia.normlab.traffic.norms.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author "Javier Morales (jmorales@iiia.csic.es)"
 *
 */
public class TrafficPredicateInconsistencies {

	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------
	
	private static Map<Integer, List<String>> inconsistencies;
	private static int numInconsistencies;

	//---------------------------------------------------------------------------
	// Methods
	//---------------------------------------------------------------------------
	
	/**
	 * Creates inconsistencies for SimpleCarScope
	 */
	static  
	{
		inconsistencies = new HashMap<Integer, List<String>>();
		numInconsistencies = 0;

		// Inconsistency 1 (< >)
		//----------------------
		List<String> inconsistency = new ArrayList<String>();
		inconsistencies.put(++numInconsistencies, inconsistency);

		inconsistency.add("<");
		inconsistency.add(">");

		// Inconsistency 2 (> <)
		//----------------------
		inconsistency = new ArrayList<String>();
		inconsistencies.put(++numInconsistencies, inconsistency);

		inconsistency.add(">");
		inconsistency.add("<");		

		// Inconsistency 3 (^ ^)
		//----------------------
		inconsistency = new ArrayList<String>();
		inconsistencies.put(++numInconsistencies, inconsistency);

		inconsistency.add("^");
		inconsistency.add("^");		

		// Inconsistency 4 (v v)
		//----------------------
		inconsistency = new ArrayList<String>();
		inconsistencies.put(++numInconsistencies, inconsistency);

		inconsistency.add("v");
		inconsistency.add("v");		

		// Inconsistency 5 (^ v)
		//----------------------
		inconsistency = new ArrayList<String>();
		inconsistencies.put(++numInconsistencies, inconsistency);

		inconsistency.add("^");
		inconsistency.add("v");		

		// Inconsistency 6 (> |)
		//----------------------
		inconsistency = new ArrayList<String>();
		inconsistencies.put(++numInconsistencies, inconsistency);

		inconsistency.add(">");
		inconsistency.add("w");		

		// Inconsistency 7 (< |)
		//----------------------
		inconsistency = new ArrayList<String>();
		inconsistencies.put(++numInconsistencies, inconsistency);

		inconsistency.add("<");
		inconsistency.add("w");		

		// Inconsistency 8 (w <)
		//----------------------
		inconsistency = new ArrayList<String>();
		inconsistencies.put(++numInconsistencies, inconsistency);

		inconsistency.add("w");
		inconsistency.add("<");

		// Inconsistency 9 (w ^)
		//----------------------
		inconsistency = new ArrayList<String>();
		inconsistencies.put(++numInconsistencies, inconsistency);

		inconsistency.add("w");
		inconsistency.add("^");

		// Inconsistency 10 (w >)
		//----------------------
		inconsistency = new ArrayList<String>();
		inconsistencies.put(++numInconsistencies, inconsistency);

		inconsistency.add("w");
		inconsistency.add("v");

		// Inconsistency 11 (w -)
		//----------------------
		inconsistency = new ArrayList<String>();
		inconsistencies.put(++numInconsistencies, inconsistency);

		inconsistency.add("w");
		inconsistency.add("-");

		// Inconsistency 12 (v ^)
		//----------------------
		inconsistency = new ArrayList<String>();
		inconsistencies.put(++numInconsistencies, inconsistency);

		inconsistency.add("v");
		inconsistency.add("^");
		
		// Inconsistency 13 (v -)
		//----------------------
		inconsistency = new ArrayList<String>();
		inconsistencies.put(++numInconsistencies, inconsistency);

		inconsistency.add("v");
		inconsistency.add("-");
	}

	/**
	 * 
	 */
	public static Map<Integer, List<String>> getAll() {
		return inconsistencies;
	}
}
