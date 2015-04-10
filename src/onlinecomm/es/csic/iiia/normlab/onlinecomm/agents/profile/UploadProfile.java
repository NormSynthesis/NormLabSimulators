package es.csic.iiia.normlab.onlinecomm.agents.profile;

import java.util.ArrayList;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.grid.Grid;
import es.csic.iiia.normlab.onlinecomm.bbdd.BbddManager;
import es.csic.iiia.normlab.onlinecomm.content.IContent;
import es.csic.iiia.normlab.onlinecomm.content.comment.CorrectComment;
import es.csic.iiia.normlab.onlinecomm.content.comment.InsultComment;
import es.csic.iiia.normlab.onlinecomm.content.comment.PornComment;
import es.csic.iiia.normlab.onlinecomm.content.comment.SpamComment;
import es.csic.iiia.normlab.onlinecomm.content.comment.ViolentComment;
import es.csic.iiia.normlab.onlinecomm.context.ContextData;

/**
 * Upload profile of the agent
 * 
 * @author davidsanchezpinsach
 * 
 */
public class UploadProfile {

	// Type of different upload content percentage
	private double correct = 0;
	private double insult = 0;
	private double spam = 0;
	private double violent = 0;
	private double porn = 0;

	private double uploadProbability = 0; // probability to upload contents

	private int maxCommentsPhotoVideo;
	private int maxCommentsForum;
	private int maxCommentsTheReporter;

	public final int sectionTheReporter = 1;
	public final int sectionForum = 2;
	public final int sectionPhotoVideo = 3;

	// Number of specific section
	private int numCommentsPhotoVideo = 0;
	private int numCommentsForum = 0;
	private int numCommentsTheReporter = 0;

	// Type of comments
	private final int idCorrect = 1;
	private final int idSpam = 2;
	private final int idPorn = 3;
	private final int idViolent = 4;
	private final int idInsult = 5;

	// List of upload profile
	protected ArrayList<IContent> listUploadProfile;

	// Total of comments of the uploadProfile
	private int totalComments = 0;

	/**
	 * Constructor of the uploadProfile
	 * 
	 * @param correct
	 *            Correct content frequency
	 * @param insult
	 *            Insult content frequency
	 * @param spam
	 *            Spam content frequency
	 * @param violent
	 *            Ciolent content frequency
	 * @param porn
	 *            Porn content frequency
	 * @param uploadProbability
	 *            UpLoadContent content frequency
	 */
	public UploadProfile(double uploadProb, double correct, double spam, double porn, double violent, double insult) {
		this.uploadProbability = uploadProb;
		this.correct = correct;
		this.spam = spam;
		this.porn = porn;
		this.violent = violent;
		this.insult = insult;

		listUploadProfile = new ArrayList<IContent>();
	}


	/**
	 * This method returns the first content of the upload list and remove it from the list.
	 * 
	 * @return content to upload.
	 */
	public IContent getFirstListUploadProfile() {
		IContent content = null;

		if (listUploadProfile.size() > 0) {
			content = listUploadProfile.remove(0);
		}
		return content;
	}

	/**
	 * 
	 * @param grid
	 */
	public void generateRandomUploadList(Grid<Object> grid) {
		ContextData contextData = (ContextData) grid.getObjectAt(0, 0);
		ArrayList<Integer> randomSectionList = generateRandomSectionList(contextData);
		listUploadProfile = generateRandomUploadList(randomSectionList, contextData);		
	}

	/**
	 * 
	 * @param randomSectionList
	 * @param contextData
	 * @return
	 */
	private ArrayList<IContent> generateRandomUploadList(ArrayList<Integer> randomSectionList, ContextData contextData) {
		int index = 0;

		int numCorrect = (int) (totalComments * correct);
		int numInsult = (int) (totalComments * insult);
		int numSpam = (int) (totalComments * spam);
		int numViolent = (int) (totalComments * violent);
		int numPorn = (int) (totalComments * porn);

		int format = 0;//0 text 
		//1 photo 
		//2 video

		createContent(randomSectionList, numCorrect, format, idCorrect);
		createContent(randomSectionList, numInsult, format, idInsult);
		createContent(randomSectionList, numSpam, format, idSpam);
		createContent(randomSectionList, numViolent, format, idViolent);
		createContent(randomSectionList, numPorn, format, idPorn);

		// Random the elements of the list
		ArrayList<IContent> tempListComment = new ArrayList<IContent>();

		for (int i = 0; i < totalComments; i++) {
			if (listUploadProfile.size() != 0) {
				index = contextData.nextIntRandom(listUploadProfile.size());
				tempListComment.add(listUploadProfile.remove(index));
			}
		}
		listUploadProfile = tempListComment;

		return listUploadProfile;
	}


