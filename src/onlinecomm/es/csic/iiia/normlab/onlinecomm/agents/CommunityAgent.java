package es.csic.iiia.normlab.onlinecomm.agents;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import es.csic.iiia.normlab.onlinecomm.agents.norms.CommunityAgentReasoner;
import es.csic.iiia.normlab.onlinecomm.agents.norms.CommunityNormEngine;
import es.csic.iiia.normlab.onlinecomm.agents.profile.ComplaintProfile;
import es.csic.iiia.normlab.onlinecomm.agents.profile.UploadProfile;
import es.csic.iiia.normlab.onlinecomm.agents.profile.ViewProfile;
import es.csic.iiia.normlab.onlinecomm.bbdd.BbddManager;
import es.csic.iiia.normlab.onlinecomm.content.IComplaint;
import es.csic.iiia.normlab.onlinecomm.content.IContent;
import es.csic.iiia.normlab.onlinecomm.content.complaint.Complaint;
import es.csic.iiia.normlab.onlinecomm.context.ContextData;
import es.csic.iiia.normlab.onlinecomm.distribution.Gamma;
import es.csic.iiia.normlab.onlinecomm.nsm.CommunityNormSynthesisAgent;
import es.csic.iiia.normlab.onlinecomm.nsm.agent.CommunityAgentAction;
import es.csic.iiia.normlab.onlinecomm.nsm.agent.CommunityAgentContext;
import es.csic.iiia.nsm.agent.EnvironmentAgent;
import es.csic.iiia.nsm.agent.language.PredicatesDomains;
import es.csic.iiia.nsm.norm.Norm;

/**
 * Agents action class.
 * 
 * @author Iosu Mendizabal
 * 
 */
public class CommunityAgent implements EnvironmentAgent {
	
	private long id;
	private int section;
	protected int posAgentRow;
	protected Calendar dataUserCreation;
	
	private CommunityAgentContext agentContext;
	private CommunityAgentAction agentAction;
	private CommunityAgentReasoner reasoner;
	
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	private ContextData contextData;

	protected BbddManager bbddManager; // database connection

	// Profiles of the agent
	protected UploadProfile upLoadProfile;
	protected ViewProfile viewProfile;
	protected ComplaintProfile complaintProfile;
	protected Context<Object> context;

	// Sections
	public static final int sectionTheReporter = 1;
	public static final int sectionForum = 2;
	public static final int sectionPhotoVideo = 3;

	// Type of view profile
	private final int contentByOrder = 0;
	private final int contentByMostViews = 1;
	private final int contentByRandom = 2;
	
	//Content types.
	private final int spam = 2;
	private final int porn = 3;
	private final int violent = 4;
	private final int insult = 5;

	// Agents characteristics
	private int type;
	private int quantity;
	private String name, population;
	
	private Gamma g;
	private IContent content = null;
	
	/**
	 * Constructor of the agent class with space and grid
	 * 
	 * @param space Space of the context
	 * @param grid Grid of the context
	 * @param posAgentRow Position of the agent
	 * @param upLoadProfile UploadProfile
	 * @param viewProfile ViewProfile
	 * @param complaintProfile Complaint profile
	 */
	public CommunityAgent(PredicatesDomains predDomains, 
			ContinuousSpace<Object> space , Grid<Object> grid, 
			int posAgentRow, UploadProfile upLoadProfile,	
			ViewProfile viewProfile, ComplaintProfile complaintProfile) {
		
		this.space = space;
		this.grid = grid;
		this.posAgentRow = posAgentRow;
		this.upLoadProfile = upLoadProfile;
		this.viewProfile = viewProfile;
		this.complaintProfile = complaintProfile;
		this.agentAction = CommunityAgentAction.Nothing;
		
		//Create the class manager to the connections between bbdd and repast
		bbddManager = new BbddManager();
		dataUserCreation = Calendar.getInstance();
		
		contextData = (ContextData) grid.getObjectAt(0,0);
		id = contextData.getNumAgents();
		
		this.agentContext = new CommunityAgentContext(this);
		this.reasoner = new CommunityAgentReasoner(predDomains, contextData);
	}
	
