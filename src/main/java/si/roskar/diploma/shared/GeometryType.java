package si.roskar.diploma.shared;

public enum GeometryType{
	POINT("POINT"),
	LINE("LINESTRING"),
	POLYGON("POLYGON"),
	MARKER("MARKER");
	
	private String geometryName;
	
	GeometryType(String geometryName){
		this.geometryName = geometryName;
	}

	public String getGeometryName(){
		return geometryName;
	}
	
	public static GeometryType getTypeFromGeometryName(String geometryName){
		if(geometryName.equals(POINT.getGeometryName())){
			return POINT;
		}
		
		if(geometryName.equals(LINE.getGeometryName())){
			return LINE;
		}
		
		if(geometryName.equals(POLYGON.getGeometryName())){
			return POLYGON;
		}
		
		if(geometryName.equals(MARKER.getGeometryName())){
			return MARKER;
		}
		
		return null;
	}
}