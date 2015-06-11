package si.roskar.diploma.client.event;

import si.roskar.diploma.client.event.EventMapScaleChanged.EventMapScaleChangedHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class EventMapScaleChanged extends GwtEvent<EventMapScaleChangedHandler>{
	
	public interface EventMapScaleChangedHandler extends EventHandler{
		void onMapScaleChanged(EventMapScaleChanged event);
	}
	
	public EventMapScaleChanged(double scale){
		this.scale = scale;
	}
	
	public static final Type<EventMapScaleChangedHandler> TYPE = new Type<EventMapScaleChangedHandler>();
	private double scale = 0.0;

	public double getScale(){
		return scale;
	}

	public void setScale(double scale){
		this.scale = scale;
	}

	@Override
	public Type<EventMapScaleChangedHandler> getAssociatedType(){
		return TYPE;
	}

	@Override
	protected void dispatch(EventMapScaleChangedHandler handler){
		handler.onMapScaleChanged(this);
	}
}
