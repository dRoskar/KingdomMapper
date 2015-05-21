package si.roskar.diploma.server;

import java.util.List;

import org.gemma.si.NetIO;

import si.roskar.diploma.client.DataService;
import si.roskar.diploma.server.DAO.LayerJDBCTemplate;
import si.roskar.diploma.server.DAO.MapJDBCTemplate;
import si.roskar.diploma.server.DAO.UserJDBCTemplate;
import si.roskar.diploma.server.DB.DBSource;
import si.roskar.diploma.shared.KingdomLayer;
import si.roskar.diploma.shared.KingdomMap;
import si.roskar.diploma.shared.KingdomUser;
import si.roskar.diploma.shared.Tools;

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
	
	
	// ===== ===== WFS-T ===== =====
	public void insertMarker(String wmsUrl, double longitude, double latitude, String label, String description, int layerId){
		NetIO netIo = new NetIO();
		
		// encode special characters
		label = Tools.encodeToNumericCharacterReference(label);
		description = Tools.encodeToNumericCharacterReference(description);
		
		// assemble request XML
//		String xml = "<wfs:Transaction service=\"WFS\" version=\"1.0.0\"\r\n" + 
//				"  xmlns:wfs=\"http://www.opengis.net/wfs\"\r\n" + 
//				"  xmlns:kingdom=\"http://kingdom.si\"\r\n" + 
//				"  xmlns:gml=\"http://www.opengis.net/gml\"\r\n" + 
//				"  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n" + 
//				"  xsi:schemaLocation=\"http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.0.0/WFS-transaction.xsd http://localhost:8080/geoserver/wfs/DescribeFeatureType?typename=topp:tasmania_roads\">\r\n" + 
//				"  <wfs:Insert>\r\n" + 
//				"    <kingdom:point>\r\n" +
//				"	   <kingdom:label>" + label + "</kingdom:label>" + 
//				"      <kingdom:description>" + description + "</kingdom:description>\r\n" + 
//				"      <geometry>\r\n" + 
//				"        <gml:Point srsName=\"http://www.opengis.net/gml/srs/epsg.xml#2170\">\r\n" + 
//				"          <gml:coordinates>" + longitude + "," + latitude + "</gml:coordinates>\r\n" + 
//				"        </gml:Point>\r\n" + 
//				"      </geometry>\r\n" + 
//				"    </kingdom:point>\r\n" + 
//				"  </wfs:Insert>\r\n" + 
//				"</wfs:Transaction>";
		
		try{
			netIo.post(wmsUrl, xml);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	// =============================
}