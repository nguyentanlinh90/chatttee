package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CoinBackResultModel implements Serializable {
    @SerializedName("created")
    @Expose
    private String created;
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
    @SerializedName("currency_code")
    @Expose
    private String currencyCode;
    @SerializedName("invoice")
    @Expose
    private String invoice;
    @SerializedName("invoice_amount")
    @Expose
    private String invoiceAmount;
    @SerializedName("payment")
    @Expose
    private String payment;
    @SerializedName("payment_amount")
    @Expose
    private String paymentAmount;
    @SerializedName("return_rate")
    @Expose
    private String returnRate;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("paging_token")
    @Expose
    private String pagingToken;
    @SerializedName("shop_name")
    @Expose
    private String shopName;

    public CoinBackResultModel() {
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

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getReturnRate() {
        return returnRate;
    }

    public void setReturnRate(String returnRate) {
        this.returnRate = returnRate;
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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
