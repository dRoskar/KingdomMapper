package si.roskar.diploma.client.presenter;

import java.util.ArrayList;
import java.util.List;

import org.gwtopenmaps.openlayers.client.Map;
import org.gwtopenmaps.openlayers.client.control.DrawFeature;
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
import si.roskar.diploma.client.util.WFSLayerPackage;
import si.roskar.diploma.client.view.AddMarkerDialog;
import si.roskar.diploma.client.view.EditLayerStyleWindow;
import si.roskar.diploma.client.view.View;
import si.roskar.diploma.shared.EditingMode;
import si.roskar.diploma.shared.GeometryType;
import si.roskar.diploma.shared.KingdomLayer;
import si.roskar.diploma.shared.KingdomMap;
import si.roskar.diploma.shared.KingdomVectorFeature;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.ColorPalette;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.DoubleSpinnerField;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class MapPresenter extends PresenterImpl<MapPresenter.Display>{
	
	public interface Display extends View{
		
		KingdomMap getMapObject();
		
		void addNewMap(KingdomMap newMap);
		
		void addLayer(KingdomLayer layer);
		
		ToolBar getDrawingToolbar();
		
		ToggleButton getDrawButton();
		
		ToggleButton getMoveFeaturesButton();
		
		ToggleButton getMoveVerticesButton();
		
		ToggleButton getDeleteFeaturesButton();
		
		ToggleButton getRotateButton();
		
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
		
		ToggleButton getGridButton();
		
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
		
		TextButton getBringToFrontButton();
		
		TextButton getSendToBackButton();
		
		TextButton getEditLayerStyleButton();
		
		void bringLayerToFront(KingdomLayer selectedLayer);
		
		void sendLayerToBack(KingdomLayer selectedLayer);
		
		List<KingdomLayer> getLayerList();
		
		TextButton getSaveMapStateButton();
		
		void setSnapEnabled(boolean enabled);
		
		void setLayerOpacity(KingdomLayer layer, float opacity);
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
		
		void setColorPickerWindow(ColorPickerWindow colorPickerWindow);
		
		ColorPickerWindow getColorPickerWindow();
		
		SimpleComboBox<String> getShapeComboBox();
		
		int getSize();
		
		TextButton getApplyButton();
		
		TextButton getCancelButton();
		
		String getColor();
		
		String getFillColor();
		
		DoubleSpinnerField getStrokeOpacitySpinner();
		
		DoubleSpinnerField getFillOpacitySpinner();
	}
	
	private AddMarkerDisplay		addMarkerDisplay		= null;
	private EditLayerStyleDisplay	editLayerStyleDisplay	= null;
	
	public MapPresenter(Display display){
		super(display);
		
		addMarkerDisplay = new AddMarkerDialog();
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
					updateLayersDB(display.getLayerList());
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
									DataServiceAsync.Util.getInstance().insertMarker("http://127.0.0.1:8080/geoserver/wms/", addMarkerDisplay.getGeometry(),
											addMarkerDisplay.getLabelField().getText(), addMarkerDisplay.getDescriptionField().getText(), display.getCurrentLayer().getId(), new AsyncCallback<Void>() {
												
												@Override
												public void onFailure(Throwable caught){
												}
												
												@Override
												public void onSuccess(Void result){
													System.out.println("marker added");
													
													// redraw layers
													display.getWfsLayerPackageHashMap().get(display.getCurrentLayer()).getRefreshStrategy().refresh();
													display.getCurrentOLWmsLayer().redraw();
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
					DataServiceAsync.Util.getInstance().insertMarker("http://127.0.0.1:8080/geoserver/wms/", geometry, "", "", display.getCurrentLayer().getId(), new AsyncCallback<Void>() {
						
						@Override
						public void onFailure(Throwable caught){
						}
						
						@Override
						public void onSuccess(Void result){
							System.out.println("point added");
							
							// redraw layers
							display.getWfsLayerPackageHashMap().get(display.getCurrentLayer()).getRefreshStrategy().refresh();
							display.getCurrentOLWmsLayer().redraw();
						}
					});
				}
				
				// LINE
				if(geometryType.equals(GeometryType.LINE) && eventObject.getVectorFeature().getGeometry().getVertices(true).length > 1){
					DataServiceAsync.Util.getInstance().insertLine("http://127.0.0.1:8080/geoserver/wms/", geometry, "", display.getCurrentLayer().getId(), new AsyncCallback<Void>() {
						
						@Override
						public void onFailure(Throwable caught){
						}
						
						@Override
						public void onSuccess(Void result){
							System.out.println("line added");
							
							// redraw layers
							display.getWfsLayerPackageHashMap().get(display.getCurrentLayer()).getRefreshStrategy().refresh();
							display.getCurrentOLWmsLayer().redraw();
						}
					});
				}
				
				// POLYGON
				if(geometryType.equals(GeometryType.POLYGON) && eventObject.getVectorFeature().getGeometry().getVertices(false).length > 2 && !display.isInAddingHolesMode()
						&& !display.isInAddingShapesMode()){
					DataServiceAsync.Util.getInstance().insertPolygon("http://127.0.0.1:8080/geoserver/wms/", geometry, "", display.getCurrentLayer().getId(), new AsyncCallback<Void>() {
						
						@Override
						public void onFailure(Throwable caught){
						}
						
						@Override
						public void onSuccess(Void result){
							System.out.println("polygon added");
							
							// redraw layers
							display.getWfsLayerPackageHashMap().get(display.getCurrentLayer()).getRefreshStrategy().refresh();
							display.getCurrentOLWmsLayer().redraw();
						}
					});
				}
				
				// ADD SHAPE
				if(geometryType.equals(GeometryType.POLYGON) && eventObject.getVectorFeature().getGeometry().getVertices(false).length > 2 && display.isInAddingShapesMode()){
					// get all geometries from the displayed vector layer
					VectorFeature[] features = display.getWfsLayerPackageHashMap().get(display.getCurrentLayer()).getWfsLayer().getFeatures();
					
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
						DataServiceAsync.Util.getInstance().bindPolygonGeometries("http://127.0.0.1:8080/geoserver/wms/", partakingFeatures, eventObject.getVectorFeature().getGeometry().toString(), new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught){
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
				
				// SUBTRACT SHAPE
				if(geometryType.equals(GeometryType.POLYGON) && eventObject.getVectorFeature().getGeometry().getVertices(false).length > 2 && display.isInAddingHolesMode()){
					// get all geometries from the displayed vector layer
					VectorFeature[] features = display.getWfsLayerPackageHashMap().get(display.getCurrentLayer()).getWfsLayer().getFeatures();
					
					// if new geometry bounds intersect any of the existing
					// geometry bounds
					for(VectorFeature feature : features){
						if(feature.getGeometry().getBounds().intersectsBounds(eventObject.getVectorFeature().getGeometry().getBounds())){
							
							// slice on serverside, if changes were made, update
							// the geometry
							DataServiceAsync.Util.getInstance().slicePolygonGeometry("http://127.0.0.1:8080/geoserver/wms/", feature.getGeometry().toString(),
									eventObject.getVectorFeature().getGeometry().toString(), feature.getFID(), new AsyncCallback<Boolean>() {
										
										@Override
										public void onFailure(Throwable caught){
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
			}
		});
		
		// handle drawing toolbar show event
		Bus.get().addHandler(EventEnableDrawingToolbar.TYPE, new EventEnableDrawingToolbarHandler() {
			
			@Override
			public void onEnableDrawingToolbar(EventEnableDrawingToolbar event){
				if(event.isEnabled()){
					display.getDrawingToolbar().enable();
				}else{
					display.getDrawingToolbar().disable();
				}
			}
		});
		
		// handle draw button type change event
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
					display.getDrawingToolbar().disable();
				}else{
					display.getDrawingToolbar().enable();
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
				
				// TODO: add for more edit modes
			}
		});
		
		// grid button
		display.getGridButton().addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event){
				display.toggleGridVisible(display.getGridButton().getValue());
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
						display.getDrawingToolbar().forceLayout();
					}else{
						display.getAddShapeButton().setValue(false);
						display.getAddShapeButton().hide();
						display.getAddHoleButton().setValue(false);
						display.getAddHoleButton().hide();
						display.getDrawingToolbar().forceLayout();
					}
				}else{
					// disable edit mode
					display.disableEditMode();
					
					// if drawing polygons
					if(display.getCurrentLayer().getGeometryType().equals(GeometryType.POLYGON)){
						display.getAddShapeButton().setValue(false);
						display.getAddShapeButton().hide();
						display.getAddHoleButton().setValue(false);
						display.getAddHoleButton().hide();
						display.getDrawingToolbar().forceLayout();
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
				}
				else{
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
							display.getDrawingToolbar().enable();
						}else{
							display.getDrawingToolbar().disable();
						}
					}
				}
			}
		});
		
		// handle edit mode toggle events
		Bus.get().addHandler(EventDisableEditMode.TYPE, new EventDisableEditModeHandler() {
			
			@Override
			public void onDisableEditMode(EventDisableEditMode event){
				display.disableEditMode();
				
				// toggle any edit buttons
				if(display.getDrawButton().getValue()){
					display.getDrawButton().setValue(false, false);
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
				editLayerStyleDisplay = new EditLayerStyleWindow(display.getCurrentLayer());
				
				// bind events
				if(!editLayerStyleDisplay.isBound()){
					
					editLayerStyleDisplay.getApplyButton().addSelectHandler(new SelectHandler() {
						
						@Override
						public void onSelect(SelectEvent event){
							if(editLayerStyleDisplay.getLayer().getGeometryType().equals(GeometryType.POINT) || editLayerStyleDisplay.getLayer().getGeometryType().equals(GeometryType.MARKER)){
								editLayerStyleDisplay.getLayer().setColor(editLayerStyleDisplay.getColor());
								editLayerStyleDisplay.getLayer().setSize(editLayerStyleDisplay.getSize());
								editLayerStyleDisplay.getLayer().setShape(editLayerStyleDisplay.getShapeComboBox().getValue());
							}else if(editLayerStyleDisplay.getLayer().getGeometryType().equals(GeometryType.LINE)){
								editLayerStyleDisplay.getLayer().setColor(editLayerStyleDisplay.getColor());
								editLayerStyleDisplay.getLayer().setStrokeWidth(editLayerStyleDisplay.getSize());
							}else if(editLayerStyleDisplay.getLayer().getGeometryType().equals(GeometryType.POLYGON)){
								editLayerStyleDisplay.getLayer().setColor(editLayerStyleDisplay.getColor());
								editLayerStyleDisplay.getLayer().setFillColor(editLayerStyleDisplay.getFillColor());
								editLayerStyleDisplay.getLayer().setStrokeWidth(editLayerStyleDisplay.getSize());
								editLayerStyleDisplay.getLayer().setStrokeOpacity(editLayerStyleDisplay.getStrokeOpacitySpinner().getValue());
								editLayerStyleDisplay.getLayer().setFillOpacity(editLayerStyleDisplay.getFillOpacitySpinner().getValue());
							}
							
							editLayerStyleDisplay.hide();
							
							// update layer to db
							updateLayerStyleDB(editLayerStyleDisplay.getLayer());
							
							// re-apply layer env parameter
							display.getCurrentOLWmsLayer().getParams().setParameter("env", display.getCurrentLayer().getEnvValues());
							
							// redraw wms layer
							display.getCurrentOLWmsLayer().redraw();
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
		
		// save data when window is closed
		Window.addWindowClosingHandler(new ClosingHandler() {
			
			@Override
			public void onWindowClosing(ClosingEvent event){
				updateLayersDB(display.getLayerList());
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
		
		// handle save map state button click
		display.getSaveMapStateButton().addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event){
				updateLayersDB(display.getLayerList());
			}
		});
		
		// handle layer opacity
		Bus.get().addHandler(EventSetLayerOpacity.TYPE, new EventSetLayerOpacityHandler(){

			@Override
			public void onSetCurrentLayer(EventSetLayerOpacity event){
				display.setLayerOpacity(event.getLayer(), event.getOpacity() / 100);
			}
		});
	}
	
	// this updates the layers visibility and z indices
	private void updateLayersDB(List<KingdomLayer> layers){
		DataServiceAsync.Util.getInstance().updateLayers(layers, new AsyncCallback<Boolean>() {
			
			@Override
			public void onFailure(Throwable caught){
			}
			
			@Override
			public void onSuccess(Boolean result){
			}
		});
	}
	
	private void updateLayerStyleDB(KingdomLayer layer){
		DataServiceAsync.Util.getInstance().updateLayerStyle(layer, new AsyncCallback<KingdomLayer>() {
			
			@Override
			public void onFailure(Throwable caught){
			}
			
			@Override
			public void onSuccess(KingdomLayer layer){
			}
		});
	}
}
