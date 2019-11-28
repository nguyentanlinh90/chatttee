package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.general.TransactionConfigModel;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;


public class ShopInvoiceModel implements Serializable {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("ex_tee")
    @Expose
    private double ex_tee;

    @SerializedName("public_key")
    @Expose
    private String public_key;

    @SerializedName("_id")
    @Expose
    private String _id;

    @SerializedName("ex_sym")
    @Expose
    private String ex_sym;

    @SerializedName("return_rate")
    @Expose
    private double return_rate;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("max_payment_rate")
    @Expose
    private double max_payment_rate;

    public ShopInvoiceModel() {

    }

    public ShopInvoiceModel(AccountModel accountModel, TransactionConfigModel transactionConfigModel) {
        this.name = accountModel.getName();
        this.ex_tee = transactionConfigModel.getCoinExchange();
        this.public_key = accountModel.getPublic_key();
        this.ex_sym = transactionConfigModel.getCode();
//        this.return_rate = TCUtils.convertReturnRateToDecimal(transactionConfigModel.getCoinBackRewardRate());
        this.return_rate = transactionConfigModel.getCoinBackRewardRate();//if 1% then send 1 not send 0.01
        this.max_payment_rate = transactionConfigModel.getMaxPaymentRate();// if 50% then send 50
        this._id = "";
        this.avatar = TCUtils.isEmpty(accountModel.getAvatar()) ? TCConstant.DEFAULT_SHOP_AVATAR_URL:accountModel.getAvatar() ;
    }

    public boolean validate(String json) {
        JSONObject object;
        try {
            object = new JSONObject(json);
            for (Field f : this.getClass().getDeclaredFields()) {
                SerializedName serializedName = f.getAnnotation(SerializedName.class);
                if (serializedName != null) {
                    if (!object.has(serializedName.value())) {
                        return false;
                    }
                }
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getEx_sym() {
        return ex_sym;
    }

    public void setEx_sym(String ex_sym) {
        this.ex_sym = ex_sym;
    }

    public double getEx_tee() {
        return ex_tee;
    }

    public void setEx_tee(double ex_tee) {
        this.ex_tee = ex_tee;
    }

    public double getReturn_rate() {
        return return_rate;
    }

    public void setReturn_rate(double return_rate) {
        this.return_rate = return_rate;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public double getMax_payment_rate() {
        return max_payment_rate;
    }

    public void setMax_payment_rate(double max_payment_rate) {
        this.max_payment_rate = max_payment_rate;
    }
}

