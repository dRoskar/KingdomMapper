package si.roskar.diploma.server.DB;

import java.util.ResourceBundle;

public class DBProperties{
	public static String getProperty(String key){
		ResourceBundle resource = ResourceBundle.getBundle("kingdomMapper");
		return resource.getString(key);
	}
}
