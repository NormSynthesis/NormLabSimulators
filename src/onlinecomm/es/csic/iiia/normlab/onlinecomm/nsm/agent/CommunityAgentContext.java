package es.csic.iiia.normlab.onlinecomm.nsm.agent;

import java.util.ArrayList;
import java.util.List;

import es.csic.iiia.normlab.onlinecomm.agents.CommunityAgent;
import es.csic.iiia.normlab.onlinecomm.content.IContent;
import es.csic.iiia.normlab.onlinecomm.nsm.CommunityNormSynthesisAgent;
import es.csic.iiia.nsm.agent.AgentContext;
import es.csic.iiia.nsm.agent.language.SetOfPredicatesWithTerms;

/**
 * Class that saves all the agents context (id, section, content that are
 * uploading...) in the social network.
 * 
 * @author Iosu Mendizabal
 *
 */
public class CommunityAgentContext implements AgentContext {

	private long agentId;
	private int section;
	private int contentType;
	
	private CommunityAgent agent;
	private String contentTypeDesc;
	
	private List<Long> perceivedAgentsIds;
	private SetOfPredicatesWithTerms description;
	
	/**
	 * First Constructor of the social agent context.
	 * 
	 * @param agentId
	 * 			Id of the agent.
	 * @param section
	 * 			Number of the section.
	 * @param contentType
	 * 			Type of the content (Number).
	 * @param contentTypeDesc
	 * 			Description of the content type (Text).
	 */
	public CommunityAgentContext(long agentId, int section, int contentType,
			String contentTypeDesc) {
		
		this.agentId = agentId;
		this.section = section;
		this.contentType = contentType;
		this.contentTypeDesc = contentTypeDesc;
		
		this.perceivedAgentsIds = new ArrayList<Long>();
		this.description = CommunityNormSynthesisAgent.
				getFactFactory().generatePredicates(this);
	}
	
	/**
	 * Second Constructor of the social agent context.
	 * 
	 * @param agent
	 * 			Agent that is in the context.
	 */
	public CommunityAgentContext(CommunityAgent agent){
		this.agent = agent;
		this.agentId = agent.getId();
		this.section = 0;
		
		this.description = CommunityNormSynthesisAgent.
				getFactFactory().generatePredicates(this);
	}
	
	/**
	 * Method to refresh social context with a new content that is being uploaded.
	 * 
	 * @param content
	 * 			content is being uploaded.
	 */
	public void update(IContent content){
		this.section = agent.getSection();
		this.contentType = content.getType();
		this.contentTypeDesc = content.getTypeDescription();
		
		this.description = CommunityNormSynthesisAgent.
				getFactFactory().generatePredicates(this);
	}
	
	/**
	 * Method to compare a given agent context with this social agent context
	 * 
	 * @return true if it is equal, false if it isn't.
	 */
	public boolean equals(AgentContext otherContext){
		CommunityAgentContext context = (CommunityAgentContext)otherContext;
	
		return context.getId() == this.agentId && 
				context.getSection() == this.section && 
				context.getContentType() == this.contentType;
	}

	/**
	 * 
	 */
	@Override
	public SetOfPredicatesWithTerms getDescription() {
		return this.description;
	}

	/**
	 * 
	 */
	@Override
	public List<Long> getPerceivedAgentsIds() {
		return this.perceivedAgentsIds;
	}
	
	/**
	 * Getter to know the id of the actual agent.
	 * 
	 * @return The agent id.
	 */
	public long getId() {
		return this.agentId;
	}
	
	/**
	 * Getter to know the actual section.
	 * 
	 * @return The section.
	 */
	public int getSection() {
		return this.section;
	}
	
	/**
	 * Getter to know the content type.
	 * 
	 * @return The content type
	 */
	public int getContentType() {
		return this.contentType;
	}
	
	/**
	 * Getter to know the content type description.
	 * 
	 * @return Content type description.
	 */
	public String getContentTypeDesc() {
		return this.contentTypeDesc;
	}
	
	/**
	 * Method to return into a text format all the social agent context parameters.
	 * Agent id, agent section, content type and content type description.
	 * 
	 * @return Text with the information.
	 */
	public String toString() {
		return "[" +
				(this.agentId != -1? "Usr:" + this.agentId : "" ) +
				(this.agentId != -1 && this.section != -1 ? ", " : "") +
				(this.section != -1? "Sec:" + this.section : "" ) + 
				((this.agentId != -1 || this.section != -1) && this.contentType != -1 ? ", " : "") +
				(this.contentType != -1? "CType:" + this.contentTypeDesc : "" )
				+ "]"; 
	}
}
