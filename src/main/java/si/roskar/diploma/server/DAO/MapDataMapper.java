package si.roskar.diploma.server.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import si.roskar.diploma.shared.KingdomMap;
import si.roskar.diploma.shared.KingdomUser;

public class MapDataMapper implements RowMapper<KingdomMap>{

	@Override
	public KingdomMap mapRow(ResultSet rs, int rowNumber) throws SQLException{
		KingdomMap map = new KingdomMap();
		map.setId(rs.getInt("id"));
		map.setName(rs.getString("name"));
		
		KingdomUser user = new KingdomUser();
		user.setId(rs.getInt("user_id"));
		
		map.setUser(user);
		
		return map;
	}
}
