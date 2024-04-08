package com.balaji.calculator.model;

public class ItemLedgerTransaction {
    private String Book;
    private String VchNo;
    private String VchDate;
    private double CrAmt;
    private double DrAmt;
    private double CrFine;
    private double DrFine;
    private double CrSilver;
    private double DrSilver;

    public ItemLedgerTransaction(String Book, String VchNo, String VchDate, double CrAmt, double DrAmt, double CrFine, double DrFine, double CrSilver, double DrSilver) {
        this.setBook(Book);
        this.setVchNo(VchNo);
        this.setVchDate(VchDate);
        this.setCrAmt(CrAmt);
        this.setDrAmt(DrAmt);
        this.setCrFine(CrFine);
        this.setDrFine(DrFine);
        this.setCrSilver(CrSilver);
        this.setDrSilver(DrSilver);
    }

    public String getBook() {
        return Book;
    }

    public void setBook(String book) {
        this.Book = book;
    }

    public String getVchNo() {
        return VchNo;
    }

    public void setVchNo(String vchNo) {
        this.VchNo = vchNo;
    }

    public String getVchDate() {
        return VchDate;
    }

    public void setVchDate(String vchDate) {
        VchDate = vchDate;
    }

    public double getCrAmt() {
        return CrAmt;
    }

    public void setCrAmt(double crAmt) {
        this.CrAmt = crAmt;
    }

    public double getDrAmt() {
        return DrAmt;
    }

    public void setDrAmt(double drAmt) {
        this.DrAmt = drAmt;
    }

    public double getCrFine() {
        return CrFine;
    }

    public void setCrFine(double crFine) {
        this.CrFine = crFine;
    }

    public double getDrFine() {
        return DrFine;
    }

    public void setDrFine(double drFine) {
        this.DrFine = drFine;
    }

    public double getCrSilver() {
        return CrSilver;
    }

    public void setCrSilver(double crSilver) {
        this.CrSilver = crSilver;
    }

    public double getDrSilver() {
        return DrSilver;
    }

    public void setDrSilver(double drSilver) {
        this.DrSilver = drSilver;
    }
}