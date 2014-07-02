package es.csic.iiia.normlab.onlinecomm.agents;


import es.csic.iiia.normlab.onlinecomm.agents.profile.ComplaintProfile;
import es.csic.iiia.normlab.onlinecomm.agents.profile.UploadProfile;
import es.csic.iiia.normlab.onlinecomm.agents.profile.ViewProfile;
import es.csic.iiia.normlab.onlinecomm.content.IContent;
import es.csic.iiia.nsm.agent.language.PredicatesDomains;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

/**
 * Rude agent
 * @author davidsanchezpinsach
 *
 */
public class RudeAgent extends CommunityAgent{
	
	IContent content;
	
	//Default RudeAgent
	public RudeAgent(PredicatesDomains predDomains,
			ContinuousSpace<Object> space , Grid<Object> grid, int row) {

		super(predDomains, space, grid, row, null, null, null);	
		upLoadProfile = new UploadProfile(0.6, 0.3, 0, 0, 0, 0.6);
		viewProfile = new ViewProfile(0.3, 0.4, 0.3, 1);
		complaintProfile = new ComplaintProfile(0.2, 0.1, 0.1, 0);
	}
	
	public RudeAgent(PredicatesDomains predDomains, 
			ContinuousSpace<Object> space , Grid<Object> grid, int row, 
			UploadProfile uProfile, ViewProfile vProfile, 
			ComplaintProfile cProfile) {
		
		super(predDomains, space, grid, row, uProfile, vProfile, cProfile);

		upLoadProfile.generateRandomUploadList(grid); 
	}
}
