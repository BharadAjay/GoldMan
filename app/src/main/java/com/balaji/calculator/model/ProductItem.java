package com.balaji.calculator.model;

import com.google.gson.annotations.SerializedName;

public class ProductItem {

    @SerializedName("productRateMain")
    private double productRateMain;

    @SerializedName("isRate")
    private boolean isRate;

    @SerializedName("TouchON")
    private String touchON;

    @SerializedName("isPcs")
    private boolean isPcs;

    @SerializedName("productShortName")
    private String productShortName;

    @SerializedName("productName")
    private String productName;

    @SerializedName("productRateDiff")
    private double productRateDiff;

    @SerializedName("primaryproductShortName")
    private String primaryproductShortName;

    @SerializedName("isShowASOther")
    private boolean isShowASOther;

    @SerializedName("productgroupShortName")
    private String productgroupShortName;

    @SerializedName("primaryproduvtName")
    private String primaryproduvtName;

    @SerializedName("productRate")
    private double productRate;

    @SerializedName("primaryproductId")
    private String primaryproductId;

    @SerializedName("productgroupName")
    private String productgroupName;

    @SerializedName("isWeight")
    private boolean isWeight;

    @SerializedName("LabourON")
    private String labourON;

    @SerializedName("isOther")
    private boolean isOther;

    @SerializedName("RateCalculateOn")
    private String rateCalculateOn;

    @SerializedName("Count")
    private int Count;

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public ProductItem(String productName) {
        this.productName = productName;
    }

    public double getProductRateMain() {
        return productRateMain;
    }

    public boolean isIsRate() {
        return isRate;
    }

    public String getTouchON() {
        return touchON;
    }

    public boolean isIsPcs() {
        return isPcs;
    }

    public String getProductShortName() {
        return productShortName;
    }

    public String getProductName() {
        return productName;
    }

    public double getProductRateDiff() {
        return productRateDiff;
    }

    public String getPrimaryproductShortName() {
        return primaryproductShortName;
    }

    public boolean isIsShowASOther() {
        return isShowASOther;
    }

    public String getProductgroupShortName() {
        return productgroupShortName;
    }

    public String getPrimaryproduvtName() {
        return primaryproduvtName;
    }

    public double getProductRate() {
        return productRate;
    }

    public String getPrimaryproductId() {
        return primaryproductId;
    }

    public String getProductgroupName() {
        return productgroupName;
    }

    public boolean isIsWeight() {
        return isWeight;
    }

    public String getLabourON() {
        return labourON;
    }

    public boolean isIsOther() {
        return isOther;
    }

    public String getRateCalculateOn() {
        return rateCalculateOn;
    }
}