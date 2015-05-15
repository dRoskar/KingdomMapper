package si.roskar.diploma.client.presenter;

import si.roskar.diploma.client.event.Bus;
import si.roskar.diploma.client.event.EventAddNewLayer;
import si.roskar.diploma.client.event.EventAddNewMap;
import si.roskar.diploma.client.event.EventChangeMapNameHeader;
import si.roskar.diploma.client.event.EventEnableMapView;
import si.roskar.diploma.client.view.AddLayerDialog;
import si.roskar.diploma.client.view.NewMapDialog;
import si.roskar.diploma.client.view.View;
import si.roskar.diploma.shared.KingdomLayer;
import si.roskar.diploma.shared.KingdomMap;

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
		
		TextButton getNewMapButton();
		
		TextButton getLoadMapButton();
		
		void enableLayerView();
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
	
	public interface NewMapDisplay extends View{
		void show();
		
		void hide();
		
		void setIsBound(boolean isBound);
		
		boolean isBound();
		
		TextButton getSaveButton();
		
		TextButton getCancelButton();
		
		TextField getNameField();
		
		boolean isValid();
	}
	
	private AddLayerDisplay	addLayerDisplay	= null;
	private NewMapDisplay	newMapDisplay	= null;
	
	public LayerPresenter(Display display){
		super(display);
		
		addLayerDisplay = new AddLayerDialog();
		newMapDisplay = new NewMapDialog();
	}
	
	@Override
	public void go(HasWidgets container){
		container.clear();
		container.add(display.asWidget());
	}
	
	@Override
	protected void bind(){
		
		// handle new map clicks
		display.getNewMapButton().addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event){
				newMapDisplay.show();
				
				// bind events
				if(!newMapDisplay.isBound()){
					
					newMapDisplay.getSaveButton().addSelectHandler(new SelectHandler() {
						
						@Override
						public void onSelect(SelectEvent event){
							if(newMapDisplay.isValid()){
								// create new map
								KingdomMap newMap = new KingdomMap();
								newMap.setName(newMapDisplay.getNameField().getText());
								
								// enable controls
								display.enableLayerView();
								
								// add map to map view
								Bus.get().fireEvent(new EventAddNewMap(newMap));
								
								// display map name
								Bus.get().fireEvent(new EventChangeMapNameHeader(newMap.getName()));
								
								// hide dialog
								newMapDisplay.hide();
							}
						}
					});
					
					newMapDisplay.getCancelButton().addSelectHandler(new SelectHandler() {
						
						@Override
						public void onSelect(SelectEvent event){
							newMapDisplay.hide();
						}
					});
					
					newMapDisplay.setIsBound(true);
				}
			}
		});
		
		// handle new layer clicks
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
								newLayer.setGeometryType(((ToggleButton) addLayerDisplay.getToggleGroup().getValue()).getItemId());
								
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
