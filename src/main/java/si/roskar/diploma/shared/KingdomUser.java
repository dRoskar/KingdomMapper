package si.roskar.diploma.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class KingdomUser implements IsSerializable{
	
	private int		id;
	private String	name		= null;
	private String	password	= null;
	
	public KingdomUser(){
		
	}
	
	public KingdomUser(int id, String name, String password){
		this.id = id;
		this.name = name;
		this.password = password;
	}

	public int getId(){
		return id;
	}

	public void setId(int id){
		this.id = id;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getPassword(){
		return password;
	}

	public void setPassword(String password){
		this.password = password;
	}
}
