package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.TeeCoinModel;

public class PaymentModel extends TeeCoinModel {

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
    private String fee_amount;

    @SerializedName("rate")
    @Expose
    private String rate;

    @SerializedName("invoice")
    @Expose
    private String invoice;

    @SerializedName("invoice_amount")
    @Expose
    private String invoice_amount;

    @SerializedName("remain_amount")
    @Expose
    private String remain_amount;

    @SerializedName("transaction_hash")
    @Expose
    private String transactionHash;

    @SerializedName("xdr")
    @Expose
    private String xdr;

    @SerializedName("return_rate")
    @Expose
    private String return_rate;

    public PaymentModel(String source, String destination, String amount, String fee_amount, String rate, String invoice, String invoice_amount, String remain_amount, String transactionHash,String xdr,String return_rate) {
        this.source = source;
        this.destination = destination;
        this.amount = amount;
        this.fee_amount = fee_amount;
        this.rate = rate;
        this.invoice = invoice;
        this.invoice_amount = invoice_amount;
        this.remain_amount = remain_amount;
        this.transactionHash = transactionHash;
        this.xdr = xdr;
        this.return_rate = return_rate;
    }

    public PaymentModel(String source, String invoice, String xdr, String transactionHash) {
        this.source = source;
        this.invoice = invoice;
        this.xdr = xdr;
        this.transactionHash = transactionHash;


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

    public String getFee_amount() {
        return fee_amount;
    }

    public void setFee_amount(String fee_amount) {
        this.fee_amount = fee_amount;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getInvoice_amount() {
        return invoice_amount;
    }

    public void setInvoice_amount(String invoice_amount) {
        this.invoice_amount = invoice_amount;
    }

    public String getRemain_amount() {
        return remain_amount;
    }

    public void setRemain_amount(String remain_amount) {
        this.remain_amount = remain_amount;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public String getXdr() {
        return xdr;
    }

    public void setXdr(String xdr) {
        this.xdr = xdr;
    }
}
