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

import si.roskar.diploma.shared.KingdomMap;
import si.roskar.diploma.shared.Tools;

public class MapJDBCTemplate{
	private DataSource		dataSource			= null;
	private JdbcTemplate	jdbcTemplateObject	= null;
	
	public DataSource getDataSource(){
		return dataSource;
	}
	
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}
	
	public int insert(String name, final int userId){
		
		// escape unicode
		final String encodedName = Tools.encodeToNumericCharacterReference(name);
		
		final String SQL = "INSERT INTO \"Map\"(name, user_id) VALUES (?, ?)";
		
		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException{
				final PreparedStatement ps = connection.prepareStatement(SQL, new String[] { "id" });
				
				ps.setString(1, encodedName);
				ps.setInt(2, userId);
				
				return ps;
			}
		};
		
		final KeyHolder keyHolder = new GeneratedKeyHolder();
		
		jdbcTemplateObject.update(psc, keyHolder);
		
		return keyHolder.getKey().intValue();
	}
	
	public boolean mapExists(String mapName, int userId){
		
		// escape unicode
		mapName = Tools.encodeToNumericCharacterReference(mapName);
		
		final String SQL = "SELECT * FROM \"Map\" WHERE user_id = ? AND name = ?";
		
		List<KingdomMap> maps = jdbcTemplateObject.query(SQL, new MapDataMapper(), userId, mapName);
		
		if(maps.isEmpty()){
			return false;
		}
		
		return true;
	}
	
	public List<KingdomMap> getMapList(int userId){
		final String SQL = "SELECT * FROM \"Map\" WHERE user_id = ?";
		
		return jdbcTemplateObject.query(SQL, new MapDataMapper(), userId);
	}
	
	public KingdomMap getMap(int id){
		final String SQL = "SELECT * FROM \"Map\" WHERE id = ?";
		
		List<KingdomMap> results = jdbcTemplateObject.query(SQL, new MapDataMapper(), id);
		
		if(!results.isEmpty()){
			return results.get(0);
		}
		
		return null;
	}
	
	public boolean deleteMap(int mapId){
		final String SQL = "DELETE FROM \"Map\" where id = ?";
		
		jdbcTemplateObject.update(SQL, mapId);
		
		return true;
	}
	
	public boolean updateMapName(int mapId, String name){
		// escape unicode
		name = Tools.encodeToNumericCharacterReference(name);
		
		String SQL = "UPDATE \"Map\" SET name = ? WHERE id = ?";
		jdbcTemplateObject.update(SQL, name, mapId);
		
		return true;
	}
	
	public boolean updatePreviousView(int mapId, double llx, double lly, double urx, double ury, int zoomLevel){
		String SQL = "UPDATE \"Map\" SET lower_left_x = ?, lower_left_y = ?, upper_right_x = ?, upper_right_y = ?, previous_zoom = ? WHERE id = ?";
		jdbcTemplateObject.update(SQL, llx, lly, urx, ury, zoomLevel, mapId);
		
		return true;
	}
}