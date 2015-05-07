package si.roskar.diploma.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;

import si.roskar.diploma.client.view.View;

public class MapPresenter extends PresenterImpl<MapPresenter.Display>{
	
	public interface Display extends View{
		
	}
	
	public MapPresenter(Display display){
		super(display);
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

	@Override
	protected void bind() {
		
	}
}
