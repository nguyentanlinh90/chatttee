package com.teecoin.feature.reviewSystem.tip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

public class TipModel {

    @SerializedName("destination")
    @Expose
    private String destination;

    @SerializedName("source")
    @Expose
    private String source;

    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("fee_amount")
    @Expose
    private String fee_amount;

    @SerializedName("comment_id")
    @Expose
    private String comment_id;

    @SerializedName("transaction_hash")
    @Expose
    private String transaction_hash;

    @SerializedName("xdr")
    @Expose
    private String xdr;

    private String exchangeCode;
    private double exchangeRate;

    public TipModel() {
    }

    public TipModel(String destination, String comment_id) {
        this.destination = destination;
        this.comment_id = comment_id;
    }

    public TipModel(String destination) {
        this.destination = destination;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFee_amount() {
        return fee_amount;
    }

    public void setFee_amount(String fee_amount) {
        this.fee_amount = fee_amount;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getTransaction_hash() {
        return transaction_hash;
    }

    public void setTransaction_hash(String transaction_hash) {
        this.transaction_hash = transaction_hash;
    }

    public String getXdr() {
        return xdr;
    }

    public void setXdr(String xdr) {
        this.xdr = xdr;
    }

    public String getTotalAmount() {
        if (!TCUtils.isEmpty(this.amount)) {
            double amount = TCUtils.convertToDouble(this.amount);
            double feeAmount = TCUtils.convertToDouble(this.fee_amount);
            double totalAmount = amount + feeAmount;
            return TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, totalAmount);
        } else {
            return "0";
        }
    }

    public String getExchangeCode() {
        return exchangeCode;
    }

    public void setExchangeCode(String exchangeCode) {
        this.exchangeCode = exchangeCode;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
