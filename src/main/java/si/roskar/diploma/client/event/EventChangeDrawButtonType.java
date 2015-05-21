package si.roskar.diploma.client.event;

import com.google.gwt.event.shared.EventHandler;
import si.roskar.diploma.client.event.EventChangeDrawButtonType.EventChangeDrawButtonTypeHandler;
import com.google.gwt.event.shared.GwtEvent;

public class EventChangeDrawButtonType extends GwtEvent<EventChangeDrawButtonTypeHandler>{
	
	public interface EventChangeDrawButtonTypeHandler extends EventHandler{
		void onEventChangeDrawButtonType(EventChangeDrawButtonType event);
	}
	
	public static Type<EventChangeDrawButtonTypeHandler> TYPE = new Type<EventChangeDrawButtonTypeHandler>();
	
	private String geometryType = null;
	
	public EventChangeDrawButtonType(String geometryType){
		this.geometryType = geometryType;
	}

	@Override
	public Type<EventChangeDrawButtonTypeHandler> getAssociatedType(){
		return TYPE;
	}

	@Override
	protected void dispatch(EventChangeDrawButtonTypeHandler handler){
		handler.onEventChangeDrawButtonType(this);
	}

	public String getGeometryType(){
		return geometryType;
	}

	public void setGeometryType(String geometryType){
		this.geometryType = geometryType;
	}
}
