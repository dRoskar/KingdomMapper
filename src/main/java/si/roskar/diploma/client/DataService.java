package si.roskar.diploma.client;

import java.util.List;

import si.roskar.diploma.shared.KingdomLayer;
import si.roskar.diploma.shared.KingdomMap;
import si.roskar.diploma.shared.KingdomUser;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("data")
public interface DataService extends RemoteService{
	
	Integer addUser(KingdomUser user);
	
	Integer addMap(KingdomMap map);
	
	Integer addLayer(KingdomLayer layer);
	
	Boolean mapExists(KingdomMap map);
	
	Boolean layerExists(KingdomLayer layer);
	
	List<KingdomMap> getMapList(KingdomUser user);
	
	KingdomMap getMap(int id);
	
	List<KingdomLayer> getLayerList(KingdomMap map);
	
	boolean deleteMap(KingdomMap map);
	
	boolean deleteLayer(KingdomLayer layer);
	
	boolean updateLayers(List<KingdomLayer> layers);
	
	void insertMarker(String wmsUrl, String wktGeometry, String label, String description, int layerId);
	
	void insertLine(String wmsUrl, String wktGeometry, String description, int layerId);
	
	void insertPolygon(String wmsUrl, String wktGeometry, String description, int layerId);
	
	boolean slicePolygonGeometry(String wmsUrl, String originalPolygonWktGeometry, String intersectorWktGeometry, String polygonFid);
	
	void updatePolygonGeometry(String wmsUrl, String wktGeometry, String polygonFid);
}