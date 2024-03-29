package si.roskar.diploma.client.presenter;

import java.util.ArrayList;
import java.util.List;

import org.gwtopenmaps.openlayers.client.Bounds;
import org.gwtopenmaps.openlayers.client.Map;
import org.gwtopenmaps.openlayers.client.control.DrawFeature;
import org.gwtopenmaps.openlayers.client.control.NavigationHistory;
import org.gwtopenmaps.openlayers.client.event.ControlActivateListener;
import org.gwtopenmaps.openlayers.client.event.EventHandler;
import org.gwtopenmaps.openlayers.client.event.EventObject;
import org.gwtopenmaps.openlayers.client.event.MapClickListener;
import org.gwtopenmaps.openlayers.client.event.MeasureEvent;
import org.gwtopenmaps.openlayers.client.event.MeasurePartialListener;
import org.gwtopenmaps.openlayers.client.event.VectorFeatureAddedListener;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.layer.WMS;

import si.roskar.diploma.client.DataServiceAsync;
import si.roskar.diploma.client.event.Bus;
import si.roskar.diploma.client.event.EventAddNewLayer;
import si.roskar.diploma.client.event.EventAddNewLayer.EventAddNewLayerHandler;
import si.roskar.diploma.client.event.EventAddNewMap;
import si.roskar.diploma.client.event.EventAddNewMap.EventAddNewMapHandler;
import si.roskar.diploma.client.event.EventChangeEditButtonGroup;
import si.roskar.diploma.client.event.EventChangeEditButtonGroup.EventChangeEditButtonGroupHandler;
import si.roskar.diploma.client.event.EventDisableEditMode;
import si.roskar.diploma.client.event.EventDisableEditMode.EventDisableEditModeHandler;
import si.roskar.diploma.client.event.EventEnableDrawingToolbar;
import si.roskar.diploma.client.event.EventEnableDrawingToolbar.EventEnableDrawingToolbarHandler;
import si.roskar.diploma.client.event.EventEnableMapView;
import si.roskar.diploma.client.event.EventGetSelectedLayer;
import si.roskar.diploma.client.event.EventMapScaleChanged;
import si.roskar.diploma.client.event.EventRemoveCurrentMap;
import si.roskar.diploma.client.event.EventRemoveCurrentMap.EventRemoveCurrentMapHandler;
import si.roskar.diploma.client.event.EventRemoveLayerFromMapView;
import si.roskar.diploma.client.event.EventRemoveLayerFromMapView.EventRemoveLayerFromMapViewHandler;
import si.roskar.diploma.client.event.EventSetCurrentLayer;
import si.roskar.diploma.client.event.EventSetCurrentLayer.EventSetCurrentLayerHandler;
import si.roskar.diploma.client.event.EventSetLayerOpacity;
import si.roskar.diploma.client.event.EventSetLayerOpacity.EventSetLayerOpacityHandler;
import si.roskar.diploma.client.event.EventSetLayerVisibility;
import si.roskar.diploma.client.event.EventSetLayerVisibility.EventSetLayerVisibilityHandler;
import si.roskar.diploma.client.event.EventSortLayerTree;
import si.roskar.diploma.client.util.ColorPickerWindow;
import si.roskar.diploma.client.util.KingdomInfo;
import si.roskar.diploma.client.util.KingdomMeasure;
import si.roskar.diploma.client.util.WFSLayerPackage;
import si.roskar.diploma.client.view.AddMarkerDialog;
import si.roskar.diploma.client.view.AddUserDialog;
import si.roskar.diploma.client.view.CreditsWindow;
import si.roskar.diploma.client.view.EditFeatureDialog;
import si.roskar.diploma.client.view.EditLayerStyleWindow;
import si.roskar.diploma.client.view.FeatureInfoSelectionMenu;
import si.roskar.diploma.client.view.MeasureDisplayWindow;
import si.roskar.diploma.client.view.View;
import si.roskar.diploma.shared.EditingMode;
import si.roskar.diploma.shared.GeometryType;
import si.roskar.diploma.shared.KingdomFeature;
import si.roskar.diploma.shared.KingdomLayer;
import si.roskar.diploma.shared.KingdomMap;
import si.roskar.diploma.shared.KingdomMarker;
import si.roskar.diploma.shared.KingdomTexture;
import si.roskar.diploma.shared.KingdomUser;
import si.roskar.diploma.shared.KingdomVectorFeature;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.ColorPalette;
import com.sencha.gxt.widget.core.client.Slider;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DoubleSpinnerField;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class MapPresenter extends PresenterImpl<MapPresenter.Display>{
	
	public interface Display extends View{
		
		KingdomMap getCurrentMap();
		
		void addNewMap(KingdomMap newMap);
		
		void addLayer(KingdomLayer layer);
		
		ToolBar getEditingToolbar();
		
		ToggleButton getDrawButton();
		
		ToggleButton getDrawRectangleButton();
		
		ToggleButton getDrawEllipseButton();
		
		ToggleButton getDrawSquareButton();
		
		ToggleButton getDrawCircleButton();
		
		ToggleButton getMoveFeaturesButton();
		
		ToggleButton getMoveVerticesButton();
		
		ToggleButton getDeleteFeaturesButton();
		
		ToggleButton getRotateButton();
		
		ToggleButton getScaleButton();
		
		ToggleButton getAddShapeButton();
		
		ToggleButton getAddHoleButton();
		
		ToggleButton getSnapButton();
		
		void setEditButtonGroup(GeometryType geometryType);
		
		Map getOLMap();
		
		KingdomLayer getCurrentLayer();
		
		KingdomLayer getEditingLayer();
		
		boolean isInEditMode();
		
		boolean isInAddingShapesMode();
		
		void setAddingShapesMode(boolean isInAddingShapesMode);
		
		boolean isInAddingHolesMode();
		
		void setAddingHolesMode(boolean isInAddingHolesMode);
		
		void setCurrentLayer(KingdomLayer currentLayer);
		
		ToggleButton getMeasureDistanceButton();
		
		ToggleButton getMeasureAreaButton();
		
		ToggleButton getGridButton();
		
		ToggleButton getInfoButton();
		
		void toggleGridVisible(boolean visible);
		
		void enableEditMode(EditingMode mode);
		
		void disableEditMode();
		
		java.util.Map<KingdomLayer, WFSLayerPackage> getWfsLayerPackageHashMap();
		
		Vector getDrawingLayer();
		
		Vector getCurrentOLWfsLayer();
		
		WMS getCurrentOLWmsLayer();
		
		DrawFeature getCurrentDrawControl();
		
		void setLayerVisibility(KingdomLayer layer, boolean visibility);
		
		void removeLayer(KingdomLayer layer);
		
		void removeMapOverlays();
		
		TextButton getZoomToFullExtentButton();
		
		TextButton getNavigateBackButton();
		
		TextButton getNavigateForwardButton();
		
		TextButton getBringToFrontButton();
		
		TextButton getSendToBackButton();
		
		TextButton getEditLayerStyleButton();
		
		TextButton getSetUpperLimitButton();
		
		TextButton getSetLowerLimitButton();
		
		TextButton getCreditsButton();
		
		TextButton getAddUsersButton();
		
		TextButton getLogOutButton();
		
		void bringLayerToFront(KingdomLayer selectedLayer);
		
		void sendLayerToBack(KingdomLayer selectedLayer);
		
		List<KingdomLayer> getLayerList();
		
		TextButton getSaveMapStateButton();
		
		KingdomMeasure getMeasureDistanceControl();
		
		KingdomMeasure getMeasureAreaControl();
		
		NavigationHistory getNavigationHistoryControl();
		
		void setSnapEnabled(boolean enabled);
		
		void setLayerOpacity(KingdomLayer layer, float opacity);
		
		void refreshLayerScaleLimit(KingdomLayer layer);
	}
	
	public interface AddMarkerDisplay extends View{
		
		void show(String wktGeometry);
		
		void hide();
		
		TextField getLabelField();
		
		TextButton getAddButton();
		
		TextButton getCancelButton();
		
		TextArea getDescriptionField();
		
		boolean isBound();
		
		void setIsBound(boolean isBound);
		
		boolean isValid();
		
		void setGeometry(String wktGeometry);
		
		String getGeometry();
	}
	
	public interface EditLayerStyleDisplay extends View{
		void show();
		
		void hide();
		
		boolean isBound();
		
		void setIsBound(boolean isBound);
		
		KingdomLayer getLayer();
		
		ColorPalette getColorPalette();
		
		ColorPalette getFillColorPalette();
		
		ColorPalette getLabelColorPalette();
		
		ColorPalette getLabelFillColorPalette();
		
		void setColorPickerWindow(ColorPickerWindow colorPickerWindow);
		
		ColorPickerWindow getColorPickerWindow();
		
		int getSize();
		
		TextButton getApplyButton();
		
		TextButton getCancelButton();
		
		String getColor();
		
		String getFillColor();
		
		String getLabelColor();
		
		String getLabelFillColor();
		
		DoubleSpinnerField getStrokeOpacitySpinner();
		
		DoubleSpinnerField getFillOpacitySpinner();
		
		ComboBox<KingdomMarker> getMarkerComboBox();
		
		ComboBox<KingdomTexture> getTextureComboBox();
		
		Slider getUpperLimitSlider();
		
		Slider getLowerLimitSlider();
		
		double getUpperSliderScale();
		
		double getLowerSliderScale();
		
		CheckBox getLabelCheckBox();
		
		CheckBox getTextureCheckBox();
	}
	
	public interface MeasureDisplay extends View{
		void show();
		
		void hide();
		
		void setPosition(int left, int top);
		
		void setMeasurementText(String text);
		
		boolean isVisible();
		
		int getWidth();
		
		int getHeight();
		
		void resetPosition(Element viewport, int padding);
	}
	
	public interface FeatureInfoSelectionDisplay extends View{
		void show(Element element, int x, int y);
		
		boolean isBound();
		
		void setIsBound(boolean isBound);
		
		void setFeatures(List<KingdomFeature> features);
		
		Menu getMenu();
		
		KingdomFeature getFeatureById(String id);
	}
	
	public interface EditFeatureDisplay extends View{
		void show();
		
		void hide();
		
		TextField getLabelField();
		
		TextArea getDescriptionArea();
		
		TextButton getSaveButton();
		
		TextButton getCloseButton();
		
		boolean isValid();
	}
	
	public interface AddUserDisplay extends View{
		void show();
		
		void hide();
		
		TextField getUsernameField();
		
		PasswordField getPasswordField();
		
		TextButton getAddButton();
		
		TextButton getCancelButton();
		
		boolean isBound();
		
		void setBound(boolean isBound);
		
		boolean isValid();
	}
	
	public interface CreditsDisplay extends View{
		void show();
	}
	
	private AddMarkerDisplay			addMarkerDisplay			= null;
	private EditLayerStyleDisplay		editLayerStyleDisplay		= null;
	private MeasureDisplay				measureDisplay				= null;
	private FeatureInfoSelectionDisplay	featureInfoSelectionDisplay	= null;
	private EditFeatureDisplay			editFeatureDisplay			= null;
	private CreditsDisplay				creditsDisplay				= null;
	private AddUserDisplay				addUserDisplay				= null;
	
	public MapPresenter(Display display){
		super(display);
		
		addMarkerDisplay = new AddMarkerDialog();
		measureDisplay = new MeasureDisplayWindow();
		featureInfoSelectionDisplay = new FeatureInfoSelectionMenu();
		creditsDisplay = new CreditsWindow();
		addUserDisplay = new AddUserDialog();
	}
	
	@Override
	public void go(HasWidgets container){
		container.clear();
		container.add(display.asWidget());
	}
	
	@Override
	protected void bind(){
		
		// handle add new map events
		Bus.get().addHandler(EventAddNewMap.TYPE, new EventAddNewMapHandler() {
			
			@Override
			public void onAddNewMap(EventAddNewMap event){
				// save previous map state
				if(display.getLayerList() != null){
					updateLayersDB(display.getLayerList(), false);
				}
				
				// disable snap mode if on
				if(display.getSnapButton().getValue()){
					display.getSnapButton().setValue(false, true);
				}
				
				display.addNewMap(event.getNewMap());
			}
		});
		
		// handle add new layer events
		Bus.get().addHandler(EventAddNewLayer.TYPE, new EventAddNewLayerHandler() {
			@Override
			public void onAddNewLayer(EventAddNewLayer event){
				// add layer to map
				display.addLayer(event.getLayer());
			}
		});
		
		// bind feature added events
		display.getDrawingLayer().addVectorFeatureAddedListener(new VectorFeatureAddedListener() {
			
			@Override
			public void onFeatureAdded(FeatureAddedEvent eventObject){
				// get geometry type
				GeometryType geometryType = display.getCurrentLayer().getGeometryType();
				
				String geometry = eventObject.getVectorFeature().getGeometry().toString();
				
				// MARKER
				if(geometryType.equals(GeometryType.MARKER)){
					// adding a marker
					if(!addMarkerDisplay.isBound()){
						addMarkerDisplay.getAddButton().addSelectHandler(new SelectHandler() {
							
							@Override
							public void onSelect(SelectEvent event){
								// evaluate fields
								if(addMarkerDisplay.isValid()){
									// insert marker
									DataServiceAsync.Util.getInstance().insertMarker(addMarkerDisplay.getGeometry(), addMarkerDisplay.getLabelField().getText(),
											addMarkerDisplay.getDescriptionField().getText(), display.getCurrentLayer().getId(), new AsyncCallback<Boolean>() {
												
												@Override
												public void onFailure(Throwable caught){
													KingdomInfo.showInfoPopUp("Error", "Error adding marker to DB");
												}
												
												@Override
												public void onSuccess(Boolean result){
													if(result){
														// redraw layers
														display.getWfsLayerPackageHashMap().get(display.getCurrentLayer()).getRefreshStrategy().refresh();
														display.getCurrentOLWmsLayer().redraw();
													}else{
														KingdomInfo.showInfoPopUp("Error", "Error adding marker");
													}
												}
											});
									
									addMarkerDisplay.hide();
								}
							}
						});
						
						addMarkerDisplay.getCancelButton().addSelectHandler(new SelectHandler() {
							
							@Override
							public void onSelect(SelectEvent event){
								addMarkerDisplay.hide();
							}
						});
						
						addMarkerDisplay.setIsBound(true);
					}
					
					addMarkerDisplay.show(geometry);
				}
				
				// POINT
				if(geometryType.equals(GeometryType.POINT)){
					DataServiceAsync.Util.getInstance().insertMarker(geometry, "", "", display.getCurrentLayer().getId(), new AsyncCallback<Boolean>() {
						
						@Override
						public void onFailure(Throwable caught){
							KingdomInfo.showInfoPopUp("Error", "Error adding point to DB");
						}
						
						@Override
						public void onSuccess(Boolean result){
							if(result){
								// redraw layers
								display.getWfsLayerPackageHashMap().get(display.getCurrentLayer()).getRefreshStrategy().refresh();
								display.getCurrentOLWmsLayer().redraw();
							}else{
								KingdomInfo.showInfoPopUp("Error", "Error adding point");
							}
						}
					});
				}
				
				// LINE
				if(geometryType.equals(GeometryType.LINE) && eventObject.getVectorFeature().getGeometry().getVertices(true).length > 1){
					DataServiceAsync.Util.getInstance().insertLine(geometry, display.getCurrentLayer().getId(), new AsyncCallback<Boolean>() {
						
						@Override
						public void onFailure(Throwable caught){
							KingdomInfo.showInfoPopUp("Error", "Error adding line to DB");
						}
						
						@Override
						public void onSuccess(Boolean result){
							if(result){
								// redraw layers
								display.getWfsLayerPackageHashMap().get(display.getCurrentLayer()).getRefreshStrategy().refresh();
								display.getCurrentOLWmsLayer().redraw();
							}else{
								KingdomInfo.showInfoPopUp("Error", "Error adding line");
							}
						}
					});
				}
				
				// POLYGON
				if(geometryType.equals(GeometryType.POLYGON) && eventObject.getVectorFeature().getGeometry().getVertices(false).length > 2 && !display.isInAddingHolesMode()
						&& !display.isInAddingShapesMode()){
					DataServiceAsync.Util.getInstance().insertPolygon(geometry, display.getCurrentLayer().getId(), new AsyncCallback<Boolean>() {
						
						@Override
						public void onFailure(Throwable caught){
							KingdomInfo.showInfoPopUp("Error", "Error adding polygon to DB");
						}
						
						@Override
						public void onSuccess(Boolean result){
							if(result){
								// redraw layers
								display.getWfsLayerPackageHashMap().get(display.getCurrentLayer()).getRefreshStrategy().refresh();
								display.getCurrentOLWmsLayer().redraw();
							}else{
								KingdomInfo.showInfoPopUp("Error", "Error adding polygon");
							}
						}
					});
				}
				
				// ADD SHAPE
				if(geometryType.equals(GeometryType.POLYGON) && eventObject.getVectorFeature().getGeometry().getVertices(false).length > 2 && display.isInAddingShapesMode()){
					// get all geometries from the displayed vector layer
					VectorFeature[] features = display.getWfsLayerPackageHashMap().get(display.getCurrentLayer()).getWfsLayer().getFeatures();
					
					if(features != null){
						// if new geometry bounds intersect any of the existing
						// geometry bounds
						List<KingdomVectorFeature> partakingFeatures = new ArrayList<KingdomVectorFeature>();
						for(VectorFeature feature : features){
							if(feature.getGeometry().getBounds().intersectsBounds(eventObject.getVectorFeature().getGeometry().getBounds())){
								partakingFeatures.add(new KingdomVectorFeature(feature.getGeometry().toString(), feature.getFID()));
							}
						}
						
						// update partaking features' geometries
						if(!partakingFeatures.isEmpty()){
							DataServiceAsync.Util.getInstance().bindPolygonGeometries(partakingFeatures, eventObject.getVectorFeature().getGeometry().toString(), new AsyncCallback<Boolean>() {
								
								@Override
								public void onFailure(Throwable caught){
									KingdomInfo.showInfoPopUp("Error", "Error binding polygon geometries");
								}
								
								@Override
								public void onSuccess(Boolean result){
									if(result){
										// polygon was updated; refresh
										// layers
										display.getWfsLayerPackageHashMap().get(display.getCurrentLayer()).getRefreshStrategy().refresh();
										display.getCurrentOLWmsLayer().redraw();
									}
								}
							});
						}
					}
				}
				
				// SUBTRACT SHAPE
				if(geometryType.equals(GeometryType.POLYGON) && eventObject.getVectorFeature().getGeometry().getVertices(false).length > 2 && display.isInAddingHolesMode()){
					// get all geometries from the displayed vector layer
					VectorFeature[] features = display.getWfsLayerPackageHashMap().get(display.getCurrentLayer()).getWfsLayer().getFeatures();
					
					if(features != null){
						// if new geometry bounds intersect any of the existing
						// geometry bounds
						for(VectorFeature feature : features){
							if(feature.getGeometry().getBounds().intersectsBounds(eventObject.getVectorFeature().getGeometry().getBounds())){
								
								// slice on serverside, if changes were made,
								// update
								// the geometry
								DataServiceAsync.Util.getInstance().slicePolygonGeometry(feature.getGeometry().toString(), eventObject.getVectorFeature().getGeometry().toString(), feature.getFID(),
										new AsyncCallback<Boolean>() {
											
											@Override
											public void onFailure(Throwable caught){
												KingdomInfo.showInfoPopUp("Error", "Error slicing polygon geometries");
											}
											
											@Override
											public void onSuccess(Boolean result){
												if(result){
													// polygon was updated;
													// refresh
													// layers
													display.getWfsLayerPackageHashMap().get(display.getCurrentLayer()).getRefreshStrategy().refresh();
													display.getCurrentOLWmsLayer().redraw();
												}
											}
										});
							}
						}
					}
				}
			}
		});
		
		// handle drawing toolbar show event
		Bus.get().addHandler(EventEnableDrawingToolbar.TYPE, new EventEnableDrawingToolbarHandler() {
			
			@Override
			public void onEnableDrawingToolbar(EventEnableDrawingToolbar event){
				if(event.isEnabled()){
					display.getEditingToolbar().enable();
				}else{
					display.getEditingToolbar().disable();
				}
			}
		});
		
		// handle edit button group change
		Bus.get().addHandler(EventChangeEditButtonGroup.TYPE, new EventChangeEditButtonGroupHandler() {
			
			@Override
			public void onEventChangeEditButtonGroup(EventChangeEditButtonGroup event){
				display.setEditButtonGroup(event.getGeometryType());
			}
		});
		
		// handle set current layer event
		Bus.get().addHandler(EventSetCurrentLayer.TYPE, new EventSetCurrentLayerHandler() {
			
			@Override
			public void onSetCurrentLayer(EventSetCurrentLayer event){
				display.setCurrentLayer(event.getCurrentLayer());
				
				if(!event.getCurrentLayer().isVisible()){
					display.getEditingToolbar().disable();
				}else{
					display.getEditingToolbar().enable();
				}
				
				// if snap mode is on, refresh it
				if(display.getSnapButton().getValue()){
					display.setSnapEnabled(false);
					display.setSnapEnabled(true);
				}
				
				// if edit mode is on switch active wfs layers
				if(display.getDrawButton().getValue()){
					display.disableEditMode();
					display.enableEditMode(EditingMode.DRAW);
				}
				
				if(display.getDrawRectangleButton().getValue()){
					if(event.getCurrentLayer().getGeometryType().equals(GeometryType.POLYGON)){
						display.disableEditMode();
						display.enableEditMode(EditingMode.DRAW_RECTANGLE);
					}else{
						display.getDrawButton().setValue(true, true);
					}
				}
				
				if(display.getDrawEllipseButton().getValue()){
					if(event.getCurrentLayer().getGeometryType().equals(GeometryType.POLYGON)){
						display.disableEditMode();
						display.enableEditMode(EditingMode.DRAW_ELLIPSE);
					}else{
						display.getDrawButton().setValue(true, true);
					}
				}
				
				if(display.getDrawSquareButton().getValue()){
					if(event.getCurrentLayer().getGeometryType().equals(GeometryType.POLYGON)){
						display.disableEditMode();
						display.enableEditMode(EditingMode.DRAW_SQUARE);
					}else{
						display.getDrawButton().setValue(true, true);
					}
				}
				
				if(display.getDrawCircleButton().getValue()){
					if(event.getCurrentLayer().getGeometryType().equals(GeometryType.POLYGON)){
						display.disableEditMode();
						display.enableEditMode(EditingMode.DRAW_CIRCLE);
					}else{
						display.getDrawButton().setValue(true, true);
					}
				}
				
				if(display.getMoveVerticesButton().getValue()){
					display.disableEditMode();
					display.enableEditMode(EditingMode.MOVE_VERTICES);
				}
				
				if(display.getMoveFeaturesButton().getValue()){
					display.disableEditMode();
					display.enableEditMode(EditingMode.MOVE_FEATURES);
				}
				
				if(display.getDeleteFeaturesButton().getValue()){
					display.disableEditMode();
					display.enableEditMode(EditingMode.DELETE);
				}
				
				if(display.getRotateButton().getValue()){
					display.disableEditMode();
					display.enableEditMode(EditingMode.ROTATE);
				}
				
				if(display.getScaleButton().getValue()){
					display.disableEditMode();
					display.enableEditMode(EditingMode.SCALE);
				}
				
				// TODO: add for more edit modes
			}
		});
		
		// handle map clicks
		display.getOLMap().addMapClickListener(new MapClickListener() {
			
			@Override
			public void onClick(final MapClickEvent mapClickEvent){
				// handle info clicks
				if(display.getInfoButton().getValue() && !display.isInEditMode()){
					
					KingdomLayer selectedLayer = display.getCurrentLayer();
					
					if(selectedLayer != null){
						if(selectedLayer.isVisible()){
							
							String bbox = display.getOLMap().getExtent().toBBox(10).toString();
							
							DataServiceAsync.Util.getInstance().getFeatureInfo(selectedLayer, bbox, (int) display.getOLMap().getSize().getWidth(), (int) display.getOLMap().getSize().getHeight(),
									mapClickEvent.getPixel().x(), mapClickEvent.getPixel().y(), new AsyncCallback<List<KingdomFeature>>() {
										
										@Override
										public void onFailure(Throwable caught){
											KingdomInfo.showInfoPopUp("Error", "Error getting feature info");
										}
										
										@Override
										public void onSuccess(List<KingdomFeature> result){
											if(!result.isEmpty()){
												// if more than one feature was
												// returned show selection
												if(result.size() > 1){
													// bind feature info
													// selection menu events
													if(!featureInfoSelectionDisplay.isBound()){
														featureInfoSelectionDisplay.getMenu().addSelectionHandler(new SelectionHandler<Item>() {
															
															@Override
															public void onSelection(SelectionEvent<Item> event){
																MenuItem selectedItem = (MenuItem) event.getSelectedItem();
																
																// get selected
																// feature
																KingdomFeature selectedFeature = featureInfoSelectionDisplay.getFeatureById(selectedItem.getId());
																
																if(selectedFeature != null){
																	// open
																	// feature
																	// in
																	// feature
																	// editor
																	
																	showEditFeatureDialog(selectedFeature);
																}else{
																	KingdomInfo.showInfoPopUp("Error", "Error retrieving feature from selection menu");
																}
															}
														});
														
														featureInfoSelectionDisplay.setIsBound(true);
													}
													
													// display feature info
													// selection menu
													featureInfoSelectionDisplay.setFeatures(result);
													featureInfoSelectionDisplay.show(display.getOLMap().getViewport(), mapClickEvent.getPixel().x(), mapClickEvent.getPixel().y());
													
												}else{
													// open feature in feature
													// editor
													showEditFeatureDialog(result.get(0));
												}
											}else{
												KingdomInfo.showInfoPopUp("Info", "no features found in this location");
											}
										}
									});
						}
					}else{
						KingdomInfo.showInfoPopUp("Info", "Please select a layer");
					}
				}
			}
		});
		
		// grid button
		display.getGridButton().addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event){
				display.toggleGridVisible(display.getGridButton().getValue());
			}
		});
		
		// handle info button click
		display.getInfoButton().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event){
				if(event.getValue()){
					
				}else{
					
				}
			}
		});
		
		// handle draw button click
		display.getDrawButton().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event){
				if(event.getValue()){
					// enable edit mode
					display.enableEditMode(EditingMode.DRAW);
					
					// if drawing polygons
					if(display.getCurrentLayer().getGeometryType().equals(GeometryType.POLYGON)){
						display.getAddShapeButton().show();
						display.getAddHoleButton().show();
						display.getEditingToolbar().forceLayout();
					}else{
						display.getAddShapeButton().setValue(false);
						display.getAddShapeButton().hide();
						display.getAddHoleButton().setValue(false);
						display.getAddHoleButton().hide();
						display.getEditingToolbar().forceLayout();
					}
				}else{
					// disable edit mode
					display.disableEditMode();
					
					// if drawing polygons and a polygon drawing button isn't
					// active
					if(display.getCurrentLayer().getGeometryType().equals(GeometryType.POLYGON) && !display.getDrawRectangleButton().getValue() && !display.getDrawCircleButton().getValue()
							&& !display.getDrawEllipseButton().getValue() && !display.getDrawSquareButton().getValue()){
						display.getAddShapeButton().setValue(false);
						display.getAddShapeButton().hide();
						display.getAddHoleButton().setValue(false);
						display.getAddHoleButton().hide();
						display.getEditingToolbar().forceLayout();
					}
				}
			}
		});
		
		// click draw rectangle button
		display.getDrawRectangleButton().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event){
				if(event.getValue()){
					// enable edit mode
					display.enableEditMode(EditingMode.DRAW_RECTANGLE);
					
					// if drawing polygons
					if(display.getCurrentLayer().getGeometryType().equals(GeometryType.POLYGON)){
						display.getAddShapeButton().show();
						display.getAddHoleButton().show();
						display.getEditingToolbar().forceLayout();
					}else{
						display.getAddShapeButton().setValue(false);
						display.getAddShapeButton().hide();
						display.getAddHoleButton().setValue(false);
						display.getAddHoleButton().hide();
						display.getEditingToolbar().forceLayout();
					}
				}else{
					// disable edit mode
					display.disableEditMode();
					
					// if drawing polygons
					if(display.getCurrentLayer().getGeometryType().equals(GeometryType.POLYGON) && !display.getDrawButton().getValue() && !display.getDrawCircleButton().getValue()
							&& !display.getDrawEllipseButton().getValue() && !display.getDrawSquareButton().getValue()){
						display.getAddShapeButton().setValue(false);
						display.getAddShapeButton().hide();
						display.getAddHoleButton().setValue(false);
						display.getAddHoleButton().hide();
						display.getEditingToolbar().forceLayout();
					}
				}
			}
		});
		
		// handle draw ellipse button click
		display.getDrawEllipseButton().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event){
				if(event.getValue()){
					// enable edit mode
					display.enableEditMode(EditingMode.DRAW_ELLIPSE);
					
					// if drawing polygons
					if(display.getCurrentLayer().getGeometryType().equals(GeometryType.POLYGON)){
						display.getAddShapeButton().show();
						display.getAddHoleButton().show();
						display.getEditingToolbar().forceLayout();
					}else{
						display.getAddShapeButton().setValue(false);
						display.getAddShapeButton().hide();
						display.getAddHoleButton().setValue(false);
						display.getAddHoleButton().hide();
						display.getEditingToolbar().forceLayout();
					}
				}else{
					// disable edit mode
					display.disableEditMode();
					
					// if drawing polygons
					if(display.getCurrentLayer().getGeometryType().equals(GeometryType.POLYGON) && !display.getDrawButton().getValue() && display.getDrawRectangleButton().getValue()
							&& !display.getDrawCircleButton().getValue() && !display.getDrawSquareButton().getValue()){
						display.getAddShapeButton().setValue(false);
						display.getAddShapeButton().hide();
						display.getAddHoleButton().setValue(false);
						display.getAddHoleButton().hide();
						display.getEditingToolbar().forceLayout();
					}
				}
			}
		});
		
		// handle draw square button click
		display.getDrawSquareButton().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event){
				if(event.getValue()){
					// enable edit mode
					display.enableEditMode(EditingMode.DRAW_SQUARE);
					
					// if drawing polygons
					if(display.getCurrentLayer().getGeometryType().equals(GeometryType.POLYGON)){
						display.getAddShapeButton().show();
						display.getAddHoleButton().show();
						display.getEditingToolbar().forceLayout();
					}else{
						display.getAddShapeButton().setValue(false);
						display.getAddShapeButton().hide();
						display.getAddHoleButton().setValue(false);
						display.getAddHoleButton().hide();
						display.getEditingToolbar().forceLayout();
					}
				}else{
					// disable edit mode
					display.disableEditMode();
					
					// if drawing polygons
					if(display.getCurrentLayer().getGeometryType().equals(GeometryType.POLYGON) && !display.getDrawButton().getValue() && display.getDrawRectangleButton().getValue()
							&& !display.getDrawCircleButton().getValue() && !display.getDrawEllipseButton().getValue()){
						display.getAddShapeButton().setValue(false);
						display.getAddShapeButton().hide();
						display.getAddHoleButton().setValue(false);
						display.getAddHoleButton().hide();
						display.getEditingToolbar().forceLayout();
					}
				}
			}
		});
		
		// handle draw circle button click
		display.getDrawCircleButton().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event){
				if(event.getValue()){
					// enable edit mode
					display.enableEditMode(EditingMode.DRAW_CIRCLE);
					
					// if drawing polygons
					if(display.getCurrentLayer().getGeometryType().equals(GeometryType.POLYGON)){
						display.getAddShapeButton().show();
						display.getAddHoleButton().show();
						display.getEditingToolbar().forceLayout();
					}else{
						display.getAddShapeButton().setValue(false);
						display.getAddShapeButton().hide();
						display.getAddHoleButton().setValue(false);
						display.getAddHoleButton().hide();
						display.getEditingToolbar().forceLayout();
					}
				}else{
					// disable edit mode
					display.disableEditMode();
					
					// if drawing polygons
					if(display.getCurrentLayer().getGeometryType().equals(GeometryType.POLYGON) && !display.getDrawRectangleButton().getValue() && !display.getDrawButton().getValue()
							&& !display.getDrawEllipseButton().getValue() && !display.getDrawSquareButton().getValue()){
						display.getAddShapeButton().setValue(false);
						display.getAddShapeButton().hide();
						display.getAddHoleButton().setValue(false);
						display.getAddHoleButton().hide();
						display.getEditingToolbar().forceLayout();
					}
				}
			}
		});
		
		// handle add shapes button click
		display.getAddShapeButton().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event){
				display.setAddingShapesMode(event.getValue());
			}
		});
		
		// handle add holes button click
		display.getAddHoleButton().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event){
				display.setAddingHolesMode(event.getValue());
			}
		});
		
		// handle move features button click
		display.getMoveFeaturesButton().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event){
				if(event.getValue()){
					// enable edit mode
					display.enableEditMode(EditingMode.MOVE_FEATURES);
				}else{
					// disable edit mode
					display.disableEditMode();
				}
			}
		});
		
		// handle move vertices button click
		display.getMoveVerticesButton().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event){
				if(event.getValue()){
					// enable edit mode
					display.enableEditMode(EditingMode.MOVE_VERTICES);
				}else{
					// disable edit mode
					display.disableEditMode();
				}
			}
		});
		
		// handle rotate button click
		display.getRotateButton().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event){
				if(event.getValue()){
					// enable edit mode
					display.enableEditMode(EditingMode.ROTATE);
				}else{
					// disable edit mode
					display.disableEditMode();
				}
			}
		});
		
		// handle scale button click
		display.getScaleButton().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event){
				if(event.getValue()){
					// enable edit mode
					display.enableEditMode(EditingMode.SCALE);
				}else{
					// disable edit mode
					display.disableEditMode();
				}
			}
		});
		
		// handle snap button click
		display.getSnapButton().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event){
				display.setSnapEnabled(event.getValue());
			}
		});
		
		// handle delete feature button click
		display.getDeleteFeaturesButton().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event){
				if(event.getValue()){
					// enable edit mode
					display.enableEditMode(EditingMode.DELETE);
				}else{
					// disable edit mode
					display.disableEditMode();
				}
			}
		});
		
		// handle layer visibility event
		Bus.get().addHandler(EventSetLayerVisibility.TYPE, new EventSetLayerVisibilityHandler() {
			
			@Override
			public void onSetLayerVisibility(EventSetLayerVisibility event){
				// if we were just editing this layer disable edit mode
				if(display.isInEditMode()){
					if(event.getLayer().getId() == display.getEditingLayer().getId()){
						Bus.get().fireEvent(new EventDisableEditMode());
					}
				}
				
				display.setLayerVisibility(event.getLayer(), event.isVisible());
				
				// if this layer is also selected
				if(display.getCurrentLayer() != null){
					if(event.getLayer().getId() == display.getCurrentLayer().getId()){
						if(event.isVisible()){
							display.getEditingToolbar().enable();
						}else{
							display.getEditingToolbar().disable();
						}
					}
				}
			}
		});
		
		// handle edit mode toggle events
		Bus.get().addHandler(EventDisableEditMode.TYPE, new EventDisableEditModeHandler() {
			
			@Override
			public void onDisableEditMode(EventDisableEditMode event){
				
				// if snap mode is on, refresh it
				if(display.getSnapButton().getValue()){
					display.setSnapEnabled(false);
				}
				
				display.disableEditMode();
				
				// toggle any edit buttons
				if(display.getDrawButton().getValue()){
					display.getDrawButton().setValue(false, false);
				}
				
				if(display.getDrawRectangleButton().getValue()){
					display.getDrawRectangleButton().setValue(false, false);
				}
				
				if(display.getDrawCircleButton().getValue()){
					display.getDrawCircleButton().setValue(false, false);
				}
				
				if(display.getMoveFeaturesButton().getValue()){
					display.getMoveFeaturesButton().setValue(false, false);
				}
				
				if(display.getMoveVerticesButton().getValue()){
					display.getMoveVerticesButton().setValue(false, false);
				}
				
				if(display.getRotateButton().getValue()){
					display.getRotateButton().setValue(false, false);
				}
				
				if(display.getScaleButton().getValue()){
					display.getScaleButton().setValue(false, false);
				}
				
				if(display.getDeleteFeaturesButton().getValue()){
					display.getDeleteFeaturesButton().setValue(false, false);
				}
				
				// TODO: add future edit buttons
			}
		});
		
		// handle remove layer from view event
		Bus.get().addHandler(EventRemoveLayerFromMapView.TYPE, new EventRemoveLayerFromMapViewHandler() {
			
			@Override
			public void onRemoveLayerFromMapView(EventRemoveLayerFromMapView event){
				display.removeLayer(event.getLayer());
			}
		});
		
		// handle remove current map event
		Bus.get().addHandler(EventRemoveCurrentMap.TYPE, new EventRemoveCurrentMapHandler() {
			
			@Override
			public void onRemoveCurrentMap(EventRemoveCurrentMap event){
				display.removeMapOverlays();
				Bus.get().fireEvent(new EventEnableMapView(false));
			}
		});
		
		// handle zoom to full extent button click
		display.getZoomToFullExtentButton().addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event){
				display.getOLMap().zoomToExtent(display.getOLMap().getMaxExtent());
			}
		});
		
		// handle navigate back button click
		display.getNavigateBackButton().addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event){
				display.getNavigationHistoryControl().previous();
			}
		});
		
		// handle navigate forward button click
		display.getNavigateForwardButton().addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event){
				display.getNavigationHistoryControl().next();
			}
		});
		
		// handle bring to front button click
		display.getBringToFrontButton().addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event){
				Bus.get().fireEvent(new EventGetSelectedLayer() {
					
					@Override
					public void setLayer(KingdomLayer selectedLayer){
						if(selectedLayer != null){
							display.bringLayerToFront(selectedLayer);
							Bus.get().fireEvent(new EventSortLayerTree());
						}
					}
				});
			}
		});
		
		// handle send to back button click
		display.getSendToBackButton().addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event){
				Bus.get().fireEvent(new EventGetSelectedLayer() {
					
					@Override
					public void setLayer(KingdomLayer selectedLayer){
						if(selectedLayer != null){
							display.sendLayerToBack(selectedLayer);
							Bus.get().fireEvent(new EventSortLayerTree());
						}
					}
				});
			}
		});
		
		// handle edit layer style button click
		display.getEditLayerStyleButton().addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event){
				// show edit layer style window
				editLayerStyleDisplay = new EditLayerStyleWindow(display.getCurrentLayer(), display.getCurrentMap().getScales());
				
				// bind events
				if(!editLayerStyleDisplay.isBound()){
					
					editLayerStyleDisplay.getApplyButton().addSelectHandler(new SelectHandler() {
						
						@Override
						public void onSelect(SelectEvent event){
							// apply new values to layer object
							KingdomLayer layer = editLayerStyleDisplay.getLayer();
							
							if(layer.getGeometryType().equals(GeometryType.POINT)){
								layer.setColor(editLayerStyleDisplay.getColor());
								layer.setLabelColor(editLayerStyleDisplay.getLabelColor());
								layer.setLabelFillColor(editLayerStyleDisplay.getLabelFillColor());
								layer.setSize(editLayerStyleDisplay.getSize());
								layer.setMarkerImage(editLayerStyleDisplay.getMarkerComboBox().getValue().getImageName());
								if(editLayerStyleDisplay.getMarkerComboBox().getValue().getImageName().equals("point")){
									layer.setStyle("kingdom_point");
								}else{
									layer.setStyle("marker");
								}
								
								if(editLayerStyleDisplay.getLabelCheckBox().getValue()){
									layer.setStyle(layer.getStyle() + "_label");
								}
							}else if(layer.getGeometryType().equals(GeometryType.LINE)){
								layer.setColor(editLayerStyleDisplay.getColor());
								layer.setStrokeWidth(editLayerStyleDisplay.getSize());
							}else if(layer.getGeometryType().equals(GeometryType.POLYGON)){
								layer.setColor(editLayerStyleDisplay.getColor());
								layer.setFillColor(editLayerStyleDisplay.getFillColor());
								layer.setStrokeWidth(editLayerStyleDisplay.getSize());
								layer.setStrokeOpacity(editLayerStyleDisplay.getStrokeOpacitySpinner().getValue());
								layer.setFillOpacity(editLayerStyleDisplay.getFillOpacitySpinner().getValue());
								if(editLayerStyleDisplay.getTextureComboBox().getValue() != null){
									layer.setTextureImage(editLayerStyleDisplay.getTextureComboBox().getValue().getImageName());
								}
								layer.setStyle(editLayerStyleDisplay.getTextureCheckBox().getValue() ? "kingdom_polygon_texture" : "kingdom_polygon");
							}else if(layer.getGeometryType().equals(GeometryType.MARKER)){
								layer.setColor(editLayerStyleDisplay.getColor());
								layer.setLabelColor(editLayerStyleDisplay.getLabelColor());
								layer.setLabelFillColor(editLayerStyleDisplay.getLabelFillColor());
								layer.setSize(editLayerStyleDisplay.getSize());
								layer.setFillColor(editLayerStyleDisplay.getFillColor());
								layer.setMarkerImage(editLayerStyleDisplay.getMarkerComboBox().getValue().getImageName());
								
								if(editLayerStyleDisplay.getMarkerComboBox().getValue().getImageName().equals("point")){
									layer.setStyle("kingdom_point");
								}else{
									layer.setStyle("marker");
								}
								
								if(editLayerStyleDisplay.getLabelCheckBox().getValue()){
									layer.setStyle(layer.getStyle() + "_label");
								}
							}
							
							layer.setMaxScale(editLayerStyleDisplay.getLowerSliderScale());
							layer.setMinScale(editLayerStyleDisplay.getUpperSliderScale());
							
							editLayerStyleDisplay.hide();
							
							// update layer to db
							updateLayerStyleDB(layer);
							
							// re-apply layer env parameter
							display.getCurrentOLWmsLayer().getParams().setParameter("env", layer.getEnvValues());
							
							// refresh layer maxscale and minscale
							display.refreshLayerScaleLimit(layer);
							
							// refresh layer style
							display.getCurrentOLWmsLayer().getParams().setStyles(layer.getStyle());
							
							// redraw wms layer
							display.getCurrentOLWmsLayer().redraw();
							
							// redraw layer tree
							Bus.get().fireEvent(new EventMapScaleChanged(display.getOLMap().getScale()));
						}
					});
					
					editLayerStyleDisplay.getCancelButton().addSelectHandler(new SelectHandler() {
						
						@Override
						public void onSelect(SelectEvent event){
							editLayerStyleDisplay.hide();
						}
					});
					
					editLayerStyleDisplay.setIsBound(true);
				}
				
				editLayerStyleDisplay.show();
			}
		});
		
		// handle set upper limit button click
		display.getSetUpperLimitButton().addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event){
				// update object
				KingdomLayer layer = display.getCurrentLayer();
				layer.setMinScale((float) Math.round(display.getOLMap().getScale()));
				
				// refresh view
				display.refreshLayerScaleLimit(layer);
				
				// redraw layer tree
				Bus.get().fireEvent(new EventMapScaleChanged(display.getOLMap().getScale()));
				
				// save layer style to db
				updateLayerStyleDB(layer);
			}
		});
		
		// handle set lower limit button click
		display.getSetLowerLimitButton().addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event){
				// update object
				KingdomLayer layer = display.getCurrentLayer();
				
				layer.setMaxScale((float) Math.round(display.getOLMap().getScale()));
				
				// refresh view
				display.refreshLayerScaleLimit(layer);
				
				// redraw layer tree
				Bus.get().fireEvent(new EventMapScaleChanged(display.getOLMap().getScale()));
				
				// save layer style to db
				updateLayerStyleDB(layer);
			}
		});
		
		// handle escape key presses
		RootPanel.get().addDomHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event){
				if(event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE){
					if(display.getCurrentDrawControl() != null && display.isInEditMode()){
						display.getCurrentDrawControl().cancel();
					}
				}
			}
		}, KeyDownEvent.getType());
		
		// handle measure distance toggle
		display.getMeasureDistanceButton().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event){
				KingdomMeasure measure = display.getMeasureDistanceControl();
				
				if(event.getValue()){
					measure.activate();
					measureDisplay.hide();
				}else{
					measure.deactivate();
					measureDisplay.hide();
				}
			}
		});
		
		// handle measure area toggle
		display.getMeasureAreaButton().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event){
				KingdomMeasure measure = display.getMeasureAreaControl();
				
				if(event.getValue()){
					measure.activate();
					measureDisplay.hide();
				}else{
					measure.deactivate();
					measureDisplay.hide();
				}
			}
		});
		
		// Measure view activation listener.
		ControlActivateListener cal = new ControlActivateListener() {
			
			@Override
			public void onActivate(ControlActivateEvent eventObject){
				eventObject.getSource().getLayer().addVectorFeatureAddedListener(new VectorFeatureAddedListener() {
					
					@Override
					public void onFeatureAdded(FeatureAddedEvent eventObject){
						if(!measureDisplay.isVisible()){
							// show measurement window
							measureDisplay.show();
							
							// position the measurement window in the bottom
							// right corner of the map view
							measureDisplay.resetPosition(display.getOLMap().getViewport(), 10);
						}
					}
				});
			}
		};
		
		// add measure distance activate listener
		display.getMeasureDistanceControl().addControlActivateListener(cal);
		
		// add measure area activate listener
		display.getMeasureAreaControl().addControlActivateListener(cal);
		
		// measure distance partial display handler
		display.getMeasureDistanceControl().addMeasurePartialListener(new MeasurePartialListener() {
			
			@Override
			public void onMeasurePartial(MeasureEvent eventObject){
				measureDisplay.setMeasurementText(NumberFormat.getFormat("0.000").format(eventObject.getMeasure()) + " " + eventObject.getUnits());
			}
		});
		
		// measure area partial display handler
		display.getMeasureAreaControl().addMeasurePartialListener(new MeasurePartialListener() {
			
			@Override
			public void onMeasurePartial(MeasureEvent eventObject){
				measureDisplay.setMeasurementText(NumberFormat.getFormat("0.000").format(eventObject.getMeasure()) + " " + eventObject.getUnits() + "²");
			}
		});
		
		// handle save map state button click
		display.getSaveMapStateButton().addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event){
				// update map state
				updateLayersDB(display.getLayerList(), true);
				
				// update current view
				updatePreviousMapView();
			}
		});
		
		// handle layer opacity
		Bus.get().addHandler(EventSetLayerOpacity.TYPE, new EventSetLayerOpacityHandler() {
			
			@Override
			public void onSetCurrentLayer(EventSetLayerOpacity event){
				display.setLayerOpacity(event.getLayer(), event.getOpacity() / 100);
			}
		});
		
		// map zoomend
		display.getOLMap().getEvents().register("zoomend", display.getOLMap(), new EventHandler() {
			
			@Override
			public void onHandle(EventObject eventObject){
				Bus.get().fireEvent(new EventMapScaleChanged(display.getOLMap().getScale()));
			}
		});
		
		// credits
		display.getCreditsButton().addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event){
				creditsDisplay.show();
			}
		});
		
		// add users (admin only)
		display.getAddUsersButton().addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event){
				// show add user dialog
				if(!addUserDisplay.isBound()){
					addUserDisplay.getAddButton().addSelectHandler(new SelectHandler() {
						
						@Override
						public void onSelect(SelectEvent event){
							if(addUserDisplay.isValid()){
								// TODO: better validation (user exists, nasty
								// sql injections)
								
								// create user object
								KingdomUser newUser = new KingdomUser(0, addUserDisplay.getUsernameField().getText(), addUserDisplay.getPasswordField().getText(), false);
								
								// add user to db
								DataServiceAsync.Util.getInstance().addUser(newUser, new AsyncCallback<Integer>() {
									
									@Override
									public void onFailure(Throwable caught){
										KingdomInfo.showInfoPopUp("Error", "Error adding new user");
									}
									
									@Override
									public void onSuccess(Integer result){
										KingdomInfo.showInfoPopUp("Success", "New user added successfully");
										addUserDisplay.hide();
									}
								});
							}
						}
					});
					
					addUserDisplay.getCancelButton().addSelectHandler(new SelectHandler() {
						
						@Override
						public void onSelect(SelectEvent event){
							addUserDisplay.hide();
						}
					});
				}
				
				addUserDisplay.show();
			}
		});
		
		// log out
		display.getLogOutButton().addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event){
				// update map state
				DataServiceAsync.Util.getInstance().updateLayers(display.getLayerList(), new AsyncCallback<Boolean>() {
					
					@Override
					public void onFailure(Throwable caught){
						Window.Location.replace("j_spring_security_logout");
					}
					
					@Override
					public void onSuccess(Boolean result){
						
						// update current view
						KingdomMap currentMap = display.getCurrentMap();
						Bounds currentView = display.getOLMap().getExtent();
						currentMap.setPreviousViewllx(currentView.getLowerLeftX());
						currentMap.setPreviousViewlly(currentView.getLowerLeftY());
						currentMap.setPreviousViewurx(currentView.getUpperRightX());
						currentMap.setPreviousViewury(currentView.getUpperRightY());
						currentMap.setPreviousZoomLevel(display.getOLMap().getZoom());
						
						DataServiceAsync.Util.getInstance().updateMapPreviousView(currentMap, new AsyncCallback<Boolean>() {
							
							@Override
							public void onFailure(Throwable caught){
								Window.Location.replace("j_spring_security_logout");
							}
							
							@Override
							public void onSuccess(Boolean result){
								Window.Location.replace("j_spring_security_logout");
							}
						});
					}
				});
			}
		});
	}
	
	// this updates the layers visibility and z indices
	private void updateLayersDB(List<KingdomLayer> layers, final boolean sourceIsButton){
		if(sourceIsButton){
			KingdomInfo.showLoadingBar("Saving", "Saving map state", "saving...");
		}
		
		DataServiceAsync.Util.getInstance().updateLayers(layers, new AsyncCallback<Boolean>() {
			
			@Override
			public void onFailure(Throwable caught){
				if(sourceIsButton){
					KingdomInfo.hideLoadingBar();
				}
				KingdomInfo.showInfoPopUp("Error", "Error saving map state");
			}
			
			@Override
			public void onSuccess(Boolean result){
				if(sourceIsButton){
					KingdomInfo.hideLoadingBar();
					KingdomInfo.showInfoPopUp("Success", "Map state was saved successfully");
				}else{
					KingdomInfo.showInfoPopUp("Autosave", "Map state was saved");
				}
			}
		});
	}
	
	private void updateLayerStyleDB(KingdomLayer layer){
		DataServiceAsync.Util.getInstance().updateLayerStyle(layer, new AsyncCallback<KingdomLayer>() {
			
			@Override
			public void onFailure(Throwable caught){
				KingdomInfo.showInfoPopUp("Error", "Error saving layer style");
			}
			
			@Override
			public void onSuccess(KingdomLayer layer){
			}
		});
	}
	
	private void updatePreviousMapView(){
		KingdomMap currentMap = display.getCurrentMap();
		Bounds currentView = display.getOLMap().getExtent();
		currentMap.setPreviousViewllx(currentView.getLowerLeftX());
		currentMap.setPreviousViewlly(currentView.getLowerLeftY());
		currentMap.setPreviousViewurx(currentView.getUpperRightX());
		currentMap.setPreviousViewury(currentView.getUpperRightY());
		currentMap.setPreviousZoomLevel(display.getOLMap().getZoom());
		
		DataServiceAsync.Util.getInstance().updateMapPreviousView(currentMap, new AsyncCallback<Boolean>() {
			
			@Override
			public void onFailure(Throwable caught){
				KingdomInfo.showInfoPopUp("Error", "Error saving current map view");
			}
			
			@Override
			public void onSuccess(Boolean result){
			}
		});
	}
	
	// edit feature dialog
	private void showEditFeatureDialog(final KingdomFeature feature){
		editFeatureDisplay = new EditFeatureDialog(feature);
		
		editFeatureDisplay.getSaveButton().addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event){
				// validate
				if(editFeatureDisplay.isValid()){
					
					KingdomInfo.showLoadingBar("Updating", "Updating feature info", "updating...");
					
					// save new feature info
					feature.setLabel(editFeatureDisplay.getLabelField().getText());
					feature.setDescription(editFeatureDisplay.getDescriptionArea().getText());
					
					// update feature info
					DataServiceAsync.Util.getInstance().updateFeatureInfo(display.getCurrentLayer(), feature.getLabel(), feature.getDescription(), feature.getFeatureId(),
							new AsyncCallback<Boolean>() {
								
								@Override
								public void onFailure(Throwable caught){
									KingdomInfo.showInfoPopUp("Error", "Error updating feature info");
									KingdomInfo.hideLoadingBar();
								}
								
								@Override
								public void onSuccess(Boolean result){
									if(result){
										KingdomInfo.showInfoPopUp("Success", "Feature info successfully updated");
										editFeatureDisplay.hide();
										KingdomInfo.hideLoadingBar();
										
										// refresh layer
										display.getCurrentOLWmsLayer().redraw();
									}else{
										KingdomInfo.showInfoPopUp("Error", "Error updating feature info");
										editFeatureDisplay.hide();
										KingdomInfo.hideLoadingBar();
									}
								}
							});
				}
			}
		});
		
		editFeatureDisplay.getCloseButton().addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event){
				editFeatureDisplay.hide();
			}
		});
		
		editFeatureDisplay.getLabelField().addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event){
				editFeatureDisplay.getSaveButton().enable();
			}
		});
		
		editFeatureDisplay.getDescriptionArea().addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event){
				editFeatureDisplay.getSaveButton().enable();
			}
		});
		
		// show dialog
		editFeatureDisplay.show();
	}
}
