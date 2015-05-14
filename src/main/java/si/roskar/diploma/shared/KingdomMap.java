package si.roskar.diploma.shared;

import java.util.ArrayList;
import java.util.List;

public class KingdomMap{
	
	private String				name	= null;
	private List<KingdomLayer>	layers	= null;
	
	public KingdomMap(){
		layers = new ArrayList<KingdomLayer>();
	}

	public KingdomMap(String name, List<KingdomLayer> layers){
		this.name = name;
		this.layers = layers;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
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
}
