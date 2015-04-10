
package es.csic.iiia.normlab.traffic.metrics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import repast.simphony.annotate.AgentAnnot;
import repast.simphony.engine.environment.RunEnvironment;
import es.csic.iiia.nsm.NormSynthesisMachine;
import es.csic.iiia.nsm.agent.EnvironmentAgentAction;
import es.csic.iiia.nsm.agent.language.SetOfPredicatesWithTerms;
import es.csic.iiia.nsm.config.Dimension;
import es.csic.iiia.nsm.config.Goal;
import es.csic.iiia.nsm.config.NormSynthesisSettings;
import es.csic.iiia.nsm.metrics.DefaultNormSynthesisMetrics;
import es.csic.iiia.nsm.norm.Norm;
import es.csic.iiia.nsm.norm.NormModality;
import es.csic.iiia.nsm.norm.NormativeSystem;
import es.csic.iiia.nsm.norm.evaluation.Utility;

/**
 * Metrics manager
 * 
 * @author Javier Morales (jmorales@iiia.csic.es)
 *
 */
@AgentAnnot(displayName = "Metrics Agent")
public class TrafficMetrics extends DefaultNormSynthesisMetrics {

	private NormSynthesisMachine nsm;
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

	private StringBuffer simOutput;
	private int fitoussiMinimality;
	
	//-----------------------------------------------------------------
	// Methods
	//-----------------------------------------------------------------

	/**
	 * Constructor
	 *  
	 * @param mainClass
	 */
	public TrafficMetrics(NormSynthesisMachine nsm) {
		super(nsm);

		this.nsm = nsm;
		this.nsmSettings = nsm.getNormSynthesisSettings();

		this.numNormClauseAdditions = 0;
		this.numNormClauseRemovals = 0;
		this.NSEffAtIRONConvergenceTick = 0;
		this.NSNecAtIRONConvergenceTick = 0;
		this.NSMinimalityAtIRONConvergenceTick = 0;
		this.NSSimplicityAtIRONConvergenceTick = 0;		
		
		// Creamos dataset para los resultados de la simulacion
		this.simOutput = new StringBuffer();
		this.simOutput.append("\"tick\";\"NormativeNetworkCardinality\";\"NormativeSystemMinimality"
				+ "\";\"NormativeSystemFitoussiMinimality\";\"AvgNonRegulatedCollisions\"");
	}

	/**
	 * Computes the metrics for the simulator
	 */
	public void update(long timeStep) {
		super.update(timeStep);

		this.tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();

		this.numNormClauseAdditions += this.computeNumClauses(this.getNormsAddedThisTick());
		this.numNormClauseRemovals += this.computeNumClauses(this.getNormsRemovedThisTick());

		if(this.tick == this.IRONConvergenceTick) {
			float NSeff = 0f, NSnec = 0f;
			List<Norm> NS =  this.nsm.getNormativeNetwork().getNormativeSystem();

			Goal goal = nsmSettings.getSystemGoals().get(0);
			for(Norm norm : NS) {
				float eff = (float)this.getNormUtility(norm).getScoreAverage(Dimension.Effectiveness, goal);
				float nec = (float)this.getNormUtility(norm).getScoreAverage(Dimension.Necessity, goal);
				NSeff += eff;
				NSnec += nec;
			}
			NSeff /= NS.size();
			NSnec /= NS.size();

			this.NSEffAtIRONConvergenceTick = NSeff;
			this.NSNecAtIRONConvergenceTick = NSnec;
			this.NSMinimalityAtIRONConvergenceTick = this.getNormativeSystemMinimality();
			this.NSSimplicityAtIRONConvergenceTick = this.computeNumClauses(NS);
		}

		this.updateDataset();
		this.computeFitoussiMinimality();
	}

	/**
	 * 
	 */
	private void updateDataset()	{
		int nnc = this.getNormativeNetworkCardinality();
		int nsm = this.getNormativeSystemMinimality();
		int nsfm = this.getNormativeSystemFitoussiMinimality();
		double cols = this.getAvgNoViolCollisions();
		String s = "\n1;" + tick + ";" + nnc + ";" + nsm + ";" + nsfm + ";" + cols;

		this.simOutput.append(s);
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
		List<Norm> NS =  this.nsm.getNormativeNetwork().getNormativeSystem();

		Goal goal = nsmSettings.getSystemGoals().get(0);
		for(Norm norm : NS)
		{
			float eff = (float)this.getNormUtility(norm).getScoreAverage(Dimension.Effectiveness, goal);
			float nec = (float)this.getNormUtility(norm).getScoreAverage(Dimension.Necessity, goal);
			NSeff += eff;
			NSnec += nec;
		}
		NSeff /= NS.size();
		NSnec /= NS.size();

		s += " " + this.getNormativeSystemMinimality();
		s += " " + this.computeNumClauses(NS);
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
			BufferedWriter writer = new BufferedWriter(
					new FileWriter(metricsFile, true));
			writer.write(this.resume());
			writer.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

		this.normSetsFileManager = new NormSetsFileManager(this, nsm,
				this.hasConverged());
		this.normSetsFileManager.load();
		this.normSetsFileManager.save();

		File outputFile = new File("output/traffic/TrafficDataOutput.dat");

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
			String s = this.simOutput.toString();
			bw.write(s);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		outputFile = new File("output/traffic/ConvergenceTicks.dat");
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, true));
			long convergenceTick = (long) (this.tick);
			bw.write(String.valueOf(convergenceTick) + "\n");
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		outputFile = new File("output/traffic/ComputationMetrics.dat");
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, true));
			
			double minCompTime = this.getMinComputationTime();
			double maxCompTime = this.getMaxComputationTime();
			double medianCompTime = this.getMedianComputationTime();
			double totalCompTime = this.getTotalComputationTime();
			double nodesSynth = this.getNumNodesSynthesised();
			double nodesInMem = this.getNumNodesInMemory();
			double nodesVisited = this.getNumNodesVisited();
			
			bw.write(String.valueOf(minCompTime) + "\t" + String.valueOf(maxCompTime) + "\t" +
					String.valueOf(medianCompTime) + "\t" + String.valueOf(totalCompTime) + "\t" + 
					String.valueOf(nodesSynth) + "\t" +	String.valueOf(nodesInMem) + "\t" + 
					String.valueOf(nodesVisited) + "\t" +"\n");
			bw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}		
