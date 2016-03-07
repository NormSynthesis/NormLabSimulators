package es.csic.iiia.normlab.traffic.normsynthesis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import es.csic.iiia.normlab.traffic.config.Gcols;
import es.csic.iiia.normlab.traffic.config.NormEvaluationConstants;
import es.csic.iiia.nsm.NormSynthesisMachine.NormGeneralisationMode;
import es.csic.iiia.nsm.config.Dimension;
import es.csic.iiia.nsm.config.Goal;
import es.csic.iiia.nsm.config.NormSynthesisSettings;

/**
 * Basic configuration for the traffic simulator and the norm synthesis process
 * 
 * @author "Javier Morales (jmorales@iiia.csic.es)"
 *
 */
public class TrafficNormSynthesisSettings implements NormSynthesisSettings {

	public static int NUM_EXEC;

	//-----------------------------------------------------------------
	// Graphical User Interface settings
	//-----------------------------------------------------------------

	public static final float ROAD_POSITION = 0f;
	public static final float WALL_POSITION = 1f;

	//-----------------------------------------------------------------
	// Goals and norm evaluation settings
	//-----------------------------------------------------------------

	/* Goals of the system */
	private static List<Goal> systemGoals;

	/* Constants to define the importance of each goal */
	private static HashMap<Goal, Float> GOAL_CONSTANTS;

	/* Constants for the evaluation of norms in terms of each goal */
	private static HashMap<Goal, NormEvaluationConstants> 
	GOAL_EVALUATION_CONSTANTS;

	/* Learning rate of the method */
	private static float LEARNING_RATE = 0.2f;

	//-----------------------------------------------------------------
	// Simulator settings
	//-----------------------------------------------------------------

	/* Number of extra ticks to evaluate the normative system after convergence */
	public static int SIM_NUM_TICKS_NORMSET_EVAL = 5000;

	/* Defines if the simulator uses the advanced GUI */
	public static boolean SIM_ADVANCED_GUI = false;

	/* Frequency for cars to enter in the scenario */
	public static int SIM_NEW_CARS_FREQUENCY;

	/* Number of cars to emit at the same time */
	public static int SIM_NUM_CARS_TO_EMIT;

	/* Random seed */
	public static int SIM_RANDOM_SEED = 0;

	/* Defines if the simulator uses a GUI */
	public static boolean SIM_GUI;

	/* Max ticks to simulate */
	public static long SIM_MAX_TICKS;

	/* Use traffic lights or not? */
	public static boolean SIM_USE_TRAFFIC_LIGHTS;

	/* Use just traffic lights? */
	public static boolean SIM_ONLY_TRAFFIC_LIGHTS;

	/* Path for data */
	public static final String SIM_DATA_PATH = "output/traffic";

	/* Name for the norms file. This file saves the resulting
	 * norms in different executions */
	public static final String SIM_TOTAL_NORMS_FILE = "TrafficNormsTotal.dat";

	/* Name for the norms file. This file saves the
	 * resulting norms in different executions */
	public static final String SIM_FINAL_NORMS_FILE = "TrafficNormsFinal.dat";

	/* Name for the norm sets file. This file saves the
	 * resulting norm sets in different executions */
	public static final String SIM_FINAL_NORMSETS_FILE = "TrafficNormSetsFinal.dat";

	/* Probability to violate a norm */
	public static double SIM_NORM_VIOLATION_RATE = 0;

	//-----------------------------------------------------------------
	// Norm Synthesis settings
	//-----------------------------------------------------------------

	/* Traffic norm synthesis agent */
	public static int NORM_SYNTHESIS_EXAMPLE;

	/* Traffic norm synthesis strategy */
	public static int NORM_SYNTHESIS_STRATEGY;

	/* Traffic norm generalisation mode */
	public static int NORM_GENERALISATION_MODE;

	/* Traffic norm generalisation step */
	public static int NORM_GENERALISATION_STEP;

	/* The effectiveness boundary to generalise norms */
	public static double NORM_GEN_EFF_THRESHOLD;

	/* The necessity boundary to generalise norms */
	public static double NORM_GEN_NEC_THRESHOLD;

