package es.csic.iiia.normlab.traffic.car;

import es.csic.iiia.nsm.agent.EnvironmentAgentAction;

/**
 * Defines an action to be taken by an agent
 * 
 * @author Javier Morales (jmoralesmat@gmail.com)
 *
 */
public enum CarAction implements EnvironmentAgentAction {

	Nothing, Go, Stop, Accelerate, Decelerate, TurnLeft, TurnRight;

	/**
	 * Returns the opposite action to the passed action
	 * 
	 * @param action
	 * @return
	 */
	public CarAction getOpposite() {

		switch(this) {
		case Stop:				return Go;
		case Go:					return Stop;
		case Accelerate:	return Decelerate;
		case Decelerate: 	return Accelerate;
		case TurnLeft:		return TurnRight;
		case TurnRight:		return TurnLeft;
		default:					return Nothing;
		}
	}
	
	/**
	 * To string method
	 */
	public String toString() {
		switch(this) {
		case Go: 					return "Go";
		case Stop: 				return "Stop";
		case Accelerate:	return "Accelerate";
		case Decelerate:	return "Decelerate";
		case TurnLeft: 		return "TurnLeft";
		case TurnRight:		return "TurnRight";
		default:					return "Nothing";
		}
	}

	/**
	 * To string method
	 */
	public String toStringExt() {
		switch(this) {
		case Go: 					return "Go";
		case Stop: 				return "Stop";
		case Accelerate:	return "Accelerate";
		case Decelerate:	return "Decelerate";
		case TurnLeft: 		return "TurnLeft";
		case TurnRight:		return "TurnRight";
		default:					return "Nothing";
		
		}
	}
}
