package fi.cosky.sdk;
/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class CoordinateData {
    private double Latitude;
    private double Longitude;
    private CoordinateSystem System;

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public CoordinateSystem getSystem() {
        return System;
    }

    public void setSystem(CoordinateSystem system) {
        System = system;
    }

    public enum CoordinateSystem{WGS84, Euclidian};
}
