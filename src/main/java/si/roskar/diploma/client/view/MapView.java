package si.roskar.diploma.client.view;

import org.gwtopenmaps.openlayers.client.MapOptions;
import org.gwtopenmaps.openlayers.client.MapUnits;
import org.gwtopenmaps.openlayers.client.MapWidget;
import org.gwtopenmaps.openlayers.client.Projection;

import si.roskar.diploma.client.presenter.MapPresenter.Display;
import si.roskar.diploma.client.resources.Resources;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class MapView implements Display {

	private MapWidget 				mapWidget		= null;
	private VerticalLayoutContainer container 		= null;
	private TextButton				zoomToExtent	= null;
	private	TextButton				navigateBack	= null;
	private TextButton				navigateForward	= null;
	private TextButton				drawLine		= null;

	public MapView() {

		// define map options
		MapOptions mapOptions = new MapOptions();
		mapOptions.setProjection("EPSG:4326");
		mapOptions.setDisplayProjection(new Projection("EPSG:4326"));
		mapOptions.setUnits(MapUnits.METERS);
		mapOptions.setAllOverlays(true);

		// create map widget
		mapWidget = new MapWidget("100%", "100%", mapOptions);

		// create map toolbar
		ToolBar mapToolBar = new ToolBar();
		
		zoomToExtent = new TextButton();
		zoomToExtent.setIcon(Resources.ICONS.world());
		zoomToExtent.setToolTip("Zoom to map extent");
		
		navigateBack = new TextButton();
		navigateBack.setIcon(Resources.ICONS.left());
		navigateBack.setToolTip("Back");
		
		navigateForward = new TextButton();
		navigateForward.setIcon(Resources.ICONS.right());
		navigateForward.setToolTip("Forward");
		
		mapToolBar.add(zoomToExtent);
		mapToolBar.add(navigateBack);
		mapToolBar.add(navigateForward);
		
		// create map container
		container = new VerticalLayoutContainer();
		
		container.setBorders(false);
		
		container.add(mapToolBar);
		container.add(mapWidget, new VerticalLayoutData(1, 1));
	}

	@Override
	public Widget asWidget() {
		return container;
	}
}