	/**
	 * Constructor to generate populations of the agents with a same type
	 * 
	 * @param type
	 *        Type of the agent
	 * @param quantity
	 *        Quantity of the agent
	 * @param name
	 *        Name of the agent
	 * @param up
	 *        UploadProfile
	 * @param vp
	 *        ViewProfile
	 * @param cp
	 *        ComplaintProfile
	 * @param population
	 *        Name of the population to use
	 */
	public CommunityAgent(int type, int quantity, String name, UploadProfile up,
			ViewProfile vp, ComplaintProfile cp, String population) {
		
		this.type = type;
		this.quantity = quantity;
		this.name = name;
		this.upLoadProfile = up;
		this.viewProfile = vp;
		this.complaintProfile = cp;
		this.population = population;
	}
	
	/**
	 * Method to generate content to upload and put this in the context
	 */
	public void upLoadContent(){
		//System.out.println("step upload content");
		
		ContextData contextData = (ContextData) grid.getObjectAt(0,0);
		
		int randomNum = contextData.nextIntRandom(100);
		double uploadContentPercentage = upLoadProfile.getUploadProbability() * 100;
		
		if(randomNum <= uploadContentPercentage){
			IContent content = upLoadProfile.getFirstListUploadProfile();
			
			if(content != null){
				// 1. Perceive: Update agent context (for IRON)
				section = content.getSection();
				agentContext.update(content);
			
				// 2: Decide action: Reason about norms taking into account the agent context
				agentAction = reasoner.decideAction(agentContext);
				
				// 3. Execute decision: Do action -> Upload / Nothing (not upload)
				if(agentAction == CommunityAgentAction.Upload){
					Norm norm = reasoner.getNormToViolate();
					if(norm != null) {
						content.setViolatedNorm(norm);
					}
					doUpload(content);
					contextData.setNumSocialNetworkContents(contextData.getNumSocialNetworkContents() + 1);
					//System.out.println("Contenido = "+contextData.getNumSocialNetworkContents());
				}	
			}
		}
	}
	
	/**
	 * Method to do a content upload from an agent
	 * 
	 * @param content
	 * 			content is going to be uploaded to the virtual community.
	 */
	@SuppressWarnings("unchecked")
	private void doUpload(IContent content){
		int positionContent = 0;
		
		if(content != null){
			context = ContextUtils.getContext(this);
			context.add(content);

			switch(content.getSection()){
				case sectionTheReporter:
					upLoadProfile.setNumCommentsTheReporter(upLoadProfile.getNumCommentsTheReporter() + 1);
					positionContent = upLoadProfile.getNumCommentsTheReporter();

					if(upLoadProfile.getNumCommentsTheReporter() > upLoadProfile.getMaxCommentsTheReporter()){
						positionContent = 11;
						moveCommentsAndComplaints(positionContent);
					}
					
					contextData.addContentListSectionTheReporter(content);
					contextData.addContentMostViewTheReporter(content);
				
					break;
				case sectionForum:
					upLoadProfile.setNumCommentsForum(upLoadProfile.getNumCommentsForum() + 1);
					positionContent = contextData.getMaxCommentsTheReporter() + 
									  upLoadProfile.getNumCommentsForum() + 
									  1;
					if(upLoadProfile.getNumCommentsForum() > upLoadProfile.getMaxCommentsForum()){
						positionContent = 23;
						moveCommentsAndComplaints(positionContent);
					}
					contextData.addContentListSectionForum(content);
					contextData.addContentMostViewForum(content);
				
					break;
				case sectionPhotoVideo:
					upLoadProfile.setNumCommentsPhotoVideo(upLoadProfile.getNumCommentsPhotoVideo() + 1);
					positionContent = contextData.getMaxCommentsTheReporter() + 
									  contextData.getMaxCommentsForum() + 
									  upLoadProfile.getNumCommentsPhotoVideo() + 
									  2;
					if(upLoadProfile.getNumCommentsPhotoVideo() > upLoadProfile.getMaxCommentsPhotoVideo()){
						positionContent = 35;
						moveCommentsAndComplaints(positionContent);
					}
					contextData.addContentListSectionPhotoVideo(content);
					contextData.addContentMostViewPhotoVideo(content);
				
					break;
			}
			if(content!=null){
				List<IContent> actualUploadList = contextData.getActualUploadList();
				content.setCreatorAgent((int) id);
				
				space.moveTo(content, positionContent, posAgentRow); 
				grid.moveTo(content, positionContent, posAgentRow);
				
				actualUploadList.add(content);
			}
		}
	}
	
