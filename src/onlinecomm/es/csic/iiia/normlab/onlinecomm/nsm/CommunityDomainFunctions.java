package es.csic.iiia.normlab.onlinecomm.nsm;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import es.csic.iiia.normlab.onlinecomm.agents.norms.CommunityFactFactory;
import es.csic.iiia.normlab.onlinecomm.content.IContent;
import es.csic.iiia.normlab.onlinecomm.nsm.agent.CommunityAgentAction;
import es.csic.iiia.normlab.onlinecomm.nsm.agent.CommunityAgentContext;
import es.csic.iiia.normlab.onlinecomm.nsm.perception.CommunityView;
import es.csic.iiia.nsm.agent.AgentAction;
import es.csic.iiia.nsm.agent.AgentContext;
import es.csic.iiia.nsm.agent.language.SetOfPredicatesWithTerms;
import es.csic.iiia.nsm.config.DomainFunctions;
import es.csic.iiia.nsm.config.Goal;
import es.csic.iiia.nsm.norm.Norm;
import es.csic.iiia.nsm.norm.generation.Conflict;
import es.csic.iiia.nsm.perception.View;
import es.csic.iiia.nsm.perception.ViewTransition;

public class CommunityDomainFunctions implements DomainFunctions {

	//-------------------------------------------------------------------------
	// Attributes
	//-------------------------------------------------------------------------
	
	final static double treshold = 0.5;
	
	//-------------------------------------------------------------------------
	// Methods
	//-------------------------------------------------------------------------
	
	/**
	 * 
	 */
	@Override
	public boolean isConsistent(SetOfPredicatesWithTerms agentContext) {
		return true;
	}

	/**
	 * 
	 */
	@Override
	public AgentContext agentContextFunction(long agentId, View view) {
		CommunityView sv = (CommunityView) view;
		int uploadListSize = sv.getActualUploadList().size();
		CommunityAgentContext agentContext = null;

		for(int i = 0 ; i < uploadListSize ; i++){
			if(sv.getActualUploadList().get(i).getCreatorAgent() == agentId) {
				IContent content = sv.getActualUploadList().get(i);
				agentContext = new CommunityAgentContext(agentId,
						content.getSection(), content.getType(),
						content.getTypeDescription());
			}
		}
		return agentContext;
	}

	/**
	 * 
	 */
	@Override
	public List<AgentAction> agentActionFunction(long agentId,
			ViewTransition viewTransition) {
		
		List<AgentAction> actions = new ArrayList<AgentAction>();
		actions.add(CommunityAgentAction.Upload);

		return actions;
	}

	/**
	 * 
	 */
	@Override
	public List<Conflict> getConflicts(Goal goal, ViewTransition viewTransition) {
		
		CommunityView sv = (CommunityView) viewTransition.getView(0);
		List<Conflict> conflicts = new ArrayList<Conflict>();

		int listComplaintsSize;
		int numComplaints;
		int numViews;
		double ratio;
		
		if(!(goal instanceof GComplaints)) { 
			return conflicts;
		}	
		
		listComplaintsSize =  sv.getActualComplaintList().size();
		for(int i = 0 ; i < listComplaintsSize ; i++){
			IContent content = sv.getActualComplaintList().get(i);
			
//			if(content.getViolatedNorm() != null)
//				continue;
			
			// TODO: Puede que lo tenga que volver a poner otra vez... lo he quitado
			// porque ahora la NSM tambi��n lo tiene en cuenta esto a la
			// hora de crear normas
//			if(SocialApplicabilityFunction.getApplicableNorms(content).size() > 0)
//				continue;
			
			numComplaints = content.getNumComplaints();
			numViews = content.getNumViews();
			ratio = (double)numComplaints / (double)numViews;
			
			// Conflict detected
			/* If we put the a minimum of view to create a norm we would have 
			 * better rules, due to there will be more people voting them */
			if(ratio >= treshold && numViews >= 5){ // TODO: JAVI... He cambiado esto de 5 a 10
				List<IContent> fakeUploads = new ArrayList<IContent>();
				fakeUploads.add(content);

				View fakeView = new CommunityView(fakeUploads, null, null);
				ViewTransition fakeViewTrans = new ViewTransition(
						viewTransition.getSensor());
				fakeViewTrans.setView(-1, fakeView);
				fakeViewTrans.setView(0, fakeView);

				List<Long> conflictingAgents = new ArrayList<Long>();
				conflictingAgents.add((long)content.getCreatorAgent());

				Conflict conflict = new Conflict(viewTransition.getSensor(), 
						fakeView, fakeViewTrans, conflictingAgents);
				conflicts.add(conflict);
			}
		}
		return conflicts;
	}

	/**
	 * 
	 */
	@Override
	public boolean hasConflict(View view, long agentId, Goal goal) {
		int size;
		int numComplaints;
		int numViews;
		double ratio;

		CommunityView sv = (CommunityView) view;

		size = sv.getActualComplaintList().size();

		if(!(goal instanceof GComplaints)) { 
			return false;
		}
		
		for(int i = 0 ; i < size ; i++){
			numComplaints = sv.getActualComplaintList().get(i).
					getComplaint().getNumComplaints();
			numViews = sv.getActualComplaintList().get(i).getNumViews();

			ratio = (double)numComplaints / (double)numViews;

			if(ratio >= treshold && numViews >= 5) {
				IContent content = sv.getActualComplaintList().get(i);
				long contentCreatorId = content.getCreatorAgent();

				if(contentCreatorId == agentId)
					return true;
			}
		}
		return false;

	}

	//---------------------------------------------------------------------------
	// Private methods
	//---------------------------------------------------------------------------

	/**
	 * 
	 */
	@Override
  public JPanel getNormDescriptionPanel(Norm norm) {
	  JPanel normDescPanel = new JPanel();
	  
	  return normDescPanel;
  }
}
