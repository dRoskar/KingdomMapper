package si.roskar.diploma.client.event;

import com.google.gwt.event.shared.HandlerManager;

public class Bus{
	
	private static HandlerManager eventBus = null;
	
	public static HandlerManager get(){
		if(eventBus == null){
			eventBus = new HandlerManager(null);
		}
		
		return eventBus;
	}
}
