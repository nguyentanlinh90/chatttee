package com.teecoin.model.general;

import java.io.Serializable;

import io.realm.RealmObject;

public class TransactionConfigModel extends RealmObject implements Serializable {


    public static final double DEFAULT_COIN_EXCHANGE = 0.25; //1 TEE = 0.125 currency
    public static final double DEFAULT_COIN_BACK_REWARD_RATE = 0.01; //coinBack, reward: 1%
    public static final double DEFAULT_MIN_AMOUNT = 0; //min Amount
    public static final String DEFAULT_CODE = "USD";
    public static final String DEFAULT_SYMBOL = "$";
    public static final String PRIMARY_KEY = "publicKey";

    private String publicKey;

    private double coinExchange;

    private double coinBackRewardRate;

    private double minAmount;

    private double maxPaymentRate;

    private String code;

    private String symbol;


    public TransactionConfigModel() {
    }

    public TransactionConfigModel(double coinExchange, double coinBackRewardRate, double minAmount,
                                  double maxPaymentRate, String code, String symbol, String publicKey) {
        this.coinExchange = coinExchange;
        this.coinBackRewardRate = coinBackRewardRate;
        this.minAmount = minAmount;
        this.maxPaymentRate = maxPaymentRate;
        this.code = code != null ? code : DEFAULT_CODE;
        this.symbol = symbol != null ? symbol : DEFAULT_SYMBOL;
        this.publicKey = publicKey;
    }

    public TransactionConfigModel(TransactionConfigModel data) {

        this.publicKey = data.publicKey;
        this.coinExchange = data.coinExchange;
        this.coinBackRewardRate = data.coinBackRewardRate;
        this.minAmount = data.minAmount;
        this.maxPaymentRate = data.maxPaymentRate;
        this.code = data.code;
        this.symbol = data.getSymbol();
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public double getCoinExchange() {
        return coinExchange;
    }

    public void setCoinExchange(double coinExchange) {
        this.coinExchange = coinExchange;
    }

    public double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(double minAmount) {
        this.minAmount = minAmount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getCoinBackRewardRate() {
        return coinBackRewardRate;
    }

    public void setCoinBackRewardRate(double coinBackRewardRate) {
        this.coinBackRewardRate = coinBackRewardRate;
    }

    public void update(TransactionConfigModel transaction) {
        this.publicKey = transaction.getPublicKey();
        this.coinExchange = transaction.getCoinExchange();
        this.coinBackRewardRate = transaction.getCoinBackRewardRate();
        this.minAmount = transaction.getMinAmount();
        this.maxPaymentRate = transaction.getMaxPaymentRate();
        this.code = transaction.getCode();
        this.symbol = transaction.symbol;
    }

    public double getMaxPaymentRate() {
        return maxPaymentRate;
    }

    public void setMaxPaymentRate(double maxPaymentRate) {
        this.maxPaymentRate = maxPaymentRate;
    }

}

