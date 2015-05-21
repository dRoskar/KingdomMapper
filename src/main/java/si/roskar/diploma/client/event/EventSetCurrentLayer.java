package si.roskar.diploma.client.event;

import si.roskar.diploma.client.event.EventSetCurrentLayer.EventSetCurrentLayerHandler;
import si.roskar.diploma.shared.KingdomLayer;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class EventSetCurrentLayer extends GwtEvent<EventSetCurrentLayerHandler>{
	
	public interface EventSetCurrentLayerHandler extends EventHandler {
		void onSetCurrentLayer(EventSetCurrentLayer event);
	}
	
	public static Type<EventSetCurrentLayerHandler> TYPE = new Type<EventSetCurrentLayerHandler>();
	private KingdomLayer currentLayer = null;
	
	public EventSetCurrentLayer(KingdomLayer currentLayer){
		this.currentLayer = currentLayer;
	}

	@Override
	public Type<EventSetCurrentLayerHandler> getAssociatedType(){
		return TYPE;
	}

	@Override
	protected void dispatch(EventSetCurrentLayerHandler handler){
		handler.onSetCurrentLayer(this);
	}

	public KingdomLayer getCurrentLayer(){
		return currentLayer;
	}

	public void setCurrentLayer(KingdomLayer currentLayer){
		this.currentLayer = currentLayer;
	}
}
