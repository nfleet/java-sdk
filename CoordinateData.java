/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 17.4.2013
 * Time: 12:42
 * To change this template use File | Settings | File Templates.
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

    enum CoordinateSystem{WGS84};
}
