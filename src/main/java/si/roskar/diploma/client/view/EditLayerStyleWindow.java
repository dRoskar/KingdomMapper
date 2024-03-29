package si.roskar.diploma.client.view;

import si.roskar.diploma.client.presenter.MapPresenter.EditLayerStyleDisplay;
import si.roskar.diploma.client.util.ColorPickerWindow;
import si.roskar.diploma.shared.GeometryType;
import si.roskar.diploma.shared.KingdomLayer;
import si.roskar.diploma.shared.KingdomMarker;
import si.roskar.diploma.shared.KingdomTexture;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.ColorPalette;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Slider;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DoubleSpinnerField;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.IntegerSpinnerField;

public class EditLayerStyleWindow extends Window implements EditLayerStyleDisplay{
	
	interface ComboBoxTemplates extends XTemplates{
		@XTemplate("<img width=\"24\" height=\"24\" src=\"{imageUri}\"> {title}")
		SafeHtml marker(SafeUri imageUri, String title);
		
		@XTemplate("<img width=\"24\" height=\"24\" src=\"{imageUri}\"> {title}")
		SafeHtml texture(SafeUri imageUri, String title);
	}
	
	interface MarkerProperties extends PropertyAccess<KingdomMarker>{
		ModelKeyProvider<KingdomMarker> imageName();
		
		LabelProvider<KingdomMarker> title();
	}
	
	interface TextureProperties extends PropertyAccess<KingdomTexture>{
		ModelKeyProvider<KingdomTexture> imageName();
		
		LabelProvider<KingdomTexture> title();
	}
	
	private KingdomLayer				layer								= null;
	private ColorPalette				colorPalette						= null;
	private ColorPalette				fillColorPalette					= null;
	private ColorPalette				labelColorPalette					= null;
	private ColorPalette				labelFillColorPalette				= null;
	private FieldLabel					colorPaletteFieldLabel				= null;
	private FieldLabel					fillColorPaletteFieldLabel			= null;
	private FieldLabel					labelColorPaletteFieldLabel			= null;
	private FieldLabel					labelFillColorPaletteFieldLabel		= null;
	private IntegerSpinnerField			sizeSpinner							= null;
	private DoubleSpinnerField			strokeOpacitySpinner				= null;
	private DoubleSpinnerField			fillOpacitySpinner					= null;
	private TextButton					applyButton							= null;
	private TextButton					cancelButton						= null;
	private boolean						isBound								= false;
	private String						color								= null;
	private String						fillColor							= null;
	private String						labelColor							= null;
	private String						labelFillColor						= null;
	private ColorPickerWindow			colorPickerWindow					= null;
	private ClickHandler				colorPaletteClickHandler			= null;
	private ClickHandler				fillColorPaletteClickHandler		= null;
	private ClickHandler				labelColorPaletteClickHandler		= null;
	private ClickHandler				labelFillColorPaletteClickHandler	= null;
	private double[]					scales								= null;
	private Slider						upperLimitSlider					= null;
	private Slider						lowerLimitSlider					= null;
	private CheckBox					labelCheckBox						= null;
	private CheckBox					textureCheckBox						= null;
	private ComboBox<KingdomMarker>		markerComboBox						= null;
	private ComboBox<KingdomTexture>	textureComboBox						= null;
	
	private MarkerProperties			markerProps							= GWT.create(MarkerProperties.class);
	private TextureProperties			textureProps						= GWT.create(TextureProperties.class);
	
	public EditLayerStyleWindow(KingdomLayer layer, double[] scales){
		this.layer = layer;
		this.scales = scales;
		setUp();
	}
	
