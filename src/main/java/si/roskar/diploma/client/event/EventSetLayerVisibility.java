package si.roskar.diploma.client.event;

import si.roskar.diploma.client.event.EventSetLayerVisibility.EventSetLayerVisibilityHandler;
import si.roskar.diploma.shared.KingdomLayer;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class EventSetLayerVisibility extends GwtEvent<EventSetLayerVisibilityHandler>{
	public interface EventSetLayerVisibilityHandler extends EventHandler{
		void onSetLayerVisibility(EventSetLayerVisibility event);
	}
	
	public static Type<EventSetLayerVisibilityHandler> TYPE = new Type<EventSetLayerVisibilityHandler>();
	private KingdomLayer layer;
	private boolean visible;
	
	public EventSetLayerVisibility(KingdomLayer layer, boolean visible){
		this.layer = layer;
		this.visible = visible;
	}

	@Override
	public Type<EventSetLayerVisibilityHandler> getAssociatedType(){
		return TYPE;
	}

	@Override
	protected void dispatch(EventSetLayerVisibilityHandler handler){
		handler.onSetLayerVisibility(this);
	}

	public KingdomLayer getLayer(){
		return layer;
	}

	public void setLayer(KingdomLayer layer){
		this.layer = layer;
	}

	public boolean isVisible(){
		return visible;
	}

	public void setVisible(boolean visible){
		this.visible = visible;
	}
}
