package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.TeeCoinModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;

public class MyQRCodeModel extends TeeCoinModel implements Serializable {

    @SerializedName("public_key")
    @Expose
    private String public_key;

    @SerializedName("first_name")
    @Expose
    private String first_name;

    @SerializedName("last_name")
    @Expose
    private String last_name;

    @SerializedName("full_name")
    @Expose
    private String full_name;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    public MyQRCodeModel() {
    }

    public MyQRCodeModel(String public_key, String first_name, String last_name, String avatar) {
        this.public_key = public_key;
        this.first_name = first_name;
        this.last_name = last_name;
        this.avatar = avatar;
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

    public String getPublic_key() {
        return public_key;
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

    public String getFull_name() {
        return full_name;
    }
}
