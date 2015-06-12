package si.roskar.diploma.client.util;

import org.gwtopenmaps.openlayers.client.control.Measure;
import org.gwtopenmaps.openlayers.client.control.MeasureOptions;
import org.gwtopenmaps.openlayers.client.handler.Handler;
import org.gwtopenmaps.openlayers.client.util.JSObject;

public class KingdomMeasure extends Measure{
	public KingdomMeasure(Handler handler, MeasureOptions options){
		super(handler, options);
	}
	
	public static native void setImmediate(JSObject self, boolean immediate) /*-{
		self.setImmediate(immediate);
	}-*/;
}
