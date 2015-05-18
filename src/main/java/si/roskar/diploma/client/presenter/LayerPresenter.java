package si.roskar.diploma.client.presenter;

import java.util.List;

import si.roskar.diploma.client.DataServiceAsync;
import si.roskar.diploma.client.event.Bus;
import si.roskar.diploma.client.event.EventAddNewMap;
import si.roskar.diploma.client.event.EventChangeMapNameHeader;
import si.roskar.diploma.client.view.AddLayerDialog;
import si.roskar.diploma.client.view.ExistingMapsWindow;
import si.roskar.diploma.client.view.NewMapDialog;
import si.roskar.diploma.client.view.View;
import si.roskar.diploma.shared.KingdomLayer;
import si.roskar.diploma.shared.KingdomMap;
import si.roskar.diploma.shared.KingdomUser;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextField;

public class LayerPresenter extends PresenterImpl<LayerPresenter.Display>{
	
	public interface Display extends View{
		
		TextButton getAddLayerButton();
		
		TextButton getNewMapButton();
		
		TextButton getExistingMapsButton();
		
		void setCurrentUser(KingdomUser currentUser);
		
		KingdomUser getCurrentUser();
		
		void setCurrentMap(KingdomMap currentMap);
		
		KingdomMap getCurrentMap();
		
		void enableLayerView();
	}
	
	public interface AddLayerDisplay extends View{
		void show();
		
		void hide();
		
		void setIsBound(boolean isBound);
		
		boolean isBound();
		
		TextButton getAddButton();
		
		TextButton getCancelButton();
		
		TextField getNameField();
		
		ToggleGroup getToggleGroup();
		
		boolean isValid();
	}
	
	public interface NewMapDisplay extends View{
		void show();
		
		void hide();
		
		void setIsBound(boolean isBound);
		
		boolean isBound();
		
		TextButton getSaveButton();
		
		TextButton getCancelButton();
		
		TextField getNameField();
		
		boolean isValid();
	}
	
	public interface ExistingMapsDisplay extends View{
		void show();
		
		void hide();
		
		void setMapData(List<KingdomMap> maps);
		
		void setIsBound(boolean isBound);
		
		boolean isBound();
		
		TextButton getLoadButton();
		
		TextButton getDeleteButton();
		
		TextButton getCloseButton();
		
		ListView<KingdomMap, String> getListView();
	}
	
	private AddLayerDisplay		addLayerDisplay		= null;
	private NewMapDisplay		newMapDisplay		= null;
	private ExistingMapsDisplay	existingMapsDisplay	= null;
	
	public LayerPresenter(Display display){
		super(display);
		
		addLayerDisplay = new AddLayerDialog();
		newMapDisplay = new NewMapDialog();
		existingMapsDisplay = new ExistingMapsWindow();
	}
	
	@Override
	public void go(HasWidgets container){
		container.clear();
		container.add(display.asWidget());
	}
	
