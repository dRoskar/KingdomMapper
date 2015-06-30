package si.roskar.diploma.server.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import si.roskar.diploma.shared.UserHash;

public class UserHashMapper implements RowMapper<UserHash>{

	@Override
	public UserHash mapRow(ResultSet rs, int rowNumber) throws SQLException{
		return new UserHash(rs.getString("password"));
	}
}