	private void createContent(ArrayList<Integer> randomSectionList, int contentLength, int format, int contentType) {
		IContent comment = null;

		double tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		
		for (int i = 0; i < contentLength && !randomSectionList.isEmpty(); i++) {
			switch(format){

			case 0:
				switch(contentType){
				case idCorrect:
					comment = new CorrectComment(1, contentType, "file", "url",
							"message", randomSectionList.remove(0), "correct", tick);
					break;
				case idSpam:
					comment = new SpamComment(2, contentType, "file", "url",
							"message", randomSectionList.remove(0), "spam", tick);
					break;
				case idPorn:
					comment = new PornComment(3, contentType, "file", "url",
							"message", randomSectionList.remove(0), "porn", tick);
					break;
				case idViolent:
					comment = new ViolentComment(4, contentType, "file", "url",
							"message", randomSectionList.remove(0), "violent", tick);
					break;		
				case idInsult:
					comment = new InsultComment(6, contentType, "file", "url",
							"message", randomSectionList.remove(0), "insult", tick);
					break;	
				}
				break;
			case 1:
				break;
			case 2:
				break;
			}
			if (comment != null) {
				listUploadProfile.add(comment);
			}
		}

	}


	/**
	 * Method to generate random upload list from BBDD
	 * 
	 * @param bbddManager
	 *            Class to manager BBDD
	 * @param grid
	 *            Grid of the context
	 */
	public void generateRandomUploadList(BbddManager bbddManager, Grid<Object> grid) {
		ContextData contextData = (ContextData) grid.getObjectAt(0, 0);
		ArrayList<Integer> randomSectionList = generateRandomSectionList(contextData);
		listUploadProfile = generateRandomUploadList(randomSectionList, contextData, bbddManager);
	}

	/**
	 * Generate the random list of the sections
	 *  
	 * @param contextData
	 * 			The context data of the execution where all the values are saved.
	 * 
	 * @return ArrayList of integers where each number means a section.
	 * 			1.- Reporter.
	 * 			2.- Forum.
	 *			3.- Multimedia (Photo/ Video).
	 */
	private ArrayList<Integer> generateRandomSectionList(ContextData contextData) {
		int sectionSize;
		int num;
		int index;
		ArrayList<Integer> sectionList = new ArrayList<Integer>();
		ArrayList<Integer> tempListSection = new ArrayList<Integer>();

		sectionSize = totalComments / 3;

		for (int i = 0; i < sectionSize; i++) {
			sectionList.add(sectionPhotoVideo);
		}
		for (int i = 0; i < sectionSize; i++) {
			sectionList.add(sectionForum);
		}
		for (int i = 0; i < sectionSize; i++) {
			sectionList.add(sectionTheReporter);
		}
		num = sectionSize * 3;

		// Random the elements of the list
		for (int i = 0 ; i < num ; i++) {
			index = contextData.nextIntRandom(sectionList.size());
			tempListSection.add(sectionList.remove(index));
		}
		sectionList = tempListSection;

		return sectionList;
	}

	/**
	 * This method generates a random list of contents charged from a data base.
	 * 
	 * @param randomSectionList
	 * 			ArrayList of integers where each number means a section.
	 * @param contextData
	 * 	 		The context data of the execution where all the values are saved.
	 * @param bbddManager
	 * 			Connection to the database functions.
	 * 
	 * @return ArrayList of contents 
	 */
	private ArrayList<IContent> generateRandomUploadList(ArrayList<Integer> randomSectionList, ContextData contextData, BbddManager bbddManager) {
		int index = 0;

		int numCorrect = (int) (totalComments * correct);
		int numInsult = (int) (totalComments * insult);
		int numSpam = (int) (totalComments * spam);
		int numViolent = (int) (totalComments * violent);
		int numPorn = (int) (totalComments * porn);

		int format = 0;//0 text 
		//1 photo 
		//2 video

		createContent(randomSectionList, bbddManager, numCorrect, format, idCorrect);
		createContent(randomSectionList, bbddManager, numInsult, format, idInsult);
		createContent(randomSectionList, bbddManager, numSpam, format, idSpam);
		createContent(randomSectionList, bbddManager, numViolent, format, idViolent);
		createContent(randomSectionList, bbddManager, numPorn, format, idPorn);

		// Random the elements of the list
		ArrayList<IContent> tempListComment = new ArrayList<IContent>();

		for (int i = 0; i < totalComments; i++) {
			if (listUploadProfile.size() != 0) {
				index = contextData.nextIntRandom(listUploadProfile.size());
				tempListComment.add(listUploadProfile.remove(index));
			}
		}
		listUploadProfile = tempListComment;

		return listUploadProfile;
	}

