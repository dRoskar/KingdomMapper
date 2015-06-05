package si.roskar.diploma.client.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.gwtopenmaps.openlayers.client.Bounds;
import org.gwtopenmaps.openlayers.client.Map;
import org.gwtopenmaps.openlayers.client.MapOptions;
import org.gwtopenmaps.openlayers.client.MapUnits;
import org.gwtopenmaps.openlayers.client.MapWidget;
import org.gwtopenmaps.openlayers.client.OpenLayers;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.Style;
import org.gwtopenmaps.openlayers.client.StyleMap;
import org.gwtopenmaps.openlayers.client.control.DrawFeature;
import org.gwtopenmaps.openlayers.client.control.ModifyFeature;
import org.gwtopenmaps.openlayers.client.control.ModifyFeatureOptions;
import org.gwtopenmaps.openlayers.client.control.ScaleLine;
import org.gwtopenmaps.openlayers.client.control.SelectFeature;
import org.gwtopenmaps.openlayers.client.event.EventHandler;
import org.gwtopenmaps.openlayers.client.event.EventObject;
import org.gwtopenmaps.openlayers.client.event.FeatureHighlightedListener;
import org.gwtopenmaps.openlayers.client.event.VectorFeatureModifiedListener;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature.State;
import org.gwtopenmaps.openlayers.client.filter.ComparisonFilter;
import org.gwtopenmaps.openlayers.client.filter.ComparisonFilter.Types;
import org.gwtopenmaps.openlayers.client.handler.PathHandler;
import org.gwtopenmaps.openlayers.client.handler.PointHandler;
import org.gwtopenmaps.openlayers.client.handler.PolygonHandler;
import org.gwtopenmaps.openlayers.client.layer.TransitionEffect;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.layer.VectorOptions;
import org.gwtopenmaps.openlayers.client.layer.WMS;
import org.gwtopenmaps.openlayers.client.layer.WMSOptions;
import org.gwtopenmaps.openlayers.client.layer.WMSParams;
import org.gwtopenmaps.openlayers.client.protocol.CRUDOptions.Callback;
import org.gwtopenmaps.openlayers.client.protocol.Response;
import org.gwtopenmaps.openlayers.client.protocol.WFSProtocol;
import org.gwtopenmaps.openlayers.client.protocol.WFSProtocolCRUDOptions;
import org.gwtopenmaps.openlayers.client.protocol.WFSProtocolOptions;
import org.gwtopenmaps.openlayers.client.strategy.BBoxStrategy;
import org.gwtopenmaps.openlayers.client.strategy.RefreshStrategy;
import org.gwtopenmaps.openlayers.client.strategy.SaveStrategy;
import org.gwtopenmaps.openlayers.client.strategy.Strategy;

