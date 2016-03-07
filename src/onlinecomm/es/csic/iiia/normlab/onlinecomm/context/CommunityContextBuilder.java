package es.csic.iiia.normlab.onlinecomm.context;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;
import repast.simphony.ui.RSApplication;
import es.csic.iiia.normlab.onlinecomm.XMLParser.XMLParser;
import es.csic.iiia.normlab.onlinecomm.agents.CommunityAgent;
import es.csic.iiia.normlab.onlinecomm.agents.CommunityManager;
import es.csic.iiia.normlab.onlinecomm.agents.ModerateAgent;
import es.csic.iiia.normlab.onlinecomm.agents.PornographicAgent;
import es.csic.iiia.normlab.onlinecomm.agents.RudeAgent;
import es.csic.iiia.normlab.onlinecomm.agents.SpammerAgent;
import es.csic.iiia.normlab.onlinecomm.agents.ViolentAgent;
import es.csic.iiia.normlab.onlinecomm.agents.profile.ComplaintProfile;
import es.csic.iiia.normlab.onlinecomm.agents.profile.UploadProfile;
import es.csic.iiia.normlab.onlinecomm.agents.profile.ViewProfile;
import es.csic.iiia.normlab.onlinecomm.graphics.CreateGraphics;
import es.csic.iiia.normlab.onlinecomm.nsm.CommunityNormSynthesisAgent;
import es.csic.iiia.normlab.onlinecomm.nsm.CommunityNormSynthesisSettings;
import es.csic.iiia.normlab.onlinecomm.nsm.perception.CommunityWatcher;
import es.csic.iiia.normlab.onlinecomm.section.SectionForum;
import es.csic.iiia.normlab.onlinecomm.section.SectionPhotoVideo;
import es.csic.iiia.normlab.onlinecomm.section.SectionTheReporter;
import es.csic.iiia.nsm.agent.language.PredicatesDomains;
import es.csic.iiia.nsm.agent.language.TaxonomyOfTerms;

/**
 * Context builder of the simulation
 * 
 * @author davidsanchezpinsach
 * 
 * modified by Iosu Mendizabal
 */
public class CommunityContextBuilder implements ContextBuilder<Object> {

	static ContextData contextData; // Context data of the context
	static ContinuousSpace<Object> space; // Space of the context
	static Grid<Object> grid; // Grid of the context
	static Context<Object> context;

	private int row = 0;

	// Random and seed of the random numbers
	private Random random;

	// Schedule parameters
	private double start = 1, interval = 1, priority = 0;
	
	private CommunityNormSynthesisAgent nsAgent;
	private PredicatesDomains predDomains;

	/**
	 * Method that is called from repast when you start the simulation (Our MAIN).
	 * 
	 * @param context
	 * 			The context of the repast simulation.
	 * 
	 * @return Context<Object>
	 * 			It returns the built context.
	 */
	public Context<Object> build(Context<Object> context) {
		CommunityContextBuilder.context = context;

		Parameters params = RunEnvironment.getInstance().getParameters();
		int maxAgents = (Integer) params.getValue("maxAgents");	
		long contentsQueueSize = (Long) params.getValue("ContentsQueueSize");
		
		contextData = new ContextData(maxAgents, contentsQueueSize);
		
		context.setId("OnlineCommunityContext");

		ContinuousSpaceFactory spaceFactory = 
				ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);

