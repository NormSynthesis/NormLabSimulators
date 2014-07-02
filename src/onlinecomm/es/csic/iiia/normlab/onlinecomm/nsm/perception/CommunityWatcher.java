package es.csic.iiia.normlab.onlinecomm.nsm.perception;

import java.util.ArrayList;
import java.util.List;

import es.csic.iiia.normlab.onlinecomm.content.IContent;
import es.csic.iiia.normlab.onlinecomm.context.ContextData;
import es.csic.iiia.nsm.perception.Sensor;
import es.csic.iiia.nsm.perception.ViewTransition;

/**
 * 
 * Class of the Social Watcher that implements sensor. 
 * 
 * @author Iosu Mendizabal
 *
 */
public class CommunityWatcher implements Sensor {

	/**
	 * Variables.
	 */
	//private int numTicks = 10;
	private ContextData contextData;
	private ViewTransition perception;
	
	/**
	 * Constructor of the social watcher.
	 * 
	 * @param contextData
	 * 			Context data to get the actual lists.
	 */
	public CommunityWatcher(ContextData contextData){
		this.perception = new ViewTransition(this);
		this.contextData = contextData;
	}
	
	/**
	 * Method perceive it is made in every tick by the iron agent, because of the scheduler. 
	 * It perceive the actual uploads, views and complaints of the agents and create a view with them.
	 */
	public void perceive(){
		// Crear la view
		List<IContent> actualUploads = new ArrayList<IContent>();
		List<IContent> actualViews = new ArrayList<IContent>();
		List<IContent> actualComplaints = new ArrayList<IContent>();
		
		List<IContent> actualUploadList = contextData.getActualUploadList();
		List<IContent> actualViewList = contextData.getActualViewList();
		List<IContent> actualComplaintList = contextData.getActualComplaintList();
		
		for(IContent c : actualUploadList){
			actualUploads.add(c);
		}
		for(IContent c : actualViewList){
			actualViews.add(c);
		}
		for(IContent c : actualComplaintList){
			actualComplaints.add(c);
		}
		
		CommunityView view = new CommunityView(actualUploads, actualViews, actualComplaints);

		// Meter esa info en la view
		perception.setView(-1, view);
		perception.setView(0, view);
	}
	
	/**
	 * Getter of the id of the watcher, only is one so...
	 * 
	 * @return 1
	 */
	public int getId() {
		return 1;
	}

	/**
	 * Getter of the watcher location, only is one seeing everything so...
	 * 
	 * @return ORACLE.
	 * 
	 */
	public String getLocation() {
		return "I am the ORACLE";
	}

	/**
	 * TODO
	 */
	public void setPerceptionWindow(int ticks) {
		//TODO
	}

	/**
	 * 
	 */
	@Override
	public ViewTransition getPerception() {
		return this.perception;
	}
}
