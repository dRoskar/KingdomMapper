package si.roskar.diploma.client.view;

import si.roskar.diploma.client.presenter.LayerPresenter.AddLayerDisplay;
import si.roskar.diploma.client.resources.Resources;
import si.roskar.diploma.shared.GeometryType;

import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.toolbar.FillToolItem;

public class AddLayerDialog extends Window implements AddLayerDisplay{
	
	private TextField		name			= null;
	private ToggleButton	pointType		= null;
	private ToggleButton	lineType		= null;
	private ToggleButton	polygonType		= null;
	private TextButton		addButton		= null;
	private TextButton		cancelButton	= null;
	private ToggleGroup		toggleGroup		= null;
	private boolean			isBound			= false;
	
	public AddLayerDialog(){
		super();
		setUp();
	}
	
	private void setUp(){
		setHeadingText("Add new layer");
		setModal(true);
		setResizable(false);
		setBodyStyle("padding:6px");
		
		ContentPanel cp = new ContentPanel();
		cp.setBorders(false);
		cp.setHeaderVisible(false);
		cp.setButtonAlign(BoxLayoutPack.END);
		cp.setBodyBorder(false);
		cp.setBodyStyle("background:transparent");
		
		VerticalLayoutContainer layoutContainer = new VerticalLayoutContainer();
		
		name = new TextField();
		name.setAllowBlank(false);
		
		layoutContainer.add(new FieldLabel(name, "Name"), new VerticalLayoutData(1, -1));
		
		toggleGroup = new ToggleGroup();
		
		pointType = new ToggleButton();
		pointType.setIcon(Resources.ICONS.pointBig());
		pointType.setToolTip("Point");
		pointType.setItemId(GeometryType.POINT);
		
		lineType = new ToggleButton();
		lineType.setIcon(Resources.ICONS.lineBig());
		lineType.setToolTip("Line");
		lineType.setItemId(GeometryType.LINE);
		
		polygonType = new ToggleButton();
		polygonType.setIcon(Resources.ICONS.polygonBig());
		polygonType.setToolTip("Polygon");
		polygonType.setItemId(GeometryType.POLYGON);
		
		toggleGroup.add(pointType);
		toggleGroup.add(lineType);
		toggleGroup.add(polygonType);
		
		// geometry type selection group
		FieldSet fs = new FieldSet();
		fs.setHeadingHtml("Geometry type");
		
		VerticalLayoutContainer fsContainer = new VerticalLayoutContainer();
		
		ButtonBar bb = new ButtonBar();
		bb.setLayoutData(new VerticalLayoutData(1, -1));
		bb.add(new FillToolItem());
		bb.add(pointType);
		bb.add(lineType);
		bb.add(polygonType);
		bb.add(new FillToolItem());
		bb.forceLayout();
		
		fsContainer.add(bb);
		
		fs.add(fsContainer);
		
		layoutContainer.add(fs);
		
		cp.add(layoutContainer);
		
		addButton = new TextButton("add");
		cancelButton = new TextButton("cancel");
		
		cp.addButton(addButton);
		cp.addButton(cancelButton);
		
		this.add(cp);
	}
	
	@Override
	public void show(){
		super.show();
		
		name.clear();
	}
	
	@Override
	public void hide(){
		super.hide();
	}
	
	@Override
	public void setIsBound(boolean isBound){
		this.isBound = isBound;
	}
	
	@Override
	public boolean isBound(){
		return isBound;
	}
	
	@Override
	public TextButton getAddButton(){
		return addButton;
	}
	
	@Override
	public TextButton getCancelButton(){
		return cancelButton;
	}
	
	@Override
	public TextField getNameField(){
		return name;
	}
	
	@Override
	public ToggleGroup getToggleGroup(){
		return toggleGroup;
	}
	
	@Override
	public boolean isValid(){
		boolean isValid = true;
		
		name.clearInvalid();
		
		if(!name.isValid()){
			isValid = false;
		}
		
		// if none of the buttons are depressed
		if(!pointType.getValue() && !lineType.getValue() && !polygonType.getValue()){
			isValid = false;
		}
		
		return isValid;
	}
}
