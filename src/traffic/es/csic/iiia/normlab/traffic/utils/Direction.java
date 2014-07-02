package es.csic.iiia.normlab.traffic.utils;

import java.util.Random;

import es.csic.iiia.normlab.traffic.TrafficSimulator;

/**
 * 
 * @author Jan
 */
public enum Direction{
	
	North, East, South , West;

	/**
	 * Returns the direction that is opposite to parameter direction
	 */
	public Direction getOppositeDirection(){
		Direction ret = null;
		switch (this){
		case North: ret = Direction.South; break;
		case East: ret = Direction.West; break;
		case South: ret = Direction.North; break;
		case West: ret = Direction.East; break;
		}
		return ret;
	}

	/**
	 * 
	 * @return
	 */
	public String getArrow(){
		String ret = null;
		switch (this){

		case North: ret = "^"; break;
		case East: ret = ">"; break;
		case South: ret = "v"; break;
		case West: ret = "<"; break;
		}
		return ret;
	}

	/**
	 * 
	 * @return
	 */
	public int get90DegreeStepsFromNorth(){
		int ret = 0;
		switch (this){

		case North: ret = 0; break;
		case East: ret = 1; break;
		case South: ret = 2; break;
		case West: ret = 3; break;
		}
		return ret;
	}

	/**
	 * 
	 * @param steps
	 * @return
	 */
	public Direction getTurnedDirectionIn90DegSteps(int steps){
		Direction ret = this;
		for(int i = 0; i<steps;i++)
			ret = ret.getRightTurnValue();
		return ret;
	}

	/**
	 * 
	 * @param relativeTo
	 * @return
	 */
	public int getRotationStepsRelativeTo(Direction relativeDir)
	{
		Direction dir = this;
		int steps = 0;
		while (dir!=relativeDir)
		{
			steps++;
			dir = dir.rotate(1);
		}
		return steps;
	}
	
	/**
	 * 
	 * @param relativeTo
	 * @return
	 */
	public Direction getDirectionRelativeTo(Direction relativeTo) {
		int rel = relativeTo.get90DegreeStepsFromNorth();
		int cur = this.get90DegreeStepsFromNorth();
		int ret = cur-rel;
		return Direction.from90DegreeStepsFromNorth(ret);
	}

	/**
	 * 
	 * @param dir
	 * @return
	 */
	public static Direction from90DegreeStepsFromNorth(int dir){
		if(dir<0)
			dir+=4;
		switch(dir){
		case 0: return North;
		case 1: return East;
		case 2: return South;
		case 3: return West;
		}
		return null;
	}

	/**
	 * 
	 * @param times
	 * @return
	 */
	public Direction rotate(int times)
	{
		Direction rotated = this;
		for(int i=0; i<times; i++)
			rotated = rotated.getRightTurnValue();
		
		return rotated;
	}
	
	/**
	 * 
	 * @return
	 */
	public Direction getRightTurnValue(){
		switch(this){
		case North: return East;
		case East: return South;
		case South: return West;
		case West: return North; 
		}
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public static Direction getRandomDirection(){
		Random rnd = TrafficSimulator.getRandom();
		int r = rnd.nextInt(4);
		Direction ret = null;
		switch (r){

		case 0: ret = Direction.North; break;
		case 1: ret = Direction.East; break;
		case 2: ret = Direction.South; break;
		case 3: ret = Direction.West; break;
		}
		return ret;
	}
	
	/**
	 * 
	 */
	public String toString() {
		switch(this) {
		case North: return "North";
		case East: return "East";
		case South: return "South";
		case West: return "West"; 			
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String toCapitalLetter() {
		switch(this) {
		case North: return "N";
		case East: return "E";
		case South: return "S";
		case West: return "W"; 			
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String toArrow() {
		switch(this) {
		case North: return "^";
		case East: return ">";
		case South: return "v";
		case West: return "<"; 			
		}
		return null;
	}
	
	/**
	 * 
	 * @param arrow
	 * @return
	 */
	public static Direction fromArrow(String arrow)
	{
		if(arrow.equals("^"))	return Direction.North;
		if(arrow.equals("<"))	return Direction.West;
		if(arrow.equals(">"))	return Direction.East;
		if(arrow.equals("v"))	return Direction.South;
		return null;
	}

//public int get90DegreeStepsFromNorthBackwards(){
//	int ret = 0;
//	switch (this){
//
//	case North: ret = 0; break;
//	case West: ret = 1; break;
//	case South: ret = 2; break;
//	case East: ret = 3; break;
//	}
//	return ret;
//}
}
