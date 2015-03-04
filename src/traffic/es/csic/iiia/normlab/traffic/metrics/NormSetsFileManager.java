package es.csic.iiia.normlab.traffic.metrics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import es.csic.iiia.normlab.traffic.normsynthesis.TrafficNormSynthesisSettings;
import es.csic.iiia.normlab.traffic.utils.StringList;
import es.csic.iiia.nsm.NormSynthesisMachine;

/**
 * Norms file manager
 * 
 * @author Javier Morales (jmoralesmat@gmail.com)
 *
 */
public class NormSetsFileManager {

	//-----------------------------------------------------------------
	// Attributes
	//-----------------------------------------------------------------

	private File finalNormSetsFile, finalNormSetsPlotFile;
	private HashMap<StringList,Integer> finalNormSets;
	private HashMap<StringList,Float> effNormSets, necNormSets;
	private HashMap<StringList,Integer> covNormSets;
	private TrafficMetrics metrics;
	private NormSynthesisMachine nsm;
	
	private boolean convergence; 
	
	//-----------------------------------------------------------------
	// Methods
	//-----------------------------------------------------------------

	/**
	 * Constructor
	 */
	public NormSetsFileManager(TrafficMetrics metrics, 
			NormSynthesisMachine nsm, boolean convergence) {
		
		this.finalNormSets = new HashMap<StringList, Integer>();
		this.covNormSets = new HashMap<StringList, Integer>();
		this.effNormSets = new HashMap<StringList, Float>();
		this.necNormSets = new HashMap<StringList, Float>();
		this.nsm = nsm;
		this.metrics = metrics;
		this.convergence = convergence;
	}

	/**
	 * Loads the norms file
	 */
	public void load() {
		String finalNormSetsFilename, finalNormSetsPlotFilename;
		
		if(this.convergence){
			finalNormSetsFilename = TrafficNormSynthesisSettings.SIM_DATA_PATH + 
					"/Converged" + TrafficNormSynthesisSettings.SIM_FINAL_NORMSETS_FILE;
			finalNormSetsPlotFilename = TrafficNormSynthesisSettings.SIM_DATA_PATH +
					"/Converged" + TrafficNormSynthesisSettings.SIM_FINAL_NORMSETS_FILE
					+ ".plot";
		}
		else {
			finalNormSetsFilename = TrafficNormSynthesisSettings.SIM_DATA_PATH + 
					"/NotConverged" + TrafficNormSynthesisSettings.SIM_FINAL_NORMSETS_FILE;
			finalNormSetsPlotFilename = TrafficNormSynthesisSettings.SIM_DATA_PATH + 
					"/NotConverged" + TrafficNormSynthesisSettings.SIM_FINAL_NORMSETS_FILE
					+ ".plot";
		}
		
		finalNormSetsFile = new File(finalNormSetsFilename);
		finalNormSetsPlotFile = new File(finalNormSetsPlotFilename);
		
		// Create if needed
		try {
			
			// Final norms
			if(!finalNormSetsFile.exists()) {
				finalNormSetsFile.createNewFile();
			}
			if(finalNormSetsPlotFile.exists()) {
				finalNormSetsPlotFile.delete();
			}
			finalNormSetsPlotFile.createNewFile();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		// Load files into hash maps
		this.loadNormsFile(finalNormSetsFile, finalNormSets, effNormSets,
				necNormSets, covNormSets);
	}

	/**
	 * Loads the norms from the norms file
	 */
	private void loadNormsFile(File file, HashMap<StringList, Integer> nsMap,
			HashMap<StringList, Float> effMap, HashMap<StringList, Float> necMap,
			HashMap<StringList, Integer> covMap) {
		
		StringList normSet = null;
		String norm = null;
		String line = null;
		
		int numApps = 0, covNormSet = 0;
		float effNormSet = 0f, necNormSet = 0f;
		
		try
		{
			BufferedReader input = new BufferedReader(new FileReader(file));
			while ((line = input.readLine()) != null)
			{
				switch(line.charAt(0)) {

				// Indicates new norm set
				case 'S':
					normSet = new StringList();
					break;

				case 'N':
					norm = input.readLine();
					normSet.add(norm);
					break;
					
				// Line of the number of appearances 
				case 'A':
					numApps = Integer.valueOf(input.readLine());
					nsMap.put(normSet, numApps);
					break;
					
				case 'F':
					effNormSet = Float.valueOf(input.readLine());
					effMap.put(normSet, effNormSet);
					necNormSet = Float.valueOf(input.readLine());
					necMap.put(normSet, necNormSet);
					covNormSet = Integer.valueOf(input.readLine());
					covMap.put(normSet, covNormSet);
					break;	
				}
			}
			input.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves the norms file
	 */
	public void save() {
		this.updateData();
		
		try
		{
			// Final norms
			BufferedWriter output = new BufferedWriter(new FileWriter(finalNormSetsFile));
			
			for(StringList normSet : finalNormSets.keySet())
			{
				output.write("S\n");
				
				for(String norm : normSet)
				{
					output.write("N\n");
					output.write(norm + "\n");	
				}
				output.write("A\n" + String.valueOf(finalNormSets.get(normSet)) + "\n");
				output.write("F\n" + String.valueOf(effNormSets.get(normSet)) + "\n");
				output.write(String.valueOf(necNormSets.get(normSet)) + "\n");
				output.write(String.valueOf(covNormSets.get(normSet)) + "\n");
			}
			output.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		// Save total and final norms
		this.saveDataToPlot(finalNormSets, finalNormSetsPlotFile);
	}
	
	/**
	 * Saves a file to plot an histogram in gnuplot
	 */
	private void saveDataToPlot(HashMap<StringList,Integer> map, File file) {
		int i = 0;

		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write("NormSet TimesAppearing\n");

			for(StringList normSet : map.keySet()) {
				output.write(++i + " " + String.valueOf(map.get(normSet)) + "\n");
			}
			output.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads the current norm set to save them to the norm sets file
	 */
	private void updateData() {
		StringList currNormSet = new StringList(nsm.getNormativeNetwork().
				getNormativeSystem());
		boolean exists = false;
		
		for(StringList normSet : this.finalNormSets.keySet())
		{
			if(normSet.equals(currNormSet))
			{
				exists = true;
				int numApps = finalNormSets.get(normSet);
				float effNormSet = effNormSets.get(normSet);
				
				this.finalNormSets.put(normSet, ++numApps);
				this.effNormSets.put(normSet, (effNormSet + metrics.getCurrentNormSetEfficiency()) / 2 );
				this.necNormSets.put(normSet, (effNormSet + metrics.getCurrentNormSetNecessity()) / 2 );
				this.covNormSets.put(normSet, this.metrics.getNormativeSystemFitoussiMinimality());
			}
		}
		
		if(!exists) {
			this.finalNormSets.put(currNormSet, 1);
			this.effNormSets.put(currNormSet, (metrics.getCurrentNormSetEfficiency()) );
			this.necNormSets.put(currNormSet, (metrics.getCurrentNormSetNecessity()) );
			this.covNormSets.put(currNormSet, metrics.getNormativeSystemFitoussiMinimality());
		}
	}
}
