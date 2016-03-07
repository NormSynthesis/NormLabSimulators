package es.csic.iiia.normlab.onlinecomm.agents.norms;

import es.csic.iiia.normlab.onlinecomm.nsm.agent.CommunityAgentContext;
import es.csic.iiia.nsm.agent.AgentContext;
import es.csic.iiia.nsm.agent.language.PredicatesDomains;
import es.csic.iiia.nsm.agent.language.SetOfPredicatesWithTerms;
import es.csic.iiia.nsm.norm.reasoning.JessFactsGenerator;

/**
 * Facts generator tool. Generates facts for the car reasoner and to build the
 * condition (left part) of a norm. It adapts the facts to the format of the car
 * reasoner or the norm condition, in base of the FactType passed by parameter  
 * 
 * @author Javier Morales (jmoralesmat@gmail.com)
 *
 */
public class CommunityFactFactory extends JessFactsGenerator {

	//-------------------------------------------------------------------------	
	// Methods 
	//-------------------------------------------------------------------------
	
	/**
	 * 
	 * @param predicatesDomains
	 */
	public CommunityFactFactory(PredicatesDomains predicatesDomains) {
		super(predicatesDomains);
	}

	
	/**
	 * Defines the type of a fact. A fact for a Reasoner is used to know what is the current
	 * state of the world. A fact for a norm is used to define a situation that fires a norm
	 *   
	 * @author Javier Morales (jmoralesmat@gmail.com)
	 *
	 */
	public enum FactType {
		Reasoner, Norm
	}

	/**
	 * Generates a string containing a set of facts
	 * 
	 * @param factType
	 * @param scope
	 * @return
	 */
	public SetOfPredicatesWithTerms generatePredicates(AgentContext aContext) {
		SetOfPredicatesWithTerms predicatesWithTerms = new SetOfPredicatesWithTerms();
		CommunityAgentContext context = (CommunityAgentContext) aContext;

		if(context.getId() != -1) {
			predicatesWithTerms.add("usr", String.valueOf(context.getId()));
		}
		if(context.getSection() != -1) {
			predicatesWithTerms.add("sec", String.valueOf(context.getSection()));
		}
		if(context.getContentType() != -1) {
			predicatesWithTerms.add("cnt", context.getContentTypeDesc());
		}
		return predicatesWithTerms;
	}	
}
