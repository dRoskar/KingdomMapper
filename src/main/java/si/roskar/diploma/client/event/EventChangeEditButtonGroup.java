package si.roskar.diploma.client.event;

import si.roskar.diploma.client.event.EventChangeEditButtonGroup.EventChangeEditButtonGroupHandler;
import si.roskar.diploma.shared.GeometryType;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class EventChangeEditButtonGroup extends GwtEvent<EventChangeEditButtonGroupHandler>{
	
	public interface EventChangeEditButtonGroupHandler extends EventHandler{
		void onEventChangeEditButtonGroup(EventChangeEditButtonGroup event);
	}
	
	public static Type<EventChangeEditButtonGroupHandler> TYPE = new Type<EventChangeEditButtonGroupHandler>();
	
	private GeometryType geometryType = null;
	
	public EventChangeEditButtonGroup(GeometryType geometryType){
		this.geometryType = geometryType;
	}

	@Override
	public Type<EventChangeEditButtonGroupHandler> getAssociatedType(){
		return TYPE;
	}

	@Override
	protected void dispatch(EventChangeEditButtonGroupHandler handler){
		handler.onEventChangeEditButtonGroup(this);
	}

	public GeometryType getGeometryType(){
		return geometryType;
	}

	public void setGeometryType(GeometryType geometryType){
		this.geometryType = geometryType;
	}
}
