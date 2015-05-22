package si.roskar.diploma.shared;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class KingdomMap implements IsSerializable{
	
	private int					id;
	private String				name	= null;
	private MapSize				mapSize	= null;
	private List<KingdomLayer>	layers	= null;
	private boolean				empty	= false;
	private KingdomUser			user	= null;
	
	public KingdomMap(){
		layers = new ArrayList<KingdomLayer>();
	}
	
	public KingdomMap(String name, List<KingdomLayer> layers, KingdomUser user){
		this.name = name;
		this.layers = layers;
		this.user = user;
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
	
	public MapSize getMapSize(){
		return mapSize;
	}

	public void setMapSize(MapSize mapSize){
		this.mapSize = mapSize;
	}

	public List<KingdomLayer> getLayers(){
		return layers;
	}
	
	public void setLayers(List<KingdomLayer> layers){
		this.layers = layers;
	}
	
	public void addLayer(KingdomLayer layer){
		layers.add(layer);
	}
	
	public boolean isEmpty(){
		return empty;
	}

	public void setEmpty(boolean empty){
		this.empty = empty;
	}

	public KingdomUser getUser(){
		return user;
	}
	
	public void setUser(KingdomUser user){
		this.user = user;
	}
}
