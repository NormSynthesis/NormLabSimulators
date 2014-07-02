package es.csic.iiia.normlab.traffic.metrics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import repast.simphony.annotate.AgentAnnot;
import repast.simphony.engine.environment.RunEnvironment;
import es.csic.iiia.normlab.traffic.agent.TrafficNormSynthesisAgent;
import es.csic.iiia.nsm.NormSynthesisMachine;
import es.csic.iiia.nsm.config.Dimension;
import es.csic.iiia.nsm.config.Goal;
import es.csic.iiia.nsm.config.NormSynthesisSettings;
import es.csic.iiia.nsm.metrics.NormSynthesisMetrics;
import es.csic.iiia.nsm.norm.Norm;

/**
 * Metrics manager
 * 
 * @author Javier Morales (jmorales@iiia.csic.es)
 *
 */
@AgentAnnot(displayName = "Metrics Agent")
public class TrafficMetrics {

	private NormSynthesisMachine nsm;
	private TrafficNormSynthesisAgent nsAgent;
	private NormSynthesisMetrics nsMetrics;
	private NormSetsFileManager normSetsFileManager;
	
	private double tick;
	private NormSynthesisSettings nsmSettings;
	private long numNormClauseAdditions, numNormClauseRemovals;
	private String metricsFileName = "metrics.dat";
	private int IRONConvergenceTick;

	private float NSEffAtIRONConvergenceTick;
	private float NSNecAtIRONConvergenceTick;
	private float NSMinimalityAtIRONConvergenceTick;
	private float NSSimplicityAtIRONConvergenceTick;
	
	//-----------------------------------------------------------------
	// Methods
	//-----------------------------------------------------------------

	/**
	 * Constructor
	 *  
	 * @param mainClass
	 */
	public TrafficMetrics(TrafficNormSynthesisAgent nsAgent) {
		
		this.nsAgent = nsAgent;
		this.nsm = nsAgent.getNormSynthesisMachine();
		this.nsMetrics = nsm.getNormSynthesisMetrics();
		this.nsmSettings = nsm.getNormSynthesisSettings();
		
		this.numNormClauseAdditions = 0;
		this.numNormClauseRemovals = 0;
		
		this.NSEffAtIRONConvergenceTick = 0;
		this.NSNecAtIRONConvergenceTick = 0;
		this.NSMinimalityAtIRONConvergenceTick = 0;
		this.NSSimplicityAtIRONConvergenceTick = 0;
		
		this.getIRONConvergenceTick();
	}

	/**
	 * Computes the metrics for the simulator
	 */
	public void update() 
	{
		this.tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		
		this.numNormClauseAdditions += this.computeNumClauses(this.nsMetrics.getNormsAddedThisTick());
		this.numNormClauseRemovals += this.computeNumClauses(this.nsMetrics.getNormsRemovedThisTick());
		
		if(this.tick == this.IRONConvergenceTick)
		{
			float NSeff = 0f, NSnec = 0f;
			List<Norm> NS =  this.nsAgent.getNormativeSystem();
			
			Goal goal = nsmSettings.getSystemGoals().get(0);
			for(Norm norm : NS)
			{
				float eff = this.nsMetrics.getNormUtility(norm).getScoreAverage(Dimension.Effectiveness, goal);
				float nec = this.nsMetrics.getNormUtility(norm).getScoreAverage(Dimension.Necessity, goal);
				NSeff += eff;
				NSnec += nec;
			}
			NSeff /= NS.size();
			NSnec /= NS.size();
			
			this.NSEffAtIRONConvergenceTick = NSeff;
			this.NSNecAtIRONConvergenceTick = NSnec;
			this.NSMinimalityAtIRONConvergenceTick = this.nsAgent.getMetrics().getNormativeSystemCardinality();
			this.NSSimplicityAtIRONConvergenceTick = this.computeNumClauses(NS);
		}
	}

	/**
	 * Prints the ironMetrics
	 */
	public void print() {
		System.out.print(this.resume());
	
	}
	
	/**
	 * 
	 * @return
	 */
	public String resume() {
		String s = Integer.toString((int)tick);

		float NSeff = 0f, NSnec = 0f;
		List<Norm> NS =  this.nsAgent.getNormativeSystem();
		
		Goal goal = nsmSettings.getSystemGoals().get(0);
		for(Norm norm : NS)
		{
			float eff = this.nsMetrics.getNormUtility(norm).getScoreAverage(Dimension.Effectiveness, goal);
			float nec = this.nsMetrics.getNormUtility(norm).getScoreAverage(Dimension.Necessity, goal);
			NSeff += eff;
			NSnec += nec;
		}
		NSeff /= NS.size();
		NSnec /= NS.size();
		
		s += " " + this.nsAgent.getMetrics().getNormativeSystemCardinality();
		s += " " + this.computeNumClauses(nsAgent.getNormativeSystem());
		s += " " + NSeff;
		s += " " + NSnec;
		
//		s += " " + this.nsMetrics.getNumNormAdditions();
//		s += " " + this.nsMetrics.getNumNormRemovals();
		s += " " + this.numNormClauseAdditions;
		s += " " + this.numNormClauseRemovals;
		
		s += " " + this.NSMinimalityAtIRONConvergenceTick;
		s += " " + this.NSSimplicityAtIRONConvergenceTick;
		s += " " + this.NSEffAtIRONConvergenceTick;
		s += " " + this.NSNecAtIRONConvergenceTick;
		
		s += "\n";
		
		return s;
	}

