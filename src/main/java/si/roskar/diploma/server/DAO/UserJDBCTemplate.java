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

	public int insert(final String username, final String password, final boolean isAdmin){
		
		final String SQL = "INSERT INTO \"User\"(name, password, is_admin) VALUES (?, ?, ?)";
		
		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException{
				final PreparedStatement ps = connection.prepareStatement(SQL, new String[] {"id"});
				
				ps.setString(1, username);
				ps.setString(2, password);
				ps.setBoolean(3, isAdmin);
				
				return ps;
			}
		};
		
		final KeyHolder keyHolder = new GeneratedKeyHolder();
		
		jdbcTemplateObject.update(psc, keyHolder);
		
		return keyHolder.getKey().intValue();
	}
	
	public KingdomUser getUserByName(String username){
		final String SQL = "SELECT * FROM \"User\" WHERE name = ?";
		
		return jdbcTemplateObject.queryForObject(SQL, new Object[]{username}, new UserDataMapper());
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
		final String SQL = "UPDATE \"User\" SET last_map = ? WHERE id = ?";
		
		jdbcTemplateObject.update(SQL, lastMapId, userId);
		
		return true;
	}
	
	public boolean isAdmin(String username){
		final String SQL = "SELECT is_admin FROM \"User\" WHERE name = ?";
		List<Boolean> results = jdbcTemplateObject.queryForList(SQL, Boolean.class, username);
		
		if(results.size() < 1){
			return false;
		}
		
		return results.get(0);
	}
}