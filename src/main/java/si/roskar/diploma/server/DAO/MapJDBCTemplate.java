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

public class MapJDBCTemplate{
	private DataSource dataSource = null;
	private JdbcTemplate jdbcTemplateObject = null;
	
	public DataSource getDataSource(){
		return dataSource;
	}
	
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}

	public int insert(String name, int userId){
		
		final String SQL = "INSERT INTO public.\"Map\"(name, user_id) VALUES ('" + name + "', " + userId + ")";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		jdbcTemplateObject.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException{
				return connection.prepareStatement(SQL, new String[] {"id"});
			}
		}, keyHolder);
		
		return keyHolder.getKey().intValue();
	}
	
	public boolean mapExists(String mapName, int userId){
		
		final String SQL = "SELECT * FROM public.\"Map\" WHERE user_id=" + userId + " AND name='" + mapName + "'";
		
		List<KingdomMap> maps = jdbcTemplateObject.query(SQL, new MapDataMapper());
		
		if(maps.isEmpty()){
			return false;
		}
		
		return true;
	}
	
	public List<KingdomMap> getMapList(int userId){
		final String SQL = "SELECT * FROM public.\"Map\" WHERE user_id=" + userId;
		
		return jdbcTemplateObject.query(SQL, new MapDataMapper());
	}
	
	public boolean deleteMap(int mapId){
		final String SQL = "DELETE FROM public.\"Map\" where id = " + mapId;
		
		jdbcTemplateObject.update(SQL);
		
		return true;
	}
}