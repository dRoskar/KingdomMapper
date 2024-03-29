package si.roskar.diploma.client;

import java.util.List;

import si.roskar.diploma.shared.InitialDataPackage;
import si.roskar.diploma.shared.KingdomFeature;
import si.roskar.diploma.shared.KingdomLayer;
import si.roskar.diploma.shared.KingdomMap;
import si.roskar.diploma.shared.KingdomUser;
import si.roskar.diploma.shared.KingdomVectorFeature;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("data")
public interface DataService extends RemoteService{
	
	Integer addUser(KingdomUser user);
	
	boolean setUserLastMap(int lastMapId, int userId);
	
	Integer addMap(KingdomMap map);
	
	Integer addLayer(KingdomLayer layer);
	
	Boolean mapExists(KingdomMap map);
	
	Boolean layerExists(KingdomLayer layer);
	
	List<KingdomMap> getMapList(KingdomUser user);
	
	KingdomMap getMap(int id);
	
	List<KingdomLayer> getLayerList(KingdomMap map);
	
	boolean deleteMap(KingdomMap map);
	
	KingdomMap updateMapName(KingdomMap map);
	
	boolean updateMapPreviousView(KingdomMap map);
	
	boolean deleteLayer(KingdomLayer layer);
	
	boolean updateLayers(List<KingdomLayer> layers);
	
	KingdomLayer updateLayerStyle(KingdomLayer layer);
	
	boolean updateLayerName(KingdomLayer layer);
	
	boolean insertMarker(String wktGeometry, String label, String description, int layerId);
	
	boolean insertLine(String wktGeometry, int layerId);
	
	boolean insertPolygon(String wktGeometry, int layerId);
	
	boolean slicePolygonGeometry(String originalPolygonWktGeometry, String intersectorWktGeometry, String polygonFid);
	
	boolean bindPolygonGeometries(List<KingdomVectorFeature> partakigFeatures, String newGeometryWkt);
	
	boolean updatePolygonGeometry(String wktGeometry, String polygonFid);
	
	List<KingdomFeature> getFeatureInfo(KingdomLayer layer, String bbox, int width, int height, int pixelX, int pixelY);
	
	boolean updateFeatureInfo(KingdomLayer layer, String label, String description, String featureId);
	
	InitialDataPackage getInitialDataPackage();
}