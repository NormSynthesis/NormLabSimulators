package es.csic.iiia.normlab.traffic.agent;

/**
 * 
 * @author Javier Morales (jmoralesmat@gmail.com)
 *
 */
public class Wall implements TrafficElement {

	/**
	 * 
	 */
	private int x, y;
	
	/**
	 * 
	 * @param x
	 * @param y
	 */
	public Wall(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * 
	 */
	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 
	 */
	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
  public void move() {
	  // TODO Auto-generated method stub
	  
  }

}
