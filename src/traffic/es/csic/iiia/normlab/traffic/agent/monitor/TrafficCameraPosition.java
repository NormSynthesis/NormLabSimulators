package es.csic.iiia.normlab.traffic.agent.monitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Position of a traffic institution
 * 
 * @author Javier Morales (jmoralesmat@gmail.com)
 *
 */
public enum TrafficCameraPosition {

	/**
	 * The institution is watching the top part of the map
	 * 
	 * -----OOOO-----
	 * -----OOOO-----
	 * ------  ------
	 *               
	 * ------  ------
	 * ------  ------
	 * ------  ------
	 */
	Top,

	/**
	 * The institution is watching the bottom part of the map
	 * 
	 * ------  ------
	 * ------  ------
	 * ------  ------
	 *               
	 * ------  ------
	 * -----OOOO-----
	 * -----OOOO-----
	 */
	Bottom,

	/**
	 * The institution is watching the left part of the map
	 * 
	 * ------  ------
	 * ------  ------
	 * OOO---  ------
	 * OOO
	 * OOO---  ------
	 * ------  ------
	 * ------  ------
	 */
	Left,

	/**
	 * The institution is watching the right part of the map
	 * 
	 * ------  ------
	 * ------  ------
	 * ------  ---OOO
	 *            OOO
	 * ------  ---OOO
	 * ------  ------
	 * ------  ------
	 */
	Right,

	/**
	 * The institution is watching the central part of the map
	 * 
	 * ------  ------
	 * ------  ------
	 * -----OOOO-----
	 *      OOOO 
	 * -----OOOO-----
	 * ------  ------
	 * ------  ------
	 */
	Center;

	/**
	 * Returns the height of the scope for the traffic institution
	 */
	public int getStartRow() 
	{
		switch(this) {
		case Top:			return 0;
		case Bottom:	return 13;
		default:			return 6;	// Left, Right, Center
		}
	}
	
	/**
	 * Returns the y coordinate for the position of the traffic institution
	 * 
	 * @return
	 */
	public int getStopRow() 
	{
		switch(this) {
		case Top:			return 5;
		case Bottom:	return 18;
		default:			return 12;	// Left, Right, Center
		}
	}
	
	
	/**
	 * Returns the x coordinate for the position of the traffic institution
	 * 
	 * @return
	 */
	public int getStartCol() 
	{
		switch(this) {
		case Left:		return 0;
		case Right:		return 13;
		default:			return 6; // Top, Bottom, Center
		}
	}

	/**
	 * Returns the width of the scope for the traffic institution
	 */
	public int getStopCol() 
	{
		switch(this) {
		case Left:		return 5;
		case Right:		return 18;
		default:			return 12;	// Top, Bottom, Center
		}
	}

	/**
	 * 
	 * @return
	 */
	public static List<TrafficCameraPosition> getPossiblePositions() {
		List<TrafficCameraPosition> posList = new ArrayList<TrafficCameraPosition>();
		
		posList.add(Center);
		posList.add(Top);
		posList.add(Bottom);
		posList.add(Left);
		posList.add(Right);
		
		return posList; 
	}
	
	/**
	 * 
	 */
	public String toString()
	{
		switch(this) {
		case Top:			return "Top";
		case Bottom:	return "Bottom";
		case Left:		return "Left";
		case Right:		return "Right";
		default:			return "Center";
		}
	}
}
