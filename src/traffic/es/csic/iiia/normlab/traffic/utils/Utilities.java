package es.csic.iiia.normlab.traffic.utils;

import java.util.Random;

import es.csic.iiia.normlab.traffic.TrafficSimulator;


/**
 * Utilities - Static general usage methods
 *
 * @author Jan Koeppen (jankoeppen@gmx.net)
 * @author Javier Morales (jmoralesmat@gmail.com)
 *
 */
public class Utilities {
	
	/**
	 * 
	 * @return a random direction
	 */
	public static Direction getRandomDirection(){
		Random rnd = TrafficSimulator.getRandom();
		int rndInt = rnd.nextInt(4);
		Direction ret = null;
		
		switch (rndInt)
		{
		case 0: ret = Direction.North; break;
		case 1: ret = Direction.East; break;
		case 2: ret = Direction.South; break;
		case 3: ret = Direction.West; break;
		}
		return ret;
	}


	/**
	 * 
	 * @return a random turn direction
	 */
	public static Turn getRandomTurn(){
		int rnd = (int)(TrafficSimulator.getRandom().nextInt(3));
		Turn ret = null;
		switch (rnd){
		case 0: ret = Turn.Left; break;
		case 1: ret = Turn.Straight; break;
		case 2: ret = Turn.Right; break;
		}
		return ret;
	}


	/**
	 * 
	 * @param direction before turn
	 * @param turn direction
	 * @return the direction after the turn
	 */
	public static Direction getTurnDirection(Direction dir, Turn turn){
		Direction ret = dir;
		if(turn == Turn.Left)
			switch (dir){
			case North: ret = Direction.West; break; 
			case East: ret = Direction.North; break; 
			case South: ret = Direction.East; break; 
			case West: ret = Direction.South; break; 
			}
		else if(turn == Turn.Right)
			switch (dir){
			case North: ret = Direction.East; break; 
			case East: ret = Direction.South; break; 
			case South: ret = Direction.West; break; 
			case West: ret = Direction.North; break; 
			}
		// else if straight, return original direction
		return ret;
	}


	/**
	 * @param direction	a direction
	 * @return a 2 dimensional vector in the direction indicated by parameter direction
	 */
	public static int[] getDirVector(Direction direction){
		int[] ret = new int[2];
		switch(direction){
		case North:
			ret[0] = 0;
			ret[1] = 1;
			break;
		case East:
			ret[0] = 1;
			ret[1] = 0;
			break;
		case South:
			ret[0] = 0;
			ret[1] = -1;
			break;
		case West:
			ret[0] = -1;
			ret[1] = 0;
			break;
		}
		return ret;
	}

	public static int getLeftLane(int xDim){
		return (int)(Math.floor(0.5*xDim))-1;
	}
	
	public static int getRightLane(int xDim){
		return (int)(Math.floor(0.5*xDim));
	}

	public static int getLowerLane(int yDim){
		return (int)(Math.floor(0.5*yDim)-1);
	}

	public static int getUpperLane(int yDim){
		return (int)(Math.floor(0.5*yDim));
	}

	/**
	 * @param point1   first point
	 * @param point2   second point
	 * @return  the  Euclidean distance of two points
	 */
	public static double getDistance(double[] point1, double[] point2) {
		double distance = 0;
		for (int i = 0; i < point1.length; i++) {
			distance = distance + Math.pow((point1[i] - point2[i]), 2);
		}
		return Math.sqrt(distance);
	}
}
