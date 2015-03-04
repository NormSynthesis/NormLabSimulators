package es.csic.iiia.normlab.onlinecomm.nsm;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import es.csic.iiia.normlab.onlinecomm.agents.norms.CommunityFactFactory;
import es.csic.iiia.normlab.onlinecomm.content.IContent;
import es.csic.iiia.normlab.onlinecomm.nsm.agent.CommunityAgentAction;
import es.csic.iiia.normlab.onlinecomm.nsm.agent.CommunityAgentContext;
import es.csic.iiia.normlab.onlinecomm.nsm.perception.CommunityView;
import es.csic.iiia.nsm.agent.EnvironmentAgentAction;
import es.csic.iiia.nsm.agent.EnvironmentAgentContext;
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

//	private double threshold;
//	private long minNumViewsToClassify;

	private CommunityFactFactory factFactory;

	//-------------------------------------------------------------------------
	// Methods
	//-------------------------------------------------------------------------

	/**
	 * 
	 * @param factFactory
	 */
	public CommunityDomainFunctions(CommunityFactFactory factFactory) {
		this.factFactory = factFactory;

		Parameters p = RunEnvironment.getInstance().getParameters();
//		threshold = (Double) p.getValue("conflictThreshold");
//		minNumViewsToClassify = (Long) p.getValue("ContentsNumViewsToClassify");
	}

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
	public EnvironmentAgentContext agentContextFunction(long agentId, View view) {
		CommunityView sv = (CommunityView) view;
		int uploadListSize = sv.getActualUploadList().size();
		CommunityAgentContext agentContext = null;

		for(int i = 0 ; i < uploadListSize ; i++){
			if(sv.getActualUploadList().get(i).getCreatorAgent() == agentId) {
				IContent content = sv.getActualUploadList().get(i);

				agentContext = new CommunityAgentContext(agentId, content.getSection(),
						content.getType(), content.getTypeDescription());

				SetOfPredicatesWithTerms desc = 
						factFactory.generatePredicates(agentContext);

				agentContext.setDescription(desc);
			}
		}
		return agentContext;
	}

	/**
	 * 
	 */
	@Override
	public List<EnvironmentAgentAction> agentActionFunction(long agentId,
			ViewTransition viewTransition) {

		List<EnvironmentAgentAction> actions = new ArrayList<EnvironmentAgentAction>();
//		actions.add(CommunityAgentAction.Upload);
		
		CommunityView view = (CommunityView)viewTransition.getView(0);
		for(IContent content : view.getActualUploadList()) {
			if(content.getCreatorAgent() == agentId) {
				actions.add(CommunityAgentAction.Upload);
			}
		}

		return actions;
	}

	/**
	 * 
	 */
	@Override
	public List<Conflict> getConflicts(Goal goal, ViewTransition viewTransition) {
		CommunityView sv = (CommunityView) viewTransition.getView(0);
		List<Conflict> conflicts = new ArrayList<Conflict>();

		if(!(goal instanceof GComplaints)) { 
			return conflicts;
		}	

		List<IContent> contents = new ArrayList<IContent>();
		for(IContent content : sv.getActualComplaintList()) {
			if(!contents.contains(content)) {
				contents.add(content);
			}
		}	

		for(IContent content : contents){
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
		return conflicts;
	}

	/**
	 * 
	 */
	public List<Conflict> getConflicts(Goal goal, ViewTransition viewTransition,
			long agentId) {

		List<Conflict> allConflicts = this.getConflicts(goal, viewTransition);
		List<Conflict> agentConflicts = new ArrayList<Conflict>();

		for(Conflict conflict : allConflicts) {
			if(conflict.getConflictingAgents().contains(agentId)) {
				agentConflicts.add(conflict);
			}
		}
		return agentConflicts;
	}

	/**
	 * 
	 */
	@Override
	public boolean hasConflict(View view, long agentId, Goal goal) {
		CommunityView sv = (CommunityView) view;

		if(!(goal instanceof GComplaints)) { 
			return false;
		}

		for(IContent content : sv.getActualComplaintList()){
			long contentCreatorId = content.getCreatorAgent();

			if(contentCreatorId == agentId) {
				return true;
			}
		}
		return false;
	}

	//	/**
	//	 * 
	//	 */
	//	@Override
	//	public List<Conflict> getConflicts(Goal goal, ViewTransition viewTransition) {
	//		
	//		CommunityView sv = (CommunityView) viewTransition.getView(0);
	//		List<Conflict> conflicts = new ArrayList<Conflict>();
	//
	//		int size;
	//		int numComplaints;
	//		int numViews;
	//		double ratio;
	//		
	//		if(!(goal instanceof GComplaints)) { 
	//			return conflicts;
	//		}	
	//		
	//		List<IContent> contents = new ArrayList<IContent>();
	//		for(IContent content : sv.getActualViewList()) {
	//			if(!contents.contains(content)) {
	//				contents.add(content);
	//			}
	//		}
	////		for(IContent content : sv.getActualComplaintList()) {
	////			if(!contents.contains(content)) {
	////				contents.add(content);
	////			}
	////		}	
	//	
	//		size = contents.size();
	//		for(int i = 0 ; i < size ; i++){
	//			IContent content = contents.get(i);
	//			
	//			numComplaints = content.getNumComplaints();
	//			numViews = content.getNumViews();
	//			ratio = (double)numComplaints / (double)numViews;
	//			
	//			/* If we put the a minimum of view to create a norm we would have 
	//			 * better rules, due to there will be more people voting them */
	//			if(ratio >= threshold && numViews >= minNumViewsToClassify) {
	//				List<IContent> fakeUploads = new ArrayList<IContent>();
	//				fakeUploads.add(content);
	//
	//				View fakeView = new CommunityView(fakeUploads, null, null);
	//				ViewTransition fakeViewTrans = new ViewTransition(
	//						viewTransition.getSensor());
	//				fakeViewTrans.setView(-1, fakeView);
	//				fakeViewTrans.setView(0, fakeView);
	//
	//				List<Long> conflictingAgents = new ArrayList<Long>();
	//				conflictingAgents.add((long)content.getCreatorAgent());
	//
	//				Conflict conflict = new Conflict(viewTransition.getSensor(), 
	//						fakeView, fakeViewTrans, conflictingAgents);
	//				conflicts.add(conflict);
	//			}
	//		}
	//		return conflicts;
	//	}
	//
	//	/**
	//	 * 
	//	 */
	//	@Override
	//	public boolean hasConflict(View view, long agentId, Goal goal) {
	//		int size;
	//		int numComplaints;
	//		int numViews;
	//		double ratio;
	//
	//		CommunityView sv = (CommunityView) view;
	//
	//		List<IContent> contents = new ArrayList<IContent>();
	//		for(IContent content : sv.getActualViewList()) {
	//			if(!contents.contains(content)) {
	//				contents.add(content);
	//			}
	//		}
	////		for(IContent content : sv.getActualComplaintList()) {
	////			if(!contents.contains(content)) {
	////				contents.add(content);
	////			}
	////		}
	//		
	//		size = contents.size();
	//		if(!(goal instanceof GComplaints)) { 
	//			return false;
	//		}
	//		
	//		for(int i = 0 ; i < size ; i++){
	//			IContent content = contents.get(i);
	//			long contentCreatorId = content.getCreatorAgent();
	//			
	//			if(contentCreatorId == agentId) {
	//				numComplaints = content.getNumComplaints();
	//				numViews = content.getNumViews();
	//				ratio = (double)numComplaints / (double)numViews;
	//
	//				if(ratio >= threshold && numViews >= minNumViewsToClassify) {
	//					return true;
	//				}
	//			}
	//		}
	//		return false;
	//	}

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
