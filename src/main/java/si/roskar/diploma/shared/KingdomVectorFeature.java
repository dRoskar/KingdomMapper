package si.roskar.diploma.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class KingdomVectorFeature implements IsSerializable{
	
	private String		wktGeometry	= null;
	private String		gmlGeometry	= null;
	private String		FID			= null;
	
	public KingdomVectorFeature(){
		
	}
	
	public KingdomVectorFeature(String wktGeometry, String fID){
		this.wktGeometry = wktGeometry;
		FID = fID;
	}
	
	public String getWktGeometry(){
		return wktGeometry;
	}
	
	public void setWktGeometry(String wktGeometry){
		this.wktGeometry = wktGeometry;
	}
	
	public String getGmlGeometry(){
		return gmlGeometry;
	}
	
	public void setGmlGeometry(String gmlGeometry){
		this.gmlGeometry = gmlGeometry;
	}
	
	public String getFID(){
		return FID;
	}
	
	public void setFID(String fID){
		FID = fID;
	}
}
