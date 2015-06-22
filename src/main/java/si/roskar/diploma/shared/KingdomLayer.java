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
	private int				zIndex			= 0;
	private GeometryType	geometryType	= null;
	private KingdomMap		map				= null;
	private String			color			= null;
	private int				size			= 0;
	private String			shape			= null;
	private int				strokeWidth		= 0;
	private String			fillColor		= null;
	private double			fillOpacity		= 1.0;
	private double			strokeOpacity	= 1.0;
	private double			maxScale		= 0.0;
	private double			minScale		= 0.0;
	private boolean			hasLabel		= false;
	private String			markerImage		= null;
	
	public KingdomLayer(){
		
	}
	
	public KingdomLayer(int id, String name, String style, boolean visibility, String format, String projection, float opacity){
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
	
	public int getZIndex(){
		return zIndex;
	}
	
	public void setZIndex(int zIndex){
		this.zIndex = zIndex;
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
	
	public String getColor(){
		return color;
	}
	
	public void setColor(String color){
		this.color = color;
	}
	
	public int getSize(){
		return size;
	}
	
	public void setSize(int size){
		this.size = size;
	}
	
	public String getShape(){
		return shape;
	}
	
	public void setShape(String shape){
		this.shape = shape;
	}
	
	public int getStrokeWidth(){
		return strokeWidth;
	}
	
	public void setStrokeWidth(int strokeWidth){
		this.strokeWidth = strokeWidth;
	}
	
	public String getFillColor(){
		return fillColor;
	}
	
	public void setFillColor(String fillColor){
		this.fillColor = fillColor;
	}
	
	public double getFillOpacity(){
		return fillOpacity;
	}
	
	public void setFillOpacity(double fillOpacity){
		this.fillOpacity = fillOpacity;
	}
	
	public double getStrokeOpacity(){
		return strokeOpacity;
	}
	
	public void setStrokeOpacity(double strokeOpacity){
		this.strokeOpacity = strokeOpacity;
	}
	
	public double getMaxScale(){
		return maxScale;
	}
	
	public void setMaxScale(double maxScale){
		this.maxScale = maxScale;
	}
	
	public double getMinScale(){
		return minScale;
	}
	
	public void setMinScale(double minScale){
		this.minScale = minScale;
	}
	
	public boolean hasLabel(){
		return hasLabel;
	}
	
	public void setHasLabel(boolean hasLabel){
		this.hasLabel = hasLabel;
	}
	
	public String getMarkerImage(){
		return markerImage;
	}

	public void setMarkerImage(String markerImage){
		this.markerImage = markerImage;
	}

	public KingdomLayer getLayer(){
		return this;
	}
	
	public String getEnvValues(){
		String env = "";
		if(this.getGeometryType().equals(GeometryType.POINT)){
			if(this.getColor() != null){
				env = env + "color:" + this.getColor() + ";";
			}
			
			if(this.getSize() > 0){
				env = env + "size:" + this.getSize() + ";";
			}
			
			if(this.getShape() != null){
				env = env + "shape:" + this.getShape() + ";";
			}
		}else if(this.getGeometryType().equals(GeometryType.LINE)){
			if(this.getColor() != null){
				env = env + "color:" + this.getColor() + ";";
			}
			
			if(this.getStrokeWidth() > 0){
				env = env + "width:" + this.getStrokeWidth() + ";";
			}
		}else if(this.getGeometryType().equals(GeometryType.POLYGON)){
			if(this.getFillColor() != null){
				env = env + "fill_color:" + this.getFillColor() + ";";
			}
			
			env = env + "fill_opacity:" + this.getFillOpacity() + ";";
			
			if(this.getColor() != null){
				env = env + "color:" + this.getColor() + ";";
			}
			
			if(this.getStrokeWidth() > 0){
				env = env + "stroke_width:" + this.getStrokeWidth() + ";";
			}
			
			env = env + "stroke_opacity:" + this.getStrokeOpacity() + ";";
		}else if(this.getGeometryType().equals(GeometryType.MARKER)){
			if(this.getFillColor() != null){
				env = env + "fill_color:" + this.getFillColor() + ";";
			}
			
			if(this.getColor() != null){
				env = env + "color:" + this.getColor() + ";";
			}
			
			if(this.getSize() > 0){
				env = env + "size:" + this.getSize() + ";";
				env = env + "label_displacement:" + ((this.getSize()/2) + this.getSize()/10) + ";";
			}
			
			if(this.getMarkerImage() != null){
				env = env + "marker:" + this.getMarkerImage() + ";";
			}
		}
		
		if(!env.equals("")){
			return env.substring(0, env.length() - 1);
		}else{
			return null;
		}
	}
	
	public boolean isInScale(double scale){
		return (scale >= maxScale && scale <= minScale);
	}
}
