package fi.cosky.sdk;
/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

 import java.util.List;

 public class ModificationAreaUpdateRequest extends BaseData {
        
    private CoordinateData TopLeft;
    private CoordinateData BottomRight;
    private String DataSource;	
    
    public CoordinateData getTopLeft(){
        return TopLeft;
    }

    public void setTopLeft(CoordinateData topLeft){
        TopLeft = topLeft;
    }

    public CoordinateData getBottomRight(){
        return BottomRight;
    }

    public void setBottomRight(CoordinateData bottomRight){
        BottomRight = bottomRight;
    }
    
    public String getDataSource(){
        return DataSource;
    }

    public void setDataSource(String dataSource){
        DataSource = dataSource;
    }
}