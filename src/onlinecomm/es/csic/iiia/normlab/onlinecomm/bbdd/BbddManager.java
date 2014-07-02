package es.csic.iiia.normlab.onlinecomm.bbdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import es.csic.iiia.normlab.onlinecomm.agents.CommunityAgent;
import es.csic.iiia.normlab.onlinecomm.agents.profile.ComplaintProfile;
import es.csic.iiia.normlab.onlinecomm.agents.profile.UploadProfile;
import es.csic.iiia.normlab.onlinecomm.agents.profile.ViewProfile;
import es.csic.iiia.normlab.onlinecomm.content.IContent;
import es.csic.iiia.normlab.onlinecomm.content.comment.CorrectComment;
import es.csic.iiia.normlab.onlinecomm.content.comment.InsultComment;
import es.csic.iiia.normlab.onlinecomm.content.comment.PornComment;
import es.csic.iiia.normlab.onlinecomm.content.comment.SpamComment;
import es.csic.iiia.normlab.onlinecomm.content.comment.ViolentComment;

/**
 * Class to manager BBDD
 * 
 * 
 * @author davidsanchezpinsach
 * 
 * Modified by Iosu Mendizabal
 *
 */
public class BbddManager {

	Connection connection; 

	//Format of the content
	private final int txt = 0;
	private final int img = 1;
	private final int video = 2;

	//Type of comments
	protected final int idOk = 1;
	protected final int idSpam = 2;
	protected final int idPorn = 3;
	protected final int idViolent = 4;
	protected final int idInsult = 5;

