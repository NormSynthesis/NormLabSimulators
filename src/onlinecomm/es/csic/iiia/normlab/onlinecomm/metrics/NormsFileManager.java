package es.csic.iiia.normlab.onlinecomm.metrics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import es.csic.iiia.normlab.onlinecomm.context.ContextData;
import es.csic.iiia.normlab.onlinecomm.nsm.CommunityNormSynthesisSettings;
import es.csic.iiia.nsm.NormSynthesisMachine;
import es.csic.iiia.nsm.net.norm.NetworkNodeState;
import es.csic.iiia.nsm.norm.Norm;

/**
 * Norms file manager
 * 
 * @author Javier Morales (jmoralesmat@gmail.com)
 *
 */
public class NormsFileManager {

	//-----------------------------------------------------------------
	// Attributes
	//-----------------------------------------------------------------

	private NormSynthesisMachine nsm;
	
	private File totalNormsFile, totalNormsPlotFile, finalNormsFile;
	private File convergenceFile, finalNormsPlotFile;
	private HashMap<String,Integer> totalNorms, finalNorms;

	//-----------------------------------------------------------------
	// Methods
	//-----------------------------------------------------------------

	/**
	 * Constructor
	 */
	public NormsFileManager(ContextData contextData, NormSynthesisMachine nsm) {
		this.totalNorms = new HashMap<String, Integer>();
		this.finalNorms = new HashMap<String, Integer>();
		this.nsm = nsm;
	}

	/**
	 * Loads the norms file
	 */
	public void load() {
		String totalNormsFilename = CommunityNormSynthesisSettings.SIM_DATA_PATH 
				+ "/" + CommunityNormSynthesisSettings.SIM_TOTAL_NORMS_FILE;
		String totalNormsPlotFilename = CommunityNormSynthesisSettings.SIM_DATA_PATH 
				+ "/" + CommunityNormSynthesisSettings.SIM_TOTAL_NORMS_FILE + ".plot";
		String finalNormsFilename = CommunityNormSynthesisSettings.SIM_DATA_PATH 
				+ "/" + CommunityNormSynthesisSettings.SIM_FINAL_NORMS_FILE;
		String finalNormsPlotFilename = CommunityNormSynthesisSettings.SIM_DATA_PATH
				+ "/" + CommunityNormSynthesisSettings.SIM_FINAL_NORMS_FILE + ".plot";
		String convergenceFileName = CommunityNormSynthesisSettings.SIM_DATA_PATH + "/Convergence.dat";
		
		totalNormsFile = new File(totalNormsFilename);
		totalNormsPlotFile = new File(totalNormsPlotFilename);
		finalNormsFile = new File(finalNormsFilename);
		finalNormsPlotFile = new File(finalNormsPlotFilename);
		convergenceFile = new File(convergenceFileName);
		
		// Create if needed
		try {
			
			// Total norms
			if(!totalNormsFile.exists()) {
				totalNormsFile.createNewFile();
			}
			if(totalNormsPlotFile.exists()) {
				totalNormsPlotFile.delete();
			}
			totalNormsPlotFile.createNewFile();
			
			// Final norms
			if(!finalNormsFile.exists()) {
				finalNormsFile.createNewFile();
			}
			if(finalNormsPlotFile.exists()) {
				finalNormsPlotFile.delete();
			}
			finalNormsPlotFile.createNewFile();
			
			// Convergence file
			if(!convergenceFile.exists()) {
				convergenceFile.createNewFile();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		// Load files into hash maps
		this.loadNormsFile(totalNormsFile, totalNorms);
		this.loadNormsFile(finalNormsFile, finalNorms);
	}

	/**
	 * Loads the norms from the norms file
	 */
	private void loadNormsFile(File file, HashMap<String,Integer> map) {
		String line = null;
		String norm = "";
		int numApps = 0;

		try
		{
			BufferedReader input = new BufferedReader(new FileReader(file));
			while ((line = input.readLine()) != null)
			{
				switch(line.charAt(0)) {

				// Line of the norm name
				case 'N':

					// Save previous norm
					if(norm.length() > 0) {
						map.put(norm, numApps);
					}
					// New norm
					norm = line + "\n";
					break;

					// Line of the number of appearances 
				case 'A':
					numApps = Integer.valueOf(line.substring(1));
					break;

				default:
					norm += line + "\n";
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
		this.updateNumApps();

		try
		{
			// Total norms
			BufferedWriter output = new BufferedWriter(new FileWriter(totalNormsFile));
			for(String norm : totalNorms.keySet()) {
				output.write(norm);
				output.write("A" + String.valueOf(totalNorms.get(norm)) + "\n");
			}
			output.write("N");
			output.close();
			
			// Final norms
			output = new BufferedWriter(new FileWriter(finalNormsFile));
			for(String norm : finalNorms.keySet()) {
				output.write(norm);
				output.write("A" + String.valueOf(finalNorms.get(norm)) + "\n");
			}
			output.write("N");
			output.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		// Save total and final norms
		this.saveDataToPlot(totalNorms, totalNormsPlotFile);
		this.saveDataToPlot(finalNorms, finalNormsPlotFile);
	}

	/**
	 * 
	 * @param converged
	 * @param convergenceQuality 
	 */
	public void saveConvergence(boolean converged, double convergenceQuality) {
		try
		{
			BufferedWriter output = new BufferedWriter(new FileWriter(convergenceFile, true));
//			output.write("----------------------------------------------------");
			output.write(String.valueOf(converged) + "\n");
			output.write(String.valueOf(convergenceQuality) + "\n");
			
//			for(Norm norm : ironMachine.) {
//				output.write(norm + "\n");
//			}
			output.close();
    }
		catch (IOException e)
		{
	    e.printStackTrace();
    }
	}
	
	/**
	 * Saves a file to plot an histogram in gnuplot
	 */
	private void saveDataToPlot(HashMap<String,Integer> map, File file) {
		int i = 0;

		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write("Norm TimesAppearing\n");

			for(String norm : map.keySet()) {
				output.write(++i + " " + String.valueOf(map.get(norm)) + "\n");
			}
			output.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads the norms in the norm layer to save them to the norms file
	 */
	private void updateNumApps() {
		String norm = "";

		for(Norm n : this.nsm.getNormativeNetwork().getNorms()) {
			norm = "N\n";
			norm += n.getPrecondition() + "-" + n.getAction() + "\n";

			// Total norms
			if(totalNorms.containsKey(norm)) {
				int numApps = totalNorms.get(norm);
				totalNorms.put(norm, ++numApps);
			} else {
				totalNorms.put(norm, 1);
			}
		}
			
		for(Norm n : this.nsm.getNormativeNetwork().getNormativeSystem()) {
			norm = "N\n";
			norm += n.getPrecondition() + "-" + n.getAction() + "\n";

			// Active norms (Evaluating)
			if(nsm.getNormativeNetwork().getState(n) == NetworkNodeState.ACTIVE) {
				if(finalNorms.containsKey(norm)) {
					int numApps = finalNorms.get(norm);
					finalNorms.put(norm, ++numApps);
				} else {
					finalNorms.put(norm, 1);
				}				
			}
		}
	}
}
