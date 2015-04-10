package es.csic.iiia.normlab.onlinecomm.content;

import es.csic.iiia.nsm.norm.Norm;

/**
 * Interface content
 * 
 * @author davidsanchezpinsach
 *
 *  Modified by Iosu Mendizabal
 */
public interface IContent {	
	public void setCreatorAgent(int numAgent);
	public int getCreatorAgent();
	public int getNumComplaints();
	public void setNumComplaints(int num);
	public int getSection();
	public int getNumViews();
	public void setNumViews(int numViews);
	public int getType();
	public double getTick();
	public void setTick(double tick);
	public IComplaint getComplaint();
	public void setComplain(IComplaint complaint);
	public int getId();
	public String getTypeDescription();
	public void setViolatedNorm(Norm norm);
	public Norm getViolatedNorm();
	
}
