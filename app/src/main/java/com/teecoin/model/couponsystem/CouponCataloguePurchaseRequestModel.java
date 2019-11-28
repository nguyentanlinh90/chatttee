package com.teecoin.model.couponsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CouponCataloguePurchaseRequestModel {
    @SerializedName("catalogue_id")
    @Expose
    private String catalogue_id;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("amount")
    @Expose
    private double amount;
    @SerializedName("xdr")
    @Expose
    private String xdr;
    @SerializedName("transaction_hash")
    @Expose
    private String transaction_hash;

    @SerializedName("fee_amount")
    @Expose
    private double fee_amount;
    @SerializedName("fee_amount_destination")
    @Expose
    private String fee_amount_destination;
    @SerializedName("basic_fee_amount_destination")
    @Expose
    private String basic_fee_amount_destination;
    @SerializedName("basic_fee_amount")
    @Expose
    private double basic_fee_amount;

    public CouponCataloguePurchaseRequestModel(String catalogue_id, String source,
                                               String destination, double amount,
                                               String fee_amount_destination, double fee_amount,
                                               String basic_fee_amount_destination, double basic_fee_amount) {
        this.catalogue_id = catalogue_id;
        this.source = source;
        this.destination = destination;
        this.amount = amount;
        this.fee_amount_destination = fee_amount_destination;
        this.fee_amount = fee_amount;
        this.basic_fee_amount_destination = basic_fee_amount_destination;
        this.basic_fee_amount = basic_fee_amount;

    }

    public String getCatalogue_id() {
        return catalogue_id;
    }

    public void setCatalogue_id(String catalogue_id) {
        this.catalogue_id = catalogue_id;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getFee_amount() {
        return fee_amount;
    }

    public void setFee_amount(double fee_amount) {
        this.fee_amount = fee_amount;
    }

    public String getFee_amount_destination() {
        return fee_amount_destination;
    }

    public String getBasic_fee_amount_destination() {
        return basic_fee_amount_destination;
    }

    public double getBasic_fee_amount() {
        return basic_fee_amount;
    }

    public String getXdr() {
        return xdr;
    }

    public void setXdr(String xdr) {
        this.xdr = xdr;
    }

    public String getTransaction_hash() {
        return transaction_hash;
    }

    public void setTransaction_hash(String transaction_hash) {
        this.transaction_hash = transaction_hash;
    }
}
