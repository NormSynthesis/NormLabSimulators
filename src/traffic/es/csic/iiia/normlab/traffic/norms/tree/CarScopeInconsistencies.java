package es.csic.iiia.normlab.traffic.norms.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Javier Morales (jmoralesmat@gmail.com)
 *
 */
public class CarScopeInconsistencies 
{
	/**
	 * 
	 */
	private static Map<Integer, List<String>> inconsistencies;
	
	/**
	 * 
	 */
	private static int numInconsistencies;
	
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
		inconsistency.add("|");		
		
		// Inconsistency 7 (< |)
		//----------------------
		inconsistency = new ArrayList<String>();
		inconsistencies.put(++numInconsistencies, inconsistency);
		
		inconsistency.add("<");
		inconsistency.add("|");		
	}

	/**
	 * 
	 */
  public static Map<Integer, List<String>> getAll() {
	  return inconsistencies;
  }
}
