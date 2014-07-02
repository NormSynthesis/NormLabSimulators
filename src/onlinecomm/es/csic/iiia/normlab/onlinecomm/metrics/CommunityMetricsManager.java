package es.csic.iiia.normlab.onlinecomm.metrics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import es.csic.iiia.normlab.onlinecomm.content.IContent;
import es.csic.iiia.normlab.onlinecomm.context.ContextData;
import es.csic.iiia.normlab.onlinecomm.graphics.SlidingWindowMetric;
import es.csic.iiia.normlab.onlinecomm.nsm.CommunityNormSynthesisSettings;
import es.csic.iiia.nsm.NormSynthesisMachine;

/**
 * 
 * @author Iosu Mendizabal
 *
 */
public class CommunityMetricsManager {

	private ContextData contextData;
	private NormSynthesisMachine nsm;
	private SlidingWindowMetric nonRegulatedComplaintsWindow; 

	private double medianViewComplaintRatio, minViewComplaintRatio, maxViewComplaintRatio;//, medianComplaints;
	private NormsFileManager normsFile;

	private long numTicksWithoutNormChanges = 0;
	private long numTicksWithoutNoViolConflicts = 0;

	private boolean converged;
	private double convergenceQuality;

	private int noViolConflictsThisRound;
	
	private File simOutput;
	private BufferedWriter simOutputWriter;

//	private StringBuffer simOutput;

