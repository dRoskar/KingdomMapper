package si.roskar.diploma.server.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

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
	
	public int insert(String name, String style, boolean visibility, String geometryType, int mapId){
		
		String visibilityString = visibility == true ? "TRUE" : "FALSE";
		final String SQL = "INSERT INTO public.\"Layer\"(name, style, visibility, geometryType, map_id) VALUES ('" + name + "', '" + style + "', " + visibilityString + ", '" + geometryType + "', "
				+ mapId + ")";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		jdbcTemplateObject.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException{
				return connection.prepareStatement(SQL, new String[] { "id" });
			}
		}, keyHolder);
		
		return keyHolder.getKey().intValue();
	}
}
