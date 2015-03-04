package es.csic.iiia.normlab.traffic.apps;

import es.csic.iiia.normlab.traffic.agent.Car;
import es.csic.iiia.normlab.traffic.agent.TrafficElement;
import es.csic.iiia.normlab.traffic.agent.Wall;
import es.csic.iiia.normlab.traffic.car.context.TrafficStateCodifier;
import es.csic.iiia.normlab.traffic.factory.CarContextFactory;
import es.csic.iiia.normlab.traffic.factory.TrafficFactFactory;
import es.csic.iiia.normlab.traffic.map.TrafficMatrix;
import es.csic.iiia.normlab.traffic.utils.Direction;
import es.csic.iiia.normlab.traffic.utils.Matrix;
import es.csic.iiia.normlab.traffic.utils.Turn;
import es.csic.iiia.normlab.traffic.utils.Utilities;
import es.csic.iiia.nsm.agent.language.PredicatesDomains;
import es.csic.iiia.nsm.agent.language.SetOfPredicatesWithTerms;
import es.csic.iiia.nsm.norm.Norm;
import es.csic.iiia.nsm.norm.evaluation.NormCompliance;

/**
 * 
 * @author "Javier Morales (jmorales@iiia.csic.es)"
 *
 */
public class SubstitutabilityMap extends TrafficMatrix {

	private int numCars;
	private int xDim, yDim;
	private PredicatesDomains predDomains;
	private Matrix<TrafficElement> map;
	private CarContextFactory carContextFactory;
	private TrafficFactFactory factFactory;
	private NormativeSystem ns;

	/**
	 * 
	 */
	public SubstitutabilityMap(int xDim, int yDim, 
			PredicatesDomains predDomains, 
			CarContextFactory carContextFactory,
			TrafficFactFactory factFactory) {

		super(xDim, yDim);

		this.ns = ns;
		this.factFactory = factFactory;
		this.carContextFactory = carContextFactory;
		this.predDomains = predDomains;
		this.xDim = xDim;
		this.yDim = yDim;
		this.map = new Matrix<TrafficElement>(xDim, yDim, "TrafficMatrix");
		this.numCars = 0;
	}

	/**
	 * 
	 */
	public void clear() {
		super.clear();
		this.map.clear();
		this.numCars = 0;
	}

	/**
	 * 
	 * @param leftToRightNorm
	 * @param bottomToTopNorm
	 */
	public void addNorms(Norm leftToRightNorm, Norm bottomToTopNorm,
			NormativeSystem ns) {
		this.addNormFromLeftToRight(leftToRightNorm, ns);
		this.addNormFromBottomToTop(bottomToTopNorm,ns);
		this.codify();
	}

