package si.roskar.diploma.client.presenter;

import si.roskar.diploma.client.event.Bus;
import si.roskar.diploma.client.event.EventAddNewLayer;
import si.roskar.diploma.client.view.AddLayerDialog;
import si.roskar.diploma.client.view.View;
import si.roskar.diploma.shared.KingdomLayer;

import com.google.gwt.user.client.ui.HasWidgets;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextField;

public class LayerPresenter extends PresenterImpl<LayerPresenter.Display>{
	
	public interface Display extends View{
		TextButton getAddLayerButton();
	}
	
	public interface AddLayerDisplay extends View{
		void show();
		
		void hide();
		
		void setIsBound(boolean isBound);
		
		boolean isBound();
		
		TextButton getAddButton();
		
		TextButton getCancelButton();
		
		TextField getNameField();
		
		ToggleGroup getToggleGroup();
		
		boolean isValid();
	}
	
	private AddLayerDisplay	addLayerDisplay	= null;
	
	public LayerPresenter(Display display){
		super(display);
		addLayerDisplay = new AddLayerDialog();
	}
	
	@Override
	public void go(HasWidgets container){
		container.clear();
		container.add(display.asWidget());
	}
	
	@Override
	protected void bind(){
		display.getAddLayerButton().addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event){
				addLayerDisplay.show();
				
				// bind dialog events
				if(!addLayerDisplay.isBound()){
					
					addLayerDisplay.getAddButton().addSelectHandler(new SelectHandler() {
						@Override
						public void onSelect(SelectEvent event){
							if(addLayerDisplay.isValid()){
								// create new layer
								KingdomLayer newLayer = new KingdomLayer();
								newLayer.setLayerName(addLayerDisplay.getNameField().getText());
								newLayer.setVisibility(true);
								newLayer.setGeometryType(((ToggleButton)addLayerDisplay.getToggleGroup().getValue()).getItemId());
								
								// add layer to map
								Bus.get().fireEvent(new EventAddNewLayer(newLayer));
								
								addLayerDisplay.hide();
							}
						}
					});
					
					addLayerDisplay.getCancelButton().addSelectHandler(new SelectHandler() {
						@Override
						public void onSelect(SelectEvent event){
							addLayerDisplay.hide();
						}
					});
					
					addLayerDisplay.setIsBound(true);
				}
			}
		});
	}
}
