package si.roskar.diploma.client.util;

import com.sencha.gxt.widget.core.client.box.AutoProgressMessageBox;
import com.sencha.gxt.widget.core.client.info.DefaultInfoConfig;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.info.InfoConfig.InfoPosition;

public final class KingdomInfo{
	static AutoProgressMessageBox	loadingBar	= null;
	
	private KingdomInfo(){
		
	}
	
	public static void showLoadingBar(String header, String message, String progressText){
		loadingBar = new AutoProgressMessageBox(header, message);
		loadingBar.setProgressText(progressText);
		loadingBar.auto();
		loadingBar.show();
	}
	
	public static void hideLoadingBar(){
		if(loadingBar != null){
			loadingBar.hide();
		}
	}
	
	public static void showInfoPopUp(String title, String message){
		DefaultInfoConfig infoConfig = new DefaultInfoConfig(title, message);
		infoConfig.setHeight(65);
		infoConfig.setPosition(InfoPosition.BOTTOM_RIGHT);
		Info.display(infoConfig);
	}
}
