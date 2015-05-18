package si.roskar.diploma.client.view;

import si.roskar.diploma.client.presenter.LayerPresenter.NewMapDisplay;

import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

public class NewMapDialog extends Window implements NewMapDisplay{
	
	private TextField		name			= null;
	private TextButton		saveButton		= null;
	private TextButton		cancelButton	= null;
	private boolean			isBound			= false;
	
	public NewMapDialog(){
		super();
		setUp();
	}
	
	private void setUp(){
		setHeadingText("Create new map");
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
		
		cp.add(layoutContainer);
		
		saveButton = new TextButton("save");
		cancelButton = new TextButton("cancel");
		
		cp.addButton(saveButton);
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
	public TextButton getSaveButton(){
		return saveButton;
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
	public boolean isValid(){
		name.clearInvalid();
		
		if(name.isValid()){
			return true;
		}
		
		return false;
	}
}
