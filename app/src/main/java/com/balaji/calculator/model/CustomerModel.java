package com.balaji.calculator.model;

public class CustomerModel {
    private String customerName;

    public CustomerModel(String customerName) {
        this.setCustomerName(customerName);
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
