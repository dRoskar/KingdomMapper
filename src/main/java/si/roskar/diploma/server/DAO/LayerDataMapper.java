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
		layer.setOpacity((float)rs.getDouble("opacity"));
		
		KingdomMap map = new KingdomMap();
		map.setId(rs.getInt("map_id"));
		
		layer.setMap(map);
		
		layer.setColor(rs.getString("color"));
		layer.setLabelColor(rs.getString("label_color"));
		layer.setSize(rs.getInt("size"));
		layer.setStrokeWidth(rs.getInt("stroke_width"));
		layer.setFillColor(rs.getString("fill_color"));
		layer.setLabelFillColor(rs.getString("label_fill_color"));
		layer.setFillOpacity(rs.getDouble("fill_opacity"));
		layer.setStrokeOpacity(rs.getDouble("stroke_opacity"));
		layer.setMaxScale(rs.getDouble("maxscale"));
		layer.setMinScale(rs.getDouble("minscale"));
		layer.setMarkerImage(rs.getString("marker_image"));
		layer.setTextureImage(rs.getString("texture_image"));
		
		return layer;
	}
}