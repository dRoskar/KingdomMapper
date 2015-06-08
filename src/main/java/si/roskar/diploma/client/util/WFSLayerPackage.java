package si.roskar.diploma.client.util;

import org.gwtopenmaps.openlayers.client.control.ModifyFeature;
import org.gwtopenmaps.openlayers.client.control.SelectFeature;
import org.gwtopenmaps.openlayers.client.control.Snapping;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.protocol.WFSProtocol;
import org.gwtopenmaps.openlayers.client.strategy.RefreshStrategy;
import org.gwtopenmaps.openlayers.client.strategy.SaveStrategy;

public class WFSLayerPackage{
	
	private Vector			wfsLayer				= null;
	private ModifyFeature	modifyFeatureControl	= null;
	private RefreshStrategy	refreshStrategy			= null;
	private WFSProtocol		wfsProtocol				= null;
	private SelectFeature	deleteFeatureControl	= null;
	private SaveStrategy	saveStrategy			= null;
	private Snapping		snapControl				= null;
	
	public WFSLayerPackage(){
		
	}
	
	public WFSLayerPackage(Vector wfsLayer, ModifyFeature modifyFeatureControl, RefreshStrategy refreshStrategy, WFSProtocol wfsProtocol, SelectFeature deleteFeatureControl, SaveStrategy saveStrategy, Snapping snapControl){
		this.wfsLayer = wfsLayer;
		this.modifyFeatureControl = modifyFeatureControl;
		this.refreshStrategy = refreshStrategy;
		this.wfsProtocol = wfsProtocol;
		this.deleteFeatureControl = deleteFeatureControl;
		this.snapControl = snapControl;
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
	
	public SelectFeature getDeleteFeatureControl(){
		return deleteFeatureControl;
	}
	
	public void setDeleteFeatureControl(SelectFeature deleteFeatureControl){
		this.deleteFeatureControl = deleteFeatureControl;
	}
	
	public SaveStrategy getSaveStrategy(){
		return saveStrategy;
	}
	
	public void setSaveStrategy(SaveStrategy saveStrategy){
		this.saveStrategy = saveStrategy;
	}

	public Snapping getSnapControl(){
		return snapControl;
	}

	public void setSnapControl(Snapping snapControl){
		this.snapControl = snapControl;
	}
}
