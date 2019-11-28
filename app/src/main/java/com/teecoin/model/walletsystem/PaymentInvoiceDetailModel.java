package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.general.Vendor;

import java.io.Serializable;

public class PaymentInvoiceDetailModel implements Serializable {
    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("fee_amount")
    @Expose
    private String fee_amount;

    @SerializedName("basic_fee_amount")
    @Expose
    private String basic_fee_amount;

    @SerializedName("currency_code")
    @Expose
    private String currency_code;

    @SerializedName("invoice")
    @Expose
    private String invoice;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("coinback_amount")
    @Expose
    private String coinback_amount;

    @SerializedName("invoice_cash_amount")
    @Expose
    private String invoice_cash_amount;

    @SerializedName("paid_cash_amount")
    @Expose
    private String paid_cash_amount;

    @SerializedName("remain_cash_amount")
    @Expose
    private String remain_cash_amount;

    @SerializedName("paid_amount")
    @Expose
    private String paid_amount;

    @SerializedName("coinback_cash_amount")
    @Expose
    private String coinback_cash_amount;

    @SerializedName("payer")
    @Expose
    private String payer;

    @SerializedName("paid_amount_destination")
    @Expose
    private String paid_amount_destination;

    @SerializedName("fee_amount_destination")
    @Expose
    private String fee_amount_destination;

    @SerializedName("basic_fee_amount_destination")
    @Expose
    private String basic_fee_amount_destination;

    @SerializedName("rate")
    @Expose
    private String rate;

    @SerializedName("source")
    @Expose
    private String source;

    @SerializedName("customer")
    @Expose
    private String customer;


    @SerializedName("vendor")
    @Expose
    private Vendor vendor;

    private String transactionHash;

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
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

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCoinback_amount() {
        return coinback_amount;
    }

    public void setCoinback_amount(String coinback_amount) {
        this.coinback_amount = coinback_amount;
    }

    public String getInvoice_cash_amount() {
        return invoice_cash_amount;
    }

    public void setInvoice_cash_amount(String invoice_cash_amount) {
        this.invoice_cash_amount = invoice_cash_amount;
    }

    public String getPaid_cash_amount() {
        return paid_cash_amount;
    }

    public void setPaid_cash_amount(String paid_cash_amount) {
        this.paid_cash_amount = paid_cash_amount;
    }

    public String getRemain_cash_amount() {
        return remain_cash_amount;
    }

    public void setRemain_cash_amount(String remain_cash_amount) {
        this.remain_cash_amount = remain_cash_amount;
    }

    public String getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(String paid_amount) {
        this.paid_amount = paid_amount;
    }

    public String getCoinback_cash_amount() {
        return coinback_cash_amount;
    }

    public void setCoinback_cash_amount(String coinback_cash_amount) {
        this.coinback_cash_amount = coinback_cash_amount;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public String getBasic_fee_amount() {
        return basic_fee_amount;
    }

    public void setBasic_fee_amount(String basic_fee_amount) {
        this.basic_fee_amount = basic_fee_amount;
    }

    public String getPaid_amount_destination() {
        return paid_amount_destination;
    }

    public void setPaid_amount_destination(String paid_amount_destination) {
        this.paid_amount_destination = paid_amount_destination;
    }

    public String getFee_amount_destination() {
        return fee_amount_destination;
    }

    public void setFee_amount_destination(String fee_amount_destination) {
        this.fee_amount_destination = fee_amount_destination;
    }

    public String getBasic_fee_amount_destination() {
        return basic_fee_amount_destination;
    }

    public void setBasic_fee_amount_destination(String basic_fee_amount_destination) {
        this.basic_fee_amount_destination = basic_fee_amount_destination;
    }

    public String getRate() {
        return rate;
    }

    public String getSource() {
        return source;
    }

    public String getCustomer() {
        return customer;
    }
}
