package si.roskar.diploma.client.util;

import si.roskar.diploma.client.colorpicker.ColorUtils;
import si.roskar.diploma.client.colorpicker.HueChangedEvent;
import si.roskar.diploma.client.colorpicker.HuePicker;
import si.roskar.diploma.client.colorpicker.IHueChangedHandler;
import si.roskar.diploma.client.colorpicker.SaturationLightnessPicker;

import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

public class ColorPickerWindow extends Window{
	
	private Window thisWindow = null;
	private SaturationLightnessPicker slPicker;
	private HuePicker huePicker;
	private TextButton okButton = null;
	private TextButton cancelButton = null;
	private String color;
	private boolean colorPicked = false;
	
	public ColorPickerWindow() {
		super();
		thisWindow = this;
		colorPicked = false;
		setUp();
	}
	
	private void setUp(){
		setHeadingText("Color picker");
		setModal(true);
		setResizable(false);
		setWidth(245);
		setHeight(270);
		setBodyStyle("padding:6px");
		
		ContentPanel panel = new ContentPanel();
		panel.setHeaderVisible(false);
		panel.setBorders(false);
		panel.setButtonAlign(BoxLayoutPack.END);
		panel.setBodyBorder(false);
		panel.setBodyStyle("background:transparent");
		
		HorizontalLayoutContainer container = new HorizontalLayoutContainer();
		
		slPicker = new SaturationLightnessPicker();
		huePicker = new HuePicker();
		
		// bind saturation/lightness picker and hue picker together
		huePicker.addHueChangedHandler(new IHueChangedHandler() {
			public void hueChanged(HueChangedEvent event) {
				slPicker.setHue(event.getHue());
			}
		});
		
		container.add(slPicker);
		container.add(huePicker);
		
		panel.add(container);
		
		okButton = new TextButton("OK");
		cancelButton = new TextButton("cancel");
		
		panel.addButton(okButton);
		panel.addButton(cancelButton);
		
		this.add(panel);
		
		okButton.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event){
				colorPicked = true;
				color = slPicker.getColor();
				thisWindow.hide();
			}
		});
		
		cancelButton.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event){
				thisWindow.hide();
			}
		});
	}
	
	public void setColor(String color) {
		int[] rgb = ColorUtils.getRGB(color);
		int[] hsl = ColorUtils.rgb2hsl(rgb);
		huePicker.setHue(hsl[0]);
		slPicker.setColor(color);
	}
	
	public String getColor(){
		return color;
	}
	
	public boolean isColorPicked(){
		return colorPicked;
	}
}
