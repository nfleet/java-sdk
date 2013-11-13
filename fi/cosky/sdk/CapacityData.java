package fi.cosky.sdk;
/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class CapacityData extends BaseData {
    private String Name;
    private int Amount;

    public CapacityData(String name, int amount) {
        this.Name = name;
        this.Amount = amount;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public static CapacityData createCapacityDto() {
        return new CapacityData("Volume", 10);
    }
}
