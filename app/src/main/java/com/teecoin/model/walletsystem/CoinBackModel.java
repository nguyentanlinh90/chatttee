package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.TeeCoinModel;

import java.io.Serializable;

public class CoinBackModel extends TeeCoinModel implements Serializable {

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

    @SerializedName("transaction_hash")
    @Expose
    private String transaction_hash; //this is used for sending to API

    @SerializedName("payment")
    @Expose
    private String payment; //payment is transactionId

    @SerializedName("xdr")
    @Expose
    private String xdr;

    @SerializedName("return_rate")
    @Expose
    private String return_rate;

    @SerializedName("invoiceId")
    @Expose
    private String invoiceId;

    private String totalAmount;
    private String paymentTransactionHash; //this is used for updateTo IsCoinBackStatus in TransactionDetailModel when coinback flow success


    public CoinBackModel(String source, String destination, String amount, String fee_amount, String paymentTransactionHash, String payment,String invoiceId,String totalAmount,String return_rate) {
        this.source = source;
        this.destination = destination;
        this.amount = amount;
        this.fee_amount = fee_amount;
        this.paymentTransactionHash = paymentTransactionHash;
        this.payment = payment;
        this.invoiceId = invoiceId;
        this.totalAmount = totalAmount;
        this.return_rate = return_rate;
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

    public String getTransaction_hash() {
        return transaction_hash;
    }

    public void setTransaction_hash(String transaction_hash) {
        this.transaction_hash = transaction_hash;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getPaymentTransactionHash() {
        return paymentTransactionHash;
    }

    public void setPaymentTransactionHash(String paymentTransactionHash) {
        this.paymentTransactionHash = paymentTransactionHash;
    }

    public String getReturn_rate() {
        return return_rate;
    }

    public void setReturn_rate(String return_rate) {
        this.return_rate = return_rate;
    }

    public String getXdr() {
        return xdr;
    }

    public void setXdr(String xdr) {
        this.xdr = xdr;
    }
}
