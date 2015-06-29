package si.roskar.diploma.server.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import si.roskar.diploma.shared.GeometryType;
import si.roskar.diploma.shared.KingdomLayer;
import si.roskar.diploma.shared.Tools;

public class LayerJDBCTemplate{
	private DataSource		dataSource			= null;
	private JdbcTemplate	jdbcTemplateObject	= null;
	
	public DataSource getDataSource(){
		return dataSource;
	}
	
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}
	
	public int insert(String name, String style, float opacity, boolean visibility, GeometryType geometryType, int zIndex, String color, String labelColor, int size, String fillColor, String labelFillColor, int strokeWidth, double strokeOpacity, double fillOpacity, double maxScale, double minScale, String markerImage, int mapId){
		// escape unicode
		name = Tools.encodeToNumericCharacterReference(name);
		
		// escape apostrophes
		name = name.replace("'", "''");
		
		String visibilityString = visibility == true ? "TRUE" : "FALSE";
		final String SQL = "INSERT INTO public.\"Layer\"(name, style, opacity, visibility, geometry_type, z_index, color, label_color, size, fill_color, label_fill_color, stroke_width, stroke_opacity, fill_opacity, maxscale, minscale, marker_image, map_id) VALUES ('" + name + "', '" + style + "', " + opacity +  ", " + visibilityString + ", '"
				+ geometryType.getGeometryName() + "', " + zIndex + ", '" + color + "', '" + labelColor + "', " + size + ", '" + fillColor + "', '" + labelFillColor + "', " + strokeWidth + ", " + strokeOpacity + ", " + fillOpacity + ", " + maxScale + ", " + minScale + ", '" + markerImage + "', " + mapId + ")";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		jdbcTemplateObject.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException{
				return connection.prepareStatement(SQL, new String[] { "id" });
			}
		}, keyHolder);
		
		return keyHolder.getKey().intValue();
	}
	
	public boolean layerExists(String name, int mapId){
		
		// escape unicode
		name = Tools.encodeToNumericCharacterReference(name);
		
		// escape apostrophes
		name = name.replace("'", "''");
		
		final String SQL = "SELECT * FROM public.\"Layer\" WHERE map_id=" + mapId + " AND name='" + name + "'";
		
		List<KingdomLayer> layers = jdbcTemplateObject.query(SQL, new LayerDataMapper());
		
		if(layers.isEmpty()){
			return false;
		}
		
		return true;
	}
	
	public List<KingdomLayer> getLayerList(int mapId){
		final String SQL = "SELECT * FROM public.\"Layer\" WHERE map_id=" + mapId;
		
		return jdbcTemplateObject.query(SQL, new LayerDataMapper());
	}
	
	public boolean deleteLayer(int layerId){
		final String SQL = "DELETE FROM public.\"Layer\" where id = " + layerId;
		
		jdbcTemplateObject.update(SQL);
		
		return true;
	}
	
	public boolean updateLayerState(int layerId, boolean visibility, int zIndex, float opacity){
		String visibilityString = visibility == true ? "TRUE" : "FALSE";
		String SQL = "UPDATE public.\"Layer\" SET visibility = " + visibilityString + ", z_index = " + zIndex + ", opacity = " + opacity + " WHERE id = " + layerId;
		jdbcTemplateObject.update(SQL);
		
		return true;
	}
	
	public boolean updateLayerName(int layerId, String name){
		// escape unicode
		name = Tools.encodeToNumericCharacterReference(name);
				
		// escape apostrophes
		name = name.replace("'", "''");
				
		String SQL = "UPDATE public.\"Layer\" SET name = '" + name + "' WHERE id = " + layerId;
		jdbcTemplateObject.update(SQL);
		
		return true;
	}
	
	public boolean updateLayerStyle(int layerId, String style, String color, String labelColor, int size, String fillColor, String labelFillColor, int strokeWidth, double fillOpacity, double strokeOpacity, double maxScale, double minScale, String markerImage, String textureImage){
		style = style == null ? "" : style;
		color = color == null ? "" : color;
		labelColor = labelColor == null ? "" : labelColor;
		fillColor = fillColor == null ? "" : fillColor;	
		labelFillColor = labelFillColor == null ? "" : labelFillColor;
		
		String SQL = "UPDATE public.\"Layer\" SET style = '" + style + "', color = '" + color + "', label_color = '" + labelColor + "', size = " + size + ", fill_color = '" + fillColor + "', label_fill_color = '" + labelFillColor + "', stroke_width = " + strokeWidth + ", fill_opacity = " + fillOpacity + ", stroke_opacity = " + strokeOpacity + ", maxscale = " + maxScale + ", minscale = " + minScale + ", marker_image = '" + markerImage + "', texture_image = '" + textureImage + "' WHERE id = " + layerId;
		jdbcTemplateObject.update(SQL);
		
		return true;
	}
}