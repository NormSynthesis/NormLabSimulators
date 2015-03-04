package es.csic.iiia.normlab.traffic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import repast.simphony.annotate.AgentAnnot;
import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.grid.Grid;
import es.csic.iiia.normlab.traffic.agent.DefaultTrafficNormSynthesisAgent;
import es.csic.iiia.normlab.traffic.agent.TrafficElement;
import es.csic.iiia.normlab.traffic.agent.TrafficNormSynthesisAgent;
import es.csic.iiia.normlab.traffic.agent.monitor.TrafficCamera;
import es.csic.iiia.normlab.traffic.agent.monitor.TrafficCameraPosition;
import es.csic.iiia.normlab.traffic.examples.ex1.TrafficNSExample1_NSAgent;
import es.csic.iiia.normlab.traffic.examples.ex2.TrafficNSExample2_NSAgent;
import es.csic.iiia.normlab.traffic.examples.ex3.TrafficNSExample3_NSAgent;
import es.csic.iiia.normlab.traffic.examples.ex4.TrafficNSExample4_NSAgent;
import es.csic.iiia.normlab.traffic.examples.ex5.TrafficNSExample5_NSAgent;
import es.csic.iiia.normlab.traffic.factory.CarContextFactory;
import es.csic.iiia.normlab.traffic.factory.TrafficFactFactory;
import es.csic.iiia.normlab.traffic.map.CarMap;
import es.csic.iiia.normlab.traffic.metrics.TrafficMetrics;
import es.csic.iiia.normlab.traffic.normsynthesis.TrafficDomainFunctions;
import es.csic.iiia.normlab.traffic.normsynthesis.TrafficNormSynthesisSettings;
import es.csic.iiia.nsm.IncorrectSetupException;
import es.csic.iiia.nsm.agent.language.PredicatesDomains;
import es.csic.iiia.nsm.agent.language.TaxonomyOfTerms;
import es.csic.iiia.nsm.config.DomainFunctions;
import es.csic.iiia.nsm.norm.Norm;

/**
 * Scene Manager - Main class of implementation. Controls cooperation
 * of all the components, agent generation etc.
 *
 * @author Javier Morales (jmoralesmat@gmail.com)
 *
 */
@AgentAnnot(displayName = "Main Agent")
public class TrafficSimulator implements TrafficElement {

	//-----------------------------------------------------------------
	// Static
	//-----------------------------------------------------------------

	private static Random random;
	private static CarMap carMap = null;
	private static TrafficFactFactory factFactory;
	private static CarContextFactory carContextFactory;
	private static TrafficNormSynthesisSettings config;
	public static long tick = 0;
	
	//-----------------------------------------------------------------
	// Attributes
	//-----------------------------------------------------------------

	private PredicatesDomains predDomains;
	private TrafficNormSynthesisAgent normSynthesisAgent;
	private TrafficMetrics trafficMetrics;
	private List<TrafficCamera> trafficCameras;
	
	//-----------------------------------------------------------------
	// Methods
	//-----------------------------------------------------------------

	/**
	 * Constructor
	 * 
	 * @param grid
	 * @param context
	 * @param valueLayer
	 * @param map
	 */
	public TrafficSimulator(Context<TrafficElement> context, Grid<TrafficElement> map) {
		long seed = System.currentTimeMillis();
		TrafficNormSynthesisSettings.init();

		// Set the defined seed only if the simulation is not batch and the seed is != 0
		if(TrafficNormSynthesisSettings.SIM_RANDOM_SEED != 0l) {
			seed = TrafficNormSynthesisSettings.SIM_RANDOM_SEED;
		}
		
		/* Create traffic predicates and their domains */
		this.createPredicatesDomains();
		
		factFactory = new TrafficFactFactory(this.predDomains);
		carContextFactory = new CarContextFactory(factFactory);
		carMap = new CarMap(context, map, predDomains,
				carContextFactory, factFactory);
		
		/* Create norm synthesis elements */
		this.createMonitorAgents();
		this.createNormSynthesisStuff(seed);
		
		random = this.normSynthesisAgent.getNormSynthesisMachine().getRandom();

		/* Create traffic metrics */
		this.trafficMetrics = (TrafficMetrics) this.normSynthesisAgent.getMetrics();

		System.out.println("\nStarting simulation with random seed = " + seed);
	}

	/**
	 * Step method. Controls everything in the simulation
	 */
	@ScheduledMethod(start=1, interval=1, priority=2)
	public void step() {

		if(tick++ > 0) {
//			System.out.println("\n==================== Tick: " + tick +  " =====================");

			// 1. Move cars, emit cars and handle new collisions
			carMap.step();

			for(TrafficCamera camera : this.trafficCameras) {
				camera.perceive(carMap);
			}

			try {
				this.normSynthesisAgent.step(tick);
			}
			catch (IncorrectSetupException e) {
				System.err.println("FATAL error, end of simulation...");
				e.printStackTrace();
				RunEnvironment.getInstance().endRun();
			}

			// Norm broadcast
			for(Norm norm : this.normSynthesisAgent.getAddedNorms()) {
				carMap.broadcastAddNorm(norm);
			}

			for(Norm norm : this.normSynthesisAgent.getRemovedNorms()) {
				carMap.broadcastRemoveNorm(norm);
			}
		}

		//		this.trafficMetrics.update();

		// Step resume
//		this.printStepResume();

//		System.out.println("\n======================= End Step ========================\n");

		// Stop simulation if required update
		if(tick >= TrafficNormSynthesisSettings.SIM_MAX_TICKS || this.mustStop())
		{
			System.out.println("End of simulation");
			RunEnvironment.getInstance().endRun();

//			trafficMetrics.print();
			trafficMetrics.save();
		}
	}


