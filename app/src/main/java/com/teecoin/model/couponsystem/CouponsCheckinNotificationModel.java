package com.teecoin.model.couponsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CouponsCheckinNotificationModel {
    @SerializedName("catalogue_coupons")
    @Expose
    private ArrayList<UserCouponCatalogueDataModel> catalogueCoupons = null;

    @SerializedName("notification_coupons")
    @Expose
    private ArrayList<NotificationCouponModel> notificationCoupons;

    public ArrayList<UserCouponCatalogueDataModel> getCatalogueCoupons() {
        return catalogueCoupons;
    }

    public ArrayList<NotificationCouponModel> getNotificationCoupons() {
        return notificationCoupons;
    }
}