	/**
	 * 
	 * @param contextData
	 * @param nsm
	 */
	public CommunityMetricsManager(ContextData contextData, NormSynthesisMachine nsm){
		this.contextData = contextData;
		this.nsm = nsm;
		this.nonRegulatedComplaintsWindow = new SlidingWindowMetric(10);

		this.normsFile = new NormsFileManager(contextData, nsm);
		this.converged = false;

		// Creamos dataset para los resultados de la simulacion
//		this.simOutput = new StringBuffer();
//		this.simOutput.append("\"run\";\"tick\";\"NormativeNetworkCardinality\";\"NormativeSystemCardinality\";\"AvgComplaintsForNonRegulatedNorms\"");

		Random r = new Random(System.currentTimeMillis());
//		Random r2 = new Random(System.currentTimeMillis());
		
		simOutput = new File("output/onlinecomm/ExperimentOutputData" + r.nextLong());
		
		try {
			simOutputWriter = new BufferedWriter(new FileWriter(simOutput));
			simOutputWriter.write("\"run\";\"tick\";\"NormativeNetworkCardinality\";\"NormativeSystemCardinality\";\"ComplaintsForNonRegulatedNorms\"");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void init()
	{

//		DataSetRegistry registry = (DataSetRegistry) RunState.getInstance().getFromRegistry(DataConstants.REGISTRY_KEY);
//		Iterator<FileDataSink> i = registry.fileSinks().iterator();
//		FileDataSink fds = i.next();
//		
//		// Creamos un nuevo sink de pega
//		FileDataSink sinkDePega = new FileDataSink("A Data Set", new File("FICHERO_BUENO"), fds.getFormatter());
//		sinkDePega.open()
//		registry.addFileDataSink(sinkDePega);
//		
//		System.out.println(fds);
		
		
		
		
	}
	
	/**
	 * 
	 */
	public void update() {
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		if(tick==1) init();
		
		this.nonRegulatedComplaintsWindow.addValue(contextData.getNumCurrentNonRegulatedComplaints());

		contextData.setSerie(tick, contextData.getNumNonRegulatedComplaints());
		contextData.setSerie2(tick, contextData.getNumComplaint());
		//contextData.setSerie3(tick, contextData.getNumCurrentNonRegulatedComplaints());
		contextData.setSerie3(tick, this.nonRegulatedComplaintsWindow.getAvg());

		// Compute median views
		List<Double> viewComplaintRatios = new ArrayList<Double>();
		//		List<Integer> complaints = new ArrayList<Integer>();

		for(IContent content : contextData.getAllContents())
		{
			if(content.getType() != 1) {
				//				complaints.add(content.getNumComplaints());
				if(content.getNumViews() > 0) {
					double viewComplaintRatio = 100 * ((double)content.getNumComplaints() / (double)content.getNumViews());
					viewComplaintRatios.add(viewComplaintRatio);
				}
			}
		}

		Collections.sort(viewComplaintRatios);

		int n = viewComplaintRatios.size();


		//odd
		if(n > 0)
		{
			this.setMinViewComplaintRatio(viewComplaintRatios.get(0));
			this.setMaxViewComplaintRatio(viewComplaintRatios.get(n-1));

			if(n%2!=0) {
				medianViewComplaintRatio = viewComplaintRatios.get((n-1)/2);
				//even
			} else {
				medianViewComplaintRatio = ( viewComplaintRatios.get(n/2) + viewComplaintRatios.get((n/2)-1) ) / 2;
			}	
		}

		calcularConvergencia(tick);

		List<Double> viewComplaintRatioWithoutNormViolation = new ArrayList<Double>();
		List<Double> viewComplaintRatioWithNormViolation = new ArrayList<Double>();


		for(IContent content : contextData.getAllContents())
		{
			if(content.getType() != 1) 
			{
				if(content.getNumViews() > 0) {
					if(content.getViolatedNorm() == null){
						double viewComplaintRatio = 100 * ((double)content.getNumComplaints() / (double)content.getNumViews());
						viewComplaintRatioWithoutNormViolation.add(viewComplaintRatio);
					}else{
						double viewComplaintRatio = 100 * ((double)content.getNumComplaints() / (double)content.getNumViews());
						viewComplaintRatioWithNormViolation.add(viewComplaintRatio);
					}
				}
			}
		}

		Parameters params = RunEnvironment.getInstance().getParameters();
		int stopTick = (Integer) params.getValue("StopTick");
		if(tick == stopTick){
			crearHistograma(viewComplaintRatioWithNormViolation, viewComplaintRatioWithoutNormViolation);
		}
		outputValues(tick, stopTick);

		this.updateDataset(tick, stopTick);

	}

	/**
	 * 
	 * @param tick
	 * @param stopTick
	 */
	private void updateDataset(int tick, int stopTick)
	{
		int nnc = this.getNormativeNetworkCardinality();
		int nsc = this.getNormativeSystemCardinality();
		double complaints = this.getAvgComplaintsForNonRegulatedNorms();
		String s = "\n1;" + tick + ";" + nnc + ";" + nsc + ";" + complaints;

//		this.simOutput.append(s);
		
		if(tick == stopTick)
			this.saveFiles();
	}

	/**
	 * 
	 */
	private void outputValues(int tick, int stopTick){
		int nnc = this.getNormativeNetworkCardinality();
		int nsc = this.getNormativeSystemCardinality();
		double acn = this.getAvgComplaintsForNonRegulatedNorms();
		
		String s = "\n1;" + tick + ";" + nnc + ";" + nsc + ";" + acn;
		try {
			simOutputWriter.write(s);
			
			if(tick == stopTick)
				simOutputWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void crearHistograma(List<Double> viewComplaintRatios, List<Double> viewComplaintRatioWithoutNormViolation) {
		HistogramDataset dataset = new HistogramDataset();

		double[] v1 = new double[viewComplaintRatios.size()];
		for(int i = 0 ; i < viewComplaintRatios.size() ; i++){
			v1[i] = viewComplaintRatios.get(i)/100;
		}
		double[] v2 = new double[viewComplaintRatioWithoutNormViolation.size()];
		for(int i = 0 ; i < viewComplaintRatioWithoutNormViolation.size() ; i++){
			v2[i] = viewComplaintRatioWithoutNormViolation.get(i)/100;
		}

		int bin = 10;
		dataset.addSeries("viewComplaintRatioViolation", v1, bin, 0, 1);
		dataset.addSeries("ViewComplaintRatioNoViolation", v2, bin, 0 ,1);

		String plotTitle = "Histogram";
		String xaxis = "Ratio";
		String yaxis = "Contents";
		PlotOrientation orientation = PlotOrientation.VERTICAL;
		boolean show = true;
		boolean toolTips = false;
		boolean urls = false;
		JFreeChart chart = ChartFactory.createHistogram(plotTitle, xaxis,
				yaxis, dataset, orientation, show, toolTips, urls);
		int width = 800;
		int height = 800;
		try {
			ChartUtilities.saveChartAsPNG(new File("histogram.png"), chart,
					width, height);
		} catch (IOException e) {
		}
	}

	private void calcularConvergencia(int tick) 
	{
		Parameters params = RunEnvironment.getInstance().getParameters();
		int stopTick = (Integer) params.getValue("StopTick");

//		// Calcular convergencia 
//		if (nsm.normativeSystemChangedThisTick()) {
//			numTicksWithoutNormChanges = 0;
//		} else {
//			numTicksWithoutNormChanges++;
//		}
//
//		if (SocialConflictDetector.getNumNonRegulatedConflictsThisRound() > 0) {
//			numTicksWithoutNoViolConflicts = 0;
//		} else {
//			numTicksWithoutNoViolConflicts++;
//		}

		// Check convergence
		this.converged = this.numTicksWithoutNormChanges >= CommunityNormSynthesisSettings.SIM_NUM_TICKS_CONVERGENCE
				&& this.numTicksWithoutNoViolConflicts >= CommunityNormSynthesisSettings.SIM_NUM_TICKS_CONVERGENCE;

		if (tick == stopTick && converged)
		{
			int actualNorms = nsm.getNormativeNetwork().getNormativeSystem().size();
			int systemIdealCardinality = contextData.getIdealNormativeSystemCardinality();
			System.out.println("actual "+actualNorms);
			System.out.println("ideal cardinality "+systemIdealCardinality);
			convergenceQuality = actualNorms / systemIdealCardinality;
		}
	}

	/**
	 * 
	 * @return
	 */
	public int getComplaintsForNonRegulatedNorms(){
		//		return this.contextData.getNumNonRegulatedComplaints();
		return this.contextData.getNumCurrentNonRegulatedComplaints();		
	}

	/**
	 * 
	 * @return
	 */
	public float getAvgComplaintsForNonRegulatedNorms() {
		return this.nonRegulatedComplaintsWindow.getAvg();
	}

	/**
	 * 
	 * @return
	 */
	public int getComplaintsForRegulatedNorms() {
		return this.contextData.getNumComplaint();
	}
	/**
	 * 
	 * @return
	 */
	public int getActualComplaints(){
		int contador = 0;
		for(int i = 0 ; i < contextData.getActualComplaintList().size() ; i++){
			if(contextData.getActualComplaintList().get(i).getNumComplaints() == 1){
				contador++;
			}
		}
		return contador;
	}

	/**
	 * Save files 
	 * @param convergenceQuality 
	 */
	public void saveFiles() 
	{
		// Cargamos ficheros (convergencia)
		this.normsFile.load();
		this.normsFile.saveConvergence(this.converged, convergenceQuality);
		
		String processName = ManagementFactory.getRuntimeMXBean().getName();
		int idx = processName.indexOf("@");
		String pid = processName.substring(0, idx);
		String n = Long.toString(System.nanoTime()) + "-" + pid;
		
		File outputFile = new File("output/onlinecomm/ExperimentOutput-" + n);
			
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
			String s = this.simOutput.toString();
			bw.write(s);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Returns the number of ACTIVE norms
	 * 
	 * @return
	 */
	public int getNormativeSystemCardinality() {
		return this.nsm.getNormativeNetwork().getNormativeSystem().size();
	}

	/**
	 * Returns the total number of synthesised norms
	 * 
	 * @return
	 */
	public int getNormativeNetworkCardinality() {
		return this.nsm.getNormativeNetwork().getNorms().size();
	}


	public double getMedianViewComplaintRatio() {
		return medianViewComplaintRatio;
	}

	public double getMinViewComplaintRatio() {
		return minViewComplaintRatio;
	}

	public void setMinViewComplaintRatio(double minViewComplaintRatio) {
		this.minViewComplaintRatio = minViewComplaintRatio;
	}

	public double getMaxViewComplaintRatio() {
		return maxViewComplaintRatio;
	}

	public void setMaxViewComplaintRatio(double maxViewComplaintRatio) {
		this.maxViewComplaintRatio = maxViewComplaintRatio;
	}

	//	public double getMedianComplaints() {
	//		return medianComplaints;
	//	}
	//
	//	public double getViewComplaintRatio() {
	//		return viewComplaintRatio;
	//	}
}
