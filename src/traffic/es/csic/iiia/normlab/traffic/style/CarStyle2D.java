package es.csic.iiia.normlab.traffic.style;

import java.awt.Color;
import java.awt.Font;

import org.piccolo2d.nodes.PText;

import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.scene.VSpatial;
import es.csic.iiia.normlab.traffic.agent.Car;
import es.csic.iiia.normlab.traffic.car.CarReasonerState;

/**
 * 
 * @author "Javier Morales (jmorales@iiia.csic.es)"
 *
 */
public class CarStyle2D extends DefaultStyleOGL2D {

	//----------------------------------------------------------
	// Attributes 
	//----------------------------------------------------------

	int width = 18;
	int height = 18;

	//----------------------------------------------------------
	// Methods 
	//----------------------------------------------------------

	/**
	 * 
	 */
	public int getBorderSize(Object object) {
		return 2;
	}
	/**
	 * @return a circle of radius 4.
	 */
	public VSpatial getVSpatial(Object agent, VSpatial spatial) {
		if (spatial == null) {
			spatial = shapeFactory.createCircle(9, 18);
		}
		return spatial;
	}


	public float getRotation(Object agent) {
		return 10f;
	}
	
	@Override
	public Color getColor(Object object) {
		Car car = (Car)object;

		if(car.isCasualStop()) {
			return Color.WHITE;
		}
		if(car.isCollided()) {
			return Color.RED;
		} 
		else if(car.getReasonerState() == CarReasonerState.NormWillBeApplied) {
			return Color.GREEN;
		}
		else if(car.getReasonerState() == CarReasonerState.NormWillBeViolated) {
			return Color.RED;
		}
		else {
			return car.getColor();
		}
	}

	public Font getLabelFont(Object object) {
		Font font = new Font("Verdana", Font.PLAIN, 11);
		return font;
	}

	@Override
	public String getLabel(Object object) {
		PText label = null;

		Car c = (Car)object;
		int steps = c.getPosition().getDirection().get90DegreeStepsFromNorth();

		if(c.isCollided()) {
			label = new PText("");
		}
		else if(c.isCasualStop()) {
			label = new PText("S");
			label.setOffset((width/7.0),(height));
			label.setTextPaint(Color.RED);
		}
		else if(c.getReasonerState() == CarReasonerState.NormWillBeApplied)	{
			label = new PText(String.valueOf(c.getNormToApply().getId()));
			label.setOffset((width/7.0),(height));
		}
		else if(c.getReasonerState() == CarReasonerState.NormWillBeViolated)	{
			label = new PText(String.valueOf(c.getNormToViolate().getId()));
			label.setOffset((width/7.0),(height));
		}
		else
		{
			switch(steps) {
			case 0:
				label = new PText("^");
				break;
			case 1:
				label = new PText(">");
				break;
			case 2:
				label = new PText("v");
				break;
			case 3:
				label = new PText("<");
				break;
			}
			label.setOffset((width/3.5),(height));
		}

		return label.getText();
	}

	/**
	 * 
	 */
	public float getLabelXOffset(Object object) {
		return 0;
	}

	/**
	 * 
	 */
	public float getLabelYOffset(Object object) {
		return -10;
	}

}