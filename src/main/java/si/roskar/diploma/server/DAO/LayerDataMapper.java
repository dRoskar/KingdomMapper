package si.roskar.diploma.server.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import si.roskar.diploma.shared.KingdomLayer;
import si.roskar.diploma.shared.KingdomMap;

public class LayerDataMapper implements RowMapper<KingdomLayer>{

	@Override
	public KingdomLayer mapRow(ResultSet rs, int rowNumber) throws SQLException{
		KingdomLayer layer = new KingdomLayer();
		layer.setId(rs.getInt("id"));
		layer.setName(rs.getString("name"));
		layer.setStyle(rs.getString("style").equals("null") ? "" : rs.getString("style"));
		layer.setVisible(rs.getBoolean("visibility"));
		layer.setGeometryType(rs.getString("geometry_type"));
		
		KingdomMap map = new KingdomMap();
		map.setId(rs.getInt("map_id"));
		
		layer.setMap(map);
		
		return layer;
	}
}
