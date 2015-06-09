package si.roskar.diploma.client.view;

import java.util.List;

import si.roskar.diploma.client.presenter.LayerPresenter.ExistingMapsDisplay;
import si.roskar.diploma.shared.KingdomMap;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;

public class ExistingMapsWindow extends Window implements ExistingMapsDisplay{
	
	interface MapProperties extends PropertyAccess<KingdomMap>{
		@Path("id")
		ModelKeyProvider<KingdomMap> key();
		
		@Path("name")
		ValueProvider<KingdomMap, String> nameProp();
	}
	
	ListStore<KingdomMap>			listStore	= null;
	ListView<KingdomMap, String>	listView	= null;
	TextButton						load		= null;
	TextButton						rename		= null;
	TextButton						delete		= null;
	TextButton						close		= null;
	private boolean					isBound		= false;
	
	public ExistingMapsWindow(){
		super();
		setUp();
	}
	
	private void setUp(){
		MapProperties properties = GWT.create(MapProperties.class);
		
		setHeadingText("Map list");
		setModal(true);
		setResizable(false);
		setWidth(420);
		setHeight(400);
		setBodyStyle("padding:6px");
		
		ContentPanel cp = new ContentPanel();
		cp.setBorders(false);
		cp.setHeaderVisible(false);
		cp.setButtonAlign(BoxLayoutPack.END);
		cp.setBodyBorder(false);
		cp.setBodyStyle("background:transparent");
		
		HorizontalLayoutContainer layoutContainer = new HorizontalLayoutContainer();
		
		listStore = new ListStore<KingdomMap>(properties.key());
		
		listView = new ListView<KingdomMap, String>(listStore, properties.nameProp());
		
		layoutContainer.add(listView, new HorizontalLayoutData(1, 1, new Margins(5)));
		
		cp.add(layoutContainer);
		
		load = new TextButton("Load");
		rename = new TextButton("Rename");
		delete = new TextButton("Delete");
		close = new TextButton("Close");
		
		cp.addButton(load);
		cp.addButton(rename);
		cp.addButton(delete);
		cp.addButton(close);
		
		this.add(cp);
	}
	
	@Override
	public void show(){
		super.show();
	}
	
	@Override
	public void hide(){
		super.hide();
	}
	
	@Override
	public void setMapData(List<KingdomMap> maps){
		listStore.clear();
		
		for(KingdomMap map : maps){
			listStore.add(map);
		}
		
		listView.refresh();
	}
	
	@Override
	public void setIsBound(boolean isBound){
		this.isBound = isBound;
	}
	
	@Override
	public boolean isBound(){
		return isBound;
	}
	
	@Override
	public TextButton getLoadButton(){
		return load;
	}
	
	@Override
	public TextButton getRenameButton(){
		return rename;
	}
	
	@Override
	public TextButton getDeleteButton(){
		return delete;
	}
	
	@Override
	public TextButton getCloseButton(){
		return close;
	}
	
	@Override
	public ListView<KingdomMap, String> getListView(){
		return listView;
	}
}