	/**
	 * Method to move the comments while new comments are incoming and visually it isn't space in the context.
	 * 
	 * @param positionComment
	 * 			The original position of the comment is going to be moved.
	 */
	private void moveCommentsAndComplaints(int positionComment) {
		int positionCommentOri = positionComment;
		int previousPositionComment = 0;
		
		positionComment = positionComment - 10;
		previousPositionComment = positionComment;
		
		//Remove the first content
		context.remove(grid.getObjectAt(positionComment, posAgentRow));
		
		positionComment++;
		
		//Move the other contents to the left.
		for(int i = 0 ; i < 10 ; i++){
			grid.moveTo(grid.getObjectAt(positionComment, posAgentRow), previousPositionComment, posAgentRow);
			positionComment++;
			previousPositionComment++;
		}
		posAgentRow--;

		//Move complaints, if there isn't complain catch the exception
		positionComment = positionCommentOri;
		positionComment = positionComment - 10;
		previousPositionComment = positionComment;
		try{
			context.remove(grid.getObjectAt(positionComment, posAgentRow));
		}catch(Exception e){}
		
		positionComment++;
		
		for(int i = 0 ; i < 10 ; i++){
			try{
				grid.moveTo(grid.getObjectAt(positionComment, posAgentRow), previousPositionComment, posAgentRow);
			}catch(Exception e){}
			positionComment++;
			previousPositionComment++;
		}					
		posAgentRow++;		
	}

	/**
	 * Method to view and complaint about the content of the virtual community.
	 */
	public void viewAndComplaintContent(){
		//System.out.println("step view and complaint");
		int i = 0;
		while(i < 5){
			//Erase the actual view and complaint lists if the actualAgent is the first one.
			List<IContent> actualViewList = contextData.getActualViewList();
			
			//1.- View some upload content
			IContent content = viewContent();
			
			//1.1.- Update the actual view list.
			if(content != null){ // May be that the agent doesn't view anything...
				actualViewList.add(content);
			
				//2.- Complaint or not about the content visited
				boolean complaintDone = complaintContent(content);
				boolean normViolated = (content.getViolatedNorm() != null);
				boolean contentIsNotRegulated = !isContentRegulated(content);
				
				//3.- Update the graphic values if a complaint is done.
				updateGraphicValues(complaintDone, normViolated, contentIsNotRegulated);
			}
			i++;
		}
		
	}
	
	/**
	 * 
	 * @param content
	 * @return
	 */
	private boolean isContentRegulated(IContent content) {
		CommunityNormEngine nrmEngine = CommunityNormSynthesisAgent.getNormEngine();
		return nrmEngine.getNormsApplicableToContent(content).size() > 0;
	}
	
	/**
	 * Update the graphic values, if the parameter is true a complaint is done if is false it isn't
	 * 
	 * @param complaintDone
	 * 			boolean to know if a complaint is done or not.
	 */
	private void updateGraphicValues(boolean complaintDone, boolean normViolated, boolean contentIsNotRegulated) {		
		if(complaintDone){
			contextData.setNumComplaint(contextData.getNumComplaint() + 1);
			if(!normViolated && contentIsNotRegulated) {
				contextData.setNumNonRegulatedComplaints(contextData.getNumNonRegulatedComplaints() + 1);
				contextData.incNumCurrentNonRegulatedComplaints();
			}
		}	
	}

