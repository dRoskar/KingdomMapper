package si.roskar.diploma.client.view;

import si.roskar.diploma.client.presenter.MapPresenter.CreditsDisplay;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.AccordionLayoutAppearance;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.ExpandMode;

public class CreditsWindow extends Window implements CreditsDisplay{
	
	AccordionLayoutAppearance appearance = GWT.<AccordionLayoutAppearance> create(AccordionLayoutAppearance.class);
	
	public CreditsWindow(){
		super();
		setUp();
	}
	
	private void setUp(){
		setHeadingText("Additional information");
		setWidth(400);
		setHeight(600);
		
		ContentPanel infoPanel = new ContentPanel(appearance);
		ContentPanel iconLicensePanel = new ContentPanel(appearance);
		
		
		infoPanel.setAnimCollapse(true);
		infoPanel.setHeadingText("Info");
		infoPanel.setBodyStyle("padding:6px");
		infoPanel.add(new Label("Kingdom Mapper was created by Damjan Roškar<br />"
								+ "More info coming soon..."));
		
		iconLicensePanel.setAnimCollapse(true);
		iconLicensePanel.setHeadingText("Icon license");
		iconLicensePanel.setBodyStyle("padding:6px");
		iconLicensePanel.add(new HTML("LICENSE<br />" + 
				"<br />" + 
				"Original GIS icons theme was created by Robert Szczepanek [1] and is licensed under a Creative Commons Attribution-Share Alike 3.0 Unported License [2].<br />" + 
				"Feel free to use it for GIS software or for any other purposes. I only ask you to let me know about that and to include licence.txt file in your work.<br />" + 
				"<br />" + 
				"TITLE:			gis-0.1<br />" + 
				"DESCRIPTION:	GIS icon theme<br />" + 
				"AUTHOR:			Robert Szczepanek	<br />" + 
				"CONTACT:		robert at szczepanek pl<br />" + 
				"SITE:			http://robert.szczepanek.pl/<br />" + 
				"<br />" + 
				"<br />" + 
				"ACKNOWLEDGEMENTS<br />" + 
				"		<br />" + 
				"OSGeo community [3] helped in whole design process. <br />" + 
				"Especially I want to acknowledge GRASS and QGIS team members for creative comments and support:<br />" + 
				"Martin Landa<br />" + 
				"Michael Barton<br />" + 
				"Tim Sutton<br />" + 
				"Borys Jurgiel<br />" + 
				"<br />" + 
				"Robert Szczepanek<br />" + 
				"Cracow 2009 <br />" + 
				"Poland<br />" + 
				"<br />" + 
				"[1] http://robert.szczepanek.pl/<br />" + 
				"[2] http://creativecommons.org/licenses/by-sa/3.0/<br />" + 
				"[3] http://www.osgeo.org/"));
		
		AccordionLayoutContainer accordion = new AccordionLayoutContainer();
		accordion.setExpandMode(ExpandMode.SINGLE_FILL);
		accordion.add(infoPanel);
		accordion.add(iconLicensePanel);
		accordion.setActiveWidget(infoPanel);
		
		add(accordion);
	}
}
