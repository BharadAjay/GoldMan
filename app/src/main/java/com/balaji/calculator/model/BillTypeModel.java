package com.balaji.calculator.model;

public class BillTypeModel {
    private String BillName;
    private boolean TaxType;
    private int BillId;

    public BillTypeModel(String billName, boolean taxType, int billId) {
        this.setBillName(billName);
        this.setTaxType(taxType);
        this.setBillId(billId);
    }

    public String getBillName() {
        return BillName;
    }

    public void setBillName(String billName) {
        BillName = billName;
    }

    public boolean getTaxType() {
        return TaxType;
    }

    public void setTaxType(boolean taxType) {
        TaxType = taxType;
    }

    public int getBillId() {
        return BillId;
    }

    public void setBillId(int billId) {
        BillId = billId;
    }
}
