package si.roskar.diploma.server;

import si.roskar.diploma.client.DataService;
import si.roskar.diploma.server.DAO.UserJDBCTemplate;
import si.roskar.diploma.server.DB.DBSource;
import si.roskar.diploma.shared.KingdomUser;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DataServiceImpl extends RemoteServiceServlet implements DataService{
	
	@Override
	public Integer addUser(KingdomUser user){
		
		UserJDBCTemplate userJdbctTemplate = new UserJDBCTemplate();
		userJdbctTemplate.setDataSource(DBSource.getDataSource());
		userJdbctTemplate.insert(user.getName(), user.getPassword());
		
		return 666;
	}
}