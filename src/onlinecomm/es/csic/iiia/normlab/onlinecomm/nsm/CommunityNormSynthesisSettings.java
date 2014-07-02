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

	/**
	 * 
	 */
	public static String SIM_DATA_PATH = "output/onlinecomm/";

	/**
	 * 
	 */
	public static String SIM_TOTAL_NORMS_FILE;

	/**
	 * 
	 */
	public static double NORM_VIOLATION_RATE_DOUBLE;

	/**
	 * 
	 */
	public static float NORM_VIOLATION_RATE_FLOAT;

	/**
	 * 
	 */
	public static long SIM_NUM_TICKS_CONVERGENCE = 1000;


	/**
	 * The minimum score for a norm
	 */
	private static double NORM_DEFAULT_UTILITY = 0.5f;

	/**
	 * Learning rate of the method
	 */
	private static float LEARNING_RATE = 0.2f;

	/**
	 * Dimensions to evaluate 
	 */
	public static List<Dimension> systemDimensions;

	/**
	 * Goals of the system
	 */
	public static List<Goal> systemGoals;

	/**
	 * Constructor of the social configuration.
	 */
	public CommunityNormSynthesisSettings(){		

		if(RunEnvironment.getInstance().isBatch()){
			//Get the violation rate from the repast initial parameters.
			Parameters params = RunEnvironment.getInstance().getParameters();
			NORM_VIOLATION_RATE_DOUBLE = (Double) params.getValue("Norm Violation Rate");	
		}else{
			//Get the violation rate from the repast initial parameters.
			Parameters params = RunEnvironment.getInstance().getParameters();
			NORM_VIOLATION_RATE_FLOAT = (Float) params.getValue("Norm Violation Rate");	
		}


		// System evaluation dimensions
		systemDimensions = new ArrayList<Dimension>();
		systemDimensions.add(Dimension.Effectiveness);
		systemDimensions.add(Dimension.Necessity);

		// System goals and their constants
		systemGoals = new ArrayList<Goal>();
		systemGoals.add(new GComplaints());

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
	 * Getter of the system dimensions
	 * 
	 * @return ArrayList of dimensions.
	 */
	public List<Dimension> getSystemDimensions() {
		return systemDimensions;
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

	@Override
	public int getNormsPerformanceRangesSize() {
		return 200;
	}

	@Override
	public float getGeneralisationBoundary(Dimension dim, Goal goal) {
		return 0.3f;
	}

	@Override
	public float getSpecialisationBoundary(Dimension dim, Goal goal) {
		return 0.1f;
	}

	@Override
	public long getNumTicksOfStabilityForConvergence() {
		return SIM_NUM_TICKS_CONVERGENCE;
	}

	@Override
  public NormGeneralisationMode getNormGeneralisationMode() {
	  return NormGeneralisationMode.Deep;
  }

	@Override
  public int getNormGeneralisationStep() {
	  return 1;
  }

	@Override
  public String getNormSynthesisStrategy() {
	  return "SIMON";
  }
}
