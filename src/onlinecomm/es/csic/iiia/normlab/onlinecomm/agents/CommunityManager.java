package es.csic.iiia.normlab.onlinecomm.agents;

import es.csic.iiia.normlab.onlinecomm.context.ContextData;

/**
 * 
 * @author "Javier Morales (jmorales@iiia.csic.es)"
 *
 */
public class CommunityManager {

	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------
	
	private ContextData contextData;

	//---------------------------------------------------------------------------
	// Methods
	//---------------------------------------------------------------------------
	
	/**
	 * 
	 */
	public CommunityManager(ContextData contextData){
		this.contextData = contextData;
	}
	
	/**
	 * 
	 */
	public void step()
	{
		this.contextData.resetNumCurrentNonRegulatedComplaints();
		this.contextData.getActualUploadList().clear();
		this.contextData.getActualComplaintList().clear();
		this.contextData.getActualViewList().clear();
	}
}
