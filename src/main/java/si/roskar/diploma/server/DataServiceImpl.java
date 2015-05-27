package si.roskar.diploma.server;

import java.util.List;

import org.gemma.si.NetIO;

import si.roskar.diploma.client.DataService;
import si.roskar.diploma.server.DAO.LayerJDBCTemplate;
import si.roskar.diploma.server.DAO.MapJDBCTemplate;
import si.roskar.diploma.server.DAO.UserJDBCTemplate;
import si.roskar.diploma.server.DAO.VectorJDBCTemplate;
import si.roskar.diploma.server.DB.DBSource;
import si.roskar.diploma.shared.GeometryType;
import si.roskar.diploma.shared.KingdomLayer;
import si.roskar.diploma.shared.KingdomMap;
import si.roskar.diploma.shared.KingdomUser;
import si.roskar.diploma.shared.Tools;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DataServiceImpl extends RemoteServiceServlet implements DataService{
	
	private UserJDBCTemplate	userJdbcTemplate	= null;
	private MapJDBCTemplate		mapJdbcTemplate		= null;
	private LayerJDBCTemplate	layerJdbcTemplate	= null;
	private VectorJDBCTemplate	vectorJdbcTemplate	= null;
	
	public DataServiceImpl(){
		userJdbcTemplate = new UserJDBCTemplate();
		userJdbcTemplate.setDataSource(DBSource.getDataSource());
		
		mapJdbcTemplate = new MapJDBCTemplate();
		mapJdbcTemplate.setDataSource(DBSource.getDataSource());
		
		layerJdbcTemplate = new LayerJDBCTemplate();
		layerJdbcTemplate.setDataSource(DBSource.getDataSource());
		
		vectorJdbcTemplate = new VectorJDBCTemplate();
		vectorJdbcTemplate.setDataSource(DBSource.getDataSource());
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
		boolean successFeatures;
		boolean successLayer;
		// remove all of layers features
		if(layer.getGeometryType().equals(GeometryType.POINT)){
			successFeatures = vectorJdbcTemplate.deletePointsInLayer(layer.getId());
		}
		else if(layer.getGeometryType().equals(GeometryType.LINE)){
			successFeatures = vectorJdbcTemplate.deleteLinesInLayer(layer.getId());
		}
		else if(layer.getGeometryType().equals(GeometryType.POLYGON)){
			successFeatures = vectorJdbcTemplate.deletePoligonsInLayer(layer.getId());
		}
		else{
			successFeatures = false;
		}
		
		// remove layer itself
		successLayer = layerJdbcTemplate.deleteLayer(layer.getId());
		
		return successFeatures && successLayer;
	}
	
	// ===========================================
	
	// ===== ===== WFS-T ===== =====
	public void insertMarker(String wmsUrl, String wktGeometry, String label, String description, int layerId){
		NetIO netIo = new NetIO();
		
		// encode special characters
		label = Tools.encodeToNumericCharacterReference(label);
		description = Tools.encodeToNumericCharacterReference(description);
		
		// escape apostrophes
		label = label.replace("'", "''");
		description = description.replace("'", "''");
		
		// assemble request XML
		String xml = "<wfs:Transaction service=\"WFS\" version=\"1.0.0\"\r\n"
				+ "  xmlns:wfs=\"http://www.opengis.net/wfs\"\r\n"
				+ "  xmlns:kingdom=\"http://kingdom.si\"\r\n"
				+ "  xmlns:gml=\"http://www.opengis.net/gml\"\r\n"
				+ "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n"
				+ "  xsi:schemaLocation=\"http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.0.0/WFS-transaction.xsd http://localhost:8080/geoserver/wfs/DescribeFeatureType?typename=topp:tasmania_roads\">\r\n"
				+ "  <wfs:Insert>\r\n" + "    <kingdom:point>\r\n" + "	   <kingdom:label>" + label + "</kingdom:label>" + "      <kingdom:description>" + description + "</kingdom:description>\r\n"
				+ "      <kingdom:layer_id>" + layerId + "</kingdom:layer_id>\r\n" + "      <geometry>\r\n" + "        <gml:Point srsName=\"http://www.opengis.net/gml/srs/epsg.xml#4326\">\r\n"
				+ "          <gml:coordinates>" + Tools.wktToGmlFriendly(wktGeometry) + "</gml:coordinates>\r\n" + "        </gml:Point>\r\n" + "      </geometry>\r\n" + "    </kingdom:point>\r\n"
				+ "  </wfs:Insert>\r\n" + "</wfs:Transaction>";
		
		// decode description (it's a multiLineString wink* wink*)
		// description = description.substring(15);
		// description = description.substring(1, description.length() - 1);
		//
		// description = description.replace("(", "");
		// String[] lines = description.split("\\)");
		//
		//
		//
		// String xml = "<wfs:Transaction service=\"WFS\" version=\"1.0.0\"\r\n"
		// +
		// "  xmlns:wfs=\"http://www.opengis.net/wfs\"\r\n" +
		// "  xmlns:kingdom=\"http://kingdom.si\"\r\n" +
		// "  xmlns:gml=\"http://www.opengis.net/gml\"\r\n" +
		// "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n" +
		// "  xsi:schemaLocation=\"http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.0.0/WFS-transaction.xsd http://www.openplans.org/topp http://localhost:8080/geoserver/wfs/DescribeFeatureType?typename=topp:tasmania_roads\">\r\n"
		// +
		// "  <wfs:Insert>\r\n" +
		// "    <kingdom:line>\r\n" +
		// "      <kingdom:geometry>\r\n" +
		// "        <gml:MultiLineString srsName=\"http://www.opengis.net/gml/srs/epsg.xml#4326\">\r\n";
		// for(String line : lines){
		// if(line.startsWith(",")){
		// line = line.substring(1);
		// }
		// line = line.replace(", ", "|");
		// line = line.replace(" ", ",");
		// line = line.replace("|", " ");
		//
		// xml = xml + "          <gml:lineStringMember>\r\n" +
		// "            <gml:LineString>\r\n" +
		// "              <gml:coordinates decimal=\".\" cs=\",\" ts=\" \">\r\n"
		// +
		// line +
		// "              </gml:coordinates>\r\n" +
		// "            </gml:LineString>\r\n" +
		// "          </gml:lineStringMember>\r\n";
		// }
		//
		// xml = xml + "        </gml:MultiLineString>\r\n" +
		// "      </kingdom:geometry>\r\n" +
		// "      <kingdom:description>grid that comes with every new map</kingdom:description>\r\n"
		// +
		// "    </kingdom:line>\r\n" +
		// "  </wfs:Insert>\r\n" +
		// "</wfs:Transaction>";
		
		try{
			byte[] response = netIo.post(wmsUrl, xml);
			
			String responseString = new String(response);
			
			System.out.println(responseString);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void insertLine(String wmsUrl, String wktGeometry, String description, int layerId){
		NetIO netIo = new NetIO();
		
		// encode special characters
		description = Tools.encodeToNumericCharacterReference(description);
		
		// escape apostrophes
		description = description.replace("'", "''");
		
		// assemble request XML
		String xml = "<wfs:Transaction service=\"WFS\" version=\"1.0.0\"\r\n"
				+ "  xmlns:wfs=\"http://www.opengis.net/wfs\"\r\n"
				+ "  xmlns:kingdom=\"http://kingdom.si\"\r\n"
				+ "  xmlns:gml=\"http://www.opengis.net/gml\"\r\n"
				+ "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n"
				+ "  xsi:schemaLocation=\"http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.0.0/WFS-transaction.xsd http://localhost:8080/geoserver/wfs/DescribeFeatureType?typename=topp:tasmania_roads\">\r\n"
				+ "  <wfs:Insert>\r\n" + "    <kingdom:line>\r\n" + "      <kingdom:description>" + description + "</kingdom:description>\r\n" + "      <kingdom:layer_id>" + layerId
				+ "</kingdom:layer_id>\r\n" + "      <geometry>\r\n" + "        <gml:LineString srsName=\"http://www.opengis.net/gml/srs/epsg.xml#4326\">\r\n" + "          <gml:coordinates>"
				+ Tools.wktToGmlFriendly(wktGeometry) + "</gml:coordinates>\r\n" + "        </gml:LineString>\r\n" + "      </geometry>\r\n" + "    </kingdom:line>\r\n" + "  </wfs:Insert>\r\n"
				+ "</wfs:Transaction>";
		
		try{
			byte[] response = netIo.post(wmsUrl, xml);
			
			String responseString = new String(response);
			
			System.out.println(responseString);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void insertPolygon(String wmsUrl, String wktGeometry, String description, int layerId){
		NetIO netIo = new NetIO();
		
		// encode special characters
		description = Tools.encodeToNumericCharacterReference(description);
		
		// escape apostrophes
		description = description.replace("'", "''");
		
		// assemble request XML
		String xml = "<wfs:Transaction service=\"WFS\" version=\"1.0.0\"\r\n"
				+ "  xmlns:wfs=\"http://www.opengis.net/wfs\"\r\n"
				+ "  xmlns:kingdom=\"http://kingdom.si\"\r\n"
				+ "  xmlns:gml=\"http://www.opengis.net/gml\"\r\n"
				+ "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n"
				+ "  xsi:schemaLocation=\"http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.0.0/WFS-transaction.xsd http://localhost:8080/geoserver/wfs/DescribeFeatureType?typename=topp:tasmania_roads\">\r\n"
				+ "  <wfs:Insert>\r\n" + "    <kingdom:polygon>\r\n" + "      <kingdom:description>" + description + "</kingdom:description>\r\n" + "      <kingdom:layer_id>" + layerId
				+ "</kingdom:layer_id>\r\n" + "      <geometry>\r\n" + "        <gml:Polygon srsName=\"http://www.opengis.net/gml/srs/epsg.xml#4326\">\r\n" + "          <gml:outerBoundaryIs>"
				+ "            <gml:LinearRing>" + "              <gml:coordinates>" + Tools.wktToGmlFriendly(wktGeometry) + "</gml:coordinates>\r\n" + "            </gml:LinearRing>"
				+ "          </gml:outerBoundaryIs>" + "        </gml:Polygon>\r\n" + "      </geometry>\r\n" + "    </kingdom:polygon>\r\n" + "  </wfs:Insert>\r\n" + "</wfs:Transaction>";
		
		try{
			byte[] response = netIo.post(wmsUrl, xml);
			
			String responseString = new String(response);
			
			System.out.println(responseString);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	// =============================
}