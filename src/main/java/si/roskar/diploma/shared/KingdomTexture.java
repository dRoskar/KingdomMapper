package si.roskar.diploma.shared;

import java.util.ArrayList;
import java.util.List;

import si.roskar.diploma.client.resources.Resources;

import com.google.gwt.resources.client.ImageResource;

public class KingdomTexture{
	
	public static final String	GRASS_TEXTURE	= "texture_grass.png";
	public static final String	DESERT_TEXTURE	= "texture_desert.png";
	
	private String				imageName		= null;
	private String				title			= null;
	private ImageResource		icon			= null;
	
	public KingdomTexture(){
		
	}
	
	public KingdomTexture(String imageName, String title, ImageResource icon){
		super();
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
	
	public static List<KingdomTexture> getTextureList(){
		List<KingdomTexture> textures = new ArrayList<KingdomTexture>();
		textures.add(new KingdomTexture(GRASS_TEXTURE, "Grass", Resources.ICONS.textureGrass()));
		textures.add(new KingdomTexture(DESERT_TEXTURE, "Desert", Resources.ICONS.textureDesert()));
		
		return textures;
	}
	
	public static KingdomTexture getTextureByImageName(List<KingdomTexture> textures, String imageName){
		KingdomTexture stock = null;
		for(KingdomTexture texture : textures){
			if(texture.getImageName().equals(imageName)){
				return texture;
			}
			
			if(texture.getImageName().equals(GRASS_TEXTURE)){
				stock = texture;
			}
		}
		
		// if not found return default marker
		return stock;
	}
}