	/**
	 * Method to established connection to BBDD
	 * 
	 * @param agent agent type or name.
	 */
	public void connect(){
		String userName = "root";
		String password = "";
		String direction = "jdbc:mysql://localhost/onlineCommunity";

		try {
			Class.forName ("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(direction, userName, password);
		}
		catch (Exception e)
		{  e.printStackTrace();
			System.err.println ("Cannot connect to database server:" + Calendar.getInstance().getTime());
		}
	}

	/**
	 * Method to finish the connection to BBDD
	 * 
	 * @param agent
	 * 			Agent type or name.
	 */
	public void disconnect(String agent){
		if (connection != null){
			try{
				connection.close();
				System.out.println ("Database connection terminated "+agent+ ": "+Calendar.getInstance().getTime());
			}catch (Exception e) { /* ignore close errors */ }
		}
	}

	/**
	 * 
	 * @return
	 */
	public Connection getConn() {
		return connection;
	}

	/**
	 * 
	 * @param connection
	 */
	public void setConn(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Method to send a query and pick up the data
	 * 
	 * @param idType 
	 * 			Identifier of the type of the content
	 * @param format 
	 * 			Format of the content
	 * @param idSection 
	 * 			Identifier of the section
	 * 
	 * @return content created with the database given data
	 */
	public IContent sendQuery(int idType, int format, int idSection){
		IContent comment = null;
		String stringFormat="";
		switch(format){
		case 0:
			stringFormat = "Text";
			break;
		case 1:
			stringFormat = "Image";
			break;
		case 2:
			stringFormat = "Video";
			break;
		}
		try {
			String SQLStatement = 
					"SELECT c.id AS id , c.category AS category, c.file AS file, c.url AS url, " +
							"c.message AS message, c.section AS section, cc.desc AS category_desc " +
							"FROM content c, content_catecory cc " +
							"WHERE c.content_type = cc.id AND " +
							"c.content_category=" +idType+ " AND " +
							"c.section=" +idSection+ " AND " +
							"c.type='" +stringFormat +"'";

			PreparedStatement statement = connection.prepareStatement(SQLStatement);
			ResultSet result = statement.executeQuery();

			if(result.next())
			{
				int id = result.getInt("id");
				int section = result.getInt("section");
				int category = result.getInt("category");
				String categoryDesc = result.getString("category_desc");
				String type = result.getString("type");
				String url = result.getString("url");
				String message = result.getString("message");

				switch(format){
				case txt:
					switch(idType){
					case idOk:
						comment = new CorrectComment(id, category, type, url, 
								message, section, categoryDesc);
						break;
					case idSpam:
						comment = new SpamComment(id, category, type, url, 
								message, section, categoryDesc);
						break;
					case idPorn:
						comment = new PornComment(id, category, type, url, 
								message, section, categoryDesc);
						break;
					case idViolent:
						comment = new ViolentComment(id, category, type, url, 
								message,section, categoryDesc);
						break;		
					case idInsult:
						comment = new InsultComment(id, category, type, url,
								message, section, categoryDesc);
						break;	
					}
					break;
				case img:
					break;
				case video:
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error with SQL query. The format isn't correct, "
					+ "see the manual of Mysql");
			return null;
		}
		return comment;
	}

	/* ------------------------------------------------ 
	 * Methods to retrieve agents and agent populations
	 * ------------------------------------------------
	 */

	/**
	 * Method to get population data from BBDD
	 * 
	 * @return
	 */
	public ArrayList<String> getPopulations() {
		ArrayList<String> popuName = new ArrayList<String>();

		try{
			PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM agent_population");
			ResultSet result = statement.executeQuery();
			while(result.next()){
				popuName.add(result.getString("description"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error with SQL query. The format isn't correct, "
					+ "see the manual of Mysql");
		}
		return popuName;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<String> getAgentTypes() {
		ArrayList<String> agentTypes = new ArrayList<String>();
		try{
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM agent_type");
			ResultSet result = statement.executeQuery();
			while(result.next()){
				agentTypes.add(result.getString("description"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error with SQL query. The format isn't correct, see the manual of Mysql");
		}

		return agentTypes;
	}

	/**
	 * Method to gets the agents profile
	 * 
	 * @param contextData
	 * 			Context data of simulation
	 * @param uploadProfile 
	 * 			Upload profile of the agent
	 * @param viewProfile 
	 * 			View profile of the agent
	 * @param complaintProfile 
	 * 			Complaint profile of the agent
	 * @param randomUpload 
	 * 			Random
	 * @param population 
	 * 			The name of population
	 * 
	 * @return ArrayList of agents
	 */
	public ArrayList<CommunityAgent> getAgentsProfile(UploadProfile uploadProfile,
			ViewProfile viewProfile, ComplaintProfile complaintProfile,
			Random randomUpload, String population) {

		ArrayList<CommunityAgent> agente = new ArrayList<CommunityAgent>();
		int id_agent_population = 0;
		ArrayList<Integer> id_agent_type = new ArrayList<Integer>();
		ArrayList<Integer> id_agent_profile = new ArrayList<Integer>();
		ArrayList<Integer> quantity = new ArrayList<Integer>();

		try{
			PreparedStatement statement = connection.prepareStatement(
					"SELECT id FROM agent_population WHERE description = '"+population+"'");
			ResultSet result = statement.executeQuery();
			while(result.next()){
				id_agent_population = result.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error with SQL query. The format isn't correct,"
					+ " see the manual of Mysql");
		}

		try{
			PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM population WHERE population = "+id_agent_population);
			ResultSet result = statement.executeQuery();
			while(result.next()){
				id_agent_type.add(result.getInt("agent_type"));
				id_agent_profile.add(result.getInt("agent_profile"));
				quantity.add(result.getInt("quantity"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error with SQL query. The format isn't correct, "
					+ "see the manual of Mysql");
		}

		for(int i = 0 ; i < id_agent_profile.size() ; i++){
			try{
				PreparedStatement statement = connection.prepareStatement(
						"SELECT * FROM agent_profile WHERE id = " + id_agent_profile.get(i));
				ResultSet result = statement.executeQuery();
				while(result.next()){

					//Upload profile settings
					uploadProfile = new UploadProfile(result.getDouble("uploadProbability"),
							result.getDouble("correct"), result.getDouble("spam"),
							result.getDouble("porn"), result.getDouble("violent"), 
							result.getDouble("insult"));

					//View profile settings
					viewProfile = new ViewProfile(result.getDouble("forum"),
							result.getDouble("theReporter"), result.getDouble("photoVideo"),
							result.getInt("viewMode"));
					//Complaint Profile
					complaintProfile = new ComplaintProfile(result.getDouble("spamComplaint"),
							result.getDouble("pornComplaint"), result.getDouble("violentComplaint"),
							result.getDouble("insultComplaint"));

					int random = 0;
					int agentType = 0;

					System.out.println(result.getString("name"));
					if(result.getString("name").equals("moderate")){
						random = 100;
						agentType = 1;
					}
					if(result.getString("name").equals("spammer")){
						random = 200;
						agentType = 2;
					}
					if(result.getString("name").equals("pornographic")){
						random = 300;
						agentType = 3;
					}
					if(result.getString("name").equals("violent")){
						random = 400;
						agentType = 4;
					}
					if(result.getString("name").equals("rude")){
						random = 500;
						agentType = 5;
					}

					randomUpload = new Random(random);
					CommunityAgent moderate = new CommunityAgent(
							agentType, quantity.get(i),
							result.getString("name"),uploadProfile,viewProfile,
							complaintProfile, population);

					agente.add(moderate);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Error with SQL query. The format isn't correct, "
						+ "see the manual of Mysql");
			}
		}
		return agente;
	}

	/**
	 * Method to save new population to BBDD
	 * 
	 * @param agents 
	 * 			ArrayList of the agents
	 * @param popName 
	 * 			Population name
	 */
	public void setNewAgentPopulation(ArrayList<CommunityAgent> agents,
			String popName) {

		ArrayList<Integer> agent_type_id= new ArrayList<Integer>();
		ArrayList<Integer> agent_id_profile = new ArrayList<Integer>();
		String stmt = null;
		int id_population = 0;

		try {

			Statement statement = connection.createStatement();
			stmt = "INSERT INTO agent_population (id, description) "
					+ " VALUES (NULL,'" + popName + "')";
			statement.executeUpdate(stmt);

			for(int i = 0 ; i < agents.size() ; i++){
				stmt= "INSERT INTO agent_profile (`id`, `name`, `correct`, `insult`,"
						+ "`spam`, `violent`, `porn`, `uploadProbability`, `forum`,"
						+ "`theReporter`, `photoVideo`, `viewMode`, `distributionMode`,"
						+ "`insultComplaint`, `spamComplaint`, `violentComplaint`,"
						+ "`pornComplaint`, `population`)"
						+ "VALUES (NULL, '"
						+ agents.get(i).getName()+"', "
						+ agents.get(i).getUploadProfile().getCorrect()+","
						+ agents.get(i).getUploadProfile().getInsult()+","
						+ agents.get(i).getUploadProfile().getSpam()+", "
						+ agents.get(i).getUploadProfile().getViolent()+", "
						+ agents.get(i).getUploadProfile().getPorn()+","
						+ agents.get(i).getUploadProfile().getUploadProbability()+", "
						+ agents.get(i).getViewProfile().getForum()+", "
						+ agents.get(i).getViewProfile().getTheReporter()+","
						+ agents.get(i).getViewProfile().getPhotoVideo()+","
						+ agents.get(i).getViewProfile().getViewMode()+"," //view mode
						+ "0," // distribution mode
						+ agents.get(i).getComplaintProfile().getInsult()+","
						+ agents.get(i).getComplaintProfile().getSpam()+","
						+ agents.get(i).getComplaintProfile().getViolent()+","
						+ agents.get(i).getComplaintProfile().getPorn()+",'"
						+ popName+"');";

				System.out.println(stmt);
				statement.executeUpdate(stmt);
			}

			try{

				PreparedStatement statemnt;
				ResultSet result;

				statemnt = connection.prepareStatement(
						"SELECT * FROM agent_population WHERE description = '"+popName+"'");
				result = statemnt.executeQuery();
				while(result.next()){
					id_population = result.getInt("id");
				}

				statemnt = connection.prepareStatement(
						"SELECT name, id FROM agent_profile WHERE population = '"+popName+"'");
				result = statemnt.executeQuery();
				ArrayList<String> profileName = new ArrayList<String>();
				while(result.next()){
					profileName.add(result.getString("name"));
					agent_id_profile.add(result.getInt("id"));
				}

				for(int i = 0 ; i < profileName.size() ; i++){	
					statemnt = connection.prepareStatement(
							"SELECT id FROM agent_type WHERE description = '"+profileName.get(i)+"'");
					result = statemnt.executeQuery();
					while(result.next()){
						agent_type_id.add(result.getInt("id"));
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Error with SQL query. The format isn't correct,"
						+ " see the manual of Mysql");
			}

			for(int i = 0 ; i < agents.size() ; i++){
				stmt = "INSERT INTO population (agent_type, agent_profile, population, quantity) "
						+ " VALUES ("+agent_id_profile.get(i)+","+agent_id_profile.get(i)+","
						+ ""+id_population+","+agents.get(i).getQuantity()+")";
				statement.executeUpdate(stmt);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}




