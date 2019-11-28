package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.utils.TCUtils;

import java.io.Serializable;

public class PaymentDetailModel implements Serializable {
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
    @SerializedName("remain_amount")
    @Expose
    private String remainAmount;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("paging_token")
    @Expose
    private String pagingToken;
    @SerializedName("is_coinback_success")
    @Expose
    private Boolean isCoinbackSuccess;
    @SerializedName("coinback_amount")
    @Expose
    private String coinbackAmount;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("shop_name")
    @Expose
    private String shopName;

    @SerializedName("return_rate")
    @Expose
    private String return_rate;

    public PaymentDetailModel() {
    }

    public PaymentDetailModel(String transactionId) {
        this.transactionId = transactionId;
    }

    public PaymentDetailModel(PaymentDetailModel paymentDetailModel) {
        this.created = paymentDetailModel.created;
        this.transactionHash = paymentDetailModel.transactionHash;
        this.transactionId = paymentDetailModel.transactionId;
        this.source = paymentDetailModel.source;
        this.destination = paymentDetailModel.destination;
        this.amount = paymentDetailModel.amount;
        this.feeAmount = paymentDetailModel.feeAmount;
        this.rate = paymentDetailModel.rate;
        this.currencyCode = paymentDetailModel.currencyCode;
        this.invoice = paymentDetailModel.invoice;
        this.invoiceAmount = paymentDetailModel.invoiceAmount;
        this.remainAmount = paymentDetailModel.remainAmount;
        this.status = paymentDetailModel.status;
        this.pagingToken = paymentDetailModel.pagingToken;
        this.isCoinbackSuccess = paymentDetailModel.isCoinbackSuccess;
        this.coinbackAmount = paymentDetailModel.coinbackAmount;
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

    public String getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(String remainAmount) {
        this.remainAmount = remainAmount;
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

    public Boolean getCoinbackSuccess() {
        return isCoinbackSuccess;
    }

    public void setCoinbackSuccess(Boolean coinbackSuccess) {
        isCoinbackSuccess = coinbackSuccess;
    }

    public String getCoinbackAmount() {
        return coinbackAmount;
    }

    public void setCoinbackAmount(String coinbackAmount) {
        this.coinbackAmount = coinbackAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getReturn_rate() {
        return return_rate;
    }

    public void setReturn_rate(String return_rate) {
        this.return_rate = return_rate;
    }

    public double getTotalAmount(boolean isAppUser) {
        double amount = TCUtils.convertToDouble(this.amount);
        double fee_amount = TCUtils.convertToDouble(this.feeAmount);
        return isAppUser ? amount + fee_amount : amount;
    }
}
