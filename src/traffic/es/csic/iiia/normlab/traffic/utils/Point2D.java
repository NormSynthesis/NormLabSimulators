package es.csic.iiia.normlab.traffic.utils;

/**
 * 
 * @author Javier Morales (jmoralesmat@gmail.com)
 *
 */
public class Point2D {

	private int x, y;
	
	/**
	 * 
	 * @param x
	 * @param y
	 */
	public Point2D(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getX() {
		return this.x;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getY() {
		return this.y;
	}
}

