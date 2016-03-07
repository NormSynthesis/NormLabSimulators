package es.csic.iiia.normlab.onlinecomm.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import es.csic.iiia.normlab.onlinecomm.content.IContent;

/**
 * Context data of the context
 * 
 * 
 * @author davidsanchezpinsach
 *
 */
public class ContextData {
	
	//Maximum of contents visible in the simulation screen.
	private int maxCommentsPhotoVideo = 11;
	private int maxCommentsForum =  11;
	private int maxCommentsTheReporter = 11;	

	//x point where the sections start in the simulation screen.
	private int sectionReporter = 6;
	private int sectionForum = 18;
	private int sectionMultimedia = 30;
	
	//Number of total comments will each agent have.
	private int totalComments;
	
	//Number of total columns that it would be in the simulation screen.
	private int numColumns = 36;//maxCommentsPhotoVideo+maxCommentsForum+maxCommentsTheReporter+3;
	
	//Get the maximum agents variable from initial parameter.
	private int maxAgents; 
	
	private int numAgents = 0;	
	private int numRows;
	
	//ArrayLists where the simulation put the contents 
	private List<IContent> listSectionTheReporter;
	private List<IContent> listSectionForum;
	private List<IContent> listSectionPhotoVideo;
	
	private List<IContent> allContents;
	
	//ArrayLists where the simulation put the contents 
	private List<IContent> mostViewListTheReporter;
	private List<IContent> mostViewListForum;
	private List<IContent> mostViewListPhotoVideo;
	
	private List<IContent> actualUploadList;
	private List<IContent> actualViewList;
	private List<IContent> actualComplaintList;
	
	//Type of view profile
	private int contentByOrder = 0;
	private int contentByMostViews = 1;
	private int contentByRandom = 2;
	
	//Random of the simulation
	private Random random;
	
	//Graphic functions
	private XYSeriesCollection collection;
	private XYSeries serie;
	private XYSeries serie2;
	private XYSeries serie3;
	
	private int numComplaints = 0;
	
	private int numNonRegulatedComplaints = 0;
	
	private int numCurrentNonRegulatedComplaints = 0;
	
	private int numActualComplaints = 0;
	
	private int numSocialNetworkContents = 0;
	
	private int idealNormativeSystemCardinality;

	
	/**
	 * 
	 * Constructor of the Context Data, where all the arrayLists are created
	 * 
	 * @param random
	 * 			Random variable to use in all the simulation.
	 */
	public ContextData(int maxAgents, long contentsQueueSize){
		
//		listSectionTheReporter = Collections.synchronizedList(new ArrayList<IContent>());
//		listSectionForum = Collections.synchronizedList(new ArrayList<IContent>());
//		listSectionPhotoVideo = Collections.synchronizedList(new ArrayList<IContent>());
//		mostViewListTheReporter = Collections.synchronizedList(new ArrayList<IContent>());
//		mostViewListForum = Collections.synchronizedList(new ArrayList<IContent>());
//		mostViewListPhotoVideo = Collections.synchronizedList(new ArrayList<IContent>());
//		actualUploadList = Collections.synchronizedList(new ArrayList<IContent>());
//		actualViewList = Collections.synchronizedList(new ArrayList<IContent>());
//		actualComplaintList = Collections.synchronizedList(new ArrayList<IContent>());
//		allContents = Collections.synchronizedList(new ArrayList<IContent>());
		
		listSectionTheReporter = Collections.synchronizedList(new CircularFifoQueue<IContent>(contentsQueueSize / 3));
		listSectionForum = Collections.synchronizedList(new CircularFifoQueue<IContent>(contentsQueueSize / 3));
		listSectionPhotoVideo = Collections.synchronizedList(new CircularFifoQueue<IContent>(contentsQueueSize / 3));
		mostViewListTheReporter = Collections.synchronizedList(new CircularFifoQueue<IContent>(contentsQueueSize / 3));
		mostViewListForum = Collections.synchronizedList(new CircularFifoQueue<IContent>(contentsQueueSize / 3));
		mostViewListPhotoVideo = Collections.synchronizedList(new CircularFifoQueue<IContent>(contentsQueueSize / 3));
		actualUploadList = Collections.synchronizedList(new CircularFifoQueue<IContent>(contentsQueueSize / 3));
		actualViewList = Collections.synchronizedList(new CircularFifoQueue<IContent>(contentsQueueSize / 3));
		actualComplaintList = Collections.synchronizedList(new CircularFifoQueue<IContent>(contentsQueueSize / 3));
		allContents = Collections.synchronizedList(new CircularFifoQueue<IContent>(contentsQueueSize));
		
		collection = new XYSeriesCollection();
		serie = new XYSeries("Complaints No regulados", true, true);
		serie2 = new XYSeries("Complaints regulados", true, true);
		serie3 = new XYSeries("Actual Complaints", true, true);
		
		this.maxAgents = maxAgents;
		this.numRows = (maxAgents * 2) + 1; // two rows for each agent and one more fore the sections		
	}	
	
