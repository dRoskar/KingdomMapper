package si.roskar.diploma.shared;

import java.util.ArrayList;
import java.util.List;

import org.gwtopenmaps.openlayers.client.geometry.Geometry;
import org.gwtopenmaps.openlayers.client.geometry.LinearRing;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.gwtopenmaps.openlayers.client.geometry.Polygon;

import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.gml2.GMLWriter;

public class Tools{
	public static String toCamelCase(String value){
		// format string
		char[] stringArray = value.toLowerCase().toCharArray();
		stringArray[0] = Character.toUpperCase(stringArray[0]);
		return new String(stringArray);
	}
	
	public static boolean isNumeric(String value){
		try{
			double number = Double.parseDouble(value);
		}
		catch(NumberFormatException e){
			return false;
		}
		
		return true;
	}
	
	public static String encodeToNumericCharacterReference(String str) {
		char[] ch = str.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ch.length; i++) {
			if (ch[i] < 0x20 || ch[i] > 0x7f)
				sb.append("&#").append((int) ch[i]).append(";");
			else
				sb.append(ch[i]);
		}
		return sb.toString();
	}
	
	public static String createColoradoSizedGrid(int strips){
		double minx = -109.045;
		double miny = 36.999;
		double maxx = -102.045;
		double maxy = 43.8;
		
		String wkt = "MULTILINESTRING(";
		
		if(!(strips < 1)){
			// divide latitude
			double latDelta = maxx - minx;
			latDelta = latDelta / strips;
			
			// divide longitude
			double lonDelta = maxy - miny;
			lonDelta = lonDelta / strips;
			
			// add vertical lines
			for(int i = 0; i < strips + 1; i++){
				wkt = wkt + "(" + (minx + (i * latDelta)) + " " + miny + ", " + (minx + (i * latDelta)) + " " + maxy + "),";
			}
			
			// add horizontal lines
			for(int i = 0; i < strips + 1; i++){
				wkt = wkt + "(" + minx + " " + (miny + (i * lonDelta)) + ", " + maxx + " " + (miny + (i * lonDelta)) + "),";
			}
			
			wkt = wkt.subSequence(0,  wkt.length() - 1).toString();
			
			wkt = wkt + ")";
		}
		
		return wkt;
	}

	public static String wktToGml(String wktGeometry){
		WKTReader wktReader = new WKTReader();
		GMLWriter gmlWriter = new GMLWriter();
		
		try{
			return gmlWriter.write(wktReader.read(wktGeometry));
		}catch(ParseException e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Polygon getPolygonFromGeometry(Geometry geometry){
		// extract linear rings from geometry
		String wkt = geometry.toString();
		
		if(!wkt.contains("POLYGON")){
			return null;
		}
		// trim
		wkt = wkt.substring(7, wkt.length());
		wkt = wkt.substring(1, wkt.length() - 1);
		
		// get outer ring
		String[] rings = wkt.split("\\)");
		String outerRingString = rings[0];
		outerRingString = outerRingString.substring(1);
		
		String[] points = outerRingString.split(",");
		
		List<Point> pointObjects = new ArrayList<Point>();
		for(String point : points){
			String[] pointString = point.split(" ");
			pointObjects.add(new Point(Double.parseDouble(pointString[0]), Double.parseDouble(pointString[1])));
		}
		
		LinearRing outerRing = new LinearRing((Point[])pointObjects.toArray());
		
		return new Polygon(new LinearRing[] { outerRing });
	}
}
