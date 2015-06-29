package si.roskar.diploma.shared;

import java.util.ArrayList;
import java.util.List;

import si.roskar.diploma.client.resources.Resources;

import com.google.gwt.resources.client.ImageResource;

public class KingdomMarker{
	
	public static final String	POINT_MARKER		= "point";
	public static final String	GREEN_MARKER		= "marker_green.svg";
	public static final String	RED_MARKER			= "marker_red.svg";
	public static final String	BLUE_MARKER			= "marker_blue.svg";
	public static final String	MAN_MARKER			= "marker_man.svg";
	public static final String	QUEST_MARKER		= "marker_quest.svg";
	public static final String	REWARD_MARKER		= "marker_reward.svg";
	public static final String	TREE_MARKER			= "marker_tree1.svg";
	public static final String	EVERGREEN_MARKER	= "marker_tree2.svg";
	public static final String	CACTUS_MARKER		= "marker_cactus.svg";
	public static final String	VILLAGE_MARKER		= "marker_village.svg";
	public static final String	CITY_MARKER			= "marker_city.svg";
	public static final String	CROSS_MARKER		= "marker_cross.svg";
	public static final String	CELTIC_CROSS_MARKER	= "marker_celtic_cross.svg";
	public static final String	MINE_MARKER			= "marker_mine.svg";
	public static final String	METULJ_MARKER		= "marker_metulj.svg";
	
	private String				imageName			= null;
	private String				title				= null;
	private ImageResource		icon				= null;
	
	public KingdomMarker(){
		
	}
	
	public KingdomMarker(String imageName, String title, ImageResource icon){
		this.imageName = imageName;
		this.title = title;
		this.icon = icon;
	}
	
	public String getImageName(){
		return imageName;
	}
	
	public void setImageName(String imageName){
		this.imageName = imageName;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public ImageResource getIcon(){
		return icon;
	}
	
	public void setIcon(ImageResource icon){
		this.icon = icon;
	}
	
	public static List<KingdomMarker> getMarkerList(){
		List<KingdomMarker> markers = new ArrayList<KingdomMarker>();
		markers.add(new KingdomMarker(POINT_MARKER, "Colored point", Resources.ICONS.markerPoint()));
		markers.add(new KingdomMarker(GREEN_MARKER, "Green", Resources.ICONS.markerGreen()));
		markers.add(new KingdomMarker(RED_MARKER, "Red", Resources.ICONS.markerRed()));
		markers.add(new KingdomMarker(BLUE_MARKER, "Blue", Resources.ICONS.markerBlue()));
		markers.add(new KingdomMarker(MAN_MARKER, "Man", Resources.ICONS.markerMan()));
		markers.add(new KingdomMarker(QUEST_MARKER, "Quest", Resources.ICONS.markerQuest()));
		markers.add(new KingdomMarker(REWARD_MARKER, "Reward", Resources.ICONS.markerReward()));
		markers.add(new KingdomMarker(TREE_MARKER, "Tree", Resources.ICONS.markerTree()));
		markers.add(new KingdomMarker(EVERGREEN_MARKER, "Evergreen", Resources.ICONS.markerEvergreen()));
		markers.add(new KingdomMarker(CACTUS_MARKER, "Cactus", Resources.ICONS.markerCactus()));
		markers.add(new KingdomMarker(VILLAGE_MARKER, "Village", Resources.ICONS.markerVillage()));
		markers.add(new KingdomMarker(CITY_MARKER, "City", Resources.ICONS.markerCity()));
		markers.add(new KingdomMarker(CROSS_MARKER, "Cross", Resources.ICONS.markerCross()));
		markers.add(new KingdomMarker(CELTIC_CROSS_MARKER, "Celtic cross", Resources.ICONS.markerCelticCross()));
		markers.add(new KingdomMarker(MINE_MARKER, "Mine", Resources.ICONS.markerMine()));
		markers.add(new KingdomMarker(METULJ_MARKER, "Amadej", Resources.ICONS.markerMetulj()));
		
		return markers;
	}
	
	public static KingdomMarker getMarkerByImageName(List<KingdomMarker> markers, String imageName){
		KingdomMarker stock = null;
		for(KingdomMarker marker : markers){
			if(marker.getImageName().equals(imageName)){
				return marker;
			}
			
			if(marker.getImageName().equals(GREEN_MARKER)){
				stock = marker;
			}
		}
		
		// if not found return default marker
		return stock;
	}
}