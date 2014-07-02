package es.csic.iiia.normlab.traffic.style;


import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

import es.csic.iiia.normlab.traffic.agent.Collision;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.NamedShapeCreator;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.VSpatial;
import sl.shapes.StarPolygon;

/**
 * CarStyle2D - Colors agents based on source positions.
 *
 * @author Javier Morales
 *
 */
public class CollisionStyle2D extends DefaultStyleOGL2D {

	protected Shape shape = new StarPolygon(0,0,30,14,12); 

	//----------------------------------------------------------
	// Attributes 
	//----------------------------------------------------------

	private ShapeFactory2D factory;

	//----------------------------------------------------------
	// Methods 
	//----------------------------------------------------------

	/**
	 * 
	 */
	public void init(ShapeFactory2D factory) {
		this.factory = factory;

		Rectangle2D bounds = shape.getBounds2D();
		float size = 30;
		float scaleX = size / (float) bounds.getWidth();
		float scaleY = size / (float) bounds.getWidth();
		shape = AffineTransform.getScaleInstance(scaleX, scaleY).createTransformedShape(shape);
		GeneralPath path = new GeneralPath(shape);
		path.closePath();
		NamedShapeCreator creator = factory.createNamedShape("collision");
		creator.addShape(shape, Color.RED, true);
		creator.registerShape();
	}

	/**
	 * @return Color.BLUE.
	 */
	public Color getColor(Object o) {
		Collision col = (Collision)o;
		if(col.isViolation()) {
			return Color.PINK;
		}
		return Color.RED;
	}

	/**
	 * @return a circle of radius 4.
	 */
	public VSpatial getVSpatial(Object agent, VSpatial spatial) {
		if (spatial == null) {
			return factory.getNamedSpatial("collision");
		} else {
			return spatial;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repast.simphony.visualizationOGL2D.StyleOGL2D#getBorderSize(java.lang.Object
	 * )
	 */
	public int getBorderSize(Object object) {
		return 5;
	}

	/**
	 * Paint method 
	 */
	//	@Override
	//	public Paint getPaint(Object o){
	//		Collision col = (Collision)o;
	//		if(col.isViolation())
	//			return Color.PINK;
	//		
	//		return Color.RED;
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
	//	public PText getLabel(Object object)
	//	{
	////		Collision col = (Collision) object;
	//		PText label= new PText("");
	////		label.setOffset((width/6.0),(height));
	////		label.setTextPaint(Color.BLACK);
	////		
	//		return label;
	//	}
}