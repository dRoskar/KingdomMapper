package si.roskar.diploma.client.view;

import java.util.HashMap;

import org.gwtopenmaps.openlayers.client.Bounds;
import org.gwtopenmaps.openlayers.client.Map;
import org.gwtopenmaps.openlayers.client.MapOptions;
import org.gwtopenmaps.openlayers.client.MapUnits;
import org.gwtopenmaps.openlayers.client.MapWidget;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.control.LayerSwitcher;
import org.gwtopenmaps.openlayers.client.control.ScaleLine;
import org.gwtopenmaps.openlayers.client.layer.TransitionEffect;
import org.gwtopenmaps.openlayers.client.layer.WMS;
import org.gwtopenmaps.openlayers.client.layer.WMSOptions;
import org.gwtopenmaps.openlayers.client.layer.WMSParams;

import si.roskar.diploma.client.presenter.MapPresenter.Display;
import si.roskar.diploma.client.resources.Resources;
import si.roskar.diploma.shared.GeometryType;
import si.roskar.diploma.shared.KingdomGridLayer;
import si.roskar.diploma.shared.KingdomLayer;
import si.roskar.diploma.shared.KingdomMap;
import si.roskar.diploma.shared.MapSize;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class MapView implements Display{
	
	private final Bounds						countryGridExtent	= new Bounds(-109.545, 36.699, -101.545, 44.1);
	
	private MapWidget							mapWidget			= null;
	private VerticalLayoutContainer				container			= null;
	private TextButton							zoomToExtent		= null;
	private TextButton							navigateBack		= null;
	private TextButton							navigateForward		= null;
	private ToggleButton						draw				= null;
	private KingdomMap							kingdomMap			= null;
	private ToolBar								drawingToolbar		= null;
	private KingdomLayer						currentLayer		= null;
	private java.util.Map<KingdomLayer, WMS>	layerHashMap		= null;
	
	public MapView(){
		
		// define map options
		MapOptions mapOptions = new MapOptions();
		mapOptions.setMaxExtent(new Bounds(-109.545, 36.699, -101.545, 44.1));
		mapOptions.setNumZoomLevels(21);
		mapOptions.setProjection("EPSG:4326");
		mapOptions.setDisplayProjection(new Projection("EPSG:4326"));
		mapOptions.setUnits(MapUnits.DEGREES);
		mapOptions.setAllOverlays(true);
		mapOptions.setRestrictedExtent((new Bounds(-109.545, 36.699, -101.545, 44.1)));
		
		// grid layer
//		WMSParams gridParams = new WMSParams();
//		gridParams.setFormat("image/png");
//		gridParams.setLayers("kingdom:line");
//		gridParams.setStyles("grid");
//		gridParams.setTransparent(true);
//		gridParams.setCQLFilter("IN ('line.11')");
		
//		WMSOptions wmsLayerParams = new WMSOptions();
//		wmsLayerParams.setTransitionEffect(TransitionEffect.RESIZE);
//		wmsLayerParams.setIsBaseLayer(true);
		
//		String wmsUrl = "http://127.0.0.1:8080/geoserver/wms";
		
//		WMS gridLayer = new WMS("Basic WMS", wmsUrl, gridParams, wmsLayerParams);
//		gridLayer.setOpacity(0.5f);
//		 gridLayer.setIsVisible(false);
		
		// create map widget
		mapWidget = new MapWidget("100%", "100%", mapOptions);
		
		Map map = mapWidget.getMap();
		
//		map.addLayer(gridLayer);
		map.addControl(new ScaleLine());
		map.addControl(new LayerSwitcher());
		
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
		
		setUpLayers(newMap);
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
	
	private void zoomToStartingBounds(){
		if(mapWidget == null || mapWidget.getMap() == null || mapWidget.getMap().getLayers().length == 0){
			return;
		}
		
		mapWidget.getMap().zoomToExtent(new Bounds(-105.591875, 40.365717041016, -105.498125, 40.433282958984));
	}
	
	private void setUpLayers(KingdomMap map){
		layerHashMap = new HashMap<KingdomLayer, WMS>();
		
		// clear old layers if any
		if(mapWidget.getMap().getBaseLayer() != null){
			mapWidget.getMap().removeOverlayLayers();
		}
		
		// set map size
		if(map.getMapSize().equals(MapSize.COUNTRY_MAP)){
			mapWidget.getMap().setMaxExtent(countryGridExtent);
			mapWidget.getMap().setRestrictedExtent(countryGridExtent);
		}
		else{
			// TODO
			mapWidget.getMap().setMaxExtent(countryGridExtent);
			mapWidget.getMap().setRestrictedExtent(countryGridExtent);
		}
		
		// add layers to map
		for(KingdomLayer layer : map.getLayers()){
			WMSOptions wmsOptions = new WMSOptions();
			wmsOptions.setTransitionEffect(TransitionEffect.NONE);
			
			WMSParams layerParams = new WMSParams();
			layerParams.setFormat(layer.getFormat());
			layerParams.setStyles(layer.getStyle());
			layerParams.setTransparent(true);
			
			if(layer instanceof KingdomGridLayer){
				layerParams.setLayers("kingdom:line");	
				layerParams.setCQLFilter("IN ('line." + ((KingdomGridLayer)layer).getDBKey() + "')");
			}
			else{
				if(layer.getGeometryType().equals(GeometryType.POINT)){
					layerParams.setLayers("kingdom:point");
					layerParams.setCQLFilter("layer_id = " + layer.getId());
				}
				else if(layer.getGeometryType().equals(GeometryType.LINE)){
					layerParams.setLayers("kingdom:line");
					layerParams.setCQLFilter("layer_id = " + layer.getId());
				}
				else{
					layerParams.setLayers("kingdom:polygon");
					layerParams.setCQLFilter("layer_id = " + layer.getId());
				}
			}
			
			WMS wms = new WMS(layer.getName(), "http://127.0.0.1:8080/geoserver/wms/", layerParams, wmsOptions);
			
			wms.setOpacity(layer.getOpacity());
			wms.setIsVisible(layer.isVisible());
			
			mapWidget.getMap().addLayer(wms);
			
			layerHashMap.put(layer, wms);
		}
		
		zoomToStartingBounds();
	}
}
