package si.roskar.diploma.shared;

import java.util.ArrayList;
import java.util.List;

import org.gwtopenmaps.openlayers.client.Bounds;

import com.google.gwt.user.client.rpc.IsSerializable;

public class KingdomMap implements IsSerializable{
	
	private int					id;
	private String				name				= null;
	private MapSize				mapSize				= null;
	private List<KingdomLayer>	layers				= null;
	private KingdomUser			user				= null;
	private double[]			scales				= null;
	private Double				previousViewllx		= null;
	private Double				previousViewlly		= null;
	private Double				previousViewurx		= null;
	private Double				previousViewury		= null;
	private int					previousZoomLevel	= 0;
	
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
	
	public KingdomUser getUser(){
		return user;
	}
	
	public void setUser(KingdomUser user){
		this.user = user;
	}
	
	public double[] getScales(){
		return scales;
	}
	
	public void setScales(double[] scales){
		this.scales = scales;
	}
	
	public Double getPreviousViewllx(){
		return previousViewllx;
	}
	
	public void setPreviousViewllx(Double previousViewllx){
		this.previousViewllx = previousViewllx;
	}
	
	public Double getPreviousViewlly(){
		return previousViewlly;
	}
	
	public void setPreviousViewlly(Double previousViewlly){
		this.previousViewlly = previousViewlly;
	}
	
	public Double getPreviousViewurx(){
		return previousViewurx;
	}
	
	public void setPreviousViewurx(Double previousViewurx){
		this.previousViewurx = previousViewurx;
	}
	
	public Double getPreviousViewury(){
		return previousViewury;
	}
	
	public void setPreviousViewury(Double previousViewury){
		this.previousViewury = previousViewury;
	}
	
	public int getPreviousZoomLevel(){
		return previousZoomLevel;
	}

	public void setPreviousZoomLevel(int previousZoomLevel){
		this.previousZoomLevel = previousZoomLevel;
	}

	public Bounds getPreviousView(){
		if(previousViewllx != null && previousViewlly != null && previousViewurx != null && previousViewury != null){
			return new Bounds(previousViewllx, previousViewlly, previousViewurx, previousViewury);
		}else
			return null;
	}
}
