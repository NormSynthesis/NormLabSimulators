package es.csic.iiia.normlab.onlinecomm.metrics;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
import es.csic.iiia.nsm.NormSynthesisMachine;
import es.csic.iiia.nsm.config.Dimension;
import es.csic.iiia.nsm.config.Goal;
import es.csic.iiia.nsm.metrics.DefaultNormSynthesisMetrics;
import es.csic.iiia.nsm.net.norm.NormativeNetwork;
import es.csic.iiia.nsm.norm.Norm;
import es.csic.iiia.nsm.norm.evaluation.Utility;

/**
 * 
 * @author Iosu Mendizabal
 *
 */
public class CommunityMetricsManager extends DefaultNormSynthesisMetrics{

	private ContextData contextData;
	private NormSynthesisMachine nsm;
	private SlidingWindowMetric nonRegulatedComplaintsWindow; 

	private double medianViewComplaintRatio, minViewComplaintRatio, maxViewComplaintRatio;//, medianComplaints;
	private NormsFileManager normsFile;
	private NormSetsFileManager normSetsFile;

	//	private long numTicksWithoutNormChanges = 0;
	//	private long numTicksWithoutNoViolConflicts = 0;

	//	private boolean converged;
	private double convergenceQuality;

	//	private int noViolConflictsThisRound;

	//	private File simOutput;
	//	private BufferedWriter simOutputWriter;
	//
	//	private StringBuffer simOutput;
	//// Creamos dataset para los resultados de la simulacion
	//this.simOutput = new StringBuffer();
	//this.simOutput.append("\"run\";\"tick\";\"NormativeNetworkCardinality\";\"NormativeSystemCardinality\";\"AvgComplaintsForNonRegulatedNorms\"");


	/**
	 * 
	 * @param contextData
	 * @param nsm
	 */
	public CommunityMetricsManager(ContextData contextData, NormSynthesisMachine nsm){
		super(nsm.getNormSynthesisSettings(), nsm.getNormativeNetwork(),
				nsm.getStrategy(), nsm.getPredicatesDomains(), nsm.getDomainFunctions());

		this.contextData = contextData;
		this.nsm = nsm;
		this.nonRegulatedComplaintsWindow = new SlidingWindowMetric(10);

		this.normsFile = new NormsFileManager(contextData, nsm);

		//		// Creamos dataset para los resultados de la simulacion
		//		this.simOutput = new StringBuffer();
		//		this.simOutput.append("\"run\";\"tick\";\"NormativeNetworkCardinality\";\"NormativeSystemCardinality\";\"AvgComplaintsForNonRegulatedNorms\"");
	}

	private void init()
	{
		//		if(RunEnvironment.getInstance().isBatch()) {
		//			DataSetRegistry registry = (DataSetRegistry) RunState.getInstance().getFromRegistry(DataConstants.REGISTRY_KEY);
		//			Iterator<FileDataSink> i = registry.fileSinks().iterator();
		//			System.out.println(i.hasNext());
		//			
		//			FileDataSink dataSink = i.next();
		//			
		//			// Creamos un nuevo sink de pega
		//			List<NonAggregateDataSource> dataSources = new ArrayList<NonAggregateDataSource>();
		//			dataSources.add(new MethodDataSource(1, , ""))
		//			Formatter formatter = new TabularFormatter(dataSources, ";");
		//			
		//			FileDataSink sinkDePega = new FileDataSink("Online Community Data Set", 
		//					new File("output/onlinecomm/ExperimentOutputData"), dataSink.getFormatter());
		//			List<String> sourceIds = new ArrayList<String>();
		//			sourceIds.add("tick");
		//			sourceIds.add("NormativeNetworkCardinality");
		//			sourceIds.add("NormativeSystemCardinality");
		//			sourceIds.add("AvgComplaintsForNonRegulatedNorms");
		//			
		//			sinkDePega.open(sourceIds);
		//			registry.addFileDataSink(sinkDePega);			
		//		}
		//		FileDataSink dataSink = i.next();
		//		
		//		dataSink.rowStarted();
		//		dataSink.append("tick", 0);
		//		dataSink.append("NormativeNetworkCardinality", 1);
		//		dataSink.append("NormativeSystemCardinality", 2);
		//		dataSink.append("AvgComplaintsForNonRegulatedNorms", 3.0);
		//		dataSink.rowEnded();
		//		dataSink.close();
		//		

		//		System.out.println(fds);
	}

