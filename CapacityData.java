/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 21.3.2013
 * Time: 10:31
 * To change this template use File | Settings | File Templates.
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
