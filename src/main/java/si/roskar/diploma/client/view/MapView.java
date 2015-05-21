package si.roskar.diploma.client.view;

import org.gwtopenmaps.openlayers.client.Bounds;
import org.gwtopenmaps.openlayers.client.Map;
import org.gwtopenmaps.openlayers.client.MapOptions;
import org.gwtopenmaps.openlayers.client.MapUnits;
import org.gwtopenmaps.openlayers.client.MapWidget;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.control.ScaleLine;
import org.gwtopenmaps.openlayers.client.layer.TransitionEffect;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.layer.WMS;
import org.gwtopenmaps.openlayers.client.layer.WMSOptions;
import org.gwtopenmaps.openlayers.client.layer.WMSParams;

import si.roskar.diploma.client.presenter.MapPresenter.Display;
import si.roskar.diploma.client.resources.Resources;
import si.roskar.diploma.shared.GeometryType;
import si.roskar.diploma.shared.KingdomLayer;
import si.roskar.diploma.shared.KingdomMap;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class MapView implements Display{
	
	private MapWidget				mapWidget		= null;
	private VerticalLayoutContainer	container		= null;
	private TextButton				zoomToExtent	= null;
	private TextButton				navigateBack	= null;
	private TextButton				navigateForward	= null;
	private ToggleButton			draw			= null;
	private KingdomMap				kingdomMap		= null;
	private ToolBar					drawingToolbar	= null;
	private KingdomLayer			currentLayer	= null;
	
	public MapView(){
		
		// define map options
		MapOptions mapOptions = new MapOptions();
//		mapOptions.setMaxExtent(new Bounds(-109.05, 37.99, -102.05, 41.00));
//		mapOptions.setNumZoomLevels(16);
		mapOptions.setProjection("EPSG:4326");
		mapOptions.setDisplayProjection(new Projection("EPSG:4326"));
		mapOptions.setUnits(MapUnits.DEGREES);
		mapOptions.setAllOverlays(true);
//		mapOptions.setRestrictedExtent((new Bounds(-109.05, 37.99, -102.05, 41.00)));
		
		// states layer
		WMSParams stateParams = new WMSParams();
		stateParams.setFormat("image/png");
		stateParams.setLayers("topp:states");
		stateParams.setStyles("population");
		
		// test layer
		WMSParams wmsParams = new WMSParams();
		wmsParams.setFormat("image/png");
		wmsParams.setLayers("kingdom:line");
		wmsParams.setStyles("line");
		wmsParams.setTransparent(true);
		
		WMSOptions wmsLayerParams = new WMSOptions();
		wmsLayerParams.setTransitionEffect(TransitionEffect.RESIZE);
		
		String wmsUrl = "http://127.0.0.1:8080/geoserver/wms";
		
		WMS statesLayer = new WMS("states", wmsUrl, stateParams, wmsLayerParams);
		WMS wmsLayer = new WMS("Basic WMS", wmsUrl, wmsParams, wmsLayerParams);
		wmsLayer.setOpacity(0.5f);
//		wmsLayer.setIsVisible(false);
		
		// create map widget
		mapWidget = new MapWidget("100%", "100%", mapOptions);
		
		Map map = mapWidget.getMap();
		
		map.addLayer(statesLayer);
		map.addLayer(wmsLayer);
		map.addControl(new ScaleLine());
		
		// create viewing toolbar
		ToolBar viewingToolbar = new ToolBar();
		
		zoomToExtent = new TextButton();
		zoomToExtent.setIcon(Resources.ICONS.world());
		zoomToExtent.setToolTip("Zoom to map extent");
		
		navigateBack = new TextButton();
		navigateBack.setIcon(Resources.ICONS.left());
		navigateBack.setToolTip("Back");
		
		navigateForward = new TextButton();
		navigateForward.setIcon(Resources.ICONS.right());
		navigateForward.setToolTip("Forward");
		
		viewingToolbar.add(zoomToExtent);
		viewingToolbar.add(navigateBack);
		viewingToolbar.add(navigateForward);
		
		// create drawing toolbar
		drawingToolbar = new ToolBar();
		
		draw = new ToggleButton();
		draw.setIcon(Resources.ICONS.line());
		draw.setToolTip("Draw");
		
		drawingToolbar.add(draw);
		drawingToolbar.hide();
		
		// create map container
		container = new VerticalLayoutContainer();
		
		container.setBorders(false);
		
		container.add(viewingToolbar);
		container.add(drawingToolbar);
		container.add(mapWidget, new VerticalLayoutData(1, 1));
	}
	
	@Override
	public Widget asWidget(){
		return container;
	}
	
	@Override
	public void addNewMap(KingdomMap newMap){
		kingdomMap = newMap;
	}
	
	@Override
	public KingdomMap getMapObject(){
		return kingdomMap;
	}
	
	@Override
	public void addLayer(KingdomLayer layer){
		// add layer to map object
		if(kingdomMap != null){
			kingdomMap.addLayer(layer);
		}
		
		// add layer to openLayers map
		addLayerToMap(layer);
	}
	
	@Override
	public ToolBar getDrawingToolbar(){
		return drawingToolbar;
	}
	
	@Override
	public ToggleButton getDrawButton(){
		return draw;
	}
	
	private void addLayerToMap(KingdomLayer layer){
		Vector newLayer = new Vector(layer.getName());
		
		mapWidget.getMap().addLayer(newLayer);
	}
	
	@Override
	public void setDrawButtonType(String geometryType){
		if(geometryType.equals(GeometryType.POINT)){
			draw.setIcon(Resources.ICONS.point());
			draw.setData("geometryType", geometryType);
		}else if(geometryType.equals(GeometryType.LINE)){
			draw.setIcon(Resources.ICONS.line());
			draw.setData("geometryType", geometryType);
		}else{
			draw.setIcon(Resources.ICONS.polygon());
			draw.setData("geometryType", geometryType);
		}
	}
	
	@Override
	public Map getOLMap(){
		return mapWidget.getMap();
	}
	
	@Override
	public KingdomLayer getCurrentLayer(){
		return currentLayer;
	}
	
	@Override
	public void setCurrentLayer(KingdomLayer currentLayer){
		this.currentLayer = currentLayer;
	}
}
