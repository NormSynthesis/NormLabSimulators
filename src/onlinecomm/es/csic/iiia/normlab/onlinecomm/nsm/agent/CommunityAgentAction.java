package es.csic.iiia.normlab.onlinecomm.nsm.agent;

import es.csic.iiia.nsm.agent.EnvironmentAgentAction;

/**
 * 
 * Enumeration of possible action of the agents in the simulation.
 * 
 * @author Iosu Mendizabal
 *
 */
public enum CommunityAgentAction implements EnvironmentAgentAction {
	Upload,
	View, 
	Complain, 
	DoNotUpload,
	Nothing
}