	/**
	 * 
	 */
	public void update() {
		super.update();

		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		if(tick==1) {
			init();
		}

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
		//		outputValues(tick, stopTick);

		this.updateDataset(tick, stopTick);

		if(tick == stopTick) {
			this.saveFiles();
		}

		/*  TODO: Si la normative network ha cambiado
		 *  (normas a��adidas/normas eliminadas), actualizar el fichero
		 *  que contiene informaci��n sobre la normative network
		 */
		
    try {
			//if(this.hasConverged()){	
    	if(this.hasNormativeSystemChangedThisTick()){
//    	if(this.hasNormativeNetworkChangedThisTick()) {
					
					PrintStream ps = new PrintStream("output/onlinecomm/norms/NormGraph-"+tick+".txt");
					PrintStream orig = System.out;
					System.setOut(ps);
				
					NormativeNetwork nNetwork = nsm.getNormativeNetwork();
					Collection<Norm> actNorms = nNetwork.getActiveNorms();
					for (Norm norm : actNorms){
						Goal gComplaints = this.nsm.getNormSynthesisSettings().getSystemGoals().get(0);
						Utility normUtility = nNetwork.getUtility(norm);
						int nec = (int) (normUtility.getScore(Dimension.Necessity, gComplaints) * 100); // Necessity
						int level = nNetwork.getGeneralisationLevel(norm);
						System.out.println("n:"+norm.getName()+"\t"+nec+"\t"+level+"\tActive");
						List<Norm> parents = nNetwork.getParents(norm);
						if(!parents.isEmpty()){
							System.out.print("e:");
							for (Norm parent : parents){
								System.out.print(parent.getName()+" ");
							}
							System.out.println();
						}	
					}
					Collection<Norm> inactNorms = nNetwork.getInactiveNorms();
					boolean represented = false;

					for (Norm norm : inactNorms){
						represented = nNetwork.isRepresented(norm);
						
						Goal gComplaints = this.nsm.getNormSynthesisSettings().getSystemGoals().get(0);
						Utility normUtility = nNetwork.getUtility(norm);
						int nec = (int) (normUtility.getScore(Dimension.Necessity, gComplaints) * 100); // Necessity
						int level = nNetwork.getGeneralisationLevel(norm);
						if(represented){
							System.out.println("n:"+norm.getName()+"\t"+nec+"\t"+level+"\tGeneralised");
						}else{
							System.out.println("n:"+norm.getName()+"\t"+nec+"\t"+level+"\tDiscarded");
						}
						List<Norm> parents = nNetwork.getParents(norm);
						if(!parents.isEmpty()){
							System.out.print("e:");
							for (Norm parent : parents){
								System.out.print(parent.getName()+" ");
							}
							System.out.println();
						}	
					}
					
					System.setOut(orig);
					ps.close();
		
					// Para saber el estado de una norma (activo/inactivo)
					// nNetwork.isActive(norm);
		
					// Para saber las relaciones de una norma:
					// 1. Padres
					// nNetwork.getParents(norm);
					// 2. Hijos
					// nNetwork.getChildren(norm);
		
					// Para saber la utilidad de una norma (para los pesos)
					/*
					Goal gComplaints = this.nsm.getNormSynthesisSettings().getSystemGoals().get(0);
					Utility normUtility = nNetwork.getUtility(norm);
					double eff = normUtility.getScore(Dimension.Effectiveness, gComplaints); // Effectiveness
					double nec = normUtility.getScore(Dimension.Effectiveness, gComplaints); // Necessity
					 */
					}
    } catch (IOException ex) {
  }
	}

	/**
	 * 
	 * @param tick
	 * @param stopTick
	 */
	private void updateDataset(int tick, int stopTick)
	{
		//		int nnc = this.getNormativeNetworkCardinality();
		//		int nsc = this.getNormativeSystemCardinality();
		//		double complaints = this.getAvgComplaintsForNonRegulatedNorms();
		//		String s = "\n1;" + tick + ";" + nnc + ";" + nsc + ";" + complaints;
		//
		//		this.simOutput.append(s);


	}

	//	/**
	//	 * 
	//	 */
	//	private void outputValues(int tick, int stopTick){
	//		int nnc = this.getNormativeNetworkCardinality();
	//		int nsc = this.getNormativeSystemCardinality();
	//		double acn = this.getAvgComplaintsForNonRegulatedNorms();
	//		
	//		String s = "\n1;" + tick + ";" + nnc + ";" + nsc + ";" + acn;
	//		try {
	//			simOutputWriter.write(s);
	//			
	//			if(tick == stopTick)
	//				simOutputWriter.close();
	//			
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//	}

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

	/**
	 * 
	 * @param tick
	 */
	private void calcularConvergencia(int tick) 
	{
		Parameters params = RunEnvironment.getInstance().getParameters();
		int stopTick = (Integer) params.getValue("StopTick");

		// Calcular convergencia 
		//		if () {
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

		//		// Check convergence
		//		this.converged = this.numTicksWithoutNormChanges >= CommunityNormSynthesisSettings.SIM_NUM_TICKS_CONVERGENCE
		//				&& this.numTicksWithoutNoViolConflicts >= CommunityNormSynthesisSettings.SIM_NUM_TICKS_CONVERGENCE;

		if (tick == stopTick && this.hasConverged())
		{
			int actualNorms = nsm.getNormativeNetwork().getNormativeSystem().size();
			int systemIdealCardinality = contextData.getIdealNormativeSystemCardinality();
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
		this.normsFile.saveConvergence(this.hasConverged(), convergenceQuality);
		this.normsFile.save();

		this.normSetsFile = new NormSetsFileManager(this, nsm, this.hasConverged());
		this.normSetsFile.load();
		this.normSetsFile.save();

		//		File outputFile = new File("output/onlinecomm/ExperimentOutputData");
		//		
		//		try {
		//			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
		//			String s = this.simOutput.toString();
		//			bw.write(s);
		//			bw.close();
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}
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
