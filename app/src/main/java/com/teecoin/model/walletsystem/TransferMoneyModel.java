package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.TeeCoinModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.realmdb.RealmController;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.io.Serializable;

public class TransferMoneyModel extends TeeCoinModel implements Serializable {

    @SerializedName("destination")
    @Expose
    private String destination;

    @SerializedName("source")
    @Expose
    private String source;

    @SerializedName("note")
    @Expose
    private String note;

    @SerializedName("amount")
    @Expose
    private double amount;

    @SerializedName("rate")
    @Expose
    private double exchangeRate;

    @SerializedName("fee_amount")
    @Expose
    private double teeCoinFee;

    @SerializedName("transaction_hash")
    @Expose
    private String transaction_hash;

    @SerializedName("xdr")
    @Expose
    private String xdr;

    @SerializedName("transaction_id")
    @Expose
    private String transactionId;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("paging_token")
    @Expose
    private String paging_token;

    @SerializedName("sender")
    @Expose
    private String sender;

    @SerializedName("receiver")
    @Expose
    private String receiver;

    @SerializedName("currency_code")
    @Expose
    private String currency_code;

    private transient String first_name;
    private transient String last_name;
    private transient String full_name;
    private transient String avatar;
    private transient double paymentAmountConverted;
    private transient String exchangeCurrency;
    private transient double walletBalance;
    private transient double totalTeeCoin;

    public TransferMoneyModel() {

    }

    public TransferMoneyModel(MyQRCodeModel myQRCodeModel) {
        this.destination = myQRCodeModel.getPublic_key();
        this.avatar = myQRCodeModel.getAvatar();
        this.first_name = myQRCodeModel.getFirst_name();
        this.last_name = myQRCodeModel.getLast_name();
        this.full_name = myQRCodeModel.getFull_name();
        AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);
        if (accountModel != null)
            this.exchangeRate = accountModel.getRate();
        this.exchangeCurrency = TCConstant.EXCHANGE_CODE_USD;
    }

    public TransferMoneyModel(QRCodeInfoModel qrCodeInfoModel) {
        this.destination = qrCodeInfoModel.getPublicKey();
        this.avatar = qrCodeInfoModel.getAvatar();
        this.first_name = qrCodeInfoModel.getFirstName();
        this.last_name = qrCodeInfoModel.getLastName();
        this.full_name = qrCodeInfoModel.getFull_name();
        AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);
        if (accountModel != null)
            this.exchangeRate = accountModel.getRate();
        this.exchangeCurrency = TCConstant.EXCHANGE_CODE_USD;
    }

    public void calculate(double payment) {
        if (payment <= TCConstant.ZERO)
            payment = TCConstant.ZERO;
        this.amount = payment;
        paymentAmountConverted = TCUtils.roundFloor(this.amount * exchangeRate, TCConstant.ROUND_SEVEN_DECIMAL);
        teeCoinFee = TCConstant.TEECOIN_FEE; //currently fix the user's fee is 0.01 TEC
        this.totalTeeCoin = payment + teeCoinFee;
    }

    public void calculateMaxTeeCoin() {
        this.amount = TCUtils.roundFloor(TCUtils.subtract(this.walletBalance, TCConstant.TEECOIN_FEE_RATE), TCConstant.ROUND_FIVE_DECIMAL);
        paymentAmountConverted = TCUtils.roundFloor(this.amount * this.exchangeRate, TCConstant.ROUND_SEVEN_DECIMAL);
        teeCoinFee = TCConstant.TEECOIN_FEE; //currently fix the user's fee is 0.01 TEC
        totalTeeCoin = TCUtils.roundFloor(amount + teeCoinFee, TCConstant.ROUND_SEVEN_DECIMAL);
    }

    public void setaaa(QRCodeInfoModel qrCodeInfoModel) {
        this.destination = qrCodeInfoModel.getPublicKey();
        this.avatar = qrCodeInfoModel.getAvatar();
        this.first_name = qrCodeInfoModel.getFirstName();
        this.last_name = qrCodeInfoModel.getLastName();
    }

    public void calculateTotalTeeCoin() {
        this.totalTeeCoin = this.amount + this.teeCoinFee;
    }

    public String getDestination() {
        return destination;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public double getPaymentAmountConverted() {
        return paymentAmountConverted;
    }

    public String getExchangeCurrency() {
        return exchangeCurrency;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getAmount() {
        return amount;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public double getTeeCoinFee() {
        return teeCoinFee;
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public double getTotalTeeCoin() {
        return totalTeeCoin;
    }

    public void setTransaction_hash(String transaction_hash) {
        this.transaction_hash = transaction_hash;
    }

    public void setXdr(String xdr) {
        this.xdr = xdr;
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

    public String getCreated() {
        return created;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public void setExchangeCurrency(String exchangeCurrency) {
        this.exchangeCurrency = exchangeCurrency;
    }

    public String getFull_name() {
        return full_name;
    }
}
