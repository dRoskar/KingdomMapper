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
		
		@Source("shape_move_front.png")
		ImageResource moveToFront();
		
		@Source("shape_move_back.png")
		ImageResource sendToBack();
		
		@Source ("vertex_move.png")
		ImageResource vertexMove();
		
		@Source ("polygon_move.png")
		ImageResource polygonMove();
		
		@Source ("line_delete.png")
		ImageResource lineDelete();
		
		@Source ("ring_add.png")
		ImageResource holeAdd();
		
		@Source ("erase.png")
		ImageResource erase();
	}
	
	public static final Icons	ICONS	= GWT.create(Icons.class);
}