package si.roskar.diploma.client.event;

import si.roskar.diploma.client.event.EventGetSelectedLayer.EventGetSelectedLayerHandler;
import si.roskar.diploma.shared.KingdomLayer;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public abstract class EventGetSelectedLayer extends GwtEvent<EventGetSelectedLayerHandler>{
	
	public interface EventGetSelectedLayerHandler extends EventHandler {
		void onGetSelectedLayer(EventGetSelectedLayer event);
	}
	
	public Type<EventGetSelectedLayerHandler> TYPE = new Type<EventGetSelectedLayerHandler>();

	@Override
	public Type<EventGetSelectedLayerHandler> getAssociatedType(){
		return TYPE;
	}

	@Override
	protected void dispatch(EventGetSelectedLayerHandler handler){
		handler.onGetSelectedLayer(this);
	}
	
	public abstract void setLayer(KingdomLayer selectedLayer);
}
