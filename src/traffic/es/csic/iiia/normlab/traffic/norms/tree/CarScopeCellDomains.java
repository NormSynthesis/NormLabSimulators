package es.csic.iiia.normlab.traffic.norms.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Global domain is
 * 
 * < (Car heading west)
 * > (Car heading east)
 * ^ (Car heading north)
 * v (Car heading south)
 * e (Empty cell)
 * w (Wall)
 * c (Collision) (Only with collisions more than 1 tick) 
 * 
 * @author Javier Morales (jmoralesmat@gmail.com)
 *
 */
public class CarScopeCellDomains {

	/**
	 * 
	 */
	private static Map<String, List<String>> values;

	/**
	 * Returns a list with the domains of each cell in the SimpleCarScope
	 */
	static {

		values = new HashMap<String, List<String>>();
		
		//-------------------------------
		// Domain for left cell (< > v -)
		//-------------------------------
		List<String> cellDomain = new ArrayList<String>();
		values.put("0-0", cellDomain);
		
		cellDomain.add("<");
		cellDomain.add(">");
		cellDomain.add("-");
		
		//---------------------------------
		// Domain for center cell (< > ^ -) 
		//---------------------------------
		cellDomain = new ArrayList<String>();
		values.put("0-1", cellDomain);
		
		cellDomain.add("<");
		cellDomain.add(">");
		cellDomain.add("^");
		cellDomain.add("-");
		
		//--------------------------------
		// Domain for right cell (< > | -)
		//--------------------------------
		cellDomain = new ArrayList<String>();
		values.put("0-2", cellDomain);
		
		cellDomain.add("<");
		cellDomain.add(">");
		cellDomain.add("|");
		cellDomain.add("-");
	}

	/**
	 * 
	 */
	public static List<String> getDomain(int row, int col) {
		return values.get(row + "-" + col);
	}
}
