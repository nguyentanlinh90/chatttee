package com.teecoin.model.general;

import android.os.Bundle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

import java.io.Serializable;
import java.util.Map;

public class DataPushNotificationModel implements Serializable {
    public static final String TAG_TYPE = "type";
    private static final String TAG_ID = "id";
    private static final String TAG_COUPON_ID = "coupon_id";
    private static final String TAG_TRANSACTION_HASH = "transaction_hash";
    private static final String TAG_TRANSACTION_ID = "transaction_id";
    private static final String TAG_URL = "url";

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("coupon_id")
    @Expose
    private String coupon_id;

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("transaction_hash")
    @Expose
    private String transaction_hash;
    @SerializedName("transaction_id")
    @Expose
    private String transaction_id;
    @SerializedName("is_read")
    @Expose
    private boolean is_read;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("serial")
    @Expose
    private String serial;

    @SerializedName("destination")
    @Expose
    private String destination;

    @SerializedName("icon")
    @Expose
    private String icon;


    public DataPushNotificationModel(Bundle extras) {
        this.id = extras.getString("id");
        this.coupon_id = extras.getString("coupon_id");
        this.type = extras.getString("type");
        this.created = extras.getString("created");
        this.title = extras.getString("title");
        this.content = extras.getString("content");
        this.transaction_hash = extras.getString("transaction_hash");
        this.transaction_id = extras.getString("transaction_id");
//        this.is_read = extras.getBoolean("is_read");
        this.url = extras.getString("url");
//        this.success = extras.getBoolean("success");
        this.serial = extras.getString("serial");

        if (this.type != null && (this.type.equals(EnumMgr.PushNotification.Coupon.getValue())
                || this.type.equals(EnumMgr.PushNotification.CatalogueCouponByCountry.getValue()))) {
            this.coupon_id = TCUtils.getIDFromURL(this.url);
        }
    }

    public DataPushNotificationModel(Map<String, String> data) {
        this.id = data.get("id");
        this.coupon_id = data.get("coupon_id");
        this.type = data.get("type");
        this.created = data.get("created");
        this.title = data.get("title");
        this.content = data.get("content");
        this.transaction_hash = data.get("transaction_hash");
        this.transaction_id = data.get("transaction_id");
//        this.is_read = data.get("is_read") != null  ? Boolean.valueOf(data.get("is_read")) : false;
        this.url = data.get("url");
//        this.success = data.get("success") != null  ? Boolean.valueOf(data.get("success")) : false;
        this.serial = data.get("serial");

        if (this.type != null && (this.type.equals(EnumMgr.PushNotification.Coupon.getValue())
                || this.type.equals(EnumMgr.PushNotification.CatalogueCouponByCountry.getValue()))) {
            this.coupon_id = TCUtils.getIDFromURL(this.url);
        }
    }

    public DataPushNotificationModel(String transaction_id) {
        this.transaction_id = transaction_id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getType() {
        return type;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTransaction_hash() {
        return transaction_hash;
    }

    public void setTransaction_hash(String transaction_hash) {
        this.transaction_hash = transaction_hash;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public boolean isIs_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getDestination() {
        return destination;
    }

    public String getIcon() {
        return icon;
    }
}
