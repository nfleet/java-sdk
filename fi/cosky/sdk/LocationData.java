package fi.cosky.sdk;
/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

/**
 *
 * LocationData only accepts coordinates at the moment, will update SDK when this will be changed.
 * 
 * - this will be changed when address correction is implemented in service.
 */
public class LocationData extends BaseData {

    private CoordinateData Coordinate;
    private int Id;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public CoordinateData getCoordinatesData() {
        return Coordinate;
    }

    public void setCoordinatesData(CoordinateData coordinatesData) {
        Coordinate = coordinatesData;
    }


}
