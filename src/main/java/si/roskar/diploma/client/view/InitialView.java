package si.roskar.diploma.client.view;

import si.roskar.diploma.client.presenter.InitialPresenter.Display;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.Viewport;

public class InitialView implements Display{
	
	private Viewport		viewport	= null;
	private ContentPanel	center		= null;
	private ContentPanel	west		= null;
	
	public InitialView(){
	}
	
	@Override
	public Widget asWidget(){
		BorderLayoutContainer layoutContainer = new BorderLayoutContainer();
		
		// center panel
		MarginData centerData = new MarginData();
		centerData.setMargins(new Margins(5));
		
		center = new ContentPanel();
		center.setHeaderVisible(false);
		
		// west panel
		BorderLayoutData westData = new BorderLayoutData(300);
		westData.setCollapsible(true);
		westData.setSplit(true);
		westData.setCollapseMini(true);
		westData.setMargins(new Margins(5));
		
		west = new ContentPanel();
		west.setHeadingHtml("Layers");
		
		layoutContainer.setCenterWidget(center, centerData);
		layoutContainer.setWestWidget(west, westData);
		
		SimpleContainer container = new SimpleContainer();
		container.add(layoutContainer, new MarginData(0));
		
		viewport = new Viewport();
		viewport.add(container);
		
		return viewport;
	}
	
	@Override
	public HasWidgets getCenterContainer(){
		return center;
	}
	
	@Override
	public HasWidgets getWestContainer(){
		return west;
	}
	
	@Override
	public void forceLayout(){
		viewport.forceLayout();
	}
}
