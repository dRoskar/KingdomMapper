package si.roskar.diploma.server.util;

import java.util.ResourceBundle;

public class ApplicationProperties{
	public static String getProperty(String key){
		ResourceBundle resource = ResourceBundle.getBundle("kingdomMapper");
		return resource.getString(key);
	}
}