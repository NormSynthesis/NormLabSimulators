package es.csic.iiia.normlab.onlinecomm.batch.creator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.csic.iiia.normlab.onlinecomm.batch.NewExperimentButton;


public class ExperimentLauncherCreator extends JFrame implements ActionListener {
	/**
	 * 
	 */
  private static final long serialVersionUID = -2082897485136628404L;

	ArrayList<Integer> NormSynthesisStrategy;

	String name;
	String configFolder = "batch/onlinecomm/configurations";
	

	
	double NormGeneralisationMode = 1;
	double NormGeneralisationStep = 1;
	double NormsGenEffThreshold = 0.0;
	double NormsGenNecThreshold = 0.0;
	double NormsSpecEffThreshold = 0.0;
	ArrayList<Double> NormsSpecNecThreshold;//0.1, 0.3, 0.5, 0.7 y 0.9
	double NormsSpecThresholdEpsilon = 0.025;
	double NormsMinEvaluations = 50;
	double NormsPerfRangeSize = 100;
	double NormsDefaultUtility = 0.5;
	double NumTicksToConverge = 1000;
	boolean NormsWithUserId = true;
	boolean NormGenerationReactive = false;
	double ContentsQueueSize = 1000;
	double ContentsNumViewsToClassify = 5; //10;
	String batchDir = "$PROJECT_DIR'/batch/onlinecomm/batch_params.xml'";
	
	double experimentRepetition = 10;

	private JList<String> ExperimentChooseList;
	private JButton FlechaDerecha;
	private JLabel label1;
	private JButton Launch;
	private JButton NewExperiment;
	private JButton FlechaIzquierda;
	private JLabel label2;
	private JList<String> ExperimentLaunchList;
	DefaultListModel<String> ExperimentChooseListModel;
	DefaultListModel<String> ExperimentLaunchListModel;

	int totalComments = 0;
	int numberAgents = 0;
	private JLabel ExperimentNameLabel;
	private JTextField ExperimentName;
	private JCheckBox AllCheckBox, PCcheckBox, ClusterCheckBox;
	double normViolationRate = 0;
	int stopTick = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExperimentLauncherCreator l = new ExperimentLauncherCreator();
		l.launch();
		l.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
	}

	/**
	 * 
	 */
	private void launch() {
		initGUI();
		NormSynthesisStrategy = new ArrayList<Integer>();
		NormSynthesisStrategy.add(1); //IRON
		NormSynthesisStrategy.add(2); //SIMON
		NormSynthesisStrategy.add(3); //SIMON+
		
		NormsSpecNecThreshold = new ArrayList<Double>();//0.1, 0.3, 0.5, 0.7 y 0.9
		NormsSpecNecThreshold.add(0.1);
		NormsSpecNecThreshold.add(0.3);
		NormsSpecNecThreshold.add(0.5);
		NormsSpecNecThreshold.add(0.7);
		NormsSpecNecThreshold.add(0.9);
		
		readExperimentNames();

		setSize(739, 624);
		setLocationRelativeTo(null);
		setVisible(true);	
	}
	
