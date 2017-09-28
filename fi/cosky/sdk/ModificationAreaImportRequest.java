package fi.cosky.sdk;
/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

 import java.util.List;

 public class ModificationAreaImportRequest extends BaseData {
        
    private String ModificationName;
    private CoordinateData TopLeft;
    private CoordinateData BottomRight;
    private String DataSource;
    
	public String getModificationName() {
		return ModificationName;
	}

	public void setModificationName(String modificationName) {
		ModificationName = modificationName;
	}
    
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