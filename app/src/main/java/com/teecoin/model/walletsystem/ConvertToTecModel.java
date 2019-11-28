package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ConvertToTecModel implements Serializable {

    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("fee_amount")
    @Expose
    private String feeAmount;
    @SerializedName("basic_fee_amount")
    @Expose
    private String basicFeeAmount;
    @SerializedName("currency_code")
    @Expose
    private String currencyCode;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("sender")
    @Expose
    private String sender;
    @SerializedName("convert_cash_amount")
    @Expose
    private String convertCashAmount;
    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("receivable_amount")
    @Expose
    private String receivable_amount;



    public String getCreated() {
        return created;
    }

    public String getAmount() {
        return amount;
    }

    public String getFeeAmount() {
        return feeAmount;
    }

    public String getBasicFeeAmount() {
        return basicFeeAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getStatus() {
        return status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getSender() {
        return sender;
    }

    public String getConvertCashAmount() {
        return convertCashAmount;
    }

    public String getRate() {
        return rate;
    }

    public String getReceivable_amount() {
        return receivable_amount;
    }
}