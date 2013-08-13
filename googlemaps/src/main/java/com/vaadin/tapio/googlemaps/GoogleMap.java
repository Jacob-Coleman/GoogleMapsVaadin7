package com.vaadin.tapio.googlemaps;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vaadin.tapio.googlemaps.client.GoogleMapControl;
import com.vaadin.tapio.googlemaps.client.GoogleMapMarker;
import com.vaadin.tapio.googlemaps.client.GoogleMapMarkerClickedRpc;
import com.vaadin.tapio.googlemaps.client.GoogleMapMarkerDraggedRpc;
import com.vaadin.tapio.googlemaps.client.GoogleMapMovedRpc;
import com.vaadin.tapio.googlemaps.client.GoogleMapPolygon;
import com.vaadin.tapio.googlemaps.client.GoogleMapPolyline;
import com.vaadin.tapio.googlemaps.client.GoogleMapState;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.events.MapMoveListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerDragListener;

/**
 * The class representing Google Maps.
 * 
 * @author Tapio Aali <tapio@vaadin.com>
 */
public class GoogleMap extends com.vaadin.ui.AbstractComponent {

    /**
     * Base map types supported by Google Maps.
     */
    public enum MapType {
        Hybrid, Roadmap, Satellite, Terrain
    }

    private GoogleMapMarkerClickedRpc markerClickedRpc = new GoogleMapMarkerClickedRpc() {
        @Override
        public void markerClicked(GoogleMapMarker marker) {
            for (MarkerClickListener listener : markerClickListeners) {
                listener.markerClicked(marker);
            }
        }
    };

    private GoogleMapMarkerDraggedRpc markerDraggedRpc = new GoogleMapMarkerDraggedRpc() {
        @Override
        public void markerDragged(GoogleMapMarker marker, LatLon newPosition) {
            for (MarkerDragListener listener : markerDragListeners) {
                listener.markerDragged(marker, newPosition);
            }
        }
    };

    private GoogleMapMovedRpc mapMovedRpc = new GoogleMapMovedRpc() {
        @Override
        public void mapMoved(double zoomLevel, LatLon center, LatLon boundsNE,
                LatLon boundsSW) {
            getState().locationFromClient = true;
            getState().zoom = zoomLevel;
            getState().center = center;

            for (MapMoveListener listener : mapMoveListeners) {
                listener.mapMoved(zoomLevel, center, boundsNE, boundsSW);
            }

        }
    };

    private List<MarkerClickListener> markerClickListeners = new ArrayList<MarkerClickListener>();

    private List<MapMoveListener> mapMoveListeners = new ArrayList<MapMoveListener>();

    private List<MarkerDragListener> markerDragListeners = new ArrayList<MarkerDragListener>();

    /**
     * Initiates a new GoogleMap object with default settings from the
     * {@link GoogleMapState state object}.
     */
    public GoogleMap(String apiKey) {
        getState().apiKey = apiKey;
        registerRpc(markerClickedRpc);
        registerRpc(mapMovedRpc);
        registerRpc(markerDraggedRpc);
    }

    /**
     * Creates a new GoogleMap object with the given center. Other settings will
     * be {@link GoogleMapState defaults of the state object}.
     * 
     * @param center
     *            Coordinates of the center.
     */
    public GoogleMap(LatLon center, String apiKey) {
        this(apiKey);
        getState().center = center;
    }

