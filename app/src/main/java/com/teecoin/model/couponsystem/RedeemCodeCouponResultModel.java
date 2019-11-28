package com.teecoin.model.couponsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.R;
import com.teecoin.model.reviewsystem.VendorModel;
import com.teecoin.utils.TCUtils;

import java.io.Serializable;
import java.util.ArrayList;

public class RedeemCodeCouponResultModel implements Serializable {
    @SerializedName("redeem_status")
    @Expose
    private int redeemStatus;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("serial")
    @Expose
    private String serial;
    @SerializedName("redeem_code")
    @Expose
    private String redeem_code;

    @SerializedName("vendor")
    @Expose
    private VendorModel vendor;
    //param for redeem
    @SerializedName("have_checkin")
    @Expose
    private Boolean have_checkin;
    //param for check_in
    @SerializedName("check_in_status")
    @Expose
    private Boolean check_in_status;
    @SerializedName("error_msg_code")
    @Expose
    private int error_msg_code;
    @SerializedName("catalogue_coupons")
    @Expose
    private ArrayList<UserCouponCatalogueDataModel> catalogueCoupons = null;
    @SerializedName("notification_coupons")
    @Expose
    private ArrayList<NotificationCouponModel> notificationCoupons = null;

    public int getRedeemStatus() {
        return redeemStatus;
    }

    public void setRedeemStatus(int redeemStatus) {
        this.redeemStatus = redeemStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VendorModel getVendor() {
        return vendor;
    }

    public void setVendor(VendorModel vendor) {
        this.vendor = vendor;
    }

    public Boolean getHave_checkin() {
        return have_checkin;
    }

    public Boolean getCheck_in_status() {
        return check_in_status;
    }

    public ArrayList<UserCouponCatalogueDataModel> getCatalogueCoupons() {
        return catalogueCoupons;
    }

    public ArrayList<NotificationCouponModel> getNotificationCoupons() {
        return notificationCoupons;
    }

    public int getError_msg_code() {
        return error_msg_code;
    }

    public String getSerial() {
        return serial;
    }

    public String getRedeem_code() {
        return redeem_code;
    }

    public String getMessageErrorCheckIn() {

        String message = TCUtils.getString(R.string.sorry_something_went_wrong);

        switch (getError_msg_code()) {
            case 1:
                message = TCUtils.getString(R.string.coupon_check_in_error_1);//Check_In_Error.ERROR_1.getName();
                break;
            case 2:
                message = TCUtils.getString(R.string.coupon_check_in_error_2);//Check_In_Error.ERROR_2.getName();
                break;
            case 3:
                message = TCUtils.getString(R.string.coupon_check_in_error_3);//Check_In_Error.ERROR_3.getName();
                break;
            case 4:
                message = TCUtils.getString(R.string.coupon_check_in_error_4);//Check_In_Error.ERROR_4.getName();
                break;
            case 6:
                message = TCUtils.getString(R.string.coupon_check_in_error_6);//Check_In_Error.ERROR_6.getName();
                break;
        }

        return message;
    }

    public enum RedeemStatus {
        FAIL(1),
        TRY_AGAIN(2),
        SUCCESS(3),
        VALIDITY_TIME(4);
        private int value;

        RedeemStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum Check_In_Error {
        SUCCESS(0, "");
        //        ERROR_1(1, TCUtils.getString(R.string.coupon_check_in_error_1)),
//        ERROR_2(2, TCUtils.getString(R.string.coupon_check_in_error_2)),
//        ERROR_3(3, TCUtils.getString(R.string.coupon_check_in_error_3)),
//        ERROR_4(4, TCUtils.getString(R.string.coupon_check_in_error_4)),
//        ERROR_5(5, TCUtils.getString(R.string.coupon_check_in_error_5)),
//        ERROR_6(6, TCUtils.getString(R.string.coupon_check_in_error_6));
        private int value;
        private String name;

        Check_In_Error(int value, String name) {
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