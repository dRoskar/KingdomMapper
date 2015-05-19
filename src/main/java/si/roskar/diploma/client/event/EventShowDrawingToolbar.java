package si.roskar.diploma.client.event;

import si.roskar.diploma.client.event.EventShowDrawingToolbar.EventShowDrawingToolbarHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class EventShowDrawingToolbar extends GwtEvent<EventShowDrawingToolbarHandler>{
	
	public interface EventShowDrawingToolbarHandler extends EventHandler{
		void onShowDrawingToolbar(EventShowDrawingToolbar event);
	}
	
	public static Type<EventShowDrawingToolbarHandler> TYPE = new Type<EventShowDrawingToolbarHandler>();
	private boolean visible = false;

	@Override
	public Type<EventShowDrawingToolbarHandler> getAssociatedType(){
		return TYPE;
	}
	
	public EventShowDrawingToolbar(boolean visible){
		this.visible = visible;
	}

	@Override
	protected void dispatch(EventShowDrawingToolbarHandler handler){
		handler.onShowDrawingToolbar(this);
	}

	public boolean isVisible(){
		return visible;
	}

	public void setVisible(boolean visible){
		this.visible = visible;
	}
}