	/**
	 * 
	 * @return
	 */
	public boolean isConsistent() {
		TrafficElement leftElement = this.map.get(0, 1);
		TrafficElement bottomElement = this.map.get(1, 0);

		if(!(leftElement instanceof Car) ||
				!(bottomElement instanceof Car)) {
			return false;
		}

		for(TrafficElement element : this.map.getAllElements()) {
			if(element instanceof Inconsistency) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 */
	public void moveCars(NormCompliance leftCompliance, 
			NormCompliance bottomCompliance) {

		/* All cars perceive */
		for(int y=2; y>=0; y--) {
			for(int x=2; x>=0; x--) {
				TrafficElement element = this.map.get(x, y);

				if(element instanceof Car) {
					Car car = (Car) element;

					/* Left car */
					if(x==0 && y==1) {
						if(leftCompliance == NormCompliance.INFRINGEMENT) {
							continue;
						}
					}
					/* Bottom car */
					else if(x==1 && y==0) {
						if(bottomCompliance == NormCompliance.INFRINGEMENT) {
							continue;
						}
					}
					car.perceiveAndReason();
				}
			}
		}

		/* All cars move */
		for(int y=2; y>=0; y--) {
			for(int x=2; x>=0; x--) {
				TrafficElement element = this.map.get(x, y);

				if(element instanceof Car) {
					Car car = (Car)element;

					int oldX = car.getX();
					int oldY = car.getY();

					//					/* Left car */
					//					if(x==0 && y==1) {
					//						if(leftCompliance == NormCompliance.Infringement) {
					//							car.move();
					//						}
					//					}
					//					/* Bottom car */
					//					else if(x==1 && y==0) {
					//						if(bottomCompliance == NormCompliance.Infringement) {
					//							car.move();
					//						}
					//					}

					/* Other cars perceive, reason and move (whenever
					 * no norms to them) */
					//					else {
					car.move();
					//					}

					int newX = car.getX();
					int newY = car.getY();

					// Move car
					this.map.set(oldX, oldY, null);
					if(!this.isCarOutOfBounds(car)) {

						/* If there is a car in the position, then the car collides */
						if(this.map.get(newX, newY) instanceof Car || 
								this.map.get(newX, newY) instanceof Collision) {
							this.map.set(newX, newY, new Collision(newX,newY));	
						}
						/* If there is nothing in the position, then the car moves towards it */
						else {
							this.map.set(newX, newY, car);	
						}
					}
				}
				this.codify();
			}	
		}
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasCollisions() {
		for(TrafficElement element : this.map.getAllElements()) {
			if(element instanceof Collision) {
				return true;
			}
		}
		return false;
	}

	//---------------------------------------------------------------------------
	// Map management methods 
	//---------------------------------------------------------------------------

	/**
	 * 
	 */
	private void addNormFromLeftToRight(Norm norm, NormativeSystem ns) {
		SetOfPredicatesWithTerms precond = norm.getPrecondition();
		String leftPred = precond.getTerms("l").get(0);
		String frontPred = precond.getTerms("f").get(0);
		String rightPred = precond.getTerms("r").get(0);

		this.addElementFromLeftToRight(leftPred, 0, 1, ns);
		this.addElementFromLeftToRight(frontPred, 1, 1, ns);
		this.addElementFromLeftToRight(rightPred, 2, 1, ns);
	}

	/**
	 * 
	 */
	private void addNormFromBottomToTop(Norm norm, NormativeSystem ns) {
		SetOfPredicatesWithTerms precond = norm.getPrecondition();
		String leftPred = precond.getTerms("l").get(0);
		String frontPred = precond.getTerms("f").get(0);
		String rightPred = precond.getTerms("r").get(0);

		this.addElementFromBottomToTop(rightPred, 1, 0, ns);
		this.addElementFromBottomToTop(frontPred, 1, 1, ns);
		this.addElementFromBottomToTop(leftPred, 1, 2, ns);
	}

	/**
	 * 
	 * @param strElement
	 */
	private void addElementFromLeftToRight(String strElement, int x, int y,
			NormativeSystem ns) {

		TrafficElement element = null;

		if(strElement.equals("w")) {
			element = new Wall(x, y);
		}
		else if(strElement.equals("-")) {
			element = new Nothing(x, y);
		}
		else if(strElement.equals("^") || strElement.equals(">") ||
				strElement.equals("v") || strElement.equals("<")) {

			Direction heading = Direction.fromArrow(strElement);
			element = new SubsCar((short) ++numCars, heading, x, y, predDomains, this,
					carContextFactory, factFactory, ns);
		}
		else {
		}
		this.addElement(element, x, y);
	}

	/**
	 * 
	 * @param strElement
	 */
	private void addElementFromBottomToTop(String strElement, int x, int y,
			NormativeSystem ns) {

		TrafficElement element = null;

		if(strElement.equals("w")) {
			element = new Wall(x, y);
		}
		else if(strElement.equals("-")) {
			element = new Nothing(x, y);
		}
		else if(strElement.equals("^") || strElement.equals(">") ||
				strElement.equals("v") || strElement.equals("<")) {

			Direction heading = Direction.fromArrow(strElement);
			heading = Utilities.getTurnDirection(heading, Turn.Right);
			element = new SubsCar((short) ++numCars, heading, x, y, predDomains, 
					this, carContextFactory, factFactory, ns);
		}
		this.addElement(element, x, y);
	}

	/**
	 * 
	 */
	private void codify() {
		String codState;

		// Clear previous information
		super.clear();

		for(int x=0; x<yDim; x++) {
			for(int y=0; y<xDim; y++) {
				TrafficElement elem = this.map.get(x, y);

				if(elem!=null) {
					// Create binary description and add it to the position
					codState = TrafficStateCodifier.codify(elem);  				
					this.set((yDim-1)-y, x, codState);
				}
			}
		}
	}

	/**
	 * 
	 * @param element
	 * @param x
	 * @param y
	 */
	private void addElement(TrafficElement element, int x, int y) {
		TrafficElement elemInPosXY = this.map.get(x, y);

		if(elemInPosXY == null) {
			this.map.set(x, y, element);
		}
		else if(element != null){
			if(!elemInPosXY.toString().equals(element.toString())) {
				this.map.set(x, y, new Inconsistency());
			}
			else {
				this.map.set(x, y, element);
			}
		}
	}

	/**
	 * Returns true if the car is out of the map
	 * 
	 * @param p
	 * @return
	 */
	private boolean isCarOutOfBounds(Car car) {
		int x = car.getX();
		int y = car.getY();

		if(x<0 || y < 0 || x >= xDim || y >= yDim)
			return true;
		return false;
	}

	/**
	 * 
	 */
	public String toString() {
		return this.map.toString();
	}
}
