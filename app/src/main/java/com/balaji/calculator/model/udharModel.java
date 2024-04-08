package com.balaji.calculator.model;

public class udharModel {

    private int id; // Assuming id is an integer field
    public int getId() {
        return id;
    }
    private String edtITNameUdhar,edtITCode,lsWt,otherAmt,ntWt,grWt,itName,itCode,accountTagNo;

    private String billEntryType,tagNo,tch,wst,totalTouch,weight,othWeight,finalWeight;

    private  String otherRate,amt,rate,ntAmt,pcs;


    public String getTagNo() {
        return tagNo;
    }
    public String getLsWt() {
        return lsWt;
    }

    public void setLsWt(String lsWt) {
        this.lsWt = lsWt;
    }

    public String getOtherAmt() {
        return otherAmt;
    }

    public void setOtherAmt(String otherAmt) {
        this.otherAmt = otherAmt;
    }

    public String getNtWt() {
        return ntWt;
    }

    public void setNtWt(String ntWt) {
        this.ntWt = ntWt;
    }

    public String getGrWt() {
        return grWt;
    }

    public void setGrWt(String grWt) {
        this.grWt = grWt;
    }

    public String getItName() {
        return itName;
    }

    public void setItName(String itName) {
        this.itName = itName;
    }

    public String getItCode() {
        return itCode;
    }

    public void setItCode(String itCode) {
        this.itCode = itCode;
    }

    public udharModel(String lsWt, String otherAmt, String ntWt, String grWt, String itName, String itCode) {
        this.lsWt = lsWt;
        this.otherAmt= otherAmt;
        this.ntWt = ntWt;
        this.grWt = grWt;
        this.itName = itName;
        this.itCode =itCode;
    }


    public udharModel(String tagNo,
                      String itCode,
                      String itName,
                      String grWt,
                      String lsWt,
                      String ntWt,
                      String tch,
                      String wst,
                      String totalTouch,
                      String weight,
                      String othWeight,
                      String finalWeight,
                      String otherAmt,
                      String otherRate,
                      String amt,
                      String rate,
                      String ntAmt,
                      String pcs) {
        this.tagNo = tagNo;
        this.itCode = itCode;
        this.itName = itName;
        this.grWt = grWt;
        this.lsWt = lsWt;
        this.ntWt = ntWt;
        this.tch = tch;
        this.wst = wst;
        this.totalTouch = totalTouch;
        this.weight = weight;
        this.othWeight = othWeight;
        this.finalWeight = finalWeight;
        this.otherAmt = otherAmt;
        this.otherRate = otherRate;
        this.amt = amt;
        this.rate = rate;
        this.ntAmt = ntAmt;
        this.pcs = pcs;
    }


    public udharModel(String itCode,
                      String itName,
                      String grWt,
                      String lsWt,
                      String ntWt,
                      String tch,
                      String wst,
                      String totalTouch,
                      String weight,
                      String othWeight,
                      String finalWeight,
                      String otherAmt,
                      String otherRate,
                      String amt,
                      String rate,
                      String ntAmt,
                      String pcs) {
        this.itCode = itCode;
        this.itName = itName;
        this.grWt = grWt;
        this.lsWt = lsWt;
        this.ntWt = ntWt;
        this.tch = tch;
        this.wst = wst;
        this.totalTouch = totalTouch;
        this.weight = weight;
        this.othWeight = othWeight;
        this.finalWeight = finalWeight;
        this.otherAmt = otherAmt;
        this.otherRate = otherRate;
        this.amt = amt;
        this.rate = rate;
        this.ntAmt = ntAmt;
        this.pcs = pcs;
    }



    public String getAccountTagNo() {
        return accountTagNo;
    }

    public void setAccountTagNo(String accountTagNo) {

        this.accountTagNo = accountTagNo;
    }

    public String getBillEntryType() {
        return billEntryType;
    }

    public void setBillEntryType(String billEntryType) {
        this.billEntryType = billEntryType;
    }

    public String getTch() {
        return tch;
    }

    public void setTch(String tch) {
        this.tch = tch;
    }

    public String getWst() {
        return wst;
    }

    public void setWst(String wst) {
        this.wst = wst;
    }

    public String getTotalTouch() {
        return totalTouch;
    }

    public void setTotalTouch(String totalTouch) {
        this.totalTouch = totalTouch;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getOthWeight() {
        return othWeight;
    }

    public void setOthWeight(String othWeight) {
        this.othWeight = othWeight;
    }

    public String getFinalWeight() {
        return finalWeight;
    }

    public void setFinalWeight(String finalWeight) {
        this.finalWeight = finalWeight;
    }

    public String getOtherRate() {
        return otherRate;
    }

    public void setOtherRate(String otherRate) {
        this.otherRate = otherRate;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getNtAmt() {
        return ntAmt;
    }

    public void setNtAmt(String ntAmt) {
        this.ntAmt = ntAmt;
    }

    public String getPcs() {
        return pcs;
    }

    public void setPcs(String pcs) {
        this.pcs = pcs;
    }


    public String getEdtITNameUdhar() {
        return edtITNameUdhar;
    }

    public void setEdtITNameUdhar(String edtITNameUdhar) {
        this.edtITNameUdhar = edtITNameUdhar;
    }

    public String getEdtITCode() {
        return edtITCode;
    }

    public void setEdtITCode(String edtITCode) {
        this.edtITCode = edtITCode;
    }

    public udharModel(String edtITNameUdhar, String edtITCode) {
        this.edtITNameUdhar = edtITNameUdhar;
        this.edtITCode = edtITCode;
    }


    public void setTagNo(String tagNo) {

        this.tagNo = tagNo;
    }
}
