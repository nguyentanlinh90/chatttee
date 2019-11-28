package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UserCoinBackDetailModel extends PaymentInvoiceDetailModel {

    @SerializedName("transaction_hash")
    @Expose
    private String transaction_hash;

    @SerializedName("transaction_id")
    @Expose
    private String transaction_id;

    @SerializedName("destination")
    @Expose
    private String destination;

    public String getTransaction_hash() {
        return transaction_hash;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public String getDestination() {
        return destination;
    }
}

