package si.roskar.diploma.client.event;

import si.roskar.diploma.client.event.EventUILoaded.EventUILoadedHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class EventUILoaded extends GwtEvent<EventUILoadedHandler>{
	public interface EventUILoadedHandler extends EventHandler{
		void onUILoaded(EventUILoaded event);
	}
	
	public static Type<EventUILoadedHandler> TYPE = new Type<EventUILoadedHandler>();

	@Override
	public Type<EventUILoadedHandler> getAssociatedType(){
		return TYPE;
	}

	@Override
	protected void dispatch(EventUILoadedHandler handler){
		handler.onUILoaded(this);	
	}
}