//		/* Sacamos patrones de cada norma al final */
//		File perfRangesFile = new File("output/traffic/NormPerfRanges.dat");
//
//		BufferedWriter bw;
//		try {
//			bw = new BufferedWriter(new FileWriter(perfRangesFile));
//
//			NormativeNetwork nNetwork = this.nsm.getNormativeNetwork();
//			Goal goal = nsm.getNormSynthesisSettings().getSystemGoals().get(0);
//			for(Norm norm : nNetwork.getNorms()) {
//				if(nNetwork.isLeaf(norm)) {
//					Utility utility = nNetwork.getUtility(norm);
//					PerformanceRange pRange = utility.getPerformanceRange(Dimension.Necessity, goal);
//
//					bw.write(norm.getPrecondition() + " punctual values; ");
//					for(Float value : pRange.getPunctualValues()) {
//						bw.write(value + ";");		
//					}
//					bw.write("\n");
//					bw.write(norm.getPrecondition() + " top boundary; ");
//					for(Float value : pRange.getTopBoundary()) {
//						bw.write(value + ";");		
//					}
//					bw.write("\n");
//					bw.write(norm.getPrecondition() + " average; ");
//					for(Float value : pRange.getAverage()) {
//						bw.write(value + ";");		
//					}
//					bw.write("\n");
//					bw.write(norm.getPrecondition() + " bottom boundary; ");
//					for(Float value : pRange.getBottomBoundary()) {
//						bw.write(value + ";");		
//					}
//					bw.write("\n");
//				}
//			}
//			bw.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	/**
	 * 
	 * @param NS
	 * @return
	 */
	private int computeNumClauses(List<Norm> norms) {
		int ret = 0;
		for(Norm norm : norms)
		{
			for(String predicate : norm.getPrecondition().getPredicates())
				if(!norm.getPrecondition().getTerms(predicate).get(0).equals("*"))
					ret++;
		}		
		return ret;
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
	
	/**
	 * Returns the f-minimality (Fitoussi's minimality)
	 * of the normative system (i.e., the number of leave
	 * norms it represetents)
	 * 
	 * @return 	the f-minimality (Fitoussi's minimality)
	 * 					of the normative system (i.e., the number of leave
	 * 					norms it represetents)
	 */
	public int getNormativeSystemFitoussiMinimality() {
		return this.fitoussiMinimality;
	}
	
  /**
   * 
   * @return
   */
  private void computeFitoussiMinimality() {
  	List<Norm> representedNorms = new ArrayList<Norm>();
  	for(Norm norm : this.normsInNormativeSystem) {
  		this.computeRepresentedNorms(representedNorms, norm);
  	}
  	this.fitoussiMinimality = representedNorms.size(); 
  }
  
  /**
   * 
   * @param norm
   * @return
   */
  private void computeRepresentedNorms(List<Norm> representedNorms, Norm norm) {
  	NormModality mod = norm.getModality();
		EnvironmentAgentAction action = norm.getAction();
		List<SetOfPredicatesWithTerms> chPreconds = this.genReasoner.
				getChildContexts(norm.getPrecondition());
		
		if(!norm.getPrecondition().toString().contains("*")) {
			boolean exists = false;
			for(Norm represented : representedNorms) {
				if(represented.equals(norm)) {
					exists = true;
				}
			}
			if(!exists) {
				representedNorms.add(norm);
				return;
			}
		}
		else {
			List<Norm> children = new ArrayList<Norm>();
			for(SetOfPredicatesWithTerms chPrecond : chPreconds) {
				Norm chNorm = new Norm(chPrecond, mod, action);
				children.add(chNorm);
			}
			
			for(Norm child : children) {
				this.computeRepresentedNorms(representedNorms, child);
			}
		}
  }

	public float getCurrentNormSetEfficiency() {
	  NormativeSystem ns = this.normativeNetwork.getNormativeSystem();
	  float eff = 0f;
	  
	  for(Norm norm : ns) {
	  	Utility u = this.normativeNetwork.getUtility(norm);
	  	Goal g = this.nsmSettings.getSystemGoals().get(0);
	  	eff += u.getScoreAverage(Dimension.Effectiveness, g);
	  }
	  eff /= ns.size();
	  return eff;
  }
	
	public float getCurrentNormSetNecessity() {
	  NormativeSystem ns = this.normativeNetwork.getNormativeSystem();
	  float nec = 0f;
	  
	  for(Norm norm : ns) {
	  	Utility u = this.normativeNetwork.getUtility(norm);
	  	Goal g = this.nsmSettings.getSystemGoals().get(0);
	  	nec += u.getScoreAverage(Dimension.Necessity, g);
	  }
	  nec /= ns.size();
	  return nec;
  }
}
