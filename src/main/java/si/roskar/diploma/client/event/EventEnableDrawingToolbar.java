package si.roskar.diploma.client.event;

import si.roskar.diploma.client.event.EventEnableDrawingToolbar.EventEnableDrawingToolbarHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class EventEnableDrawingToolbar extends GwtEvent<EventEnableDrawingToolbarHandler>{
	
	public interface EventEnableDrawingToolbarHandler extends EventHandler{
		void onEnableDrawingToolbar(EventEnableDrawingToolbar event);
	}
	
	public static Type<EventEnableDrawingToolbarHandler> TYPE = new Type<EventEnableDrawingToolbarHandler>();
	private boolean enabled = false;

	@Override
	public Type<EventEnableDrawingToolbarHandler> getAssociatedType(){
		return TYPE;
	}
	
	public EventEnableDrawingToolbar(boolean enabled){
		this.enabled = enabled;
	}

	@Override
	protected void dispatch(EventEnableDrawingToolbarHandler handler){
		handler.onEnableDrawingToolbar(this);
	}

	public boolean isEnabled(){
		return enabled;
	}

	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
}
