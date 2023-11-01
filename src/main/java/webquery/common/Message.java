package webquery.common;

import java.util.ResourceBundle;

public class Message {

	private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");
	
	private Message() {
		
	}
	
	/**
	 * Get a message from message.properties file.
	 * @param id Message ID.
	 * @return Message of the ID passed as a parameter.
	 */
	public static String get(String id) {
		return bundle.getString(id);
	}

}
