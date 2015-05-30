package si.roskar.diploma.client.event;

import si.roskar.diploma.client.event.EventDisableEditMode.EventDisableEditModeHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class EventDisableEditMode extends GwtEvent<EventDisableEditModeHandler>{
	public interface EventDisableEditModeHandler extends EventHandler{
		void onDisableEditMode(EventDisableEditMode event);
	}
	
	public static Type<EventDisableEditModeHandler> TYPE = new Type<EventDisableEditModeHandler>();
	
	@Override
	public Type<EventDisableEditModeHandler> getAssociatedType(){
		return TYPE;
	}
	
	@Override
	protected void dispatch(EventDisableEditModeHandler handler){
		handler.onDisableEditMode(this);
	}
}
