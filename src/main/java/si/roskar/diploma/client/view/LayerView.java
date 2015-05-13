package si.roskar.diploma.client.view;

import si.roskar.diploma.client.presenter.LayerPresenter.Display;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.sencha.gxt.widget.core.client.tree.TreeView;

public class LayerView implements Display{
	
	private VerticalLayoutContainer container	= null;
	private TextButton				addLayer	= null;
	private TreeView				layerTree	= null;
	
	public LayerView(){
		
		// create button bar
		ToolBar toolbar = new ToolBar();
		addLayer = new TextButton("Add Layer");
		toolbar.add(addLayer);
		
		// create content panel
		ContentPanel layerTreePanel = new ContentPanel();
		layerTreePanel.setHeadingHtml("Layers");
		
		// create layer tree
		
		
		// insert items into layout container
		container = new VerticalLayoutContainer();
		container.add(toolbar);
		container.add(layerTreePanel);
	}

	@Override
	public Widget asWidget() {
		return container;
	}
}
