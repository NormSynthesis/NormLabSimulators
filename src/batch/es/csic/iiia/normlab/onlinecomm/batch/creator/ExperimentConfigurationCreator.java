package es.csic.iiia.normlab.onlinecomm.batch.creator;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import es.csic.iiia.normlab.onlinecomm.agents.CommunityAgent;
import es.csic.iiia.normlab.onlinecomm.agents.profile.ComplaintProfile;
import es.csic.iiia.normlab.onlinecomm.agents.profile.UploadProfile;
import es.csic.iiia.normlab.onlinecomm.agents.profile.ViewProfile;
import es.csic.iiia.normlab.onlinecomm.batch.ExperimentConfigurationCreatorXMLParser;

/**
 * 
 * @author "Javier Morales (jmorales@iiia.csic.es)"
 *
 */
public class ExperimentConfigurationCreator {

	Scanner teclado;
	int stopTick = 5000;
	int contentsPerCommunityAgent = 5000;
	int agents = 10;
	int runs = 3;

	int cuenta = 0;
	int cuenta2 = 0;

	final int moderate = 1;
	final int spammer = 2;
	final int porno = 3;
	final int violent = 4;
	final int rude = 5;

	double low = 0.3;
	double medium = 0.5;
	double high = 0.7;

	UploadProfile up;
	ViewProfile vp;
	ComplaintProfile cp;

	int quantity;
	String poblacion;

