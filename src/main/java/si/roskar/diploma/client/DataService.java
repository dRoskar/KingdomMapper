package si.roskar.diploma.client;

import si.roskar.diploma.shared.KingdomUser;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("data")
public interface DataService extends RemoteService{
	Integer addUser(KingdomUser user);
}
