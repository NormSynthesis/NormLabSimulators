package es.csic.iiia.normlab.traffic.factory;

import es.csic.iiia.normlab.traffic.car.context.CarContext;
import es.csic.iiia.normlab.traffic.car.context.TrafficStateCodifier;
import es.csic.iiia.normlab.traffic.car.context.TrafficStateManager;
import es.csic.iiia.normlab.traffic.car.context.CarContext.Type;
import es.csic.iiia.normlab.traffic.car.context.TrafficStateManager.StateType;
import es.csic.iiia.normlab.traffic.map.TrafficMatrix;
import es.csic.iiia.normlab.traffic.utils.Direction;
import repast.simphony.space.grid.GridPoint;

/**
 * 
 * @author Javier Morales (jmoralesmat@gmail.com)
 *
 */
public class CarContextFactory {

	private int innerLoopXOp, innerLoopYOp, outerLoopXOp, outerLoopYOp;
	private int x1, x2, y1, y2, szX, szY;
	
	private TrafficFactFactory factFactory;
	
	/**
	 * 
	 */
	public CarContextFactory(TrafficFactFactory factFactory) {
		this.factFactory = factFactory;
	}
	
	/**
	 * Generates the scope of a car
	 * 
	 * @param carMap
	 * @param car
	 * @return
	 */
	public CarContext getCarContextIn(TrafficMatrix matrix,
			long carId, Type type) {
		
		GridPoint pos = matrix.getCarPosition(carId);
		Direction dir = matrix.getCarHeading(carId);
		CarContext context = new CarContext(type);

		szX = matrix.getNumCols();
		szY = matrix.getNumRows();
		
		innerLoopXOp = 0;
		innerLoopYOp = 0;
		outerLoopXOp = 0;
		outerLoopYOp = 0;
		x1 = 0;
		x2 = 0;
		y1 = 0;
		y2 = 0;		

		// Rotate if it is left or right scope
		switch(context.getType()) 
		{
		case Left:
			dir = dir.getTurnedDirectionIn90DegSteps(3);
			break;

		case Right:
			dir = dir.getTurnedDirectionIn90DegSteps(1);
			break;
			
		default:
			break;
		}

		// Compute coordinates
		generateCoordinates(dir, pos);

		// Obtain sub map
		int x = x1, y = y1;
		int col = 0, row = CarContext.NUM_ROWS - 1;

		for(;;){
			col = 0;
			if(innerLoopXOp!=0) {
				x=x1;
			} else {
				y=y1;
			}

			for(;;) {
				if(x>=0 && x<szX && y>=0 && y<szY)
				{
					// Cars must not perceive collisions (by now)
					String codDesc = matrix.get(y, x);
					StateType stateType = TrafficStateManager.getType(codDesc);
					if(stateType != StateType.Collision && stateType != StateType.ViolCollision)
					{
						context.set(row, col, codDesc);
					}						
				}
				
				/* Agent's context out of bounds */
				else {
					return null;
				}
				
				if((innerLoopXOp != 0 && x==x2)|| innerLoopYOp != 0 && y == y2)
					break;
				x+=innerLoopXOp;
				y+=innerLoopYOp;
				col++;
			}
			row++;
			if(x==x2&&y==y2)
				break;

			x+=outerLoopXOp;
			y+=outerLoopYOp;
		}

		// 1. Cars cannot perceive other cars ID's -> Remove car ID's
		// 2. Rotate car headings to make them relative to the reference car 
		for(row=0; row<context.getNumRows(); row++) {
			for(col=0; col<context.getNumCols(); col++) {
				String codDesc = context.get(row, col);
				
				if(TrafficStateManager.getType(codDesc) == TrafficStateManager.StateType.Car) {
					
					/* Remove Id */
//					codDesc = TrafficStateManager.setCarId(codDesc, 0);

					/* Rotate car heading */
					String desc = TrafficStateCodifier.decodify(codDesc);
					Direction otherCarHeading = Direction.fromArrow(desc);
					int steps = dir.getRotationStepsRelativeTo(otherCarHeading);
					Direction newHeading = Direction.North.getTurnedDirectionIn90DegSteps(steps);
					
					codDesc = TrafficStateManager.setCarHeading(codDesc, newHeading);
					context.set(row, col, codDesc);
				}
			}
		}
		context.pack(this.factFactory);
		
		return context;
	}

	/**
	 * 
	 * @param car
	 * @param pos
	 */
	private void generateCoordinates(Direction dir, GridPoint pos) 
	{
		int numRows = CarContext.NUM_ROWS;
		int numCols = CarContext.NUM_COLS; 
				
		// Calculate coordinates
		switch(dir) {
		case North:
			x1 = pos.getX() - numCols/2;
			x2 = x1 + numCols - 1;
			y1 = pos.getY() - 1;
			y2 = pos.getY() - numRows;

			innerLoopXOp = 1;
			outerLoopYOp = 1;
			break;

		case South:
			x1 = pos.getX() + numCols/2;
			x2 = x1 - numCols + 1; 
			y1 = pos.getY() + 1;
			y2 = pos.getY() + numRows;

			innerLoopXOp = -1;
			outerLoopYOp = 1;
			break;
			
		case East:
			x1 = pos.getX() + 1;
			x2 = pos.getX() + numRows;
			y1 = pos.getY() - numCols/2;
			y2 = y1 + numCols - 1;

			innerLoopYOp = 1;
			outerLoopXOp = 1;
			break;

		case West:
			x1 = pos.getX() - 1;
			x2 = pos.getX() - numRows;
			y1 = pos.getY() + numCols/2;
			y2 = y1 - numCols + 1;

			innerLoopYOp = -1;
			outerLoopXOp = 1;
			break;
		}
	}
}
