package es.csic.iiia.normlab.traffic.factory;

import es.csic.iiia.normlab.traffic.car.context.CarContext;
import es.csic.iiia.normlab.traffic.car.context.TrafficStateManager;
import es.csic.iiia.normlab.traffic.car.context.TrafficStateManager.StateType;
import es.csic.iiia.nsm.agent.language.PredicatesDomains;
import es.csic.iiia.nsm.agent.language.SetOfPredicatesWithTerms;
import es.csic.iiia.nsm.norm.reasoning.JessFactsGenerator;

/**
 * Facts generator tool. Generates facts for the car reasoner and to build the
 * condition (left part) of a norm. It adapts the facts to the format of the car
 * reasoner or the norm condition, in base of the FactType passed by parameter  
 * 
 * @author Javier Morales (jmoralesmat@gmail.com)
 *
 */
public class TrafficFactFactory extends JessFactsGenerator {

	/**
	 * Defines the type of a fact. A fact for a CarReasoner is used to know what is the current
	 * state of the world. A fact for a norm is used to define a situation that fires a norm
	 *   
	 * @author Javier Morales (jmoralesmat@gmail.com)
	 *
	 */
	public enum FactType {
		CarReasoner, Norm
	}
	
	/**
	 * 
	 * @param predicatesDomains
	 */
	public TrafficFactFactory(PredicatesDomains predicatesDomains) {
		super(predicatesDomains);
	}

	/**
	 * Generates a string containing a set of facts
	 * 
	 * @param factType
	 * @param scope
	 * @return
	 */
	public SetOfPredicatesWithTerms generatePredicates(CarContext context) {
		SetOfPredicatesWithTerms predicatesWithTerms = new SetOfPredicatesWithTerms();
		int dimX = context.getNumCols();
		int dimY = context.getNumRows();

		// Add new facts for those cars that are not collided
		for(int row=0; row<dimY; row++) { 
			for(int col=0; col<dimX; col++) {
				String codDesc = context.get(row, col);
				StateType type = TrafficStateManager.getType(codDesc);
				String predicate = getPosition(col, dimX);
				String term = "";

				// Wall
				switch(type) {
				case Wall:				term = "w";		break;
				case Nothing:			term = "-";		break;
				case Collision:		term = "c";		break;
				case Car:					term = TrafficStateManager.getCarHeading(codDesc).getArrow();	break;
				default: 					break;
				}
				
				predicatesWithTerms.add(predicate, term);
			}
		}
		return predicatesWithTerms;
	}	

	//--------------------------------------------------------------------------------
	// Utilities
	//--------------------------------------------------------------------------------

	/**
	 * Returns the position of a car relative to us
	 * 
	 * @param x
	 * @param dimX
	 * @return
	 */
	private String getPosition(int x, int dimX) {
		if(x < dimX/2)			return "l";
		else if(x > dimX/2)	return "r";
		else								return "f";
	}
}
