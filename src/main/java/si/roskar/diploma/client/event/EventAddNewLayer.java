package si.roskar.diploma.client.event;

import si.roskar.diploma.client.event.EventAddNewLayer.EventAddNewLayerHandler;
import si.roskar.diploma.shared.KingdomLayer;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class EventAddNewLayer extends GwtEvent<EventAddNewLayerHandler>{
	
	public interface EventAddNewLayerHandler extends EventHandler{
		void onAddNewLayer(EventAddNewLayer event);
	}
	
	public static Type<EventAddNewLayerHandler> TYPE = new Type<EventAddNewLayerHandler>();
	public KingdomLayer layer = null;
	
	public EventAddNewLayer(KingdomLayer layer){
		this.layer = layer;
	}

	public KingdomLayer getLayer(){
		return layer;
	}

	public void setLayer(KingdomLayer layer){
		this.layer = layer;
	}

	@Override
	public Type<EventAddNewLayerHandler> getAssociatedType(){
		return TYPE;
	}

	@Override
	protected void dispatch(EventAddNewLayerHandler handler){
		handler.onAddNewLayer(this);
	}
}
