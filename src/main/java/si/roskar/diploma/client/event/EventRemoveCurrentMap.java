package si.roskar.diploma.client.event;

import si.roskar.diploma.client.event.EventRemoveCurrentMap.EventRemoveCurrentMapHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class EventRemoveCurrentMap extends GwtEvent<EventRemoveCurrentMapHandler>{
	public interface EventRemoveCurrentMapHandler extends EventHandler{
		void onRemoveCurrentMap(EventRemoveCurrentMap event);
	}
	
	public static Type<EventRemoveCurrentMapHandler> TYPE = new Type<EventRemoveCurrentMapHandler>();

	@Override
	public Type<EventRemoveCurrentMapHandler> getAssociatedType(){
		return TYPE;
	}

	@Override
	protected void dispatch(EventRemoveCurrentMapHandler handler){
		handler.onRemoveCurrentMap(this);
	}
}
