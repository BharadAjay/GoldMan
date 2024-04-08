package com.balaji.calculator.model;

import com.google.gson.annotations.SerializedName;

public class TagResponse {

    @SerializedName("isReturn")
    private boolean isReturn;

    @SerializedName("Size")
    private String size;

    @SerializedName("BranchID")
    private int branchID;

    @SerializedName("colorDiamondWit")
    private double colorDiamondWit;

    @SerializedName("GhatWet")
    private double ghatWet;

    @SerializedName("PrintCounter")
    private int printCounter;

    @SerializedName("TagNo")
    private String tagNo;

    @SerializedName("GrCode")
    private String grCode;

    @SerializedName("RFIDNo")
    private String rFIDNo;

    @SerializedName("IsStord")
    private boolean isStord;

    @SerializedName("TagImage")
    private String tagImage;

    @SerializedName("DiamondWit")
    private double diamondWit;

    @SerializedName("LbrRate")
    private double lbrRate;

    @SerializedName("Ac_In")
    private String acIn;

    @SerializedName("Psc")
    private double psc;

    @SerializedName("colorData")
    private int colorData;

    @SerializedName("Touch_Out")
    private double touchOut;

    @SerializedName("currentInstock")
    private boolean currentInstock;

    @SerializedName("OtherAmt")
    private double otherAmt;

    @SerializedName("TotalTouch_Out")
    private double totalTouchOut;

    @SerializedName("VchNo")
    private String vchNo;

    @SerializedName("CstMRP")
    private double cstMRP;

    @SerializedName("SalesNtWt")
    private double salesNtWt;

    @SerializedName("Ac_Out")
    private String acOut;

    @SerializedName("DiamondQuality")
    private String diamondQuality;

    @SerializedName("IsStock")
    private boolean isStock;

    @SerializedName("VarietyName")
    private String varietyName;

    @SerializedName("PrCode")
    private String prCode;

    @SerializedName("priproCode")
    private String priproCode;

    @SerializedName("GrWet")
    private double grWet;

    @SerializedName("LbrAmt")
    private double lbrAmt;

    @SerializedName("HUID")
    private String hUID;

    @SerializedName("MRP")
    private double mRP;

    @SerializedName("ItCode")
    private String itCode;

    @SerializedName("Touch")
    private double touch;

    @SerializedName("LabourOn")
    private String LabourOn;

    @SerializedName("LessWet")
    private double lessWet;

    @SerializedName("BoxID")
    private int boxID;

    @SerializedName("BillEntryVch")
    private String billEntryVch;

    @SerializedName("BoxName")
    private String boxName;

    @SerializedName("NetAmt")
    private double netAmt;

    @SerializedName("wst_Out")
    private double wstOut;

    @SerializedName("VaeietyID")
    private int vaeietyID;

    @SerializedName("isCancel")
    private boolean isCancel;

    @SerializedName("West")
    private double west;

    @SerializedName("DiamondClearty")
    private String diamondClearty;

    @SerializedName("CstOthAmt")
    private double cstOthAmt;

    @SerializedName("PackingWeight")
    private double packingWeight;

    @SerializedName("ItName")
    private String itName;

    @SerializedName("EntryType")
    private String entryType;

    @SerializedName("LbrType")
    private String lbrType;

    @SerializedName("NetWet")
    private double netWet;

    @SerializedName("User_Id")
    private String userId;

    @SerializedName("TagSrNo")
    private int tagSrNo;

    @SerializedName("Desing")
    private String desing;

    @SerializedName("Rate")
    private double Rate;

    public String getRateOn() {
        return RateOn;
    }

    public void setRateOn(String rateOn) {
        RateOn = rateOn;
    }

    @SerializedName("RateOn")
    private String RateOn;

    @SerializedName("LabourValueCal")
    private String LabourValueCal;

    public double getRate() {
        return Rate;
    }

    public String getLabourValuCal() {
        return LabourValueCal;
    }

    public String getLabourOn() {
        return LabourOn;
    }

    public void setLabourOn(String labourOn) {
        LabourOn = labourOn;
    }

    public boolean isIsReturn() {
        return isReturn;
    }

    public String getSize() {
        return size;
    }

    public int getBranchID() {
        return branchID;
    }

    public double getColorDiamondWit() {
        return colorDiamondWit;
    }

    public double getGhatWet() {
        return ghatWet;
    }

    public int getPrintCounter() {
        return printCounter;
    }

    public String getTagNo() {
        return tagNo;
    }

    public String getGrCode() {
        return grCode;
    }

    public String getRFIDNo() {
        return rFIDNo;
    }

    public boolean isIsStord() {
        return isStord;
    }

    public String getTagImage() {
        return tagImage;
    }

    public double getDiamondWit() {
        return diamondWit;
    }

    public double getLbrRate() {
        return lbrRate;
    }

    public String getAcIn() {
        return acIn;
    }

    public double getPsc() {
        return psc;
    }

    public int getColorData() {
        return colorData;
    }

    public double getTouchOut() {
        return touchOut;
    }

    public boolean isCurrentInstock() {
        return currentInstock;
    }

    public double getOtherAmt() {
        return otherAmt;
    }

