package es.csic.iiia.normlab.onlinecomm.agents;

import es.csic.iiia.normlab.onlinecomm.agents.profile.ComplaintProfile;
import es.csic.iiia.normlab.onlinecomm.agents.profile.UploadProfile;
import es.csic.iiia.normlab.onlinecomm.agents.profile.ViewProfile;
import es.csic.iiia.normlab.onlinecomm.content.IContent;
import es.csic.iiia.nsm.agent.language.PredicatesDomains;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

/**
 * Violent agent
 * @author davidsanchezpinsach
 *
 */
public class ViolentAgent extends CommunityAgent{
	
	IContent content;
	
	/**
	 * 
	 * @param predDomains
	 * @param space
	 * @param grid
	 * @param row
	 */
	public ViolentAgent(PredicatesDomains predDomains,
			ContinuousSpace<Object> space , Grid<Object> grid, int row) {

		super(predDomains, space, grid, row, null, null, null);	
		
		upLoadProfile = new UploadProfile(0.4, 0, 0, 0, 1, 0);
		viewProfile = new ViewProfile(0.5, 0.1, 0.4, 1);
		complaintProfile = new ComplaintProfile(1, 1, 1, 1);	
	}
	
	/**
	 * 
	 * @param predDomains
	 * @param space
	 * @param grid
	 * @param row
	 * @param uProfile
	 * @param vProfile
	 * @param cProfile
	 */
	public ViolentAgent(PredicatesDomains predDomains, 
			ContinuousSpace<Object> space , Grid<Object> grid, int row, 
			UploadProfile uProfile, ViewProfile vProfile, 
			ComplaintProfile cProfile) {
		
		super(predDomains, space, grid, row, uProfile, vProfile, cProfile);

		upLoadProfile.generateRandomUploadList(grid); 

		//Feed the upload list with database connection
		
		//bbddManager.establishedConnection("Violent");
		//if(bbddManager.getConn()!=null){
		//	upLoadProfile.generateRandomUploadList(bbddManager, grid);
		//	bbddManager.terminatedConnection("Violent");
		//}
	}
}
