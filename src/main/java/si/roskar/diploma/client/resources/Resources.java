package si.roskar.diploma.client.resources;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public class Resources{
	
	public interface Icons extends ClientBundle{
		
		@Source("add.png")
		ImageResource add();
		
		@Source("arrow_left.png")
		ImageResource left();
		
		@Source("arrow_right.png")
		ImageResource right();
		
		@Source("world.png")
		ImageResource world();
		
		@Source("point.png")
		ImageResource point();
		
		@Source("line.png")
		ImageResource line();
		
		@Source("polygon.png")
		ImageResource polygon();
		
		@Source("point_big.png")
		ImageResource pointBig();
		
		@Source("line_big.png")
		ImageResource lineBig();
		
		@Source("polygon_big.png")
		ImageResource polygonBig();
		
		@Source("map.png")
		ImageResource map();
		
		@Source("map_add.png")
		ImageResource mapAdd();
		
		@Source("map_delete.png")
		ImageResource mapDelete();
		
		@Source("map_load.png")
		ImageResource mapLoad();
		
		@Source("map_save.png")
		ImageResource mapSave();
		
		@Source("grid.png")
		ImageResource grid();
		
		@Source("delete.png")
		ImageResource delete();
		
		@Source("cancel.png")
		ImageResource cancel();
		
		@Source("accept.png")
		ImageResource accept();
		
		@Source("marker.png")
		ImageResource marker();
		
		@Source("marker_big.png")
		ImageResource markerBig();
	}
	
	public static final Icons	ICONS	= GWT.create(Icons.class);
}