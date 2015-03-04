package es.csic.iiia.normlab.traffic.apps;

import es.csic.iiia.normlab.traffic.agent.TrafficElement;

public class Nothing implements TrafficElement {

	private int x,y;
	
	public Nothing(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void move() {}

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}
	
	public String toString() {
		return "-";
	}
}
