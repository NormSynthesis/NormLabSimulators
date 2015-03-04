package es.csic.iiia.normlab.traffic.car;

import java.awt.Color;
import java.util.Random;

import es.csic.iiia.normlab.traffic.TrafficSimulator;

/**
 * Color for a car
 * 
 * @author Javier Morales (jmoralesmat@gmail.com)
 *
 */
public class CarColor {

	//-----------------------------------------------------------------
	// Attributes
	//-----------------------------------------------------------------
	
	private static final int COLOR_BROWN = 0;
	private static final int COLOR_CYAN = 1;
	private static final int COLOR_YELLOW = 2;
	private static final int COLOR_PURPLE = 3;
	private static final int COLOR_GREEN = 4;
	private static final int COLOR_ORANGE = 5;
	
	private Color color;
	
	//-----------------------------------------------------------------
	// Methods
	//-----------------------------------------------------------------
	
	/**
	 * Constructor
	 */
	public CarColor() {
		Random rnd = new Random();
		int n = rnd.nextInt(6);
		float s = 0.5f, b = 0.75f;
		
		switch(n) {
		case COLOR_BROWN:
			color = Color.getHSBColor(0.1f, s, b);
			break;
		case COLOR_CYAN:
			color = Color.getHSBColor(0.25f, s,b);
			break;
		case COLOR_YELLOW:
			color = Color.getHSBColor(0.4f, s,b);
			break;
		case COLOR_PURPLE:
			color = Color.getHSBColor(0.55f, s,b);
			break;
		case COLOR_GREEN:
			color = Color.getHSBColor(0.7f, s,b);
			break;
		case COLOR_ORANGE:
			color = Color.getHSBColor(0.85f, s,b);
			break;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public Color getColor() {
		return this.color;
	}
}
