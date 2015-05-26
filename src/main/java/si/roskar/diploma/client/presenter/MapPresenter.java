package si.roskar.diploma.client.presenter;

import org.gwtopenmaps.openlayers.client.Map;
import org.gwtopenmaps.openlayers.client.event.VectorFeatureAddedListener;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.layer.WMS;
import org.gwtopenmaps.openlayers.client.strategy.RefreshStrategy;

import si.roskar.diploma.client.DataServiceAsync;
import si.roskar.diploma.client.event.Bus;
import si.roskar.diploma.client.event.EventAddNewLayer;
import si.roskar.diploma.client.event.EventAddNewLayer.EventAddNewLayerHandler;
import si.roskar.diploma.client.event.EventAddNewMap;
import si.roskar.diploma.client.event.EventAddNewMap.EventAddNewMapHandler;
import si.roskar.diploma.client.event.EventChangeDrawButtonType;
import si.roskar.diploma.client.event.EventChangeDrawButtonType.EventChangeDrawButtonTypeHandler;
import si.roskar.diploma.client.event.EventEnableDrawingToolbar;
import si.roskar.diploma.client.event.EventEnableDrawingToolbar.EventEnableDrawingToolbarHandler;
import si.roskar.diploma.client.event.EventSetCurrentLayer;
import si.roskar.diploma.client.event.EventSetCurrentLayer.EventSetCurrentLayerHandler;
import si.roskar.diploma.client.event.EventSetLayerVisibility;
import si.roskar.diploma.client.event.EventSetLayerVisibility.EventSetLayerVisibilityHandler;
import si.roskar.diploma.client.event.EventToggleEditMode;
import si.roskar.diploma.client.event.EventToggleEditMode.EventToggleEditModeHandler;
import si.roskar.diploma.client.view.AddMarkerDialog;
import si.roskar.diploma.client.view.View;
import si.roskar.diploma.shared.GeometryType;
import si.roskar.diploma.shared.KingdomLayer;
import si.roskar.diploma.shared.KingdomMap;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class MapPresenter extends PresenterImpl<MapPresenter.Display>{
	
	public interface Display extends View{
		
		KingdomMap getMapObject();
		
		void addNewMap(KingdomMap newMap);
		
		void addLayer(KingdomLayer layer);
		
		ToolBar getDrawingToolbar();
		
		ToggleButton getDrawButton();
		
		void setDrawButtonType(String geometryType);
		
		Map getOLMap();
		
		KingdomLayer getCurrentLayer();
		
		void setCurrentLayer(KingdomLayer currentLayer);
		
		ToggleButton getGridButton();
		
		void toggleGridVisible(boolean visible);
		
		void enableEditMode();
		
		void disableEditMode();
		
		java.util.Map<KingdomLayer, Vector> getWfsLayerHashMap();
		
		Vector getDrawingLayer();
		
		Vector getCurrentOLWfsLayer();
		
		WMS getCurrentOLWmsLayer();
		
		java.util.Map<KingdomLayer, RefreshStrategy> getRefreshStrategyHashMap();
		
		void setLayerVisibility(KingdomLayer layer, boolean visibility);
	}
	
	public interface AddMarkerDisplay extends View{
		
		void show(String wktGeometry);
		
		void hide();
		
		TextField getLabelField();
		
		TextButton getAddButton();
		
		TextButton getCancelButton();
		
		TextArea getDescriptionField();
		
		boolean isBound();
		
		void setIsBound(boolean isBound);
		
		boolean isValid();
		
		void setValid(boolean isValid);
		
		void setGeometry(String wktGeometry);
		
		String getGeometry();
	}
	
	private AddMarkerDisplay addMarkerDisplay = null;
	
	public MapPresenter(Display display){
		super(display);
		
		addMarkerDisplay = new AddMarkerDialog();
	}
	
	@Override
	public void go(HasWidgets container){
		container.clear();
		container.add(display.asWidget());
	}
	
	@Override
	protected void bind(){
		
		// handle add new map events
		Bus.get().addHandler(EventAddNewMap.TYPE, new EventAddNewMapHandler() {
			
			@Override
			public void onAddNewMap(EventAddNewMap event){
				display.addNewMap(event.getNewMap());
			}
		});
		
		// handle add new layer events
		Bus.get().addHandler(EventAddNewLayer.TYPE, new EventAddNewLayerHandler() {
			@Override
			public void onAddNewLayer(EventAddNewLayer event){
				// add layer to map
				display.addLayer(event.getLayer());
			}
		});
		
		// bind feature added events
		display.getDrawingLayer().addVectorFeatureAddedListener(new VectorFeatureAddedListener() {
			
			@Override
			public void onFeatureAdded(FeatureAddedEvent eventObject){
				// get geometry type
				String geometryType = display.getCurrentLayer().getGeometryType();
				
				String geometry = eventObject.getVectorFeature().getGeometry().toString();
				
				// POINT
				if(geometryType.equals(GeometryType.POINT)){
					if(!addMarkerDisplay.isBound()){
						addMarkerDisplay.getAddButton().addSelectHandler(new SelectHandler() {
							
							@Override
							public void onSelect(SelectEvent event){
								// evaluate fields
								if(addMarkerDisplay.isValid()){
									// insert marker
									DataServiceAsync.Util.getInstance().insertMarker("http://127.0.0.1:8080/geoserver/wms/", addMarkerDisplay.getGeometry(), addMarkerDisplay.getLabelField().getText(), addMarkerDisplay.getDescriptionField().getText(), display.getCurrentLayer().getId(), new AsyncCallback<Void>() {

										@Override
										public void onFailure(Throwable caught){
										}

										@Override
										public void onSuccess(Void result){
											System.out.println("marker added");
											
											// redraw layers
											display.getRefreshStrategyHashMap().get(display.getCurrentLayer()).refresh();
											display.getCurrentOLWmsLayer().redraw();
										}
									});
									
									addMarkerDisplay.hide();
								}
							}
						});
						
						addMarkerDisplay.getCancelButton().addSelectHandler(new SelectHandler() {
							
							@Override
							public void onSelect(SelectEvent event){
								addMarkerDisplay.hide();
							}
						});
						
						addMarkerDisplay.setIsBound(true);
					}
					
					addMarkerDisplay.show(geometry);
				}
				
				// LINE
				if(geometryType.equals(GeometryType.LINE)){
					DataServiceAsync.Util.getInstance().insertLine("http://127.0.0.1:8080/geoserver/wms/", geometry, "", display.getCurrentLayer().getId(), new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught){
						}

						@Override
						public void onSuccess(Void result){
							System.out.println("line added");
							
							// redraw layers
							display.getRefreshStrategyHashMap().get(display.getCurrentLayer()).refresh();
							display.getCurrentOLWmsLayer().redraw();
						}
					});
				}
				
				// POLYGON
				if(geometryType.equals(GeometryType.POLYGON)){
					DataServiceAsync.Util.getInstance().insertPolygon("http://127.0.0.1:8080/geoserver/wms/", geometry, "", display.getCurrentLayer().getId(), new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught){
						}

						@Override
						public void onSuccess(Void result){
							System.out.println("polygon added");
							
							// redraw layers
							display.getRefreshStrategyHashMap().get(display.getCurrentLayer()).refresh();
							display.getCurrentOLWmsLayer().redraw();
						}
					});
				}
			}
		});
		
		// handle drawing toolbar show event
		Bus.get().addHandler(EventEnableDrawingToolbar.TYPE, new EventEnableDrawingToolbarHandler() {
			
			@Override
			public void onEnableDrawingToolbar(EventEnableDrawingToolbar event){
				if(event.isEnabled()){
					display.getDrawingToolbar().enable();
				}else{
					display.getDrawingToolbar().disable();
				}
			}
		});
		
		// handle draw button type change event
		Bus.get().addHandler(EventChangeDrawButtonType.TYPE, new EventChangeDrawButtonTypeHandler() {
			
			@Override
			public void onEventChangeDrawButtonType(EventChangeDrawButtonType event){
				display.setDrawButtonType(event.getGeometryType());
			}
		});
		
		// handle set current layer event
		Bus.get().addHandler(EventSetCurrentLayer.TYPE, new EventSetCurrentLayerHandler(){

			@Override
			public void onSetCurrentLayer(EventSetCurrentLayer event){
				display.setCurrentLayer(event.getCurrentLayer());
				
				// if edit mode is on switch active wfs layers
				if(display.getDrawButton().getValue()){
					display.disableEditMode();
					display.enableEditMode();
				}
			}
		});
		
		// grid button
		display.getGridButton().addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event){
				display.toggleGridVisible(display.getGridButton().getValue());
			}
		});
		
		// handle draw button click
		display.getDrawButton().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event){
				if(event.getValue()){
					// enable edit mode
					display.enableEditMode();
				}else{
					// disable edit mode
					display.disableEditMode();
				}
			}
		});
		
		// handle layer visibility event
		Bus.get().addHandler(EventSetLayerVisibility.TYPE, new EventSetLayerVisibilityHandler(){

			@Override
			public void onSetLayerVisibility(EventSetLayerVisibility event){
				display.setLayerVisibility(event.getLayer(), event.isVisible());
			}
		});
		
		// handle edit mode toggle events
		Bus.get().addHandler(EventToggleEditMode.TYPE, new EventToggleEditModeHandler(){

			@Override
			public void onToggleEditMode(EventToggleEditMode event){
				if(event.isEditModeEnabled()){
					display.enableEditMode(); 
				}
				else{
					display.disableEditMode();
					
					// toggle any edit buttons
					if(display.getDrawButton().getValue()){
						display.getDrawButton().setValue(false, false);
					}
					
					//TODO: add future edit buttons
				}
			}
		});
	}
}
