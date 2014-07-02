package es.csic.iiia.normlab.onlinecomm.XMLParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.csic.iiia.normlab.onlinecomm.agents.CommunityAgent;
import es.csic.iiia.normlab.onlinecomm.agents.profile.ComplaintProfile;
import es.csic.iiia.normlab.onlinecomm.agents.profile.UploadProfile;
import es.csic.iiia.normlab.onlinecomm.agents.profile.ViewProfile;

/**
 * Class to manipulate or create XML files.
 * 
 * @author Iosu Mendizabal
 *
 */
public class XMLParser {

	String xmlFileName;
	ArrayList<CommunityAgent> agente;

	/**
	 * First constructor of the XMLParser
	 * 
	 * @param xmlFileName
	 * 			Name of the XML file to write.
	 * @param agente
	 * 			ArrayList of the agents to save in the XML.
	 */
	public XMLParser(String xmlFileName, ArrayList<CommunityAgent> agente) {
		this.xmlFileName = xmlFileName;
		this.agente = agente;
		writeXmlFile(agente);
	}

	/**
	 * Second and empty constructor of the XMLParser.
	 */
	public XMLParser() {
	}

	/**
	 * Method to write an XML file with a given ArrayList.
	 * 
	 * @param agente
	 * 			ArrayList of agents that we want to save in the XML.
	 */
	public void writeXmlFile(ArrayList<CommunityAgent> agente) {
		try{
			DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
			DocumentBuilder build = dFact.newDocumentBuilder();
			Document doc = build.newDocument();

			Element	root = doc.createElement("Agents");
			doc.appendChild(root);

			for(int i = 0 ; i < agente.size() ; i++ ) { 
				if(agente.get(i).getQuantity() > 0){
					Element Details = doc.createElement("Agent");
					root.appendChild(Details);

					Element quantity = doc.createElement("quantity");
					quantity.appendChild(doc.createTextNode(String.valueOf(agente.get(i).getQuantity())));
					Details.appendChild(quantity);

					Element name = doc.createElement("name");
					name.appendChild(doc.createTextNode(String.valueOf(agente.get(i).getName())));
					Details.appendChild(name);

					Element correct = doc.createElement("correct");
					correct.appendChild(doc.createTextNode(String.valueOf(agente.get(i).getUploadProfile().getCorrect())));
					Details.appendChild(correct);

					Element insult = doc.createElement("insult");
					insult.appendChild(doc.createTextNode(String.valueOf(agente.get(i).getUploadProfile().getInsult())));
					Details.appendChild(insult);

					Element spam = doc.createElement("spam");
					spam.appendChild(doc.createTextNode(String.valueOf(agente.get(i).getUploadProfile().getSpam())));
					Details.appendChild(spam);

					Element violent = doc.createElement("violent");
					violent.appendChild(doc.createTextNode(String.valueOf(agente.get(i).getUploadProfile().getViolent())));
					Details.appendChild(violent);

					Element porn = doc.createElement("porn");
					porn.appendChild(doc.createTextNode(String.valueOf(agente.get(i).getUploadProfile().getPorn())));
					Details.appendChild(porn);

					Element uploadContent = doc.createElement("uploadProbability");
					uploadContent.appendChild(doc.createTextNode(String.valueOf(agente.get(i).getUploadProfile().getUploadProbability())));
					Details.appendChild(uploadContent);

					Element forum = doc.createElement("secForum");
					forum.appendChild(doc.createTextNode(String.valueOf(agente.get(i).getViewProfile().getForum())));
					Details.appendChild(forum);

					Element reporter = doc.createElement("secTheReporter");
					reporter.appendChild(doc.createTextNode(String.valueOf(agente.get(i).getViewProfile().getTheReporter())));
					Details.appendChild(reporter);

					Element photoVideo = doc.createElement("secPhotoVideo");
					photoVideo.appendChild(doc.createTextNode(String.valueOf(agente.get(i).getViewProfile().getPhotoVideo())));
					Details.appendChild(photoVideo);

					Element viewMode = doc.createElement("viewMode");
					viewMode.appendChild(doc.createTextNode(String.valueOf(agente.get(i).getViewProfile().getViewMode())));
					Details.appendChild(viewMode);

					Element distributionMode = doc.createElement("distributionMode");
					distributionMode.appendChild(doc.createTextNode(String.valueOf(0)));
					Details.appendChild(distributionMode);

					Element cInsult = doc.createElement("complaintInsult");
					cInsult.appendChild(doc.createTextNode(String.valueOf(agente.get(i).getComplaintProfile().getInsult())));
					Details.appendChild(cInsult);

					Element cSpam = doc.createElement("complaintSpam");
					cSpam.appendChild(doc.createTextNode(String.valueOf(agente.get(i).getComplaintProfile().getSpam())));
					Details.appendChild(cSpam);

					Element cViolent = doc.createElement("complaintViolent");
					cViolent.appendChild(doc.createTextNode(String.valueOf(agente.get(i).getComplaintProfile().getViolent())));
					Details.appendChild(cViolent);

					Element cPorn = doc.createElement("complaintPorn");
					cPorn.appendChild(doc.createTextNode(String.valueOf(agente.get(i).getComplaintProfile().getPorn())));
					Details.appendChild(cPorn);

					Element popName = doc.createElement("populationName");
					popName.appendChild(doc.createTextNode(String.valueOf(xmlFileName)));
					Details.appendChild(popName);
				}
			}

			/* Save the document to the disk file */
			TransformerFactory tranFactory = TransformerFactory.newInstance();
			Transformer aTransformer = tranFactory.newTransformer();

			/* Format the XML nicely */
			aTransformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
			aTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");

			DOMSource source = new DOMSource(doc);
			try {
				File file = new File("files/onlinecomm/populations/" + xmlFileName + ".xml");
				if(!file.exists()) {
					file.createNewFile();
				}
				BufferedWriter fos = new BufferedWriter(new FileWriter(file));
				
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
	 * Method to read an XML file an return an agent ArrayList.
	 * 
	 * @return The ArrayList of agents read from the XML file.
	 */
	public ArrayList<CommunityAgent> getPopulationFromXML() {
		ArrayList<CommunityAgent> agenteDeXML = new ArrayList<CommunityAgent>();

		try {

//		File fXmlFile = new File("Experiments/population.xml");
			File fXmlFile = new File("files/onlinecomm/populations/population.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("Agente");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					Double correct = Double.parseDouble(eElement.getElementsByTagName(
							"Correct").item(0).getTextContent());
					Double spam = Double.parseDouble(eElement.getElementsByTagName(
							"Spam").item(0).getTextContent());
					Double porn = Double.parseDouble(eElement.getElementsByTagName(
							"Porn").item(0).getTextContent());
					Double violent = Double.parseDouble(eElement.getElementsByTagName(
							"Violent").item(0).getTextContent());
					Double insult = Double.parseDouble(eElement.getElementsByTagName(
							"Insult").item(0).getTextContent());
					Double uploadProb = Double.parseDouble(eElement.getElementsByTagName(
							"UploadProbability").item(0).getTextContent());

					//Upload profile settings
					UploadProfile uploadProfile = new UploadProfile(uploadProb, 
							correct, spam, porn, violent, insult);

					//View profile settings
					ViewProfile viewProfile = new ViewProfile(
							Double.parseDouble(eElement.getElementsByTagName("SecForum").item(0).getTextContent()),
							Double.parseDouble(eElement.getElementsByTagName("SecTheReporter").item(0).getTextContent()),
							Double.parseDouble(eElement.getElementsByTagName("SecPhotoVideo").item(0).getTextContent()), 
							Integer.parseInt(eElement.getElementsByTagName("ViewMode").item(0).getTextContent()));
					
					//Complaint Profile
					ComplaintProfile complaintProfile = new ComplaintProfile(
							Double.parseDouble(eElement.getElementsByTagName("ComplaintSpam").item(0).getTextContent()),
							Double.parseDouble(eElement.getElementsByTagName("ComplaintPorn").item(0).getTextContent()),
							Double.parseDouble(eElement.getElementsByTagName("ComplaintViolent").item(0).getTextContent()),
							Double.parseDouble(eElement.getElementsByTagName("ComplaintInsult").item(0).getTextContent()));

					int numAgent = 0;
					if(eElement.getElementsByTagName("Name").item(0).getTextContent().equals("Moderate")){
						numAgent = 1;
					}
					if(eElement.getElementsByTagName("Name").item(0).getTextContent().equals("Spammer")){
						numAgent = 2;
					}
					if(eElement.getElementsByTagName("Name").item(0).getTextContent().equals("Pornographic")){
						numAgent = 3;
					}
					if(eElement.getElementsByTagName("Name").item(0).getTextContent().equals("Violent")){
						numAgent = 4;
					}
					if(eElement.getElementsByTagName("Name").item(0).getTextContent().equals("Rude")){
						numAgent = 5;
					}
					if(eElement.getElementsByTagName("Name").item(0).getTextContent().equals("another")){
						numAgent = 6;
					}
					CommunityAgent moderate = new CommunityAgent(
							numAgent, Integer.parseInt(eElement.getElementsByTagName("Quantity").item(0).getTextContent()),
							eElement.getElementsByTagName("Name").item(0).getTextContent(),
							uploadProfile,viewProfile,complaintProfile,
							eElement.getElementsByTagName("PopulationName").item(0).getTextContent());

					agenteDeXML.add(moderate);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return agenteDeXML;
	}

}
