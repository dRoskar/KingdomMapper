package si.roskar.diploma.client.presenter;

import java.util.ArrayList;
import java.util.List;

import si.roskar.diploma.client.DataServiceAsync;
import si.roskar.diploma.client.event.Bus;
import si.roskar.diploma.client.event.EventAddNewLayer;
import si.roskar.diploma.client.event.EventAddNewMap;
import si.roskar.diploma.client.event.EventChangeEditButtonGroup;
import si.roskar.diploma.client.event.EventChangeMapNameHeader;
import si.roskar.diploma.client.event.EventDisableEditMode;
import si.roskar.diploma.client.event.EventEnableDrawingToolbar;
import si.roskar.diploma.client.event.EventEnableMapView;
import si.roskar.diploma.client.event.EventGetSelectedLayer;
import si.roskar.diploma.client.event.EventGetSelectedLayer.EventGetSelectedLayerHandler;
import si.roskar.diploma.client.event.EventMapScaleChanged;
import si.roskar.diploma.client.event.EventMapScaleChanged.EventMapScaleChangedHandler;
import si.roskar.diploma.client.event.EventRemoveCurrentMap;
import si.roskar.diploma.client.event.EventRemoveLayerFromMapView;
import si.roskar.diploma.client.event.EventSetCurrentLayer;
import si.roskar.diploma.client.event.EventSetLayerOpacity;
import si.roskar.diploma.client.event.EventSetLayerVisibility;
import si.roskar.diploma.client.event.EventSortLayerTree;
import si.roskar.diploma.client.event.EventSortLayerTree.EventSortLayerTreeHandler;
import si.roskar.diploma.client.event.EventUILoaded;
import si.roskar.diploma.client.event.EventUILoaded.EventUILoadedHandler;
import si.roskar.diploma.client.util.KingdomInfo;
import si.roskar.diploma.client.view.AddLayerDialog;
import si.roskar.diploma.client.view.ExistingMapsWindow;
import si.roskar.diploma.client.view.NewMapDialog;
import si.roskar.diploma.client.view.View;
import si.roskar.diploma.shared.GeometryType;
import si.roskar.diploma.shared.KingdomGridLayer;
import si.roskar.diploma.shared.KingdomLayer;
import si.roskar.diploma.shared.KingdomMap;
import si.roskar.diploma.shared.KingdomUser;
import si.roskar.diploma.shared.MapSize;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
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
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.Slider;
import com.sencha.gxt.widget.core.client.box.PromptMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.event.BeforeShowContextMenuEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowContextMenuEvent.BeforeShowContextMenuHandler;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent.CheckChangeHandler;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;

public class LayerPresenter extends PresenterImpl<LayerPresenter.Display>{
	
	public interface Display extends View{
		
		TextButton getAddLayerButton();
		
		TextButton getDeleteLayerButton();
		
		TextButton getNewMapButton();
		
		TextButton getExistingMapsButton();
		
		void setCurrentUser(KingdomUser currentUser);
		
		KingdomUser getCurrentUser();
		
		void setCurrentMap(KingdomMap currentMap);
		
		KingdomMap getCurrentMap();
		
		void addLayer(KingdomLayer layer);
		
		void setLayers(List<KingdomLayer> layers);
		
		void enableLayerView(boolean enable);
		
		Slider getOpacitySlider();
		
		void setLayerOpacitySliderValue(final float value);
		
		Tree<KingdomLayer, KingdomLayer> getLayerTree();
		
		HasSelectionHandlers<Item> getRenameLayerItem();
		
		HasSelectionHandlers<Item> getDeleteLayerItem();
		
		KingdomLayer getSelectedLayer();
		
		Menu getContextMenu();
		
		void updateLayersInScaleStyle(double scale);
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
		
		TextButton getRenameButton();
		
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
		
