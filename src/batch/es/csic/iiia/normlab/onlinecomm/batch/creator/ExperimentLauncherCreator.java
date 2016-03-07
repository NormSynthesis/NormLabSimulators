package es.csic.iiia.normlab.onlinecomm.batch.creator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import es.csic.iiia.normlab.onlinecomm.batch.NewExperimentButton;


public class ExperimentLauncherCreator extends JFrame implements ActionListener {
	private JList ExperimentChooseList;
	private JButton FlechaDerecha;
	private JLabel label1;
	private JButton Launch;
	private JButton NewExperiment;
	private JButton FlechaIzquierda;
	private JLabel label2;
	private JList ExperimentLaunchList;
	DefaultListModel ExperimentChooseListModel;
	DefaultListModel ExperimentLaunchListModel;

	String nombre;
	String carpeta = "batch/onlinecomm/configurations";

	int totalComments = 0;
	int numberAgents = 0;
	private JLabel ExperimentNameLabel;
	private JTextField ExperimentName;
	private JCheckBox AllCheckBox;
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

	private void launch() {
		initGUI();

		readExperimentNames();

		setSize(739, 624);
		setLocationRelativeTo(null);
		setVisible(true);	
	}

	private void readExperimentNames() {		
		File directorio = new File (carpeta);
		File[] archivos = directorio.listFiles();

		ExperimentChooseListModel.removeAllElements();
		for(File file:archivos){
			nombre = file.getName();
			if(nombre.contains(".xml"))
				ExperimentChooseListModel.addElement(nombre);
		}
	}

	private void initGUI() {
		try {
			// Set Look & Feel
			javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			getContentPane().setLayout(null);

			ExperimentChooseListModel = new DefaultListModel();
			ExperimentChooseList = new JList();
			getContentPane().add(ExperimentChooseList);
			ExperimentChooseList.setModel(ExperimentChooseListModel);
			ExperimentChooseList.setBounds(98, 115, 224, 315);


			ExperimentLaunchListModel = new DefaultListModel();
			ExperimentLaunchList = new JList();
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

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == FlechaDerecha){
			if(AllCheckBox.isSelected()){
				for(int i = 0 ; i < ExperimentChooseListModel.size() ; i++){
					ExperimentLaunchListModel.addElement(ExperimentChooseListModel.get(i));
				}
				ExperimentChooseListModel.removeAllElements();
			}else{
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
			}else{
				int index = ExperimentLaunchList.getSelectedIndex();
				ExperimentChooseListModel.addElement(ExperimentLaunchListModel.get(index));
				ExperimentLaunchListModel.remove(index);
				ExperimentLaunchList.clearSelection();	
			}
		}

		if(e.getSource() == NewExperiment){
			new NewExperimentButton(new javax.swing.JFrame(), true);	
			readExperimentNames();
			ExperimentLaunchListModel.removeAllElements();
		}

		if(e.getSource() == Launch){
			clusterNewClasspath();
			//createClusterBatchLauncher();//cluster viejo
			//crearBashFuncionaUnaEjecucion(); //NormLab probado asuka
			
			
//			crearBashParaCluster();
			//launchExperiments();
		}
	}