	/**
	 * Method to view content of the context
	 * 
	 * @return The content that was visit
	 */
	public IContent viewContent(){		
		int gammaNum = 0;
		g = new Gamma();
		section = getSectionToView();

		switch(viewProfile.getViewMode()){
			case contentByOrder:
				content = viewContentByOrder(gammaNum);
				break;
			case contentByMostViews:
				content = viewContentByMostView(gammaNum);
				break;
			case contentByRandom:
				content = viewContentByRandom(gammaNum);
				break;
		}
		return content;
	}

	/**
	 * Method to get a content from a list by time order.
	 * 
	 * @param gammaNum
	 * 			the random number generated by a gamma distribution.
	 * 
	 * @return the visited content.
	 */
	
	private IContent viewContentByOrder(int gammaNum) {
		switch(section){
			case sectionTheReporter:
				if(!contextData.getListSectionTheReporter().isEmpty()){
					//Get an aleatory number from the list using a gamma distribution.
					gammaNum = g.getNum(contextData.getListSectionTheReporter());
					
					//Increment the number of visits of the content in one.
					contextData.getContentListSectionTheReporter(gammaNum)
							   .setNumViews(contextData.getContentListSectionTheReporter(gammaNum)
									   				   .getNumViews() + 1);
					
					//Get the content that we just visited
					content = contextData.getContentListSectionTheReporter(gammaNum);
				}
				break;
			case sectionForum:
				if(!contextData.getListSectionForum().isEmpty()){
					//Get an aleatory number from the list using a gamma distribution.
					gammaNum = g.getNum(contextData.getListSectionForum());
					
					//Increment the number of visits of the content in one.
					contextData.getContentListSectionForum(gammaNum)
							   .setNumViews(contextData.getContentListSectionForum(gammaNum)
									   				   .getNumViews() + 1);
					
					//Get the content that we just visited
					content = contextData.getContentListSectionForum(gammaNum);
				}
				break;
			case sectionPhotoVideo:
				if(!contextData.getListSectionPhotoVideo().isEmpty()){
					//Get an aleatory number from the list using a gamma distribution.
					gammaNum = g.getNum(contextData.getListSectionPhotoVideo());
					
					//Increment the number of visits of the content in one.
					contextData.getContentListSectionPhotoVideo(gammaNum)
							   .setNumViews(contextData.getContentListSectionPhotoVideo(gammaNum)
									   				   .getNumViews() + 1);
					
					//Get the content that we just visited
					content = contextData.getContentListSectionPhotoVideo(gammaNum);
				}
				break;
		}		
		return content;
	}
	
	/**
	 * Method to get a content from a most view list.
	 * 
	 * @param gammaNum
	 * 			the random number generated by a gamma distribution.
	 * 
	 * @return the visited content.
	 */
	private IContent viewContentByMostView(int gammaNum) {
		switch(section){
			case sectionTheReporter:
				if(!contextData.getMostViewListTheReporter().isEmpty()){
					gammaNum = g.getNum(contextData.getMostViewListTheReporter());
					contextData.updateList(contextData.getMostViewListTheReporter(), gammaNum);
					content = contextData.getContentMostViewTheReporter(gammaNum);
				}
				break;
			case sectionForum:
				if(!contextData.getMostViewListForum().isEmpty()){
					gammaNum = g.getNum(contextData.getMostViewListForum());
					contextData.updateList(contextData.getMostViewListForum(), gammaNum);
					content = contextData.getContentMostViewForum(gammaNum);
				}
				break;
			case sectionPhotoVideo:
				if(!contextData.getMostViewListPhotoVideo().isEmpty()){
					gammaNum = g.getNum(contextData.getMostViewListPhotoVideo());
					contextData.updateList(contextData.getMostViewListPhotoVideo(), gammaNum);
					content = contextData.getContentMostViewPhotoVideo(gammaNum);
				}
				break;
			}
		return content;
	}

