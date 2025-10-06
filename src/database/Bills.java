package database;

public class Bills {
    private final int electricity_bill;
    private final int internet_bill;
    private final int gas_bill;
    private final int water_bill;

    public Bills(int electricity_bill, int internet_bill, int gas_bill, int water_bill) {
        this.electricity_bill = electricity_bill;
        this.internet_bill = internet_bill;
        this.gas_bill = gas_bill;
        this.water_bill = water_bill;
    }

    public int getElectricity_bill() {
        return electricity_bill;
    }

    public int getInternet_bill() {
        return internet_bill;
    }

    public int getGas_bill() {
        return gas_bill;
    }

    public int getWater_bill() {
        return water_bill;
    }
}
