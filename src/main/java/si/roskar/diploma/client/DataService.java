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
	
	List<KingdomMap> getMapList(KingdomUser user);
	
	boolean deleteMap(KingdomMap map);
}