	/**
	 * Method to get a content from a list randomly.
	 * 
	 * @param gammaNum
	 * 			the random number generated by a gamma distribution.
	 * 
	 * @return the visited content.
	 */
	private IContent viewContentByRandom(int gammaNum) {
		int listSize = 0;
		int randomContent = 0;
		
		switch(section){
			case sectionTheReporter:
				if(!contextData.getListSectionTheReporter().isEmpty()){
					listSize = contextData.getListSectionTheReporter().size();
					if(listSize != 0){
						randomContent = contextData.nextIntRandom(listSize); //Give random item
					}
					contextData.getContentListSectionTheReporter(randomContent)
							   .setNumViews(contextData.getContentListSectionTheReporter(randomContent)
									   				   .getNumViews() + 1);
					
					content = contextData.getContentListSectionTheReporter(randomContent);
				}
				break;
			case sectionForum:
				if(!contextData.getListSectionForum().isEmpty()){
					listSize = contextData.getListSectionForum().size();
					if(listSize != 0){
						
						randomContent = contextData.nextIntRandom(listSize); //Give random item
					
					}
					contextData.getContentListSectionForum(randomContent)
							   .setNumViews(contextData.getContentListSectionForum(randomContent)
													   .getNumViews() + 1);

					content = contextData.getContentListSectionForum(randomContent);
				}
				break;
			case sectionPhotoVideo:
				if(!contextData.getListSectionPhotoVideo().isEmpty()){
					listSize = contextData.getListSectionPhotoVideo().size();
					if(listSize != 0){
						randomContent = contextData.nextIntRandom(listSize); //Give random item
						
					}
					contextData.getContentListSectionPhotoVideo(randomContent)
							   .setNumViews(contextData.getContentListSectionPhotoVideo(randomContent)
									   				   .getNumViews() + 1);
					
					content = contextData.getContentListSectionPhotoVideo(randomContent);
				}
				break;
		}		
		return content;
	}

	/**
	 * Method to get a section according to the percentage of the agent for each one.
	 * 
	 * @return
	 * 		 The section to view.
	 */
	private int getSectionToView() {
		ContextData contextData = (ContextData) grid.getObjectAt(0,0);
		
		int randomNum = contextData.nextIntRandom(100);
		int section = 0;
		
		double reporterSecPercentage = viewProfile.getTheReporter() * 100;
		double forumSecPercentage = viewProfile.getForum() * 100;
		//double multimediaSecPercentage = viewProfile.getPhotoVideo() * 100;

		if(0 <= randomNum && randomNum <= reporterSecPercentage){
			section = sectionTheReporter;
		}else if(reporterSecPercentage <= randomNum && randomNum <= (forumSecPercentage + reporterSecPercentage)){
			section = sectionForum;
		}else if((forumSecPercentage + reporterSecPercentage) <= randomNum && randomNum <= 100){
			section = sectionPhotoVideo;
		}

		return section;
	}

	
	/**
	 * Method to complaint contents
	 * 
	 * @param content Content that the agent visited
	 * 
	 * @return boolean
	 * 				true if a complaint is done.
	 * 				false if a complaint isn't done.
	 */
	public boolean complaintContent(IContent content){		
		int randomNum = 0;
		boolean complaintDone = false;
		List<IContent> actualComplaintList = contextData.getActualComplaintList();

		if(content != null){
			randomNum = contextData.nextIntRandom(100);
			double complaintType = 0;

			if(content.getCreatorAgent() != id){
				switch(content.getType()){
					case spam:
						complaintType = complaintProfile.getSpam() * 100;
						break;
					case porn:
						complaintType = complaintProfile.getPorn() * 100;
						break;
					case violent:
						complaintType = complaintProfile.getViolent() * 100;
						break;
					case insult:
						complaintType = complaintProfile.getInsult() * 100;
						break;
				}
			}
			
			try{
				complaintDone = contentIsPaintedInContext(content, randomNum, complaintType, actualComplaintList);
			}catch(Exception e){
				complaintDone = contentIsNotVisibleOnContext(content, randomNum, complaintType, actualComplaintList);
			}
		}
		return complaintDone;
	}

