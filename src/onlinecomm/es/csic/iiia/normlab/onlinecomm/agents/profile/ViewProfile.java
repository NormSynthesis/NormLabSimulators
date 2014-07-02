package es.csic.iiia.normlab.onlinecomm.agents.profile;

/**
 * View Profile of the agents.
 * 
 * 
 * @author Iosu Mendizabal
 *
 */
public class ViewProfile{
	//Type of different section
	private double forum;
	private double theReporter;
	private double photoVideo;
	
	//View Mode and distribution mode of the view mode
	private int viewMode;

	/**
	 * 
	 * @param forum
	 * @param theReporter
	 * @param photoVideo
	 * @param viewMode
	 */
	public ViewProfile(double forum, double theReporter, double photoVideo, int viewMode){
		this.forum = forum;
		this.theReporter = theReporter;
		this.photoVideo = photoVideo;
		this.viewMode = viewMode;
	}

	/**
	 * 
	 * 
	 * Getters and Setters
	 * 
	 * 
	 */
	public double getForum() {
		return forum;
	}

	public void setForum(double forum) {
		this.forum = forum;
	}

	public double getTheReporter() {
		return theReporter;
	}

	public void setTheReporter(double theReporter) {
		this.theReporter = theReporter;
	}

	public double getPhotoVideo() {
		return photoVideo;
	}

	public void setPhotoVideo(double photoVideo) {
		this.photoVideo = photoVideo;
	}

	public int getViewMode() {
		return viewMode;
	}

	public void setViewMode(int viewMode) {
		this.viewMode = viewMode;
	}
}
