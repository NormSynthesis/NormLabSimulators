package es.csic.iiia.normlab.traffic.apps;

import java.util.ArrayList;
import java.util.List;

import es.csic.iiia.normlab.traffic.agent.TrafficElement;

/**
 * 
 * @author "Javier Morales (jmorales@iiia.csic.es)"
 *
 */
public class Collision implements TrafficElement
{
	/**
	 * 
	 */
	private int x, y;
	
	/**
	 * 
	 */
	private List<TrafficElement> elements;
	
	/**
	 * 
	 * @param car1
	 * @param car2
	 */
	public Collision(int x, int y)	{
		this.elements = new ArrayList<TrafficElement>();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<TrafficElement> getElements()
	{
		return this.elements;
	}
	/**
	 * 
	 */
	@Override
  public int getX() {
	  return this.x;
  }

	/**
	 * 
	 */
	@Override
  public int getY() {
	  return this.y;
  }


	@Override
  public void move() {}
	
	public String toString() { 
		return "X";
	}
}