	@Override
	protected void bind(){
		
		// handle new map clicks
		display.getNewMapButton().addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event){
				newMapDisplay.show();
				
				// bind events
				if(!newMapDisplay.isBound()){
					
					newMapDisplay.getSaveButton().addSelectHandler(new SelectHandler() {
						
						@Override
						public void onSelect(SelectEvent event){
							if(newMapDisplay.isValid()){
								// create new map
								final KingdomMap newMap = new KingdomMap();
								newMap.setName(newMapDisplay.getNameField().getText());
								newMap.setUser(display.getCurrentUser());
								
								// check if map name already exists for this
								// user
								DataServiceAsync.Util.getInstance().mapExists(newMap, new AsyncCallback<Boolean>() {
									
									@Override
									public void onSuccess(Boolean result){
										if(!result){
											// save map to DB
											DataServiceAsync.Util.getInstance().addMap(newMap, new AsyncCallback<Integer>() {
												
												@Override
												public void onFailure(Throwable caught){
												}
												
												@Override
												public void onSuccess(Integer result){
													setMap(newMap);
													
													// hide dialog
													newMapDisplay.hide();
												}
											});
										}else{
											// map name already exists
											System.out.println("map name already exists");
											newMapDisplay.getNameField().forceInvalid("Map name already exists");
										}
									}
									
									@Override
									public void onFailure(Throwable caught){
									}
								});
							}
						}
					});
					
					newMapDisplay.getCancelButton().addSelectHandler(new SelectHandler() {
						
						@Override
						public void onSelect(SelectEvent event){
							newMapDisplay.hide();
						}
					});
					
					newMapDisplay.setIsBound(true);
				}
			}
		});
		
		// handle map list clicks
		display.getExistingMapsButton().addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event){
				// fetch existing map data
				DataServiceAsync.Util.getInstance().getMapList(display.getCurrentUser(), new AsyncCallback<List<KingdomMap>>() {

					@Override
					public void onFailure(Throwable caught){
					}

					@Override
					public void onSuccess(List<KingdomMap> result){
						existingMapsDisplay.setMapData(result);
						
						existingMapsDisplay.show();
						
						// bind events
						if(!existingMapsDisplay.isBound()){
							existingMapsDisplay.getLoadButton().addSelectHandler(new SelectHandler() {
								
								@Override
								public void onSelect(SelectEvent event){
									KingdomMap selectedMap = existingMapsDisplay.getListView().getSelectionModel().getSelectedItem();
									if(selectedMap != null){
										setMap(selectedMap);
										existingMapsDisplay.hide();
									}
								}
							});
							
							existingMapsDisplay.getListView().addDomHandler(new DoubleClickHandler() {

								@Override
								public void onDoubleClick(DoubleClickEvent event){
									KingdomMap selectedMap = existingMapsDisplay.getListView().getSelectionModel().getSelectedItem();
									
									if(selectedMap != null){
										setMap(selectedMap);
										existingMapsDisplay.hide();
									}
								}
								
							}, DoubleClickEvent.getType());
							
							existingMapsDisplay.getDeleteButton().addSelectHandler(new SelectHandler() {
								
								@Override
								public void onSelect(SelectEvent event){
									
									final KingdomMap selectedMap = existingMapsDisplay.getListView().getSelectionModel().getSelectedItem();
									
									if(selectedMap != null){
										final Dialog dialog = new Dialog();
										dialog.setHeadingText("Delete map");
										dialog.setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO);
										dialog.add(new Label("Are you sure you want to delete this map? All of its contents will be lost forever!"));
										dialog.setModal(true);
										dialog.setHideOnButtonClick(true);
										
										dialog.addDialogHideHandler(new DialogHideHandler() {
											
											@Override
											public void onDialogHide(DialogHideEvent event){
												if(event.getHideButton().compareTo(PredefinedButton.YES) == 0){
													// delete map
													DataServiceAsync.Util.getInstance().deleteMap(selectedMap, new AsyncCallback<Boolean>() {
	
														@Override
														public void onFailure(Throwable caught){
															
														}
	
														@Override
														public void onSuccess(Boolean result){
															// TODO: deletion notification
															
															// delete from list
															existingMapsDisplay.getListView().getStore().remove(selectedMap);
															existingMapsDisplay.getListView().refresh();
															
															//TODO: 
															// if deleted map is current map
															// if deleted map is only map
														}
													});
												}
											}
										});
										
										dialog.show();
									}
								}
							});
							
							existingMapsDisplay.getCloseButton().addSelectHandler(new SelectHandler() {
								
								@Override
								public void onSelect(SelectEvent event){
									existingMapsDisplay.hide();
								}
							});
							
							existingMapsDisplay.setIsBound(true);
						}						
					}
				});
			}
		});
		
		// handle new layer clicks
		display.getAddLayerButton().addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event){
				addLayerDisplay.show();
				
				// bind dialog events
				if(!addLayerDisplay.isBound()){
					
					addLayerDisplay.getAddButton().addSelectHandler(new SelectHandler() {
						@Override
						public void onSelect(SelectEvent event){
							if(addLayerDisplay.isValid()){
								// create new layer
								KingdomLayer newLayer = new KingdomLayer();
								newLayer.setLayerName(addLayerDisplay.getNameField().getText());
								newLayer.setVisible(true);
								newLayer.setGeometryType(((ToggleButton) addLayerDisplay.getToggleGroup().getValue()).getItemId());
								
								// add layer to DB
								
								addLayerDisplay.hide();
							}
						}
					});
					
					addLayerDisplay.getCancelButton().addSelectHandler(new SelectHandler() {
						@Override
						public void onSelect(SelectEvent event){
							addLayerDisplay.hide();
						}
					});
					
					addLayerDisplay.setIsBound(true);
				}
			}
		});
	}
	
	private void setMap(KingdomMap newMap){
		// enable controls
		display.enableLayerView();
		
		// set as current map
		display.setCurrentMap(newMap);
		
		// add map to map view
		Bus.get().fireEvent(new EventAddNewMap(newMap));
		
		// display map name
		Bus.get().fireEvent(new EventChangeMapNameHeader(newMap.getName()));
	}
}
