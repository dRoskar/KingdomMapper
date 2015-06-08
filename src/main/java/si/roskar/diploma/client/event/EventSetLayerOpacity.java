package si.roskar.diploma.client.event;

import si.roskar.diploma.client.event.EventSetLayerOpacity.EventSetLayerOpacityHandler;
import si.roskar.diploma.shared.KingdomLayer;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class EventSetLayerOpacity extends GwtEvent<EventSetLayerOpacityHandler>{
	
	public interface EventSetLayerOpacityHandler extends EventHandler {
		void onSetCurrentLayer(EventSetLayerOpacity event);
	}
	
	public static Type<EventSetLayerOpacityHandler> TYPE = new Type<EventSetLayerOpacityHandler>();
	public KingdomLayer layer = null;
	public float opacity = 1f;
	
	public EventSetLayerOpacity(KingdomLayer layer, float opacity){
		this.layer = layer;
		this.opacity = opacity;
	}

	public KingdomLayer getLayer(){
		return layer;
	}

	public void setLayer(KingdomLayer layer){
		this.layer = layer;
	}

	public float getOpacity(){
		return opacity;
	}

	public void setOpacity(float opacity){
		this.opacity = opacity;
	}

	@Override
	public Type<EventSetLayerOpacityHandler> getAssociatedType(){
		return TYPE;
	}

	@Override
	protected void dispatch(EventSetLayerOpacityHandler handler){
		handler.onSetCurrentLayer(this);
	}
}
