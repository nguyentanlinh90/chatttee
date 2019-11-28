package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.TeeCoinModel;
import com.teecoin.utils.TCUtils;

import java.io.Serializable;

import io.realm.annotations.PrimaryKey;

public class RewardResultModel extends TeeCoinModel implements Serializable {
    @SerializedName("created")
    @Expose
    private String created;
    @PrimaryKey  //Realm Primary Key
    @SerializedName("transaction_hash")
    @Expose
    private String transactionHash;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("fee_amount")
    @Expose
    private String feeAmount;
    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("currency_code")
    @Expose
    private String currencyCode;
    @SerializedName("invoice")
    @Expose
    private String invoice;
    @SerializedName("invoice_amount")
    @Expose
    private String invoiceAmount;
    @SerializedName("return_rate")
    @Expose
    private String return_rate;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("paging_token")
    @Expose
    private String pagingToken;
    @SerializedName("shop_name")
    @Expose
    private String shop_name;

    public RewardResultModel(String transaction_id) {
        this.transactionId = transaction_id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(String feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getReturn_rate() {
        return return_rate;
    }

    public void setReturn_rate(String return_rate) {
        this.return_rate = return_rate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPagingToken() {
        return pagingToken;
    }

    public void setPagingToken(String pagingToken) {
        this.pagingToken = pagingToken;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public double getTotalReward(boolean isAppUser){
        double reward = TCUtils.convertToDouble(this.amount);
        double feeReward = TCUtils.convertToDouble(this.feeAmount);
        return isAppUser ? reward : reward+feeReward;
    }
}
