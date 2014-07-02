package es.csic.iiia.normlab.onlinecomm.agents.profile;

/**
 * Complaint profile of the agent
 * 
 * 
 * @author davidsanchezpinsach
 *
 */
public class ComplaintProfile{
	private double insult = 0;
	private double spam = 0;
	private double violent = 0;
	private double porn = 0;

	/**
	 * Constructor of the complaint profile
	 * 
	 * @param wrongPlacement 
	 * 			WrongPlacement complaint frequency
	 * @param insult 
	 * 			Insult complaint frequency
	 * @param spam 
	 * 			Spam complaint frequency
	 * @param violent 
	 * 			Violent complaint frequency
	 * @param porn 
	 * 			Porn complaint frequency
	 */
	public ComplaintProfile(double spam, double porn, double violent, double insult){
		this.insult = insult;
		this.spam = spam;
		this.violent = violent;
		this.porn = porn;
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
}
