package es.csic.iiia.normlab.onlinecomm.content.complaint;

import es.csic.iiia.normlab.onlinecomm.content.IComplaint;

/**
 * Complaint profile
 * 
 * @author davidsanchezpinsach
 * 
 * Modified by Iosu Mendizabal
 */
public class Complaint implements IComplaint {
	private int type = 0; // Type of the complaint
	private int numComplaints = 0;
	private int numViews = 0;

	public Complaint(int type, int numComplaints) {
		this.type = type;
		this.numComplaints = numComplaints;
	}

	public int getNumComplaints() {
		return numComplaints;
	}

	public int getType() {
		return type;
	}

	public void setNumComplaints(int numComplaints) {
		this.numComplaints = numComplaints;
	}

	public int getNumViews() {
		return numViews;
	}

	public void setNumViews(int numViews) {
		this.numViews = numViews;
	}
}
