package edu.gatech.cc.lostandfound.mobile.googlemaps;

import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.gatech.cc.lostandfound.mobile.entity.Position;

/**
 * Created by guoweidong on 11/1/15.
 */
public class DraggableCircle {
    public static final double RADIUS_OF_EARTH_METERS = 6371009;
    private static final int FILL_COLOR = Color.argb(122, 255, 0, 0);
    private static final int STORKE_COLOR = Color.BLACK;
    private static final int STROKE_WIDTH = 0;
    private static final double DEFAULT_RADIUS = 50;
    private final Marker centerMarker;

    private final Marker radiusMarker;

    private final Circle circle;

    private String address;

    private double radius;
    private GoogleMap mMap;

    public DraggableCircle(GoogleMap mMap, LatLng center, double radius, String address) {
        this.mMap = mMap;
        this.radius = radius;
        centerMarker = mMap.addMarker(new MarkerOptions()
                .position(center)
                .title("Delete")
                .draggable(true));
        radiusMarker = mMap.addMarker(new MarkerOptions()
                .position(toRadiusLatLng(center, radius))
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_AZURE)));
        circle = mMap.addCircle(new CircleOptions()
                .center(center)
                .radius(radius)
                .strokeWidth(STROKE_WIDTH)
                .strokeColor(STORKE_COLOR)
                .fillColor(FILL_COLOR));
        this.address = address;
    }

    public DraggableCircle(GoogleMap mMap, LatLng center, LatLng radiusLatLng, String address) {
        this.mMap = mMap;
        this.radius = toRadiusMeters(center, radiusLatLng);
        centerMarker = mMap.addMarker(new MarkerOptions()
                .position(center)
                .title("Delete")
                .draggable(true));
        radiusMarker = mMap.addMarker(new MarkerOptions()
                .position(radiusLatLng)
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_AZURE)));
        circle = mMap.addCircle(new CircleOptions()
                .center(center)
                .radius(radius)
                .strokeWidth(STROKE_WIDTH)
                .strokeColor(STORKE_COLOR)
                .fillColor(FILL_COLOR));
        this.address = address;
    }

    public DraggableCircle(GoogleMap mMap, LatLng center, String address) {
        this(mMap, center, DEFAULT_RADIUS, address);
    }

    public static LatLng toRadiusLatLng(LatLng center, double radius) {
        double radiusAngle = Math.toDegrees(radius / RADIUS_OF_EARTH_METERS) /
                Math.cos(Math.toRadians(center.latitude));
        return new LatLng(center.latitude, center.longitude + radiusAngle);
    }

    public static double toRadiusMeters(LatLng center, LatLng radius) {
        float[] result = new float[1];
        Location.distanceBetween(center.latitude, center.longitude,
                radius.latitude, radius.longitude, result);
        return result[0];
    }

    public boolean onCenterMarkerMoved(Marker marker) {
        if (marker.equals(centerMarker)) {
            circle.setCenter(marker.getPosition());
            radiusMarker.setPosition(toRadiusLatLng(marker.getPosition(), radius));
            return true;
        }
        return false;
    }

    public boolean onRadiusMarkerMoved(Marker marker) {
        if (marker.equals(radiusMarker)) {
            radius = toRadiusMeters(centerMarker.getPosition(), radiusMarker.getPosition());
            circle.setRadius(radius);
            return true;
        }
        return false;
    }

    public boolean remove(Marker marker) {
        if (marker.equals(centerMarker) || marker.equals(radiusMarker)) {
            circle.remove();
            centerMarker.remove();
            radiusMarker.remove();
            return true;
        }
        return false;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Address: " + address + "\n" +
                "Latitude:" + centerMarker.getPosition().latitude + "\n" +
                "Longitude:" + centerMarker.getPosition().longitude + "\n" +
                "Radius:" + radius;
    }

    public void getPosition(Position pos) {
        pos.address = address;
        pos.lat = (float) centerMarker.getPosition().latitude;
        pos.lng = (float) centerMarker.getPosition().longitude;
    }
}
