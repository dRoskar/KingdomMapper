package si.roskar.diploma.client.view;

import java.util.List;

import si.roskar.diploma.client.presenter.MapPresenter.FeatureInfoSelectionDisplay;
import si.roskar.diploma.shared.KingdomFeature;

import com.google.gwt.dom.client.Element;
import com.sencha.gxt.core.client.Style.Anchor;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

public class FeatureInfoSelectionMenu extends Menu implements FeatureInfoSelectionDisplay{
	
	List<KingdomFeature>	features	= null;
	boolean					isBound		= false;
	
	public FeatureInfoSelectionMenu(){
		super();
	}
	
	@Override
	public void setFeatures(List<KingdomFeature> features){
		this.features = features;
		super.clear();
		
		// populate menu
		for(KingdomFeature feature : features){
			MenuItem menuItem = new MenuItem(feature.getLabel().equals("") ? feature.getFeatureId() : feature.getLabel());
			menuItem.setId(feature.getFeatureId());
			
			super.add(menuItem);
		}
	}
	
	@Override
	public void show(Element element, int x, int y){
		super.show(element, new AnchorAlignment(Anchor.TOP_LEFT, Anchor.TOP_LEFT), x, y);
	}
	
	@Override
	public boolean isBound(){
		return isBound;
	}
	
	@Override
	public void setIsBound(boolean isBound){
		this.isBound = isBound;
	}

	@Override
	public Menu getMenu(){
		return this;
	}

	@Override
	public KingdomFeature getFeatureById(String id){
		for(KingdomFeature feature : features){
			if(feature.getFeatureId().equals(id)){
				return feature;
			}
		}
		
		return null;
	}
}
