package si.roskar.diploma.client.presenter;

import si.roskar.diploma.client.event.Bus;
import si.roskar.diploma.client.event.EventAddNewLayer;
import si.roskar.diploma.client.event.EventAddNewMap;
import si.roskar.diploma.client.event.EventShowDrawingToolbar;
import si.roskar.diploma.client.event.EventShowDrawingToolbar.EventShowDrawingToolbarHandler;
import si.roskar.diploma.client.event.EventAddNewMap.EventAddNewMapHandler;
import si.roskar.diploma.client.event.EventEnableMapView;
import si.roskar.diploma.client.event.EventAddNewLayer.EventAddNewLayerHandler;
import si.roskar.diploma.client.view.View;
import si.roskar.diploma.shared.KingdomLayer;
import si.roskar.diploma.shared.KingdomMap;

import com.google.gwt.user.client.ui.HasWidgets;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class MapPresenter extends PresenterImpl<MapPresenter.Display>{
	
	public interface Display extends View{
		
		KingdomMap getMapObject();
		
		void addNewMap(KingdomMap newMap);
		
		void addLayer(KingdomLayer layer);
		
		ToolBar getDrawingToolbar();
	}
	
	public MapPresenter(Display display){
		super(display);
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
		Bus.get().addHandler(EventShowDrawingToolbar.TYPE, new EventShowDrawingToolbarHandler(){

			@Override
			public void onShowDrawingToolbar(EventShowDrawingToolbar event){
				if(event.isVisible()){
					display.getDrawingToolbar().show();
				}
				else{
					display.getDrawingToolbar().hide();
				}
			}
		});
	}
}
