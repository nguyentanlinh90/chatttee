package com.teecoin.model.general;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.walletsystem.CreateAccountModel;
import com.teecoin.utils.SecretKeyEncryption;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.io.Serializable;

import io.realm.RealmObject;

public class AccountModel extends RealmObject implements Serializable {

    public static final String PRIMARY_KEY = "public_key";
    public static final String _ID = "_id";
    public static final String LANGUAGE = "language";
    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("uuid")
    @Expose
    private String uuid;

    @SerializedName("joined_date")
    @Expose
    private String joined_date;

    @SerializedName("public_key")
    @Expose
    private String public_key;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("full_name")
    @Expose
    private String full_name;

    @SerializedName("first_name")
    @Expose
    private String first_name;

    @SerializedName("last_name")
    @Expose
    private String last_name;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("notes")
    @Expose
    private String notes;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("confirm_password")
    @Expose
    private String confirm_password;

    // use for login
    @SerializedName("return_rate")
    @Expose
    private double return_rate;
    @SerializedName("rate")
    @Expose
    private double rate;
    @SerializedName("currency_code")
    @Expose
    private String currency_code;
    @SerializedName("min_amount")
    @Expose
    private double min_amount;
    @SerializedName("currency_symbol")
    @Expose
    private String currency_symbol;
    @SerializedName("device_id")
    @Expose
    private String deviceId;

    @SerializedName("device_token")
    @Expose
    private String device_token;

    @SerializedName("referral_code")
    @Expose
    private String referralCode;

    @SerializedName("language")
    @Expose
    private String language;

    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("country_code")
    @Expose
    private String country_code;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("dob")
    @Expose
    private String dob;

    @SerializedName("provider")
    @Expose
    private String provider;

    @SerializedName("provider_id")
    @Expose
    private String provider_id;

    @SerializedName("secret_key")
    @Expose
    private String secret_key;

    @SerializedName("have_password")
    @Expose
    private boolean have_password;


    @SerializedName("vendor")// for shop app
    @Expose
    private Vendor vendor;


    //---
    private String balance;
    private boolean isLogin;

    public AccountModel() {
    }

    public AccountModel(AccountModel clone) {
        if (clone == null)
            clone = new AccountModel();

        this._id = clone.get_id();
        this.url = clone.getUrl();
        this.token = clone.getToken();
        this.uuid = clone.getUuid();
        this.joined_date = clone.getJoined_date();
        this.public_key = clone.getPublic_key();
        this.email = clone.getEmail();
        this.phone = clone.getPhone();
        this.country_code = clone.getCountry_code();
        this.first_name = clone.getFirst_name();
        this.last_name = clone.getLast_name();
        this.full_name = clone.getFull_name();
        this.gender = clone.getGender();
        this.dob = clone.getDob();
        this.name = clone.getName();
        this.notes = clone.getNotes();
        this.avatar = clone.getAvatar();
        this.secret_key = clone.getSecret_key();
        this.password = clone.getPassword();
        this.rate = clone.getRate();
        this.min_amount = clone.getMin_amount();
        this.return_rate = clone.getReturn_rate();
        this.currency_code = clone.getCurrency_code();
        this.currency_symbol = clone.getCurrency_symbol();
        this.confirm_password = clone.getConfirm_password();
        this.balance = clone.getBalance();
        this.isLogin = clone.isLogin();
        this.device_token = clone.getDevice_token();
        this.provider = TCUtils.isEmpty(clone.getProvider()) ? "" : clone.getProvider();
        this.provider_id = clone.getProvider_id();
        this.have_password = clone.isHave_password();
//        this.vendor = clone.getVendor();

    }


    public AccountModel(String public_key) {
        this.public_key = public_key;
    }

    public AccountModel(String public_key, String password, String secret_key) {
        this.public_key = public_key;
        this.password = password;
        this.deviceId = TCUtils.getUniquePseudoID();
        this.language = TCUtils.getLanguageCode();
        this.secret_key = SecretKeyEncryption.encrypt(secret_key);
        //  this.secret_key = stellarAccount.getSecretSeed();
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getJoined_date() {
        return joined_date;
    }

    public void setJoined_date(String joined_date) {
        this.joined_date = joined_date;
    }

//    public String getPublicKey() {
//        return public_key;
//    }
//
//    public void setPublicKey(String public_key) {
//        this.public_key = public_key;
//    }


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecret_key() {
        return secret_key;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

    public void setTokenAndUUID(CreateAccountModel accountResponseModel) {
        this.token = accountResponseModel.getToken();
        this.uuid = accountResponseModel.getUuid();
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public Double getReturn_rate() {
        return return_rate;
    }

    public void setReturn_rate(Double return_rate) {
        this.return_rate = return_rate;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public Double getMin_amount() {
        return min_amount;
    }

    public void setMin_amount(Double min_amount) {
        this.min_amount = min_amount;
    }

    public String getCurrency_symbol() {
        return currency_symbol;
    }

    public void setCurrency_symbol(String currency_symbol) {
        this.currency_symbol = currency_symbol;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
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

    public boolean isHave_password() {
        return have_password;
    }

    public void setHave_password(boolean have_password) {
        this.have_password = have_password;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public void update(AccountModel accountModel) {
        this._id = accountModel.get_id();
        this.url = accountModel.getUrl();
        this.token = accountModel.getToken();
        this.uuid = accountModel.getUuid();
        this.joined_date = accountModel.getJoined_date();
        this.public_key = accountModel.getPublic_key();
        this.email = accountModel.getEmail();
        this.phone = accountModel.getPhone();
        this.country_code = accountModel.getCountry_code();
        this.full_name = accountModel.getFull_name();
        this.first_name = accountModel.getFirst_name();
        this.last_name = accountModel.getLast_name();
        this.gender = accountModel.getGender();
        this.dob = accountModel.getDob();
        this.notes = accountModel.getNotes();
        this.avatar = accountModel.getAvatar();
        this.secret_key = accountModel.getSecret_key();
        this.language = accountModel.getLanguage();
        this.password = accountModel.getPassword();
        this.rate = accountModel.getRate();
        this.min_amount = accountModel.getMin_amount();
        this.return_rate = accountModel.getReturn_rate();
        this.currency_code = accountModel.getCurrency_code();
        this.currency_symbol = accountModel.getCurrency_symbol();
        this.confirm_password = accountModel.getConfirm_password();
        this.balance = accountModel.getBalance();
        this.isLogin = accountModel.isLogin();
        this.device_token = accountModel.getDevice_token();
        this.provider = TCUtils.isEmpty(accountModel.getProvider()) ? "" : accountModel.getProvider();
        this.provider_id = accountModel.getProvider_id();
        this.have_password = accountModel.isHave_password();
//        this.vendor = accountModel.getVendor();

    }

    public void updateProfile(ProfileModel profileModel) {
        if (!TextUtils.isEmpty(profileModel.getAvatar()))
            this.avatar = profileModel.getAvatar();
        this.first_name = profileModel.getFirst_name();
        this.last_name = profileModel.getLast_name();
        this.language = profileModel.getLanguage();
        this.full_name = profileModel.getFull_name();
        this.gender = profileModel.getGender();
        this.dob = profileModel.getDob();
    }


    public void calculateBalance(double amount) {
        this.balance = TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_NO_COMMAS_FORMAT, TCUtils.convertToDouble(this.balance) + amount);
    }

}
