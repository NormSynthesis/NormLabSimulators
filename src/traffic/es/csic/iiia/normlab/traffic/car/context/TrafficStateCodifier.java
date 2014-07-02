package es.csic.iiia.normlab.traffic.car.context;

import org.apache.commons.lang3.StringUtils;

import es.csic.iiia.normlab.traffic.agent.Car;
import es.csic.iiia.normlab.traffic.agent.Collision;
import es.csic.iiia.normlab.traffic.agent.TrafficElement;
import es.csic.iiia.normlab.traffic.agent.TrafficLight;
import es.csic.iiia.normlab.traffic.agent.Wall;
import es.csic.iiia.normlab.traffic.utils.Direction;

/**
 * 
 * @author Javier Morales (jmoralesmat@gmail.com)
 *
 */
public class TrafficStateCodifier 
{	
	/**
	 * 
	 * @param pos
	 * @return
	 */
	public static String codify(TrafficElement element)
	{
		String desc = "";
		
		if(element instanceof Wall)
		{
			desc = TrafficStateManager.WALL;
		}
		else if(element instanceof TrafficLight)
		{
			desc = TrafficStateManager.TRAFFIC_LIGHT + codifyTrafficLight((TrafficLight)element);
		}
		else if(element instanceof Car)
		{
			desc = TrafficStateManager.CAR + codifyCar((Car)element);
		}
		else if(element instanceof Collision)
		{
			if(((Collision) element).isViolation())
			{
				desc = TrafficStateManager.VIOL_COL + codifyCollision((Collision)element);	
			}
			else
			{
				desc = TrafficStateManager.COLLISION + codifyCollision((Collision)element);
			}
		}
		
		desc = StringUtils.rightPad(desc, 63, '0');
		
		return desc;
	}

	/**
	 * 
	 * @return
	 */
	public static String decodify(String codDesc)
	{
		String desc;
		
		switch(TrafficStateManager.getType(codDesc))
		{
			case Wall:
				desc = "|";
				break;
				
			case TrafficLight:
				desc = decodifyTrafficLight(codDesc);
  			break;
				
  		case Car:
  			desc = decodifyCar(codDesc);
  			break;
  			
  		case Collision:
  			desc = decodifyCollision(codDesc);
  			break;
  			
  		case ViolCollision:
  			desc = decodifyViolCollision(codDesc);
  			break;
  			
  		case Anything:
  			desc = "*";
  			break;
  			
  		default:
  			desc = "-";
		}
		
		return desc;
	}
	
	/**
	 * 
	 * @return
	 */
	public static String codify(String desc)
	{
		String codDesc = "";
		
		if(desc.equals("|"))
			codDesc = StringUtils.rightPad(TrafficStateManager.WALL, 63, '0');
		
		else if(desc.equals("^"))
			codDesc = StringUtils.rightPad((TrafficStateManager.CAR + "0000000000"), 63, '0');
		
		else if(desc.equals(">"))
			codDesc = StringUtils.rightPad((TrafficStateManager.CAR + "0000000001"), 63, '0');
		
		else if(desc.equals("v"))
			codDesc = StringUtils.rightPad((TrafficStateManager.CAR + "0000000010"), 63, '0');
		
		else if(desc.equals("<"))
			codDesc = StringUtils.rightPad((TrafficStateManager.CAR + "0000000011"), 63, '0');
		
		else if(desc.equals("*"))
			codDesc = StringUtils.rightPad(TrafficStateManager.ANYTHING, 63, '0');
		
		else if(desc.equals("-"))
			codDesc = StringUtils.rightPad("0", 63, '0');
		
		return codDesc;
	}
	
	//----------------------------------------------------------------------
	// Private methods
	//----------------------------------------------------------------------	
	
	/**
	 * 
	 * @return
	 */
	private static String codifyTrafficLight(TrafficLight light)
	{
		String state = "";

		// Heading
		int dir = light.getHeading().get90DegreeStepsFromNorth();
		state += StringUtils.leftPad(Long.toBinaryString(dir), 2, '0');
				
		return state;
	}
	
	/**
	 * 
	 * @param codDesc
	 * @return
	 */
	private static String decodifyTrafficLight(String codDesc)
	{
		int steps = Integer.valueOf(codDesc.substring(3, 5), 2);
		Direction dir = Direction.North.getTurnedDirectionIn90DegSteps(steps);
		
		return dir.toCapitalLetter();
	}
	/**
	 * 
	 * @return
	 */
	private static String codifyCar(Car car)
	{
		String state = "";
		
		// ID
		state += codifyCarId(car.getId());
		
		// Heading
		Direction dir = car.getPosition().getDirection();
		state += codifyCarHeading(dir);
		
		return state;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	private static String codifyCarId(long id)
	{
		return StringUtils.leftPad(Long.toBinaryString(id), 8, '0');	
	}
	
	/**
	 * 
	 * @param dir
	 */
	private static String codifyCarHeading(Direction dir)
	{
		int steps = dir.get90DegreeStepsFromNorth();
		return StringUtils.leftPad(Long.toBinaryString(steps), 2, '0');
	}
	
	/**
	 * 
	 * @return
	 */
	private static String decodifyCar(String codDesc)
	{
		int steps = Integer.valueOf(codDesc.substring(11, 13), 2);
		Direction dir = Direction.North.getTurnedDirectionIn90DegSteps(steps);
		
		return dir.toArrow();
	}
	
	/**
	 * 
	 * @return
	 */
	private static String codifyCollision(Collision col)
	{
		int numElems = col.getElements().size();
		String state = "";
		
		// Num cars
		state += StringUtils.leftPad(Long.toBinaryString(numElems), 3, '0');
		
		// Codify each car
		for(TrafficElement elem : col.getElements())
		{
			Car car = (Car)elem;
			state += codifyCar(car);
		}			
		return state;
	}
	
	/**
	 * 
	 * @param codDesc
	 * @return
	 */
	private static String decodifyCollision(String codDesc)
	{
		return "X";
	}
	
	/**
	 * 
	 * @param codDesc
	 * @return
	 */
	private static String decodifyViolCollision(String codDesc)
	{
		return "O";
	}
}
