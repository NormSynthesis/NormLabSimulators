package es.csic.iiia.normlab.onlinecomm.agents;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import es.csic.iiia.normlab.onlinecomm.agents.profile.ComplaintProfile;
import es.csic.iiia.normlab.onlinecomm.agents.profile.UploadProfile;
import es.csic.iiia.normlab.onlinecomm.agents.profile.ViewProfile;
import es.csic.iiia.normlab.onlinecomm.content.IContent;
import es.csic.iiia.nsm.agent.language.PredicatesDomains;

/**
 * Moderate agent
 * @author davidsanchezpinsach
 *
 */
public class ModerateAgent extends CommunityAgent {

	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------

	IContent content;

	//---------------------------------------------------------------------------
	// Methods
	//---------------------------------------------------------------------------

	/**
	 * 
	 * @param predDomains
	 * @param space
	 * @param grid
	 * @param row
	 */
	public ModerateAgent(PredicatesDomains predDomains,
			ContinuousSpace<Object> space , Grid<Object> grid, int row) {

		super(predDomains, space, grid, row, null, null, null);	
		upLoadProfile = new UploadProfile(0.4, 0.8, 0, 0, 0, 0);
		viewProfile = new ViewProfile(0.5, 0.1, 0.4, 1);
		complaintProfile = new ComplaintProfile(1, 1, 1, 1);	
	}

	/**
	 * 
	 * @param space
	 * @param grid
	 * @param row
	 * @param uProfile
	 * @param vProfile
	 * @param cProfile
	 */
	public ModerateAgent(PredicatesDomains predDomains, 
			ContinuousSpace<Object> space , Grid<Object> grid, int row, 
			UploadProfile uProfile, ViewProfile vProfile, 
			ComplaintProfile cProfile) {
		
		super(predDomains, space, grid, row, uProfile, vProfile, cProfile);

		upLoadProfile.generateRandomUploadList(grid); 
	}
}