	/**
	 * Save files 
	 */
	public void save() 
	{
		File metricsFile = new File(this.metricsFileName);
		
		try {
	    BufferedWriter writer = new BufferedWriter(new FileWriter(metricsFile, true));
	    writer.write(this.resume());
	    writer.close();
    } 
		catch (IOException e) {
	    e.printStackTrace();
    }
		
		this.normSetsFileManager = new NormSetsFileManager(this, nsAgent, nsMetrics.hasConverged());
		this.normSetsFileManager.load();
		this.normSetsFileManager.save();
	}

	/**
	 * 
	 * @param NS
	 * @return
	 */
	private int computeNumClauses(List<Norm> norms)
	{
		int ret = 0;
		for(Norm norm : norms)
		{
			for(String predicate : norm.getPrecondition().getPredicates())
				if(!norm.getPrecondition().getTerms(predicate).get(0).equals("*"))
					ret++;
		}		
		return ret;
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasConverged()
	{
		return false;
//		return this.nsMetrics.hasConverged();
	}
	
	/**
	 * 
	 */
	private void getIRONConvergenceTick()
	{
//		File IRONConvergenceTicksFile = new File("IRONConvergenceTicks");
//		
//		try {
//	    BufferedReader reader = new BufferedReader(new FileReader(IRONConvergenceTicksFile));
//	    
//	    int i=0;
//	    do{
//	    	IRONConvergenceTick = Integer.parseInt(reader.readLine());
//	    	i++;
//	    }while(i != TrafficNormSynthesisSettings.NUM_EXEC);
//	    
//	    	reader.close();
//    } 
//		catch (IOException e) {
//	    e.printStackTrace();
//    }
	}
	
	//-----------------------------------------------------------------
	// Number of cases
	//-----------------------------------------------------------------

	/**
	 * Returns the current number of cases name
	 * 
	 * @return
	 */
	public String getNumberOfCasesName(){
		return "Cases";
	}

	/**
	 * Returns the current number of cases
	 * 
	 * @return
	 */
	public int getNumberOfCases(){
		return 0;
	}

	//-----------------------------------------------------------------
	// Number of solutions
	//-----------------------------------------------------------------

	/**
	 * Returns the current number of solutions name
	 * 
	 * @return
	 */
	public String getNumberOfSolutionsName(){
		return "NumSolutions";
	}

	/**
	 * Returns the current number of solutions
	 * 
	 * @return
	 */
	public int getNumberOfSolutions(){
		return 0;
	}

	//-----------------------------------------------------------------
	// Number of norms
	//-----------------------------------------------------------------

	/**
	 * Returns the current number of norms name
	 * 
	 * @return
	 */
	public String getNumberOfNormsName() {
		return "Norms generated";
	}

	/**
	 * Returns the current number of norms
	 * 
	 * @return
	 */
	public int getNumberOfNorms() {
		return 0;
	}

	//-----------------------------------------------------------------
	// Number of active norms
	//-----------------------------------------------------------------

	/**
	 * Returns the current number of active norms name
	 * 
	 * @return
	 */
	public String getNumberOfActiveNormsName() {
		return "Active norms";
	}

	/**
	 * Returns the current number of active norms
	 * 
	 * @return
	 */
	public int getNumberOfActiveNorms() {
		return 0;
	}


	//-----------------------------------------------------------------
	// Average number of collisions
	//-----------------------------------------------------------------

	/**
	 * Returns the current average of collisions name
	 * 
	 * @return
	 */
	public String getAvgTotalCollisionsName(){
		return "AvgCollisions";
	}

	/**
	 * Returns the current average of collisions
	 * 
	 * @return
	 */
	public float getAvgTotalCollisions(){
		return 0f;
	}

	//-----------------------------------------------------------------
	// Average number of no violation collisions
	//-----------------------------------------------------------------

	/**
	 * Returns the current average of no violation collisions name
	 * 
	 * @return
	 */
	public String getAvgNoViolCollisionsName(){
		return "AvgNoViolCollisions";
	}

	/**
	 * Returns the current average of no violation collisions
	 * 
	 * @return
	 */
	public float getAvgNoViolCollisions() {
		return 0f;
	}

	//----------------------------------------------------------------
	// Number of cars currently in the map
	//----------------------------------------------------------------

	/**
	 * Returns the current number of cars in the scenario name
	 * 
	 * @return
	 */
	public String getNumberOfCarsInScenarioName(){
		return "NumCarsInScenario";
	}

	/**
	 * Returns the current number of cars in the scenario
	 * 
	 * @return 
	 */
	public float getNumberOfCarsInScenario() {
		return 0f;
	}

	//----------------------------------------------------------------
	// Number of cars currently in the map
	//----------------------------------------------------------------

	/**
	 * 
	 * @return
	 */
	public String getCarsThroughputName(){
		return "CarsThroughput";
	}

	/**
	 * 
	 */
	public float getCarsThroughput() {
		return 0f;
	}

	//----------------------------------------------------------------
	// Number of cars currently in the map
	//----------------------------------------------------------------

	/**
	 * Returns the current number of stops name
	 * 
	 * @return
	 */
	public String getNumberOfStopsName(){
		return "NumberOfStops";
	}

	/**
	 * Returns the current number of stops
	 * 
	 * @return
	 */
	public float getNumberOfStops() {
		return 0f;
	}
}
