package si.roskar.diploma.server.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import si.roskar.diploma.shared.GeometryType;
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
		layer.setGeometryType(GeometryType.getTypeFromGeometryName(rs.getString("geometry_type")));
		layer.setZIndex(rs.getInt("z_index"));
		
		KingdomMap map = new KingdomMap();
		map.setId(rs.getInt("map_id"));
		
		layer.setMap(map);
		
		layer.setColor(rs.getString("color"));
		layer.setSize(rs.getInt("size"));
		layer.setShape(rs.getString("shape"));
		layer.setStrokeWidth(rs.getInt("stroke_width"));
		layer.setFillColor(rs.getString("fill_color"));
		layer.setFillOpacity(rs.getDouble("fill_opacity"));
		layer.setStrokeOpacity(rs.getDouble("stroke_opacity"));
		
		return layer;
	}
}