	private void setUp(){
		
		colorPaletteClickHandler = new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event){
				colorPickerWindow = new ColorPickerWindow();
				colorPickerWindow.show();
				colorPickerWindow.setColor(layer.getColor());
				
				colorPickerWindow.addHideHandler(new HideHandler() {
					
					@Override
					public void onHide(HideEvent event){
						if(colorPickerWindow.isColorPicked()){
							setColor(colorPickerWindow.getColor());
						}
					}
				});
			}
		};
		
		fillColorPaletteClickHandler = new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event){
				colorPickerWindow = new ColorPickerWindow();
				colorPickerWindow.show();
				colorPickerWindow.setColor(layer.getFillColor());
				
				colorPickerWindow.addHideHandler(new HideHandler() {
					
					@Override
					public void onHide(HideEvent event){
						if(colorPickerWindow.isColorPicked()){
							setFillColor(colorPickerWindow.getColor());
						}
					}
				});
			}
		};
		
		labelColorPaletteClickHandler = new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event){
				colorPickerWindow = new ColorPickerWindow();
				colorPickerWindow.show();
				colorPickerWindow.setColor(layer.getLabelColor());
				
				colorPickerWindow.addHideHandler(new HideHandler() {
					
					@Override
					public void onHide(HideEvent event){
						if(colorPickerWindow.isColorPicked()){
							setLabelColor(colorPickerWindow.getColor());
						}
					}
				});
			}
		};
		
		labelFillColorPaletteClickHandler = new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event){
				colorPickerWindow = new ColorPickerWindow();
				colorPickerWindow.show();
				colorPickerWindow.setColor(layer.getLabelFillColor());
				
				colorPickerWindow.addHideHandler(new HideHandler() {
					
					@Override
					public void onHide(HideEvent event){
						if(colorPickerWindow.isColorPicked()){
							setLabelFillColor(colorPickerWindow.getColor());
						}
					}
				});
			}
		};
		
		setHeadingText("Layer style - " + layer.getName());
		setModal(true);
		setResizable(false);
		setBodyStyle("padding:6px");
		
		ContentPanel cp = new ContentPanel();
		cp.setBorders(false);
		cp.setHeaderVisible(false);
		cp.setButtonAlign(BoxLayoutPack.END);
		cp.setBodyBorder(false);
		cp.setBodyStyle("background:transparent");
		
		VerticalLayoutContainer layoutContainer = null;
		
		if(layer.getGeometryType().equals(GeometryType.POINT)){
			layoutContainer = assemblePointStyleEditorLayout();
		}else if(layer.getGeometryType().equals(GeometryType.LINE)){
			layoutContainer = assembleLineStyleEditorLayout();
		}else if(layer.getGeometryType().equals(GeometryType.POLYGON)){
			layoutContainer = assemblePolygonStyleEditorLayout();
		}else if(layer.getGeometryType().equals(GeometryType.MARKER)){
			layoutContainer = assembleMarkerStyleEditorLayout();
		}
		
		cp.add(layoutContainer);
		
		applyButton = new TextButton("Apply");
		cancelButton = new TextButton("Cancel");
		
		cp.addButton(applyButton);
		cp.addButton(cancelButton);
		
		this.add(cp);
	}
	
	private VerticalLayoutContainer assemblePointStyleEditorLayout(){
		this.color = layer.getColor();
		this.labelColor = layer.getLabelColor();
		this.labelFillColor = layer.getLabelFillColor();
		
		VerticalLayoutContainer layoutContainer = new VerticalLayoutContainer();
		
		labelCheckBox = new CheckBox();
		labelCheckBox.setValue(layer.getStyle().contains("label"));
		layoutContainer.add(new FieldLabel(labelCheckBox, "Labeled"));
		
		String[] colors = new String[] { layer.getColor() };
		colorPalette = new ColorPalette(colors, colors);
		colorPalette.addDomHandler(colorPaletteClickHandler, ClickEvent.getType());
		
		colorPaletteFieldLabel = new FieldLabel();
		colorPaletteFieldLabel.setWidget(colorPalette);
		colorPaletteFieldLabel.setText("Color");
		layoutContainer.add(colorPaletteFieldLabel);
		
		String[] labelColors = new String[] { layer.getLabelColor() };
		labelColorPalette = new ColorPalette(labelColors, labelColors);
		labelColorPalette.addDomHandler(labelColorPaletteClickHandler, ClickEvent.getType());
		
		labelColorPaletteFieldLabel = new FieldLabel();
		labelColorPaletteFieldLabel.setWidget(labelColorPalette);
		labelColorPaletteFieldLabel.setText("Label background color");
		layoutContainer.add(labelColorPaletteFieldLabel);
		
		String[] labelFillColors = new String[] { layer.getLabelFillColor() };
		labelFillColorPalette = new ColorPalette(labelFillColors, labelFillColors);
		labelFillColorPalette.addDomHandler(labelFillColorPaletteClickHandler, ClickEvent.getType());
		
		labelFillColorPaletteFieldLabel = new FieldLabel();
		labelFillColorPaletteFieldLabel.setWidget(labelFillColorPalette);
		labelFillColorPaletteFieldLabel.setText("Label color");
		layoutContainer.add(labelFillColorPaletteFieldLabel);
		
		sizeSpinner = new IntegerSpinnerField();
		sizeSpinner.setIncrement(1);
		sizeSpinner.setMinValue(1);
		sizeSpinner.setMaxValue(1000);
		sizeSpinner.setEditable(true);
		sizeSpinner.setValue(layer.getSize());
		layoutContainer.add(new FieldLabel(sizeSpinner, "Size"));
		
		ListStore<KingdomMarker> markers = new ListStore<KingdomMarker>(markerProps.imageName());
		markers.addAll(KingdomMarker.getMarkerList());
		
		markerComboBox = new ComboBox<KingdomMarker>(markers, markerProps.title(), new AbstractSafeHtmlRenderer<KingdomMarker>() {
			final ComboBoxTemplates	comboBoxTemplates	= GWT.create(ComboBoxTemplates.class);
			
			@Override
			public SafeHtml render(KingdomMarker object){
				return comboBoxTemplates.marker(object.getIcon().getSafeUri(), object.getTitle());
			}
		});
		
		markerComboBox.setValue(KingdomMarker.getMarkerByImageName(markers.getAll(), layer.getMarkerImage()));
		markerComboBox.setTriggerAction(TriggerAction.ALL);
		layoutContainer.add(new FieldLabel(markerComboBox, "Marker"));
		
		upperLimitSlider = new Slider();
		upperLimitSlider.setIncrement(1);
		upperLimitSlider.setMinValue(1);
		upperLimitSlider.setMaxValue(scales.length);
		upperLimitSlider.setMessage("Layer will be invisible until zoom level {0}");
		upperLimitSlider.setValue(getScaleIndex(layer.getMinScale(), true));
		layoutContainer.add(new FieldLabel(upperLimitSlider, "Upper visibility limit"));
		
		lowerLimitSlider = new Slider();
		lowerLimitSlider.setIncrement(1);
		lowerLimitSlider.setMinValue(1);
		lowerLimitSlider.setMaxValue(scales.length);
		lowerLimitSlider.setMessage("Layer will become invisible after zoom level {0}");
		lowerLimitSlider.setValue(getScaleIndex(layer.getMaxScale(), false));
		layoutContainer.add(new FieldLabel(lowerLimitSlider, "Lower visibility limit"));
		
		return layoutContainer;
	}
	
	private VerticalLayoutContainer assembleLineStyleEditorLayout(){
		this.color = layer.getColor();
		
		VerticalLayoutContainer layoutContainer = new VerticalLayoutContainer();
		
		String[] colors = new String[] { layer.getColor() };
		colorPalette = new ColorPalette(colors, colors);
		colorPalette.addDomHandler(colorPaletteClickHandler, ClickEvent.getType());
		
		colorPaletteFieldLabel = new FieldLabel();
		colorPaletteFieldLabel.setWidget(colorPalette);
		colorPaletteFieldLabel.setText("Color");
		layoutContainer.add(colorPaletteFieldLabel);
		
		sizeSpinner = new IntegerSpinnerField();
		sizeSpinner.setIncrement(1);
		sizeSpinner.setMinValue(1);
		sizeSpinner.setMaxValue(1000);
		sizeSpinner.setEditable(true);
		sizeSpinner.setValue(layer.getStrokeWidth());
		layoutContainer.add(new FieldLabel(sizeSpinner, "Stroke width"));
		
		upperLimitSlider = new Slider();
		upperLimitSlider.setIncrement(1);
		upperLimitSlider.setMinValue(1);
		upperLimitSlider.setMaxValue(scales.length);
		upperLimitSlider.setMessage("Layer will be invisible until zoom level {0}");
		upperLimitSlider.setValue(getScaleIndex(layer.getMinScale(), true));
		layoutContainer.add(new FieldLabel(upperLimitSlider, "Upper visibility limit"));
		
		lowerLimitSlider = new Slider();
		lowerLimitSlider.setIncrement(1);
		lowerLimitSlider.setMinValue(1);
		lowerLimitSlider.setMaxValue(scales.length);
		lowerLimitSlider.setMessage("Layer will become invisible after zoom level {0}");
		lowerLimitSlider.setValue(getScaleIndex(layer.getMaxScale(), false));
		layoutContainer.add(new FieldLabel(lowerLimitSlider, "Lower visibility limit"));
		
		return layoutContainer;
	}
	
	private VerticalLayoutContainer assemblePolygonStyleEditorLayout(){
		this.color = layer.getColor();
		this.fillColor = layer.getFillColor();
		
		VerticalLayoutContainer layoutContainer = new VerticalLayoutContainer();
		
		textureCheckBox = new CheckBox();
		textureCheckBox.setValue(layer.getStyle().contains("texture"));
		layoutContainer.add(new FieldLabel(textureCheckBox, "Textured"));
		
		ListStore<KingdomTexture> textures = new ListStore<KingdomTexture>(textureProps.imageName());
		textures.addAll(KingdomTexture.getTextureList());
		
		textureComboBox = new ComboBox<KingdomTexture>(textures, textureProps.title(), new AbstractSafeHtmlRenderer<KingdomTexture>() {
			final ComboBoxTemplates	comboBoxTemplates	= GWT.create(ComboBoxTemplates.class);
			
			@Override
			public SafeHtml render(KingdomTexture object){
				return comboBoxTemplates.texture(object.getIcon().getSafeUri(), object.getTitle());
			}
		});
		
		textureComboBox.setValue(KingdomTexture.getTextureByImageName(textures.getAll(), layer.getTextureImage()));
		
		textureComboBox.setTriggerAction(TriggerAction.ALL);
		layoutContainer.add(new FieldLabel(textureComboBox, "Texture"));
		
		String[] strokeColors = new String[] { layer.getColor() };
		colorPalette = new ColorPalette(strokeColors, strokeColors);
		colorPalette.addDomHandler(colorPaletteClickHandler, ClickEvent.getType());
		
		colorPaletteFieldLabel = new FieldLabel();
		colorPaletteFieldLabel.setWidget(colorPalette);
		colorPaletteFieldLabel.setText("Stroke color");
		layoutContainer.add(colorPaletteFieldLabel);
		
		String[] fillColors = new String[] { layer.getFillColor() };
		fillColorPalette = new ColorPalette(fillColors, fillColors);
		fillColorPalette.addDomHandler(fillColorPaletteClickHandler, ClickEvent.getType());
		
		fillColorPaletteFieldLabel = new FieldLabel();
		fillColorPaletteFieldLabel.setWidget(fillColorPalette);
		fillColorPaletteFieldLabel.setText("Fill color");
		layoutContainer.add(fillColorPaletteFieldLabel);
		
		sizeSpinner = new IntegerSpinnerField();
		sizeSpinner.setIncrement(1);
		sizeSpinner.setMinValue(1);
		sizeSpinner.setMaxValue(1000);
		sizeSpinner.setEditable(true);
		sizeSpinner.setValue(layer.getStrokeWidth());
		layoutContainer.add(new FieldLabel(sizeSpinner, "Stroke width"));
		
		strokeOpacitySpinner = new DoubleSpinnerField();
		strokeOpacitySpinner.setIncrement(.1d);
		strokeOpacitySpinner.setMaxValue(1d);
		strokeOpacitySpinner.setMinValue(0d);
		strokeOpacitySpinner.setEditable(false);
		strokeOpacitySpinner.getPropertyEditor().setFormat(NumberFormat.getFormat("0.0"));
		strokeOpacitySpinner.setValue(layer.getStrokeOpacity());
		layoutContainer.add(new FieldLabel(strokeOpacitySpinner, "Stroke opacity"));
		
		fillOpacitySpinner = new DoubleSpinnerField();
		fillOpacitySpinner.setIncrement(.1d);
		fillOpacitySpinner.setMaxValue(1d);
		fillOpacitySpinner.setMinValue(0d);
		fillOpacitySpinner.setEditable(false);
		fillOpacitySpinner.getPropertyEditor().setFormat(NumberFormat.getFormat("0.0"));
		fillOpacitySpinner.setValue(layer.getFillOpacity());
		layoutContainer.add(new FieldLabel(fillOpacitySpinner, "Fill opacity"));
		
		upperLimitSlider = new Slider();
		upperLimitSlider.setIncrement(1);
		upperLimitSlider.setMinValue(1);
		upperLimitSlider.setMaxValue(scales.length);
		upperLimitSlider.setMessage("Layer will be invisible until zoom level {0}");
		upperLimitSlider.setValue(getScaleIndex(layer.getMinScale(), true));
		layoutContainer.add(new FieldLabel(upperLimitSlider, "Upper visibility limit"));
		
		lowerLimitSlider = new Slider();
		lowerLimitSlider.setIncrement(1);
		lowerLimitSlider.setMinValue(1);
		lowerLimitSlider.setMaxValue(scales.length);
		lowerLimitSlider.setMessage("Layer will become invisible after zoom level {0}");
		lowerLimitSlider.setValue(getScaleIndex(layer.getMaxScale(), false));
		layoutContainer.add(new FieldLabel(lowerLimitSlider, "Lower visibility limit"));
		
		return layoutContainer;
	}
	
	private VerticalLayoutContainer assembleMarkerStyleEditorLayout(){
		this.fillColor = layer.getFillColor();
		this.color = layer.getColor();
		this.labelColor = layer.getLabelColor();
		this.labelFillColor = layer.getLabelFillColor();
		
		VerticalLayoutContainer layoutContainer = new VerticalLayoutContainer();
		
		ListStore<KingdomMarker> markers = new ListStore<KingdomMarker>(markerProps.imageName());
		markers.addAll(KingdomMarker.getMarkerList());
		
		markerComboBox = new ComboBox<KingdomMarker>(markers, markerProps.title(), new AbstractSafeHtmlRenderer<KingdomMarker>() {
			final ComboBoxTemplates	comboBoxTemplates	= GWT.create(ComboBoxTemplates.class);
			
			@Override
			public SafeHtml render(KingdomMarker object){
				return comboBoxTemplates.marker(object.getIcon().getSafeUri(), object.getTitle());
			}
		});
		
		markerComboBox.setValue(KingdomMarker.getMarkerByImageName(markers.getAll(), layer.getMarkerImage()));
		markerComboBox.setTriggerAction(TriggerAction.ALL);
		layoutContainer.add(new FieldLabel(markerComboBox, "Marker"));
		
		sizeSpinner = new IntegerSpinnerField();
		sizeSpinner.setIncrement(1);
		sizeSpinner.setMinValue(1);
		sizeSpinner.setMaxValue(1000);
		sizeSpinner.setEditable(true);
		sizeSpinner.setValue(layer.getSize());
		layoutContainer.add(new FieldLabel(sizeSpinner, "Size"));
		
		labelCheckBox = new CheckBox();
		labelCheckBox.setValue(layer.getStyle().contains("label"));
		layoutContainer.add(new FieldLabel(labelCheckBox, "Labeled"));
		
		String[] colors = new String[] { layer.getColor() };
		colorPalette = new ColorPalette(colors, colors);
		colorPalette.addDomHandler(colorPaletteClickHandler, ClickEvent.getType());
		
		colorPaletteFieldLabel = new FieldLabel();
		colorPaletteFieldLabel.setWidget(colorPalette);
		colorPaletteFieldLabel.setText("Color");
		layoutContainer.add(colorPaletteFieldLabel);
		
		String[] labelColors = new String[] { layer.getLabelColor() };
		labelColorPalette = new ColorPalette(labelColors, labelColors);
		labelColorPalette.addDomHandler(labelColorPaletteClickHandler, ClickEvent.getType());
		
		labelColorPaletteFieldLabel = new FieldLabel();
		labelColorPaletteFieldLabel.setWidget(labelColorPalette);
		labelColorPaletteFieldLabel.setText("Label background color");
		layoutContainer.add(labelColorPaletteFieldLabel);
		
		String[] labelFillColors = new String[] { layer.getLabelFillColor() };
		labelFillColorPalette = new ColorPalette(labelFillColors, labelFillColors);
		labelFillColorPalette.addDomHandler(labelFillColorPaletteClickHandler, ClickEvent.getType());
		
		labelFillColorPaletteFieldLabel = new FieldLabel();
		labelFillColorPaletteFieldLabel.setWidget(labelFillColorPalette);
		labelFillColorPaletteFieldLabel.setText("Label color");
		layoutContainer.add(labelFillColorPaletteFieldLabel);
		
		upperLimitSlider = new Slider();
		upperLimitSlider.setIncrement(1);
		upperLimitSlider.setMinValue(1);
		upperLimitSlider.setMaxValue(scales.length);
		upperLimitSlider.setMessage("Layer will be invisible until zoom level {0}");
		upperLimitSlider.setValue(getScaleIndex(layer.getMinScale(), true));
		layoutContainer.add(new FieldLabel(upperLimitSlider, "Upper visibility limit"));
		
		lowerLimitSlider = new Slider();
		lowerLimitSlider.setIncrement(1);
		lowerLimitSlider.setMinValue(1);
		lowerLimitSlider.setMaxValue(scales.length);
		lowerLimitSlider.setMessage("Layer will become invisible after zoom level {0}");
		lowerLimitSlider.setValue(getScaleIndex(layer.getMaxScale(), false));
		layoutContainer.add(new FieldLabel(lowerLimitSlider, "Lower visibility limit"));
		
		return layoutContainer;
	}
	
	@Override
	public void show(){
		super.show();
	}
	
	@Override
	public void hide(){
		super.hide();
	}
	
	@Override
	public boolean isBound(){
		return isBound;
	}
	
	@Override
	public void setIsBound(boolean isBound){
		this.isBound = isBound;
	}
	
	@Override
	public KingdomLayer getLayer(){
		return layer;
	}
	
	@Override
	public ColorPalette getColorPalette(){
		return colorPalette;
	}
	
	@Override
	public ColorPalette getFillColorPalette(){
		return colorPalette;
	}
	
	@Override
	public ColorPalette getLabelColorPalette(){
		return labelColorPalette;
	}
	
	@Override
	public ColorPalette getLabelFillColorPalette(){
		return labelFillColorPalette;
	}
	
	private void setColor(String color){
		this.color = color;
		String[] colors = new String[] { color };
		colorPalette = new ColorPalette(colors, colors);
		colorPalette.addDomHandler(colorPaletteClickHandler, ClickEvent.getType());
		colorPaletteFieldLabel.setWidget(colorPalette);
	}
	
	private void setFillColor(String color){
		this.fillColor = color;
		String[] fillColors = new String[] { color };
		fillColorPalette = new ColorPalette(fillColors, fillColors);
		fillColorPalette.addDomHandler(fillColorPaletteClickHandler, ClickEvent.getType());
		fillColorPaletteFieldLabel.setWidget(fillColorPalette);
	}
	
	private void setLabelColor(String color){
		this.labelColor = color;
		String[] labelColors = new String[] { color };
		labelColorPalette = new ColorPalette(labelColors, labelColors);
		labelColorPalette.addDomHandler(labelColorPaletteClickHandler, ClickEvent.getType());
		labelColorPaletteFieldLabel.setWidget(labelColorPalette);
	}
	
	private void setLabelFillColor(String color){
		this.labelFillColor = color;
		String[] labelFillColors = new String[] { color };
		labelFillColorPalette = new ColorPalette(labelFillColors, labelFillColors);
		labelFillColorPalette.addDomHandler(labelFillColorPaletteClickHandler, ClickEvent.getType());
		labelFillColorPaletteFieldLabel.setWidget(labelFillColorPalette);
	}
	
	@Override
	public int getSize(){
		return sizeSpinner.getValue();
	}
	
	@Override
	public TextButton getApplyButton(){
		return applyButton;
	}
	
	@Override
	public TextButton getCancelButton(){
		return cancelButton;
	}
	
	@Override
	public String getColor(){
		return color;
	}
	
	@Override
	public String getFillColor(){
		return fillColor;
	}
	
	@Override
	public String getLabelColor(){
		return labelColor;
	}
	
	@Override
	public String getLabelFillColor(){
		return labelFillColor;
	}
	
	@Override
	public void setColorPickerWindow(ColorPickerWindow colorPickerWindow){
		this.colorPickerWindow = colorPickerWindow;
	}
	
	@Override
	public ColorPickerWindow getColorPickerWindow(){
		return colorPickerWindow;
	}
	
	@Override
	public DoubleSpinnerField getStrokeOpacitySpinner(){
		return strokeOpacitySpinner;
	}
	
	@Override
	public DoubleSpinnerField getFillOpacitySpinner(){
		return fillOpacitySpinner;
	}
	
	@Override
	public ComboBox<KingdomMarker> getMarkerComboBox(){
		return markerComboBox;
	}
	
	@Override
	public ComboBox<KingdomTexture> getTextureComboBox(){
		return textureComboBox;
	}
	
	@Override
	public Slider getUpperLimitSlider(){
		return upperLimitSlider;
	}
	
	@Override
	public Slider getLowerLimitSlider(){
		return lowerLimitSlider;
	}
	
	@Override
	public CheckBox getLabelCheckBox(){
		return labelCheckBox;
	}
	
	@Override
	public CheckBox getTextureCheckBox(){
		return textureCheckBox;
	}
	
	@Override
	public double getUpperSliderScale(){
		return scales[upperLimitSlider.getValue() - 1];
	}
	
	@Override
	public double getLowerSliderScale(){
		return scales[lowerLimitSlider.getValue() - 1];
	}
	
	private int getScaleIndex(double scale, boolean upper){
		for(int i = 0; i < scales.length; i++){
			if(Math.round(scale) == Math.round(scales[i])){
				return (i + 1);
			}
		}
		
		if(upper){
			return 1;
		}else{
			return scales.length;
		}
	}
}
