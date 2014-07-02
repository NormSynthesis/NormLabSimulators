package es.csic.iiia.normlab.traffic.agent.monitor;

import java.util.ArrayList;
import java.util.List;

import es.csic.iiia.normlab.traffic.agent.Car;
import es.csic.iiia.normlab.traffic.agent.Collision;
import es.csic.iiia.normlab.traffic.agent.TrafficElement;
import es.csic.iiia.normlab.traffic.map.CarMap;
import es.csic.iiia.normlab.traffic.map.TrafficMatrix;
import es.csic.iiia.nsm.norm.Norm;
import es.csic.iiia.nsm.perception.View;

/**
 * Traffic view. A subset of a traffic state.
 * 
 * @author Javier Morales (jmoralesmat@gmail.com)
 *
 */
public class TrafficView extends TrafficMatrix implements View {

	//-----------------------------------------------------------------
	// Attributes
	//-----------------------------------------------------------------
	
	/**
	 * Agent IDs
	 */
	private List<Long> agentIds;
	
	/**
	 * Norm applicability for agents
	 */
//	private NormApplicability normApplicability;
	
	/**
	 * Traffic view dimensions
	 */
	private int startRow, stopRow, startCol, stopCol;
	
	//-----------------------------------------------------------------
	// Methods
	//-----------------------------------------------------------------

	/**
	 * Constructor
	 * 
	 * @param dimX
	 * @param dimY
	 * @param name
	 */
	public TrafficView(int startRow, int stopRow, int startCol, int stopCol)
	{
		super((stopRow - startRow + 1), (stopCol - startCol + 1));
		this.startRow = startRow;
		this.stopRow = stopRow;
		this.startCol = startCol;
		this.stopCol = stopCol;
		
		this.agentIds = new ArrayList<Long>();
//		this.normApplicability = new NormApplicability();
	}

	/**
	 * Clears the traffic view
	 */
	private void clearView()
	{
		this.clear();
		this.agentIds.clear();
//		this.normApplicability.clear();
	}
	
	/**
	 * Updates the traffic view, copying its corresponding area from the car map
	 * 
	 * @param carMap
	 * @param car
	 * @return
	 */
	public void update(CarMap carMap)
	{
		this.clear();
		
		// Clear previous information and get sub matrix from car map
		this.clearView();		
		this.set(carMap.getSubMatrix(startRow, stopRow+1, startCol, stopCol+1));
		
		// Add agent ID's and norm applicability
		for(int row=startRow; row<=stopRow; row++)
		{
			for(int col=startCol; col<=stopCol; col++)
			{
  			TrafficElement elem = carMap.getElement(row, col);
  			
  			if(elem!=null)
  			{//  				
  				// Add agent id's and norm applicability
  				this.addAgentIds(elem);
  				this.addNormApplicability(elem);
  			}
			}
		}
	}
	
  /**
   * 
   * @param otherDesc
   */
  public void copy(TrafficView tView)
	{
		List<Long> ids = new ArrayList<Long>();
		ids.addAll(tView.getAgentIds());
		
		// Save copy to previous description
		super.copy(tView);
		this.agentIds = ids;
//		this.normApplicability = tView.getNormApplicability().clone();
	}
	
	/**
	 * 
	 * @return
	 */
//	public NormApplicability getNormApplicability()
//	{
//		return this.normApplicability;
//	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public List<Long> getAgentIds()
	{
		return this.agentIds;
	}

	/**
	 * 
	 */
	@Override
	public float getSimilarity(View otherView) {
		return 0;
	}
	
	//----------------------------------------------------------------------	
	// Private methods
	//----------------------------------------------------------------------
	
	/**
	 * 
	 * @param state
	 */
	private void addAgentIds(TrafficElement elem)
	{
		// Norm applicability for car
		if(elem instanceof Car)
		{
			this.agentIds.add(((Car)elem).getId());
		}
		else if(elem instanceof Collision)
		{
			Collision col = ((Collision)elem);
			for(TrafficElement e : col.getElements())
			{
				this.agentIds.add(((Car)e).getId());
			}
		}
	}
	
	/**
	 * Adds applicable norms for each agent
	 * 
	 * @param state
	 */
	private void addNormApplicability(TrafficElement elem)
	{
		// Norm applicability for car
		if(elem instanceof Car)
		{
			Car car = (Car)elem;
			long id = car.getId();
			List<Norm> appNorms = car.getReasoner().getApplicableNorms();
			
//			FactFactory.generatePredicates(car.getReasoner().)
//			NormsApplicableToPredicate nAppToPred = NormsApplicableToPredicate(predicate, appNorms);
//			this.normApplicability.add(id, nAppToPred);
		}
	}
}
