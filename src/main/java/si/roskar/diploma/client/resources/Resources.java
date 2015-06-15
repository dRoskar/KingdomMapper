
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
		
		@Source("marker_man.png")
		ImageResource markerMan();
		
		@Source("marker_quest.png")
		ImageResource markerQuest();
		
		@Source("marker_reward.png")
		ImageResource markerReward();
		
		@Source("shape_move_front.png")
		ImageResource moveToFront();
		
		@Source("shape_move_back.png")
		ImageResource sendToBack();
		
		@Source ("vertex_move.png")
		ImageResource vertexMove();
		
		@Source ("point_move.png")
		ImageResource pointMove();
		
		@Source ("line_move.png")
		ImageResource lineMove();
		
		@Source ("polygon_move.png")
		ImageResource polygonMove();
		
		@Source ("marker_move.png")
		ImageResource markerMove();
		
		@Source ("line_delete.png")
		ImageResource lineDelete();
		
		@Source ("erase.png")
		ImageResource erase();
		
		@Source ("layer_style.png")
		ImageResource layerStyle();
		
		@Source ("polygon_takeaway.png")
		ImageResource polygonTake();
		
		@Source ("polygon_add.png")
		ImageResource polygonAdd();
		
		@Source ("line_snap.png")
		ImageResource lineSnap();
		
		@Source ("rectangle.png")
		ImageResource rectangle();
		
		@Source ("polygon_scale.png")
		ImageResource polygonScale();
		
		@Source ("polygon_rotate.png")
		ImageResource polygonRotate();
		
		@Source ("line_scale.png")
		ImageResource lineScale();
		
		@Source ("line_rotate.png")
		ImageResource lineRotate();
		
		@Source ("circle.png")
		ImageResource circle();
		
		@Source ("line_rectangle.png")
		ImageResource lineRectangle();
		
		@Source ("line_circle.png")
		ImageResource lineCircle();
		
		@Source ("ellipse.png")
		ImageResource ellipse();
		
		@Source ("square.png")
		ImageResource square();
		
		@Source ("rename.png")
		ImageResource rename();
		
		@Source ("upper_limit.png")
		ImageResource upperLimit();
		
		@Source ("lower_limit.png")
		ImageResource lowerLimit();
		
		@Source ("measure.png")
		ImageResource measure();
		
		@Source ("distance_measure.png")
		ImageResource measureDistance();
		
		@Source ("area_measure.png")
		ImageResource measureArea();
	}
	
	public static final Icons	ICONS	= GWT.create(Icons.class);
}