	/**
	 * 
	 * Method to add or create a complaint if the content is visible in the context, if it is painted.
	 * 
	 * @param content
	 * 			Content which are complaining.
	 * @param randomNum
	 * 			Random number from 0 to 100
	 * @param complaintType 
	 * 			Number from 0 to 100 that exposes the percentage the agent is complaining about this content type.
	 * @param actualComplaintList 
	 * 			ArrayList of contents that are complaint in the actual tick.
	 * 
	 * @return boolean
	 * 			true complaint done.
	 * 			false complaint not done.
	 */
	private boolean contentIsPaintedInContext(IContent content, int randomNum, double complaintType, List<IContent> actualComplaintList) {
		int x, y;

		//Get the content location in the grid.
		GridPoint gp = grid.getLocation(content);
		
		x = gp.getX();
		y = gp.getY();

		//Get the complaint from the grid.
		IComplaint complaint = (IComplaint)grid.getObjectAt(x, y - 1);
		
		if((randomNum <= complaintType) && (complaintType != 0)){
			if(complaint != null){
				content.setNumComplaints(content.getNumComplaints() + 1);
				
				complaint.setNumComplaints(content.getNumComplaints());
				complaint.setNumViews(content.getNumViews());
				
				//If a complaint is done, actualize the actual complaint list.
				actualComplaintList.add(content);
				
				return true;
			}
			
			complaint = new Complaint(content.getType(), 1);
			content.setComplain(complaint);
			content.setNumComplaints(1);
			complaint.setNumViews(content.getNumViews());
			
			//If a complaint is done, actualize the actual complaint list.
			actualComplaintList.add(content);
			
			context.add(complaint);
			space.moveTo(complaint, x, y - 1); 
			grid.moveTo(complaint, x, y - 1);
			
			return true;
		}
		return false;
	}

	
	/**
	 * Method to add or create a complaint if the content is not visible in the context
	 * 
	 * @param content
	 * 			Content which are complaining.
	 * @param randomNum
	 * 			Random number from 0 to 100
	 * @param complaintType 
	 * 			Number from 0 to 100 that exposes the percentage the agent is complaining about this content type.
	 * @param actualComplaintList 
	 * 	 		ArrayList of contents that are complaint in the actual tick.
	 *
	 * @return boolean
	 * 			true complaint done.
	 * 			false complaint not done.
	 */
	private boolean contentIsNotVisibleOnContext(IContent content, int randomNum, double complaintType, List<IContent> actualComplaintList) {
			IComplaint complaint = null;
			
			if((randomNum <= complaintType) && (complaintType != 0)){
				if(content.getNumComplaints() != 0){
					complaint = content.getComplaint();
					content.setNumComplaints(content.getNumComplaints() + 1);
					
					complaint.setNumComplaints(content.getNumComplaints());
					complaint.setNumViews(content.getNumViews());
					
					//If a complaint is done, update the actual complaint list.
					actualComplaintList.add(content);

					return true;
					
				}else{
					complaint = new Complaint(content.getType(), 1);
					content.setNumComplaints(1);
					complaint.setNumViews(content.getNumViews());
					content.setComplain(complaint);
				
					//If a complaint is done, actualize the actual complaint list.
					actualComplaintList.add(content);
					
					return true;
				}
			}
		return false;
	}

	
	/**
	 * Method to show the data of the user creation
	 */
	public void showDataUserCreation(){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		System.out.println(dateFormat.format(dataUserCreation.getTime()));
	}
	
	
	/**
	 * Getters and Setters
	 * 
	 * 
	 */
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UploadProfile getUploadProfile() {
		return upLoadProfile;
	}

	public void setUploadProfile(UploadProfile upLoadProfile) {
		this.upLoadProfile = upLoadProfile;
	}

	public ViewProfile getViewProfile() {
		return viewProfile;
	}

	public void setViewProfile(ViewProfile viewProfile) {
		this.viewProfile = viewProfile;
	}

	public ComplaintProfile getComplaintProfile() {
		return complaintProfile;
	}

	public void setComplaintProfile(ComplaintProfile complaintProfile) {
		this.complaintProfile = complaintProfile;
	}
	
	public String getPopulation(){
		return population;
	}

	public long getId() {
		return this.id;
	}

	public int getSection() {
		return this.section;
	}
	
	public CommunityAgentReasoner getReasoner(){
		return this.reasoner;
	}
}
