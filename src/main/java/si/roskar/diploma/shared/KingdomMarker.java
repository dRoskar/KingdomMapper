package si.roskar.diploma.shared;

import java.util.ArrayList;
import java.util.List;

import si.roskar.diploma.client.resources.Resources;

import com.google.gwt.resources.client.ImageResource;

public class KingdomMarker{
	
	public static final String	GREEN_MARKER	= "marker_green.png";
	public static final String	MAN_MARKER		= "marker_man.png";
	public static final String	QUEST_MARKER	= "marker_quest.png";
	public static final String	REWARD_MARKER	= "marker_reward.png";
	
	private String				imageName		= null;
	private String				title			= null;
	private ImageResource		icon			= null;
	
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
		markers.add(new KingdomMarker(GREEN_MARKER, "Green", Resources.ICONS.marker()));
		markers.add(new KingdomMarker(MAN_MARKER, "Man", Resources.ICONS.markerMan()));
		markers.add(new KingdomMarker(QUEST_MARKER, "Quest", Resources.ICONS.markerQuest()));
		markers.add(new KingdomMarker(REWARD_MARKER, "Reward", Resources.ICONS.markerReward()));
		
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