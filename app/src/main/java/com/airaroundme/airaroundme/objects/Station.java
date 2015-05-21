package com.airaroundme.airaroundme.objects;

/**
 * Created by borax12 on 22/05/15.
 */
public class Station {
    private int stationId;
    private Double latitude;
    private Double longitude;
    private float distanceFromCurr;

    public Station(int stationId, Double latitude, Double longitude) {
        this.stationId = stationId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public float getDistanceFromCurr() {
        return distanceFromCurr;
    }

    public void setDistanceFromCurr(float distanceFromCurr) {
        this.distanceFromCurr = distanceFromCurr;
    }
}
