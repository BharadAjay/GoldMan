package com.balaji.calculator.model;

public class PaymentDetailsModel {
    private double total;
    private double discount;
    private double tax;
    private double roundOff;
    private double subtotal;
    private double outstanding;
    private double credit;
    private double grandTotal;
    private double cashAmount;
    private double cardAmount;
    private double onlineAmount;
    private double chequeAmount;
    private double roundAmount;
    private double balance;
    private int billId;
    private String note;
    private String AccountNo;
    private String narration;
    private String clientBankName;
    private String companyBankName;
    private String code;

    public PaymentDetailsModel(double total, double discount, double tax, double roundOff, double subtotal, double outstanding, double credit, double grandTotal, double cashAmount,
                               double cardAmount, double onlineAmount, double chequeAmount, double roundAmount, double balance, int billId, String note, String AccountNo,
                               String narration, String clientBankName, String companyBankName, String code) {
        this.setTotal(total);
        this.setDiscount(discount);
        this.setTax(tax);
        this.setRoundOff(roundOff);
        this.setSubtotal(subtotal);
        this.setOutstanding(outstanding);
        this.setCredit(credit);
        this.setGrandTotal(grandTotal);
        this.setCashAmount(cashAmount);
        this.setCardAmount(cardAmount);
        this.setOnlineAmount(onlineAmount);
        this.setChequeAmount(chequeAmount);
        this.setRoundAmount(roundAmount);
        this.setBalance(balance);
        this.setBillId(billId);
        this.setNote(note);
        this.setAccountNo(AccountNo);
        this.setNarration(narration);
        this.setClientBankName(clientBankName);
        this.setCompanyBankName(companyBankName);
        this.setCode(code);
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getRoundOff() {
        return roundOff;
    }

    public void setRoundOff(double roundOff) {
        this.roundOff = roundOff;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(double outstanding) {
        this.outstanding = outstanding;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public double getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(double cashAmount) {
        this.cashAmount = cashAmount;
    }

    public double getCardAmount() {
        return cardAmount;
    }

    public void setCardAmount(double cardAmount) {
        this.cardAmount = cardAmount;
    }

    public double getOnlineAmount() {
        return onlineAmount;
    }

    public void setOnlineAmount(double onlineAmount) {
        this.onlineAmount = onlineAmount;
    }

    public double getChequeAmount() {
        return chequeAmount;
    }

    public void setChequeAmount(double chequeAmount) {
        this.chequeAmount = chequeAmount;
    }

    public double getRoundAmount() {
        return roundAmount;
    }

    public void setRoundAmount(double roundAmount) {
        this.roundAmount = roundAmount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public String getAccountNo() {
        return AccountNo;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setAccountNo(String accounNo) {
        AccountNo = accounNo;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getClientBankName() {
        return clientBankName;
    }

    public void setClientBankName(String clientBankName) {
        this.clientBankName = clientBankName;
    }

    public String getCompanyBankName() {
        return companyBankName;
    }

    public void setCompanyBankName(String companyBankName) {
        this.companyBankName = companyBankName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
