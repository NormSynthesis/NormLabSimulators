package es.csic.iiia.normlab.traffic.car.context;

import java.util.ArrayList;
import java.util.List;

import es.csic.iiia.normlab.traffic.factory.TrafficFactFactory;
import es.csic.iiia.normlab.traffic.map.TrafficMatrix;
import es.csic.iiia.nsm.agent.EnvironmentAgentContext;
import es.csic.iiia.nsm.agent.language.SetOfPredicatesWithTerms;

/**
 *  The scope of a car. This is the part of the model visible by a car
 * 
 * @author Javier Morales (jmoralesmat@gmail.com)
 *
 */
public class CarContext extends TrafficMatrix implements EnvironmentAgentContext {

	/**
	 * 
	 * @author Javier Morales (jmoralesmat@gmail.com)
	 *
	 */
	public enum Type {
		Left,
		Front,
		Right
	}

	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------

	public static int NUM_ROWS = 1;
	public static int NUM_COLS = 3;
	protected Type type;
	
	private List<Long> perceivedAgentsIds;
	private SetOfPredicatesWithTerms description;

	//---------------------------------------------------------------------------
	// Methods
	//---------------------------------------------------------------------------

	/**
	 * 
	 */
	public CarContext(Type type) {
		super(NUM_ROWS, NUM_COLS);
		this.type = type;
	}

	/**
	 * 
	 * @param value
	 */
	public void set(int row, int col, String codDesc) {
		super.set(row, col,codDesc);
	}
	
	/**
	 * 
	 */
	public void pack(TrafficFactFactory factFactory) {
		
		/* Generate description in the form of predicates and terms */
		this.description = factFactory.generatePredicates(this);
		
		/* Get perceived agents identifiers */
		this.perceivedAgentsIds = new ArrayList<Long>();
		
		for(int i=0; i<this.getNumElements(); i++) {
			String cell = this.get(i);
			perceivedAgentsIds.addAll(TrafficStateManager.getCarIds(cell));
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * 
	 */
	public boolean equals(EnvironmentAgentContext otherContext) {
		CarContext context = (CarContext) otherContext;
		return this.getDistance(context) == 0;
	}

	/**
	 * 
	 */
	public String toString() {
		return "|" + super.toString() + "|";
	}

	/**
	 * 
	 */
	@Override
  public SetOfPredicatesWithTerms getDescription() {
	  return this.description;
  }

	/**
	 * 
	 */
	@Override
  public List<Long> getPerceivedAgentsIds() {
	  return this.perceivedAgentsIds;
  }
}
