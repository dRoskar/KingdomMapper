package si.roskar.diploma.client.view;

import si.roskar.diploma.client.presenter.MapPresenter.EditFeatureDisplay;
import si.roskar.diploma.client.util.KingdomFieldValidator;
import si.roskar.diploma.client.util.KingdomValidation;
import si.roskar.diploma.shared.KingdomFeature;

import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

public class EditFeatureDialog extends Window implements EditFeatureDisplay{
	
	private KingdomFeature	feature		= null;
	private TextField		label		= null;
	private TextArea		description	= null;
	private TextButton		saveButton	= null;
	private TextButton		closeButton	= null;
	
	public EditFeatureDialog(KingdomFeature feature){
		super();
		this.feature = feature;
		setUp();
	}
	
	private void setUp(){
		setHeadingText("Feature info");
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
		label.setValue(feature.getLabel());
		cp.setBodyStyle("background:transparent");
		
		description = new TextArea();
		description.setWidth(250);
		description.setHeight(300);
		description.setValue(feature.getDescription());
		
		layoutContainer.add(new FieldLabel(label, "Label"), new VerticalLayoutData(1, -1));
		layoutContainer.add(new FieldLabel(description, "Description"), new VerticalLayoutData(1, -1));
		
		cp.add(layoutContainer);
		
		saveButton = new TextButton("Save");
		saveButton.disable();
		closeButton = new TextButton("Close");
		
		cp.addButton(saveButton);
		cp.addButton(closeButton);
		
		add(cp);
	}
	
	@Override
	public TextField getLabelField(){
		return label;
	}

	@Override
	public TextArea getDescriptionArea(){
		return description;
	}
	
	@Override
	public TextButton getSaveButton(){
		return saveButton;
	}
	
	@Override
	public TextButton getCloseButton(){
		return closeButton;
	}
	
	@Override
	public boolean isValid(){
		boolean isValid = true;
		KingdomValidation validation = null;
		
		label.clearInvalid();
		description.clearInvalid();
		
		// label
		validation = KingdomFieldValidator.validate(label.getText(), KingdomFieldValidator.TYPE_LABEL, true);
		
		if(!validation.isValid()){
			isValid = false;
			label.forceInvalid(validation.getInvalidMessage());
		}
		
		// description
		validation = KingdomFieldValidator.validate(description.getText(), KingdomFieldValidator.TYPE_DESCRIPTION, false);
		
		if(!validation.isValid()){
			isValid = false;
			description.forceInvalid(validation.getInvalidMessage());
		}
		
		return isValid;
	}
}
