package es.csic.iiia.normlab.onlinecomm.batch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
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

/**
 * 
 * @author "Javier Morales (jmorales@iiia.csic.es)"
 *
 */
public class NewExperimentButton extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = 8256626066929595536L;
  private JLabel jLabel1;
	private JTextField urlPopulation;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JTextField NumAgents;
	private JLabel jLabel5;
	private JButton Cancel;
	private JButton Create;
	private JTextField StopTick;
	private JTextField normViolationRate;
	private JLabel jLabel4;
	private JTextField tComments;
	private JButton Browse;
	private String xmlFileName;

	public NewExperimentButton(JFrame parent, boolean modal) {
		super(parent, modal);
		initGUI();
		this.setSize(569, 377);
		setLocationRelativeTo(null);// Situa la ventana en la mitad de la
									// pantalla
		setVisible(true);
	}

	private void initGUI() {
		try {

			getContentPane().setLayout(null);

			jLabel1 = new JLabel();
			getContentPane().add(jLabel1);
			jLabel1.setText("Choose Population");
			jLabel1.setBounds(74, 57, 179, 15);

			urlPopulation = new JTextField();
			getContentPane().add(urlPopulation);
			urlPopulation.setBounds(265, 54, 171, 22);

			Browse = new JButton();
			getContentPane().add(Browse);
			Browse.setText("Browse");
			Browse.setBounds(474, 54, 55, 22);
			Browse.addActionListener(this);

			jLabel2 = new JLabel();
			getContentPane().add(jLabel2);
			jLabel2.setText("Total Comments");
			jLabel2.setBounds(74, 99, 173, 15);

			tComments = new JTextField();
			getContentPane().add(tComments);
			tComments.setBounds(265, 96, 84, 22);

			jLabel3 = new JLabel();
			getContentPane().add(jLabel3);
			jLabel3.setText("Number of Agents");
			jLabel3.setBounds(74, 146, 173, 15);

			NumAgents = new JTextField();
			getContentPane().add(NumAgents);
			NumAgents.setBounds(265, 139, 84, 22);

			jLabel4 = new JLabel();
			getContentPane().add(jLabel4);
			jLabel4.setText("Norm Violation Rate");
			jLabel4.setBounds(74, 192, 173, 15);

			normViolationRate = new JTextField();
			getContentPane().add(normViolationRate);
			normViolationRate.setBounds(265, 189, 84, 22);

			jLabel5 = new JLabel();
			getContentPane().add(jLabel5);
			jLabel5.setText("Stop Tick");
			jLabel5.setBounds(74, 235, 159, 15);

			StopTick = new JTextField();
			getContentPane().add(StopTick);
			StopTick.setBounds(265, 232, 84, 22);

			Create = new JButton();
			getContentPane().add(Create);
			Create.setText("Create");
			Create.setBounds(173, 290, 74, 22);
			Create.addActionListener(this);

			Cancel = new JButton();
			getContentPane().add(Cancel);
			Cancel.setText("Cancel");
			Cancel.setBounds(349, 290, 74, 22);
			Cancel.addActionListener(this);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == Browse) {
			 final JFileChooser fc = new JFileChooser();
			 int result = fc.showDialog(this, "Open/Save");
		        if (result == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            urlPopulation.setText(file.toString());
		            xmlFileName = fc.getSelectedFile().getName();
		            xmlFileName = xmlFileName.substring(0, xmlFileName.length() - 4);

		        }	            
		}
		if (e.getSource() == Create) {
			CrearXMLDelExperimento();
			
			dispose();
		}
		if (e.getSource() == Cancel) {
			dispose();
		}
	}

	private void CrearXMLDelExperimento() {
		    try{
		        DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
		        DocumentBuilder build = dFact.newDocumentBuilder();
		        Document doc = build.newDocument();

		        Element	root = doc.createElement("Experiment");
	  	        doc.appendChild(root);

			    Element Details = doc.createElement("PopulationURL");
			    Details.appendChild(doc.createTextNode(urlPopulation.getText()));
			    root.appendChild(Details);     
			    
			    Details = doc.createElement("TotalComments");
			    Details.appendChild(doc.createTextNode(tComments.getText()));
			    root.appendChild(Details); 
			    
			    Details = doc.createElement("NumberAgents");
			    Details.appendChild(doc.createTextNode(NumAgents.getText()));
			    root.appendChild(Details); 
			    
			    Details = doc.createElement("NormViolationRate");
			    Details.appendChild(doc.createTextNode(normViolationRate.getText()));
			    root.appendChild(Details); 
			    
			    Details = doc.createElement("StopTick");
			    Details.appendChild(doc.createTextNode(StopTick.getText()));
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
		            FileWriter fos = new FileWriter("batch/onlinecomm/configurations/"+xmlFileName+".xml");
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