	/**
	 * Method to create content from the data base
	 * 
	 * @param randomSectionList
	 * 			ArrayList of contents.
	 * @param bbddManager
	 * 			Connection to the database functions.
	 * @param contentLength
	 * 			The number of contents of that type the agent have to upload.
	 * @param format
	 * 			The format of the content.
	 * @param contentType
	 * 			Type of the content (Spam, porn...)
	 */
	private void createContent(ArrayList<Integer> randomSectionList, BbddManager bbddManager, int contentLength, int format, int contentType) {
		for (int i = 0; i < contentLength && !randomSectionList.isEmpty(); i++) {
			IContent comment = bbddManager.sendQuery(contentType, format, randomSectionList.remove(0));
			if (comment != null) {
				listUploadProfile.add(comment);
			}
		}
	}

	/**
	 * 
	 * 
	 * Getters and Setters
	 * 
	 * 
	 */
	public double getCorrect() {
		return correct;
	}

	public void setCorrect(double correct) {
		this.correct = correct;
	}

	public double getInsult() {
		return insult;
	}

	public void setInsult(double insult) {
		this.insult = insult;
	}

	public double getSpam() {
		return spam;
	}

	public void setSpam(double spam) {
		this.spam = spam;
	}

	public double getViolent() {
		return violent;
	}

	public void setViolent(double violent) {
		this.violent = violent;
	}

	public double getPorn() {
		return porn;
	}

	public void setPorn(double porn) {
		this.porn = porn;
	}

	public ArrayList<IContent> getListUploadProfile() {
		return listUploadProfile;
	}

	public void setListUploadProfile(ArrayList<IContent> listUploadProfile) {
		this.listUploadProfile = listUploadProfile;
	}

	public int getMaxCommentsPhotoVideo() {
		return maxCommentsPhotoVideo;
	}

	public void setMaxCommentsPhotoVideo(int maxCommentsPhotoVideo) {
		this.maxCommentsPhotoVideo = maxCommentsPhotoVideo;
	}

	public int getMaxCommentsForum() {
		return maxCommentsForum;
	}

	public void setMaxCommentsForum(int maxCommentsForum) {
		this.maxCommentsForum = maxCommentsForum;
	}

	public int getMaxCommentsTheReporter() {
		return maxCommentsTheReporter;
	}

	public void setMaxCommentsTheReporter(int maxCommentsTheReporter) {
		this.maxCommentsTheReporter = maxCommentsTheReporter;
	}

	public int getTotalComments() {
		return totalComments;
	}

	public void setTotalComments(int totalComments) {
		this.totalComments = totalComments;
	}

	public double getUploadProbability() {
		return uploadProbability;
	}

	public void setUploadProbability(double upLoadContent) {
		this.uploadProbability = upLoadContent;
	}

	public int getNumCommentsPhotoVideo() {
		return numCommentsPhotoVideo;
	}

	public void setNumCommentsPhotoVideo(int numCommentsPhotoVideo) {
		this.numCommentsPhotoVideo = numCommentsPhotoVideo;
	}

	public int getNumCommentsForum() {
		return numCommentsForum;
	}

	public void setNumCommentsForum(int numCommentsForum) {
		this.numCommentsForum = numCommentsForum;
	}

	public int getNumCommentsTheReporter() {
		return numCommentsTheReporter;
	}

	public void setNumCommentsTheReporter(int numCommentsTheReporter) {
		this.numCommentsTheReporter = numCommentsTheReporter;
	}



}
