package com.teecoin.javastellarsdk.stellar.model;

import java.io.Serializable;

public class StellarPayment implements Serializable {

    private String amount;
    private String fromAccountId;
    private String assetCode;
    private String issuer;
    private String transactionHash;
    private String lastToken;

    public StellarPayment() {

    }

    public StellarPayment(String fromAccountId, String amount, String transactionHash) {
        this.amount = amount;
        this.fromAccountId = fromAccountId;
        this.transactionHash = transactionHash;
    }

    public StellarPayment(String amount, String fromAccountId, String transactionHash, String lastToken) {
        this.amount = amount;
        this.fromAccountId = fromAccountId;
        this.transactionHash = transactionHash;
        this.lastToken = lastToken;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public String getLastToken() {
        return lastToken;
    }

    public void setLastToken(String lastToken) {
        this.lastToken = lastToken;
    }
}
