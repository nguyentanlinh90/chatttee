package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.TeeCoinModel;
import com.teecoin.model.general.FeeConfigModel;
import com.teecoin.realmdb.RealmController;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.io.Serializable;
import java.util.Locale;

public class RewardModel extends TeeCoinModel implements Serializable {

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

    @SerializedName("return_rate")
    @Expose
    private String return_rate;

    @SerializedName("invoice")
    @Expose
    private String invoice;

    @SerializedName("invoice_amount")
    @Expose
    private String invoice_amount;

    @SerializedName("transaction_hash")
    @Expose
    private String transactionHash;

    @SerializedName("xdr")
    @Expose
    private String xdr;

    public RewardModel() {
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

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public String getReturn_rate() {
        return return_rate;
    }


    public void setReturn_rate(String return_rate) {
        this.return_rate = return_rate;
    }


    private transient double coinExchange = 0;//transient make variable not to  be serialized (not converted to json)
    private transient double totalCoinValue;
    private transient double rewardValueConvertToCurrentUnit;
    private transient double invoiceAmountConvertToTEC;
    private transient double walletBalance;

    public void calculate(String invoiceAmount) {
        this.invoice_amount = invoiceAmount;
        calculate(TCUtils.convertToDouble(invoiceAmount));
    }

    private void calculate(double invoiceAmount){
        double rewardValue = TCUtils.multiply(TCUtils.divide(invoiceAmount, coinExchange),
                TCUtils.convertReturnRateToDecimal(TCUtils.convertToDouble(return_rate)));
        rewardValue = TCUtils.roundCeil(rewardValue, TCConstant.ROUND_FIVE_DECIMAL);

        FeeConfigModel feeConfigModel = RealmController.getInstance().getData(FeeConfigModel.class);
        double rewardFeeValue = TCUtils.calculateTeeCoinFee(
                TCUtils.multiply(rewardValue,
                        TCUtils.convertReturnRateToDecimal(TCUtils.convertToDouble(feeConfigModel.getFeeRate()))),
                TCConstant.ROUND_SEVEN_DECIMAL);

        //set minimum of RewardFee is less than 0.01, set equal to 0.01
        if (rewardFeeValue < TCConstant.TEECOIN_FEE)
            rewardFeeValue = TCConstant.TEECOIN_FEE;

        this.rewardValueConvertToCurrentUnit = TCUtils.multiply(rewardValue, coinExchange);
        this.invoiceAmountConvertToTEC = TCUtils.divide(invoiceAmount, coinExchange);
        this.totalCoinValue = rewardValue + rewardFeeValue;

        this.amount = TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_FORMAT, rewardValue, Locale.US);
        this.fee_amount = TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, rewardFeeValue, Locale.US);
    }
    public void calculateMaxTeeCoin() {
        double invoiceAmount = getMaxInvoiceAmount();
        this.invoice_amount = TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_FORMAT,invoiceAmount);
        calculate(invoiceAmount);
    }

    private double getMaxInvoiceAmount() {
        FeeConfigModel feeConfigModel = RealmController.getInstance().getData(FeeConfigModel.class);
        double rewardValue = TCUtils.roundFloor(TCUtils.divide(this.walletBalance,
                (1 + TCUtils.convertReturnRateToDecimal(TCUtils.convertToDouble(feeConfigModel.getFeeRate())))),
                TCConstant.ROUND_FIVE_DECIMAL);
        double maxInvoiceAmount = TCUtils.roundFloor(
                TCUtils.divide(TCUtils.multiply(rewardValue, coinExchange),
                        TCUtils.convertReturnRateToDecimal(
                                TCUtils.convertToDouble(return_rate))),
                TCConstant.ROUND_THREE_DECIMAL);
        return maxInvoiceAmount;
    }
    public double getTotalCoinValue() {
        return totalCoinValue;
    }

    public void setTotalCoinValue(double totalCoinValue) {
        this.totalCoinValue = totalCoinValue;
    }

    public double getRewardValueConvertToCurrentUnit() {
        return rewardValueConvertToCurrentUnit;
    }

    public void setRewardValueConvertToCurrentUnit(double rewardValueConvertToCurrentUnit) {
        this.rewardValueConvertToCurrentUnit = rewardValueConvertToCurrentUnit;
    }

    public double getInvoiceAmountConvertToTEC() {
        return invoiceAmountConvertToTEC;
    }

    public void setInvoiceAmountConvertToTEC(double invoiceAmountConvertToTEC) {
        this.invoiceAmountConvertToTEC = invoiceAmountConvertToTEC;
    }

    public double getCoinExchange() {
        return coinExchange;
    }

    public void setCoinExchange(double coinExchange) {
        this.coinExchange = coinExchange;
    }

    public void setXdr(String xdr) {
        this.xdr = xdr;
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }
}
