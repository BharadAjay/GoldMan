package com.balaji.calculator.model;

import java.util.Date;

public class ItemLedger {
    private String AccountNo;
    private String AccountName;
    private double OpningAmt;
    private double CrAmt;
    private double DrAmt;
    private double OpnigFine;
    private int OpeningSilver;
    private String Moblie;
    private String City;
    private String Address;
    private String RefBy;
    private String Pancard;
    private String DrivingLicence;
    private String ElectionCard;
    private String Aadhar_Card;
    private String GstTinNo;
    private String AcGpName;
    //    private Date DOB;
//    private Date Anniv;
    private boolean isNew;

    public ItemLedger(String AccountName, String AccountNo, String AcGpName, double OpningAmt, double OpnigFine, int OpeningSilver, String Moblie, String City) {
        this.setAccountName(AccountName);
        this.setAccountNo(AccountNo);
        this.setAcGpName(AcGpName);
        this.setOpningAmt(OpningAmt);
        this.setOpnigFine(OpnigFine);
        this.setOpeningSilver(OpeningSilver);
        this.setMoblie(Moblie);
        this.setCity(City);
    }

    public ItemLedger(String accountNo, String accountName, double opningAmt, double CrAmt, double DrAmt, double opnigFine, int OpeningSilver, String moblie, String city, String address, String refBy, String pancard, String drivingLicence, String electionCard, String aadhar_Card, String gstTinNo, boolean isNew) {
        this.setAccountNo(accountNo);
        this.setAccountName(accountName);
        this.setOpningAmt(opningAmt);
        this.setCrAmt(CrAmt);
        this.setDrAmt(DrAmt);
        this.setOpnigFine(opnigFine);
        this.setOpeningSilver(OpeningSilver);
        this.setMoblie(moblie);
        this.setCity(city);
        this.setAddress(address);
        this.setRefBy(refBy);
        this.setPancard(pancard);
        this.setDrivingLicence(drivingLicence);
        this.setElectionCard(electionCard);
        this.setAadhar_Card(aadhar_Card);
        this.setGstTinNo(gstTinNo);
        this.setNew(isNew);
    }

    public ItemLedger() {

    }

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        this.AccountName = accountName;
    }

    public String getAccountNo() {
        return AccountNo;
    }

    public void setAccountNo(String accountNo) {
        AccountNo = accountNo;
    }

    public String getAcGpName() {
        return AcGpName;
    }

    public void setAcGpName(String acGpName) {
        AcGpName = acGpName;
    }

    public double getOpningAmt() {
        return OpningAmt;
    }

    public void setOpningAmt(double opningAmt) {
        this.OpningAmt = opningAmt;
    }

    public double getCrAmt() {
        return CrAmt;
    }

    public void setCrAmt(double crAmt) {
        CrAmt = crAmt;
    }

    public double getDrAmt() {
        return DrAmt;
    }

    public void setDrAmt(double drAmt) {
        DrAmt = drAmt;
    }

    public double getOpnigFine() {
        return OpnigFine;
    }

    public void setOpnigFine(double opnigFine) {
        this.OpnigFine = opnigFine;
    }

    public int getOpeningSilver() {
        return OpeningSilver;
    }

    public void setOpeningSilver(int openingSilver) {
        this.OpeningSilver = openingSilver;
    }

    public String getMoblie() {
        return Moblie;
    }

    public void setMoblie(String Mobile) {
        this.Moblie = Mobile;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        this.City = city;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getRefBy() {
        return RefBy;
    }

    public void setRefBy(String refBy) {
        RefBy = refBy;
    }

    public String getPancard() {
        return Pancard;
    }

    public void setPancard(String pancard) {
        Pancard = pancard;
    }

    public String getDrivingLicence() {
        return DrivingLicence;
    }

    public void setDrivingLicence(String drivingLicence) {
        DrivingLicence = drivingLicence;
    }

    public String getElectionCard() {
        return ElectionCard;
    }

    public void setElectionCard(String electionCard) {
        ElectionCard = electionCard;
    }

    public String getAadhar_Card() {
        return Aadhar_Card;
    }

    public void setAadhar_Card(String aadhar_Card) {
        Aadhar_Card = aadhar_Card;
    }

    public String getGstTinNo() {
        return GstTinNo;
    }

    public void setGstTinNo(String gstTinNo) {
        GstTinNo = gstTinNo;
    }

   /* public Date getDOB() {
        return DOB;
    }

    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }

    public Date getAnniv() {
        return Anniv;
    }

    public void setAnniv(Date anniv) {
        Anniv = anniv;
    }*/

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }
}
