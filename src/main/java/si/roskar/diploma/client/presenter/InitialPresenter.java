package si.roskar.diploma.client.presenter;

import si.roskar.diploma.client.event.Bus;
import si.roskar.diploma.client.event.EventChangeMapNameHeader;
import si.roskar.diploma.client.event.EventEnableDrawingToolbar;
import si.roskar.diploma.client.event.EventChangeMapNameHeader.EventChangeMapNameHeaderHandler;
import si.roskar.diploma.client.event.EventEnableMapView;
import si.roskar.diploma.client.event.EventEnableMapView.EventEnableMapViewHandler;
import si.roskar.diploma.client.event.EventUILoaded;
import si.roskar.diploma.client.view.LayerView;
import si.roskar.diploma.client.view.MapView;
import si.roskar.diploma.client.view.View;
import si.roskar.diploma.shared.KingdomUser;

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
		
		// get current user  - temp hardcoded
		KingdomUser user = new KingdomUser(1, "Boris", "");
		user.setLastMapId(33);
		
		// create map presenter
		new MapPresenter(new MapView()).go(display.getCenterContainer());
		
		// create layer presenter
		new LayerPresenter(new LayerView(user)).go(display.getWestContainer());
		
		// event UI loaded
		Bus.get().fireEvent(new EventUILoaded());
		
		display.forceLayout();
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