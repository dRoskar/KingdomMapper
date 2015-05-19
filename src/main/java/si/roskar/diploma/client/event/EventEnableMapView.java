package si.roskar.diploma.client.event;

import si.roskar.diploma.client.event.EventEnableMapView.EventEnableMapViewHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class EventEnableMapView extends GwtEvent<EventEnableMapViewHandler>{
	
	public interface EventEnableMapViewHandler extends EventHandler{
		void onEnableMapView(EventEnableMapView event);
	}
	
	public static Type<EventEnableMapViewHandler> TYPE = new Type<EventEnableMapViewHandler>();
	private boolean enable = false;
	
	public EventEnableMapView(boolean enable){
		this.enable = enable;
	}

	@Override
	public Type<EventEnableMapViewHandler> getAssociatedType(){
		return TYPE;
	}

	@Override
	protected void dispatch(EventEnableMapViewHandler handler){
		handler.onEnableMapView(this);
	}

	public boolean isEnable(){
		return enable;
	}

	public void setEnable(boolean enable){
		this.enable = enable;
	}
}
