package si.roskar.diploma.server.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import si.roskar.diploma.shared.KingdomUser;

public class UserDataMapper implements RowMapper<KingdomUser>{

	@Override
	public KingdomUser mapRow(ResultSet rs, int rowNumber) throws SQLException{
		KingdomUser user = new KingdomUser();
		
		user.setId(rs.getInt("id"));
		user.setName(rs.getString("name"));
		user.setLastMapId(rs.getInt("last_map"));
		
		return user;
	}
}
