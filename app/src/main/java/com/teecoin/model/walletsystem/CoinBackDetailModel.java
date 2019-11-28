package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.TeeCoinModel;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.io.Serializable;

public class CoinBackDetailModel extends TeeCoinModel implements Serializable {
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
    private String payment_amount;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("paging_token")
    @Expose
    private String pagingToken;
    @SerializedName("return_rate")
    @Expose
    private String return_rate;
    @SerializedName("remain_amount")
    @Expose
    private String remainAmount;
    @SerializedName("rate")
    @Expose
    private String rate;


    public CoinBackDetailModel(String transactionId) {
        this.transactionId = transactionId;
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

    public String getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(String payment_amount) {
        this.payment_amount = payment_amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getReturnRate() {
        return return_rate;
    }

    public void setReturnRate(String returnRate) {
        this.return_rate = returnRate;
    }

    /**
     * Coin Back is based on Remain: %Coin Back x (Remain / Exchange Rate)
     *
     * @param returnRate
     * @param remainAmount
     * @param exchangeRate
     * @return
     */
    public static double calculateCoinBack(double returnRate, double remainAmount, double exchangeRate) {
        return TCUtils.roundCeil(
                TCUtils.multiply(returnRate, TCUtils.divide(remainAmount, exchangeRate)),
                TCConstant.ROUND_SEVEN_DECIMAL);
    }

    public String getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(String remainAmount) {
        this.remainAmount = remainAmount;
    }

    public String getReturn_rate() {
        return return_rate;
    }

    public void setReturn_rate(String return_rate) {
        this.return_rate = return_rate;
    }

    public String getRate() {
        return rate;
    }

    public double getTotalCoinBack(boolean isAppUser) {
        double amount = TCUtils.convertToDouble(this.amount);
        double fee_amount = TCUtils.convertToDouble(this.feeAmount);
        return isAppUser ? amount : amount + fee_amount;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}