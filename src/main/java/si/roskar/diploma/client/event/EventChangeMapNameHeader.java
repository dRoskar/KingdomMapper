package si.roskar.diploma.client.event;

import si.roskar.diploma.client.event.EventChangeMapNameHeader.EventChangeMapNameHeaderHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class EventChangeMapNameHeader extends GwtEvent<EventChangeMapNameHeaderHandler>{
	
	public interface EventChangeMapNameHeaderHandler extends EventHandler{
		void onChangeMapNameHeader(EventChangeMapNameHeader event);
	}
	
	public static Type<EventChangeMapNameHeaderHandler> TYPE = new Type<EventChangeMapNameHeaderHandler>();
	public String headerText;
	
	public EventChangeMapNameHeader(String headerText){
		this.headerText = headerText;
	}

	public String getHeaderText(){
		return headerText;
	}

	public void setHeaderText(String headerText){
		this.headerText = headerText;
	}

	@Override
	public Type<EventChangeMapNameHeaderHandler> getAssociatedType(){
		return TYPE;
	}

	@Override
	protected void dispatch(EventChangeMapNameHeaderHandler handler){
		handler.onChangeMapNameHeader(this);
	}
}
