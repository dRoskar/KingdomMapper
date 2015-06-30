package si.roskar.diploma.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserHash implements IsSerializable{
	private String hash = null;

	public UserHash(){
		
	}
	
	public UserHash(String hash){
		super();
		this.hash = hash;
	}

	public String getHash(){
		return hash;
	}

	public void setHash(String hash){
		this.hash = hash;
	}
}
