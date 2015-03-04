package es.csic.iiia.normlab.onlinecomm.agents.norms;

import java.util.ArrayList;
import java.util.List;

import es.csic.iiia.normlab.onlinecomm.content.IContent;
import es.csic.iiia.normlab.onlinecomm.nsm.CommunityNormSynthesisAgent;
import es.csic.iiia.normlab.onlinecomm.nsm.perception.CommunityView;
import es.csic.iiia.nsm.agent.EnvironmentAgentContext;
import es.csic.iiia.nsm.agent.language.PredicatesDomains;
import es.csic.iiia.nsm.agent.language.SetOfPredicatesWithTerms;
import es.csic.iiia.nsm.config.DomainFunctions;
import es.csic.iiia.nsm.norm.Norm;
import es.csic.iiia.nsm.norm.reasoning.NormEngine;

/**
 * 
 * @author "Javier Morales (jmorales@iiia.csic.es)"
 *
 */
public class CommunityNormEngine extends NormEngine {

	//---------------------------------------------------------------------------
	// Attributes 
	//---------------------------------------------------------------------------
	
	private DomainFunctions dmFunctions;
	
	//---------------------------------------------------------------------------
	// Methods 
	//---------------------------------------------------------------------------
	
	/**
	 * 
	 * @param dmFunctions
	 */
	public CommunityNormEngine(DomainFunctions dmFunctions, 
			PredicatesDomains predDomains) {
		
	  super(predDomains);
	  this.dmFunctions = dmFunctions;
  }
	
	/**
	 * 
	 * @return
	 */
	public List<Norm> getNormsApplicableToContent(IContent content) {
		long agentId = (long) content.getCreatorAgent();
		List<IContent> fakeUploads = new ArrayList<IContent>();
		fakeUploads.add(content);

		CommunityView fakeView = new CommunityView(fakeUploads, null, null);
		EnvironmentAgentContext context = this.dmFunctions.agentContextFunction(agentId, fakeView);
		SetOfPredicatesWithTerms predicates = CommunityNormSynthesisAgent.
				getFactFactory().generatePredicates(context);
		
		this.reset();
		this.addFacts(predicates);
		this.reason();
		
		return this.applicableNorms;
	}
}
