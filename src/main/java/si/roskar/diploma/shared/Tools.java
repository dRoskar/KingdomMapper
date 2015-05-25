package si.roskar.diploma.shared;

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
	
	public static String wktToGmlFriendly(String wktGeometry){
		// convert point
		if(wktGeometry.contains("POINT")){
			// remove geometry type label
			wktGeometry = wktGeometry.substring(wktGeometry.indexOf("("));
			
			// remove outer brackets
			wktGeometry = wktGeometry.substring(1, wktGeometry.length() -1);
			
			// switch space and colon
			wktGeometry = wktGeometry.replace(" ", ",");
			
			return wktGeometry;
		}
		
		if(wktGeometry.contains("LINESTRING")){
			// remove geometry type label
			wktGeometry = wktGeometry.substring(wktGeometry.indexOf("("));
			
			// remove outer brackets
			wktGeometry = wktGeometry.substring(1, wktGeometry.length() -1);
			
			// switch spaces and colons
			wktGeometry = wktGeometry.replace(",", "|");
			wktGeometry = wktGeometry.replace(" ", ",");
			wktGeometry = wktGeometry.replace("|", " ");
			
			return wktGeometry;
		}
		
		if(wktGeometry.contains("POLYGON")){
			// remove geometry type label
			wktGeometry = wktGeometry.substring(wktGeometry.indexOf("("));
			
			// remove outer brackets
			wktGeometry = wktGeometry.substring(2, wktGeometry.length() -2);
			
			// switch spaces and colons
			wktGeometry = wktGeometry.replace(",", "|");
			wktGeometry = wktGeometry.replace(" ", ",");
			wktGeometry = wktGeometry.replace("|", " ");
			
			return wktGeometry;
		}
		
		return null;
	}
}