import si.roskar.diploma.client.presenter.MapPresenter.Display;
import si.roskar.diploma.client.resources.Resources;
import si.roskar.diploma.client.util.WFSLayerPackage;
import si.roskar.diploma.shared.EditingMode;
import si.roskar.diploma.shared.GeometryType;
import si.roskar.diploma.shared.KingdomGridLayer;
import si.roskar.diploma.shared.KingdomLayer;
import si.roskar.diploma.shared.KingdomMap;
import si.roskar.diploma.shared.LayerZIndexComparator;
import si.roskar.diploma.shared.MapSize;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class MapView implements Display{
	
	private final Bounds									countryGridExtent		= new Bounds(-109.545, 36.699, -101.545, 44.1);
	
	private MapWidget										mapWidget				= null;
	private VerticalLayoutContainer							container				= null;
	private TextButton										zoomToExtent			= null;
	private TextButton										navigateBack			= null;
	private TextButton										navigateForward			= null;
	private TextButton										bringToFrontButton		= null;
	private TextButton										sendToBackButton		= null;
	private TextButton										editLayerStyleButton	= null;
	private ToggleButton									grid					= null;
	private TextButton										saveMapStateButton		= null;
	private ToggleButton									drawButton				= null;
	private ToggleButton									moveFeaturesButton		= null;
	private ToggleButton									moveVerticesButton		= null;
	private ToggleButton									deleteFeaturesButton	= null;
	private ToggleButton									addHoleButton			= null;
	private ToggleGroup										editButtonToggleGroup	= null;
	private KingdomMap										kingdomMap				= null;
	private ToolBar											drawingToolbar			= null;
	private KingdomLayer									currentLayer			= null;
	private java.util.Map<KingdomLayer, WMS>				wmsLayerHashMap			= null;
	private java.util.Map<KingdomLayer, WFSLayerPackage>	wfsLayerPackageHashMap	= null;
	private Vector											drawingLayer			= null;
	private WMS												gridLayer				= null;
	private KingdomLayer									editingLayer			= null;
	private KingdomLayer									modifiedLayer			= null;
	private DrawFeature										currentDrawControl		= null;
	private boolean											isInEditMode			= false;
	private boolean											isInAddingHolesMode		= false;
	private List<KingdomLayer>								layerList				= null;
	
	public MapView(){
		
		OpenLayers.setProxyHost("olproxy?targetURL=");
		
		// define map options
		MapOptions mapOptions = new MapOptions();
		mapOptions.setMaxExtent(new Bounds(-109.545, 36.699, -101.545, 44.1));
		mapOptions.setNumZoomLevels(21);
		mapOptions.setProjection("EPSG:4326");
		mapOptions.setDisplayProjection(new Projection("EPSG:4326"));
		mapOptions.setUnits(MapUnits.DEGREES);
		mapOptions.setAllOverlays(true);
		mapOptions.setRestrictedExtent((new Bounds(-109.545, 36.699, -101.545, 44.1)));
		
		// create map widget
		mapWidget = new MapWidget("100%", "100%", mapOptions);
		
		Map map = mapWidget.getMap();
		
		// create drawing layer
		drawingLayer = new Vector("drawingLayer");
		
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
		
		grid = new ToggleButton();
		grid.setIcon(Resources.ICONS.grid());
		grid.setToolTip("Show/hide grid");
		grid.setValue(true);
		
		saveMapStateButton = new TextButton();
		saveMapStateButton.setIcon(Resources.ICONS.mapSave());
		saveMapStateButton.setToolTip("Save map state");
		
		viewingToolbar.add(zoomToExtent);
		viewingToolbar.add(navigateBack);
		viewingToolbar.add(navigateForward);
		viewingToolbar.add(grid);
		viewingToolbar.add(saveMapStateButton);
		
		// create drawing toolbar
		drawingToolbar = new ToolBar();
		
		drawButton = new ToggleButton();
		drawButton.setIcon(Resources.ICONS.line());
		drawButton.setToolTip("Draw");
		
		moveFeaturesButton = new ToggleButton();
		moveFeaturesButton.setIcon(Resources.ICONS.polygonMove());
		moveFeaturesButton.setToolTip("Move features");
		
		moveVerticesButton = new ToggleButton();
		moveVerticesButton.setIcon(Resources.ICONS.vertexMove());
		moveVerticesButton.setToolTip("Move vertices");
		moveVerticesButton.hide();
		
		deleteFeaturesButton = new ToggleButton();
		deleteFeaturesButton.setIcon(Resources.ICONS.erase());
		deleteFeaturesButton.setToolTip("Delete features");
		
		addHoleButton = new ToggleButton();
		addHoleButton.setIcon(Resources.ICONS.holeAdd());
		addHoleButton.setToolTip("Add holes");
		addHoleButton.hide();
		
		bringToFrontButton = new TextButton();
		bringToFrontButton.setIcon(Resources.ICONS.moveToFront());
		bringToFrontButton.setToolTip("Bring to front");
		
		sendToBackButton = new TextButton();
		sendToBackButton.setIcon(Resources.ICONS.sendToBack());
		sendToBackButton.setToolTip("Send to back");
		
		editLayerStyleButton = new TextButton();
		editLayerStyleButton.setIcon(Resources.ICONS.layerStyle());
		editLayerStyleButton.setToolTip("Edit layer style");
		
		editButtonToggleGroup = new ToggleGroup();
		editButtonToggleGroup.add(drawButton);
		editButtonToggleGroup.add(moveFeaturesButton);
		editButtonToggleGroup.add(moveVerticesButton);
		editButtonToggleGroup.add(deleteFeaturesButton);
		
		drawingToolbar.add(drawButton);
		drawingToolbar.add(addHoleButton);
		drawingToolbar.add(new SeparatorToolItem());
		drawingToolbar.add(moveFeaturesButton);
		drawingToolbar.add(moveVerticesButton);
		drawingToolbar.add(deleteFeaturesButton);
		drawingToolbar.add(new SeparatorToolItem());
		drawingToolbar.add(bringToFrontButton);
		drawingToolbar.add(sendToBackButton);
		drawingToolbar.add(editLayerStyleButton);
		drawingToolbar.disable();
		
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
	public ToolBar getDrawingToolbar(){
		return drawingToolbar;
	}
	
	@Override
	public ToggleButton getDrawButton(){
		return drawButton;
	}
	
	@Override
	public ToggleButton getMoveFeaturesButton(){
		return moveFeaturesButton;
	}
	
	@Override
	public ToggleButton getMoveVerticesButton(){
		return moveVerticesButton;
	}
	
	@Override
	public ToggleButton getDeleteFeaturesButton(){
		return deleteFeaturesButton;
	}
	
	@Override
	public ToggleButton getAddHoleButton(){
		return addHoleButton;
	}
	
	@Override
	public ToggleButton getGridButton(){
		return grid;
	}
	
	@Override
	public void setEditButtonGroup(GeometryType geometryType){
		if(geometryType.equals(GeometryType.POINT)){
			drawButton.setIcon(Resources.ICONS.point());
			drawButton.setData("geometryType", geometryType);
			
			moveVerticesButton.hide();
			addHoleButton.setValue(false);
			addHoleButton.hide();
			drawingToolbar.forceLayout();
		}else if(geometryType.equals(GeometryType.LINE)){
			drawButton.setIcon(Resources.ICONS.line());
			drawButton.setData("geometryType", geometryType);
			
			moveVerticesButton.show();
			addHoleButton.setValue(false);
			addHoleButton.hide();
			drawingToolbar.forceLayout();
		}else if(geometryType.equals(GeometryType.POLYGON)){
			drawButton.setIcon(Resources.ICONS.polygon());
			drawButton.setData("geometryType", geometryType);
			
			moveVerticesButton.show();
			if(isInEditMode){
				addHoleButton.show();
				drawingToolbar.forceLayout();
			}
			drawingToolbar.forceLayout();
		}else if(geometryType.equals(GeometryType.MARKER)){
			drawButton.setIcon(Resources.ICONS.marker());
			drawButton.setData("geometryType", geometryType);
			
			moveVerticesButton.hide();
			addHoleButton.setValue(false);
			addHoleButton.hide();
			drawingToolbar.forceLayout();
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
	public KingdomLayer getEditingLayer(){
		return editingLayer;
	}
	
	@Override
	public boolean isInEditMode(){
		return isInEditMode;
	}
	
	@Override
	public boolean isInAddingHolesMode(){
		return isInAddingHolesMode;
	}
	
	@Override
	public void setAddingHolesMode(boolean isInAddingHolesMode){
		this.isInAddingHolesMode = isInAddingHolesMode;
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
		layerList = new ArrayList<KingdomLayer>();
		
		wmsLayerHashMap = new HashMap<KingdomLayer, WMS>();
		wfsLayerPackageHashMap = new HashMap<KingdomLayer, WFSLayerPackage>();
		
		// clear old layers
		mapWidget.getMap().removeOverlayLayers();
		
		// clear cached layers
		wmsLayerHashMap.clear();
		wfsLayerPackageHashMap.clear();
		
		currentLayer = null;
		editingLayer = null;
		
		// set map size
		if(map.getMapSize().equals(MapSize.COUNTRY_MAP)){
			mapWidget.getMap().setMaxExtent(countryGridExtent);
			mapWidget.getMap().setRestrictedExtent(countryGridExtent);
		}else{
			// TODO
			mapWidget.getMap().setMaxExtent(countryGridExtent);
			mapWidget.getMap().setRestrictedExtent(countryGridExtent);
		}
		
		for(KingdomLayer layer : map.getLayers()){
			addLayer(layer);
		}
		
		zoomToStartingBounds();
	}
	
	@Override
	public void addLayer(KingdomLayer layer){
		layerList.add(layer);
		
		// WMS
		WMSOptions wmsOptions = new WMSOptions();
		wmsOptions.setTransitionEffect(TransitionEffect.NONE);
		
		WMSParams layerParams = new WMSParams();
		layerParams.setFormat(layer.getFormat());
		layerParams.setStyles(layer.getStyle());
		layerParams.setTransparent(true);
		
		if(layer instanceof KingdomGridLayer){
			layerParams.setLayers("kingdom:line");
			layerParams.setCQLFilter("IN ('line." + ((KingdomGridLayer) layer).getDBKey() + "')");
		}else{
			if(layer.getGeometryType().equals(GeometryType.POINT)){
				layerParams.setLayers("kingdom:point");
			}else if(layer.getGeometryType().equals(GeometryType.LINE)){
				layerParams.setLayers("kingdom:line");
			}else if(layer.getGeometryType().equals(GeometryType.POLYGON)){
				layerParams.setLayers("kingdom:polygon");
			}else if(layer.getGeometryType().equals(GeometryType.MARKER)){
				layerParams.setLayers("kingdom:point");
			}
			
			layerParams.setCQLFilter("layer_id = " + layer.getId());
			
			// style
			if(layer.getEnvValues() != null){
				layerParams.setParameter("env", layer.getEnvValues());
			}
		}
		
		WMS wms = new WMS(layer.getName(), "http://127.0.0.1:8080/geoserver/wms/", layerParams, wmsOptions);
		
		wms.setOpacity(layer.getOpacity());
		wms.setIsVisible(layer.isVisible());
		wms.setZIndex(layer.getZIndex());
		
		mapWidget.getMap().addLayer(wms);
		
		wms.setZIndex(layer.getZIndex());
		
		wmsLayerHashMap.put(layer, wms);
		
		if(layer instanceof KingdomGridLayer){
			gridLayer = wms;
		}
		
		// WFS
		if(!(layer instanceof KingdomGridLayer)){
			WFSProtocolOptions wfsProtocolOptions = new WFSProtocolOptions();
			wfsProtocolOptions.setUrl("http://127.0.0.1:8080/geoserver/wfs/");
			
			if(layer.getGeometryType().equals(GeometryType.POINT)){
				wfsProtocolOptions.setFeatureType("point");
			}else if(layer.getGeometryType().equals(GeometryType.LINE)){
				wfsProtocolOptions.setFeatureType("line");
			}else if(layer.getGeometryType().equals(GeometryType.POLYGON)){
				wfsProtocolOptions.setFeatureType("polygon");
			}else if(layer.getGeometryType().equals(GeometryType.MARKER)){
				wfsProtocolOptions.setFeatureType("point");
			}
			
			wfsProtocolOptions.setFeatureNameSpace("http://kingdom.si");
			wfsProtocolOptions.setSrsName("EPSG:4326");
			wfsProtocolOptions.setVersion("1.1.0");
			
			WFSProtocol wfsProtocol = new WFSProtocol(wfsProtocolOptions);
			
			// refresh strategy
			RefreshStrategy refreshStrategy = new RefreshStrategy();
			refreshStrategy.setForce(true);
			
			// save strategy
			SaveStrategy saveStrategy = new SaveStrategy();
			saveStrategy.getEvents().register("success", saveStrategy, new EventHandler() {
				
				@Override
				public void onHandle(EventObject eventObject){
					// feature saved - refresh the wms layer
					wmsLayerHashMap.get(modifiedLayer).redraw();
				}
			});
			
			saveStrategy.setAuto(true);
			
			VectorOptions vectorOptions = new VectorOptions();
			vectorOptions.setProtocol(wfsProtocol);
			vectorOptions.setStrategies(new Strategy[] { new BBoxStrategy(), refreshStrategy, saveStrategy });
			
			ComparisonFilter wfsFilter = new ComparisonFilter();
			wfsFilter.setType(Types.EQUAL_TO);
			wfsFilter.setProperty("layer_id");
			wfsFilter.setNumberValue(layer.getId());
			
			// wfs layer styles
			Style defaultStyle = new Style();
			defaultStyle.setStrokeWidth(3);
			
			Style selectStyle = new Style();
			selectStyle.setStrokeColor("#0033CC");
			selectStyle.setFillColor("#0066FF");
			selectStyle.setStrokeWidth(3);
			
			Vector wfsLayer = new Vector(layer.getName() + "Wfs", vectorOptions);
			wfsLayer.setFilter(wfsFilter);
			wfsLayer.setZIndex(layer.getZIndex());
			wfsLayer.setStyleMap(new StyleMap(defaultStyle, selectStyle, defaultStyle));
			
			// modify feature
			ModifyFeatureOptions modifyFeatureOptions = new ModifyFeatureOptions();
			modifyFeatureOptions.setMode(ModifyFeature.RESHAPE);
			
			ModifyFeature modifyFeature = new ModifyFeature(wfsLayer, modifyFeatureOptions);
			
			wfsLayer.addVectorFeatureModifiedListener(new VectorFeatureModifiedListener() {
				
				@Override
				public void onFeatureModified(FeatureModifiedEvent eventObject){
					modifiedLayer = currentLayer;
				}
			});
			
			// delete feature
			SelectFeature deleteFeature = new SelectFeature(wfsLayer);
			deleteFeature.addFeatureHighlightedListener(new FeatureHighlightedListener() {
				
				@Override
				public void onFeatureHighlighted(VectorFeature vectorFeature){
					modifiedLayer = currentLayer;
					
					vectorFeature.toState(State.Unknown);
					vectorFeature.toState(State.Delete);
					
					wfsLayerPackageHashMap.get(editingLayer).getWfsProtocol().commit(vectorFeature, new WFSProtocolCRUDOptions(new Callback() {
						
						@Override
						public void computeResponse(Response response){
							wmsLayerHashMap.get(modifiedLayer).redraw();
							wfsLayerPackageHashMap.get(modifiedLayer).getRefreshStrategy().refresh();
						}
					}));
				}
			});
			
			WFSLayerPackage layerPackage = new WFSLayerPackage(wfsLayer, modifyFeature, refreshStrategy, wfsProtocol, deleteFeature, saveStrategy);
			
			wfsLayerPackageHashMap.put(layer, layerPackage);
		}
	}
	
	@Override
	public void toggleGridVisible(boolean visible){
		if(gridLayer != null){
			gridLayer.setIsVisible(visible);
		}
	}
	
	@Override
	public void enableEditMode(EditingMode mode){
		// disable old editing controls (just in case)
		if(editingLayer != null){
			ModifyFeature modifyFeature = wfsLayerPackageHashMap.get(editingLayer).getModifyFeatureControl();
			if(modifyFeature != null && modifyFeature.isActive()){
				modifyFeature.deactivate();
				mapWidget.getMap().removeControl(modifyFeature);
			}
			
			SelectFeature deleteFeature = wfsLayerPackageHashMap.get(editingLayer).getDeleteFeatureControl();
			if(deleteFeature != null && deleteFeature.isActive()){
				deleteFeature.deactivate();
				mapWidget.getMap().removeControl(deleteFeature);
			}
		}
		
		editingLayer = currentLayer;
		
		WMS wmsLayer = wmsLayerHashMap.get(currentLayer);
		Vector wfsLayer = wfsLayerPackageHashMap.get(currentLayer).getWfsLayer();
		
		// special cases
		if(mode.equals(EditingMode.MOVE_VERTICES) && (currentLayer.getGeometryType().equals(GeometryType.POINT) || currentLayer.getGeometryType().equals(GeometryType.MARKER))){
			return;
		}
		
		if(wmsLayer != null && wfsLayer != null && !isInEditMode && currentLayer.isVisible()){
			wmsLayer.setIsVisible(false);
			
			// add wfs (viewing and editing) layer
			mapWidget.getMap().addLayer(wfsLayer);
			
			// discard old drawing control (just in case)
			if(currentDrawControl != null){
				mapWidget.getMap().removeControl(currentDrawControl);
				currentDrawControl.destroy();
			}
			
			if(mode.equals(EditingMode.DRAW)){
				// enable drawing
				currentDrawControl = createDrawFeatureControl(drawingLayer);
				mapWidget.getMap().addControl(currentDrawControl);
				currentDrawControl.activate();
			}else if(mode.equals(EditingMode.MOVE_FEATURES)){
				// enable draging
				ModifyFeature modifyFeature = wfsLayerPackageHashMap.get(currentLayer).getModifyFeatureControl();
				mapWidget.getMap().addControl(modifyFeature);
				modifyFeature.setMode(ModifyFeature.DRAG);
				modifyFeature.activate();
			}else if(mode.equals(EditingMode.MOVE_VERTICES)){
				// enable draging
				ModifyFeature modifyFeature = wfsLayerPackageHashMap.get(currentLayer).getModifyFeatureControl();
				mapWidget.getMap().addControl(modifyFeature);
				modifyFeature.setMode(ModifyFeature.RESHAPE);
				modifyFeature.activate();
			}else if(mode.equals(EditingMode.DELETE_FEATURES)){
				// enable deleting
				SelectFeature deleteFeature = wfsLayerPackageHashMap.get(currentLayer).getDeleteFeatureControl();
				mapWidget.getMap().addControl(deleteFeature);
				deleteFeature.activate();
			}
			
			isInEditMode = true;
		}
		
		// when removing controls from the OL map the layer z indices get reset
		// for some reason
		gridLayer.setZIndex(325);
		
		for(KingdomLayer layer : layerList){
			if(!(layer instanceof KingdomGridLayer)){
				wmsLayerHashMap.get(layer).setZIndex(layer.getZIndex());
			}
		}
	}
	
	@Override
	public void disableEditMode(){
		if(editingLayer != null && isInEditMode){
			WMS wmsLayer = wmsLayerHashMap.get(editingLayer);
			Vector wfsLayer = wfsLayerPackageHashMap.get(editingLayer).getWfsLayer();
			
			// remove draw control
			if(currentDrawControl != null){
				currentDrawControl.deactivate();
				mapWidget.getMap().removeControl(currentDrawControl);
				currentDrawControl.destroy();
			}
			
			// disable modify control
			ModifyFeature modifyFeature = wfsLayerPackageHashMap.get(editingLayer).getModifyFeatureControl();
			if(modifyFeature != null && modifyFeature.isActive()){
				modifyFeature.deactivate();
				mapWidget.getMap().removeControl(modifyFeature);
			}
			
			// disable delete control
			SelectFeature deleteFeature = wfsLayerPackageHashMap.get(editingLayer).getDeleteFeatureControl();
			if(deleteFeature != null && deleteFeature.isActive()){
				deleteFeature.deactivate();
				mapWidget.getMap().removeControl(deleteFeature);
			}
			
			// remove wfs (viewing/editing) layer
			mapWidget.getMap().removeLayer(wfsLayer);
			
			wmsLayer.setIsVisible(true);
			
			isInEditMode = false;
			
			// when removing controls from the OL map the layer z indices get
			// reset for some reason
			gridLayer.setZIndex(325);
			
			for(KingdomLayer layer : layerList){
				if(!(layer instanceof KingdomGridLayer)){
					wmsLayerHashMap.get(layer).setZIndex(layer.getZIndex());
				}
			}
		}
	}
	
	private DrawFeature createDrawFeatureControl(Vector vectorLayer){
		if(editingLayer != null){
			if(editingLayer.getGeometryType().equals(GeometryType.POINT) || editingLayer.getGeometryType().equals(GeometryType.MARKER)){
				DrawFeature drawPointFeature = new DrawFeature(vectorLayer, new PointHandler());
				return drawPointFeature;
			}else if(editingLayer.getGeometryType().equals(GeometryType.LINE)){
				DrawFeature drawLineFeature = new DrawFeature(vectorLayer, new PathHandler());
				return drawLineFeature;
			}else if(editingLayer.getGeometryType().equals(GeometryType.POLYGON)){
				DrawFeature drawPolygonFeature = new DrawFeature(vectorLayer, new PolygonHandler());
				return drawPolygonFeature;
			}
		}
		
		return null;
	}
	
	@Override
	public java.util.Map<KingdomLayer, WFSLayerPackage> getWfsLayerPackageHashMap(){
		return wfsLayerPackageHashMap;
	}
	
	@Override
	public Vector getDrawingLayer(){
		return drawingLayer;
	}
	
	@Override
	public Vector getCurrentOLWfsLayer(){
		return wfsLayerPackageHashMap.get(currentLayer).getWfsLayer();
	}
	
	@Override
	public WMS getCurrentOLWmsLayer(){
		return wmsLayerHashMap.get(currentLayer);
	}
	
	@Override
	public DrawFeature getCurrentDrawControl(){
		return currentDrawControl;
	}
	
	@Override
	public void setLayerVisibility(KingdomLayer layer, boolean visibility){
		wmsLayerHashMap.get(layer).setIsVisible(visibility);
	}
	
	@Override
	public void removeLayer(KingdomLayer layer){
		if(isInEditMode){
			disableEditMode();
		}
		
		mapWidget.getMap().removeLayer(wmsLayerHashMap.get(layer));
		wmsLayerHashMap.remove(layer);
		
		wfsLayerPackageHashMap.remove(layer);
	}
	
	@Override
	public void removeMapOverlays(){
		mapWidget.getMap().removeOverlayLayers();
	}
	
	@Override
	public TextButton getBringToFrontButton(){
		return bringToFrontButton;
	}
	
	@Override
	public TextButton getSendToBackButton(){
		return sendToBackButton;
	}
	
	@Override
	public TextButton getEditLayerStyleButton(){
		return editLayerStyleButton;
	}
	
	@Override
	public void bringLayerToFront(KingdomLayer selectedLayer){
		// get a list of current layers without the grids
		List<KingdomLayer> layers = new ArrayList<KingdomLayer>();
		
		for(KingdomLayer current : layerList){
			if(!(current instanceof KingdomGridLayer)){
				layers.add(current);
			}
		}
		
		// re-sort to eliminate potential holes in the sort order
		resortLayerZIndices(layers);
		
		// find selected layer and give it a top index
		for(KingdomLayer layer : layers){
			if(layer.getId() == selectedLayer.getId()){
				layer.setZIndex(layers.size() + 1);
			}
		}
		
		resortLayerZIndices(layers);
		
		// set OL Z indices; get highest z index
		for(KingdomLayer layer : layers){
			wmsLayerHashMap.get(layer).setZIndex(layer.getZIndex());
			wfsLayerPackageHashMap.get(layer).getWfsLayer().setZIndex(layer.getZIndex());
		}
	}
	
	@Override
	public void sendLayerToBack(KingdomLayer selectedLayer){
		// get a list of current layers without the grids
		List<KingdomLayer> layers = new ArrayList<KingdomLayer>();
		
		for(KingdomLayer current : layerList){
			if(!(current instanceof KingdomGridLayer)){
				layers.add(current);
			}
		}
		
		// re-sort to eliminate potential holes in the sort order
		resortLayerZIndices(layers);
		
		// find selected layer and give it a bottom index
		for(KingdomLayer layer : layers){
			if(layer.getId() == selectedLayer.getId()){
				layer.setZIndex(0);
			}
		}
		
		resortLayerZIndices(layers);
		
		// set OL Z indices
		for(KingdomLayer layer : layers){
			wmsLayerHashMap.get(layer).setZIndex(layer.getZIndex());
			wfsLayerPackageHashMap.get(layer).getWfsLayer().setZIndex(layer.getZIndex());
		}
	}
	
	private List<KingdomLayer> resortLayerZIndices(List<KingdomLayer> layers){
		// sort layers by Z index
		Collections.sort(layers, new LayerZIndexComparator());
		
		// re-apply Z index value to eliminate holes in the sort order
		for(int i = 1; i < (layers.size() + 1); i++){
			layers.get(i - 1).setZIndex(i);
		}
		
		return layers;
	}
	
	@Override
	public List<KingdomLayer> getLayerList(){
		return layerList;
	}
	
	@Override
	public TextButton getSaveMapStateButton(){
		return saveMapStateButton;
	}
}