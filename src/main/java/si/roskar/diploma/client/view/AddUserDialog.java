package si.roskar.diploma.client.view;

import si.roskar.diploma.client.presenter.MapPresenter.AddUserDisplay;

import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextField;

public class AddUserDialog extends Window implements AddUserDisplay{
	
	private TextField		usernameField	= null;
	private PasswordField	passwordField	= null;
	private TextButton		addButton		= null;
	private TextButton		cancelButton	= null;
	private boolean			isBound			= false;
	
	public AddUserDialog(){
		super();
		setUp();
	}
	
	private void setUp(){
		setHeadingText("Add new user");
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
		
		usernameField = new TextField();
		usernameField.setAllowBlank(false);
		
		passwordField = new PasswordField();
		passwordField.setAllowBlank(false);
		
		layoutContainer.add(new FieldLabel(usernameField, "Username"), new VerticalLayoutData(1, -1));
		layoutContainer.add(new FieldLabel(passwordField, "Password"), new VerticalLayoutData(1, -1));
		
		cp.add(layoutContainer);
		
		addButton = new TextButton("add");
		cancelButton = new TextButton("cancel");
		
		cp.addButton(addButton);
		cp.addButton(cancelButton);
		
		this.add(cp);
	}
	
	@Override
	public TextField getUsernameField(){
		return usernameField;
	}
	
	@Override
	public PasswordField getPasswordField(){
		return passwordField;
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
	public void setBound(boolean isBound){
		this.isBound = isBound;
	}
	
	@Override
	public void hide(){
		usernameField.clear();
		passwordField.clear();
		super.hide();
	}
	
	@Override
	public boolean isValid(){
		boolean isValid = true;
		
		usernameField.clearInvalid();
		
		if(!usernameField.isValid()){
			isValid = false;
		}

		if(!passwordField.isValid()){
			isValid = false;
		}
		
		return isValid;
	}
}
