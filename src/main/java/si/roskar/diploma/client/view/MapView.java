package si.roskar.diploma.client.view;

import org.gwtopenmaps.openlayers.client.MapOptions;
import org.gwtopenmaps.openlayers.client.MapUnits;
import org.gwtopenmaps.openlayers.client.MapWidget;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import si.roskar.diploma.client.event.Bus;
import si.roskar.diploma.client.presenter.MapPresenter.Display;
import si.roskar.diploma.client.resources.Resources;
import si.roskar.diploma.shared.KingdomLayer;
import si.roskar.diploma.shared.KingdomMap;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class MapView implements Display{
	
	private MapWidget				mapWidget		= null;
	private VerticalLayoutContainer	container		= null;
	private TextButton				zoomToExtent	= null;
	private TextButton				navigateBack	= null;
	private TextButton				navigateForward	= null;
	private TextButton				draw			= null;
	private KingdomMap				kingdomMap		= null;
	private ToolBar					drawingToolbar	= null;
	
	public MapView(){
		
		// define map options
		MapOptions mapOptions = new MapOptions();
		mapOptions.setProjection("EPSG:4326");
		mapOptions.setDisplayProjection(new Projection("EPSG:4326"));
		mapOptions.setUnits(MapUnits.METERS);
		mapOptions.setAllOverlays(true);
		
		// create map widget
		mapWidget = new MapWidget("100%", "100%", mapOptions);
		
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
		
		draw = new TextButton();
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
	
	private void addLayerToMap(KingdomLayer layer){
		Vector newLayer = new Vector(layer.getName());
		
		mapWidget.getMap().addLayer(newLayer);
	}
}
