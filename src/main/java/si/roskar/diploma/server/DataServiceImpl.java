package si.roskar.diploma.server;

import java.util.ArrayList;
import java.util.List;

import org.gemma.si.NetIO;
import org.springframework.security.core.context.SecurityContextHolder;

import si.roskar.diploma.client.DataService;
import si.roskar.diploma.server.DAO.LayerJDBCTemplate;
import si.roskar.diploma.server.DAO.MapJDBCTemplate;
import si.roskar.diploma.server.DAO.UserJDBCTemplate;
import si.roskar.diploma.server.DAO.VectorJDBCTemplate;
import si.roskar.diploma.server.DB.DBSource;
import si.roskar.diploma.shared.GeometryType;
import si.roskar.diploma.shared.KingdomFeature;
import si.roskar.diploma.shared.KingdomGridLayer;
import si.roskar.diploma.shared.KingdomLayer;
import si.roskar.diploma.shared.KingdomMap;
import si.roskar.diploma.shared.KingdomUser;
import si.roskar.diploma.shared.KingdomVectorFeature;
import si.roskar.diploma.shared.Tools;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.gml2.GMLWriter;

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
	
	@Override
	public KingdomUser getCurrentUser(){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		List<KingdomUser> users = userJdbcTemplate.getUserByName(username);
		
		if(!users.isEmpty()){
			return users.get(0);
		}
		
		return null;
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
		boolean successLayers = true;
		boolean successMap;
		
		// remove all of maps layers
		for(KingdomLayer layer : map.getLayers()){
			if(!deleteLayer(layer)){
				successLayers = false;
			}
		}
		
		// remove map
		successMap = mapJdbcTemplate.deleteMap(map.getId());
		
		return successLayers && successMap;
	}
	
	@Override
	public KingdomMap getMap(int id){
		return mapJdbcTemplate.getMap(id);
	}
	
	@Override
	public KingdomMap updateMapName(KingdomMap map){
		mapJdbcTemplate.updateMapName(map.getId(), map.getName());
		
		return map;
	}
	
	@Override
	public boolean updateMapPreviousView(KingdomMap map){
		return mapJdbcTemplate.updatePreviousView(map.getId(), map.getPreviousViewllx(), map.getPreviousViewlly(), map.getPreviousViewurx(), map.getPreviousViewury(), map.getPreviousZoomLevel());
	}
	
	// =========================================
	
	// ===== ===== LAYER DATA SERVICES ===== =====
	@Override
	public Integer addLayer(KingdomLayer layer){
		return layerJdbcTemplate.insert(layer.getName(), layer.getStyle(), layer.getOpacity(), layer.isVisible(), layer.getGeometryType(), layer.getZIndex(), layer.getColor(), layer.getLabelColor(),
				layer.getSize(), layer.getFillColor(), layer.getLabelFillColor(), layer.getStrokeWidth(), layer.getStrokeOpacity(), layer.getFillOpacity(), layer.getMaxScale(), layer.getMinScale(),
				layer.getMarkerImage(), layer.getMap().getId());
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
		if(layer.getGeometryType().equals(GeometryType.POINT) || layer.getGeometryType().equals(GeometryType.MARKER)){
			successFeatures = vectorJdbcTemplate.deletePointsInLayer(layer.getId());
		}else if(layer.getGeometryType().equals(GeometryType.LINE)){
			successFeatures = vectorJdbcTemplate.deleteLinesInLayer(layer.getId());
		}else if(layer.getGeometryType().equals(GeometryType.POLYGON)){
			successFeatures = vectorJdbcTemplate.deletePoligonsInLayer(layer.getId());
		}else{
			successFeatures = false;
		}
		
		// remove layer itself
		successLayer = layerJdbcTemplate.deleteLayer(layer.getId());
		
		return successFeatures && successLayer;
	}
	
	@Override
	public boolean updateLayers(List<KingdomLayer> layers){
		boolean success = true;
		for(KingdomLayer layer : layers){
			if(!(layer instanceof KingdomGridLayer)){
				if(!layerJdbcTemplate.updateLayerState(layer.getId(), layer.isVisible(), layer.getZIndex(), layer.getOpacity())){
					success = false;
				}
			}
		}
		
		return success;
	}
	
	@Override
	public KingdomLayer updateLayerStyle(KingdomLayer layer){
		layerJdbcTemplate.updateLayerStyle(layer.getId(), layer.getStyle(), layer.getColor(), layer.getLabelColor(), layer.getSize(), layer.getFillColor(), layer.getLabelFillColor(),
				layer.getStrokeWidth(), layer.getFillOpacity(), layer.getStrokeOpacity(), layer.getMaxScale(), layer.getMinScale(), layer.getMarkerImage(), layer.getTextureImage());
		
		return layer;
	}
	
	@Override
	public boolean updateLayerName(KingdomLayer layer){
		return layerJdbcTemplate.updateLayerName(layer.getId(), layer.getName());
	}
	
	// ===========================================
	
	// ===== ===== WFS-T ===== =====
	public boolean insertMarker(String wktGeometry, String label, String description, int layerId){
		String wmsUrl = GeoserverSource.getWmsUrl();
		
		NetIO netIo = new NetIO();
		
		// encode special characters
		label = Tools.encodeToNumericCharacterReference(label);
		description = Tools.encodeToNumericCharacterReference(description);
		
		// escape apostrophes
		label = label.replace("'", "''");
		description = description.replace("'", "''");
		
		// assemble request XML
		//@formatter:off
		String xml = "<wfs:Transaction service=\"WFS\" version=\"1.0.0\"\r\n"
				+ "  xmlns:wfs=\"http://www.opengis.net/wfs\"\r\n"
				+ "  xmlns:kingdom=\"http://kingdom.si\"\r\n"
				+ "  xmlns:gml=\"http://www.opengis.net/gml\"\r\n"
				+ "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n"
				+ "  xsi:schemaLocation=\"http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.0.0/WFS-transaction.xsd http://localhost:8080/geoserver/wfs/DescribeFeatureType?typename=topp:tasmania_roads\">\r\n"
				+ "  <wfs:Insert>\r\n" 
				+ "    <kingdom:point>\r\n" 
				+ "	   <kingdom:label>" + label + "</kingdom:label>" 
				+ "      <kingdom:description>" + description + "</kingdom:description>\r\n"
				+ "      <kingdom:layer_id>" + layerId + "</kingdom:layer_id>\r\n" 
				+ "      <geometry>" + Tools.wktToGml(wktGeometry) + "</geometry>\r\n" 
				+ "    </kingdom:point>\r\n"
				+ "  </wfs:Insert>\r\n" 
				+ "</wfs:Transaction>";
		//@formatter:on
		
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
			
			if(responseString.contains("ServiceException")){
				return false;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return true;
	}
	
	public boolean insertLine(String wktGeometry, int layerId){
		String wmsUrl = GeoserverSource.getWmsUrl();
		
		NetIO netIo = new NetIO();
		
		// assemble request XML
		//@formatter:off
		String xml = "<wfs:Transaction service=\"WFS\" version=\"1.0.0\"\r\n"
				+ "  xmlns:wfs=\"http://www.opengis.net/wfs\"\r\n"
				+ "  xmlns:kingdom=\"http://kingdom.si\"\r\n"
				+ "  xmlns:gml=\"http://www.opengis.net/gml\"\r\n"
				+ "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n"
				+ "  xsi:schemaLocation=\"http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.0.0/WFS-transaction.xsd http://localhost:8080/geoserver/wfs/DescribeFeatureType?typename=topp:tasmania_roads\">\r\n"
				+ "  <wfs:Insert>\r\n" 
				+ "    <kingdom:line>\r\n" 
				+ "      <kingdom:layer_id>" + layerId + "</kingdom:layer_id>\r\n" 
				+ "      <geometry>" + Tools.wktToGml(wktGeometry) + "</geometry>\r\n" 
				+ "    </kingdom:line>\r\n" 
				+ "  </wfs:Insert>\r\n"
				+ "</wfs:Transaction>";
		//@formatter:on
		
		try{
			byte[] response = netIo.post(wmsUrl, xml);
			
			String responseString = new String(response);
			
			if(responseString.contains("ServiceException")){
				return false;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return true;
	}
	
	public boolean insertPolygon(String wktGeometry, int layerId){
		String wmsUrl = GeoserverSource.getWmsUrl();
		
		NetIO netIo = new NetIO();
		
		// assemble request XML
		//@formatter:off
		String xml = "<wfs:Transaction service=\"WFS\" version=\"1.0.0\"\r\n"
				+ "  xmlns:wfs=\"http://www.opengis.net/wfs\"\r\n"
				+ "  xmlns:kingdom=\"http://kingdom.si\"\r\n"
				+ "  xmlns:gml=\"http://www.opengis.net/gml\"\r\n"
				+ "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n"
				+ "  xsi:schemaLocation=\"http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.0.0/WFS-transaction.xsd http://localhost:8080/geoserver/wfs/DescribeFeatureType?typename=topp:tasmania_roads\">\r\n"
				+ "  <wfs:Insert>\r\n" 
				+ "    <kingdom:polygon>\r\n"
				+ "      <kingdom:layer_id>" + layerId + "</kingdom:layer_id>\r\n" 
				+ "      <geometry> " + Tools.wktToGml(wktGeometry) + "</geometry>\r\n" 
				+ "    </kingdom:polygon>\r\n" 
				+ "  </wfs:Insert>\r\n" 
				+ "</wfs:Transaction>";
		//@formatter:on
		
		try{
			byte[] response = netIo.post(wmsUrl, xml);
			
			String responseString = new String(response);
			
			if(responseString.contains("ServiceException")){
				return false;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return true;
	}
	
	public boolean updatePolygonGeometry(String gmlGeometry, String polygonFid){
		String wmsUrl = GeoserverSource.getWmsUrl();
		
		NetIO netIo = new NetIO();
		
		// assemble request XML
		//@formatter:off
		String xml = "<wfs:Transaction service=\"WFS\" version=\"1.0.0\"\r\n" + 
				"  xmlns:ogc=\"http://www.opengis.net/ogc\"\r\n" + 
				"  xmlns:kingdom=\"http://kingdom.si\"\r\n" +
				"  xmlns:gml=\"http://www.opengis.net/gml\"\r\n" +
				"  xmlns:wfs=\"http://www.opengis.net/wfs\">\r\n" +
				"  <wfs:Update typeName=\"kingdom:polygon\">\r\n" + 
				"    <wfs:Property>\r\n" + 
				"      <wfs:Name>geometry</wfs:Name>\r\n" + 
				"      <wfs:Value>" + gmlGeometry + "</wfs:Value>\r\n" +
				"    </wfs:Property>\r\n" + 
				"    <ogc:Filter>\r\n" + 
				"      <ogc:FeatureId fid=\"" + polygonFid + "\"/>\r\n" +
				"    </ogc:Filter>\r\n" + 
				"  </wfs:Update>\r\n" + 
				"</wfs:Transaction>";
		//@formatter:on
		
		try{
			byte[] response = netIo.post(wmsUrl, xml);
			
			String responseString = new String(response);
			
			if(responseString.contains("ServiceException")){
				return false;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return true;
	}
	
	public boolean updateFeatureInfo(KingdomLayer layer, String label, String description, String featureId){
		String wmsUrl = GeoserverSource.getWmsUrl();
		
		NetIO netIo = new NetIO();
		
		// encode special characters
		label = Tools.encodeToNumericCharacterReference(label);
		description = Tools.encodeToNumericCharacterReference(description);
		
		// escape apostrophes
		label = label.replace("'", "''");
		description = description.replace("'", "''");
		
		// assemble request XML
		//@formatter:off
		String xml = "<wfs:Transaction service=\"WFS\" version=\"1.0.0\"\r\n" + 
				"  xmlns:ogc=\"http://www.opengis.net/ogc\"\r\n" + 
				"  xmlns:kingdom=\"http://kingdom.si\"\r\n" +
				"  xmlns:gml=\"http://www.opengis.net/gml\"\r\n" +
				"  xmlns:wfs=\"http://www.opengis.net/wfs\">\r\n" +
				"  <wfs:Update typeName=\"" + layer.getServerName() + "\">\r\n" + 
				"  <wfs:Property>\r\n" + 
				"    <wfs:Name>label</wfs:Name>\r\n" + 
				"      <wfs:Value>" + label + "</wfs:Value>\r\n" +
				"    </wfs:Property>\r\n" + 
				"    <wfs:Property>\r\n" + 
				"      <wfs:Name>description</wfs:Name>\r\n" + 
				"        <wfs:Value>" + description + "</wfs:Value>\r\n" +
				"      </wfs:Property>\r\n" + 
				"      <ogc:Filter>\r\n" + 
				"        <ogc:FeatureId fid=\"" + featureId + "\"/>\r\n" +
				"      </ogc:Filter>\r\n" + 
				"    </wfs:Update>\r\n" + 
				"  </wfs:Transaction>";
		//@formatter:on
		
		try{
			byte[] response = netIo.post(wmsUrl, xml);
			
			String responseString = new String(response);
			
			if(responseString.contains("ServiceException")){
				return false;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return true;
	}
	
	public boolean deletePolygon(String polygonFid){
		String wmsUrl = GeoserverSource.getWmsUrl();
		
		NetIO netIo = new NetIO();
		
		// assemble request XML
		//@formatter:off
		String xml = "<wfs:Transaction service=\"WFS\" version=\"1.0.0\"\r\n" + 
				"  xmlns:ogc=\"http://www.opengis.net/ogc\"\r\n" + 
				"  xmlns:kingdom=\"http://kingdom.si\"\r\n" +
				"  xmlns:gml=\"http://www.opengis.net/gml\"\r\n" +
				"  xmlns:wfs=\"http://www.opengis.net/wfs\">\r\n" +
				"  <wfs:Delete typeName=\"kingdom:polygon\">\r\n" + 
				"    <ogc:Filter>\r\n" + 
				"      <ogc:FeatureId fid=\"" + polygonFid + "\"/>\r\n" + 
				"    </ogc:Filter>\r\n" + 
				"  </wfs:Delete>\r\n" + 
				"</wfs:Transaction>";
		//@formatter:on
		
		try{
			byte[] response = netIo.post(wmsUrl, xml);
			
			String responseString = new String(response);
			
			if(responseString.contains("ServiceException")){
				return false;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return true;
	}
	
	// =============================
	
	// ===== ===== UTIL ===== =====
	public boolean slicePolygonGeometry(String originalPolygonWktGeometry, String intersectorWktGeometry, String polygonFid){
		
		// get geometry difference
		WKTReader wktReader = new WKTReader();
		GMLWriter gmlWriter = new GMLWriter();
		Geometry originalPolygon = null;
		Geometry intersector = null;
		Geometry difference = null;
		try{
			originalPolygon = wktReader.read(originalPolygonWktGeometry);
			intersector = wktReader.read(intersectorWktGeometry);
			
			difference = originalPolygon.difference(intersector);
			
			// if intersector completely erases the original polygon, remove the
			// original polygon
			if(difference.isEmpty()){
				deletePolygon(polygonFid);
				return true;
			}else if(!difference.equalsExact(originalPolygon)){
				updatePolygonGeometry(gmlWriter.write(difference), polygonFid);
				return true;
			}
			
			return false;
			
		}catch(ParseException e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean bindPolygonGeometries(List<KingdomVectorFeature> partakigFeatures, String newGeometryWkt){
		WKTReader wktReader = new WKTReader();
		GMLWriter gmlWriter = new GMLWriter();
		Geometry newGeometry = null;
		
		// get get all geometry transformations
		try{
			newGeometry = wktReader.read(newGeometryWkt);
			Geometry unionGeometry = newGeometry;
			List<KingdomVectorFeature> intersectingFeatures = new ArrayList<KingdomVectorFeature>();
			
			// get intersecting features
			for(KingdomVectorFeature feature : partakigFeatures){
				Geometry featureGeometry = wktReader.read(feature.getWktGeometry());
				
				// bind geometries
				if(featureGeometry.intersects(newGeometry)){
					unionGeometry = unionGeometry.union(featureGeometry);
					intersectingFeatures.add(feature);
				}
			}
			
			// update new geometry to first of the intersecting features; delete
			// the rest
			boolean updated = false;
			for(KingdomVectorFeature feature : intersectingFeatures){
				if(!updated){
					updatePolygonGeometry(gmlWriter.write(unionGeometry), feature.getFID());
					updated = true;
				}else{
					deletePolygon(feature.getFID());
				}
			}
			
			return true;
			
		}catch(ParseException e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	// info
	public List<KingdomFeature> getFeatureInfo(KingdomLayer layer, String bbox, int width, int height, int pixelX, int pixelY){
		String wmsUrl = GeoserverSource.getWmsUrl();
		
		List<KingdomFeature> features = new ArrayList<KingdomFeature>();
		byte[] result = null;
		
		NetIO netIo = new NetIO();
		
		bbox = bbox.replace(",", "%2C");
		
		// assemble request url
		//@formatter:off
		wmsUrl += "?request=GetFeatureInfo"
				+ "&service=WMS"
				+ "&version=1.1.1"
				+ "&layers=" + layer.getServerName()
				+ "&query_layers=" + layer.getServerName()
				+ "&styles="
				+ "&srs=EPSG%3A4326"
 				+ "&format=jpeg"
				+ "&info_format=application/json"
 				+ "&exceptions=application/json"
				+ "&bbox=" + bbox
				+ "&width=" + width
				+ "&height=" + height
				+ "&x=" + pixelX
				+ "&y=" + pixelY
				+ "&cql_filter=layer_id%3D" + layer.getId()
				+ "&feature_count=50";
				
		//@formatter:on
		
		// execute request
		try{
			result = netIo.get(wmsUrl);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		// parse result
		JsonObject jsonObject = new JsonParser().parse(new String(result)).getAsJsonObject();
		
		if(!jsonObject.has("exceptions")){
			JsonArray jsonFeatures = jsonObject.getAsJsonArray("features");
			
			for(JsonElement element : jsonFeatures){
				String featureId = element.getAsJsonObject().get("id").getAsString();
				
				jsonObject = element.getAsJsonObject().get("properties").getAsJsonObject();
				
				String label = "";
				if(!jsonObject.get("label").isJsonNull()){
					label = jsonObject.get("label").getAsString();
				}
				
				String description = "";
				if(!jsonObject.get("description").isJsonNull()){
					description = jsonObject.get("description").getAsString();
				}
				
				KingdomFeature kingdomFeature = new KingdomFeature(featureId, layer, label, description);
				
				features.add(kingdomFeature);
			}
		}
		
		return features;
	}
	// =============================
}