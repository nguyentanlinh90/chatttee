package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WithdrawModel implements Serializable {

    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("cash_amount")
    @Expose
    private String cashAmount;
    @SerializedName("currency_code")
    @Expose
    private String currencyCode;
    @SerializedName("withdrawal_processing_fee")
    @Expose
    private String withdrawalProcessingFee;

    @SerializedName("invoice_url")
    @Expose
    private String invoice_url;

    @SerializedName("sender")
    @Expose
    private String sender;

    @SerializedName("fiat_balance")
    @Expose
    private String fiat_balance;

    @SerializedName("interest_amount")
    @Expose
    private String interest_amount;


    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(String cashAmount) {
        this.cashAmount = cashAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getWithdrawalProcessingFee() {
        return withdrawalProcessingFee;
    }

    public void setWithdrawalProcessingFee(String withdrawalProcessingFee) {
        this.withdrawalProcessingFee = withdrawalProcessingFee;
    }

    public String getInvoice_url() {
        return invoice_url;
    }

    public String getSender() {
        return sender;
    }

    public String getFiat_balance() {
        return fiat_balance;
    }

    public String getInterest_amount() {
        return interest_amount;
    }
}