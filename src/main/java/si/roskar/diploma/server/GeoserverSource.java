package si.roskar.diploma.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import si.roskar.diploma.server.util.ApplicationProperties;

public class GeoserverSource{
	
	private static final Logger logger = LoggerFactory.getLogger(GeoserverSource.class);
	
	public static String getWmsUrl(){
		logger.trace("Retrieving geoserver wms source (unknown user)");
		return ApplicationProperties.getProperty("GEOSERVER.WMS");
	}
	
	public static String getWfsUrl(){
		logger.trace("Retrieving geoserver wfs source (unknown user)");
		return ApplicationProperties.getProperty("GEOSERVER.WFS");
	}
}
