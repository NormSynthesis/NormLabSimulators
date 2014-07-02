package es.csic.iiia.normlab.onlinecomm.nsm.perception;

import java.util.ArrayList;
import java.util.List;

import es.csic.iiia.normlab.onlinecomm.content.IContent;
import es.csic.iiia.nsm.perception.View;

/**
 * Method that creates the perception of the iron library. It contains what has happened in the last tick of time.
 * 
 * @author Iosu Mendizabal
 *
 */
public class CommunityView implements View {

	private List<IContent> actualUploadList;
	private List<IContent> actualViewList;
	private List<IContent> actualComplaintList;

	/**
	 * Constructor of the Social View
	 * 
	 * @param actualUploads
	 * 		  ArrayList of contents that have been upload in the last tick of time.
	 * 
	 * @param actualViews
	 * 		  ArrayList of contents that have been visited in the last tick of time.
	 * 
	 * @param actualComplaints
	 * 		  ArrayList of contents that have been complaint in the last tick of time.
	 */
	public CommunityView(List<IContent> actualUploads, List<IContent> actualViews, List<IContent> actualComplaints) {
		this.actualUploadList = actualUploads;
		this.actualViewList = actualViews;
		this.actualComplaintList = actualComplaints;		
	}

	/**
	 * This method returns the agentsIds that are doing uploads in the last tick.
	 * 
	 * @return agentIds
	 * 		 	ArrayList containing the id's of the agents that make upload.
	 */
	public List<Long> getAgentIds() {
		ArrayList<Long> agentIds = new ArrayList<Long>();
		int uploadListSize = actualUploadList.size();
		
		for(int i = 0 ; i < uploadListSize ; i++){
			agentIds.add((long) actualUploadList.get(i).getCreatorAgent());
		}
		return agentIds;
	}

	/**
	 * 
	 * Method to get similarity.
	 * 
	 * @param otherView
	 * 			not used.
	 * 
	 * @return 0
	 */
	public float getSimilarity(View otherView) {
		return 0;
	}

	/**
	 * Getter of the actual upload list.
	 * 
	 * @return actualUploadList
	 * 		actual upload list.
	 */
	public List<IContent> getActualUploadList() {
		return actualUploadList;
	}

	/**
	 * Setter of the actual upload list.
	 * 
	 * @param actualUploadList
	 */
	public void setActualUploadList(List<IContent> actualUploadList) {
		this.actualUploadList = actualUploadList;
	}
	
	/**
	 * Getter of the actual view list.
	 * 
	 * @return ArrayList of contents.
	 */
	public List<IContent> getActualViewList() {
		return actualViewList;
	}

	/**
	 * Setter of the actual view list.
	 * 
	 * @param actualViewList
	 * 			ArrayList of the actual view list contents.
	 */
	public void setActualViewList(List<IContent> actualViewList) {
		this.actualViewList = actualViewList;
	}
	
	/**
	 * Getter of the actual complaint list.
	 * 
	 * @return ArrayList of the actual complaint contents.
	 */
	public List<IContent> getActualComplaintList() {
		return actualComplaintList;
	}

	/**
	 * Setter of the actual complaint list.
	 * 
	 * @param actualComplaintList
	 * 			ArrayList of the actual complaint list contents.
	 */
	public void setActualComplaintList(List<IContent> actualComplaintList) {
		this.actualComplaintList = actualComplaintList;
	}

}
