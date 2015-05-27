package si.roskar.diploma.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class KingdomLayer implements IsSerializable{
	
	private int				id;
	private String			name			= null;
	private String			style			= null;
	private boolean			visible			= false;
	private String			format			= "image/png";
	private String			projection		= "EPSG:4326";
	private float			opacity			= 1.0f;
	private GeometryType	geometryType	= null;
	private KingdomMap		map				= null;
	
	public KingdomLayer(){
		
	}
	
	public KingdomLayer(int id, String name, String style, boolean visibility, String format, String projection, float opacity){
		super();
		this.id = id;
		this.name = name;
		this.style = style;
		this.visible = visibility;
		this.format = format;
		this.projection = projection;
		this.opacity = opacity;
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
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
	
	public GeometryType getGeometryType(){
		return geometryType;
	}
	
	public void setGeometryType(GeometryType geometryType){
		this.geometryType = geometryType;
	}
	
	public KingdomMap getMap(){
		return map;
	}
	
	public void setMap(KingdomMap map){
		this.map = map;
	}
}