	ArrayList<Double> combinations;
	ArrayList<Double> cpCombinations;
	ArrayList<Double> combinationsMixedPopu;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExperimentConfigurationCreator cm = new ExperimentConfigurationCreator();
		cm.start();
	}

	/**
	 * 
	 */
	public ExperimentConfigurationCreator (){
		combinations = new ArrayList<Double>();
		combinations.add(low);
		combinations.add(medium);
		combinations.add(high);

		cpCombinations = new ArrayList<Double>();
		cpCombinations.add(low);
		cpCombinations.add(medium);
		cpCombinations.add(high);
		cpCombinations.add(1.0);
		/*cpCombinations.add(0.275);
		cpCombinations.add(0.375);
		cpCombinations.add(0.625);*/

		combinationsMixedPopu = new ArrayList<Double>();
		combinationsMixedPopu.add(0.2);
		combinationsMixedPopu.add(0.3);
		combinationsMixedPopu.add(0.5);

		teclado = new Scanner(System.in);
	}

	/**
	 * 
	 */
	private void start() {
		System.out.println("Pure experiments (1), mixed behaviour experiments (2), mixed population experiments (3)");
		int experimento = teclado.nextInt();

		switch (experimento){
		case 1: 
			pureExperiment();				
			break;
		case 2:
			mixedBehaviour();
			break;
		case 3:
			mixedPopulation();
		}
	}
	private void mixedPopulation() {
		System.out.println("Choose three agent types:\n\tModerate (1)\n\tSpammer (2)\n\tPornographic (3)\n\tViolent (4)\n\tRude(5)");
		int type1 = teclado.nextInt();
		System.out.println("The second type:");
		int type2 = teclado.nextInt();
		System.out.println("The third type:");
		int type3 = teclado.nextInt();

		System.out.println("How many agents:");
		quantity = teclado.nextInt();

		double popu1=0;
		double popu2=0;
		double popu3=0;
		double cp1;

		for(int i = 0 ; i < combinationsMixedPopu.size() ; i++){
			popu1 = combinationsMixedPopu.get(i);
			popu2 = 1 - popu1;
			if(popu2 == 0.8){
				popu2 = 0.3;
				popu3 = 0.5;
			}else if(popu2 == 0.7){
				popu2 = 0.2;
				popu3 = 0.5;
			}else if(popu2 == 0.5){
				popu2 = 0.2;
				popu3 = 0.3;
			}

			for(int j = 0 ; j < cpCombinations.size() ; j++){
				cp1 = cpCombinations.get(j);
				poblacion = crearPoblacion(type1, popu1, cp1, type2, popu2, type3, popu3, quantity);
				crearExperimento(poblacion);

			}
		}
	}

	/**
	 * 
	 * @param type1
	 * @param popu1
	 * @param cp1
	 * @param type2
	 * @param popu2
	 * @param type3
	 * @param popu3
	 * @param quantity
	 * @return
	 */
	private String crearPoblacion(int type1, double popu1, double cp1, int type2, double popu2, int type3, double popu3, int quantity) {

		ArrayList<CommunityAgent> agentArray = new ArrayList<CommunityAgent>();
		int quantity1 = (int) (popu1 * quantity);
		CommunityAgent a = createCommunityAgent(type1, cp1, quantity1);
		agentArray.add(a);
		int quantity2 = (int) (popu2 * quantity);
		CommunityAgent b = createCommunityAgent(type2, 0, quantity2);
		agentArray.add(b);
		int quantity3 = (int) (popu3 * quantity);
		CommunityAgent c = createCommunityAgent(type2, 0, quantity3);
		agentArray.add(c);

		cuenta++;
		String texto = "Population"+cuenta;
		new ExperimentConfigurationCreatorXMLParser(texto, agentArray);


		return "../batch/onlinecomm/populations/"+texto+".xml";
	}

	/**
	 * 
	 */
	private void pureExperiment() {
		System.out.println("Choose two agent types:\n\tModerate (1)\n\tSpammer (2)\n\tPornographic (3)\n\tViolent (4)\n\tRude(5)");
		int type1 = teclado.nextInt();
		System.out.println("The second type:");
		int type2 = teclado.nextInt();

		System.out.println("How many agents:");
		quantity = teclado.nextInt();


		double popu1=0;
		double popu2=0;
		double cp1;

		for(int i = 0 ; i < combinations.size() ; i++){
			popu1 = combinations.get(i);
			popu2 = 1 - popu1;
			BigDecimal aux = new BigDecimal(Double.toString(popu2));
			aux.setScale(1, BigDecimal.ROUND_HALF_DOWN);
			popu2 = aux.doubleValue();

			for(int j = 0 ; j < cpCombinations.size() ; j++){
				cp1 = cpCombinations.get(j);

				//Si no se quiere que los spammers o los otros tengan complaint profile quitar el cp1 la segunda vez y poner un 0
				//poblacion = crearPoblacion(type1, popu1, cp1, type2, popu2, cp1, quantity);//cp to anothers
				poblacion = crearPoblacion(type1, popu1, cp1, type2, popu2, 0, quantity);// no cp to anothers

				crearExperimento(poblacion);
			}
		}
	}

	/**
	 * 
	 * @param poblacion
	 */
	private void crearExperimento(String poblacion) {
		int numCommunityAgents = quantity;

		for(int i = 0 ; i < 3 ; i++){
			switch(i){
			case 0:
				CrearXMLDelExperimento(poblacion, contentsPerCommunityAgent, numCommunityAgents, stopTick, 0.3f);
				break;
			case 1:
				CrearXMLDelExperimento(poblacion, contentsPerCommunityAgent, numCommunityAgents, stopTick, 0.5f);
				break;
			case 2:
				CrearXMLDelExperimento(poblacion, contentsPerCommunityAgent, numCommunityAgents, stopTick, 0.7f);
				break;
			}
		}
	}

	/**
	 * 
	 * @param urlPopulation
	 * @param totalComments
	 * @param numCommunityAgents
	 * @param stopTick
	 * @param normViolation
	 */
	private void CrearXMLDelExperimento(String urlPopulation, int totalComments, int numCommunityAgents, int stopTick, float normViolation) {
		try{
			cuenta2++;
			String name = "Configuration"+cuenta2;

			DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
			DocumentBuilder build = dFact.newDocumentBuilder();
			Document doc = build.newDocument();

			Element	root = doc.createElement("Experiment");
			doc.appendChild(root);

			Element Details = doc.createElement("PopulationURL");
			Details.appendChild(doc.createTextNode(urlPopulation));
			root.appendChild(Details);     

			Details = doc.createElement("TotalComments");
			Details.appendChild(doc.createTextNode(""+totalComments));
			root.appendChild(Details); 

			Details = doc.createElement("NumberAgents");
			Details.appendChild(doc.createTextNode(""+numCommunityAgents));
			root.appendChild(Details); 

			Details = doc.createElement("NormViolationRate");
			Details.appendChild(doc.createTextNode(""+normViolation));
			root.appendChild(Details); 

			Details = doc.createElement("StopTick");
			Details.appendChild(doc.createTextNode(""+stopTick));
			root.appendChild(Details); 

			// Save the document to the disk file
			TransformerFactory tranFactory = TransformerFactory.newInstance();
			Transformer aTransformer = tranFactory.newTransformer();

			// format the XML nicely
			aTransformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");

			aTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");

			DOMSource source = new DOMSource(doc);
			try {

				FileWriter fos = new FileWriter("batch/onlinecomm/configurations/"+name+".xml");
				StreamResult result = new StreamResult(fos);
				aTransformer.transform(source, result);

			} catch (IOException e) {

				e.printStackTrace();
			}
		} catch (TransformerException ex) {
			System.out.println("Error outputting document");

		} catch (ParserConfigurationException ex) {
			System.out.println("Error building document");
		}
	}

	/**
	 * 
	 * @param type1
	 * @param type1QuantityPer
	 * @param type1ComplaintPer
	 * @param type2
	 * @param type2QuantityPer
	 * @param type2ComplaintPer
	 * @param quantity
	 * @return
	 */
	private String crearPoblacion(int type1, double type1QuantityPer, double type1ComplaintPer, int type2, 
			double type2QuantityPer, double type2ComplaintPer, int quantity) {

		ArrayList<CommunityAgent> agentArray = new ArrayList<CommunityAgent>();
		int quantity1 = (int) (type1QuantityPer * quantity);
		CommunityAgent a = createCommunityAgent(type1, type1ComplaintPer, quantity1);
		agentArray.add(a);
		int quantity2 = (int) (type2QuantityPer * quantity);
		CommunityAgent b = createCommunityAgent(type2, type2ComplaintPer, quantity2);
		agentArray.add(b);

		cuenta++;
		String texto = "Population"+cuenta;
		new ExperimentConfigurationCreatorXMLParser(texto, agentArray);


		return "batch/onlinecomm/populations/"+texto+".xml";
	}

	/**
	 * 
	 * @param type
	 * @param complaintPer
	 * @param quantity
	 * @return
	 */
	private CommunityAgent createCommunityAgent(int type, double complaintPer, int quantity) {
		String name = null;

		UploadProfile up = null;
		ViewProfile vp = null;
		ComplaintProfile cp = null;

		switch(type){
		case moderate:
			up = new UploadProfile(1, 1, 0, 0, 0, 0);
			vp = new ViewProfile(0.4,0.4,0.2,2);
			cp = new ComplaintProfile(complaintPer, complaintPer, complaintPer, complaintPer);
			name = "moderate";
			break;
		case spammer:
			up = new UploadProfile(1, 0, 1, 0, 0, 0);
			vp = new ViewProfile(0.4,0.4,0.2,2);
			cp = new ComplaintProfile(complaintPer, complaintPer, complaintPer, complaintPer);
			name = "spammer";
			break;
		case porno:
			up = new UploadProfile(1, 0, 0, 1, 0, 0);
			vp = new ViewProfile(0.4,0.4,0.2,2);
			cp = new ComplaintProfile(0,0,0,0);		
			name = "pornographic";
			break;
		case violent:
			up = new UploadProfile(1, 0, 0, 0, 1, 0);
			vp = new ViewProfile(0.4,0.4,0.2,2);
			cp = new ComplaintProfile(0,0,0,0);
			name = "violent";
			break;
		case rude:
			up = new UploadProfile(1, 0, 0, 0, 0, 1);
			vp = new ViewProfile(0.4,0.4,0.2,2);
			cp = new ComplaintProfile(0,0,0,0);
			name = "rude";
			break;
		}
		CommunityAgent newCommunityAgent = new CommunityAgent(type, quantity, name, up, vp, cp, "");
		return newCommunityAgent;
	}

	/**
	 * 
	 */
	private void mixedBehaviour() {
		System.out.println("Choose an CommunityAgent type:\n\tModerate (1)\n\tSpammer (2)\n\tPornographic (3)\n\tViolent (4)\n\tRude(5)");
		int type1 = teclado.nextInt();
		System.out.println("Made an CommunityAgent type choosing what two contents it uploads:\n\tModerate (1)\n\tSpammer (2)\n\tPornographic (3)\n\tViolent (4)\n\tRude(5)");
		System.out.println("These contents will be uploaded equitably.");
		int content1 = teclado.nextInt();

		System.out.println("The second content:");
		int content2 = teclado.nextInt();
		System.out.println("How many agents:");
		quantity = teclado.nextInt();

		double popu1=0;
		double popu2=0;
		double cp;
		double con1;
		double con2;
		double normVio = 0;

		for(int i = 0 ; i < combinations.size() ; i++){
			popu1 = combinations.get(i);
			popu2 = 1 - popu1;
			BigDecimal aux = new BigDecimal(Double.toString(popu2));
			aux.setScale(1, BigDecimal.ROUND_HALF_DOWN);
			popu2 = aux.doubleValue();
			
			for(int j = 0 ; j < cpCombinations.size() ; j++){
				cp = cpCombinations.get(j);
				String poblacion = createPopulations(type1, content1, content2, quantity, popu1, popu2, cp, 0.5, 0.5, normVio);
				crearExperimento(poblacion);
			}
			
//			for(int j = 0 ; j < combinations.size() ; j++){
//				cp = combinations.get(j);
//				for(int k = 0 ; k < combinations.size() ; k++){
//					con1 = combinations.get(k);
//					con2 = 1 - con1;
//					BigDecimal aux2 = new BigDecimal(Double.toString(con2));
//					aux2.setScale(1, BigDecimal.ROUND_HALF_DOWN);
//					con2 = aux2.doubleValue();
//
//					if(con2 > 0.29 && con2 < 0.31)
//						con2 = 0.3;
//
//					//for(int l = 0 ; l < combinations.size() ; l++){
//					//normVio = combinations.get(l);
//					String poblacion = createPopulations(type1, content1, content2, quantity, popu1, popu2, cp, con1, con2, normVio);
//					crearExperimento(poblacion);
//					//}
//				}
//			}
		}
	}

	/**
	 * 
	 * @param agentType1
	 * @param contentType1
	 * @param contentType2
	 * @param agentQuantity
	 * @param agentType1Population
	 * @param agentType2Population
	 * @param agent1ComplaintProfile
	 * @param agent2Content1Percentage
	 * @param agent2Content2Percentage
	 * @param normViolationPercentage
	 * @return
	 */
	private String createPopulations(int agentType1, int contentType1, int contentType2, int agentQuantity, double agentType1Population, double agentType2Population, double agent1ComplaintProfile, double agent2Content1Percentage, double agent2Content2Percentage, double normViolationPercentage) {
		ArrayList<CommunityAgent> agentArray = new ArrayList<CommunityAgent>();
		int quantity1 = (int) (agentType1Population * quantity);
		CommunityAgent a = createCommunityAgent(agentType1, agent1ComplaintProfile, quantity1);
		agentArray.add(a);
		int quantity2 = (int) (agentType2Population * quantity);
		CommunityAgent b = createCommunityAgent(quantity2, contentType1, contentType2, agent2Content1Percentage, agent2Content2Percentage);
		agentArray.add(b);

		cuenta++;
		String texto = "Population"+cuenta;
		new ExperimentConfigurationCreatorXMLParser(texto, agentArray);

		return "batch/onlinecomm/populations/"+texto+".xml";

	}

	/**
	 * 
	 * @param quantity2
	 * @param contentType1
	 * @param contentType2
	 * @param agent2Content1Percentage
	 * @param agent2Content2Percentage
	 * @return
	 */
	private CommunityAgent createCommunityAgent(int quantity2, int contentType1, int contentType2, double agent2Content1Percentage, double agent2Content2Percentage) {
		ArrayList<Double> upPers;
		String name = null;

		UploadProfile up = null;
		ViewProfile vp = null;
		ComplaintProfile cp = null;

		upPers = uploadProfilePercentages(contentType1, contentType2, agent2Content1Percentage, agent2Content2Percentage);

		up = new UploadProfile(1, upPers.get(0), upPers.get(1), upPers.get(2), upPers.get(3), upPers.get(4));
		vp = new ViewProfile(0.4,0.4,0.2,2);
		cp = new ComplaintProfile(0, 0, 0, 0);
		name = "another";


		CommunityAgent newCommunityAgent = new CommunityAgent(spammer, quantity2, name, up, vp, cp, name);
		return newCommunityAgent;
	}

	/**
	 * 
	 * @param contentType1
	 * @param contentType2
	 * @param agent2Content1Percentage
	 * @param agent2Content2Percentage
	 * @return
	 */
	private ArrayList<Double> uploadProfilePercentages(int contentType1, int contentType2, double agent2Content1Percentage, double agent2Content2Percentage) {
		ArrayList<Double> upPers = new ArrayList<Double>();

		for(int i = 0 ; i < 5 ; i++){
			upPers.add(0.0);
		}
		upPers.set((contentType1-1), agent2Content1Percentage);
		upPers.set((contentType2-1), agent2Content2Percentage);
		return upPers;
	}
}
