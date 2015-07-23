package si.roskar.diploma.client.presenter;

import si.roskar.diploma.client.DataServiceAsync;
import si.roskar.diploma.client.event.Bus;
import si.roskar.diploma.client.event.EventChangeMapNameHeader;
import si.roskar.diploma.client.event.EventChangeMapNameHeader.EventChangeMapNameHeaderHandler;
import si.roskar.diploma.client.event.EventEnableDrawingToolbar;
import si.roskar.diploma.client.event.EventEnableMapView;
import si.roskar.diploma.client.event.EventEnableMapView.EventEnableMapViewHandler;
import si.roskar.diploma.client.event.EventUILoaded;
import si.roskar.diploma.client.util.KingdomInfo;
import si.roskar.diploma.client.view.LayerView;
import si.roskar.diploma.client.view.MapView;
import si.roskar.diploma.client.view.View;
import si.roskar.diploma.shared.InitialDataPackage;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.sencha.gxt.widget.core.client.Header;

public class InitialPresenter extends PresenterImpl<InitialPresenter.Display>{
	
	public interface Display extends View{
		
		public HasWidgets getCenterContainer();
		
		public HasWidgets getWestContainer();
		
		public void forceLayout();
		
		Header getMapNameHeader();
		
		void enableMapView(boolean enable);
	}
	
	public InitialPresenter(Display display){
		super(display);
	}
	
	@Override
	public void go(HasWidgets container){
		container.clear();
		container.add(display.asWidget());
		
		// get current user
		DataServiceAsync.Util.getInstance().getInitialDataPackage(new AsyncCallback<InitialDataPackage>() {
			
			@Override
			public void onFailure(Throwable caught){
			}
			
			@Override
			public void onSuccess(InitialDataPackage dataPackage){
				if(dataPackage.getCurrentUser() != null && dataPackage.getWmsSoruce() != null){
					// create map presenter
					new MapPresenter(new MapView(dataPackage.getWmsSoruce())).go(display.getCenterContainer());
					
					// create layer presenter
					new LayerPresenter(new LayerView(dataPackage.getCurrentUser())).go(display.getWestContainer());
					
					// event UI loaded
					Bus.get().fireEvent(new EventUILoaded());
					
					display.forceLayout();
				}else{
					if(dataPackage.getCurrentUser() == null){
						KingdomInfo.showLoadingBar("FATAL ERROR", "Failed to retreive user info", "dead...");
					}
					else if(dataPackage.getWmsSoruce() == null){
						KingdomInfo.showLoadingBar("FATAL ERROR", "Failed to retreive wms source", "dead...");
					}
				}
			}
		});
	}
	
	@Override
	protected void bind(){
		
		// handle 'enable map view' event
		Bus.get().addHandler(EventEnableMapView.TYPE, new EventEnableMapViewHandler(){

			@Override
			public void onEnableMapView(EventEnableMapView event){
				display.enableMapView(event.isEnable());

				Bus.get().fireEvent(new EventEnableDrawingToolbar(false));
			}
		});
		
		Bus.get().addHandler(EventChangeMapNameHeader.TYPE, new EventChangeMapNameHeaderHandler(){

			@Override
			public void onChangeMapNameHeader(EventChangeMapNameHeader event){
				display.getMapNameHeader().setText(event.getHeaderText());
			}
		});
	}
}