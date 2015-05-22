package si.roskar.diploma.client.presenter;

import org.gwtopenmaps.openlayers.client.Map;
import org.gwtopenmaps.openlayers.client.event.EventHandler;
import org.gwtopenmaps.openlayers.client.event.EventObject;
import org.gwtopenmaps.openlayers.client.event.MapClickListener;

import si.roskar.diploma.client.DataServiceAsync;
import si.roskar.diploma.client.event.Bus;
import si.roskar.diploma.client.event.EventAddNewLayer;
import si.roskar.diploma.client.event.EventAddNewLayer.EventAddNewLayerHandler;
import si.roskar.diploma.client.event.EventAddNewMap;
import si.roskar.diploma.client.event.EventAddNewMap.EventAddNewMapHandler;
import si.roskar.diploma.client.event.EventChangeDrawButtonType;
import si.roskar.diploma.client.event.EventChangeDrawButtonType.EventChangeDrawButtonTypeHandler;
import si.roskar.diploma.client.event.EventSetCurrentLayer;
import si.roskar.diploma.client.event.EventSetCurrentLayer.EventSetCurrentLayerHandler;
import si.roskar.diploma.client.event.EventShowDrawingToolbar;
import si.roskar.diploma.client.event.EventShowDrawingToolbar.EventShowDrawingToolbarHandler;
import si.roskar.diploma.client.view.AddMarkerDialog;
import si.roskar.diploma.client.view.View;
import si.roskar.diploma.shared.GeometryType;
import si.roskar.diploma.shared.KingdomLayer;
import si.roskar.diploma.shared.KingdomMap;

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
	}
	
	public interface AddMarkerDisplay extends View{
		
		void show();
		
		void hide();
		
		TextField getLabelField();
		
		TextButton getAddButton();
		
		TextButton getCancelButton();
		
		TextArea getDescriptionField();
		
		boolean isBound();
		
		void setIsBound(boolean isBound);
		
		boolean isValid();
		
		void setValid(boolean isValid);
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
				// if map was previously empty, enable map view
				if(display.getMapObject() != null){
					if(display.getMapObject().getLayers().isEmpty()){
						
					}
				}
				
				// add layer to map
				display.addLayer(event.getLayer());
			}
		});
		
		// handle drawing toolbar show event
		Bus.get().addHandler(EventShowDrawingToolbar.TYPE, new EventShowDrawingToolbarHandler() {
			
			@Override
			public void onShowDrawingToolbar(EventShowDrawingToolbar event){
				if(event.isVisible()){
					display.getDrawingToolbar().show();
				}else{
					display.getDrawingToolbar().hide();
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
			}
		});
		
		// handle map click events
		display.getOLMap().addMapClickListener(new MapClickListener() {
			
			@Override
			public void onClick(final MapClickEvent mapClickEvent){
//				System.out.println(Tools.createColoradoSizedGrid(100));
					
				// handle adding markers
				if(display.getDrawButton().getValue() && display.getDrawButton().getData("geometryType").equals(GeometryType.POINT)){
					if(!addMarkerDisplay.isBound()){
						addMarkerDisplay.getAddButton().addSelectHandler(new SelectHandler() {
							
							@Override
							public void onSelect(SelectEvent event){
								// evaluate fields
								if(addMarkerDisplay.isValid()){
									// insert marker
									DataServiceAsync.Util.getInstance().insertMarker("http://127.0.0.1:8080/geoserver/wms/", mapClickEvent.getLonLat().lon(), mapClickEvent.getLonLat().lat(), addMarkerDisplay.getLabelField().getText(), addMarkerDisplay.getDescriptionField().getText(), display.getCurrentLayer().getId(), new AsyncCallback<Void>() {

										@Override
										public void onFailure(Throwable caught){
										}

										@Override
										public void onSuccess(Void result){
											System.out.println("marker was apparently added?");
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
					
					addMarkerDisplay.show();
				}
			}
		});
		
		display.getOLMap().getEvents().register("zoomend", display.getOLMap(), new EventHandler() {
			
			@Override
			public void onHandle(EventObject eventObject){
//				System.out.println(display.getOLMap().getExtent().toString());
			}
		});
	}
}
