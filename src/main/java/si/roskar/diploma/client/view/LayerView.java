package si.roskar.diploma.client.view;

import java.util.List;

import si.roskar.diploma.client.presenter.LayerPresenter.Display;
import si.roskar.diploma.client.resources.Resources;
import si.roskar.diploma.shared.KingdomLayer;
import si.roskar.diploma.shared.KingdomMap;
import si.roskar.diploma.shared.KingdomUser;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.sencha.gxt.widget.core.client.tree.Tree;

public class LayerView implements Display{
	
	interface LayerProperties extends PropertyAccess<KingdomLayer>{
		@Path("id")
		ModelKeyProvider<KingdomLayer> key();
		
		@Path("name")
		ValueProvider<KingdomLayer, String> nameProp();
	}
	
	private VerticalLayoutContainer		container		= null;
	private TextButton					newMap			= null;
	private TextButton					existingMaps	= null;
	private TextButton					addLayer		= null;
	private TreeStore<KingdomLayer>		layerStore		= null;
	private Tree<KingdomLayer, String>	layerTree		= null;
	private ContentPanel				layerTreePanel	= null;
	private KingdomUser					currentUser		= null;
	private KingdomMap					currentMap		= null;
	
	public LayerView(){
		LayerProperties properties = GWT.create(LayerProperties.class);
		
		// ===== ===== TEMP DEFAULT USER = BORIS ===== ===== =====
		currentUser = new KingdomUser(1, "Boris", "theforefather");
		
		// =======================================================
		
		// create map toolbar
		ToolBar mapToolbar = new ToolBar();
		newMap = new TextButton("New map", Resources.ICONS.mapAdd());
		existingMaps = new TextButton("Existing maps", Resources.ICONS.mapLoad());
		mapToolbar.add(newMap);
		mapToolbar.add(existingMaps);
		
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
		
		layerStore = new TreeStore<KingdomLayer>(properties.key());
		
		layerTree = new Tree<KingdomLayer, String>(layerStore, properties.nameProp());
		layerTree.setCheckable(true);
		layerTree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		layerTreePanel.add(layerTree);
		
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
	public TextButton getExistingMapsButton(){
		return existingMaps;
	}
	
	@Override
	public void setCurrentUser(KingdomUser currentUser){
		this.currentUser = currentUser;
	}
	
	@Override
	public KingdomUser getCurrentUser(){
		return currentUser;
	}
	
	@Override
	public void setCurrentMap(KingdomMap currentMap){
		this.currentMap = currentMap;
	}
	
	@Override
	public KingdomMap getCurrentMap(){
		return currentMap;
	}
	
	@Override
	public void addLayer(KingdomLayer layer){
		layerStore.add(layer);
		layerTree.setChecked(layer, Tree.CheckState.CHECKED);
		layerTree.getSelectionModel().select(layer, false);
	}
	
	@Override
	public void addLayers(List<KingdomLayer> layers){
		layerStore.clear();
		
		for(KingdomLayer layer : layers){
			layerStore.add(layer);
			layerTree.setChecked(layer, Tree.CheckState.CHECKED);
			layerTree.getSelectionModel().select(layer, false);
		}
	}
	
	@Override
	public void enableLayerView(){
		layerTreePanel.enable();
		addLayer.enable();
	}
}
