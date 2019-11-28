package com.teecoin.model.general;

import com.facebook.AccessToken;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.javastellarsdk.stellar.StellarProcess;
import com.teecoin.javastellarsdk.stellar.model.StellarAccount;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.SecretKeyEncryption;
import com.teecoin.utils.TCLog;
import com.teecoin.utils.TCUtils;

import org.json.JSONObject;

import java.io.Serializable;


public class SocialInfoModel implements Serializable {
    private static final String FB_ID = "id";
    private static final String FB_NAME = "name";
    private static final String FB_EMAIL = "email";
    private static final String ANDROID_OS = "aos";
    @SerializedName("provider")
    @Expose
    private String provider;
    @SerializedName("provider_id")
    @Expose
    private String provider_id;
    @SerializedName("access_token")
    @Expose
    private String access_token;
    @SerializedName("full_name")
    @Expose
    private String full_name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("country_code")
    @Expose
    private String country_code;
    @SerializedName("referral_code")
    @Expose
    private String referral_code;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("device_id")
    @Expose
    private String device_id;
    @SerializedName("public_key")
    @Expose
    private String public_key;
    @SerializedName("secret_key")
    @Expose
    private String secret_key;
    @SerializedName("os")
    @Expose
    private String os;
    @SerializedName("gender")
    @Expose
    private String gender;

    public SocialInfoModel() {
        this.os = ANDROID_OS;
    }

    public SocialInfoModel(JSONObject object, AccessToken token, String referral_code) {
        this.provider = EnumMgr.SocialProvider.Facebook.getValue();
        this.referral_code = referral_code;
        this.language = TCUtils.getLanguageCode();
        this.device_id = TCUtils.getUniquePseudoID();
        StellarAccount stellarAccount = StellarProcess.create();
        this.public_key = stellarAccount.getAccountId();
        this.secret_key = SecretKeyEncryption.encrypt(stellarAccount.getSecretSeed());
        this.os = ANDROID_OS;
        try {
            if (!object.isNull(FB_ID)) {
                this.provider_id = object.getString(FB_ID);
            }
            if (token != null) {
                this.access_token = String.valueOf(token.getToken());
            }

            if (!object.isNull(FB_NAME)) {
                this.full_name = object.getString(FB_NAME);
            }

            if (!object.isNull(FB_EMAIL)) {
                this.email = object.getString(FB_EMAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            TCLog.e("error  facebook " + e.getMessage());
        }
    }

    public SocialInfoModel(String provider, String provider_id, String access_token, String full_name, String email, String referral_code) {
        this.provider = provider;
        this.provider_id = provider_id;
        this.access_token = access_token;
        this.full_name = full_name;
        this.email = email;
        this.referral_code = referral_code;
        this.language = TCUtils.getLanguageCode();
        this.device_id = TCUtils.getUniquePseudoID();
        StellarAccount stellarAccount = StellarProcess.create();
        this.public_key = stellarAccount.getAccountId();
        this.secret_key = SecretKeyEncryption.encrypt(stellarAccount.getSecretSeed());
        this.os = ANDROID_OS;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getReferral_code() {
        return referral_code;
    }

    public void setReferral_code(String referral_code) {
        this.referral_code = referral_code;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

    public String getSecret_key() {
        return secret_key;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
