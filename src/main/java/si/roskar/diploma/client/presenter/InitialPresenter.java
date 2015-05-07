package si.roskar.diploma.client.presenter;

import si.roskar.diploma.client.view.MapView;
import si.roskar.diploma.client.view.View;

import com.google.gwt.user.client.ui.HasWidgets;

public class InitialPresenter extends PresenterImpl<InitialPresenter.Display>{
	
	public interface Display extends View{
		
		public HasWidgets getCenterContainer();
		
		public HasWidgets getWestContainer();
		
		public void forceLayout();
	}
	
	public InitialPresenter(Display display){
		super(display);
	}
	
	@Override
	public void go(HasWidgets container){
		container.clear();
		container.add(display.asWidget());
		
		// create map presenter
		new MapPresenter(new MapView()).go(display.getCenterContainer());
	}
	
	@Override
	protected void bind(){
		
	}
}