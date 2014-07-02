package es.csic.iiia.normlab.onlinecomm.content;

/**
 * Interface complaint
 * 
 * @author davidsanchezpinsach
 * 
 *
 */
public interface IComplaint {
	public void setNumComplaints(int numComplaints);
	public int getNumComplaints();
	public int getType();
	public int getNumViews();
	public void setNumViews(int numViews);
}
