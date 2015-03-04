package es.csic.iiia.normlab.onlinecomm.batch;

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

import es.csic.iiia.normlab.onlinecomm.agents.CommunityAgent;

/**
 * Class to manipulate or create XML files.
 * 
 * @author Iosu Mendizabal
 *
 */
public class ExperimentConfigurationCreatorXMLParser {

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
	public ExperimentConfigurationCreatorXMLParser(String xmlFileName, ArrayList<CommunityAgent> agente) {
		this.xmlFileName = xmlFileName;
		this.agente = agente;
		writeXmlFile(agente);
	}

	/**
	 * Second and empty constructor of the XMLParser.
	 */
	public ExperimentConfigurationCreatorXMLParser() {
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

			Element	root = doc.createElement("agents");
			doc.appendChild(root);

			for(int i = 0 ; i < agente.size() ; i++ ) { 
				if(agente.get(i).getQuantity() > 0){
					Element Details = doc.createElement("agent");
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
				File file = new File("batch/onlinecomm/populations/" + xmlFileName + ".xml");
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
}
