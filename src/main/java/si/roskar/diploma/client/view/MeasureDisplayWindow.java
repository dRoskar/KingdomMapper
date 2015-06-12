package si.roskar.diploma.client.view;

import si.roskar.diploma.client.presenter.MapPresenter.MeasureDisplay;

import com.google.gwt.dom.client.Element;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.form.TextField;

public class MeasureDisplayWindow extends Window implements MeasureDisplay
{
	private TextField measureValue = null;

	public MeasureDisplayWindow() {
		super();
		setUp();
	}

	private void setUp(){
		measureValue = new TextField();

		setHeadingHtml("Measure");
		setMinWidth(100);
		setMinHeight(30);
		add(measureValue);
	}

	@Override
	public void show(){
		super.show();
	}

	@Override
	public void setPosition(int left, int top){
		super.setPosition(left, top);
	}

	@Override
	public void setMeasurementText(String text) {
		measureValue.setValue(text);
	}

	@Override
	public int getWidth(){
		return getOffsetWidth();
	}

	@Override
	public int getHeight(){
		return getOffsetHeight();
	}

	@Override
	public void resetPosition(Element e, int padding)
	{
		setPosition(e.getAbsoluteRight() - getWidth() - padding, e.getAbsoluteBottom() - getHeight() - padding);		
	}
}
