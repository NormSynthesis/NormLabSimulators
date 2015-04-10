package es.csic.iiia.normlab.onlinecomm.content.comment;


/**
 * Correct comment
 * 
 * @author davidsanchezpinsach
 * 
 * Modified by Iosu Mendizabal
 *
 */
public class CorrectComment extends Comment {

	public CorrectComment(int id, int contentType, String file, String url,
			String message, int section, String contentTypeDesc, double timeStep) {
		super( id, contentType, file, url, message, section, contentTypeDesc, timeStep);
	}
}