    public double getTotalTouchOut() {
        return totalTouchOut;
    }

    public String getVchNo() {
        return vchNo;
    }

    public double getCstMRP() {
        return cstMRP;
    }

    public double getSalesNtWt() {
        return salesNtWt;
    }

    public String getAcOut() {
        return acOut;
    }

    public String getDiamondQuality() {
        return diamondQuality;
    }

    public boolean isIsStock() {
        return isStock;
    }

    public String getVarietyName() {
        return varietyName;
    }

    public String getPrCode() {
        return prCode;
    }

    public String getPriproCode() {
        return priproCode;
    }

    public double getGrWet() {
        return grWet;
    }

    public double getLbrAmt() {
        return lbrAmt;
    }

    public String getHUID() {
        return hUID;
    }

    public double getMRP() {
        return mRP;
    }

    public String getItCode() {
        return itCode;
    }

    public double getTouch() {
        return touch;
    }

    public double getLessWet() {
        return lessWet;
    }

    public int getBoxID() {
        return boxID;
    }

    public String getBillEntryVch() {
        return billEntryVch;
    }

    public String getBoxName() {
        return boxName;
    }

    public double getNetAmt() {
        return netAmt;
    }

    public double getWstOut() {
        return wstOut;
    }

    public int getVaeietyID() {
        return vaeietyID;
    }

    public boolean isIsCancel() {
        return isCancel;
    }

    public double getWest() {
        return west;
    }

    public String getDiamondClearty() {
        return diamondClearty;
    }

    public double getCstOthAmt() {
        return cstOthAmt;
    }

    public double getPackingWeight() {
        return packingWeight;
    }

    public String getItName() {
        return itName;
    }

    public String getEntryType() {
        return entryType;
    }

    public String getLbrType() {
        return lbrType;
    }

    public double getNetWet() {
        return netWet;
    }

    public String getUserId() {
        return userId;
    }

    public int getTagSrNo() {
        return tagSrNo;
    }

    public String getDesing() {
        return desing;
    }

    @Override
    public String toString() {
        return
                "TagResponse{" +
                        "isReturn = '" + isReturn + '\'' +
                        ",size = '" + size + '\'' +
                        ",branchID = '" + branchID + '\'' +
                        ",colorDiamondWit = '" + colorDiamondWit + '\'' +
                        ",ghatWet = '" + ghatWet + '\'' +
                        ",printCounter = '" + printCounter + '\'' +
                        ",tagNo = '" + tagNo + '\'' +
                        ",grCode = '" + grCode + '\'' +
                        ",rFIDNo = '" + rFIDNo + '\'' +
                        ",isStord = '" + isStord + '\'' +
                        ",tagImage = '" + tagImage + '\'' +
                        ",diamondWit = '" + diamondWit + '\'' +
                        ",lbrRate = '" + lbrRate + '\'' +
                        ",ac_In = '" + acIn + '\'' +
                        ",psc = '" + psc + '\'' +
                        ",colorData = '" + colorData + '\'' +
                        ",touch_Out = '" + touchOut + '\'' +
                        ",currentInstock = '" + currentInstock + '\'' +
                        ",otherAmt = '" + otherAmt + '\'' +
                        ",totalTouch_Out = '" + totalTouchOut + '\'' +
                        ",vchNo = '" + vchNo + '\'' +
                        ",cstMRP = '" + cstMRP + '\'' +
                        ",salesNtWt = '" + salesNtWt + '\'' +
                        ",ac_Out = '" + acOut + '\'' +
                        ",diamondQuality = '" + diamondQuality + '\'' +
                        ",isStock = '" + isStock + '\'' +
                        ",varietyName = '" + varietyName + '\'' +
                        ",prCode = '" + prCode + '\'' +
                        ",priproCode = '" + priproCode + '\'' +
                        ",grWet = '" + grWet + '\'' +
                        ",lbrAmt = '" + lbrAmt + '\'' +
                        ",hUID = '" + hUID + '\'' +
                        ",mRP = '" + mRP + '\'' +
                        ",itCode = '" + itCode + '\'' +
                        ",touch = '" + touch + '\'' +
                        ",lessWet = '" + lessWet + '\'' +
                        ",boxID = '" + boxID + '\'' +
                        ",billEntryVch = '" + billEntryVch + '\'' +
                        ",boxName = '" + boxName + '\'' +
                        ",netAmt = '" + netAmt + '\'' +
                        ",wst_Out = '" + wstOut + '\'' +
                        ",vaeietyID = '" + vaeietyID + '\'' +
                        ",isCancel = '" + isCancel + '\'' +
                        ",west = '" + west + '\'' +
                        ",diamondClearty = '" + diamondClearty + '\'' +
                        ",cstOthAmt = '" + cstOthAmt + '\'' +
                        ",packingWeight = '" + packingWeight + '\'' +
                        ",itName = '" + itName + '\'' +
                        ",entryType = '" + entryType + '\'' +
                        ",lbrType = '" + lbrType + '\'' +
                        ",netWet = '" + netWet + '\'' +
                        ",user_Id = '" + userId + '\'' +
                        ",tagSrNo = '" + tagSrNo + '\'' +
                        ",desing = '" + desing + '\'' +
                        "}";
    }
}