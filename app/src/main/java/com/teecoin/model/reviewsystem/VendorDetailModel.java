package com.teecoin.model.reviewsystem;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.feature.couponSystem.user.couponQRCodeResult.UserCouponRedeemDataModel;
import com.teecoin.model.couponsystem.CheckinNotificationResultModel;
import com.teecoin.model.couponsystem.CouponCataloguePurchaseResponseModel;
import com.teecoin.model.couponsystem.CouponDetailModel;
import com.teecoin.model.couponsystem.RedeemCodeCouponResultModel;
import com.teecoin.model.walletsystem.PaymentInvoiceModel;
import com.teecoin.model.walletsystem.TransactionDetailModel;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

public class VendorDetailModel extends VendorModel implements Parcelable {
    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("is_headquarter")
    @Expose
    private Boolean is_headquarter;

    @SerializedName("open_hours")
    @Expose
    private ArrayList<OpenHoursVendorModel> open_hours;

    @SerializedName("payments")
    @Expose
    private ArrayList<PaymentsVendor> payments;

    @SerializedName("conveniences")
    @Expose
    private ArrayList<ConveniencesVendorModel> conveniences;

    @SerializedName("reviews")
    @Expose
    private ArrayList<ReviewVendorDetailModel> reviews;

    @SerializedName("images")
    @Expose
    private ArrayList<ImageModel> images;

    @SerializedName("tag_prices")
    @Expose
    private ArrayList<TagPricesModel> tag_prices;
    @SerializedName("place_id")
    @Expose
    private String place_id;
    @SerializedName("website")
    @Expose
    private String website;

    @SerializedName("outlet_count")
    @Expose
    private int outlet_count;



    private String transaction_id;
    private String destination;
    private String linkImageShop;

    public VendorDetailModel(TransactionDetailModel transModel) {
        super();
        this.name = transModel.getShop_name();
        this.transaction_id = transModel.getTransaction_id();
        destination = transModel.getType().equals(EnumMgr.TransactionType.Reward.getValue())
                || transModel.getType().equals(EnumMgr.TransactionType.CoinBack.getValue()) ?
                transModel.getSource() :// from reward
                transModel.getDestination();// from payment
        this.linkImageShop = "";//get from api RequestTarget.GET_LOGO_REVIEW_SHOP
    }

    public VendorDetailModel(PaymentInvoiceModel paymentInvoiceModel) {
        super();
        this.name = paymentInvoiceModel.getShop().getName();
        this.transaction_id = paymentInvoiceModel.getTransactionId();
        this.destination = paymentInvoiceModel.getShop().getPublic_key();
        if (paymentInvoiceModel.getShop().getAvatar() != null) {
            this.linkImageShop = paymentInvoiceModel.getShop().getAvatar();
        }
    }

    public VendorDetailModel(ReviewFromNotificationModel reviewFromNotificationModel) {
        super();
        this.transaction_id = reviewFromNotificationModel.getTransaction_id();
        this.destination = reviewFromNotificationModel.getDestination();
        this.linkImageShop = "";//get from api RequestTarget.GET_LOGO_REVIEW_SHOP
    }

    public VendorDetailModel(CouponDetailModel couponDetailModel) {
        super();

        if (couponDetailModel.getVendor() != null) {
            this.name = couponDetailModel.getVendor().getName();
            this.id = couponDetailModel.getVendor().getId();
            if (couponDetailModel.getVendor().getFeaturedImage() != null && !TCUtils.isEmpty(couponDetailModel.getVendor().getFeaturedImage())) {
                this.linkImageShop = couponDetailModel.getVendor().getFeaturedImage();
            }
            this.destination = "";//get from api RequestTarget.GET_WALLET_VENDOR
        }
    }

    public VendorDetailModel(RedeemCodeCouponResultModel userCouponModels) {
        super();
        if (userCouponModels.getVendor() != null) {
            this.name = userCouponModels.getVendor().getName();
            this.id = userCouponModels.getVendor().getId();
            if (userCouponModels.getVendor().getFeaturedImage() != null && !TCUtils.isEmpty(userCouponModels.getVendor().getFeaturedImage())) {
                this.linkImageShop = userCouponModels.getVendor().getFeaturedImage();
            }
            this.destination = "";//get from api RequestTarget.GET_WALLET_VENDOR
        }
    }

    public VendorDetailModel(CheckinNotificationResultModel checkinNotificationResultModel) {
        super();
        this.destination = checkinNotificationResultModel.getDestination();
        if (checkinNotificationResultModel.getVendor() != null) {
            this.name = checkinNotificationResultModel.getVendor().getName();
            this.id = checkinNotificationResultModel.getVendor().getId();
            linkImageShop = checkinNotificationResultModel.getVendor().getFeaturedImage();
            this.destination = "";//get from api RequestTarget.GET_WALLET_VENDOR
        }
    }

    public VendorDetailModel(CouponCataloguePurchaseResponseModel purchaseResponseModel) {
        super();
    }

    public VendorDetailModel(UserCouponRedeemDataModel couponResultModel) {
        super();
        if (couponResultModel != null && couponResultModel.getVendor() != null) {
            this.name = couponResultModel.getVendor().getName();
            this.id = couponResultModel.getVendor().getId();
            if (!TCUtils.isEmpty(couponResultModel.getVendor().getFeaturedImage())) {
                this.linkImageShop = couponResultModel.getVendor().getFeaturedImage();
            }
            this.destination = "";//get from api RequestTarget.GET_WALLET_VENDOR
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getIs_headquarter() {
        return is_headquarter;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<OpenHoursVendorModel> getOpen_hours() {
        return open_hours;
    }

    public void setOpen_hours(ArrayList<OpenHoursVendorModel> open_hours) {
        this.open_hours = open_hours;
    }

    public ArrayList<PaymentsVendor> getPayments() {
        return payments;
    }

    public void setPayments(ArrayList<PaymentsVendor> payments) {
        this.payments = payments;
    }

    public ArrayList<ConveniencesVendorModel> getConveniences() {
        return conveniences;
    }

    public void setConveniences(ArrayList<ConveniencesVendorModel> conveniences) {
        this.conveniences = conveniences;
    }

    public ArrayList<ReviewVendorDetailModel> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<ReviewVendorDetailModel> reviews) {
        this.reviews = reviews;
    }

    public VendorDetailModel(Parcel in) {

    }
    public static final Creator<VendorDetailModel> CREATOR = new Creator<VendorDetailModel>() {
        @Override
        public VendorDetailModel createFromParcel(Parcel in) {
            return new VendorDetailModel(in);
        }

        @Override
        public VendorDetailModel[] newArray(int size) {
            return new VendorDetailModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(phone);
    }

    public ArrayList<ImageModel> getImages() {
        return images;
    }

    public ArrayList<TagPricesModel> getTag_prices() {
        return tag_prices;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getPlace_id() {
        return place_id;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }


    public String getTransaction_id() {
        return transaction_id;
    }

    public String getDestination() {
        return destination;
    }

    public String getLinkImageShop() {
        return linkImageShop;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getOutlet_count() {
        return outlet_count;
    }
}
