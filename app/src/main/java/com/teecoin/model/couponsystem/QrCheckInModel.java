package com.teecoin.model.couponsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;

public class QrCheckInModel implements Serializable {
    @SerializedName("vendor_code")
    @Expose
    private String vendor_code;

    public QrCheckInModel() {

    }

    public String getVendor_code() {
        return vendor_code;
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
}
