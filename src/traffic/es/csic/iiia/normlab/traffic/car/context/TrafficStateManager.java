package es.csic.iiia.normlab.traffic.car.context;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import es.csic.iiia.normlab.traffic.utils.Direction;

/**
 * 
 */
public class TrafficStateManager 
{
	/**
	 * 
	 */
	public static final String NOTHING = "000";
	
	/**
	 * 
	 */
	public static final String WALL = "001";

	/**
	 * 
	 */
	public static final String TRAFFIC_LIGHT = "010";

	/**
	 * 
	 */
	public static final String CAR = "011";

	/**
	 * 
	 */
	public static final String COLLISION = "100";

	/**
	 * 
	 */
	public static final String VIOL_COL = "101";

	/**
	 * 
	 */
	public static final String ANYTHING = "110";
	
	/**
	 * 
	 */
	public static final String UNKNOWN = "111";
	
	/**
	 * 
	 * @param state
	 * @return
	 */
	public static StateType getType(String codDesc)
	{
		String type = codDesc.substring(0, 3);

		if (type.equals(WALL))							return StateType.Wall;
		else if(type.equals(TRAFFIC_LIGHT))	return StateType.TrafficLight;
		else if(type.equals(CAR)) 					return StateType.Car;
		else if (type.equals(COLLISION))		return StateType.Collision;
		else if (type.equals(VIOL_COL))			return StateType.ViolCollision;
		else if (type.equals(ANYTHING))			return StateType.Anything;
		else if (type.equals(NOTHING))			return StateType.Nothing;
		else 																return StateType.Unknown;
	}

	/**
	 * 
	 * @param desc
	 * @return
	 */
	public static List<Long> getCarIds(String codDesc)
	{
		List<Long> agentIds = new ArrayList<Long>();
		long agentId;

		switch(getType(codDesc))
		{		
		case Car:
			agentId = getCarId(codDesc);
			agentIds.add(agentId);

			break;

		case Collision:
		case ViolCollision:
			int numCars = Integer.valueOf(codDesc.substring(3, 6), 2);
			int car = 0;

			while (car<numCars)
			{
				int carPos = 6+car*10;
				agentId = Long.valueOf(codDesc.substring(carPos,carPos+8), 2);
				agentIds.add(agentId);
				car++;
			}
			break;

		default:
			break;
		}		
		return agentIds;
	}

	/**
	 * 
	 * @param codDesc
	 * @return
	 */
	public static long getCarId(String codDesc)	{
		return Long.valueOf(codDesc.substring(3, 11), 2);
	}

	/**
	 * 
	 * @param id
	 */
	public static String setCarId(String codDesc, long id)
	{
		String newCodDesc = codDesc.substring(0, 3);
		newCodDesc += StringUtils.leftPad(Long.toBinaryString(id), 8, '0');
		newCodDesc += codDesc.substring(11, 63);

		return newCodDesc;
	}

	/**
	 * 
	 * @param codDesc
	 * @return
	 */
	public static Direction getCarHeading(String codDesc)
	{
		int steps = Integer.valueOf(codDesc.substring(11, 13),2);
		return Direction.North.getTurnedDirectionIn90DegSteps(steps);
	}

	/**
	 * Returns car heading into a collision
	 * 
	 * @param agentId
	 * @param codDesc
	 * @return
	 */
	public static Direction getCarHeading(long carId, String codDesc)
	{
		int steps;

		switch(getType(codDesc))
		{
		case Car:
			steps = Integer.valueOf(codDesc.substring(11, 13),2);
			return Direction.North.getTurnedDirectionIn90DegSteps(steps);

		case Collision:
		case ViolCollision:
			int numCars = Integer.valueOf(codDesc.substring(3, 6), 2);
			int car = 0;
			long id;

			while (car<numCars)
			{
				int carPos = 6+car*10;
				id = Long.valueOf(codDesc.substring(carPos,carPos+8), 2);
				if(id == carId)
				{
					steps = Integer.valueOf(codDesc.substring(carPos+8,carPos+10),2);
					return Direction.North.getTurnedDirectionIn90DegSteps(steps);	
				}
				car++;
			}
			return null;

		default:
			return null;
		}
	}

	/**
	 * 
	 * @param codDesc
	 * @param times
	 * @return
	 */
	public static String setCarHeading(String codDesc, Direction dir)
	{
		String ret = "";
		int steps = dir.get90DegreeStepsFromNorth();

		ret += codDesc.substring(0, 11);
		ret += StringUtils.leftPad(Long.toBinaryString(steps), 2, '0');

		ret = StringUtils.rightPad(ret, 63, '0');

		return ret;
	}

	//----------------------------------------------------------------------
	// Enumerations
	//----------------------------------------------------------------------

	/**
	 * 
	 */
	public enum StateType
	{
		Wall, 

		TrafficLight, 

		Car,

		Collision, 

		ViolCollision,

		Anything, 
		
		Nothing,
		
		Unknown;
	}
}
