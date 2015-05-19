package si.roskar.diploma.server.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class UserJDBCTemplate{
	private DataSource dataSource = null;
	private JdbcTemplate jdbcTemplateObject = null;
	
	public DataSource getDataSource(){
		return dataSource;
	}
	
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}

	public int insert(String username, String password){
		final String SQL = "INSERT INTO public.\"User\"(name, password) VALUES ('" + username + "', '" + password + "')";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		jdbcTemplateObject.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException{
				return connection.prepareStatement(SQL, new String[] {"id"});
			}
		}, keyHolder);
		
		return keyHolder.getKey().intValue();
	}
}