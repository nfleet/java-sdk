/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 26.3.2013
 * Time: 15:26
 *
 * LocationData only accepts coordinates at the moment, will update SDK when this will be changed.
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
