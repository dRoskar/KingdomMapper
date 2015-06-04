package si.roskar.diploma.client.colorpicker;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.widget.core.client.Composite;

public class Canvas extends Composite implements HasAllMouseHandlers {
	private HTML html;
	private Element canvas;

	public Canvas() {
		html = new HTML("<canvas></canvas>"); //$NON-NLS-1$
		initWidget(html);
		canvas = (Element) html.getElement().getFirstChild();
	}
	
	public native RenderingContext getContext() /*-{
		return this.@si.roskar.diploma.client.colorpicker.Canvas::canvas.getContext("2d");
	}-*/;

	public void setCanvasSize(int width, int height) {
		canvas.setPropertyInt("width", width);
		canvas.setPropertyInt("height", height);
//		DOM.setElementPropertyInt(canvas, "width", width); //$NON-NLS-1$
//		DOM.setElementPropertyInt(canvas, "height", height); //$NON-NLS-1$
	}

	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		return html.addMouseDownHandler(handler);
	}
	
	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		return html.addMouseUpHandler(handler);
	}
	
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return html.addMouseOverHandler(handler);
	}
	
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return html.addMouseOutHandler(handler);
	}
	
	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		return html.addMouseMoveHandler(handler);
	}
	
	public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
		return html.addMouseWheelHandler(handler);
	}
}
