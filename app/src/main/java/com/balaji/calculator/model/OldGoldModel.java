package com.balaji.calculator.model;

public class OldGoldModel {
    String itemName;
    double grossWeight;
    double rate;
    double amount;

    public OldGoldModel(String itemName, double grossWeight, double rate, double amount) {
        this.itemName = itemName;
        this.grossWeight = grossWeight;
        this.rate = rate;
        this.amount = amount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(double grossWeight) {
        this.grossWeight = grossWeight;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