		space = spaceFactory.
				createContinuousSpace("space", context, 
						new RandomCartesianAdder <Object >(),
						new repast.simphony.space.continuous.WrapAroundBorders(),
						contextData.getNumColumns(), contextData.getNumRows());

		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null); 
		grid = gridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(
						new WrapAroundBorders(), new SimpleGridAdder<Object>(), 
						false, contextData.getNumColumns(), 
						contextData.getNumRows())); 

		//Save the context data in position 0,0
		context.add(contextData);
		space.moveTo(contextData, 0, 0);
		grid.moveTo(contextData, 0, 0);

		//Make the social web sections
		makeSections();
		
		this.createPredicatesDomains();
		
		/* Create norm synthesis agent and metrics */
		this.createNSMAgent();

		/* Create online community manager */
		createManager();
		int idealNormativeSystemCardinality = 0;
		ArrayList<CommunityAgent> agents;

		/* If the simulation is running in batch get the agents and parameters
		 * from the XML file. If not open the configuration window */
		if(!RunEnvironment.getInstance().isBatch()) 	{
			makeUserPanel();

			/* Open the agent configuration window */
			ConfigureAgentWindow caw = new ConfigureAgentWindow(new JFrame(), true);
			createAgentsPopulation(caw.getAgents(), this.predDomains);
			agents = caw.getAgents();
		}
		else {
			XMLParser leerFichero = new XMLParser();
			ArrayList<CommunityAgent> agentes = leerFichero.getPopulationFromXML();		
			createAgentsPopulation(agentes, this.predDomains);
			agents = agentes;
		}
	
		for(CommunityAgent a : agents) {
			if(a.getType() != 1) {
				idealNormativeSystemCardinality += a.getQuantity();
			}
		}

		contextData.setIdealNormativeSystemCardinality(idealNormativeSystemCardinality);

		// Read the stop tick from the parameters of the simulation 
		int stopTick= (Integer) params.getValue("StopTick");		
		
		// Edit the simulation to stop at the tick read from the parameter.
		RunEnvironment.getInstance().endAt(stopTick);


		return context;
	}

	/**
	 * Method to create the iron agent and also create its scheduled methods to run every tick.
	 */
	private CommunityNormSynthesisAgent createNSMAgent() {
		ScheduleParameters scheduleParams;
		ISchedule schedule;

		/* Create random seed */
		int seed = (int)System.currentTimeMillis();
		CommunityNormSynthesisSettings.init();

		// Set the defined seed only if the simulation is not batch and the seed is != 0
		if(CommunityNormSynthesisSettings.SIM_RANDOM_SEED != 0l) {
			seed = CommunityNormSynthesisSettings.SIM_RANDOM_SEED;
		}

		schedule = RunEnvironment.getInstance().getCurrentSchedule();
		CommunityWatcher watcher = new CommunityWatcher(contextData);
		
		boolean isGui = !RunEnvironment.getInstance().isBatch();
		CommunityNormSynthesisAgent nsAgent =
				new CommunityNormSynthesisAgent(watcher, contextData, predDomains, seed);

		// Create scheduler for watcher
		scheduleParams = ScheduleParameters.createRepeating(start, interval, -2);
		schedule.schedule(scheduleParams, watcher, "perceive");

		// Create scheduler for view and complaint content
		scheduleParams = ScheduleParameters.createRepeating(start, interval, ScheduleParameters.LAST_PRIORITY);
		schedule.schedule(scheduleParams, nsAgent, "step");

		context.add(nsAgent);
		context.add(nsAgent.getMetricsManager());

		return nsAgent;
	}

	/**
	 * Method to create the iron agent and also create its scheduled methods to run every tick.
	 */
	private void createManager() {
		ScheduleParameters scheduleParams;
		ISchedule schedule;

		schedule = RunEnvironment.getInstance().getCurrentSchedule();

		CommunityManager comMannager = new CommunityManager(contextData);

		// Create scheduler for watcher
		scheduleParams = ScheduleParameters.createRepeating(start, interval, ScheduleParameters.FIRST_PRIORITY);
		schedule.schedule(scheduleParams, comMannager, "step");

		context.add(comMannager);
	}


	/**
	 * Method to create sections of the simulation
	 */
	private void makeSections() {
		// Add the section title to display
		row = contextData.getNumRows() - 1;

		// Section The reporter
		SectionTheReporter sectionTheReporter = new SectionTheReporter();

		context.add(sectionTheReporter);
		space.moveTo(sectionTheReporter, contextData.getSectionReporter(), row);
		grid.moveTo(sectionTheReporter, contextData.getSectionReporter(), row);

		// Section Forum
		SectionForum sectionForum = new SectionForum();

		context.add(sectionForum);
		space.moveTo(sectionForum, contextData.getSectionForum(), row);
		grid.moveTo(sectionForum, contextData.getSectionForum(), row);

		// Section Photo & Video
		SectionPhotoVideo sectionPhotoVideo = new SectionPhotoVideo();

		context.add(sectionPhotoVideo);
		space.moveTo(sectionPhotoVideo, contextData.getSectionMultimedia(), row);
		grid.moveTo(sectionPhotoVideo, contextData.getSectionMultimedia(), row);
	}

	/**
	 * Method to create agents population for the simulation.
	 * 
	 * @param agents
	 *            ArrayList of agents that are going to take part in the simulation.
	 */
	public void createAgentsPopulation(ArrayList<CommunityAgent> agents,
			PredicatesDomains predDomains) {
		row = 0;
//		contextData.setNumAgents(agents.size());

		for(int numeroAgente = 0 ; numeroAgente < agents.size() ; numeroAgente++){
			for(int cantidadAgentes = 0 ; cantidadAgentes < agents.get(numeroAgente).getQuantity() ; cantidadAgentes++){				
				row = contextData.getNewRow();
				createAgent(agents.get(numeroAgente), predDomains);
				contextData.setNumAgents(contextData.getNumAgents()+1);
			}
		}
	}

	/**
	 * Method to create a new Agent
	 * 
	 * @param agent
	 * 			Object with all the information to create a new one.
	 */
	private void createAgent(CommunityAgent agent, 
			PredicatesDomains predDomains) {
		UploadProfile up = new UploadProfile(
				agent.getUploadProfile().getUploadProbability(),
				agent.getUploadProfile().getCorrect(),
				agent.getUploadProfile().getSpam(),
				agent.getUploadProfile().getPorn(),
				agent.getUploadProfile().getViolent(),
				agent.getUploadProfile().getInsult());

		// Take the total comments number from the parameters of Repast.
		Parameters params = RunEnvironment.getInstance().getParameters();
		int totalComments = (Integer) params.getValue("Total Comments");					

		contextData.setTotalComments(totalComments);

		up.setTotalComments(contextData.getTotalComments());
		up.setMaxCommentsTheReporter(contextData.getMaxCommentsTheReporter());
		up.setMaxCommentsForum(contextData.getMaxCommentsForum());
		up.setMaxCommentsPhotoVideo(contextData.getMaxCommentsPhotoVideo());

		ViewProfile vp = new ViewProfile(agent.getViewProfile().getForum(),
				agent.getViewProfile().getTheReporter(),
				agent.getViewProfile().getPhotoVideo(),
				agent.getViewProfile().getViewMode());

		ComplaintProfile cp = new ComplaintProfile(
				agent.getComplaintProfile().getSpam(),
				agent.getComplaintProfile().getPorn(),
				agent.getComplaintProfile().getViolent(),
				agent.getComplaintProfile().getInsult());
				

		/* Create agent depending on the type */
		CommunityAgent commAgent = null;

		switch (agent.getType()) {
		case 1:
			commAgent = new ModerateAgent(predDomains,space, grid, row, up, vp, cp);
			break;
		case 2:
			commAgent = new SpammerAgent(predDomains,space, grid, row, up, vp, cp);
			break;
		case 3:
			commAgent = new PornographicAgent(predDomains,space, grid, row, up, vp, cp);
			break;
		case 4:
			commAgent = new ViolentAgent(predDomains,space, grid, row, up, vp, cp);
			break;
		case 5:
			commAgent = new RudeAgent(predDomains,space, grid, row, up, vp, cp);
			break;

		default:
			break;
		}

		/* Add agent to the repast context */
		context.add(commAgent);
		space.moveTo(commAgent, 0, row);
		grid.moveTo(commAgent, 0, row);
		configureScheduleAgent(commAgent, start, interval, priority);		
	}

	/**
	 * Method to create schedule methods for each agent
	 * 
	 * @param agent
	 *            Agent that is going to use this schedule
	 * @param start
	 *            Start of tick action of the agent
	 * @param interval
	 *            Interval of tick action of the agent
	 * @param priority
	 *            Priority of the schedule of this agent
	 */
	private void configureScheduleAgent(CommunityAgent agent, double start, double interval, double priority) {
		ScheduleParameters scheduleParams;
		ISchedule schedule;

		schedule = RunEnvironment.getInstance().getCurrentSchedule();

		// Create scheduler for upload content
		scheduleParams = ScheduleParameters.createRepeating(start, interval, priority);
		schedule.schedule(scheduleParams, agent, "upLoadContent");

		// Create scheduler for view and complaint content
		scheduleParams = ScheduleParameters.createRepeating(500, interval, priority + contextData.getMaxAgents());
		schedule.schedule(scheduleParams, agent, "viewAndComplaintContent");
	}

	/**
	 * Method to feed the user panel
	 */
	private void makeUserPanel() {
		JPanel panel = new JPanel();

		panel.setBorder(new TitledBorder("Graphics:"));
		panel.setLayout(new GridLayout(6, 2, 15, 0));// 6 rows, 2 cols

		final JButton button = new JButton("Complaint Graphic");
		JLabel label = new JLabel("View Graphics");
		button.setEnabled(true);

		panel.add(label);
		panel.add(button);

		// Add action listener to button
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				CreateGraphics graphic = new CreateGraphics("Grafica Complaints");
				contextData.addSeriesToCollection(contextData.getSeries());
				//contextData.addSeriesToCollection(contextData.getSerie2());
				contextData.addSeriesToCollection(contextData.getSerie3());
				graphic.setDataset(contextData.getCollection());
				graphic.createChart();
				//button.setEnabled(false);
			}
		});
		//		RSApplication.getRSApplicationInstance().removeCustomUserPanel();
		RSApplication.getRSApplicationInstance().addCustomUserPanel(panel);
	}
	
	/**
	 * Creates the predicate and their domains for the traffic scenario
	 */
	private void createPredicatesDomains() {

		/* Predicate "usr" domain */
//		TaxonomyOfNaturalNumbers usrPredTaxonomy = new TaxonomyOfNaturalNumbers("usr");
//		TaxonomyOfNaturalNumbers secPredTaxonomy = new TaxonomyOfNaturalNumbers("sec");
		
		/* Predicate "user" domain*/
		TaxonomyOfTerms usrPredTaxonomy = new TaxonomyOfTerms("usr");

		usrPredTaxonomy.addTerm("*");
		
		//TODO los predicados no se ponen con el numero de agentes porque se calculan mas tarde.
		
		//for (int i = 0 ; i <= contextData.getMaxAgents() ; i++){
		for (int i = 0 ; i <= 1000 ; i++){
			usrPredTaxonomy.addTerm(""+i);
			usrPredTaxonomy.addRelationship(""+i, "*");
		}
//		usrPredTaxonomy.addTerm("0");
//		usrPredTaxonomy.addTerm("1");
//		usrPredTaxonomy.addTerm("2");
//		usrPredTaxonomy.addTerm("3");
//		usrPredTaxonomy.addTerm("4");
//		usrPredTaxonomy.addTerm("5");
//		usrPredTaxonomy.addTerm("6");
//		usrPredTaxonomy.addTerm("7");
//		usrPredTaxonomy.addTerm("8");
//		usrPredTaxonomy.addTerm("9");
//		usrPredTaxonomy.addTerm("10");
//		usrPredTaxonomy.addTerm("11");
//		usrPredTaxonomy.addTerm("12");
//		usrPredTaxonomy.addTerm("13");
//		usrPredTaxonomy.addTerm("14");
//		usrPredTaxonomy.addTerm("15");
//		usrPredTaxonomy.addTerm("16");
//		usrPredTaxonomy.addTerm("17");
//		usrPredTaxonomy.addTerm("18");
//		usrPredTaxonomy.addTerm("19");
//		usrPredTaxonomy.addTerm("20");
//		usrPredTaxonomy.addTerm("21");
//		usrPredTaxonomy.addTerm("22");
//		usrPredTaxonomy.addTerm("23");
//		usrPredTaxonomy.addTerm("24");
//		usrPredTaxonomy.addTerm("25");
//		usrPredTaxonomy.addTerm("26");
//		usrPredTaxonomy.addTerm("27");
//		usrPredTaxonomy.addTerm("28");
//		usrPredTaxonomy.addTerm("29");
//		usrPredTaxonomy.addTerm("30");
//		usrPredTaxonomy.addTerm("31");
//		usrPredTaxonomy.addTerm("32");
//		usrPredTaxonomy.addTerm("33");
//		usrPredTaxonomy.addTerm("34");
//		usrPredTaxonomy.addTerm("35");
//		usrPredTaxonomy.addTerm("36");
//		usrPredTaxonomy.addTerm("37");
//		usrPredTaxonomy.addTerm("38");
//		usrPredTaxonomy.addTerm("39");
//		usrPredTaxonomy.addTerm("40");
//		usrPredTaxonomy.addTerm("41");
//		usrPredTaxonomy.addTerm("42");
//		usrPredTaxonomy.addTerm("43");
//		usrPredTaxonomy.addTerm("44");
//		usrPredTaxonomy.addTerm("45");
//		usrPredTaxonomy.addTerm("46");
//		usrPredTaxonomy.addTerm("47");
//		usrPredTaxonomy.addTerm("48");
//		usrPredTaxonomy.addTerm("49");
//		usrPredTaxonomy.addTerm("50");
//		usrPredTaxonomy.addTerm("51");
//		usrPredTaxonomy.addTerm("52");
//		usrPredTaxonomy.addTerm("53");
//		usrPredTaxonomy.addTerm("54");
//		usrPredTaxonomy.addTerm("55");
//		usrPredTaxonomy.addTerm("56");
//		usrPredTaxonomy.addTerm("57");
//		usrPredTaxonomy.addTerm("58");
//		usrPredTaxonomy.addTerm("59");
//		usrPredTaxonomy.addTerm("60");
//		usrPredTaxonomy.addTerm("61");
//		usrPredTaxonomy.addTerm("62");
//		usrPredTaxonomy.addTerm("63");
//		usrPredTaxonomy.addTerm("64");
//		usrPredTaxonomy.addTerm("65");
//		usrPredTaxonomy.addTerm("66");
//		usrPredTaxonomy.addTerm("67");
//		usrPredTaxonomy.addTerm("68");
//		usrPredTaxonomy.addTerm("69");
//		usrPredTaxonomy.addTerm("70");
//		usrPredTaxonomy.addTerm("71");
//		usrPredTaxonomy.addTerm("72");
//		usrPredTaxonomy.addTerm("73");
//		usrPredTaxonomy.addTerm("74");
//		usrPredTaxonomy.addTerm("75");
//		usrPredTaxonomy.addTerm("76");
//		usrPredTaxonomy.addTerm("77");
//		usrPredTaxonomy.addTerm("78");
//		usrPredTaxonomy.addTerm("79");
//		usrPredTaxonomy.addTerm("80");
//		usrPredTaxonomy.addTerm("81");
//		usrPredTaxonomy.addTerm("82");
//		usrPredTaxonomy.addTerm("83");
//		usrPredTaxonomy.addTerm("84");
//		usrPredTaxonomy.addTerm("85");
//		usrPredTaxonomy.addTerm("86");
//		usrPredTaxonomy.addTerm("87");
//		usrPredTaxonomy.addTerm("88");
//		usrPredTaxonomy.addTerm("89");
//		usrPredTaxonomy.addTerm("90");
//		usrPredTaxonomy.addTerm("91");
//		usrPredTaxonomy.addTerm("92");
//		usrPredTaxonomy.addTerm("93");
//		usrPredTaxonomy.addTerm("94");
//		usrPredTaxonomy.addTerm("95");
//		usrPredTaxonomy.addTerm("96");
//		usrPredTaxonomy.addTerm("97");
//		usrPredTaxonomy.addTerm("98");
//		usrPredTaxonomy.addTerm("99");
//		usrPredTaxonomy.addTerm("100");

//		
//		usrPredTaxonomy.addRelationship("0", "*");
//		usrPredTaxonomy.addRelationship("1", "*");
//		usrPredTaxonomy.addRelationship("2", "*");
//		usrPredTaxonomy.addRelationship("3", "*");
//		usrPredTaxonomy.addRelationship("4", "*");
//		usrPredTaxonomy.addRelationship("5", "*");
//		usrPredTaxonomy.addRelationship("6", "*");
//		usrPredTaxonomy.addRelationship("7", "*");
//		usrPredTaxonomy.addRelationship("8", "*");
//		usrPredTaxonomy.addRelationship("9", "*");
//		usrPredTaxonomy.addRelationship("10", "*");
//		usrPredTaxonomy.addRelationship("11", "*");
//		usrPredTaxonomy.addRelationship("12", "*");
//		usrPredTaxonomy.addRelationship("13", "*");
//		usrPredTaxonomy.addRelationship("14", "*");
//		usrPredTaxonomy.addRelationship("15", "*");
//		usrPredTaxonomy.addRelationship("16", "*");
//		usrPredTaxonomy.addRelationship("17", "*");
//		usrPredTaxonomy.addRelationship("18", "*");
//		usrPredTaxonomy.addRelationship("19", "*");
//		usrPredTaxonomy.addRelationship("20", "*");
//		usrPredTaxonomy.addRelationship("21", "*");
//		usrPredTaxonomy.addRelationship("22", "*");
//		usrPredTaxonomy.addRelationship("23", "*");
//		usrPredTaxonomy.addRelationship("24", "*");
//		usrPredTaxonomy.addRelationship("25", "*");
//		usrPredTaxonomy.addRelationship("26", "*");
//		usrPredTaxonomy.addRelationship("27", "*");
//		usrPredTaxonomy.addRelationship("28", "*");
//		usrPredTaxonomy.addRelationship("29", "*");
//		usrPredTaxonomy.addRelationship("30", "*");
//		usrPredTaxonomy.addRelationship("31", "*");
//		usrPredTaxonomy.addRelationship("32", "*");
//		usrPredTaxonomy.addRelationship("33", "*");
//		usrPredTaxonomy.addRelationship("34", "*");
//		usrPredTaxonomy.addRelationship("35", "*");
//		usrPredTaxonomy.addRelationship("36", "*");
//		usrPredTaxonomy.addRelationship("37", "*");
//		usrPredTaxonomy.addRelationship("38", "*");
//		usrPredTaxonomy.addRelationship("39", "*");
//		usrPredTaxonomy.addRelationship("40", "*");
//		usrPredTaxonomy.addRelationship("41", "*");
//		usrPredTaxonomy.addRelationship("42", "*");
//		usrPredTaxonomy.addRelationship("43", "*");
//		usrPredTaxonomy.addRelationship("44", "*");
//		usrPredTaxonomy.addRelationship("45", "*");
//		usrPredTaxonomy.addRelationship("46", "*");
//		usrPredTaxonomy.addRelationship("47", "*");
//		usrPredTaxonomy.addRelationship("48", "*");
//		usrPredTaxonomy.addRelationship("49", "*");
//		usrPredTaxonomy.addRelationship("50", "*");
//		usrPredTaxonomy.addRelationship("51", "*");
//		usrPredTaxonomy.addRelationship("52", "*");
//		usrPredTaxonomy.addRelationship("53", "*");
//		usrPredTaxonomy.addRelationship("54", "*");
//		usrPredTaxonomy.addRelationship("55", "*");
//		usrPredTaxonomy.addRelationship("56", "*");
//		usrPredTaxonomy.addRelationship("57", "*");
//		usrPredTaxonomy.addRelationship("58", "*");
//		usrPredTaxonomy.addRelationship("59", "*");
//		usrPredTaxonomy.addRelationship("60", "*");
//		usrPredTaxonomy.addRelationship("61", "*");
//		usrPredTaxonomy.addRelationship("62", "*");
//		usrPredTaxonomy.addRelationship("63", "*");
//		usrPredTaxonomy.addRelationship("64", "*");
//		usrPredTaxonomy.addRelationship("65", "*");
//		usrPredTaxonomy.addRelationship("66", "*");
//		usrPredTaxonomy.addRelationship("67", "*");
//		usrPredTaxonomy.addRelationship("68", "*");
//		usrPredTaxonomy.addRelationship("69", "*");
//		usrPredTaxonomy.addRelationship("70", "*");
//		usrPredTaxonomy.addRelationship("71", "*");
//		usrPredTaxonomy.addRelationship("72", "*");
//		usrPredTaxonomy.addRelationship("73", "*");
//		usrPredTaxonomy.addRelationship("74", "*");
//		usrPredTaxonomy.addRelationship("75", "*");
//		usrPredTaxonomy.addRelationship("76", "*");
//		usrPredTaxonomy.addRelationship("77", "*");
//		usrPredTaxonomy.addRelationship("78", "*");
//		usrPredTaxonomy.addRelationship("79", "*");
//		usrPredTaxonomy.addRelationship("80", "*");
//		usrPredTaxonomy.addRelationship("81", "*");
//		usrPredTaxonomy.addRelationship("82", "*");
//		usrPredTaxonomy.addRelationship("83", "*");
//		usrPredTaxonomy.addRelationship("84", "*");
//		usrPredTaxonomy.addRelationship("85", "*");
//		usrPredTaxonomy.addRelationship("86", "*");
//		usrPredTaxonomy.addRelationship("87", "*");
//		usrPredTaxonomy.addRelationship("88", "*");
//		usrPredTaxonomy.addRelationship("89", "*");
//		usrPredTaxonomy.addRelationship("90", "*");
//		usrPredTaxonomy.addRelationship("91", "*");
//		usrPredTaxonomy.addRelationship("92", "*");
//		usrPredTaxonomy.addRelationship("93", "*");
//		usrPredTaxonomy.addRelationship("94", "*");
//		usrPredTaxonomy.addRelationship("95", "*");
//		usrPredTaxonomy.addRelationship("96", "*");
//		usrPredTaxonomy.addRelationship("97", "*");
//		usrPredTaxonomy.addRelationship("98", "*");
//		usrPredTaxonomy.addRelationship("99", "*");
//		usrPredTaxonomy.addRelationship("100", "*");

		/* Predicate "section" domain*/
		TaxonomyOfTerms secPredTaxonomy = new TaxonomyOfTerms("sec");
		secPredTaxonomy.addTerm("*");
		secPredTaxonomy.addTerm("1");
		secPredTaxonomy.addTerm("2");
		secPredTaxonomy.addTerm("3");
		
		secPredTaxonomy.addRelationship("1", "*");
		secPredTaxonomy.addRelationship("2", "*");
		secPredTaxonomy.addRelationship("3", "*");
		
		/* Predicate "content" domain*/
		TaxonomyOfTerms cntTypePredTaxonomy = new TaxonomyOfTerms("cnt");
		cntTypePredTaxonomy.addTerm("correct");
		cntTypePredTaxonomy.addTerm("spam");
		cntTypePredTaxonomy.addTerm("porn");
		cntTypePredTaxonomy.addTerm("violent");
		cntTypePredTaxonomy.addTerm("insult");
		
		cntTypePredTaxonomy.addRelationship("correct", "*");
		cntTypePredTaxonomy.addRelationship("spam", "*");
		cntTypePredTaxonomy.addRelationship("porn", "*");
		cntTypePredTaxonomy.addRelationship("violent", "*");
		cntTypePredTaxonomy.addRelationship("insult", "*");
		
		this.predDomains = new PredicatesDomains();
		this.predDomains.addPredicateDomain("usr", usrPredTaxonomy);
		this.predDomains.addPredicateDomain("sec", secPredTaxonomy);
		this.predDomains.addPredicateDomain("cnt", cntTypePredTaxonomy);
	}
}
