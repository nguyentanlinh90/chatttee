package com.teecoin.model.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.TeeCoinModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;


public class DeviceRegistrationModel extends TeeCoinModel {

    @SerializedName("device_id")
    @Expose
    private String deviceId;

    @SerializedName("create_date")
    @Expose
    private String createDate;

    @SerializedName("create_wallet_count")
    @Expose
    private int createWalletCount;

    public DeviceRegistrationModel() {
    }

    public DeviceRegistrationModel(String deviceId, int createWalletCount, String createDate) {
        this.deviceId = deviceId;
        this.createWalletCount = createWalletCount;
        this.createDate = createDate;
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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getCreateWalletCount() {
        return createWalletCount;
    }

    public void setCreateWalletCount(int createWalletCount) {
        this.createWalletCount = createWalletCount;
    }

    public String getCreateDate() {
        return createDate;
    }
}
