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
	
	public int insert(String name, final String style, final float opacity, final boolean visibility, final GeometryType geometryType, final int zIndex, final String color, final String labelColor,
			final int size, final String fillColor, final String labelFillColor, final int strokeWidth, final double strokeOpacity, final double fillOpacity, final double maxScale,
			final double minScale, final String markerImage, final int mapId){
		// escape unicode
		final String encodedName = Tools.encodeToNumericCharacterReference(name);
		final String visibilityString = visibility == true ? "TRUE" : "FALSE";
		
		final String SQL = "INSERT INTO \"Layer\"(name, style, opacity, visibility, geometry_type, z_index, color, label_color, size, fill_color, label_fill_color, stroke_width, stroke_opacity, fill_opacity, maxscale, minscale, marker_image, map_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException{
				final PreparedStatement ps = connection.prepareStatement(SQL, new String[] { "id" });
				
				ps.setString(1, encodedName);
				ps.setString(2, style);
				ps.setFloat(3, opacity);
				ps.setString(4, visibilityString);
				ps.setString(5, geometryType.getGeometryName());
				ps.setInt(6, zIndex);
				ps.setString(7, color);
				ps.setString(8, labelColor);
				ps.setInt(9, size);
				ps.setString(10, fillColor);
				ps.setString(11, labelFillColor);
				ps.setInt(12, strokeWidth);
				ps.setDouble(13, strokeOpacity);
				ps.setDouble(14, fillOpacity);
				ps.setDouble(15, maxScale);
				ps.setDouble(16, minScale);
				ps.setString(17, markerImage);
				ps.setInt(18, mapId);
				
				return ps;
			}
		};
		
		final KeyHolder keyHolder = new GeneratedKeyHolder();
		
		jdbcTemplateObject.update(psc, keyHolder);
		
		return keyHolder.getKey().intValue();
	}
	
	public boolean layerExists(String name, int mapId){
		
		// escape unicode
		name = Tools.encodeToNumericCharacterReference(name);
		
		// escape apostrophes
		name = name.replace("'", "''");
		
		final String SQL = "SELECT * FROM \"Layer\" WHERE map_id = ? AND name = ?";
		
		List<KingdomLayer> layers = jdbcTemplateObject.query(SQL, new LayerDataMapper(), mapId, name);
		
		if(layers.isEmpty()){
			return false;
		}
		
		return true;
	}
	
	public List<KingdomLayer> getLayerList(int mapId){
		final String SQL = "SELECT * FROM \"Layer\" WHERE map_id = ?";
		
		return jdbcTemplateObject.query(SQL, new LayerDataMapper(), mapId);
	}
	
	public boolean deleteLayer(int layerId){
		final String SQL = "DELETE FROM \"Layer\" where id = ?";
		
		jdbcTemplateObject.update(SQL, layerId);
		
		return true;
	}
	
	public boolean updateLayerState(int layerId, boolean visibility, int zIndex, float opacity){
		String visibilityString = visibility == true ? "TRUE" : "FALSE";
		String SQL = "UPDATE \"Layer\" SET visibility = ?, z_index = ?, opacity = ? WHERE id = ?";
		jdbcTemplateObject.update(SQL, visibilityString, zIndex, opacity, layerId);
		
		return true;
	}
	
	public boolean updateLayerName(int layerId, String name){
		// escape unicode
		name = Tools.encodeToNumericCharacterReference(name);
		
		String SQL = "UPDATE \"Layer\" SET name = ? WHERE id = ?";
		jdbcTemplateObject.update(SQL, name, layerId);
		
		return true;
	}
	
	public boolean updateLayerStyle(int layerId, String style, String color, String labelColor, int size, String fillColor, String labelFillColor, int strokeWidth, double fillOpacity,
			double strokeOpacity, double maxScale, double minScale, String markerImage, String textureImage){
		style = style == null ? "" : style;
		color = color == null ? "" : color;
		labelColor = labelColor == null ? "" : labelColor;
		fillColor = fillColor == null ? "" : fillColor;
		labelFillColor = labelFillColor == null ? "" : labelFillColor;
		
		String SQL = "UPDATE \"Layer\" SET style = ?, color = ?, label_color = ?, size = ?, fill_color = ?, label_fill_color = ?, stroke_width = ?, fill_opacity = ?, stroke_opacity = ?, maxscale = ?, minscale = ?, marker_image = ?, texture_image = ? WHERE id = ?";
		
		jdbcTemplateObject.update(SQL, style, color, labelColor, size, fillColor, labelFillColor, strokeWidth, fillOpacity, strokeOpacity, maxScale, minScale, markerImage, textureImage, layerId);
		
		return true;
	}
}