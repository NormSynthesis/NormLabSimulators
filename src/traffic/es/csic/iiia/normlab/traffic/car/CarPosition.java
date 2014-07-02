package es.csic.iiia.normlab.traffic.car;

import es.csic.iiia.normlab.traffic.utils.Direction;
import es.csic.iiia.normlab.traffic.utils.Turn;
import es.csic.iiia.normlab.traffic.utils.Utilities;
import repast.simphony.space.grid.GridPoint;

/**
 * Position of a car. Contains coordinates and direction
 * 
 * @author Javier Morales (jmoralesmat@gmail.com)
 *
 */
public class CarPosition {

	//-----------------------------------------------------------------
	// Attributes
	//-----------------------------------------------------------------

	private Direction direction = null;
	private int x, y;

	//-----------------------------------------------------------------
	// Methods
	//-----------------------------------------------------------------
	
	/**
	 * Constructor
	 * 
	 * @param x
	 * @param y
	 * @param direction
	 */
	public CarPosition(int x, int y, Direction direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
	}

	/**
	 * Turns the direction and returns it
	 * 
	 * @param turn
	 * @return
	 */
	public Direction turn(Turn turn){
		this.direction = Utilities.getTurnDirection(this.direction, turn);
		return this.direction;
	}

	/**
	 * Generates a new position with the (x,y) coordinates passed by parameter
	 * 
	 * @param gp
	 * @return
	 */
	public CarPosition add(GridPoint gp){
		CarPosition newPos = new CarPosition(this.x + gp.getX(),this.y + gp.getY(),this.direction);
		return newPos;
	}

	/**
	 * Returns a point with the (x,y) coordinates
	 * 
	 * @return
	 */
	public GridPoint getGridPoint(){
		return new GridPoint(x,y);
	}

	/**
	 * Returns the direction
	 * 
	 * @return
	 */
	public Direction getDirection(){
		return direction;
	}

	/**
	 * Sets the direction
	 * 
	 * @param direction
	 */
	public void setDirection(Direction direction){
		this.direction = direction;
	}

	/**
	 * Returns the x coordinate
	 * 
	 * @return
	 */
	public int getX(){
		return x;
	}

	/**
	 * Returns the y coordinate
	 * 
	 * @return
	 */
	public int getY(){
		return y;
	}

	/**
	 * Sets the x coordinate
	 * 
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * Sets the y coordinate
	 * 
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * Returns a string describing the object 
	 */
	@Override
	public String toString(){
		return this.direction.getArrow();
	}

	/**
	 * Returns a detailed string describing the object
	 * 
	 * @return
	 */
	public String toStringDetailled(){
		return "[" + x + "," + y + "," + direction.name() + "]";
	}
	
	/**
	 * Clones the position
	 */
	@Override
	public CarPosition clone() {
		CarPosition c = new CarPosition(this.x,this.y,this.direction);
		return c;
	}
}
