package si.roskar.diploma.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;

import si.roskar.diploma.client.view.View;

public class LayerPresenter extends PresenterImpl<LayerPresenter.Display> {

	public interface Display extends View {

	}

	public LayerPresenter(Display display) {
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