	/* The effectiveness boundary to specialise norms */
	public static double NORM_SPEC_EFF_THRESHOLD;

	/* The necessity boundary to specialise norms */
	public static double NORM_SPEC_NEC_THRESHOLD;

	/* Length of historic sliding window that is summed for metrics */
	private static int NORM_UTILITY_WINDOW_SIZE;

	/* Norms' default utility when created */
	private static double NORM_DEFAULT_UTILITY;

	/* Number of ticks of stability to converge */
	private static long NUM_TICKS_TO_CONVERGE;

	/* Weight that captures the importance of car collisions */
	private static int NORM_WEIGHT_COL;

	/* Weight that captures the importance of cars not colliding */
	private static int NORM_WEIGHT_NO_COL;

	/* Weight that captures the importance of fluid traffic */
	private static int NORM_WEIGHT_FLUID_TRAFFIC;

	/* */
	public static double NORM_SPEC_THRESHOLD_EPSILON;
	
	/* */
	public static int NORM_MIN_EVALS;
	
	//-----------------------------------------------------------------
	// Methods
	//-----------------------------------------------------------------

	/**
	 * Initialises the configuration
	 */
	public static void init() {
		Parameters p = RunEnvironment.getInstance().getParameters();

		// Simulator parameters
		SIM_RANDOM_SEED = (Integer)p.getValue("randomSeed");
		SIM_NEW_CARS_FREQUENCY = (Integer)p.getValue("SimNewCarsFreq");
		SIM_NUM_CARS_TO_EMIT = (Integer)p.getValue("SimNumCarsToEnter");
		SIM_MAX_TICKS = (Long)p.getValue("SimMaxTicks");
		SIM_GUI = (Boolean)p.getValue("SimGUI");
		SIM_NORM_VIOLATION_RATE = (Double)p.getValue("SimNormViolationRate");

		// Norm Synthesis settings
		NORM_GEN_EFF_THRESHOLD = (Double)p.getValue("NormsGenEffThreshold");
		NORM_GEN_NEC_THRESHOLD = (Double)p.getValue("NormsGenNecThreshold");
		NORM_SPEC_EFF_THRESHOLD = (Double)p.getValue("NormsSpecEffThreshold");
		NORM_SPEC_NEC_THRESHOLD = (Double)p.getValue("NormsSpecNecThreshold");

		NORM_UTILITY_WINDOW_SIZE = (Integer)p.getValue("NormsPerfRangeSize");
		NORM_DEFAULT_UTILITY = (Double)p.getValue("NormsDefaultUtility");
		NUM_TICKS_TO_CONVERGE = (Long)p.getValue("NumTicksToConverge");
		NUM_EXEC = (Integer)p.getValue("NumExec");

		NORM_SYNTHESIS_EXAMPLE = (Integer)p.getValue("NormSynthesisExample");
		NORM_SYNTHESIS_STRATEGY = (Integer)p.getValue("NormSynthesisStrategy");
		NORM_GENERALISATION_MODE = (Integer)p.getValue("NormGeneralisationMode");
		NORM_GENERALISATION_STEP = (Integer)p.getValue("NormGeneralisationStep");
		NORM_SPEC_THRESHOLD_EPSILON = (Double)p.getValue("NormsSpecThresholdEpsilon");
		NORM_MIN_EVALS = (Integer)p.getValue("NormsMinEvaluations");
		
		checkNSExamples();
		
		// System goals and their constants
		Goal gCols = new Gcols();
		systemGoals = new ArrayList<Goal>();
		systemGoals.add(gCols);

		GOAL_CONSTANTS = new HashMap<Goal, Float>();
		GOAL_CONSTANTS.put(gCols, 3f);

		// Constants for Gcols
		NormEvaluationConstants gColsConstants = new NormEvaluationConstants
				(NORM_WEIGHT_NO_COL, NORM_WEIGHT_COL, NORM_WEIGHT_FLUID_TRAFFIC, 1);

		GOAL_EVALUATION_CONSTANTS = new HashMap<Goal, NormEvaluationConstants>();
		GOAL_EVALUATION_CONSTANTS.put(gCols, gColsConstants);
		
//		double tMinusEpsilon = NORM_SPEC_NEC_THRESHOLD - NORM_SPEC_THRESHOLD_EPSILON;
//		NORM_DEFAULT_UTILITY = (float)(tMinusEpsilon * (NORM_MIN_EVALS+1));
		
		NORM_DEFAULT_UTILITY = 0.5f;
	}

