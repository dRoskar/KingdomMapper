package si.roskar.diploma.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class KingdomLayer implements IsSerializable{
	
	private String		id				= null;
	private String		layerName		= null;
	private String		style			= null;
	private boolean		visible			= false;
	private String		format			= "image/png";
	private String		projection		= "EPSG:4326";
	private float		opacity			= 1.0f;
	private String		geometryType	= null;
	private KingdomMap	map				= null;
	
	public KingdomLayer(){
		
	}
	
	public KingdomLayer(String id, String layerName, String style, boolean visibility, String format, String projection, float opacity){
		super();
		this.id = id;
		this.layerName = layerName;
		this.style = style;
		this.visible = visibility;
		this.format = format;
		this.projection = projection;
		this.opacity = opacity;
	}
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getLayerName(){
		return layerName;
	}
	
	public void setLayerName(String layerName){
		this.layerName = layerName;
	}
	
	public String getStyle(){
		return style;
	}
	
	public void setStyle(String style){
		this.style = style;
	}
	
	public boolean isVisible(){
		return visible;
	}

	public void setVisible(boolean visible){
		this.visible = visible;
	}

	public String getFormat(){
		return format;
	}
	
	public void setFormat(String format){
		this.format = format;
	}
	
	public String getProjection(){
		return projection;
	}
	
	public void setProjection(String projection){
		this.projection = projection;
	}
	
	public float getOpacity(){
		return opacity;
	}
	
	public void setOpacity(float opacity){
		this.opacity = opacity;
	}
	
	public String getGeometryType(){
		return geometryType;
	}
	
	public void setGeometryType(String geometryType){
		this.geometryType = geometryType;
	}

	public KingdomMap getMap(){
		return map;
	}

	public void setMap(KingdomMap map){
		this.map = map;
	}
}
