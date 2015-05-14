package si.roskar.diploma.client.view;

import si.roskar.diploma.client.presenter.LayerPresenter.Display;
import si.roskar.diploma.client.resources.Resources;
import si.roskar.diploma.shared.KingdomLayer;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.sencha.gxt.widget.core.client.tree.Tree;

public class LayerView implements Display{
	
	private VerticalLayoutContainer				container		= null;
	private TextButton							newMap			= null;
	private TextButton							loadMap			= null;
	private TextButton							addLayer		= null;
	private TreeStore<KingdomLayer>				layerStore		= null;
	private Tree<KingdomLayer, KingdomLayer>	layerTree		= null;
	private ContentPanel						layerTreePanel	= null;
	
	public LayerView(){
		
		// create map toolbar
		ToolBar mapToolbar = new ToolBar();
		newMap = new TextButton("New map", Resources.ICONS.mapAdd());
		loadMap = new TextButton("Load map", Resources.ICONS.mapLoad());
		mapToolbar.add(newMap);
		mapToolbar.add(loadMap);
		
		// create layer button bar
		ToolBar layerToolbar = new ToolBar();
		addLayer = new TextButton("Add Layer", Resources.ICONS.add());
		addLayer.setToolTip("Add layer");
		addLayer.disable();
		layerToolbar.add(addLayer);
		
		// create content panel
		layerTreePanel = new ContentPanel();
		layerTreePanel.setHeadingHtml("Layers");
		layerTreePanel.disable();
		
		// insert items into layout container
		container = new VerticalLayoutContainer();
		container.add(mapToolbar);
		container.add(layerToolbar);
		container.add(layerTreePanel, new VerticalLayoutData(1, 1));
	}
	
	@Override
	public Widget asWidget(){
		return container;
	}
	
	@Override
	public TextButton getAddLayerButton(){
		return addLayer;
	}
	
	@Override
	public TextButton getNewMapButton(){
		return newMap;
	}
	
	@Override
	public TextButton getLoadMapButton(){
		return loadMap;
	}
	
	@Override
	public void enableLayerView(){
		layerTreePanel.enable();
		addLayer.enable();
	}
}
