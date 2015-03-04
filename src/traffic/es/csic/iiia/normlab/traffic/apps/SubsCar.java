package es.csic.iiia.normlab.traffic.apps;

import es.csic.iiia.normlab.traffic.agent.Car;
import es.csic.iiia.normlab.traffic.agent.TrafficElement;
import es.csic.iiia.normlab.traffic.car.CarAction;
import es.csic.iiia.normlab.traffic.car.CarPosition;
import es.csic.iiia.normlab.traffic.car.context.CarContext;
import es.csic.iiia.normlab.traffic.factory.CarContextFactory;
import es.csic.iiia.normlab.traffic.factory.TrafficFactFactory;
import es.csic.iiia.normlab.traffic.map.TrafficMatrix;
import es.csic.iiia.normlab.traffic.utils.Direction;
import es.csic.iiia.nsm.agent.language.PredicatesDomains;
import es.csic.iiia.nsm.norm.Norm;

/**
 * The car agent
 *
 * @author Javier Morales (jmoralesmat@gmail.com)
 *
 */
public class SubsCar extends Car implements TrafficElement {

	//-----------------------------------------------------------------
	// Constructors
	//-----------------------------------------------------------------

	private TrafficMatrix matrix;
	private SubsCarReasoner reasoner;
	
	/**
	 * This constructor chooses a random start point for the car
	 */
	public SubsCar(short id, Direction heading, int x, int y,
			PredicatesDomains predDomains, TrafficMatrix matrix, 
			CarContextFactory contextFactory,
			TrafficFactFactory factFactory, NormativeSystem ns) {
		
		super(id, true, predDomains, contextFactory, factFactory);
		
		this.reasoner = new SubsCarReasoner(predDomains, factFactory);
		this.matrix = matrix;
		
		CarPosition pos = new CarPosition(x,y,heading);
		this.init(pos);
		
		/* Make the car aware of the current norms in the normative system */
		for(Norm norm : ns) {
			this.reasoner.addNorm(norm);
		}
	}
	
	/**
	 * Initializes the car 
	 */
	public void init(CarPosition position) {
		this.position = position;
		this.collided = false;
	}
	
	/**
	 * Reason about the current situation. Then decide what to do
	 */
	public void perceiveAndReason() {

		// Perceive and reason to get the action to apply in the next step
		this.context = this.perceive(matrix);
		this.nextAction = this.reason();
	}
	
	/**
	 * The car perceives the environment 
	 */
	private CarContext perceive(TrafficMatrix matrix) {
		return this.generateScope(matrix);
	}
	

	/**
	 * Generates the car scope in the current moment
	 */
	public CarContext generateScope(TrafficMatrix matrix) {
		CarContext context = this.contextFactory.getCarContextIn(matrix, id, CarContext.Type.Front);
		return context;
	}
	
	/**
	 * Makes the car reason about its current situation
	 */
	private CarAction reason() {
		CarAction action;

		// Decide next action with the reasoner
		action = this.reasoner.decideAction(this, context);
		this.reasonerState = reasoner.getState();

		this.normToViolate = reasoner.getNormToViolate();
		this.normToApply = reasoner.getNormToApply();
		
		return action;
	}
}