	//Getters and setters
	public int getSectionReporter() {
		return sectionReporter;
	}
	public void setSectionReporter(int sectionReporter) {
		this.sectionReporter = sectionReporter;
	}
	public int getSectionForum() {
		return sectionForum;
	}
	public void setSectionForum(int sectionForum) {
		this.sectionForum = sectionForum;
	}
	public int getSectionMultimedia() {
		return sectionMultimedia;
	}
	public void setSectionMultimedia(int sectionMultimedia) {
		this.sectionMultimedia = sectionMultimedia;
	}
	public int getMaxCommentsPhotoVideo() {
		return maxCommentsPhotoVideo;
	}
	public void setMaxCommentsPhotoVideo(int maxCommentsPhotoVideo) {
		this.maxCommentsPhotoVideo = maxCommentsPhotoVideo;
	}
	public int getMaxCommentsForum() {
		return maxCommentsForum;
	}
	public void setMaxCommentsForum(int maxCommentsForum) {
		this.maxCommentsForum = maxCommentsForum;
	}
	public int getMaxCommentsTheReporter() {
		return maxCommentsTheReporter;
	}
	public void setMaxCommentsTheReporter(int maxCommentsTheReporter) {
		this.maxCommentsTheReporter = maxCommentsTheReporter;
	}
	public int getTotalComments() {
		return totalComments;
	}
	public void setTotalComments(int totalComments) {
		this.totalComments = totalComments;
	}
	public int getNumColumns() {
		return numColumns;
	}
	public void setNumColumns(int numColumns) {
		this.numColumns = numColumns;
	}
	public int getMaxAgents() {
		return maxAgents;
	}
	public void setMaxAgents(int maxAgents) {
		this.maxAgents = maxAgents;
	}
	public int getNumAgents() {
		return numAgents;
	}
	public void setNumAgents(int numAgents) {
		this.numAgents = numAgents;
	}
	public int getNumRows() {
		return numRows;
	}
	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}
	public int getContentByRandom() {
		return contentByRandom;
	}
	public void setContentByRandom(int contentByRandom) {
		this.contentByRandom = contentByRandom;
	}
	
	
	//Methods of the list of categories
	public void addContentListSectionTheReporter(IContent content){
//		listSectionTheReporter.add(0,content);
		listSectionTheReporter.add(content);
		this.allContents.add(content);
	}
	public IContent getContentListSectionTheReporter(int index){
		return listSectionTheReporter.get(index);
	}
	public void removeContentListSectionTheReporter(int index){
		listSectionTheReporter.remove(index);
	}
	public void removeContentListSectionTheReporter(IContent content){
		listSectionTheReporter.remove(content);
	}
	public void addContentListSectionForum(IContent content){
//		listSectionForum.add(0,content);
		listSectionForum.add(content);
		this.allContents.add(content);
	}
	public IContent getContentListSectionForum(int index){
		return listSectionForum.get(index);
	}
	public void removeContentListSectionForum(int index){
		listSectionForum.remove(index);
	}
	public void removeContentListSectionForum(IContent content){
		listSectionForum.remove(content);
	}
	public void addContentListSectionPhotoVideo(IContent content){
//		listSectionPhotoVideo.add(0,content);
		listSectionPhotoVideo.add(content);
		this.allContents.add(content);
	}
	public IContent getContentListSectionPhotoVideo(int index){
		return listSectionPhotoVideo.get(index);
	}
	public void removeContentListSectionPhotoVideo(int index){
		listSectionPhotoVideo.remove(index);
	}
	public void removeContentListSectionPhotoVideo(IContent content){
		listSectionPhotoVideo.remove(content);
	}
	
	
	//Methods of mostViewContents
	public void addContentMostViewTheReporter(IContent content){
		mostViewListTheReporter.add(content);
	}
	public IContent getContentMostViewTheReporter(int index){
		return mostViewListTheReporter.get(index);
	}
	public void removeContentMostViewTheReporter(int index){
		mostViewListTheReporter.remove(index);
	}
	public void removeContentMostViewTheReporter(IContent content){
		mostViewListTheReporter.remove(content);
	}
	public void addContentMostViewForum(IContent content){
		mostViewListForum.add(content);	
	}
	public IContent getContentMostViewForum(int index){
		return mostViewListForum.get(index);
	}
	public void removeContentMostViewForum(int index){
		mostViewListForum.remove(index);
	}
	public void removeContentMostViewForum(IContent content){
		mostViewListForum.remove(content);
	}
	public void addContentMostViewPhotoVideo(IContent content){
		mostViewListPhotoVideo.add(content);	
	}
	public IContent getContentMostViewPhotoVideo(int index){
		return mostViewListPhotoVideo.get(index);
	}
	public void removeContentMostViewPhotoVideo(int index){
		mostViewListPhotoVideo.remove(index);
	}
	public void removeContentMostViewPhotoVideo(IContent content){
		mostViewListPhotoVideo.remove(content);
	}
	
	
	//Getters and Setters of the different list
	public List<IContent> getListSectionTheReporter() {
		return listSectionTheReporter;
	}
	public void setListSectionTheReporter(List<IContent> listSectionTheReporter) {
		this.listSectionTheReporter = listSectionTheReporter;
	}
	public List<IContent> getListSectionForum() {
		return listSectionForum;
	}
	public void setListSectionForum(List<IContent> listSectionForum) {
		this.listSectionForum = listSectionForum;
	}
	public List<IContent> getListSectionPhotoVideo() {
		return listSectionPhotoVideo;
	}
	public void setListSectionPhotoVideo(List<IContent> listSectionPhotoVideo) {
		this.listSectionPhotoVideo = listSectionPhotoVideo;
	}
	public List<IContent> getMostViewListTheReporter() {
		return mostViewListTheReporter;
	}
	public void setMostViewListTheReporter(List<IContent> mostViewListTheReporter) {
		this.mostViewListTheReporter = mostViewListTheReporter;
	}
	public List<IContent> getMostViewListForum() {
		return mostViewListForum;
	}
	public void setMostViewListForum(List<IContent> mostViewListForum) {
		this.mostViewListForum = mostViewListForum;
	}
	public List<IContent> getMostViewListPhotoVideo() {
		return mostViewListPhotoVideo;
	}
	public void setMostViewListPhotoVideo(List<IContent> mostViewListPhotoVideo) {
		this.mostViewListPhotoVideo = mostViewListPhotoVideo;
	}
	public int getContentByOrder() {
		return contentByOrder;
	}
	public void setContentByOrder(int contentByOrder) {
		this.contentByOrder = contentByOrder;
	}
	public int getContentByMostViews() {
		return contentByMostViews;
	}
	public void setContentByMostViews(int contentByMostViews) {
		this.contentByMostViews = contentByMostViews;
	}
	public List<IContent> getActualUploadList() {
		return actualUploadList;
	}
	public List<IContent> getActualViewList() {
		return actualViewList;
	}
	public List<IContent> getActualComplaintList() {
		return actualComplaintList;
	}
	
	//Method to get random integers
	public int nextIntRandom(int x){
		return random.nextInt(x);
	}

	//Calculate new row for new Agent
	public int getNewRow() {
		int newRow = ((getNumRows() -1) - (2 * getNumAgents())) - 1; //numRows - 1 because the zero counts 
																	 // Every agent occupy two spaces (* 2)
																	 // everything -1 because of the section row
		return newRow;
	}
	
	/**
	 * Method to update the order of a list according to most visited contents.
	 * 
	 * @param list
	 * @param contentPosition
	 */
	public void updateList(List<IContent> list, int contentPosition){
		IContent content = list.get(contentPosition);
		
		content.setNumViews(content.getNumViews() + 1);
		list.remove(content);
		int newPosition = getPosition(content, list);
		list.add(newPosition, content);
		showList(list);
	}
	
	/**
	 * Method to get the new position of a content according to the number of views from a list.
	 * 
	 * @param content
	 * @param list
	 * @return new position of the content 
	 */
	public int getPosition(IContent content, List<IContent> list){
		int contentNum = 0;
		int position = 0;
		
		if(list.size() != 0){
			contentNum = list.size() - 1;
			while( (contentNum >= 0 ) && (list.get(contentNum).getNumViews() <= content.getNumViews())){
				position = contentNum; 
				contentNum--;
			}
		}
		return position;
	}
	
	public void showList(List<IContent> list){
		/*System.out.println("--Mostrando lista ordenada--");
		for(IContent content : list){
			System.out.println("Views: "+content.getNumViews());
		}
		System.out.println("--Fin lista ordenada--");*/
	}
	
	// Methods to create the graphic
	
	public XYSeriesCollection getCollection() {
		return collection;
	}
	public void addSeriesToCollection(XYSeries series){
		collection.addSeries(series);
	}
	public XYSeries getSeries(){
		return serie;
	}
	public void setSerie(int tick, int complaint){
		serie.add(tick, complaint);
	}
	
	public XYSeries getSerie2() {
		return serie2;
	}
	
	public XYSeries getSerie3(){
		return serie3;
	}

	public void setSerie2(int tick, int complaint) {
		serie2.add(tick, complaint);
	}
	
	public void setSerie3(int tick, float avgComplaint) {
		serie3.add(tick, avgComplaint);
	}
	
	
	public void setNumComplaint(int num){
		numComplaints = num;
	}
	public int getNumComplaint(){
		return numComplaints;
	}
	
	public void setNumNonRegulatedComplaints(int num){
		this.numNonRegulatedComplaints = num;
	}
	
	public int getNumNonRegulatedComplaints() {
		return this.numNonRegulatedComplaints;
	}
	
	/**
	 * 
	 */
	public void reset()
	{
		this.actualComplaintList.clear();
		this.actualUploadList.clear();
		this.actualViewList.clear();
	}

	public void setActualNumComplaints(int size) {
		this.numActualComplaints = size;		
	}

	public int getActualNumComplaints() {
		return numActualComplaints;
	}

	public void incNumCurrentNonRegulatedComplaints() {
		this.numCurrentNonRegulatedComplaints++;
	}

	public int getNumCurrentNonRegulatedComplaints() {
		return this.numCurrentNonRegulatedComplaints;
	}
	
	public void resetNumCurrentNonRegulatedComplaints() {
		this.numCurrentNonRegulatedComplaints = 0;
	}

	public int getNumSocialNetworkContents() {
		return numSocialNetworkContents;
	}

	public void setNumSocialNetworkContents(int numSocialNetworkContents) {
		this.numSocialNetworkContents = numSocialNetworkContents;
	}
	
	public List<IContent> getAllContents() {
		return this.allContents;
	}

	public int getIdealNormativeSystemCardinality() {
		return this.idealNormativeSystemCardinality;
	}
	
	public void setIdealNormativeSystemCardinality(
			int idealNormativeSystemCardinality) {
		this.idealNormativeSystemCardinality = idealNormativeSystemCardinality;
		
	}

	public Random getRandom() {
		return random;
	}

	public void setRandom(Random random) {
		this.random = random;
	}

	public IContent getContentWithId(int contentId) {
		for(IContent content: this.allContents) {
			if(content.getId() == contentId) {
				return content;
			}
		}
		return null;
	}
}
