package si.roskar.diploma.client.view;

import si.roskar.diploma.client.presenter.MapPresenter.AddMarkerDisplay;

import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

public class AddMarkerDialog extends Window implements AddMarkerDisplay{
	
	private TextField	label			= null;
	private TextArea	description		= null;
	private TextButton	addButton		= null;
	private TextButton	cancelButton	= null;
	private boolean		isBound			= false;
	private String		wktGeometry		= null;
	
	public AddMarkerDialog(){
		super();
		setUp();
	}
	
	private void setUp(){
		setHeadingText("Add new marker");
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
		
		label = new TextField();
		label.setAllowBlank(false);
		
		description = new TextArea();
		description.setWidth(250);
		description.setHeight(300);
		
		layoutContainer.add(new FieldLabel(label, "Label"), new VerticalLayoutData(1, -1));
		layoutContainer.add(new FieldLabel(description, "Description"), new VerticalLayoutData(1, -1));
		
		cp.add(layoutContainer);
		
		addButton = new TextButton("Add");
		cancelButton = new TextButton("Cancel");
		
		cp.addButton(addButton);
		cp.addButton(cancelButton);
		
		add(cp);
	}
	
	@Override
	public void show(String wktGeometry){
		this.wktGeometry = wktGeometry;
		
		label.clear();
		description.clear();
		super.show();
	}
	
	@Override
	public void hide(){
		super.hide();
	}
	
	@Override
	public TextField getLabelField(){
		return label;
	}
	
	@Override
	public TextArea getDescriptionField(){
		return description;
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
	public boolean isBound(){
		return isBound;
	}
	
	@Override
	public void setIsBound(boolean isBound){
		this.isBound = isBound;
	}
	
	@Override
	public boolean isValid(){
		return label.isValid();
	}
	
	@Override
	public void setGeometry(String wktGeometry){
		this.wktGeometry = wktGeometry;
	}
	
	@Override
	public String getGeometry(){
		return wktGeometry;
	}
}
