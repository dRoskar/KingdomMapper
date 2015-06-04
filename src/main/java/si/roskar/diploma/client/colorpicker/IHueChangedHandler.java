package si.roskar.diploma.client.colorpicker;

import com.google.gwt.event.shared.EventHandler;

public interface IHueChangedHandler extends EventHandler {
	void hueChanged(HueChangedEvent event);
}
