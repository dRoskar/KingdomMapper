package si.roskar.diploma.client.colorpicker;

import com.google.gwt.event.shared.EventHandler;

public interface IColorChangedHandler extends EventHandler {
	void colorChanged(ColorChangedEvent event);
}
