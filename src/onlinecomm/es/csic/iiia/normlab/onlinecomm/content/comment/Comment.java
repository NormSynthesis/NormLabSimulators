package es.csic.iiia.normlab.onlinecomm.content.comment;

import es.csic.iiia.normlab.onlinecomm.content.IComplaint;
import es.csic.iiia.normlab.onlinecomm.content.IContent;
import es.csic.iiia.nsm.norm.Norm;

/**
 * Comment (Content) class.
 * 
 * 
 * @author Iosu Mendizabal
 *
 */
public class Comment implements IContent {

	private int id;
	private int type;
	private int section;
	private int numViews = 0;
	private int numAgent;
	private int numComplaints = 0;
	private String typeDesc;
	private String message;
	private Norm violatedNorm = null;
	private IComplaint complaint;
	private double tick;
	
	/**
	 * Constructor of comments.
	 * 
	 * @param id
	 * 			The identifier of the comment.
	 * @param contentType
	 * 			Type of the content/comment.
	 * @param file
	 * 			Not used.
	 * @param url
	 * 			Not used.
	 * @param message
	 * 			Message the comment/content have.
	 * @param section
	 * 			Section where the comment is in.
	 */
	public Comment(int id, int contentType, String file, String url, String message,
			int section, String contentTypeDesc, double timeStep) {
		this.id = id;
		this.type = contentType;
		this.message = message;
		this.section = section;
		this.typeDesc = contentTypeDesc;
		this.tick = timeStep;
	}
	
	/**
	 * Getter of the description type.
	 * 
	 * @return Description type.
	 */
	public String getTypeDescription()	{
		return this.typeDesc;
	}

	/**
	 * Return the message the content have.
	 * 
	 */
	public String toString(){
		if(message!=null){
			return message;
		}else{
			return "";
		}
	}
	
	/**
	 * 
	 * 
	 * Getters and Setters
	 *
	 *
	 */
	public int getSection() {
		return section;
	}
	
	public void setSection(int section) {
		this.section = section;
	}
	
	public int getNumViews() {
		return numViews;
	}

	public void setNumViews(int numViews) {
		this.numViews = numViews;
	}

	public int getType() {
		return type;
	}

	public void setCreatorAgent(int numAgent) {
		this.numAgent = numAgent;		
	}

	public int getCreatorAgent() {
		return numAgent;
	}

	public int getNumComplaints() {
		return numComplaints;
	}

	public void setNumComplaints(int num) {
		this.numComplaints = num;	
	}

	public IComplaint getComplaint() {
		return complaint;
	}

	public void setComplain(IComplaint complaint) {
		this.complaint = complaint;	
	}

	public int getId() {
		return this.id;
	}

	public Norm getViolatedNorm() {
		return violatedNorm;
	}

	public void setViolatedNorm(Norm violatedNorm) {
		this.violatedNorm = violatedNorm;
	}

	@Override
  public double getTick() {
		return this.tick;
  }

	@Override
  public void setTick(double tick) {
	  this.tick = tick;
  }
}
