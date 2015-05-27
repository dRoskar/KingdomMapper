package si.roskar.diploma.client.event;

import si.roskar.diploma.client.event.EventRemoveLayerFromMapView.EventRemoveLayerFromMapViewHandler;
import si.roskar.diploma.shared.KingdomLayer;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class EventRemoveLayerFromMapView extends GwtEvent<EventRemoveLayerFromMapViewHandler>{
	public interface EventRemoveLayerFromMapViewHandler extends EventHandler{
		void onRemoveLayerFromMapView(EventRemoveLayerFromMapView event);
	}
	
	public static Type<EventRemoveLayerFromMapViewHandler> TYPE = new Type<EventRemoveLayerFromMapViewHandler>();
	private KingdomLayer layer;
	
	public EventRemoveLayerFromMapView(KingdomLayer layer){
		this.layer = layer;
	}

	public KingdomLayer getLayer(){
		return layer;
	}

	public void setLayer(KingdomLayer layer){
		this.layer = layer;
	}

	@Override
	public Type<EventRemoveLayerFromMapViewHandler> getAssociatedType(){
		return TYPE;
	}

	@Override
	protected void dispatch(EventRemoveLayerFromMapViewHandler handler){
		handler.onRemoveLayerFromMapView(this);
	}
}
