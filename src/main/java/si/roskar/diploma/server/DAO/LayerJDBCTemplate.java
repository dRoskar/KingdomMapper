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
	
	public int insert(String name, String style, boolean visibility, GeometryType geometryType, int zIndex, int mapId){
		
		// escape unicode
		name = Tools.encodeToNumericCharacterReference(name);
		
		// escape apostrophes
		name = name.replace("'", "''");
		
		String visibilityString = visibility == true ? "TRUE" : "FALSE";
		final String SQL = "INSERT INTO public.\"Layer\"(name, style, visibility, geometry_type, z_index, map_id) VALUES ('" + name + "', '" + style + "', " + visibilityString + ", '"
				+ geometryType.getGeometryName() + "', " + zIndex + ", " + mapId + ")";
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
	
	public boolean updateLayer(int layerId, String name, String style, boolean visibility, int zIndex){
		String visibilityString = visibility == true ? "TRUE" : "FALSE";
		String SQL = "UPDATE public.\"Layer\" SET name = '" + name + "', style = '" + style + "', visibility = " + visibilityString + ", z_index = " + zIndex + "  WHERE id = " + layerId;
		jdbcTemplateObject.update(SQL);
		
		return true;
	}
}