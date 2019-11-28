package com.teecoin.model.couponsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopCouponSendNotificationModel {
    @SerializedName("coupon_template_id")
    @Expose
    private String couponTemplateId;
    @SerializedName("campaign_no")
    @Expose
    private String campaignNo;
    @SerializedName("notify_type")
    @Expose
    private String notifyType;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("start")
    @Expose
    private String start;
    @SerializedName("end")
    @Expose
    private String end;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("long")
    @Expose
    private String _long;

    public ShopCouponSendNotificationModel(String couponTemplateId, String campaignNo, String notifyType, String distance, String quantity, String start, String end, String lat, String _long) {
        this.couponTemplateId = couponTemplateId;
        this.campaignNo = campaignNo;
        this.notifyType = notifyType;
        this.distance = distance;
        this.quantity = quantity;
        this.start = start;
        this.end = end;
        this.lat = lat;
        this._long = _long;
    }
}