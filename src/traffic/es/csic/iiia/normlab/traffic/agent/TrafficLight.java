package es.csic.iiia.normlab.traffic.agent;

import java.awt.Color;

import es.csic.iiia.normlab.traffic.normsynthesis.TrafficNormSynthesisSettings;
import es.csic.iiia.normlab.traffic.utils.Direction;

/**
 * 
 * @author Javier Morales (jmoralesmat@gmail.com)
 *
 */
public class TrafficLight implements TrafficElement {

	/**
	 * 
	 */
	private static final int RED_DURATION = 32;
	private static final int GREEN_DURATION = 28;
	private static int ID_COUNTER = 0;
	
	/**
	 * 
	 */
	private int x, y;
	
	/**
	 * 
	 */
	private Color color;
	private int id, counter;
	private int turn, duration;
	private Direction heading;

	/**
	 * 
	 */
	public TrafficLight(int x, int y, int turn, Direction heading)
	{
		this.x = x;
		this.y = y;
		this.heading = heading;
		this.id = ++ID_COUNTER;
		this.turn = turn;

		switch(turn) {
		case 1:
			this.counter = RED_DURATION;
			this.color = Color.RED;
			break;

		case 2:
			this.counter = RED_DURATION*turn;
			this.color = Color.RED;
			break;
		
		case 3:
			this.counter = RED_DURATION*turn;
			this.color = Color.RED;
			break;
			
		case 4:
			this.counter = GREEN_DURATION;
			this.color = Color.GREEN;
			break;
		}
	}

	/**
	 * 
	 * @return
	 */
	public Color getColor() {
		if(TrafficNormSynthesisSettings.SIM_USE_TRAFFIC_LIGHTS) {
			return this.color;	
		}
		return Color.GRAY;
	}
	
	/**
	 * 
	 * @return
	 */
	public String toStringColor() {
		if(color == Color.RED)					return "red";
		else if(color == Color.GREEN)		return "green";
		else if(color == Color.YELLOW)	return "yellow";
		else														return "unknown";
	}
	
	/**
	 * 
	 * @return
	 */
	public Direction getHeading() {
		return this.heading;
	}
	
	/**
	 * 
	 * @param heading
	 */
	public void setHeading(Direction heading) {
		this.heading = heading;
	}
	
	/**
	 * 
	 * @param color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * 
	 * @return
	 */
	public int getTurn() {
		return this.turn;
	}
	
	/**
	 * 
	 */
	public void step() {
		counter--;

		if(counter <= 0) {
			if(color == Color.RED) {
				counter = GREEN_DURATION;
				color = Color.GREEN;
			} else {
				counter = RED_DURATION * 3 + (RED_DURATION - GREEN_DURATION);
				color = Color.RED;
			}
		}
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
  public void move() {
	  // TODO Auto-generated method stub
	  
  }
}
