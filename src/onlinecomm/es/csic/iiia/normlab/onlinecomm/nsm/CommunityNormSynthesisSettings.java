package es.csic.iiia.normlab.onlinecomm.nsm;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import es.csic.iiia.nsm.NormSynthesisMachine.NormGeneralisationMode;
import es.csic.iiia.nsm.config.Dimension;
import es.csic.iiia.nsm.config.Goal;
import es.csic.iiia.nsm.config.NormSynthesisSettings;

/**
 * 
 * Class to make the necessary iron configurations.
 * 
 * @author Iosu Mendizabal
 *
 */
public class CommunityNormSynthesisSettings implements NormSynthesisSettings {

	/* Random seed */
	public static int SIM_RANDOM_SEED;

	//-----------------------------------------------------------------
	// Goals and norm evaluation settings
	//-----------------------------------------------------------------

	/* Goals of the system */
	private static List<Goal> systemGoals;

	/* Learning rate of the method */
	private static float LEARNING_RATE = 0.2f;

	/* Path to output files */
	public static String SIM_DATA_PATH = "output/onlinecomm/";

	/* File to output all norms */
	public static String SIM_TOTAL_NORMS_FILE = "TotalNorms";

	/* File to output the final norms upon convergence */
	public static String SIM_FINAL_NORMS_FILE = "FinalNorms";

	/* Name of the file that tracks the normative systems synthesised upon coonvergence */
	public static String SIM_FINAL_NORMSETS_FILE = "NormativeSystems";

	/* Norm violation rate of the agents */
	public static float SIM_NORM_VIOLATION_RATE;

	/* Size of the contents queue */
	public static long CONTENTS_QUEUE_SIZE;
	
	//-----------------------------------------------------------------
	// Norm Synthesis settings
	//-----------------------------------------------------------------

	/* Number of agents in the simulation */ 
	public static int SIM_NUM_AGENTS;

	/* Norm synthesis strategy */
	public static int NORM_SYNTHESIS_STRATEGY;

	/* Is norm generation highly reactive to conflicts? */
	public static boolean NORM_GENERATION_REACTIVE;

	/* Norm generalisation mode */
	public static int NORM_GENERALISATION_MODE;

	/* Norm generalisation step */
	public static int NORM_GENERALISATION_STEP;

	/* The effectiveness boundary to generalise norms */
	public static double NORM_GEN_EFF_THRESHOLD;

	/* The necessity boundary to generalise norms */
	public static double NORM_GEN_NEC_THRESHOLD;

	/* The effectiveness boundary to specialise norms */
	public static double NORM_SPEC_EFF_THRESHOLD;

	/* The necessity boundary to specialise norms */
	public static double NORM_SPEC_NEC_THRESHOLD;

	/* Length of the cumulative moving average for norms' performance */
	private static int NORM_UTILITY_WINDOW_SIZE;

	/* Norms' default utility when created */
	private static double NORM_DEFAULT_UTILITY;

	/* Epsilon to build the deactivation/activation band for norms */
	public static double NORM_SPEC_THRESHOLD_EPSILON;

	/* Number of ticks of stability to converge */
	private static long NUM_TICKS_TO_CONVERGE;

	/* */
	public static int NORMS_MIN_EVALS_CLASSIFY;

	/* Norms include predicate usr(id) or not? */
	public static boolean NORMS_WITH_USER_ID;

	//---------------------------------------------------------------------------
	// Methods
	//---------------------------------------------------------------------------

	/**
	 * 
	 */
	public CommunityNormSynthesisSettings() {
		init();
	}
	
