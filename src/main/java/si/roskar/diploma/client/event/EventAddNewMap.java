package si.roskar.diploma.client.event;

import si.roskar.diploma.client.event.EventAddNewMap.EventAddNewMapHandler;
import si.roskar.diploma.shared.KingdomMap;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class EventAddNewMap extends GwtEvent<EventAddNewMapHandler>{
	
	public interface EventAddNewMapHandler extends EventHandler{
		public void onAddNewMap(EventAddNewMap event);
	}
	
	public static Type<EventAddNewMapHandler>	TYPE	= new Type<EventAddNewMapHandler>();
	private KingdomMap							newMap	= null;
	
	public EventAddNewMap(KingdomMap newMap){
		this.newMap = newMap;
	}
	
	public KingdomMap getNewMap(){
		return newMap;
	}
	
	public void setNewMap(KingdomMap newMap){
		this.newMap = newMap;
	}
	
	@Override
	public Type<EventAddNewMapHandler> getAssociatedType(){
		return TYPE;
	}
	
	@Override
	protected void dispatch(EventAddNewMapHandler handler){
		handler.onAddNewMap(this);
	}
}
