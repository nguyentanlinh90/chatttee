package com.teecoin.model.couponsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;

public class VendorCodeRedeemModel implements Serializable {
    private String id;
    private String typeRedeem;
    @SerializedName("vendor_code")
    @Expose
    private String vendor_code;
    @SerializedName("serial")
    @Expose
    private String serial;

    public VendorCodeRedeemModel() {
    }

    public VendorCodeRedeemModel(String vendor_code, String serial) {
        this.vendor_code = vendor_code;
        this.serial = serial;
    }

    public String getVendor_code() {
        return vendor_code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeRedeem() {
        return typeRedeem;
    }

    public void setTypeRedeem(String typeRedeem) {
        this.typeRedeem = typeRedeem;
    }

    public void setVendor_code(String vendor_code) {
        this.vendor_code = vendor_code;
    }

    public void setSerial(String serial) {
        this.serial = serial;
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

    public enum TypeRedeem {
        NOTIFICATIONS(1, "notifications"),
        CATALOGUES(2, "catalogues");
        private int value;
        private String name;

        TypeRedeem(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }
}
