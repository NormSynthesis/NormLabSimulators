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
import es.csic.iiia.normlab.onlinecomm.metrics.CommunityMetricsManager;
import es.csic.iiia.normlab.onlinecomm.nsm.CommunityNormSynthesisAgent;
import es.csic.iiia.normlab.onlinecomm.nsm.perception.CommunityWatcher;
import es.csic.iiia.normlab.onlinecomm.section.SectionForum;
import es.csic.iiia.normlab.onlinecomm.section.SectionPhotoVideo;
import es.csic.iiia.normlab.onlinecomm.section.SectionTheReporter;
import es.csic.iiia.nsm.NormSynthesisMachine;
import es.csic.iiia.nsm.agent.language.PredicatesDomains;
import es.csic.iiia.nsm.agent.language.TaxonomyOfNaturalNumbers;
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

		random = new Random();
		contextData = new ContextData(random);

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

		int idealNormativeSystemCardinality = 0;
		ArrayList<CommunityAgent> agents;

		/* Create norm synthesis agent and metrics */
		CommunityNormSynthesisAgent nsmAgent = createNSMAgent();
		createMetricsManager(nsmAgent.getNormSynthesisMachine()); 

		/* Create online community manager */
		createManager();


		/* If the simulation is running in batch get the agents and parameters
		 * from the XML file. If not open the configuration window */
		if(!RunEnvironment.getInstance().isBatch()) 	{
			makeUserPanel();

			/* Open the agent configuration window */
			ConfigureAgentWindow caw = new ConfigureAgentWindow(new JFrame(), true);
			createAgentsPopulation(caw.getAgents(),this.predDomains);
			agents = caw.getAgents();

		}
		else {
			XMLParser leerFichero = new XMLParser();
			ArrayList<CommunityAgent> agentes = leerFichero.getPopulationFromXML();
			createAgentsPopulation(agentes,this.predDomains);
			agents = agentes;

		}

		for(CommunityAgent a : agents)
			if(a.getType() != 1)
				idealNormativeSystemCardinality = a.getQuantity();

		contextData.setIdealNormativeSystemCardinality(idealNormativeSystemCardinality);


		//Read the stop tick from the parameters of the simulation 
		Parameters params = RunEnvironment.getInstance().getParameters();
		int stopTick= (Integer) params.getValue("StopTick");		
		//Edit the simulation to stop at the tick read from the parameter.
		RunEnvironment.getInstance().endAt(stopTick);


		return context;
	}

	/**
	 * Method to create the iron agent and also create its scheduled methods to run every tick.
	 */
	private CommunityNormSynthesisAgent createNSMAgent()
	{
		ScheduleParameters scheduleParams;
		ISchedule schedule;

		schedule = RunEnvironment.getInstance().getCurrentSchedule();

		CommunityWatcher watcher = new CommunityWatcher(contextData);
		CommunityNormSynthesisAgent nsAgent =
				new CommunityNormSynthesisAgent(watcher, contextData, predDomains);

		// Create scheduler for watcher
		scheduleParams = ScheduleParameters.createRepeating(start, interval, -2);
		schedule.schedule(scheduleParams, watcher, "perceive");

		// Create scheduler for view and complaint content
		scheduleParams = ScheduleParameters.createRepeating(start, interval, ScheduleParameters.LAST_PRIORITY);
		schedule.schedule(scheduleParams, nsAgent, "step");

		context.add(nsAgent);

		return nsAgent;
	}

	/**
	 * Method to create the iron agent and also create its scheduled methods to run every tick.
	 */
	private void createMetricsManager(NormSynthesisMachine nsm)
	{
		ScheduleParameters scheduleParams;
		ISchedule schedule;

		schedule = RunEnvironment.getInstance().getCurrentSchedule();

		CommunityMetricsManager metricsManager = new CommunityMetricsManager(contextData, nsm);

		// Create scheduler for manager
		scheduleParams = ScheduleParameters.createRepeating(start, interval, -2);
		schedule.schedule(scheduleParams, metricsManager, "update");

		context.add(metricsManager);
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

		for(int numeroAgente = 0 ; numeroAgente < agents.size() ; numeroAgente++){
			for(int cantidadAgentes = 0 ; cantidadAgentes < agents.get(numeroAgente).getQuantity() ; cantidadAgentes++){				
				row = contextData.getNewRow();
				createAgent(agents.get(numeroAgente), predDomains);
				contextData.setNumAgents(contextData.getNumAgents() + 1);
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
		TaxonomyOfNaturalNumbers usrPredTaxonomy = new TaxonomyOfNaturalNumbers("usr");
		TaxonomyOfNaturalNumbers secPredTaxonomy = new TaxonomyOfNaturalNumbers("sec");
		
		/* Predicate "front" domain*/
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