	/**
	 * Constructor of the social configuration.
	 */
	public static void init(){		
		Parameters p = RunEnvironment.getInstance().getParameters();

		SIM_RANDOM_SEED = (Integer)p.getValue("randomSeed");
		SIM_NUM_AGENTS =  (Integer)p.getValue("maxAgents");
		SIM_NORM_VIOLATION_RATE = (Float) p.getValue("Norm Violation Rate");
		NUM_TICKS_TO_CONVERGE = (Long)p.getValue("NumTicksToConverge");
		CONTENTS_QUEUE_SIZE = (Long)p.getValue("ContentsQueueSize");
		
		NORM_GEN_EFF_THRESHOLD = (Double)p.getValue("NormsGenEffThreshold");
		NORM_GEN_NEC_THRESHOLD = (Double)p.getValue("NormsGenNecThreshold");
		NORM_SPEC_EFF_THRESHOLD = (Double)p.getValue("NormsSpecEffThreshold");
		NORM_SPEC_NEC_THRESHOLD = (Double)p.getValue("NormsSpecNecThreshold");
		NORM_UTILITY_WINDOW_SIZE = (Integer)p.getValue("NormsPerfRangeSize");
		NORM_DEFAULT_UTILITY = (Double)p.getValue("NormsDefaultUtility");

		NORM_SYNTHESIS_STRATEGY = (Integer)p.getValue("NormSynthesisStrategy");
		NORM_GENERATION_REACTIVE = (Boolean)p.getValue("NormGenerationReactive");
		NORM_GENERALISATION_MODE = (Integer)p.getValue("NormGeneralisationMode");
		NORM_GENERALISATION_STEP = (Integer)p.getValue("NormGeneralisationStep");
		NORM_SPEC_THRESHOLD_EPSILON = (Double)p.getValue("NormsSpecThresholdEpsilon");
		NORMS_MIN_EVALS_CLASSIFY = (Integer)p.getValue("NormsMinEvaluationsToClassify");
		NORMS_WITH_USER_ID = (Boolean)p.getValue("NormsWithUserId");

		
		
		// System goals and their constants
		systemGoals = new ArrayList<Goal>();
		systemGoals.add(new GComplaints());

		//		double tMinusEpsilon = NORM_SPEC_NEC_THRESHOLD - NORM_SPEC_THRESHOLD_EPSILON;
		//		
		//		/* For SIMON+ and LION, set default utility in a different manner... */
		//		if(NORM_SYNTHESIS_STRATEGY == 3 || NORM_SYNTHESIS_STRATEGY == 4) {
		//			NORM_DEFAULT_UTILITY = (float)(tMinusEpsilon * (NORM_MIN_EVALS+1)); 
		//		}

		/* For SIMON+ and LION, set default utility in a different manner... */
		if((NORM_SYNTHESIS_STRATEGY == 3 || NORM_SYNTHESIS_STRATEGY == 4) &&
				!NORM_GENERATION_REACTIVE) 
		{
			NORM_DEFAULT_UTILITY = 0f; 
			System.out.println("Norm generation is set as Deliberative");
		}
	}

	/**
	 * Getter of the system goals.
	 * 
	 * @return ArrayList of goals.
	 */
	public List<Goal> getSystemGoals() {
		return systemGoals;
	}

	/**
	 * Getter of the NumTicksPerceptionWindow
	 * 
	 * @return 0
	 */
	public int getNumTicksPerceptionWindow() {
		return 0;
	}

	/**
	 * Method to use norms tracer.
	 * 
	 * @return true.
	 */
	public boolean useNormsTracer() {
		return true;
	}

	/**
	 * Method to show all norms score charts.
	 * 
	 * @return false.
	 */
	public boolean showAllNormsScoreChart() {
		return false;
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
	public float getGeneralisationBoundary(Dimension dim, Goal goal) {
		if(dim == Dimension.Effectiveness) {
			return (float)CommunityNormSynthesisSettings.NORM_GEN_EFF_THRESHOLD;
		}
		else if(dim == Dimension.Necessity) {
			return (float)CommunityNormSynthesisSettings.NORM_GEN_NEC_THRESHOLD;
		}
		return 0f;
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
	public NormGeneralisationMode getNormGeneralisationMode() {		
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
		return NORM_GENERALISATION_STEP;
	}

	/**
	 * 
	 */
	@Override
	public String getNormSynthesisStrategy() {

		switch(NORM_SYNTHESIS_STRATEGY) {
		case 1:
			return "IRON";

		case 2:
			return "SIMON";

		case 3: 
			return "DON-SIMON";

		case 4:
			return "LION";

		default:
			return "Custom";
		}
	}

	/**
	 * 
	 */
	@Override
	public float getSpecialisationBoundary(Dimension dim, Goal goal) {

		if(dim == Dimension.Effectiveness) {
			return (float)CommunityNormSynthesisSettings.NORM_SPEC_EFF_THRESHOLD;
		}
		else if(dim == Dimension.Necessity) {
			return (float)CommunityNormSynthesisSettings.NORM_SPEC_NEC_THRESHOLD;
		}
		return 0f;
	}

	/**
	 * 
	 */
	public float getSpecialisationBoundaryEpsilon(Dimension dim, Goal goal) {
		return (float)NORM_SPEC_THRESHOLD_EPSILON;
	}

}
