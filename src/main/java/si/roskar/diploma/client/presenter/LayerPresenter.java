package si.roskar.diploma.client.presenter;

import si.roskar.diploma.client.view.AddLayerDialog;
import si.roskar.diploma.client.view.View;

import com.google.gwt.user.client.ui.HasWidgets;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

public class LayerPresenter extends PresenterImpl<LayerPresenter.Display> {

	public interface Display extends View {
		TextButton getaddLayerButton();
	}
	
	public interface AddLayerDisplay extends View{
		void show();
		
		void hide();
		
		void setIsBound(boolean isBound);
		
		boolean isBound();
		
		TextButton getAddButton();
		
		TextButton getCancelButton();
	}
	
	private AddLayerDisplay addLayerDisplay = null;

	public LayerPresenter(Display display) {
		super(display);
		addLayerDisplay = new AddLayerDialog();
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

	@Override
	protected void bind() {
		display.getaddLayerButton().addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				addLayerDisplay.show();
				
				// bind dialog events
				if(!addLayerDisplay.isBound()){
					
					addLayerDisplay.getAddButton().addSelectHandler(new SelectHandler() {
						@Override
						public void onSelect(SelectEvent event) {
							addLayerDisplay.hide();
						}
					});
					
					addLayerDisplay.getCancelButton().addSelectHandler(new SelectHandler() {
						@Override
						public void onSelect(SelectEvent event) {
							addLayerDisplay.hide();
						}
					});
				}
			}
		});
	}
}
