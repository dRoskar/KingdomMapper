package si.roskar.diploma.client.event;

import si.roskar.diploma.client.event.EventSortLayerTree.EventSortLayerTreeHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class EventSortLayerTree extends GwtEvent<EventSortLayerTreeHandler>{
	public interface EventSortLayerTreeHandler extends EventHandler{
		public void onSortLayerTree(EventSortLayerTree event);
	}
	
	public static Type<EventSortLayerTreeHandler> TYPE = new Type<EventSortLayerTreeHandler>();

	@Override
	public Type<EventSortLayerTreeHandler> getAssociatedType(){
		return TYPE;
	}

	@Override
	protected void dispatch(EventSortLayerTreeHandler handler){
		handler.onSortLayerTree(this);
	}
}
