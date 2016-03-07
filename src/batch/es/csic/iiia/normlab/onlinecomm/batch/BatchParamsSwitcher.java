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
		String totalComments = args[0];
		String numberAgents = args[1];
		String normViolationRate = args[2];
		String stopTick = args[3];
//		String poblacion = args[4];

		changeBatchParams(totalComments, numberAgents, normViolationRate, stopTick);
//		changeOutputDataFileName(poblacion);
	}

//	/**
//	 * 
//	 * @param poblacion
//	 */
//	private static void changeOutputDataFileName(String poblacion) {
//		try {
//			File file = new File("repast-settings/OnlineCommunity.rs/repast.simphony.action.file_sink_0.xml");
//
//			//Create instance of DocumentBuilderFactory
//			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//
//			//Get the DocumentBuilder
//			DocumentBuilder docBuilder = factory.newDocumentBuilder();
//
//			//Using existing XML Document
//			Document doc = docBuilder.parse(file);
//			NodeList nList = doc.getElementsByTagName("fileName");
//
////			Random r = new Random(System.currentTimeMillis());
//			nList.item(0).setTextContent("output/onlinecomm/ExperimentOutputData");//+poblacion+"-"+r.nextLong());
//
//			//set up a transformer
//			TransformerFactory transfac = TransformerFactory.newInstance();
//			Transformer trans = transfac.newTransformer();
//
//			//create string from xml tree
//			StringWriter sw = new StringWriter();
//			StreamResult result = new StreamResult(sw);
//			DOMSource source = new DOMSource(doc);
//			trans.transform(source, result);
//			String xmlString = sw.toString();
//
//			OutputStream f0;
//
//			byte buf[] = xmlString.getBytes();
//			f0 = new FileOutputStream("repast-settings/OnlineCommunity.rs/repast.simphony.action.file_sink_0.xml");
//			for(int i=0;i<buf .length;i++) {
//				f0.write(buf[i]);
//			}
//			f0.close();
//			buf = null;
//			System.out.println("DATA SINK PARAMETERS COPIED...");
//		}
//		catch(IOException e) {
//			e.printStackTrace();
//		}
//		catch(ParserConfigurationException e) {
//			e.printStackTrace();
//		} 
//		catch(TransformerException e) {
//			e.printStackTrace();
//		} catch (SAXException e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * 
	 * @param totalComments
	 * @param numberAgents
	 * @param normViolationRate
	 * @param stopTick
	 */
	private static void changeBatchParams(String totalComments, String numberAgents, String normViolationRate, String stopTick) {
		try {
			File file = new File("batch/onlinecomm/batch_params.xml");
			if(!file.exists()) {
				file.createNewFile();
			}

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
			f0 = new FileOutputStream("batch/onlinecomm/batch_params.xml");
			for(int i=0;i<buf .length;i++) {
				f0.write(buf[i]);
			}
			f0.close();
			buf = null;
			System.out.println("BATCH PARAMETERS COPIED...");
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

}
