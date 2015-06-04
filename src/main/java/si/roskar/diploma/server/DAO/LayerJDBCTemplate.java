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
	
	public int insert(String name, String style, boolean visibility, GeometryType geometryType, int zIndex, String color, int size, String shape, String fillColor, int strokeWidth, double strokeOpacity, double fillOpacity, int mapId){
		
		// escape unicode
		name = Tools.encodeToNumericCharacterReference(name);
		
		// escape apostrophes
		name = name.replace("'", "''");
		
		String visibilityString = visibility == true ? "TRUE" : "FALSE";
		final String SQL = "INSERT INTO public.\"Layer\"(name, style, visibility, geometry_type, z_index, color, size, shape, fill_color, stroke_width, stroke_opacity, fill_opacity, map_id) VALUES ('" + name + "', '" + style + "', " + visibilityString + ", '"
				+ geometryType.getGeometryName() + "', " + zIndex + ", '" + color + "', " + size + ", '" + shape + "', '" + fillColor + "', " + strokeWidth + ", " + strokeOpacity + ", " + fillOpacity + ", " + mapId + ")";
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
	
	public boolean updateLayerState(int layerId, boolean visibility, int zIndex){
		String visibilityString = visibility == true ? "TRUE" : "FALSE";
		String SQL = "UPDATE public.\"Layer\" SET visibility = " + visibilityString + ", z_index = " + zIndex + "  WHERE id = " + layerId;
		jdbcTemplateObject.update(SQL);
		
		return true;
	}
	
	public boolean updateLayerStyle(int layerId, String style, String color, int size, String shape, String fillColor, int strokeWidth, double fillOpacity, double strokeOpacity){
		style = style == null ? "" : style;
		color = color == null ? "" : color;
		shape = shape == null ? "" : shape;
		fillColor = fillColor == null ? "" : fillColor;	
		
		String SQL = "UPDATE public.\"Layer\" SET style = '" + style + "', color = '" + color + "', size = " + size + ", shape = '" + shape + "', fill_color = '" + fillColor + "', stroke_width = " + strokeWidth + ", fill_opacity = " + fillOpacity + ", stroke_opacity = " + strokeOpacity + "  WHERE id = " + layerId;
		jdbcTemplateObject.update(SQL);
		
		return true;
	}
}