	/**
	 * Creates the traffic institutions
	 */
	private void createMonitorAgents() {
		this.trafficCameras = new ArrayList<TrafficCamera>();

		for(TrafficCameraPosition pos : TrafficCameraPosition.getPossiblePositions()) {
			this.trafficCameras.add(new TrafficCamera(pos, carMap));
		}
	}

	/**
	 * Creates the norm synthesis agent
	 */
	private void createNormSynthesisStuff(long randomSeed) {
		
		DomainFunctions dmFunctions = new TrafficDomainFunctions(predDomains, 
				TrafficSimulator.getCarContextFactory());
		
		TrafficNormSynthesisSettings nsSettings = 
				new TrafficNormSynthesisSettings();
		
		switch(TrafficNormSynthesisSettings.NORM_SYNTHESIS_EXAMPLE) {
		case 0:
			this.normSynthesisAgent = new	DefaultTrafficNormSynthesisAgent(
					this.trafficCameras, predDomains, dmFunctions,
					nsSettings, randomSeed);
			break;
			
		case 1:
			this.normSynthesisAgent = new	TrafficNSExample1_NSAgent(
					nsSettings, this.predDomains,	dmFunctions);
			break;

		case 2:
			this.normSynthesisAgent = new	TrafficNSExample2_NSAgent(
					nsSettings, this.predDomains,	dmFunctions);
			break;

		case 3:
			this.normSynthesisAgent = new	TrafficNSExample3_NSAgent(
					nsSettings, this.predDomains, dmFunctions, this.trafficCameras);
			break;
			
		case 4:
			this.normSynthesisAgent = new	TrafficNSExample4_NSAgent(
					nsSettings, this.predDomains, dmFunctions, this.trafficCameras);
			break;

		case 5:
			this.normSynthesisAgent = new	TrafficNSExample5_NSAgent(
					nsSettings, this.predDomains, dmFunctions, this.trafficCameras);
			break;			
		}
	}
	
	/**
	 * Creates the agent language. That is, the predicates, and their domains 
	 */
	private void createPredicatesDomains() {
		
		/* Predicate "left" domain */
		TaxonomyOfTerms leftPredTaxonomy = new TaxonomyOfTerms("l");
		leftPredTaxonomy.addTerm("*");
		leftPredTaxonomy.addTerm("<");
		leftPredTaxonomy.addTerm(">");
		leftPredTaxonomy.addTerm("-");
		leftPredTaxonomy.addRelationship("<", "*");
		leftPredTaxonomy.addRelationship(">", "*");
		leftPredTaxonomy.addRelationship("-", "*");

		/* Predicate "front" domain*/
		TaxonomyOfTerms frontPredTaxonomy = new TaxonomyOfTerms("f", leftPredTaxonomy);
		frontPredTaxonomy.addTerm("^");
		frontPredTaxonomy.addRelationship("^", "*");

		/* Predicate "right" domain*/
		TaxonomyOfTerms rightPredTaxonomy = new TaxonomyOfTerms("r", leftPredTaxonomy);
		rightPredTaxonomy.addTerm("w");
		rightPredTaxonomy.addRelationship("w", "*");

		this.predDomains = new PredicatesDomains();
		this.predDomains.addPredicateDomain("l", leftPredTaxonomy);
		this.predDomains.addPredicateDomain("f", frontPredTaxonomy);
		this.predDomains.addPredicateDomain("r", rightPredTaxonomy);
	}

	//-----------------------------------------------------------------
	// Other
	//-----------------------------------------------------------------

	/**
	 * 
	 */
	private boolean mustStop() {
//		if(tick < 14000) { // TODO: Min num ticks to check convergence
//			return false;
//		}
		return this.trafficMetrics.hasConverged();
	}

//	/**
//	 * Prints information about metrics in the current step
//	 */
//	private void printStepResume() {
//		System.out.println("\nSTEP RESUME");
//		System.out.println("----------------------------");
//		System.out.println("Step: " + tick);
//	}

	//-----------------------------------------------------------------
	// Static methods
	//-----------------------------------------------------------------

	/**
	 * 
	 * @return
	 */
	public static long getTick() {
		return tick;
	}

	/**
	 * 
	 * @return
	 */
	public static Random getRandom() {
		return random;
	}

	/**
	 * 
	 * @return
	 */
	public static CarMap getMap() {
		return carMap;
	}

	/**
	 * 
	 */
	public static TrafficNormSynthesisSettings getConfig() {
		return config;
	}

	@Override
	public int getX() {
		return -1;
	}

	@Override
	public int getY() {
		return -1;
	}

	@Override
	public void move() {

	}

	public static CarContextFactory getCarContextFactory() {
		return carContextFactory;
	}
	
	public static TrafficFactFactory getFactFactory() {
		return factFactory;
	}
}

