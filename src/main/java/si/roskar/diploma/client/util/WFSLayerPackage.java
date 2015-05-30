package si.roskar.diploma.client.util;

import org.gwtopenmaps.openlayers.client.control.ModifyFeature;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.protocol.WFSProtocol;
import org.gwtopenmaps.openlayers.client.strategy.RefreshStrategy;

public class WFSLayerPackage{
	
	private Vector			wfsLayer				= null;
	private ModifyFeature	modifyFeatureControl	= null;
	private RefreshStrategy	refreshStrategy			= null;
	private WFSProtocol		wfsProtocol				= null;
	
	public WFSLayerPackage(){
		
	}

	public WFSLayerPackage(Vector wfsLayer, ModifyFeature modifyFeatureControl, RefreshStrategy refreshStrategy, WFSProtocol wfsProtocol){
		this.wfsLayer = wfsLayer;
		this.modifyFeatureControl = modifyFeatureControl;
		this.refreshStrategy = refreshStrategy;
		this.wfsProtocol = wfsProtocol;
	}

	public Vector getWfsLayer(){
		return wfsLayer;
	}

	public void setWfsLayer(Vector wfsLayer){
		this.wfsLayer = wfsLayer;
	}

	public ModifyFeature getModifyFeatureControl(){
		return modifyFeatureControl;
	}

	public void setModifyFeatureControl(ModifyFeature modifyFeatureControl){
		this.modifyFeatureControl = modifyFeatureControl;
	}

	public RefreshStrategy getRefreshStrategy(){
		return refreshStrategy;
	}

	public void setRefreshStrategy(RefreshStrategy refreshStrategy){
		this.refreshStrategy = refreshStrategy;
	}

	public WFSProtocol getWfsProtocol(){
		return wfsProtocol;
	}

	public void setWfsProtocol(WFSProtocol wfsProtocol){
		this.wfsProtocol = wfsProtocol;
	}
}