		// handle UI loaded event
		Bus.get().addHandler(EventUILoaded.TYPE, new EventUILoadedHandler() {
			
			@Override
			public void onUILoaded(EventUILoaded event){
				// check if user was already working on a map on his last visit
				if(display.getCurrentUser().getLastMapId() > 0){
					
					KingdomInfo.showLoadingBar("Loading", "Loading your last work", "loading...");
					
					// get the map
					DataServiceAsync.Util.getInstance().getMap(display.getCurrentUser().getLastMapId(), new AsyncCallback<KingdomMap>() {
						
						@Override
						public void onFailure(Throwable caught){
							KingdomInfo.hideLoadingBar();
							KingdomInfo.showInfoPopUp("Error", "Error loading your last work");
						}
						
						@Override
						public void onSuccess(KingdomMap result){
							if(result != null){
								setMap(result);
								display.getDeleteLayerButton().disable();
								KingdomInfo.hideLoadingBar();
							}
							else{
								KingdomInfo.hideLoadingBar();
								KingdomInfo.showInfoPopUp("Error", "Error2 loading your last work");
							}
						}
					});
				}
			}
		});
		
		// handle new map clicks
		display.getNewMapButton().addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event){
				newMapDisplay.show();
				
				Bus.get().fireEvent(new EventDisableEditMode());
				
				// bind events
				if(!newMapDisplay.isBound()){
					
					newMapDisplay.getSaveButton().addSelectHandler(new SelectHandler() {
						
						@Override
						public void onSelect(SelectEvent event){
							if(newMapDisplay.isValid()){
								KingdomInfo.showLoadingBar("Creating", "Creating new map", "creating...");
								
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
													KingdomInfo.hideLoadingBar();
													KingdomInfo.showInfoPopUp("Error", "Error adding map to DB");
												}
												
												@Override
												public void onSuccess(Integer result){
													newMap.setId(result);
													setMap(newMap);
													display.getDeleteLayerButton().disable();
													
													// hide dialog
													newMapDisplay.hide();
												}
											});
										}else{
											// map name already exists
											newMapDisplay.getNameField().forceInvalid("Map name already exists");
											
											KingdomInfo.hideLoadingBar();
										}
									}
									
									@Override
									public void onFailure(Throwable caught){
										KingdomInfo.hideLoadingBar();
										KingdomInfo.showInfoPopUp("Error", "Error checking if map name exists");
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
				KingdomInfo.showLoadingBar("Loading", "Loading map list", "loading...");
				
				// disable edit mode
				Bus.get().fireEvent(new EventDisableEditMode());
				
				// fetch existing map data
				DataServiceAsync.Util.getInstance().getMapList(display.getCurrentUser(), new AsyncCallback<List<KingdomMap>>() {
					
					@Override
					public void onFailure(Throwable caught){
						KingdomInfo.hideLoadingBar();
						KingdomInfo.showInfoPopUp("Error", "Error loading map list");
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
										KingdomInfo.showLoadingBar("Loading", "Loading map data", "loading...");
										
										setMap(selectedMap);
										display.getDeleteLayerButton().disable();
										existingMapsDisplay.hide();
									}
								}
							});
							
							existingMapsDisplay.getListView().addDomHandler(new DoubleClickHandler() {
								
								@Override
								public void onDoubleClick(DoubleClickEvent event){
									KingdomMap selectedMap = existingMapsDisplay.getListView().getSelectionModel().getSelectedItem();
									
									if(selectedMap != null){
										KingdomInfo.showLoadingBar("Loading", "Loading map data", "loading...");
										setMap(selectedMap);
										display.getDeleteLayerButton().disable();
										existingMapsDisplay.hide();
									}
								}
								
							}, DoubleClickEvent.getType());
							
							// rename map
							existingMapsDisplay.getRenameButton().addSelectHandler(new SelectHandler() {
								
								@Override
								public void onSelect(SelectEvent event){
									KingdomMap renamedMap = existingMapsDisplay.getListView().getSelectionModel().getSelectedItem();
									
									if(renamedMap != null){
										final PromptMessageBox messageBox = new PromptMessageBox("Rename map", "Enter a new name for this map");
										messageBox.setModal(true);
										messageBox.addDialogHideHandler(new DialogHideHandler() {
											
											@Override
											public void onDialogHide(DialogHideEvent event){
												if(event.getHideButton().equals(PredefinedButton.OK)){
													// validate
													if(messageBox.getValue() != ""){
														// rename the layer
														KingdomInfo.showLoadingBar("Renaming", "Renaming map", "renaming...");
														
														final KingdomMap map = existingMapsDisplay.getListView().getSelectionModel().getSelectedItem();
														map.setName(messageBox.getValue());
														
														DataServiceAsync.Util.getInstance().mapExists(map, new AsyncCallback<Boolean>() {
															
															@Override
															public void onFailure(Throwable caught){
																KingdomInfo.hideLoadingBar();
																KingdomInfo.showInfoPopUp("Error", "Error checking if map name exists");
															}
															
															@Override
															public void onSuccess(Boolean result){
																if(!result){
																	DataServiceAsync.Util.getInstance().updateMapName(map, new AsyncCallback<KingdomMap>() {
																		
																		@Override
																		public void onFailure(Throwable caught){
																			KingdomInfo.hideLoadingBar();
																			KingdomInfo.showInfoPopUp("Error", "Error updating map name");
																		}
																		
																		@Override
																		public void onSuccess(KingdomMap result){
																			// refresh
																			// list
																			existingMapsDisplay.getListView().refresh();
																			
																			if(display.getCurrentMap().getId() == result.getId()){
																				// refresh
																				// map
																				// name
																				// header
																				Bus.get().fireEvent(new EventChangeMapNameHeader(result.getName()));
																			}
																			KingdomInfo.hideLoadingBar();
																			KingdomInfo.showInfoPopUp("Success",  "Map renamed successfully");
																		}
																	});
																}else{
																	KingdomInfo.hideLoadingBar();
																	KingdomInfo.showInfoPopUp("Info",  "Map name already taken");
																}
															}
														});
													}else{
														KingdomInfo.showInfoPopUp("Info",  "Invalid name");
													}
												}
											}
										});
										
										messageBox.show();
									}
								}
							});
							
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
												if(event.getHideButton().equals(PredefinedButton.YES)){
													KingdomInfo.showLoadingBar("Deleting", "Deleting map data", "deleting...");
													
													// load map layers
													DataServiceAsync.Util.getInstance().getLayerList(selectedMap, new AsyncCallback<List<KingdomLayer>>() {
														
														@Override
														public void onFailure(Throwable caught){
															KingdomInfo.hideLoadingBar();
															KingdomInfo.showInfoPopUp("Error", "Error getting layer list");
														}
														
														@Override
														public void onSuccess(List<KingdomLayer> result){
															// add layers to map
															// object
															selectedMap.setLayers(result);
															
															// delete map
															DataServiceAsync.Util.getInstance().deleteMap(selectedMap, new AsyncCallback<Boolean>() {
																
																@Override
																public void onFailure(Throwable caught){
																	KingdomInfo.hideLoadingBar();
																	KingdomInfo.showInfoPopUp("Error", "Error deleting map");
																}
																
																@Override
																public void onSuccess(Boolean result){
																	// delete
																	// from list
																	existingMapsDisplay.getListView().getStore().remove(selectedMap);
																	existingMapsDisplay.getListView().refresh();
																	
																	if(display.getCurrentMap().getId() == selectedMap.getId()){
																		// clear
																		// layer
																		// list
																		display.getAddLayerButton().disable();
																		display.getDeleteLayerButton().disable();
																		display.getLayerTree().getStore().clear();
																		display.enableLayerView(false);
																		
																		Bus.get().fireEvent(new EventRemoveCurrentMap());
																	}
																	
																	KingdomInfo.hideLoadingBar();
																	KingdomInfo.showInfoPopUp("Success", "Map deleted successfully");
																}
															});
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
						
						KingdomInfo.hideLoadingBar();
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
								KingdomInfo.showLoadingBar("Adding", "Adding new layer", "adding...");
								
								// create new layer
								final KingdomLayer newLayer = new KingdomLayer();
								newLayer.setName(addLayerDisplay.getNameField().getText());
								newLayer.setVisible(true);
								newLayer.setGeometryType(GeometryType.getTypeFromGeometryName(((ToggleButton) addLayerDisplay.getToggleGroup().getValue()).getItemId()));
								newLayer.setMap(display.getCurrentMap());
								newLayer.setStyle("");
								newLayer.setOpacity(1);
								newLayer.setMaxScale(display.getCurrentMap().getScales()[display.getCurrentMap().getScales().length - 1]);
								newLayer.setMinScale(display.getCurrentMap().getScales()[0]);
								
								// get all z indexes (except for the grid
								// layer) and give this guy the highest one
								int highestZIndex = 0;
								for(KingdomLayer layer : display.getCurrentMap().getLayers()){
									if(!(layer instanceof KingdomGridLayer)){
										if(layer.getZIndex() > highestZIndex){
											highestZIndex = layer.getZIndex();
										}
									}
								}
								
								newLayer.setZIndex(highestZIndex + 1);
								
								// set default style
								if(newLayer.getGeometryType().equals(GeometryType.POINT) || newLayer.getGeometryType().equals(GeometryType.MARKER)){
									newLayer.setColor("0066FF");
									newLayer.setShape("square");
									newLayer.setSize(6);
								}else if(newLayer.getGeometryType().equals(GeometryType.LINE)){
									newLayer.setColor("FF3300");
									newLayer.setStrokeWidth(1);
								}else if(newLayer.getGeometryType().equals(GeometryType.POLYGON)){
									newLayer.setFillColor("33CC33");
									newLayer.setFillOpacity(1);
									newLayer.setStrokeWidth(1);
									newLayer.setColor("009933");
									newLayer.setStrokeOpacity(1);
								}
								
								// check if layer already exists for this map
								DataServiceAsync.Util.getInstance().layerExists(newLayer, new AsyncCallback<Boolean>() {
									
									@Override
									public void onFailure(Throwable caught){
										KingdomInfo.hideLoadingBar();
										KingdomInfo.showInfoPopUp("Error", "Error checking if layer name exists");
									}
									
									@Override
									public void onSuccess(Boolean result){
										if(!result){
											// add layer to DB
											DataServiceAsync.Util.getInstance().addLayer(newLayer, new AsyncCallback<Integer>() {
												
												@Override
												public void onFailure(Throwable caught){
													KingdomInfo.hideLoadingBar();
													KingdomInfo.showInfoPopUp("Error", "Error adding layer to DB");
												}
												
												@Override
												public void onSuccess(Integer result){
													newLayer.setId(result);
													
													// add layer to layer tree
													display.addLayer(newLayer);
													
													// add layer to OL map
													Bus.get().fireEvent(new EventAddNewLayer(newLayer));
													
													KingdomInfo.hideLoadingBar();
												}
											});
											
											addLayerDisplay.hide();
										}else{
											KingdomInfo.hideLoadingBar();
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
		
		// opacity slider
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
					Bus.get().fireEvent(new EventSetLayerOpacity(layer, event.getValue()));
				}
			}
		});
		
		// before show context menu
		display.getLayerTree().addBeforeShowContextMenuHandler(new BeforeShowContextMenuHandler() {
			@Override
			public void onBeforeShowContextMenu(BeforeShowContextMenuEvent event){
				KingdomLayer layer = display.getLayerTree().getSelectionModel().getSelectedItem();
				if(layer != null){
					display.setLayerOpacitySliderValue(layer.getOpacity());
				}
			}
		});
		
		// handle delete layer button click
		display.getDeleteLayerButton().addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event){
				deleteLayer();
			}
		});
		
		// handle rename layer from menu click
		display.getRenameLayerItem().addSelectionHandler(new SelectionHandler<Item>() {
			
			@Override
			public void onSelection(SelectionEvent<Item> event){
				if(display.getLayerTree().getSelectionModel().getSelectedItem() != null){
					final PromptMessageBox messageBox = new PromptMessageBox("Rename layer", "Enter a new name for this layer");
					messageBox.setModal(true);
					messageBox.addDialogHideHandler(new DialogHideHandler() {
						
						@Override
						public void onDialogHide(DialogHideEvent event){
							if(event.getHideButton().equals(PredefinedButton.OK)){
								// validate
								if(messageBox.getValue() != ""){
									KingdomInfo.showLoadingBar("Renaming", "Renaming layer", "renaming...");
									
									// rename the layer
									final KingdomLayer renamedLayer = display.getSelectedLayer();
									renamedLayer.setName(messageBox.getValue());
									
									DataServiceAsync.Util.getInstance().layerExists(renamedLayer, new AsyncCallback<Boolean>() {

										@Override
										public void onFailure(Throwable caught){
											KingdomInfo.hideLoadingBar();
											KingdomInfo.showInfoPopUp("Error", "Error checking if layer name exists");
										}

										@Override
										public void onSuccess(Boolean result){
											if(!result){
												DataServiceAsync.Util.getInstance().updateLayerName(renamedLayer, new AsyncCallback<Boolean>() {
													
													@Override
													public void onFailure(Throwable caught){
														KingdomInfo.hideLoadingBar();
														KingdomInfo.showInfoPopUp("Error", "Error updating layer name");
													}
													
													@Override
													public void onSuccess(Boolean result){
														// refresh layer tree
														display.getLayerTree().refresh(display.getLayerTree().getSelectionModel().getSelectedItem());
														
														KingdomInfo.hideLoadingBar();
														KingdomInfo.showInfoPopUp("Success", "Layer renamed successfully");
													}
												});
											}
											else{
												KingdomInfo.hideLoadingBar();
												
												KingdomInfo.showInfoPopUp("Info", "Layer name already taken");
											}
										}
									});
								}else{
									KingdomInfo.showInfoPopUp("Info", "Invalid layer name");
								}
							}
						}
					});
					
					messageBox.show();
				}
			}
		});
		
		// handle delete layer from menu click
		display.getDeleteLayerItem().addSelectionHandler(new SelectionHandler<Item>() {
			
			@Override
			public void onSelection(SelectionEvent<Item> event){
				deleteLayer();
			}
		});
		
		// handle layer tree item selection
		display.getLayerTree().getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<KingdomLayer>() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent<KingdomLayer> event){
				if(!event.getSelection().isEmpty()){
					KingdomLayer selectedLayer = event.getSelection().get(0);
					
					// change edit button group
					Bus.get().fireEvent(new EventChangeEditButtonGroup(selectedLayer.getGeometryType()));
					
					// enable drawing toolbar
					Bus.get().fireEvent(new EventEnableDrawingToolbar(true));
					
					// enable delete layer button
					display.getDeleteLayerButton().enable();
					
					// set current layer
					Bus.get().fireEvent(new EventSetCurrentLayer(selectedLayer));
				}else{
					// disable drawing toolbar
					Bus.get().fireEvent(new EventEnableDrawingToolbar(false));
					
					// disable edit mode
					Bus.get().fireEvent(new EventDisableEditMode());
				}
			}
		});
		
		// handle get selected layer event
		Bus.get().addHandler(EventGetSelectedLayer.TYPE, new EventGetSelectedLayerHandler() {
			
			@Override
			public void onGetSelectedLayer(EventGetSelectedLayer event){
				event.setLayer(display.getSelectedLayer());
			}
		});
		
		// handle layer tree checks
		display.getLayerTree().addCheckChangeHandler(new CheckChangeHandler<KingdomLayer>() {
			
			@Override
			public void onCheckChange(CheckChangeEvent<KingdomLayer> event){
				event.getItem().setVisible(event.getChecked().equals(CheckState.CHECKED));
				Bus.get().fireEvent(new EventSetLayerVisibility(event.getItem(), event.getChecked().equals(CheckState.CHECKED)));
			}
		});
		
		// handle sort layer tree handler
		Bus.get().addHandler(EventSortLayerTree.TYPE, new EventSortLayerTreeHandler() {
			
			@Override
			public void onSortLayerTree(EventSortLayerTree event){
				// save selected layer
				KingdomLayer selectedLayer = display.getSelectedLayer();
				
				display.getLayerTree().disableEvents();
				
				// sort tree (bug in library causes items to get unchecked and
				// (visibly) unselected)
				display.getLayerTree().getStore().applySort(false);
				
				// reselect unselected items
				display.getLayerTree().getSelectionModel().deselect(selectedLayer);
				display.getLayerTree().getSelectionModel().select(selectedLayer, true);
				
				// recheck unchecked items
				for(KingdomLayer layer : display.getCurrentMap().getLayers()){
					if(layer.isVisible()){
						
						display.getLayerTree().setChecked(layer, CheckState.CHECKED);
					}
				}
				display.getLayerTree().enableEvents();
			}
		});
	};
	
	private void setMap(final KingdomMap newMap){
		// temp
		newMap.setMapSize(MapSize.COUNTRY_MAP);
		
		// clear layers
		display.getLayerTree().getStore().clear();
		
		// enable controls
		display.enableLayerView(true);
		
		// set as current map
		display.setCurrentMap(newMap);
		
		// display map name
		Bus.get().fireEvent(new EventChangeMapNameHeader(newMap.getName()));
		
		// load map layers
		DataServiceAsync.Util.getInstance().getLayerList(newMap, new AsyncCallback<List<KingdomLayer>>() {
			
			@Override
			public void onFailure(Throwable caught){
				KingdomInfo.hideLoadingBar();
				KingdomInfo.showInfoPopUp("Error", "Error getting map's layers");
			}
			
			@Override
			public void onSuccess(List<KingdomLayer> result){
				// add layers to layer tree view
				display.setLayers(result);
				
				// prepend grid layer
				KingdomGridLayer gridLayer = new KingdomGridLayer(-2, "Grid", "grid", true, "image/png", "EPSG:4326", 0.5f, MapSize.COUNTRY_MAP);
				gridLayer.setZIndex(325);
				
				List<KingdomLayer> layers = new ArrayList<KingdomLayer>();
				layers.add(gridLayer);
				
				for(KingdomLayer layer : result){
					layers.add(layer);
				}
				
				// add layers to map object
				for(KingdomLayer layer : layers){
					newMap.addLayer(layer);
				}
				
				// add layers to map view
				Bus.get().fireEvent(new EventAddNewMap(newMap));
				
				// enable map view
				Bus.get().fireEvent(new EventEnableMapView(true));
				
				KingdomInfo.hideLoadingBar();
			}
		});
		
		// handle map scale schanged
		Bus.get().addHandler(EventMapScaleChanged.TYPE, new EventMapScaleChangedHandler(){

			@Override
			public void onMapScaleChanged(EventMapScaleChanged event){
				display.updateLayersInScaleStyle(event.getScale());
			}
		});
	}
	
	private void deleteLayer(){
		KingdomLayer layer = display.getLayerTree().getSelectionModel().getSelectedItem();
		
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
					if(event.getHideButton().equals(PredefinedButton.YES)){
						KingdomInfo.showLoadingBar("Deleting", "Deleteing layer", "deleting...");
						
						// remove layer from db
						DataServiceAsync.Util.getInstance().deleteLayer(display.getLayerTree().getSelectionModel().getSelectedItem(), new AsyncCallback<Boolean>() {
							
							@Override
							public void onFailure(Throwable caught){
								KingdomInfo.hideLoadingBar();
								KingdomInfo.showInfoPopUp("Info", "Error deleting layer");
							}
							
							@Override
							public void onSuccess(Boolean result){
								// remove layer from mapview
								Bus.get().fireEvent(new EventRemoveLayerFromMapView(display.getLayerTree().getSelectionModel().getSelectedItem()));
								
								// remove layer from current map
								display.getCurrentMap().getLayers().remove(display.getLayerTree().getSelectionModel().getSelectedItem());
								
								// remove layer from tree
								display.getLayerTree().getStore().remove(display.getLayerTree().getSelectionModel().getSelectedItem());
								
								// disable drawing bar
								Bus.get().fireEvent(new EventEnableDrawingToolbar(false));
								
								KingdomInfo.hideLoadingBar();
								KingdomInfo.showInfoPopUp("Success", "Layer deleted succesfully");
							}
						});
					}
				}
			});
			
			dialog.show();
		}
	}
}
