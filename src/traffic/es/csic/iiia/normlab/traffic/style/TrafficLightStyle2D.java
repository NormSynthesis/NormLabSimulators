package es.csic.iiia.normlab.traffic.style;


import java.awt.Color;
import java.awt.Font;

import repast.simphony.visualizationOGL2D.StyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.Position;
import saf.v3d.scene.VSpatial;
import es.csic.iiia.normlab.traffic.agent.TrafficLight;

/**
 * AgentStyle2D - Colors agents based on source positions.
 *
 * @author Jan Koeppen (jankoeppen@gmx.net)
 *
 */
public class TrafficLightStyle2D implements StyleOGL2D<TrafficLight> {

	//----------------------------------------------------------
	// Attributes 
	//----------------------------------------------------------
	int width = 18;
	int height = 18;

	//----------------------------------------------------------
	// Methods 
	//----------------------------------------------------------

//	/**
//	 * Paint method 
//	 */
//	@Override
//	public Paint getPaint(Object o){
//		return ((TrafficLight)o).getColor();
//	}
//
//	/**
//	 * Returns the stroke to paint
//	 */
//	public Stroke getStroke(Object o){
//		return new BasicStroke();
//	}
//
//	/**
//	 * Returns the rectangle to paint
//	 */
//	@Override
//	public Rectangle2D getBounds(Object object) {
//		return new Rectangle2D.Float(0, 0, width, height);
//	}
//
//	/**
//	 * Returns the text to paint
//	 */
//	@Override
//	public PText getLabel(Object object) {
//		return null;
//	}

	@Override
	public void init(ShapeFactory2D factory) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public VSpatial getVSpatial(TrafficLight object, VSpatial spatial) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getColor(TrafficLight object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getBorderSize(TrafficLight object) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Color getBorderColor(TrafficLight object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getRotation(TrafficLight object) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getScale(TrafficLight object) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getLabel(TrafficLight object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Font getLabelFont(TrafficLight object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getLabelXOffset(TrafficLight object) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getLabelYOffset(TrafficLight object) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Position getLabelPosition(TrafficLight object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getLabelColor(TrafficLight object) {
		// TODO Auto-generated method stub
		return null;
	}
}