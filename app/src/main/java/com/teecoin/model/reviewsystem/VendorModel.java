package com.teecoin.model.reviewsystem;

import com.google.android.gms.maps.model.Marker;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class VendorModel implements Serializable {

    @SerializedName("id")
    @Expose
    protected String id;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("name")
    @Expose
    protected String name;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("rating")
    @Expose
    private float rating;

    @SerializedName("review_count")
    @Expose
    private int reviewCount;

    @SerializedName("reward_count")
    @Expose
    private String rewardCount;

    @SerializedName("featured_image")
    @Expose
    private String featuredImage;

    @SerializedName("logo")
    @Expose
    private String logo;

    @SerializedName("highlight_image")
    @Expose
    private String highlightImage;

    @SerializedName("free_tags")
    @Expose
    private String freeTags;

    @SerializedName("cuisines")
    @Expose
    private ArrayList<CuisineModel> cuisines;

    @SerializedName("cuisine_list")
    @Expose
    private ArrayList<String> cuisineList;

    @SerializedName("open_hour_search")
    @Expose
    private ArrayList<OpenHourModel> openHourSearch;

    @SerializedName("open_status")
    @Expose
    private OpenStatusModel openStatus;

    @SerializedName("distance")
    @Expose
    private String distance;

    @SerializedName("avg_price")
    @Expose
    private String avgPrice;

    @SerializedName("have_catalogue_coupon")
    @Expose
    private boolean haveCatalogueCoupon;

    @SerializedName("have_cash_voucher")
    @Expose
    private boolean haveCashVoucher;

    @SerializedName("is_favorite")
    @Expose
    private boolean isFavorite;

    @SerializedName("url_share")
    @Expose
    private String urlShare;

    @SerializedName("content_share")
    @Expose
    private String contentShare;

    @SerializedName("country_code")
    @Expose
    private String countryCode;

    @SerializedName("point")
    @Expose
    private String point;

    @SerializedName("checkin_amount")
    @Expose
    private String checkin_amount;

    @SerializedName("total_checkin")
    @Expose
    private String total_checkin;

    private boolean isSelectVendor;

    private Marker marker;


    private VendorMarker vendorMarker;

    public String getId() {
        return id;
    }

    public String getCreated() {
        return created;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getLocation() {
        return location;
    }

    public float getRating() {
        return rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getRewardCount() {
        return rewardCount;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public String getHighlightImage() {
        return highlightImage;
    }

    public String getFreeTags() {
        return freeTags;
    }

    public ArrayList<CuisineModel> getCuisines() {
        return cuisines;
    }

    public ArrayList<OpenHourModel> getOpenHourSearch() {
        return openHourSearch;
    }

    public OpenStatusModel getOpenStatus() {
        return openStatus;
    }

    public String getDistance() {
        return distance;
    }

    public String getAvgPrice() {
        return avgPrice;
    }

    public boolean isHaveCatalogueCoupon() {
        return haveCatalogueCoupon;
    }

    public boolean isHaveCashVoucher() {
        return haveCashVoucher;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public String getUrlShare() {
        return urlShare;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getPoint() {
        return point;
    }

    public void setVendorMarker(VendorMarker vendorMarker) {
        this.vendorMarker = vendorMarker;
    }

    public VendorMarker getVendorMarker() {
        return vendorMarker;
    }

    public boolean isSelectVendor() {
        return isSelectVendor;
    }

    public void setSelectVendor(boolean selectVendor) {
        isSelectVendor = selectVendor;
    }

    public String getLogo() {
        return logo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContentShare() {
        return contentShare;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public ArrayList<String> getCuisineList() {
        return cuisineList;
    }

    public void setCuisineList(ArrayList<String> cuisineList) {
        this.cuisineList = cuisineList;
    }

    public String getCheckin_amount() {
        return checkin_amount;
    }

    public String getTotal_checkin() {
        return total_checkin;
    }
}


