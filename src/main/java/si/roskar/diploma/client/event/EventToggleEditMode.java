package si.roskar.diploma.client.event;

import si.roskar.diploma.client.event.EventToggleEditMode.EventToggleEditModeHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class EventToggleEditMode extends GwtEvent<EventToggleEditModeHandler>{
	public interface EventToggleEditModeHandler extends EventHandler{
		void onToggleEditMode(EventToggleEditMode event);
	}
	
	public static Type<EventToggleEditModeHandler> TYPE = new Type<EventToggleEditModeHandler>();
	private boolean editModeEnabled;
	
	public EventToggleEditMode(boolean editModeEnabled){
		this.editModeEnabled = editModeEnabled;
	}
	
	public boolean isEditModeEnabled(){
		return editModeEnabled;
	}

	public void setEditModeEnabled(boolean editModeEnabled){
		this.editModeEnabled = editModeEnabled;
	}
	
	@Override
	public Type<EventToggleEditModeHandler> getAssociatedType(){
		return TYPE;
	}



	@Override
	protected void dispatch(EventToggleEditModeHandler handler){
		handler.onToggleEditMode(this);
	}
}
