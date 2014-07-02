package es.csic.iiia.normlab.traffic.style;

import java.awt.Color;
import java.awt.Paint;

import es.csic.iiia.normlab.traffic.normsynthesis.TrafficNormSynthesisSettings;
import repast.simphony.valueLayer.ValueLayer;
import repast.simphony.visualizationOGL2D.ValueLayerStyleOGL;

/**
 * NormStyle2D - Coloring of value layer (higher performance than agent color) used to paint background of blocking norms red and background of non-lane fields green.
 *
 * @author Jan Koeppen (jankoeppen@gmx.net)
 *
 */
public class CarMapPositionStyle2D implements ValueLayerStyleOGL {

	//-----------------------------------------------------------------
	// Attributes
	//-----------------------------------------------------------------

	/**
	 * 
	 */
	private final Color cWall = Color.getHSBColor(0.6f, 0.8f, 0.4f);

	/**
	 * 
	 */
	private final Color cRoad = Color.LIGHT_GRAY;

	/**
	 * 
	 */
	private ValueLayer layer;

	//-----------------------------------------------------------------
	// Methods
	//-----------------------------------------------------------------

	/**
	 * Returns the color based on the value at given coordinates.
	 */
	public Paint getPaint(double... coordinates) {
		double v = layer.get(coordinates);

		if (v == TrafficNormSynthesisSettings.WALL_POSITION) return cWall;
		return cRoad;
	}

	/**
	 * 
	 */
	public float getCellSize() {
		return 20.2f;
	}

	@Override
	public Color getColor(double... coordinates) {
		double v = layer.get(coordinates);

		if (v == TrafficNormSynthesisSettings.WALL_POSITION) return cWall;
		return cRoad;
	}

	@Override
	public void init(ValueLayer layer) {
		this.layer = layer;
	}
}