	private void clusterNewClasspath() {
			ArrayList<String> folders = leerFichero("batch/onlinecomm/classpath/Cluster-Folders.txt");
//			ArrayList<String> classpath = leerFichero("batch/onlinecomm/classpath/Cluster-Classpath.txt");

			String nombreCarpeta = ExperimentName.getText();
			String experimentOutputFile = "ExperimentOutputData";

			FileWriter fichero = null;
			PrintWriter pw = null;

			//		String ejecutarRepast = "CLASSPATH=$CLASSPATH $JAVA_EXECUTABLE $JAVA_FLAGS -Xss10M -Xmx400M repast.simphony.batch.BatchMain -params $PROJECT_DIR/batch/batch_params.xml $PROJECT_DIR/virtualCommunities.rs\n";
			String ejecutarRepast = "CLASSPATH=$CLASSPATH $JAVA_EXECUTABLE $JAVA_FLAGS -Xss10M -Xms400M -Xmx1g repast.simphony.batch.BatchMain -params $PROJECT_DIR/batch/onlinecomm/batch_params.xml $PROJECT_DIR/repast-settings/OnlineCommunity.rs\n";

			int launchCount = ExperimentLaunchListModel.getSize();

			for (int i = 0; i < launchCount; i++) {
				try{
					fichero = new FileWriter("batch/onlinecomm/launchers/cluster/Experiments_Launcher_"+(i+1)+".sh");
					pw = new PrintWriter(fichero);


					for (int j = 0; j < folders.size(); j++) {
						pw.write(folders.get(j));
						pw.write("\n");
					}    

					pw.write("PROJECT_DIR=$WORKSPACE/Simulators/NormLabSimulators_"+(i+1)+"\n");

//					for (int j = 0; j < classpath.size(); j++) {
//						pw.write(classpath.get(j));
//						pw.write("\n");
//					}

					String populationURL = leerPopulationURL(i);
					String nombreCarpetaConParametros = "$NORMLAB_DIR/output/onlinecomm/"+nombreCarpeta +"_Content-"+totalComments+"_Agents-"+numberAgents+"_Viol-"+normViolationRate+"_StopTick-"+stopTick;
					String poblacion = sacarNombrePoblacion(populationURL);

					//Crear carpeta dentro de la carpeta de experiments para guardar todos los ficheros del experimento.
					//				String nombreCarpetaConParametros = "Experiments/"+nombreCarpeta +"_Content-"+totalComments+"_Agents-"+numberAgents+"_Viol-"+normViolationRate+"_StopTick-"+stopTick+"_run-"+a;
					pw.write("mkdir "+nombreCarpetaConParametros+"\n");
					pw.write("mkdir $NORMLAB_DIR/output/onlinecomm/norms\n");

					//cambiarPoblacion (populationURL)
					pw.write("cd $NORMLAB_DIR\n");
//					String javaCambiarPopulation = "java -cp $PROJECT_DIR/bin es.csic.iiia.normlab.onlinecomm.batch.PopulationSwitcher $NORMLAB_DIR'/"+populationURL+"' \n";
					String javaCambiarPopulation = "java -cp $PROJECT_DIR/bin es.csic.iiia.normlab.onlinecomm.batch.PopulationSwitcher '"+populationURL+"' \n";
					pw.write(javaCambiarPopulation);

					//cambiarBatchParams
					String javaCambiarBatchParams = "java -cp $PROJECT_DIR/bin es.csic.iiia.normlab.onlinecomm.batch.BatchParamsSwitcher "+totalComments+" "+numberAgents+" "+normViolationRate+" "+stopTick+" "+poblacion+"\n";
					pw.write(javaCambiarBatchParams);

					//copiar ficheros a carpeta del experimento
					//				String poblacion = sacarNombrePoblacion(populationURL);
					pw.write("cp $PROJECT_DIR/" + populationURL + " " + nombreCarpetaConParametros+"\n\n");

					// TODO: Javi... elimino el for y lo pongo en el script...
					//				for(int a = 1 ; a <= 100 ; a++){

					pw.write("for i in `seq 10`; do\n");

					pw.write("  cd $PROJECT_DIR\n");

					//Ejecutar Repast
					pw.write("  " + ejecutarRepast);

					pw.write("  cd $PROJECT_DIR/output/onlinecomm\n");
					// pw.write("  dataFile=`ls | grep 'ExperimentOutputData'`\n");
					pw.write("  mv " + experimentOutputFile + " " + nombreCarpetaConParametros+"/"+poblacion+"-run$i.dat\n");

					//Crear grafica del experimento
					//pw.write("  cd $NORMLAB_DIR/batch/onlinecomm/scripts\n");
					//pw.write("  python plotData.py " + nombreCarpetaConParametros+"/"+poblacion+"-run$i.dat\n");
					//				pw.write("  mv ExperimentGraphic.png "+nombreCarpetaConParametros+"/"+poblacion+"-Graphic-run"+i+".png\n");
					//				pw.write("  mv histogram.png "+nombreCarpetaConParametros+"/"+poblacion+"-Histogram-run"+i+".png\n");
					//pw.write("  cd $PROJECT_DIR/output/onlinecomm\n");
					pw.write("  rm " + experimentOutputFile + "* \n");
					pw.write("  mv norms/ "+ nombreCarpetaConParametros+"/"+poblacion+"-run$i-norms\n");
					pw.write("  mkdir norms\n");

					//Crear grafica del experimento
					//pw.write("python ../VirtualCommunitiesBatchLauncher/plotData.py ../NormLab/"+nombreCarpetaConParametros+"/"+poblacion+"-run"+a+".dat\n");
					//pw.write("mv ExperimentGraphic.png "+nombreCarpetaConParametros+"/"+poblacion+"-Graphic-run"+a+".png\n");

					//					pw.write("mv histogram.png ../../NormLab/"+nombreCarpetaConParametros+"/"+poblacion+"-Histogram-run"+a+".png\n");
					//					pw.write("cd $PROJECT_DIR/output/onlinecomm\n");
					//				pw.write("  rm $PROJECT_DIR/output/onlinecomm/ExperimentOutput* \n");
					//				pw.write("  rm $PROJECT_DIR/output/onlinecomm/ExperimentDataPopulation* \n");
					pw.write("done\n");

					//				}

					pw.write("mv Convergence.dat " + nombreCarpetaConParametros+"/"+poblacion+"-Convergence.dat\n");
					pw.write("mv ConvergedNormativeSystems " + nombreCarpetaConParametros+"/"+poblacion+"-ConvergedNormativeSystems.dat\n");
					pw.write("mv ConvergedNormativeSystems.plot " + nombreCarpetaConParametros+"/"+poblacion+"-ConvergedNormativeSystems.plot\n");
					pw.write("mv NotConvergedNormativeSystems " + nombreCarpetaConParametros+"/"+poblacion+"-NotConvergedNormativeSystems.dat\n");
					pw.write("mv NotConvergedNormativeSystems.plot " + nombreCarpetaConParametros+"/"+poblacion+"-NotConvergedNormativeSystems.plot\n");
					pw.write("mv FinalNorms " + nombreCarpetaConParametros+"/"+poblacion+"-FinalNorms.dat\n");
					pw.write("mv FinalNorms.plot " + nombreCarpetaConParametros+"/"+poblacion+"-FinalNorms.plot\n");
					pw.write("mv TotalNorms " + nombreCarpetaConParametros+"/"+poblacion+"-TotalNorms.dat\n");
					pw.write("mv TotalNorms.plot " + nombreCarpetaConParametros+"/"+poblacion+"-TotalNorms.plot\n");

//					
//					pw.write("for i in `seq 10`; do\n");
	//
//					pw.write("  cd $PROJECT_DIR\n");
	//
//					//Ejecutar Repast
//					pw.write("  " + ejecutarRepast);
	//
//					pw.write("  cd $PROJECT_DIR/output/onlinecomm\n");
//					pw.write("  dataFile=`ls | grep 'ExperimentOutputData'`");
//					pw.write("  mv $PROJECT_DIR/output/onlinecomm/$dataFile "+nombreCarpetaConParametros+"/"+poblacion+"-run$i.dat\n");
	//
//					//Crear grafica del experimento
//					//pw.write("python ../VirtualCommunitiesBatchLauncher/plotData.py ../NormLab/"+nombreCarpetaConParametros+"/"+poblacion+"-run"+a+".dat\n");
//					//pw.write("mv ExperimentGraphic.png "+nombreCarpetaConParametros+"/"+poblacion+"-Graphic-run"+a+".png\n");
	//
//					//					pw.write("mv histogram.png ../../NormLab/"+nombreCarpetaConParametros+"/"+poblacion+"-Histogram-run"+a+".png\n");
//					//					pw.write("cd $PROJECT_DIR/output/onlinecomm\n");
//					pw.write("  rm $PROJECT_DIR/output/onlinecomm/ExperimentOutput* \n");
//					pw.write("  rm $PROJECT_DIR/output/onlinecomm/ExperimentDataPopulation* \n");
//					pw.write("done\n");
	//
//					//				}
	//
//					pw.write("mv $PROJECT_DIR/output/onlinecomm/Convergence.dat "+nombreCarpetaConParametros+"/"+poblacion+"-Convergence.dat\n");
	//
	//
//					//				pw.write("cd $PROJECT_DIR/\n");
//					//				pw.write("mv $PROJECT_DIR/ExperimentsOutputData/Convergence.dat ../VirtualCommunitiesSimulation/" + nombreCarpetaConParametros + "/" + poblacion+ "-Convergence.dat\n");
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						// Nuevamente aprovechamos el finally para
						// asegurarnos que se cierra el fichero.
						if (null != fichero)
							fichero.close();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
  }

	/**
	 * 
	 */
	private void crearBashFuncionaUnaEjecucion() {
		ArrayList<String> classpath = leerFichero("batch/onlinecomm/classpath/AsukaPC-Classpath.txt");
		

		String nombreCarpeta = ExperimentName.getText();
		String experimentOutputFile = "ExperimentOutputData";

		FileWriter fichero = null;
		PrintWriter pw = null;

		//		String ejecutarRepast = "CLASSPATH=$CLASSPATH $JAVA_EXECUTABLE $JAVA_FLAGS -Xss10M -Xmx400M repast.simphony.batch.BatchMain -params $PROJECT_DIR/batch/batch_params.xml $PROJECT_DIR/virtualCommunities.rs\n";
		//		String ejecutarRepast = "CLASSPATH=$CLASSPATH $JAVA_EXECUTABLE $JAVA_FLAGS -Xss10M -Xmx400M repast.simphony.batch.BatchMain -params $PROJECT_DIR/batch/onlinecomm/batch_params.xml $PROJECT_DIR/repast-settings/OnlineCommunity.rs\n";
		String ejecutarRepast = "CLASSPATH=$CLASSPATH $JAVA_EXECUTABLE $JAVA_FLAGS -Xss10M -Xms400M -Xmx1g repast.simphony.batch.BatchMain -params $PROJECT_DIR/batch/onlinecomm/batch_params.xml $PROJECT_DIR/repast-settings/OnlineCommunity.rs\n";

		int launchCount = ExperimentLaunchListModel.getSize();

		for (int i = 0; i < launchCount; i++) {
			try{
				fichero = new FileWriter("batch/onlinecomm/launchers/pc/Experiments_Launcher_"+(i+1)+".sh");
				pw = new PrintWriter(fichero);


				for (int j = 0; j < classpath.size(); j++) {
					pw.write(classpath.get(j));
					pw.write("\n");
				}    

				String populationURL = leerPopulationURL(i);
				String nombreCarpetaConParametros = "$NORMLAB_DIR/output/onlinecomm/"+nombreCarpeta +"_Content-"+totalComments+"_Agents-"+numberAgents+"_Viol-"+normViolationRate+"_StopTick-"+stopTick;
				String poblacion = sacarNombrePoblacion(populationURL);

				//Crear carpeta dentro de la carpeta de experiments para guardar todos los ficheros del experimento.
				//				String nombreCarpetaConParametros = "Experiments/"+nombreCarpeta +"_Content-"+totalComments+"_Agents-"+numberAgents+"_Viol-"+normViolationRate+"_StopTick-"+stopTick+"_run-"+a;
				pw.write("mkdir "+nombreCarpetaConParametros+"\n");
				pw.write("mkdir $NORMLAB_DIR/output/onlinecomm/norms\n");
				//cambiarPoblacion (populationURL)
				String javaCambiarPopulation = "java -cp $PROJECT_DIR/bin es.csic.iiia.normlab.onlinecomm.batch.PopulationSwitcher '"+populationURL+"' \n";
				pw.write(javaCambiarPopulation);

				//cambiarBatchParams
				String javaCambiarBatchParams = "java -cp $PROJECT_DIR/bin es.csic.iiia.normlab.onlinecomm.batch.BatchParamsSwitcher "+totalComments+" "+numberAgents+" "+normViolationRate+" "+stopTick+" "+poblacion+"\n";
				pw.write(javaCambiarBatchParams);

				//copiar ficheros a carpeta del experimento
				//				String poblacion = sacarNombrePoblacion(populationURL);
				pw.write("cp " + populationURL + " " + nombreCarpetaConParametros+"\n\n");

				// TODO: Javi... elimino el for y lo pongo en el script...
				//				for(int a = 1 ; a <= 100 ; a++){

				pw.write("for i in `seq 10`; do\n");

				pw.write("  cd $PROJECT_DIR\n");

				//Ejecutar Repast
				pw.write("  " + ejecutarRepast);

				pw.write("  cd $PROJECT_DIR/output/onlinecomm\n");
				// pw.write("  dataFile=`ls | grep 'ExperimentOutputData'`\n");
				pw.write("  mv " + experimentOutputFile + " " + nombreCarpetaConParametros+"/"+poblacion+"-run$i.dat\n");

				//Crear grafica del experimento
				//pw.write("  cd $NORMLAB_DIR/batch/onlinecomm/scripts\n");
				//pw.write("  python plotData.py " + nombreCarpetaConParametros+"/"+poblacion+"-run$i.dat\n");
				//				pw.write("  mv ExperimentGraphic.png "+nombreCarpetaConParametros+"/"+poblacion+"-Graphic-run"+i+".png\n");
				//				pw.write("  mv histogram.png "+nombreCarpetaConParametros+"/"+poblacion+"-Histogram-run"+i+".png\n");
				//pw.write("  cd $PROJECT_DIR/output/onlinecomm\n");
				pw.write("  rm " + experimentOutputFile + "* \n");
				pw.write("  mv norms/ "+ nombreCarpetaConParametros+"/"+poblacion+"-run$i-norms\n");
				pw.write("  mkdir norms\n");

				//Crear grafica del experimento
				//pw.write("python ../VirtualCommunitiesBatchLauncher/plotData.py ../NormLab/"+nombreCarpetaConParametros+"/"+poblacion+"-run"+a+".dat\n");
				//pw.write("mv ExperimentGraphic.png "+nombreCarpetaConParametros+"/"+poblacion+"-Graphic-run"+a+".png\n");

				//					pw.write("mv histogram.png ../../NormLab/"+nombreCarpetaConParametros+"/"+poblacion+"-Histogram-run"+a+".png\n");
				//					pw.write("cd $PROJECT_DIR/output/onlinecomm\n");
				//				pw.write("  rm $PROJECT_DIR/output/onlinecomm/ExperimentOutput* \n");
				//				pw.write("  rm $PROJECT_DIR/output/onlinecomm/ExperimentDataPopulation* \n");
				pw.write("done\n");

				//				}

				pw.write("mv Convergence.dat " + nombreCarpetaConParametros+"/"+poblacion+"-Convergence.dat\n");
				pw.write("mv ConvergedNormativeSystems " + nombreCarpetaConParametros+"/"+poblacion+"-ConvergedNormativeSystems.dat\n");
				pw.write("mv ConvergedNormativeSystems.plot " + nombreCarpetaConParametros+"/"+poblacion+"-ConvergedNormativeSystems.plot\n");
				pw.write("mv FinalNorms " + nombreCarpetaConParametros+"/"+poblacion+"-FinalNorms.dat\n");
				pw.write("mv FinalNorms.plot " + nombreCarpetaConParametros+"/"+poblacion+"-FinalNorms.plot\n");
				pw.write("mv TotalNorms " + nombreCarpetaConParametros+"/"+poblacion+"-TotalNorms.dat\n");
				pw.write("mv TotalNorms.plot " + nombreCarpetaConParametros+"/"+poblacion+"-TotalNorms.plot\n");

				//				pw.write("cd $PROJECT_DIR/\n");
				//				pw.write("mv $PROJECT_DIR/ExperimentsOutputData/Convergence.dat ../VirtualCommunitiesSimulation/" + nombreCarpetaConParametros + "/" + poblacion+ "-Convergence.dat\n");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					// Nuevamente aprovechamos el finally para
					// asegurarnos que se cierra el fichero.
					if (null != fichero)
						fichero.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}

		//		
		//		
		//		
		//		
		//		String nombreCarpeta = ExperimentName.getText();
		//
		//		FileWriter fichero = null;
		//		PrintWriter pw = null;
		//		//String ejecutarRepast = "CLASSPATH=$CLASSPATH $JAVA_EXECUTABLE $JAVA_FLAGS -Xss10M -Xmx400M repast.simphony.batch.BatchMain -params $PROJECT_DIR/batch/batch_params.xml $PROJECT_DIR/virtualCommunities.rs\n";
		//		String ejecutarRepast = "CLASSPATH=$CLASSPATH $JAVA_EXECUTABLE $JAVA_FLAGS -Xss10M -Xmx400M repast.simphony.batch.BatchMain -params $PROJECT_DIR/batch/batch_params.xml $PROJECT_DIR/repast-settings/OnlineCommunity.rs\n";
		//		try{
		//			fichero = new FileWriter("Experiments_Launcher.sh");
		//			pw = new PrintWriter(fichero);
		//
		//			for(int i = 0 ; i < lectura.size() ; i++){
		//				pw.write(lectura.get(i));
		//				pw.write("\n");
		//			}
		//
		//			int launchCount = ExperimentLaunchListModel.getSize();
		//			for(int i = 0 ; i < launchCount ; i++){
		//				String populationURL = leerPopulationURL(i);
		//				String nombreCarpetaConParametros = "Experiments/"+nombreCarpeta +"_Content-"+totalComments+"_Agents-"+numberAgents+"_Viol-"+normViolationRate+"_StopTick-"+stopTick;
		//				String poblacion = sacarNombrePoblacion(populationURL);
		//
		//				for(int a = 1 ; a <= 10 ; a++){
		//
		//					//Crear carpeta dentro de la carpeta de experiments para guardar todos los ficheros del experimento.
		//					//					String nombreCarpetaConParametros = "Experiments/"+nombreCarpeta +"_Content-"+totalComments+"_Agents-"+numberAgents+"_Viol-"+normViolationRate+"_StopTick-"+stopTick+"_run-"+a;
		//					pw.write("mkdir "+nombreCarpetaConParametros+"\n");
		//
		//					//cambiarPoblacion (populationURL)
		//					String javaCambiarPopulation = "java -cp ../simulatorsTools/CambiarPopulation/bin Cambiar '"+populationURL+"' \n";
		//					pw.write(javaCambiarPopulation);
		//
		//					//cambiarBatchParams
		//					String javaCambiarBatchParams = "java -cp ../simulatorsTools/CambiarBatchParams/bin Principal "+totalComments+" "+numberAgents+" "+normViolationRate+" "+stopTick+" "+poblacion+"\n";
		//					pw.write(javaCambiarBatchParams);
		//
		//					//Ejecutar Repast
		//					pw.write(ejecutarRepast);
		//
		//					//copiar ficheros a carpeta del experimento
		//					//					String poblacion = sacarNombrePoblacion(populationURL);
		//					pw.write("cp " + populationURL + " " + nombreCarpetaConParametros+"\n");
		//
		//					//BatchParams
		//					/*pw.write("cd $PROJECT_DIR/ExperimentsOutputData/\n");
		//					pw.write("batchParams=`ls | grep '\\<batch_param'`\ncd ..\n");
		//					pw.write("mv ../VirtualCommunitiesSimulation/ExperimentsOutputData/$batchParams ../VirtualCommunitiesSimulation/"+nombreCarpetaConParametros+"/"+poblacion+"-BatchParams.dat\n");
		//					 */
		//					//Data file
		//					pw.write("cd $PROJECT_DIR/output/onlinecomm\n");
		//					pw.write("dataFile=`ls | grep 'ExperimentOutputData'`\ncd ../../\n");
		//					pw.write("mv ../NormLab/output/onlinecomm/$dataFile ../NormLab/"+nombreCarpetaConParametros+"/"+poblacion+"-run"+a+".dat\n");
		//
		//					//Crear grafica del experimento
		//					pw.write("python ../VirtualCommunitiesBatchLauncher/plotData.py ../NormLab/"+nombreCarpetaConParametros+"/"+poblacion+"-run"+a+".dat\n");
		//					pw.write("mv ExperimentGraphic.png "+nombreCarpetaConParametros+"/"+poblacion+"-Graphic-run"+a+".png\n");
		//
		//					pw.write("mv histogram.png "+nombreCarpetaConParametros+"/"+poblacion+"-Histogram-run"+a+".png\n");
		//					pw.write("cd $PROJECT_DIR/output/onlinecomm\n");
		//					pw.write("rm ExperimentOutput* \ncd ../../\n");
		//
		//
		//				}
		//				pw.write("cd $PROJECT_DIR/\n");
		//				pw.write("mv output/onlinecomm/Convergence.dat "+nombreCarpetaConParametros+"/"+poblacion+"-Convergence.dat\n");
		//
		//			}
		//			pw.write("popd");
		//
		//		}catch (Exception e) {
		//			e.printStackTrace();
		//		}finally{
		//			try{
		//				// Nuevamente aprovechamos el finally para 
		//				// asegurarnos que se cierra el fichero.
		//				if (null != fichero)
		//					fichero.close();
		//			} catch (Exception e2) {
		//				e2.printStackTrace();
		//			}
		//		}	
	}

	//	private void crearBashParaCluster() {
	//		ArrayList<String> lectura = leerFichero("Cluster-Classpath.txt");
	//
	//		String nombreCarpeta = ExperimentName.getText();
	//
	//		FileWriter fichero = null;
	//		PrintWriter pw = null;
	//		String ejecutarRepast = "CLASSPATH=$CLASSPATH $JAVA_EXECUTABLE $JAVA_FLAGS -Xss10M -Xmx400M repast.simphony.batch.BatchMain -params $PROJECT_DIR/batch/batch_params.xml $PROJECT_DIR/virtualCommunities.rs\n";
	//
	//		int launchCount = ExperimentLaunchListModel.getSize();
	//		try{
	//			fichero = new FileWriter("Experiments_Launcher.sh");
	//			pw = new PrintWriter(fichero);
	//			for (int j = 0; j < lectura.size(); j++) {
	//				pw.write(lectura.get(j));
	//				pw.write("\n");
	//			}
	//			for (int i = 0; i < launchCount; i++) {
	//				String populationURL = leerPopulationURL(i);
	//				String poblacion = sacarNombrePoblacion(populationURL);
	//
	//				String nombreCarpetaConParametros = "Experiments/"+nombreCarpeta +"_Content-"+totalComments+"_Agents-"+numberAgents+"_Viol-"+normViolationRate+"_StopTick-"+stopTick;
	//
	//
	//
	//				pw.write("cd $PROJECT_DIR\n");
	//
	//				//Crear carpeta dentro de la carpeta de experiments para guardar todos los ficheros del experimento.
	//				pw.write("mkdir "+nombreCarpetaConParametros+"\n");
	//				pw.write("cd Experiments/"+nombreCarpeta +"_Content-"+totalComments+"_Agents-"+numberAgents+"_Viol-"+normViolationRate+"_StopTick-"+stopTick+"\n");
	//
	//				pw.write("mkdir "+poblacion+"\n");
	//				pw.write("cd $PROJECT_DIR\n");
	//
	//				String javaCambiarPopulation = "java -cp ../CambiarPopulation/bin Cambiar '"+populationURL+"' \n";
	//				pw.write(javaCambiarPopulation);
	//
	//				//cambiarBatchParams
	//				String javaCambiarBatchParams = "java -cp ../CambiarBatchParams/bin Principal "+totalComments+" "+numberAgents+" "+normViolationRate+" "+stopTick+" "+poblacion+"\n";
	//				pw.write(javaCambiarBatchParams);
	//
	//				//copiar ficheros a carpeta del experimento
	//				pw.write("cp " + populationURL + " " + nombreCarpetaConParametros+"/"+poblacion+"\n");
	//
	//				pw.write("for i in `seq 1 100`; do\n");
	//				//Ejecutar Repast
	//				//pw.write("cd ../VirtualCommunitiesBatchLauncher/\n");
	//
	//
	//				//pw.write("qsub CLASSPATH=$CLASSPATH $JAVA_EXECUTABLE $JAVA_FLAGS -Xss10M -Xmx400M repast.simphony.batch.BatchMain -params $PROJECT_DIR/batch/batch_params.xml $PROJECT_DIR/virtualCommunities.rs\n");
	//				//pw.write("done\n");
	//
	//				pw.write("qsub ../VirtualCommunitiesBatchLauncher/ejecutarSimulador.sh\ndone\n");
	//				pw.write("while [ `ls ExperimentsOutputData | grep 'ExperimentOutput' | wc -l` -lt 100 ]\ndo\nsleep 30\ndone\n");
	//
	//				pw.write("cd $PROJECT_DIR/ExperimentsOutputData\n");
	//				pw.write("rm `ls | grep 'Data'`\n");
	//
	//				pw.write("mv `ls | grep 'ExperimentOutput'` /gpfs/home/iosu/workspace/VirtualCommunitiesSimulation/"+nombreCarpetaConParametros+"/"+poblacion+"\n");
	//
	//				//BatchParams
	//				/*pw.write("cd $PROJECT_DIR/ExperimentsOutputData/\n");
	//				pw.write("batchParams=`ls | grep '\\<batch_param'`\ncd ..\n");
	//				pw.write("mv $PROJECT_DIR/ExperimentsOutputData/$batchParams ../VirtualCommunitiesSimulation/"+nombreCarpetaConParametros+"/"+poblacion+"-BatchParams.dat\n");
	//
	//				//Data file
	//				pw.write("cd $PROJECT_DIR/ExperimentsOutputData/\n");
	//				pw.write("dataFile=`ls | grep 'ExperimentData"+poblacion+"'`\ncd ..\n");
	//				pw.write("mv $PROJECT_DIR/ExperimentsOutputData/$dataFile ../VirtualCommunitiesSimulation/"+nombreCarpetaConParametros+"/"+poblacion+"-run"+a+".dat\n");
	//
	//				//Crear grafica del experimento
	//				pw.write("#python ../VirtualCommunitiesBatchLauncher/plotData.py ../VirtualCommunitiesSimulation/"+nombreCarpetaConParametros+"/"+poblacion+"-run"+a+".dat\n");
	//				pw.write("#mv $PROJECT_DIR/ExperimentGraphic.png "+nombreCarpetaConParametros+"/"+poblacion+"-Graphic-run"+a+".png\n");
	//
	//				pw.write("mv $PROJECT_DIR/histogram.png ../VirtualCommunitiesSimulation/"+nombreCarpetaConParametros+"/"+poblacion+"-Histogram-run"+a+".png\n");
	//				 */
	//
	//				pw.write("cd $PROJECT_DIR/\n");
	//				pw.write("mv $PROJECT_DIR/ExperimentsOutputData/Convergence.dat ../VirtualCommunitiesSimulation/" + nombreCarpetaConParametros + "/" + poblacion+ "-Convergence.dat\n");
	//			}
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//				// Nuevamente aprovechamos el finally para
	//				// asegurarnos que se cierra el fichero.
	//				if (null != fichero)
	//					fichero.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//
	//	}

	private void createClusterBatchLauncher() {

		ArrayList<String> folders = leerFichero("batch/onlinecomm/classpath/Cluster-Folders.txt");
		ArrayList<String> classpath = leerFichero("batch/onlinecomm/classpath/Cluster-Classpath.txt");

		String nombreCarpeta = ExperimentName.getText();
		String experimentOutputFile = "ExperimentOutputData";

		FileWriter fichero = null;
		PrintWriter pw = null;

		//		String ejecutarRepast = "CLASSPATH=$CLASSPATH $JAVA_EXECUTABLE $JAVA_FLAGS -Xss10M -Xmx400M repast.simphony.batch.BatchMain -params $PROJECT_DIR/batch/batch_params.xml $PROJECT_DIR/virtualCommunities.rs\n";
		String ejecutarRepast = "CLASSPATH=$CLASSPATH $JAVA_EXECUTABLE $JAVA_FLAGS -Xss10M -Xms400M -Xmx1g repast.simphony.batch.BatchMain -params $PROJECT_DIR/batch/onlinecomm/batch_params.xml $PROJECT_DIR/repast-settings/OnlineCommunity.rs\n";

		int launchCount = ExperimentLaunchListModel.getSize();

		for (int i = 0; i < launchCount; i++) {
			try{
				fichero = new FileWriter("batch/onlinecomm/launchers/cluster/Experiments_Launcher_"+(i+1)+".sh");
				pw = new PrintWriter(fichero);


				for (int j = 0; j < folders.size(); j++) {
					pw.write(folders.get(j));
					pw.write("\n");
				}    

				pw.write("PROJECT_DIR=$WORKSPACE/Simulators/NormLabSimulators_"+(i+1)+"\n");

				for (int j = 0; j < classpath.size(); j++) {
					pw.write(classpath.get(j));
					pw.write("\n");
				}

				String populationURL = leerPopulationURL(i);
				String nombreCarpetaConParametros = "$NORMLAB_DIR/output/onlinecomm/"+nombreCarpeta +"_Content-"+totalComments+"_Agents-"+numberAgents+"_Viol-"+normViolationRate+"_StopTick-"+stopTick;
				String poblacion = sacarNombrePoblacion(populationURL);

				//Crear carpeta dentro de la carpeta de experiments para guardar todos los ficheros del experimento.
				//				String nombreCarpetaConParametros = "Experiments/"+nombreCarpeta +"_Content-"+totalComments+"_Agents-"+numberAgents+"_Viol-"+normViolationRate+"_StopTick-"+stopTick+"_run-"+a;
				pw.write("mkdir "+nombreCarpetaConParametros+"\n");
				pw.write("mkdir $NORMLAB_DIR/output/onlinecomm/norms\n");

				//cambiarPoblacion (populationURL)
				String javaCambiarPopulation = "java -cp $PROJECT_DIR/bin es.csic.iiia.normlab.onlinecomm.batch.PopulationSwitcher '"+populationURL+"' \n";
				pw.write(javaCambiarPopulation);

				//cambiarBatchParams
				String javaCambiarBatchParams = "java -cp $PROJECT_DIR/bin es.csic.iiia.normlab.onlinecomm.batch.BatchParamsSwitcher "+totalComments+" "+numberAgents+" "+normViolationRate+" "+stopTick+" "+poblacion+"\n";
				pw.write(javaCambiarBatchParams);

				//copiar ficheros a carpeta del experimento
				//				String poblacion = sacarNombrePoblacion(populationURL);
				pw.write("cp " + populationURL + " " + nombreCarpetaConParametros+"\n\n");

				// TODO: Javi... elimino el for y lo pongo en el script...
				//				for(int a = 1 ; a <= 100 ; a++){

				
				pw.write("for i in `seq 10`; do\n");

				pw.write("  cd $PROJECT_DIR\n");

				//Ejecutar Repast
				pw.write("  " + ejecutarRepast);

				pw.write("  cd $PROJECT_DIR/output/onlinecomm\n");
				// pw.write("  dataFile=`ls | grep 'ExperimentOutputData'`\n");
				pw.write("  mv " + experimentOutputFile + " " + nombreCarpetaConParametros+"/"+poblacion+"-run$i.dat\n");

				//Crear grafica del experimento
				//pw.write("  cd $NORMLAB_DIR/batch/onlinecomm/scripts\n");
				//pw.write("  python plotData.py " + nombreCarpetaConParametros+"/"+poblacion+"-run$i.dat\n");
				//				pw.write("  mv ExperimentGraphic.png "+nombreCarpetaConParametros+"/"+poblacion+"-Graphic-run"+i+".png\n");
				//				pw.write("  mv histogram.png "+nombreCarpetaConParametros+"/"+poblacion+"-Histogram-run"+i+".png\n");
				//pw.write("  cd $PROJECT_DIR/output/onlinecomm\n");
				pw.write("  rm " + experimentOutputFile + "* \n");
				pw.write("  mv norms/ "+ nombreCarpetaConParametros+"/"+poblacion+"-run$i-norms\n");
				pw.write("  mkdir norms\n");

				//Crear grafica del experimento
				//pw.write("python ../VirtualCommunitiesBatchLauncher/plotData.py ../NormLab/"+nombreCarpetaConParametros+"/"+poblacion+"-run"+a+".dat\n");
				//pw.write("mv ExperimentGraphic.png "+nombreCarpetaConParametros+"/"+poblacion+"-Graphic-run"+a+".png\n");

				//					pw.write("mv histogram.png ../../NormLab/"+nombreCarpetaConParametros+"/"+poblacion+"-Histogram-run"+a+".png\n");
				//					pw.write("cd $PROJECT_DIR/output/onlinecomm\n");
				//				pw.write("  rm $PROJECT_DIR/output/onlinecomm/ExperimentOutput* \n");
				//				pw.write("  rm $PROJECT_DIR/output/onlinecomm/ExperimentDataPopulation* \n");
				pw.write("done\n");

				//				}

				pw.write("mv Convergence.dat " + nombreCarpetaConParametros+"/"+poblacion+"-Convergence.dat\n");
				pw.write("mv ConvergedNormativeSystems " + nombreCarpetaConParametros+"/"+poblacion+"-ConvergedNormativeSystems.dat\n");
				pw.write("mv ConvergedNormativeSystems.plot " + nombreCarpetaConParametros+"/"+poblacion+"-ConvergedNormativeSystems.plot\n");
				pw.write("mv FinalNorms " + nombreCarpetaConParametros+"/"+poblacion+"-FinalNorms.dat\n");
				pw.write("mv FinalNorms.plot " + nombreCarpetaConParametros+"/"+poblacion+"-FinalNorms.plot\n");
				pw.write("mv TotalNorms " + nombreCarpetaConParametros+"/"+poblacion+"-TotalNorms.dat\n");
				pw.write("mv TotalNorms.plot " + nombreCarpetaConParametros+"/"+poblacion+"-TotalNorms.plot\n");

//				
//				pw.write("for i in `seq 10`; do\n");
//
//				pw.write("  cd $PROJECT_DIR\n");
//
//				//Ejecutar Repast
//				pw.write("  " + ejecutarRepast);
//
//				pw.write("  cd $PROJECT_DIR/output/onlinecomm\n");
//				pw.write("  dataFile=`ls | grep 'ExperimentOutputData'`");
//				pw.write("  mv $PROJECT_DIR/output/onlinecomm/$dataFile "+nombreCarpetaConParametros+"/"+poblacion+"-run$i.dat\n");
//
//				//Crear grafica del experimento
//				//pw.write("python ../VirtualCommunitiesBatchLauncher/plotData.py ../NormLab/"+nombreCarpetaConParametros+"/"+poblacion+"-run"+a+".dat\n");
//				//pw.write("mv ExperimentGraphic.png "+nombreCarpetaConParametros+"/"+poblacion+"-Graphic-run"+a+".png\n");
//
//				//					pw.write("mv histogram.png ../../NormLab/"+nombreCarpetaConParametros+"/"+poblacion+"-Histogram-run"+a+".png\n");
//				//					pw.write("cd $PROJECT_DIR/output/onlinecomm\n");
//				pw.write("  rm $PROJECT_DIR/output/onlinecomm/ExperimentOutput* \n");
//				pw.write("  rm $PROJECT_DIR/output/onlinecomm/ExperimentDataPopulation* \n");
//				pw.write("done\n");
//
//				//				}
//
//				pw.write("mv $PROJECT_DIR/output/onlinecomm/Convergence.dat "+nombreCarpetaConParametros+"/"+poblacion+"-Convergence.dat\n");
//
//
//				//				pw.write("cd $PROJECT_DIR/\n");
//				//				pw.write("mv $PROJECT_DIR/ExperimentsOutputData/Convergence.dat ../VirtualCommunitiesSimulation/" + nombreCarpetaConParametros + "/" + poblacion+ "-Convergence.dat\n");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					// Nuevamente aprovechamos el finally para
					// asegurarnos que se cierra el fichero.
					if (null != fichero)
						fichero.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}

	}

	private String sacarNombrePoblacion(String populationURL) {
		String[] cadena = populationURL.split("\\/");
		String[] cadena2 = cadena[cadena.length - 1].split("\\.");
		return cadena2[0];
	}

	private ArrayList<String> leerFichero(String fichero) {
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

	private void launchExperiments() {
		int launchCount = ExperimentLaunchListModel.getSize();
		for(int i = 0 ; i < launchCount ; i++){
			String populationURL = leerPopulationURL(i);
			copiarPopulation(populationURL);
			cambiarBathParams();

			//ejecutar();	
		}
		System.out.println("Launch finished....");
	}

	private void ejecutar(){
		try 
		{
			StringBuilder sb = new StringBuilder(2000);
			Reader r;
			int ch;

			Runtime run = Runtime.getRuntime();
			String[] cmd = {"/bin/bash", "-c", "./launch.sh"};
			Process p = run.exec(cmd);

			//			ProcessBuilder pb = new ProcessBuilder("bash", "launch.sh");
			//			Process p = pb.start();

			r = new InputStreamReader(p.getInputStream());

			while((ch = r.read()) != -1) {
				sb.append((char)ch);
			}
			//p.waitFor();
			System.out.println(sb);			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	private void cambiarBathParams() {
		try {
			File file = new File("../VirtualCommunitiesSimulation/batch/batch_params.xml");

			//Create instance of DocumentBuilderFactory
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			//Get the DocumentBuilder
			DocumentBuilder docBuilder = factory.newDocumentBuilder();

			//Using existing XML Document
			Document doc = docBuilder.parse(file);

			NodeList nList = doc.getElementsByTagName("parameter");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					if(eElement.getAttribute("name").equals("Total Comments")){
						eElement.setAttribute("value", ""+totalComments);
					}
					if(eElement.getAttribute("name").equals("maxAgents")){
						eElement.setAttribute("value", ""+numberAgents);
					}
					if(eElement.getAttribute("name").equals("Norm Violation Rate")){
						eElement.setAttribute("value", ""+normViolationRate);
					}
					if(eElement.getAttribute("name").equals("StopTick")){
						eElement.setAttribute("value", ""+stopTick);
					}
				}

			}
			//set up a transformer
			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();

			//create string from xml tree
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			DOMSource source = new DOMSource(doc);
			trans.transform(source, result);
			String xmlString = sw.toString();

			OutputStream f0;

			byte buf[] = xmlString.getBytes();
			f0 = new FileOutputStream("../VirtualCommunitiesSimulation/batch/batch_params.xml");
			for(int i=0;i<buf .length;i++) {
				f0.write(buf[i]);
			}
			f0.close();
			buf = null;
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		catch(ParserConfigurationException e) {
			e.printStackTrace();
		} 
		catch(TransformerException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	private String leerPopulationURL(int i) {
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

	private void copiarPopulation(String populationURL) {
		System.out.println(populationURL);
		File origen = new File(populationURL);
		File destino = new File("../VirtualCommunitiesSimulation/Experiments/population.xml");

		InputStream in;
		OutputStream out;
		try {
			in = new FileInputStream(origen);
			out = new FileOutputStream(destino);
			byte[] buf = new byte[1024];
			int len;

			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
