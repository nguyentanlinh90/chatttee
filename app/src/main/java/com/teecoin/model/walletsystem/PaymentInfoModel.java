package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.utils.TCUtils;

import java.io.Serializable;

public class PaymentInfoModel implements Serializable {

    @SerializedName("cash_amount")
    @Expose
    private String cashAmount;
    @SerializedName("monthly_shop_limit_amount")
    @Expose
    private String monthlyShopLimitAmount;
    @SerializedName("min_balance")
    @Expose
    private String minBalance;
    @SerializedName("withdrawal_processing_fee")
    @Expose
    private String withdrawalProcessingFee;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("coinback_percentage")
    @Expose
    private String coinbackPercentage;
    @SerializedName("rate")
    @Expose
    private String rate = "1";
    @SerializedName("fee")
    @Expose
    private String fee;
    @SerializedName("basic_fee")
    @Expose
    private String basicFee;
    @SerializedName("spread")
    @Expose
    private String spread;
    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("interest_amount")
    @Expose
    private String interest_amount;

    private String total = "0";
    private String amountWallet = "0";
    private String minAmountWallet = "0";
    private String inputAmountCash = "0";
    private String coinBack = "0";
    private String convertToTec = "0";
    private String feeCalculator = "0";

    public double getCashAmount() {
        return TCUtils.convertToDouble(cashAmount);
    }

    public double getMonthlyShopLimitAmount() {
        return Math.abs(TCUtils.convertToDouble(monthlyShopLimitAmount));
    }

    public String getCurrency() {
        return currency;
    }

    public double getCoinbackPercentage() {
        return TCUtils.convertToDouble(coinbackPercentage);
    }

    public double getRate() {
        return TCUtils.convertToDouble(rate);
    }

    public String getFee() {
        return fee;
    }

    public String getBasicFee() {
        return basicFee;
    }

    public double getSpread() {
        return TCUtils.convertToDouble(spread);
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getAmountWallet() {
        return amountWallet;
    }

    public void setAmountWallet(String amountWallet) {
        this.amountWallet = amountWallet;
    }

    public String getMinAmountWallet() {
        return minAmountWallet;
    }

    public void setMinAmountWallet(String minAmountWallet) {
        this.minAmountWallet = minAmountWallet;
    }

    public String getInputAmountCash() {
        return inputAmountCash;
    }

    public void setInputAmountCash(String inputAmountCash) {
        this.inputAmountCash = inputAmountCash;
    }

    public String getCoinBack() {
        return coinBack;
    }

    public void setCoinBack(String coinBack) {
        this.coinBack = coinBack;
    }

    public String getConvertToTec() {
        return convertToTec;
    }

    public void setConvertToTec(String convertToTec) {
        this.convertToTec = convertToTec;
    }

    public String getMinBalance() {
        return minBalance;
    }

    public String getWithdrawalProcessingFee() {
        return withdrawalProcessingFee;
    }

    public String getType() {
        return type;
    }

    public String getFeeCalculator() {
        return feeCalculator;
    }

    public void setFeeCalculator(String feeCalculator) {
        this.feeCalculator = feeCalculator;
    }

    public String getInterest_amount() {
        return interest_amount;
    }
}