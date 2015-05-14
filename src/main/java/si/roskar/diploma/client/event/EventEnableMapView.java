package si.roskar.diploma.client.event;

import si.roskar.diploma.client.event.EventEnableMapView.EventEnableMapViewHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class EventEnableMapView extends GwtEvent<EventEnableMapViewHandler>{
	
	public interface EventEnableMapViewHandler extends EventHandler{
		void onEnableMapView(EventEnableMapView event);
	}
	
	public static Type<EventEnableMapViewHandler> TYPE = new Type<EventEnableMapViewHandler>();
	
	public EventEnableMapView(){
		
	}

	@Override
	public Type<EventEnableMapViewHandler> getAssociatedType(){
		return TYPE;
	}

	@Override
	protected void dispatch(EventEnableMapViewHandler handler){
		handler.onEnableMapView(this);
	}
}
