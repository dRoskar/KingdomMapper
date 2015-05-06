package si.roskar.diploma.client;

import si.roskar.diploma.client.presenter.InitialPresenter;
import si.roskar.diploma.client.view.InitialView;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class KingdomMapper implements EntryPoint {

	public void onModuleLoad() {
		new InitialPresenter(new InitialView()).go(RootPanel.get());
	}
}
