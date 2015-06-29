package si.roskar.diploma.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class KingdomFeature implements IsSerializable{
	private String			featureId	= null;
	private KingdomLayer	layer		= null;
	private String			label		= null;
	private String			description	= null;
	
	public KingdomFeature(){
		
	}
	
	public KingdomFeature(String featureId, KingdomLayer layer, String label, String description){
		this.featureId = featureId;
		this.layer = layer;
		this.label = label;
		this.description = description;
	}
	
	public String getFeatureId(){
		return featureId;
	}

	public void setFeatureId(String featureId){
		this.featureId = featureId;
	}

	public KingdomLayer getLayer(){
		return layer;
	}
	
	public void setLayer(KingdomLayer layer){
		this.layer = layer;
	}
	
	public String getLabel(){
		return label;
	}
	
	public void setLabel(String label){
		this.label = label;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
}
