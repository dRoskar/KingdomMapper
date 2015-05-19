package si.roskar.diploma.client.presenter;

import java.util.List;

import si.roskar.diploma.client.DataServiceAsync;
import si.roskar.diploma.client.event.Bus;
import si.roskar.diploma.client.event.EventAddNewMap;
import si.roskar.diploma.client.event.EventChangeMapNameHeader;
import si.roskar.diploma.client.event.EventEnableMapView;
import si.roskar.diploma.client.event.EventShowDrawingToolbar;
import si.roskar.diploma.client.view.AddLayerDialog;
import si.roskar.diploma.client.view.ExistingMapsWindow;
import si.roskar.diploma.client.view.NewMapDialog;
import si.roskar.diploma.client.view.View;
import si.roskar.diploma.shared.KingdomLayer;
import si.roskar.diploma.shared.KingdomMap;
import si.roskar.diploma.shared.KingdomUser;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Slider;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.tree.Tree;

public class LayerPresenter extends PresenterImpl<LayerPresenter.Display>{
	
	public interface Display extends View{
		
		TextButton getAddLayerButton();
		
		TextButton getNewMapButton();
		
		TextButton getExistingMapsButton();
		
		void setCurrentUser(KingdomUser currentUser);
		
		KingdomUser getCurrentUser();
		
		void setCurrentMap(KingdomMap currentMap);
		
		KingdomMap getCurrentMap();
		
		void addLayer(KingdomLayer layer);
		
		void setLayers(List<KingdomLayer> layers);
		
		void enableLayerView();
		
		Slider getOpacitySlider();
		
		void setLayerOpacitySliderValue(final float value);
		
		Tree<KingdomLayer, String> getLayerTree();
		
		HasSelectionHandlers<Item> getDeleteLayerItem();
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
													newMap.setId(result);
													setMap(newMap);
													
													// hide dialog
													newMapDisplay.hide();
												}
											});
										}else{
											// map name already exists
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
															// TODO: deletion
															// notification
															
															// delete from list
															existingMapsDisplay.getListView().getStore().remove(selectedMap);
															existingMapsDisplay.getListView().refresh();
															
															// TODO:
															// if deleted map is
															// current map
															// if deleted map is
															// only map
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
								final KingdomLayer newLayer = new KingdomLayer();
								newLayer.setName(addLayerDisplay.getNameField().getText());
								newLayer.setVisible(true);
								newLayer.setGeometryType(((ToggleButton) addLayerDisplay.getToggleGroup().getValue()).getItemId());
								newLayer.setMap(display.getCurrentMap());
								
								// check if layer already exists for this map
								DataServiceAsync.Util.getInstance().layerExists(newLayer, new AsyncCallback<Boolean>() {

									@Override
									public void onFailure(Throwable caught){
									}

									@Override
									public void onSuccess(Boolean result){
										if(!result){
											// add layer to DB
											DataServiceAsync.Util.getInstance().addLayer(newLayer, new AsyncCallback<Integer>() {
												
												@Override
												public void onFailure(Throwable caught){
												}
												
												@Override
												public void onSuccess(Integer result){
													newLayer.setId(result);
													
													// add layer to layer tree
													display.addLayer(newLayer);
													
													display.getCurrentMap().setEmpty(false);
													
													// enable map view
													Bus.get().fireEvent(new EventEnableMapView(true));
												}
											});
											
											addLayerDisplay.hide();
										}
										else{
											// layer name already exists
											addLayerDisplay.getNameField().forceInvalid("Layer name already exists");
										}
									}
								});
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
		
		display.getOpacitySlider().addValueChangeHandler(new ValueChangeHandler<Integer>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event){
				
				// redraw slider
				display.getOpacitySlider().redraw();
				
				if(display.getLayerTree().getSelectionModel().getSelectedItem() != null){
					KingdomLayer layer = display.getLayerTree().getSelectionModel().getSelectedItem();
					
					if(layer == null){
						return;
					}
					
					// opacity stuff
				}
			}
		});
		
		display.getDeleteLayerItem().addSelectionHandler(new SelectionHandler<Item>() {
			
			@Override
			public void onSelection(SelectionEvent<Item> event){
				final KingdomLayer layer = display.getLayerTree().getSelectionModel().getSelectedItem();
				
				if(layer != null){
					// are you sure?
					final Dialog dialog = new Dialog();
					dialog.setHeadingText("Delete layer");
					dialog.setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO);
					dialog.add(new Label("Are you sure you want to delete this layer? All of its contents will be lost forever!"));
					dialog.setModal(true);
					dialog.setHideOnButtonClick(true);
					
					dialog.addDialogHideHandler(new DialogHideHandler() {
						
						@Override
						public void onDialogHide(DialogHideEvent event){
							if(event.getHideButton().compareTo(PredefinedButton.YES) == 0){
								// remove layer from db
								DataServiceAsync.Util.getInstance().deleteLayer(layer, new AsyncCallback<Boolean>() {

									@Override
									public void onFailure(Throwable caught){	
									}

									@Override
									public void onSuccess(Boolean result){
										// TODO: deletion notification
										
										// remove layer from tree
										display.getLayerTree().getStore().remove(layer);
										
										// disable map view if tree is now empty
										if(display.getLayerTree().getStore().getAllItemsCount() < 1){
											Bus.get().fireEvent(new EventEnableMapView(false));
										}
									}
								});
							}
						}
					});
					
					dialog.show();
				}
			}
		});
		
		// handle layer tree item selection
		display.getLayerTree().getSelectionModel().addBeforeSelectionHandler(new BeforeSelectionHandler<KingdomLayer>() {
			
			@Override
			public void onBeforeSelection(BeforeSelectionEvent<KingdomLayer> event){
				KingdomLayer selectedLayer = event.getItem();
				
				if(selectedLayer != null){
					Bus.get().fireEvent(new EventShowDrawingToolbar(true));
				}
			}
		});
	};
	
	private void setMap(KingdomMap newMap){
		// clear layers
		display.getLayerTree().getStore().clear();
		
		// enable controls
		display.enableLayerView();
		
		// set as current map
		display.setCurrentMap(newMap);
		
		// add map to map view
		Bus.get().fireEvent(new EventAddNewMap(newMap));
		
		// display map name
		Bus.get().fireEvent(new EventChangeMapNameHeader(newMap.getName()));
		
		// load map layers
		if(!newMap.isEmpty()){
			DataServiceAsync.Util.getInstance().getLayerList(newMap, new AsyncCallback<List<KingdomLayer>>() {

				@Override
				public void onFailure(Throwable caught){
				}

				@Override
				public void onSuccess(List<KingdomLayer> result){
					if(!result.isEmpty()){
						display.setLayers(result);
						Bus.get().fireEvent(new EventEnableMapView(true));
					}
					else{
						Bus.get().fireEvent(new EventEnableMapView(false));
					}
				}
			});
		}
	}
}
