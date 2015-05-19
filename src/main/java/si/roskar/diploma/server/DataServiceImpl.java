package si.roskar.diploma.server;

import java.util.List;

import si.roskar.diploma.client.DataService;
import si.roskar.diploma.server.DAO.LayerJDBCTemplate;
import si.roskar.diploma.server.DAO.MapJDBCTemplate;
import si.roskar.diploma.server.DAO.UserJDBCTemplate;
import si.roskar.diploma.server.DB.DBSource;
import si.roskar.diploma.shared.KingdomLayer;
import si.roskar.diploma.shared.KingdomMap;
import si.roskar.diploma.shared.KingdomUser;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DataServiceImpl extends RemoteServiceServlet implements DataService{
	
	private UserJDBCTemplate userJdbcTemplate = null;
	private MapJDBCTemplate mapJdbcTemplate = null;
	private LayerJDBCTemplate layerJdbcTemplate = null;
	
	public DataServiceImpl(){
		userJdbcTemplate = new UserJDBCTemplate();
		userJdbcTemplate.setDataSource(DBSource.getDataSource());
		
		mapJdbcTemplate = new MapJDBCTemplate();
		mapJdbcTemplate.setDataSource(DBSource.getDataSource());
		
		layerJdbcTemplate = new LayerJDBCTemplate();
		layerJdbcTemplate.setDataSource(DBSource.getDataSource());
	}
	
	// ===== ===== USER DATA SERVICES ===== =====
	@Override
	public Integer addUser(KingdomUser user){
		return userJdbcTemplate.insert(user.getName(), user.getPassword());
	}
	// ==========================================
	
	
	// ===== ===== MAP DATA SERVICES ===== =====
	@Override
	public Integer addMap(KingdomMap map){
		return mapJdbcTemplate.insert(map.getName(), map.getUser().getId());
	}
	
	@Override
	public Boolean mapExists(KingdomMap map){
		return mapJdbcTemplate.mapExists(map.getName(), map.getUser().getId());
	}
	
	@Override
	public List<KingdomMap> getMapList(KingdomUser user){
		return mapJdbcTemplate.getMapList(user.getId());
	}
	
	@Override
	public boolean deleteMap(KingdomMap map){
		return mapJdbcTemplate.deleteMap(map.getId());
	}
	// =========================================
	
	
	// ===== ===== LAYER DATA SERVICES ===== =====
	@Override
	public Integer addLayer(KingdomLayer layer){
		return layerJdbcTemplate.insert(layer.getName(), layer.getStyle(), layer.isVisible(), layer.getGeometryType(), layer.getMap().getId());
	}
	
	@Override
	public Boolean layerExists(KingdomLayer layer){
		return layerJdbcTemplate.layerExists(layer.getName(), layer.getMap().getId());
	}
	
	@Override
	public List<KingdomLayer> getLayerList(KingdomMap map){
		return layerJdbcTemplate.getLayerList(map.getId());
	}
	
	@Override
	public boolean deleteLayer(KingdomLayer layer){
		return layerJdbcTemplate.deleteLayer(layer.getId());
	}
	// ===========================================
}