package si.roskar.diploma.server;

import si.roskar.diploma.server.util.ApplicationProperties;

public class GeoserverSource{
	public static String getWmsUrl(){
		return ApplicationProperties.getProperty("GEOSERVER.WMS");
	}
	
	public static String getWfsUrl(){
		return ApplicationProperties.getProperty("GEOSERVER.WFS");
	}
}