	/**
	 * 
	 */
	private static void checkNSExamples() {

	  
  }

	/**
	 * 
	 * @param flag
	 */
	public synchronized static void setUseAdvancedGUI(boolean flag) {
		SIM_ADVANCED_GUI = flag;
	}

	/**
	 * 
	 * @return
	 */
	public synchronized static boolean getUseAdvancedGUI() {
		return SIM_ADVANCED_GUI;
	}

	/**
	 * 
	 */
	@Override
	public List<Goal> getSystemGoals() {
		return systemGoals;
	}

	/**
	 * 
	 */
	@Override
	public float getNormsDefaultUtility() {
		return (float)NORM_DEFAULT_UTILITY;
	}

	/**
	 * 
	 */
	@Override
	public float getNormEvaluationLearningRate() {
		return LEARNING_RATE;
	}

	/**
	 * 
	 */
	@Override
	public int getNormsPerformanceRangesSize() {
		return NORM_UTILITY_WINDOW_SIZE;
	}

	/**
	 * 
	 */
	@Override
	public long getNumTicksOfStabilityForConvergence() {
		return NUM_TICKS_TO_CONVERGE;
	}

	/**
	 * 
	 */
	@Override
	public float getGeneralisationBoundary(Dimension dim, Goal goal) {
		if(dim == Dimension.Effectiveness) {
			return (float)TrafficNormSynthesisSettings.NORM_GEN_EFF_THRESHOLD;
		}
		else if(dim == Dimension.Necessity) {
			return (float)TrafficNormSynthesisSettings.NORM_GEN_NEC_THRESHOLD;
		}
		return 0f;
	}

	/**
	 * 
	 */
	@Override
	public float getSpecialisationBoundary(Dimension dim, Goal goal) {

		if(dim == Dimension.Effectiveness) {
			return (float)TrafficNormSynthesisSettings.NORM_SPEC_EFF_THRESHOLD;
		}
		else if(dim == Dimension.Necessity) {
			return (float)TrafficNormSynthesisSettings.NORM_SPEC_NEC_THRESHOLD;
		}
		return 0f;
	}

	/**
	 * 
	 */
	public float getSpecialisationBoundaryEpsilon(Dimension dim, Goal goal) {
		return (float)NORM_SPEC_THRESHOLD_EPSILON;
	}
	
	/**
	 * 
	 */
	@Override
	public NormGeneralisationMode getNormGeneralisationMode() {
		if(NORM_SYNTHESIS_EXAMPLE > 0 && NORM_SYNTHESIS_EXAMPLE < 5) {
			return NormGeneralisationMode.None;
		}
		
		switch(NORM_GENERALISATION_MODE) {
		case 0:
			return NormGeneralisationMode.Shallow;
		default:
			return NormGeneralisationMode.Deep;
		}
	}

	/**
	 * 
	 */
	@Override
	public int getNormGeneralisationStep() {
		if(NORM_SYNTHESIS_EXAMPLE > 0 && NORM_SYNTHESIS_EXAMPLE < 5) {
			return 0;
		}
		return NORM_GENERALISATION_STEP;
	}

	/**
	 * 
	 */
	@Override
	public String getNormSynthesisStrategy() {
		
	  switch(NORM_SYNTHESIS_EXAMPLE) {
	  case 1:
	  	return "Example 1";
	  case 2:
	  	return "Example 2";
	  case 3: 
	  	return "Example 3";
	  case 4: 
	  	return "Example 4";
	  case 5: 
	  	return "Example 5";
	  }
		
		switch(NORM_SYNTHESIS_STRATEGY) {
		case 1:
			return "IRON";

		case 2:
			return "SIMON";

		case 3:
			return "LION";

		default:
			return "Custom";
		}
	}
}