    /**
     * Creates a new GoogleMap object with the given center and zoom. Other
     * settings will be {@link GoogleMapState defaults of the state object}.
     * 
     * @param center
     *            Coordinates of the center.
     * @param zoom
     *            Amount of zoom.
     */
    public GoogleMap(LatLon center, double zoom, String apiKey) {
        this(apiKey);
        getState().zoom = zoom;
        getState().center = center;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.AbstractComponent#getState()
     */
    @Override
    protected GoogleMapState getState() {
        return (GoogleMapState) super.getState();
    }

    /**
     * Sets the center of the map to the given coordinates.
     * 
     * @param center
     *            The new coordinates of the center.
     */
    public void setCenter(LatLon center) {
        getState().locationFromClient = false;
        getState().center = center;
    }

    /**
     * Returns the current position of the center of the map.
     * 
     * @return Coordinates of the center.
     */
    public LatLon getCenter() {
        return getState().center;
    }

    /**
     * Zooms the map to the given value.
     * 
     * @param zoom
     *            New amount of the zoom.
     */
    public void setZoom(double zoom) {
        getState().locationFromClient = false;
        getState().zoom = zoom;
    }

    /**
     * Returns the current zoom of the map.
     * 
     * @return Current value of the zoom.
     */
    public double getZoom() {
        return getState().zoom;
    }

    /**
     * Adds a new marker to the map.
     * 
     * @param caption
     *            Caption of the marker shown when the marker is hovered.
     * @param position
     *            Coordinates of the marker on the map.
     * @param draggable
     *            Set true to enable dragging of the marker.
     * @return GoogleMapMarker object created with the given settings.
     */
    public GoogleMapMarker addMarker(String caption, LatLon position,
            boolean draggable) {
        GoogleMapMarker marker = new GoogleMapMarker(caption, position,
                draggable);
        getState().markers.add(marker);
        return marker;
    }

    /**
     * Adds a marker to the map.
     * 
     * @param marker
     *            The marker to add.
     */
    public void addMarker(GoogleMapMarker marker) {
        getState().markers.add(marker);
    }

    /**
     * Removes a marker from the map.
     * 
     * @param marker
     *            The marker to remove.
     */
    public void removeMarker(GoogleMapMarker marker) {
        getState().markers.remove(marker);
    }

    /**
     * Removes all the markers from the map.
     */
    public void clearMarkers() {
        getState().markers.clear();
    }

    /**
     * Adds a MarkerClickListener to the map.
     * 
     * @param listener
     *            the listener
     */
    public void addMarkerClickListener(MarkerClickListener listener) {
        markerClickListeners.add(listener);
    }

    /**
     * Removes a MarkerClickListener from the map.
     * 
     * @param listener
     *            the listener
     */
    public void removeMarkerClickListener(MarkerClickListener listener) {
        markerClickListeners.remove(listener);
    }

    /**
     * Adds a MarkerDragListener to the map.
     * 
     * @param listener
     *            the listener
     */
    public void addMarkerDragListener(MarkerDragListener listener) {
        markerDragListeners.add(listener);
    }

    /**
     * Removes a MarkerDragListenr from the map.
     * 
     * @param listener
     *            the listener
     */
    public void removeMarkerDragListener(MarkerDragListener listener) {
        markerDragListeners.remove(listener);
    }

    /**
     * Adds a MapMoveListener to the map.
     * 
     * @param listener
     *            the listener
     */
    public void addMapMoveListener(MapMoveListener listener) {
        mapMoveListeners.add(listener);
    }

    /**
     * Removes a MapMoveListener from the map.
     * 
     * @param listener
     *            the listener
     */
    public void removeMapMoveListener(MapMoveListener listener) {
        mapMoveListeners.remove(listener);
    }

    /**
     * Enables/disables limiting of the center bounds.
     * 
     * @param enable
     *            Set true to enable the limiting.
     */
    public void setCenterBoundLimitsEnabled(boolean enable) {
        getState().limitCenterBounds = enable;
    }

    /**
     * Sets the limits of the bounds of the center to given values.
     * 
     * @param limitNE
     *            The coordinates of the northeast limit.
     * @param limitSW
     *            The coordinates of the southwest limit.
     */
    public void setCenterBoundLimits(LatLon limitNE, LatLon limitSW) {
        getState().centerNELimit = limitNE;
        getState().centerSWLimit = limitSW;
    }

    /**
     * Adds a polygon overlay to the map.
     * 
     * @param polygon
     *            The GoogleMapPolygon to add.
     */
    public void addPolygonOverlay(GoogleMapPolygon polygon) {
        getState().polygons.add(polygon);
    }

    /**
     * Removes a polygon overlay from the map.
     * 
     * @param polygon
     *            The GoogleMapPolygon to remove.
     */
    public void removePolygonOverlay(GoogleMapPolygon polygon) {
        getState().polygons.remove(polygon);
    }

    /**
     * Adds a GoogleMapPolyline to the map.
     * 
     * @param polyline
     *            The GoogleMapPolyline to add.
     */
    public void addPolyline(GoogleMapPolyline polyline) {
        getState().polylines.add(polyline);
    }

    /**
     * Removes a GoogleMapPolyline from the map.
     * 
     * @param polyline
     *            The GoogleMapPolyline to add.
     */
    public void removePolyline(GoogleMapPolyline polyline) {
        getState().polylines.remove(polyline);
    }

    /**
     * Sets the type of the base map.
     * 
     * @param type
     *            The new MapType to use.
     */
    public void setMapType(MapType type) {
        getState().mapTypeId = type.name();
    }

    /**
     * Returns the current type of the base map.
     * 
     * @return The current MapType.
     */
    public MapType getMapType() {
        return MapType.valueOf(getState().mapTypeId);
    }

    /**
     * Checks if the map is currently draggable.
     * 
     * @return true, if the map draggable.
     */
    public boolean isDraggable() {
        return getState().draggable;
    }

    /**
     * Enables/disables dragging of the map.
     * 
     * @param draggable
     *            Set to true to enable dragging.
     */
    public void setDraggable(boolean draggable) {
        getState().draggable = draggable;
    }

    /**
     * Checks if the keyboard shortcuts are enabled.
     * 
     * @return true, if the shortcuts are enabled.
     */
    public boolean areKeyboardShortcutsEnabled() {
        return getState().keyboardShortcutsEnabled;
    }

    /**
     * Enables/disables the keyboard shortcuts.
     * 
     * @param enabled
     *            Set true to enable keyboard shortcuts.
     */
    public void setKeyboardShortcutsEnabled(boolean enabled) {
        getState().keyboardShortcutsEnabled = enabled;
    }

    /**
     * Checks if the scroll wheel is enabled.
     * 
     * @return true, if the scroll wheel is enabled
     */
    public boolean isScrollWheelEnabled() {
        return getState().scrollWheelEnabled;
    }

    /**
     * Enables/disables the scroll wheel.
     * 
     * @param enabled
     *            Set true to enable scroll wheel.
     */
    public void setScrollWheelEnabled(boolean enabled) {
        getState().scrollWheelEnabled = enabled;
    }

    /**
     * Returns the currently enabled map controls.
     * 
     * @return Currently enabled map controls.
     */
    public Set<GoogleMapControl> getControls() {
        return getState().controls;
    }

    /**
     * Sets the controls of the map.
     * 
     * @param controls
     *            The new controls to use.
     */
    public void setControls(Set<GoogleMapControl> controls) {
        getState().controls = controls;
    }

    /**
     * Enables the given control on the map. Does nothing if the control is
     * already enabled.
     * 
     * @param control
     *            The control to enable.
     */
    public void addControl(GoogleMapControl control) {
        getState().controls.add(control);
    }

    /**
     * Removes the control from the map. Does nothing if the control isn't
     * enabled.
     * 
     * @param control
     *            The control to remove.
     */
    public void removeControl(GoogleMapControl control) {
        getState().controls.remove(control);
    }

}