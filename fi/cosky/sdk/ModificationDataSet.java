package fi.cosky.sdk;
/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

 import java.util.List;

 public class ModificationDataSet extends BaseData {
    
    public static final String MimeType =  "application/vnd.jyu.nfleet.modificationset";
	public static final double MimeVersion = 2.0;

    private List<ModificationData> Items;

    public List<ModificationData> getItems(){
        return Items;
    }

    public void setItems(List<ModificationData> items){
        Items = items;
    }
 }