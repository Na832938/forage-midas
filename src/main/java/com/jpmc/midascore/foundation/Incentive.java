package com.jpmc.midascore.foundation;

public class Incentive {

    private float amount;

    public Incentive() {}

    public float getAmount() {
        return this.amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return (String.format(String.valueOf(amount)));
    }


}
