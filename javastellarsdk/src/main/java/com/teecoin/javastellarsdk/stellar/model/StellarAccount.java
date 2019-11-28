package com.teecoin.javastellarsdk.stellar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.stellar.sdk.KeyPair;

import java.io.Serializable;

public class StellarAccount implements Serializable {

    @SerializedName("accountId")
    @Expose
    private String accountId;

    @SerializedName("secretSeed")
    @Expose
    private String secretSeed;

    public StellarAccount(KeyPair key) {
        this.accountId = key.getAccountId();
        this.secretSeed = new String(key.getSecretSeed());
    }

    public StellarAccount(String accountId) {
        this.accountId = accountId;
    }

    public StellarAccount(String accountId, String secretSeed) {
        this.accountId = accountId;
        this.secretSeed = secretSeed;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getSecretSeed() {
        return secretSeed;
    }
}
