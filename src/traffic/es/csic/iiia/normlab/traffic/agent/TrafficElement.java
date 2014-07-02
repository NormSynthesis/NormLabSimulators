package es.csic.iiia.normlab.traffic.agent;

import repast.simphony.annotate.AgentAnnot;

/**
 * 
 * @author javi
 *
 */
@AgentAnnot(displayName = "Traffic Element")
public interface TrafficElement
{
	/**
	 * 
	 */
	public void move();
	
	/**
	 * 
	 * @return
	 */
	public int getX();
	
	/**
	 * 
	 * @return
	 */
	public int getY();
	
}