/**
 * 
 */
	private void readExperimentNames() {		
		File directorio = new File (configFolder);
		File[] archivos = directorio.listFiles();

		ExperimentChooseListModel.removeAllElements();
		for(File file:archivos){
			name = file.getName();
			if(name.contains(".xml"))
				ExperimentChooseListModel.addElement(name);
		}
	}

	/**
	 * 
	 */
	private void initGUI() {
		try {
			// Set Look & Feel
			javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			getContentPane().setLayout(null);

			ExperimentChooseListModel = new DefaultListModel<String>();
			ExperimentChooseList = new JList<String>();
			getContentPane().add(ExperimentChooseList);
			ExperimentChooseList.setModel(ExperimentChooseListModel);
			ExperimentChooseList.setBounds(98, 115, 224, 315);


			ExperimentLaunchListModel = new DefaultListModel<String>();
			ExperimentLaunchList = new JList<String>();
			getContentPane().add(ExperimentLaunchList);
			ExperimentLaunchList.setModel(ExperimentLaunchListModel);
			ExperimentLaunchList.setBounds(431, 115, 232, 317);


			FlechaDerecha = new JButton();
			getContentPane().add(FlechaDerecha);
			FlechaDerecha.setText("--->");
			FlechaDerecha.setBounds(328, 217, 81, 29);
			FlechaDerecha.addActionListener(this);

			label1 = new JLabel();
			getContentPane().add(label1);
			label1.setText("Experiments in the folder");
			label1.setBounds(98, 76, 159, 16);

			label2 = new JLabel();
			getContentPane().add(label2);
			label2.setText("Experiments To Launch");
			label2.setBounds(431, 76, 147, 16);

			FlechaIzquierda = new JButton();
			getContentPane().add(FlechaIzquierda);
			FlechaIzquierda.setText("<---");
			FlechaIzquierda.setBounds(328, 264, 81, 32);
			FlechaIzquierda.addActionListener(this);

			NewExperiment = new JButton();
			getContentPane().add(NewExperiment);
			NewExperiment.setText("Make new Experiment");
			NewExperiment.setBounds(127, 442, 181, 29);
			NewExperiment.addActionListener(this);

			AllCheckBox = new JCheckBox();
			AllCheckBox.setText("All");
			AllCheckBox.setBounds(342, 182, 49, 23);
			getContentPane().add(AllCheckBox);

			PCcheckBox = new JCheckBox();
			PCcheckBox.setText("PC");
			PCcheckBox.setBounds(342, 300, 49, 23);
			getContentPane().add(PCcheckBox);
			PCcheckBox.addActionListener(this);

			ClusterCheckBox = new JCheckBox();
			ClusterCheckBox.setText("Cluster");
			ClusterCheckBox.setBounds(342, 320, 100, 23);
			getContentPane().add(ClusterCheckBox);
			ClusterCheckBox.addActionListener(this);

			ExperimentNameLabel = new JLabel();
			ExperimentNameLabel.setText("Experiment Name:");
			ExperimentNameLabel.setBounds(261, 481, 115, 16);
			getContentPane().add(ExperimentNameLabel);

			ExperimentName = new JTextField();
			ExperimentName.setBounds(382, 474, 169, 30);
			getContentPane().add(ExperimentName);

			Launch = new JButton();
			getContentPane().add(Launch);
			Launch.setText("Create Experiment Launcher");
			Launch.setBounds(294, 521, 200, 43);
			Launch.addActionListener(this);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == FlechaDerecha){
			if(AllCheckBox.isSelected())
			{
				for(int i = 0 ; i < ExperimentChooseListModel.size() ; i++){
					ExperimentLaunchListModel.addElement(ExperimentChooseListModel.get(i));
				}
				ExperimentChooseListModel.removeAllElements();
			}
			else{
				int index = ExperimentChooseList.getSelectedIndex();
				ExperimentLaunchListModel.addElement(ExperimentChooseListModel.get(index));
				ExperimentChooseListModel.remove(index);
				ExperimentChooseList.clearSelection();

			}		
		}

		if(e.getSource() == FlechaIzquierda){
			if(AllCheckBox.isSelected()){
				for(int i = 0 ; i < ExperimentLaunchListModel.size() ; i++){
					ExperimentChooseListModel.addElement(ExperimentLaunchListModel.get(i));
				}
				ExperimentLaunchListModel.removeAllElements();
			}
			else{
				int index = ExperimentLaunchList.getSelectedIndex();
				ExperimentChooseListModel.addElement(ExperimentLaunchListModel.get(index));
				ExperimentLaunchListModel.remove(index);
				ExperimentLaunchList.clearSelection();	
			}
		}

		if(e.getSource() == PCcheckBox){
			ClusterCheckBox.setSelected(false);
		}

		if(e.getSource() == ClusterCheckBox){
			PCcheckBox.setSelected(false);
		}
		if(e.getSource() == NewExperiment){
			new NewExperimentButton(new javax.swing.JFrame(), true);	
			readExperimentNames();
			ExperimentLaunchListModel.removeAllElements();
		}

		if(e.getSource() == Launch){
			if (PCcheckBox.isSelected() || ClusterCheckBox.isSelected()){
				//1 = ASUKA
				//2 = ATTO CLUSTER
				int machine;
				if(PCcheckBox.isSelected()){
					machine = 1;
				}else{
					machine = 2;
				}
				createExperimentLaunchers(machine);
				JOptionPane.showMessageDialog(null, "Done");
				//				this.dispose();
				System.exit(0);

				//launchExperiments();
			}			
			JOptionPane.showMessageDialog(null, "Select machine (PC or Cluster).");
		}
	}

	/**
	 * 
	 * @param machine
	 */
	private void createExperimentLaunchers(int machine) {
		String experimentOutputFile = "ExperimentOutputData";
		String experimentsFolderName = ExperimentName.getText();
		String launcherFileName = "";
		String projectDir = "";
		String strategyName = "";

		ArrayList<String> foldersLine = new ArrayList<String>();
		FileWriter launcherFileWriter = null;
		PrintWriter launcherPrintWriter = null;

		int numPopulations = ExperimentLaunchListModel.getSize();
		int strategyNum = 0;

		/* Generate a different set of launchers for each
		 *  different norm synthesis approach */
		for(int strategy = 0; strategy < NormSynthesisStrategy.size(); strategy++){
			/* Get norm synthesis strategy (its name) */
			switch(NormSynthesisStrategy.get(strategy)) {
			case 1:	strategyName = "IRON";	
							strategyNum = 0;
							break;
			case 2:	strategyName = "SIMON";
							strategyNum = 3;
							break;
			case 3:	strategyName = "SIMONPlus";				
							strategyNum = 6;
							break;
			case 4:	strategyName = "LION";			break;
			}

			/* Generate a different launcher for each different population */
			for (int numPopulation = 0; numPopulation < numPopulations; numPopulation++) {
				
				/* Get population URL */
				String populationURL = getPopulationURL(numPopulation);
				
				/* Get launcher file name */
				switch (machine){
				case 1:
					foldersLine = readFile("batch/onlinecomm/classpath/AsukaPC-Classpath.txt");
					launcherFileName = "batch/onlinecomm/launchers/pc/" + strategyName + "_Experiments_Launcher_";
					projectDir = "PROJECT_DIR=$NORMLAB_DIR\n";
					break;
				case 2:
					foldersLine = readFile("batch/onlinecomm/classpath/Cluster-Folders.txt");
					launcherFileName = "batch/onlinecomm/launchers/cluster/" + strategyName + "_Experiments_Launcher_";
					projectDir = "PROJECT_DIR=$WORKSPACE/Simulators/NormLabSimulators_"+(numPopulation+1+strategyNum)+"\n";
					break;
				}

				try
				{
					/* Create launcher output files*/
					launcherFileWriter = new FileWriter(launcherFileName + (numPopulation+1) + ".sh");
					launcherPrintWriter = new PrintWriter(launcherFileWriter);

					/* Add folders configuration */
					for (int j = 0; j < foldersLine.size(); j++) {
						launcherPrintWriter.write(foldersLine.get(j));
						launcherPrintWriter.write("\n");
					}    
					launcherPrintWriter.write(projectDir);

					for(int i = 0;i < NormsSpecNecThreshold.size() ; i++){
					
					/* Prepare folders for the experiment */
					String experimentsFolderNameWithParameters = "$NORMLAB_DIR/output/onlinecomm/" + experimentsFolderName + "-NSStrategy-" +
							strategyName + "_Content-" + totalComments + "_Agents-" + numberAgents + "_Viol-" + normViolationRate + 
							"_StopTick-"+stopTick+"ConflictThreshold-"+	NormsSpecNecThreshold.get(i) +"NPR-"+NormsPerfRangeSize+"CQS-"+ContentsQueueSize+"CNV-"+ContentsNumViewsToClassify;
					
					String population = getPopulationName(populationURL);

					/* Create new folder into the experiments folder. This new folder will 
					 * keep all the files resulting from the experiments */
					launcherPrintWriter.write("mkdir " + experimentsFolderNameWithParameters + "\n");
					launcherPrintWriter.write("mkdir $NORMLAB_DIR/output/onlinecomm/norms\n");

					String simulationLauncherLine = "CLASSPATH=$CLASSPATH $JAVA_EXECUTABLE $JAVA_FLAGS -Xss10M -Xms1024M "
							+ "-Xmx1g repast.simphony.batch.BatchMain -params $PROJECT_DIR/batch/onlinecomm/batch_params.xml "
							+ "$PROJECT_DIR/repast-settings/OnlineCommunity.rs > "+ experimentsFolderNameWithParameters +"/" + population + "-Run$i.log";

					String populationSwitcherLine = "java -cp $PROJECT_DIR/bin es.csic.iiia.normlab.onlinecomm.batch.PopulationSwitcher "
							+ "$NORMLAB_DIR'/"+populationURL+"' $PROJECT_DIR'/files/onlinecomm/populations/population.xml' \n";

					String batchParamsSwitcherLine = "java -cp $PROJECT_DIR/bin es.csic.iiia.normlab.onlinecomm.batch.BatchParamsSwitcher "+
							totalComments+" "+numberAgents + " " + "" + normViolationRate + " " + stopTick + " " + population + " " + 
							NormSynthesisStrategy.get(strategy) + " " + NormGeneralisationMode + " " + NormGeneralisationStep + " " + 
							NormsGenEffThreshold + "" + " " + NormsGenNecThreshold + " " + NormsSpecEffThreshold + " " +
							NormsSpecNecThreshold.get(i)  +" " + NormsSpecThresholdEpsilon	+ " " + NormsMinEvaluations + " " +
							NormsPerfRangeSize + " " +	NormsDefaultUtility + " " + NumTicksToConverge + " " + 
							NormsWithUserId + " " + NormGenerationReactive + " "+ ContentsQueueSize + " "+batchDir+"\n";

					launcherPrintWriter.write("cd $NORMLAB_DIR\n");

					/* Change population and batch params */
					launcherPrintWriter.write(populationSwitcherLine);
					launcherPrintWriter.write(batchParamsSwitcherLine);

					/* Copy experiment files to the corresponding folder */
					launcherPrintWriter.write("cp $PROJECT_DIR/" + populationURL + " " + experimentsFolderNameWithParameters+"\n\n");
					
					launcherPrintWriter.write("echo \"Launching simulations for population \'" + population + "\'\"\n");
					
					launcherPrintWriter.write("for i in `seq "+ experimentRepetition +"`; do\n");
					launcherPrintWriter.write("  cd $PROJECT_DIR\n");

					/* Launch simulation */
					launcherPrintWriter.write("  echo \"  Simulation number\" $i\n");
					launcherPrintWriter.write("  " + simulationLauncherLine + "\n");
					launcherPrintWriter.write("  cd $PROJECT_DIR/output/onlinecomm\n");
					launcherPrintWriter.write("  mv " + experimentOutputFile + " " + experimentsFolderNameWithParameters+"/"+population+"-run$i.dat\n");
					launcherPrintWriter.write("  mv ContentMetrics.dat " + experimentsFolderNameWithParameters+"/"+population+"-run$i-contentMetrics.dat\n");
					launcherPrintWriter.write("  rm " + experimentOutputFile + "* \n");
					launcherPrintWriter.write("  mv norms/ "+ experimentsFolderNameWithParameters+"/"+population+"-run$i-norms\n");
//					launcherPrintWriter.write("  mv $NORMLAB_DIR/output/onlinecomm/" + population + "-Run$i.log " + experimentsFolderNameWithParameters + "\n" );
					launcherPrintWriter.write("  mkdir norms\n");
					launcherPrintWriter.write("done\n");

					launcherPrintWriter.write("mv Convergence.dat " + experimentsFolderNameWithParameters+"/"+population+"-Convergence.dat\n");
					launcherPrintWriter.write("mv ComputationMetrics.dat " + experimentsFolderNameWithParameters+"/"+population+"-ComputationMetrics.dat\n");
					launcherPrintWriter.write("mv ConvergedNormativeSystems " + experimentsFolderNameWithParameters+"/"+population+"-ConvergedNormativeSystems.dat\n");
					launcherPrintWriter.write("mv ConvergedNormativeSystems.plot " + experimentsFolderNameWithParameters+"/"+population+"-ConvergedNormativeSystems.plot\n");
					launcherPrintWriter.write("mv NotConvergedNormativeSystems " + experimentsFolderNameWithParameters+"/"+population+"-NotConvergedNormativeSystems.dat\n");
					launcherPrintWriter.write("mv NotConvergedNormativeSystems.plot " + experimentsFolderNameWithParameters+"/"+population+"-NotConvergedNormativeSystems.plot\n");
					launcherPrintWriter.write("mv FinalNorms " + experimentsFolderNameWithParameters+"/"+population+"-FinalNorms.dat\n");
					launcherPrintWriter.write("mv FinalNorms.plot " + experimentsFolderNameWithParameters+"/"+population+"-FinalNorms.plot\n");
					launcherPrintWriter.write("mv TotalNorms " + experimentsFolderNameWithParameters+"/"+population+"-TotalNorms.dat\n");
					launcherPrintWriter.write("mv TotalNorms.plot " + experimentsFolderNameWithParameters+"/"+population+"-TotalNorms.plot\n");
//					launcherPrintWriter.write("mv *.log " + experimentsFolderNameWithParameters+"/"+population+"\n");

					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					try {
						/* Close output files */
						if (null != launcherFileWriter)
							launcherFileWriter.close();
					}
					catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			} 
		}
	}

	/**
	 * 
	 * @param populationURL
	 * @return
	 */
	private String getPopulationName(String populationURL) {
		String[] cadena = populationURL.split("\\/");
		String[] cadena2 = cadena[cadena.length - 1].split("\\.");
		return cadena2[0];
	}

	/**
	 * 
	 * @param fichero
	 * @return
	 */
	private ArrayList<String> readFile(String fichero) {
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;
		String linea = "";
		ArrayList<String> texto = new ArrayList<String>();
		try {
			// Apertura del fichero y creacion de BufferedReader para poder
			// hacer una lectura comoda (disponer del metodo readLine()).

			//Para hacer experimentos para el cluster 
			archivo = new File (fichero);
			//Experimentos en ordenador
			// archivo = new File ("Classpath.txt");
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);

			// Lectura del fichero
			while((linea = br.readLine())!=null){
				texto.add(linea);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}finally{
			try{                    
				if( null != fr ){   
					fr.close();     
				}                  
			}catch (Exception e2){ 
				e2.printStackTrace();
			}
		}		
		return texto;
	}

	/**
	 * 
	 * @param i
	 * @return
	 */
	private String getPopulationURL(int i) {
		String populationURL = null;
		try {

			File fXmlFile = new File("batch/onlinecomm/configurations/"+ExperimentLaunchListModel.get(i));
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("Experiment");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					populationURL = eElement.getElementsByTagName("PopulationURL").item(0).getTextContent();
					totalComments = Integer.parseInt(eElement.getElementsByTagName("TotalComments").item(0).getTextContent());
					numberAgents = Integer.parseInt(eElement.getElementsByTagName("NumberAgents").item(0).getTextContent());
					normViolationRate = Double.parseDouble(eElement.getElementsByTagName("NormViolationRate").item(0).getTextContent());					
					stopTick = Integer.parseInt(eElement.getElementsByTagName("StopTick").item(0).getTextContent());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return populationURL;
	}
}
