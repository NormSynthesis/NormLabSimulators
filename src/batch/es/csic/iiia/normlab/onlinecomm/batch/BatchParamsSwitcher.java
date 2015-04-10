package es.csic.iiia.normlab.onlinecomm.batch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;

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

public class BatchParamsSwitcher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Simulator settings
		String batchDir = args[20];
		String totalComments = args[0];
		String numberAgents = args[1];
		String normViolationRate = args[2];
		String stopTick = args[3];
//		String conflictThreshold = args[20];
//		String ContentsNumViewsToClassify = args[21];
		
		
		//Norm Synthesis machine settings
		//(0=CUSTOM/1=IRON/2=SIMON/3=SIMON+/4=LION)
		String NormSynthesisStrategy = args[5];
		String NormGeneralisationMode = args[6];
		String NormGeneralisationStep = args[7];
		String NormsGenEffThreshold = args[8];
		String NormsGenNecThreshold = args[9];
		String NormsSpecEffThreshold = args[10];
		String NormsSpecNecThreshold = args[11];
		String NormsSpecThresholdEpsilon = args[12];
		String NormsMinEvaluationsToClassify = args[13];
		String NormsPerfRangeSize = args[14];
		String NormsDefaultUtility = args[15];
		String NumTicksToConverge = args[16];
		String NormsWithUserId = args[17];
		String NormGenerationReactive = args[18];
		String ContentsQueueSize = args[19];

		changeBatchParams(totalComments, numberAgents, normViolationRate, stopTick, NormSynthesisStrategy, 
				NormGeneralisationMode, NormGeneralisationStep, NormsGenEffThreshold, NormsGenNecThreshold, 
				NormsSpecEffThreshold, NormsSpecNecThreshold, NormsSpecThresholdEpsilon, NormsMinEvaluationsToClassify, 
				NormsPerfRangeSize, NormsDefaultUtility, NumTicksToConverge, NormsWithUserId, NormGenerationReactive, 
				batchDir, ContentsQueueSize);//, conflictThreshold, ContentsNumViewsToClassify);
	}


	/**
	 * 
	 * @param totalComments
	 * @param numberAgents
	 * @param normViolationRate
	 * @param stopTick
	 */
	private static void changeBatchParams(String totalComments,
	    String numberAgents, String normViolationRate, String stopTick,
	    String NormSynthesisStrategy, String NormGeneralisationMode,
	    String NormGeneralisationStep, String NormsGenEffThreshold,
	    String NormsGenNecThreshold, String NormsSpecEffThreshold,
	    String NormsSpecNecThreshold, String NormsSpecThresholdEpsilon,
	    String NormsMinEvaluationsToClassify, String NormsPerfRangeSize, 
	    String NormsDefaultUtility, String NumTicksToConverge,
	    String NormsWithUserId, String NormGenerationReactive, String batchDir,
	    String ContentsQueueSize){//, String conflictThreshold, String ContentsNumViewsToClassify) {
		
		try {
			File file = new File(batchDir);
			if (!file.exists()) {
				file.createNewFile();
			}

			// Create instance of DocumentBuilderFactory
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			// Get the DocumentBuilder
			DocumentBuilder docBuilder = factory.newDocumentBuilder();

			// Using existing XML Document
			Document doc = docBuilder.parse(file);

			NodeList nList = doc.getElementsByTagName("parameter");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					
					/* Simulator Settings */
					
					if (eElement.getAttribute("name").equals("Total Comments")) {
						eElement.setAttribute("value", "" + totalComments);
					}
					if (eElement.getAttribute("name").equals("maxAgents")) {
						eElement.setAttribute("value", "" + numberAgents);
					}
					if (eElement.getAttribute("name").equals("Norm Violation Rate")) {
						eElement.setAttribute("value", "" + normViolationRate);
					}
					if (eElement.getAttribute("name").equals("StopTick")) {
						eElement.setAttribute("value", "" + stopTick);
					}
					if (eElement.getAttribute("name").equals("ContentsQueueSize")) {
						eElement.setAttribute("value", "" + ContentsQueueSize);
					}
					/*if (eElement.getAttribute("name").equals("conflictThreshold")) {
						eElement.setAttribute("value", "" + conflictThreshold);
					}
					if (eElement.getAttribute("name").equals("ContentsNumViewsToClassify")) {
						eElement.setAttribute("value", "" + ContentsNumViewsToClassify);
					}*/
					
					
					/* Norm Synthesis Settings */
					
					if (eElement.getAttribute("name").equals("NormSynthesisStrategy")) {
						eElement.setAttribute("value", "" + NormSynthesisStrategy);
					}
					if (eElement.getAttribute("name").equals("NormGeneralisationMode")) {
						eElement.setAttribute("value", "" + NormGeneralisationMode);
					}
					if (eElement.getAttribute("name").equals("NormGeneralisationStep")) {
						eElement.setAttribute("value", "" + NormGeneralisationStep);
					}
					if (eElement.getAttribute("name").equals("NormsGenEffThreshold")) {
						eElement.setAttribute("value", "" + NormsGenEffThreshold);
					}
					if (eElement.getAttribute("name").equals("NormsGenNecThreshold")) {
						eElement.setAttribute("value", "" + NormsGenNecThreshold);
					}
					if (eElement.getAttribute("name").equals("NormsSpecEffThreshold")) {
						eElement.setAttribute("value", "" + NormsSpecEffThreshold);
					}
					if (eElement.getAttribute("name").equals("NormsSpecNecThreshold")) {
						eElement.setAttribute("value", "" + NormsSpecNecThreshold);
					}
					if (eElement.getAttribute("name").equals("NormsSpecThresholdEpsilon")) {
						eElement.setAttribute("value", "" + NormsSpecThresholdEpsilon);
					}
					if (eElement.getAttribute("name").equals("NormsMinEvaluationsToClassify")) {
						eElement.setAttribute("value", "" + NormsMinEvaluationsToClassify);
					}
					if (eElement.getAttribute("name").equals("NormsPerfRangeSize")) {
						eElement.setAttribute("value", "" + NormsPerfRangeSize);
					}
					if (eElement.getAttribute("name").equals("NormsDefaultUtility")) {
						eElement.setAttribute("value", "" + NormsDefaultUtility);
					}
					if (eElement.getAttribute("name").equals("NumTicksToConverge")) {
						eElement.setAttribute("value", "" + NumTicksToConverge);
					}
					if (eElement.getAttribute("name").equals("NormsWithUserId")) {
						eElement.setAttribute("value", "" + NormsWithUserId);
					}
					if (eElement.getAttribute("name").equals("NormGenerationReactive")) {
						eElement.setAttribute("value", "" + NormGenerationReactive);
					}					
				}

			}
			// set up a transformer
			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();

			// create string from xml tree
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			DOMSource source = new DOMSource(doc);
			trans.transform(source, result);
			String xmlString = sw.toString();

			OutputStream f0;

			byte buf[] = xmlString.getBytes();
			f0 = new FileOutputStream(batchDir);
			for (int i = 0; i < buf.length; i++) {
				f0.write(buf[i]);
			}
			f0.close();
			buf = null;
			System.out.println("BATCH PARAMETERS COPIED...");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

	}

}
