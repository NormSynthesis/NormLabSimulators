package es.csic.iiia.normlab.traffic;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import repast.simphony.context.Context;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.InfiniteBorders;
import repast.simphony.space.grid.RandomGridAdder;
import repast.simphony.valueLayer.GridValueLayer;
import es.csic.iiia.normlab.traffic.TrafficSimulator;
import es.csic.iiia.normlab.traffic.agent.TrafficElement;
import es.csic.iiia.normlab.traffic.agent.TrafficLight;
import es.csic.iiia.normlab.traffic.agent.Wall;
import es.csic.iiia.normlab.traffic.utils.Direction;

/**
 * Interface to simphony runtime environment. Inject agents and system setup here
 *
 * @author Javier Morales (jmoralesmat@gmail.com)
 *
 */
public class TrafficContextCreator implements ContextBuilder<TrafficElement> {

	//-------------------------------------------------------------------------
	// Attributes
	//-------------------------------------------------------------------------
	
	private final float ROAD_POSITION = 0f;
	private final float WALL_POSITION = 1f;

	private GridFactory gridFactory;
	private InfiniteBorders<TrafficElement> iBorders;
	private RandomGridAdder<TrafficElement> gridAdder;

	//-------------------------------------------------------------------------
	// Methods
	//-------------------------------------------------------------------------
	
	/**
	 * 
	 */
	public TrafficContextCreator()	{
		this.gridFactory = GridFactoryFinder.createGridFactory(null);
		this.iBorders = new InfiniteBorders<TrafficElement>();
		this.gridAdder = new RandomGridAdder<TrafficElement>();
	}

	/**
	 * Builds and returns a context. Building a context consists of filling it
	 * with agents, adding projects and so forth. When this is called for the
	 * master context the system will pass in a created context based on
	 * information given in the model.score file. When called for sub-contexts,
	 * each sub-context that was added when the master context was built
	 * will be passed in.
	 *
	 * @param context
	 * @return the built context
	 */
	public  Context<TrafficElement> build(Context<TrafficElement> context)
	{
		Parameters p = RunEnvironment.getInstance().getParameters();
		Grid<TrafficElement> grid = null;
		int mapToUse = (Integer)p.getValue("SimMap");

		try {
			
			String usrDir = System.getProperty("user.dir");
//			System.out.println(usrDir);
			// Open map file
			FileInputStream file = new FileInputStream("maps/" + mapToUse + ".map");
			DataInputStream input = new DataInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));

			// Read map dimensions
			int xDim = Integer.valueOf(reader.readLine());
			int yDim = Integer.valueOf(reader.readLine());

			// Create empty map (grid)
			grid = this.createEmptyMap(context, xDim, yDim, "trafficMap");

			// Create value layer 
			GridValueLayer vLayer = new GridValueLayer("trafficMapPositions", true, iBorders, xDim, yDim);
			this.fill(grid, vLayer, context, reader);
			context.addValueLayer(vLayer);

			//Close the input stream
			input.close();
			reader.close();
			file.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Add main agent to the simulation
		TrafficSimulator mainAgent = new TrafficSimulator(context, grid);

		context.add(mainAgent);
		grid.moveTo(mainAgent, 0,0);

		return context;


	}

	/**
	 * Creates an empty map (grid)
	 * 
	 * @return
	 */
	public Grid<TrafficElement> createEmptyMap(Context<TrafficElement> context, int xDim, int yDim, String name) {
		GridBuilderParameters<TrafficElement> parameters = 
				new GridBuilderParameters<TrafficElement>(iBorders,gridAdder, true, xDim, yDim);

		Grid<TrafficElement> map = gridFactory.createGrid(name, context, parameters);

		return map;
	}

	/**
	 * 
	 * @param map
	 * @param vLayer
	 * @param reader
	 */
	private void fill(Grid<TrafficElement> map, GridValueLayer vLayer, 
			Context<TrafficElement> context, BufferedReader reader)
	{
		TrafficElement element = null;
		String strLine;

		char pos;
		int x;
		int y = map.getDimensions().getHeight()-1;

		try
		{
			// Fill map and value layer
			while ((strLine = reader.readLine()) != null) 
			{
				for(x=0; x<strLine.length(); x++)
				{
					pos = strLine.charAt(x);
					element = null;

					switch(pos)
					{
					// Set wall in value layer
					case 'O':
						element = new Wall(x,y);
						vLayer.set(WALL_POSITION, x, y);
						break;

						// Add traffic light for north
					case 'N':
						element = new TrafficLight(x, y, 1, Direction.North);
						break;

						// Add traffic light for east
					case 'E':
						element = new TrafficLight(x, y, 2, Direction.East);
						break;

						// Add traffic light for south
					case 'S':
						element = new TrafficLight(x, y, 3, Direction.South);
						break;

						// Add traffic light for west
					case 'W':
						element = new TrafficLight(x, y, 4, Direction.West);
						break;

					default:
						vLayer.set(ROAD_POSITION, x, y);
					}

					// Add element and move to its position in the map (grid)
					if(element != null)
					{
						context.add(element);
						map.moveTo(element, x, y);
					}
				}
				y--;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}