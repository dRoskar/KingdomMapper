package si.roskar.diploma.shared;

public class KingdomLayer {
	private String	id			= null;
	private String	layerName	= null;
	private String	style		= null;
	private boolean	visibility	= false;
	private String	format		= "image/png";
	private String 	projection	= null;
	private	float	opacity		=	1.0f;
	
	public KingdomLayer(){
		
	}

	public KingdomLayer(String id, String layerName, String style, boolean visibility, String format, String projection, float opacity) {
		super();
		this.id = id;
		this.layerName = layerName;
		this.style = style;
		this.visibility = visibility;
		this.format = format;
		this.projection = projection;
		this.opacity = opacity;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLayerName() {
		return layerName;
	}

	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public boolean isVisibility() {
		return visibility;
	}

	public void setVisibility(boolean visibility) {
		this.visibility = visibility;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getProjection() {
		return projection;
	}

	public void setProjection(String projection) {
		this.projection = projection;
	}

	public float getOpacity() {
		return opacity;
	}

	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}
}