package es.csic.iiia.normlab.onlinecomm.context;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.UIManager;

import es.csic.iiia.normlab.onlinecomm.XMLParser.XMLParser;
import es.csic.iiia.normlab.onlinecomm.agents.CommunityAgent;
import es.csic.iiia.normlab.onlinecomm.agents.profile.ComplaintProfile;
import es.csic.iiia.normlab.onlinecomm.agents.profile.UploadProfile;
import es.csic.iiia.normlab.onlinecomm.agents.profile.ViewProfile;
import es.csic.iiia.normlab.onlinecomm.bbdd.BbddManager;

/**
 * Window to configure agents
 * 
 * @author Iosu Mendizabal Borda
 * 
 */
public class ConfigureAgentWindow extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	/**
	 * Graphical components.
	 */
	// Combo Boxes
	private JComboBox AgentCombo;
	private JComboBox PopulationCombo;

	// Labels
	private JLabel ForumViewLabel;
	private JLabel ModerateImage;
	private JLabel AgentNameLabel;
	private JLabel AgentTypeLabel;
	private JLabel PornComplaintLabel;
	private JLabel ViolentComplaintLabel;
	private JLabel SpamComplaintLabel;
	private JLabel InsultComplaintLabel;
	private JLabel MultimediaViewLabel;
	private JLabel ReporterViewLabel;
	private JLabel PornContentLabel;
	private JLabel PopulationLabel;
	private JLabel NumberOfAgentsLabel;
	private JLabel ViolentLabel;
	private JLabel SpammerLabel;
	private JLabel RudeLabel;
	private JLabel PornographicLabel;
	private JLabel ModerateLabel;
	private JLabel CorrectContentLabel;
	private JLabel ViolentContentLabel;
	private JLabel SpamContentLabel;
	private JLabel InsultContentLabel;
	private JLabel UploadContentLabel;
	private JLabel UploadProfile;
	private JLabel populationLabel;
	private JLabel ComplaintProfile;
	private JLabel ViewProfile;
	private JLabel SumaLabel;
	private JLabel ViewModeLabel;

	// Text Fields
	private JTextField ViolentComplaintPer;
	private JTextField ViolentAgentNumber;
	private JTextField newPopulationName;
	private JTextField PornComplaintPer;
	private JTextField SpammerAgentNumber;
	private JTextField RudeAgentNumber;
	private JTextField PornoAgentNumber;
	private JTextField ModerateAgentNumber;
	private JTextField NumberOfAgents;
	private JTextField SpamComplaintPer;
	private JTextField InsultComplaintPer;
	private JTextField MultimediaPer;
	private JTextField ReporterPer;
	private JTextField ForumPer;
	private JTextField PornPer;
	private JTextField ViolentPer;
	private JTextField SpammPer;
	private JTextField InsultPer;
	private JTextField CorrectPer;
	private JTextField UploadPercentage;

	// Buttons
	private JButton ChangeButton;
	private JButton SaveButton;
	private JButton ExitButton;

	// Separators
	private JSeparator Separator;

	// Radio Buttons
	private JRadioButton ByRandomRadio;
	private JRadioButton ByMostViewRadio;
	private JRadioButton ByOrderRadio;

	/**
	 * Database Connection
	 */
	protected BbddManager bbddManager;

	/**
	 * Access to other classes.
	 */
	private UploadProfile uploadProfile;
	private ViewProfile viewProfile;
	private ComplaintProfile complaintProfile;
	private Random randomUpload;

	/**
	 * ArrayList of Agent type.
	 */
	private ArrayList<CommunityAgent> agents = new ArrayList<CommunityAgent>();

	/**
	 * Variables.
	 */
	// Integers.
	final static int orderRadio = 0;
	final static int mostViewRadio = 1;
	final static int randomRadio = 2;

	// Strings
	private String texto;

	/**
	 * Constructor that is called from the Simulator main to create the configure
	 * window.
	 * 
	 * @param parent
	 *          JFrame of the parent that is going to create this dialog.
	 * @param modal
	 *          Boolean that says to the dialog if the dialog created is modal or
	 *          not. True: Is modal so you can't accede to the contain that is
	 *          below before you close the dialog. False: It isn't modal so you
	 *          can accede to the contain is below the dialog.
	 */

	public ConfigureAgentWindow(JFrame parent, boolean modal) {
		super(parent, modal);

		bbddManager = new BbddManager();
		initGUI();
		cargarDatosComboBox();

		setSize(926, 726);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * 
	 * Feed the dialog with the graphical components.
	 * 
	 */
	public void initGUI() {
		// Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(UIManager
			    .getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			getContentPane().setLayout(null);

			ComboBoxModel PopulationComboModel = new DefaultComboBoxModel();
			PopulationCombo = new JComboBox();
			getContentPane().add(PopulationCombo);
			PopulationCombo.setModel(PopulationComboModel);
			PopulationCombo.setBounds(366, 22, 190, 22);
			PopulationCombo.addActionListener(this);

			ComboBoxModel AgentComboModel = new DefaultComboBoxModel();
			AgentCombo = new JComboBox();
			getContentPane().add(AgentCombo);
			AgentCombo.setModel(AgentComboModel);
			AgentCombo.setBounds(366, 56, 190, 22);
			AgentCombo.addActionListener(this);

			UploadProfile = new JLabel();
			getContentPane().add(UploadProfile);
			UploadProfile.setText("Upload Profile");
			UploadProfile.setBounds(110, 134, 144, 15);
			UploadProfile.setFont(new java.awt.Font("Dialog", 1, 16));

			ViewProfile = new JLabel();
			getContentPane().add(ViewProfile);
			ViewProfile.setText("View Profile");
			ViewProfile.setBounds(381, 134, 122, 15);
			ViewProfile.setFont(new java.awt.Font("Dialog", 1, 16));

			ComplaintProfile = new JLabel();
			getContentPane().add(ComplaintProfile);
			ComplaintProfile.setText("Complaint Profile");
			ComplaintProfile.setBounds(643, 134, 155, 15);
			ComplaintProfile.setFont(new java.awt.Font("Dialog", 1, 16));

			UploadPercentage = new JTextField();
			getContentPane().add(UploadPercentage);
			UploadPercentage.setBounds(223, 167, 45, 19);

			CorrectPer = new JTextField();
			getContentPane().add(CorrectPer);
			CorrectPer.setBounds(223, 205, 45, 19);

			SpammPer = new JTextField();
			getContentPane().add(SpammPer);
			SpammPer.setBounds(223, 236, 45, 19);

			PornPer = new JTextField();
			getContentPane().add(PornPer);
			PornPer.setBounds(223, 267, 45, 19);

			ViolentPer = new JTextField();
			getContentPane().add(ViolentPer);
			ViolentPer.setBounds(223, 298, 45, 19);

			InsultPer = new JTextField();
			getContentPane().add(InsultPer);
			InsultPer.setBounds(223, 325, 45, 19);

			ForumPer = new JTextField();
			getContentPane().add(ForumPer);
			ForumPer.setBounds(512, 168, 45, 19);

			ReporterPer = new JTextField();
			getContentPane().add(ReporterPer);
			ReporterPer.setBounds(512, 201, 45, 19);

			MultimediaPer = new JTextField();
			getContentPane().add(MultimediaPer);
			MultimediaPer.setBounds(512, 232, 45, 19);

			SpamComplaintPer = new JTextField();
			getContentPane().add(SpamComplaintPer);
			SpamComplaintPer.setBounds(804, 170, 45, 19);

			PornComplaintPer = new JTextField();
			getContentPane().add(PornComplaintPer);
			PornComplaintPer.setBounds(804, 201, 45, 19);

			ViolentComplaintPer = new JTextField();
			getContentPane().add(ViolentComplaintPer);
			ViolentComplaintPer.setBounds(804, 232, 45, 19);

			InsultComplaintPer = new JTextField();
			getContentPane().add(InsultComplaintPer);
			InsultComplaintPer.setBounds(804, 263, 45, 19);

			UploadContentLabel = new JLabel();
			getContentPane().add(UploadContentLabel);
			UploadContentLabel.setText("Upload Frequency");
			UploadContentLabel.setBounds(79, 170, 112, 15);

			CorrectContentLabel = new JLabel();
			getContentPane().add(CorrectContentLabel);
			CorrectContentLabel.setText("Correct Content");
			CorrectContentLabel.setBounds(82, 207, 113, 15);

			SpamContentLabel = new JLabel();
			getContentPane().add(SpamContentLabel);
			SpamContentLabel.setText("Spam Content");
			SpamContentLabel.setBounds(82, 238, 122, 15);

			PornContentLabel = new JLabel();
			getContentPane().add(PornContentLabel);
			PornContentLabel.setText("Porn Content");
			PornContentLabel.setBounds(82, 269, 101, 15);

			ViolentContentLabel = new JLabel();
			getContentPane().add(ViolentContentLabel);
			ViolentContentLabel.setText("Violent Content");
			ViolentContentLabel.setBounds(82, 300, 103, 15);

			InsultContentLabel = new JLabel();
			getContentPane().add(InsultContentLabel);
			InsultContentLabel.setText("Insult Content");
			InsultContentLabel.setBounds(82, 331, 110, 15);

			ForumViewLabel = new JLabel();
			getContentPane().add(ForumViewLabel);
			ForumViewLabel.setText("Forum View");
			ForumViewLabel.setBounds(372, 170, 108, 15);

			ReporterViewLabel = new JLabel();
			getContentPane().add(ReporterViewLabel);
			ReporterViewLabel.setText("The Reporter View");
			ReporterViewLabel.setBounds(372, 203, 123, 15);

			MultimediaViewLabel = new JLabel();
			getContentPane().add(MultimediaViewLabel);
			MultimediaViewLabel.setText("Multimedia View");
			MultimediaViewLabel.setBounds(372, 234, 123, 15);

			SpamComplaintLabel = new JLabel();
			getContentPane().add(SpamComplaintLabel);
			SpamComplaintLabel.setText("Spam Complaint");
			SpamComplaintLabel.setBounds(615, 174, 110, 15);

			PornComplaintLabel = new JLabel();
			getContentPane().add(PornComplaintLabel);
			PornComplaintLabel.setText("Porn Complaint");
			PornComplaintLabel.setBounds(615, 203, 104, 15);

			ViolentComplaintLabel = new JLabel();
			getContentPane().add(ViolentComplaintLabel);
			ViolentComplaintLabel.setText("Violent Complaint");
			ViolentComplaintLabel.setBounds(615, 234, 117, 15);

			InsultComplaintLabel = new JLabel();
			getContentPane().add(InsultComplaintLabel);
			InsultComplaintLabel.setText("Insult Complaint");
			InsultComplaintLabel.setBounds(615, 265, 108, 15);

			PopulationLabel = new JLabel();
			getContentPane().add(PopulationLabel);
			PopulationLabel.setText("Current population");
			PopulationLabel.setBounds(90, 455, 753, 32);
			PopulationLabel.setFont(new java.awt.Font("Dialog", 1, 18));
			PopulationLabel.setBorder(BorderFactory.createTitledBorder(""));

			ModerateLabel = new JLabel();
			getContentPane().add(ModerateLabel);
			ModerateLabel.setText("Moderate");
			ModerateLabel.setBounds(187, 525, 110, 15);

			PornographicLabel = new JLabel();
			getContentPane().add(PornographicLabel);
			PornographicLabel.setText("Pornographic");
			PornographicLabel.setBounds(187, 554, 110, 15);

			RudeLabel = new JLabel();
			getContentPane().add(RudeLabel);
			RudeLabel.setText("Rude");
			RudeLabel.setBounds(187, 582, 94, 15);

			SpammerLabel = new JLabel();
			getContentPane().add(SpammerLabel);
			SpammerLabel.setText("Spammer");
			SpammerLabel.setBounds(549, 522, 99, 15);

			ViolentLabel = new JLabel();
			getContentPane().add(ViolentLabel);
			ViolentLabel.setText("Violent");
			ViolentLabel.setBounds(549, 552, 120, 15);

			NumberOfAgents = new JTextField();
			getContentPane().add(NumberOfAgents);
			NumberOfAgents.setBounds(511, 408, 45, 19);

			NumberOfAgentsLabel = new JLabel();
			getContentPane().add(NumberOfAgentsLabel);
			NumberOfAgentsLabel.setText("Number of Agents");
			NumberOfAgentsLabel.setBounds(333, 411, 138, 15);

			ChangeButton = new JButton();
			getContentPane().add(ChangeButton);
			ChangeButton.setText("Modify");
			ChangeButton.setBounds(602, 407, 119, 25);
			ChangeButton.addActionListener(this);
			ChangeButton.setActionCommand("modificar");

			ModerateAgentNumber = new JTextField();
			getContentPane().add(ModerateAgentNumber);
			ModerateAgentNumber.setBounds(359, 523, 39, 17);
			ModerateAgentNumber.setEditable(false);

			PornoAgentNumber = new JTextField();
			getContentPane().add(PornoAgentNumber);
			PornoAgentNumber.setBounds(359, 552, 39, 17);
			PornoAgentNumber.setEditable(false);

			RudeAgentNumber = new JTextField();
			getContentPane().add(RudeAgentNumber);
			RudeAgentNumber.setBounds(359, 580, 39, 17);
			RudeAgentNumber.setEditable(false);

			SpammerAgentNumber = new JTextField();
			getContentPane().add(SpammerAgentNumber);
			SpammerAgentNumber.setBounds(721, 522, 39, 17);
			SpammerAgentNumber.setEditable(false);

			ViolentAgentNumber = new JTextField();
			getContentPane().add(ViolentAgentNumber);
			ViolentAgentNumber.setBounds(721, 552, 39, 17);
			ViolentAgentNumber.setEditable(false);

			ExitButton = new JButton();
			getContentPane().add(ExitButton);
			ExitButton.setText("Exit and Start");
			ExitButton.setBounds(398, 639, 127, 29);
			ExitButton.addActionListener(this);
			ExitButton.setActionCommand("ExitButton");

			newPopulationName = new JTextField();
			getContentPane().add(newPopulationName);
			newPopulationName.setBounds(725, 18, 155, 28);

			AgentTypeLabel = new JLabel();
			getContentPane().add(AgentTypeLabel);
			AgentTypeLabel.setText("Agent Type");
			AgentTypeLabel.setBounds(221, 58, 106, 16);

			AgentNameLabel = new JLabel();
			getContentPane().add(AgentNameLabel);
			AgentNameLabel.setText("New Population Name");
			AgentNameLabel.setBounds(573, 24, 140, 16);

			ImageIcon icon = new ImageIcon(
			    "misc/onlinecomm/icons/agents/moderate_agent.png");
			ModerateImage = new JLabel();
			getContentPane().add(ModerateImage);
			ModerateImage.setIcon(icon);
			ModerateImage.setBounds(122, 519, 39, 17);

			icon = new ImageIcon(
			    "misc/onlinecomm/icons/agents/pornographic_agent.png");
			JLabel PornoImage = new JLabel();
			getContentPane().add(PornoImage);
			PornoImage.setIcon(icon);
			PornoImage.setBounds(123, 548, 39, 17);

			icon = new ImageIcon("misc/onlinecomm/icons/agents/rude_agent.png");
			JLabel rudeImage = new JLabel();
			getContentPane().add(rudeImage);
			rudeImage.setIcon(icon);
			rudeImage.setBounds(123, 578, 39, 17);

			icon = new ImageIcon("misc/onlinecomm/icons/agents/spammer_agent.png");
			JLabel SpammerImage = new JLabel();
			getContentPane().add(SpammerImage);
			SpammerImage.setIcon(icon);
			SpammerImage.setBounds(486, 519, 39, 17);

			icon = new ImageIcon("misc/onlinecomm/icons/agents/troll_agent.png");
			JLabel TrollImage = new JLabel();
			getContentPane().add(TrollImage);
			TrollImage.setIcon(icon);
			TrollImage.setBounds(486, 548, 39, 17);

			icon = new ImageIcon("misc/onlinecomm/icons/agents/violent_agent.png");
			JLabel ViolentImage = new JLabel();
			getContentPane().add(ViolentImage);
			ViolentImage.setIcon(icon);
			ViolentImage.setBounds(486, 578, 39, 17);

			populationLabel = new JLabel();
			getContentPane().add(populationLabel);
			populationLabel.setText("Population");
			populationLabel.setBounds(221, 23, 106, 16);

			SaveButton = new JButton();
			getContentPane().add(SaveButton);
			SaveButton.setText("Save Population");
			SaveButton.setBounds(731, 53, 139, 32);
			SaveButton.addActionListener(this);
			SaveButton.setActionCommand("SaveButton");

			SumaLabel = new JLabel();
			getContentPane().add(SumaLabel);
			SumaLabel.setBounds(215, 367, 10, 10);

			ViewModeLabel = new JLabel();
			getContentPane().add(ViewModeLabel);
			ViewModeLabel.setText("View Mode");
			ViewModeLabel.setBounds(377, 275, 99, 16);
			ViewModeLabel.setFont(new java.awt.Font("Lucida Grande", 1, 16));

			ByOrderRadio = new JRadioButton();
			getContentPane().add(ByOrderRadio);
			ByOrderRadio.setText("By Order");
			ByOrderRadio.setBounds(383, 306, 85, 23);
			ByOrderRadio.addActionListener(this);

			ByMostViewRadio = new JRadioButton();
			getContentPane().add(ByMostViewRadio);
			ByMostViewRadio.setText("By Most View");
			ByMostViewRadio.setBounds(383, 330, 114, 23);
			ByMostViewRadio.addActionListener(this);

			ByRandomRadio = new JRadioButton();
			getContentPane().add(ByRandomRadio);
			ByRandomRadio.setText("By Random");
			ByRandomRadio.setBounds(383, 356, 101, 23);
			ByRandomRadio.addActionListener(this);

			ButtonGroup group = new ButtonGroup();

			group.add(ByOrderRadio);
			group.add(ByMostViewRadio);
			group.add(ByRandomRadio);

			Separator = new JSeparator();
			getContentPane().add(Separator);
			Separator.setBounds(57, 189, 261, 12);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Feed the combo boxes with the data.
	 * 
	 */
	private void cargarDatosComboBox() {
		bbddManager.connect();

		if (bbddManager.getConn() != null) {
			ArrayList<String> popuName = bbddManager.getPopulations();

			for (int i = 0; i < popuName.size(); i++) {
				PopulationCombo.addItem(popuName.get(i));
			}
		}

		bbddManager.connect();

		if (bbddManager.getConn() != null) {
			ArrayList<String> agentTypeName = bbddManager.getAgentTypes();

			for (int i = 0; i < agentTypeName.size(); i++) {
				AgentCombo.addItem(agentTypeName.get(i));
			}
			bbddManager.disconnect("");
		}
	}

	/**
	 * 
	 * Action performed to control when the user do actions in the graphical
	 * components and interact with it.
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == PopulationCombo) {
			cargarArrayAgenteSegunPoblacion();
			visualizarInformacionAgente();
		}

		if (e.getSource() == AgentCombo) {
			visualizarInformacionAgente();
		}

		if (e.getActionCommand().equals("modificar")) {
			boolean comprobacion = comprobarValores();
			if (comprobacion) {
				guardarCambiosEnArrayAgente();
			}
		}

		if (e.getActionCommand().equals("SaveButton")) {
			guardarCambiosEnBD();
			crearFicheroXML();
		}

		if (e.getActionCommand().equals("ExitButton")) {
			this.dispose();
		}
	}

	/**
	 * 
	 * Method to create a XML file with the configuration selected from the
	 * configuration window.
	 * 
	 */
	private void crearFicheroXML() {
		new XMLParser(newPopulationName.getText(), agents);
	}

	/**
	 * 
	 * Method to feed ArrayList of agent with the population selected.
	 * 
	 */
	private void cargarArrayAgenteSegunPoblacion() {
		agents.removeAll(agents);
		bbddManager.connect();
		if (bbddManager.getConn() != null) {
			agents = bbddManager.getAgentsProfile(uploadProfile, viewProfile,
			    complaintProfile, randomUpload,
			    "" + PopulationCombo.getSelectedItem());
			bbddManager.disconnect("");
		}
	}

	/**
	 * 
	 * Method to save the changes done in the configuration window in the data
	 * base.
	 * 
	 */
	private void guardarCambiosEnBD() {
		bbddManager.connect();
		if (bbddManager.getConn() != null) {
			bbddManager.setNewAgentPopulation(agents, newPopulationName.getText());
			bbddManager.disconnect("Configure Agent Window");
		}
		texto = "The changes have been saved in the database.";
		informationMessage(texto);
	}

	/**
	 * 
	 * Method to save the changes done in the configuration window in the
	 * arrayList of the agent.
	 * 
	 */
	private void guardarCambiosEnArrayAgente() {
		for (int i = 0; i < agents.size(); i++) {
			if (AgentCombo.getSelectedItem().equals(agents.get(i).getName())
			    && PopulationCombo.getSelectedItem().equals(
			        agents.get(i).getPopulation())) {

				agents
				    .get(i)
				    .getUploadProfile()
				    .setUploadProbability(
				        Double.parseDouble(UploadPercentage.getText()));
				agents.get(i).getUploadProfile()
				    .setCorrect(Double.parseDouble(CorrectPer.getText()));
				agents.get(i).getUploadProfile()
				    .setInsult(Double.parseDouble(this.InsultPer.getText()));
				agents.get(i).getUploadProfile()
				    .setSpam(Double.parseDouble(this.SpammPer.getText()));
				agents.get(i).getUploadProfile()
				    .setViolent(Double.parseDouble(this.ViolentPer.getText()));
				agents.get(i).getUploadProfile()
				    .setPorn(Double.parseDouble(this.PornPer.getText()));

				agents.get(i).getViewProfile()
				    .setForum(Double.parseDouble(this.ForumPer.getText()));
				agents.get(i).getViewProfile()
				    .setTheReporter(Double.parseDouble(this.ReporterPer.getText()));
				agents.get(i).getViewProfile()
				    .setPhotoVideo(Double.parseDouble(this.MultimediaPer.getText()));

				agents.get(i).getComplaintProfile()
				    .setInsult(Double.parseDouble(this.InsultComplaintPer.getText()));
				agents.get(i).getComplaintProfile()
				    .setSpam(Double.parseDouble(this.SpamComplaintPer.getText()));
				agents.get(i).getComplaintProfile()
				    .setViolent(Double.parseDouble(this.ViolentComplaintPer.getText()));
				agents.get(i).getComplaintProfile()
				    .setPorn(Double.parseDouble(this.PornComplaintPer.getText()));

				agents.get(i).setQuantity(
				    Integer.parseInt(this.NumberOfAgents.getText()));

				if (agents.get(i).getName().equals("Moderate")) {
					this.ModerateAgentNumber.setText("" + agents.get(i).getQuantity());
				}
				if (agents.get(i).getName().equals("Pornographic")) {
					this.PornoAgentNumber.setText("" + agents.get(i).getQuantity());
				}
				if (agents.get(i).getName().equals("Rude")) {
					this.RudeAgentNumber.setText("" + agents.get(i).getQuantity());
				}
				if (agents.get(i).getName().equals("Spammer")) {
					this.SpammerAgentNumber.setText("" + agents.get(i).getQuantity());
				}
				if (agents.get(i).getName().equals("Violent")) {
					this.ViolentAgentNumber.setText("" + agents.get(i).getQuantity());
				}

				if (ByMostViewRadio.isSelected()) {
					agents.get(i).getViewProfile().setViewMode(mostViewRadio);
				}
				if (ByOrderRadio.isSelected()) {
					agents.get(i).getViewProfile().setViewMode(orderRadio);
				}
				if (ByRandomRadio.isSelected()) {
					agents.get(i).getViewProfile().setViewMode(randomRadio);
				}
			}
		}
		texto = "The changes have been saved.";
		informationMessage(texto);
	}

	/**
	 * 
	 * Method to check the values of the configuration window.
	 * 
	 * @return True: There aren't errors. False: There are errors to show.
	 */
	private boolean comprobarValores() {
		ArrayList<String> errores = new ArrayList<String>();

		if (Double.parseDouble(UploadPercentage.getText()) > 1
		    || Double.parseDouble(UploadPercentage.getText()) < 0) {
			errores.add("Upload Frequency debe ser de 0 a 1.\n");
		}
		double sumaUP = Double.parseDouble(CorrectPer.getText())
		    + Double.parseDouble(InsultPer.getText())
		    + Double.parseDouble(SpammPer.getText())
		    + Double.parseDouble(ViolentPer.getText())
		    + Double.parseDouble(PornPer.getText());

		if (sumaUP > 1 || sumaUP < 0) {
			errores
			    .add("La suma de los valores de Upload Profile tiene que dar 1.\n");
		}
		double sumaView = Double.parseDouble(ForumPer.getText())
		    + Double.parseDouble(ReporterPer.getText())
		    + Double.parseDouble(MultimediaPer.getText());

		if (sumaView > 1 || sumaView < 0) {
			errores.add("La suma de view profile tiene que dar 1.\n");
		}
		if (Double.parseDouble(InsultComplaintPer.getText()) > 1
		    || Double.parseDouble(InsultComplaintPer.getText()) < 0) {
			errores.add("Insult complaint percentage debe ser de 0 a 1.\n");
		}
		if (Double.parseDouble(SpamComplaintPer.getText()) > 1
		    || Double.parseDouble(SpamComplaintPer.getText()) < 0) {
			errores.add("Spam complaint percentage debe ser de 0 a 1.\n");
		}
		if (Double.parseDouble(ViolentComplaintPer.getText()) > 1
		    || Double.parseDouble(ViolentComplaintPer.getText()) < 0) {
			errores.add("Violent complaint percentage debe ser de 0 a 1.\n");
		}
		if (Double.parseDouble(PornComplaintPer.getText()) > 1
		    || Double.parseDouble(PornComplaintPer.getText()) < 0) {
			errores.add("Porn complaint percentage debe ser de 0 a 1.\n");
		}
		if (errores.size() > 0) {
			String errorMostrar = "";

			for (int i = 0; i < errores.size(); i++) {
				errorMostrar = errorMostrar + errores.get(i);
			}
			errorMessage(errorMostrar);

			return false;
		} else {
			return true;
		}
	}

	/**
	 * 
	 * Method to feed the graphical components of the configuration windows with
	 * the values of the arrayList of the agent.
	 * 
	 */
	private void visualizarInformacionAgente() {
		try {
			for (int i = 0; i < agents.size(); i++) {
				if (AgentCombo.getSelectedItem().equals(agents.get(i).getName())
				    && PopulationCombo.getSelectedItem().equals(
				        agents.get(i).getPopulation())) {

					UploadPercentage.setText(""
					    + agents.get(i).getUploadProfile().getUploadProbability());
					CorrectPer
					    .setText("" + agents.get(i).getUploadProfile().getCorrect());
					InsultPer.setText("" + agents.get(i).getUploadProfile().getInsult());
					SpammPer.setText("" + agents.get(i).getUploadProfile().getSpam());
					ViolentPer
					    .setText("" + agents.get(i).getUploadProfile().getViolent());
					PornPer.setText("" + agents.get(i).getUploadProfile().getPorn());

					ForumPer.setText("" + agents.get(i).getViewProfile().getForum());
					ReporterPer.setText(""
					    + agents.get(i).getViewProfile().getTheReporter());
					MultimediaPer.setText(""
					    + agents.get(i).getViewProfile().getPhotoVideo());

					InsultComplaintPer.setText(""
					    + agents.get(i).getComplaintProfile().getInsult());
					SpamComplaintPer.setText(""
					    + agents.get(i).getComplaintProfile().getSpam());
					ViolentComplaintPer.setText(""
					    + agents.get(i).getComplaintProfile().getViolent());
					PornComplaintPer.setText(""
					    + agents.get(i).getComplaintProfile().getPorn());

					NumberOfAgents.setText("" + agents.get(i).getQuantity());

					switch (agents.get(i).getViewProfile().getViewMode()) {
					case orderRadio:
						ByOrderRadio.setSelected(true);
						ByMostViewRadio.setSelected(false);
						ByRandomRadio.setSelected(false);
						break;
					case mostViewRadio:
						ByMostViewRadio.setSelected(true);
						ByOrderRadio.setSelected(false);
						ByRandomRadio.setSelected(false);
						break;
					case randomRadio:
						ByRandomRadio.setSelected(true);
						ByOrderRadio.setSelected(false);
						ByMostViewRadio.setSelected(false);
						break;
					}
				}

				if (agents.get(i).getName().equals("moderate")) {
					this.ModerateAgentNumber.setText("" + agents.get(i).getQuantity());
				}
				if (agents.get(i).getName().equals("pornographic")) {
					this.PornoAgentNumber.setText("" + agents.get(i).getQuantity());
				}
				if (agents.get(i).getName().equals("rude")) {
					this.RudeAgentNumber.setText("" + agents.get(i).getQuantity());
				}
				if (agents.get(i).getName().equals("spammer")) {
					this.SpammerAgentNumber.setText("" + agents.get(i).getQuantity());
				}
				if (agents.get(i).getName().equals("violent")) {
					this.ViolentAgentNumber.setText("" + agents.get(i).getQuantity());
				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 
	 * Method to show an OptionPane of an error message.
	 * 
	 * @param errorMostrar
	 *          String of the error message to show.
	 */
	private void errorMessage(String errorMostrar) {
		JOptionPane optionPane = new JOptionPane();
		optionPane.setMessage(errorMostrar);
		optionPane.setMessageType(JOptionPane.ERROR_MESSAGE);
		JDialog dialog = optionPane.createDialog(null, "Information Dialog");
		dialog.setVisible(true);
	}

	/**
	 * 
	 * Method to show an optionPane of an information message.
	 * 
	 * @param texto
	 *          String of the information message to show.
	 */
	private void informationMessage(String texto) {
		JOptionPane optionPane = new JOptionPane();
		optionPane.setMessage(texto);
		optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
		JDialog dialog = optionPane.createDialog(null, "Information Dialog");
		dialog.setVisible(true);
	}

	/**
	 * 
	 * Getter of the arrayList agent.
	 * 
	 * @return ArrayList of agents.
	 * 
	 */
	public ArrayList<CommunityAgent> getAgents() {
		return agents;
	}
}
