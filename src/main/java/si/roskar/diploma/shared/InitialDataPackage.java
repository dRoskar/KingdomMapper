package si.roskar.diploma.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class InitialDataPackage implements IsSerializable{
	
	private KingdomUser	currentUser	= null;
	private String		wmsSoruce	= null;
	
	public InitialDataPackage(){
		
	}
	
	public InitialDataPackage(KingdomUser currentUser, String wmsSoruce){
		this.currentUser = currentUser;
		this.wmsSoruce = wmsSoruce;
	}

	public KingdomUser getCurrentUser(){
		return currentUser;
	}

	public void setCurrentUser(KingdomUser currentUser){
		this.currentUser = currentUser;
	}

	public String getWmsSoruce(){
		return wmsSoruce;
	}

	public void setWmsSoruce(String wmsSoruce){
		this.wmsSoruce = wmsSoruce;
	}
}
