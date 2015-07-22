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

import si.roskar.diploma.shared.KingdomUser;
import si.roskar.diploma.shared.UserHash;

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
		final String SQL = "INSERT INTO \"User\"(name, password) VALUES ('" + username + "', '" + password + "')";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		jdbcTemplateObject.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException{
				return connection.prepareStatement(SQL, new String[] {"id"});
			}
		}, keyHolder);
		
		return keyHolder.getKey().intValue();
	}
	
	public List<KingdomUser> getUserByName(String username){
		final String SQL = "SELECT * FROM \"User\" WHERE name='" + username + "'";
		
		return jdbcTemplateObject.query(SQL, new UserDataMapper());
	}
	
	public String getUserHash(String username){
		final String SQL = "SELECT password FROM \"User\" WHERE name='" + username + "'";
		
		List<UserHash> hashes = jdbcTemplateObject.query(SQL, new UserHashMapper());
		
		if(!hashes.isEmpty()){
			return hashes.get(0).getHash();
		}
		
		return null;
	}
	
	public boolean setUserLastMap(int lastMapId, int userId){
		final String SQL = "UPDATE \"User\" SET last_map = '" + lastMapId + "' WHERE id = " + userId;
		
		jdbcTemplateObject.update(SQL);
		
		return true;
	}
}