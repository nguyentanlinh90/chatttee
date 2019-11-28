package com.teecoin.feature.couponSystem.user.couponQRCodeResult;

import com.teecoin.model.couponsystem.CouponCataloguePurchaseResponseModel;
import com.teecoin.model.couponsystem.CouponDetailModel;
import com.teecoin.model.couponsystem.RedeemCodeCouponResultModel;
import com.teecoin.model.couponsystem.VendorCodeCheckInModel;
import com.teecoin.model.couponsystem.VendorCodeRedeemModel;
import com.teecoin.model.reviewsystem.CuisineModel;
import com.teecoin.model.reviewsystem.VendorModel;

import java.io.Serializable;
import java.util.ArrayList;

public class UserCouponRedeemDataModel implements Serializable {

    private boolean fromWallet;
    private boolean isWrongCode;
    private VendorCodeCheckInModel vendorCodeCheckInModel;
    private VendorModel vendor;
    private String couponName;
    private String serial;
    private ArrayList<CuisineModel> couponCuisines;
    private VendorCodeRedeemModel vendorCodeRedeemModel;

    public UserCouponRedeemDataModel(CouponDetailModel couponDetailModel, boolean isWrongCode) {
        this.vendor = couponDetailModel.getVendor();
        if (couponDetailModel.getVendor() != null) {
            if (couponDetailModel.getHashTags() != null && couponDetailModel.getHashTags().size() > 0) {
                this.couponCuisines = new ArrayList<>();
                for (String hashTag : couponDetailModel.getHashTags()) {
                    this.couponCuisines.add(new CuisineModel(hashTag));
                }
            }
        }
        this.couponName = couponDetailModel.getName();
        this.serial = couponDetailModel.getSerial();
        this.isWrongCode = isWrongCode;
        this.vendorCodeRedeemModel = new VendorCodeRedeemModel();
        this.vendorCodeRedeemModel.setId(String.valueOf(couponDetailModel.getId()));
        this.vendorCodeRedeemModel.setTypeRedeem(couponDetailModel.getTypeToRequestAPIRedeem());
        this.vendorCodeRedeemModel.setSerial(couponDetailModel.getSerial());
    }

    public UserCouponRedeemDataModel(CouponCataloguePurchaseResponseModel purchaseResponseModel, boolean isWrongCode) {
        this.vendor = purchaseResponseModel.getVendor();
        if (purchaseResponseModel.getVendor() != null) {
            this.couponCuisines = purchaseResponseModel.getVendor().getCuisines();
        }
        this.couponName = purchaseResponseModel.getVendor().getName();
        this.serial = purchaseResponseModel.getCoupon().getSerial();
        this.isWrongCode = isWrongCode;
        this.vendorCodeRedeemModel = new VendorCodeRedeemModel();
        //  this.vendorCodeRedeemModel.setId(String.valueOf(purchaseResponseModel.getCoupon().getCoupon_id()));
        this.vendorCodeRedeemModel.setId(String.valueOf(purchaseResponseModel.getCoupon().getId()));
        this.vendorCodeRedeemModel.setTypeRedeem(VendorCodeRedeemModel.TypeRedeem.CATALOGUES.getName());
        this.vendorCodeRedeemModel.setSerial(purchaseResponseModel.getCoupon().getSerial());
    }


    public UserCouponRedeemDataModel(CouponDetailModel couponDetailModel, VendorCodeRedeemModel vendorCodeRedeemModel) {
        this.vendor = couponDetailModel.getVendor();
        if (couponDetailModel.getVendor() != null) {
            if (couponDetailModel.getHashTags() != null && couponDetailModel.getHashTags().size() > 0) {
                this.couponCuisines = new ArrayList<>();
                for (String hashTag : couponDetailModel.getHashTags()) {
                    this.couponCuisines.add(new CuisineModel(hashTag));
                }
            }
        }
        this.couponName = couponDetailModel.getName();
        this.serial = couponDetailModel.getSerial();
        this.vendorCodeRedeemModel = vendorCodeRedeemModel;
        if (this.vendorCodeRedeemModel != null) {
            this.vendorCodeRedeemModel.setId(String.valueOf(couponDetailModel.getId()));
            this.vendorCodeRedeemModel.setTypeRedeem(couponDetailModel.getTypeToRequestAPIRedeem());
        }

    }

    public UserCouponRedeemDataModel(CouponCataloguePurchaseResponseModel couponCataloguePurchaseResponseModel, VendorCodeRedeemModel vendorCodeRedeemModel) {
        this.vendor = couponCataloguePurchaseResponseModel.getVendor();
        if (couponCataloguePurchaseResponseModel.getVendor() != null) {
            this.couponCuisines = couponCataloguePurchaseResponseModel.getVendor().getCuisines();
        }
        this.couponName = couponCataloguePurchaseResponseModel.getVendor().getName();
        this.serial = couponCataloguePurchaseResponseModel.getCoupon().getSerial();
        this.vendorCodeRedeemModel = vendorCodeRedeemModel;
        if (this.vendorCodeRedeemModel != null) {
            this.vendorCodeRedeemModel.setId(String.valueOf(couponCataloguePurchaseResponseModel.getCoupon().getId()));
            this.vendorCodeRedeemModel.setTypeRedeem(VendorCodeRedeemModel.TypeRedeem.CATALOGUES.getName());
        }
    }

    public UserCouponRedeemDataModel(RedeemCodeCouponResultModel redeemCodeCouponResultModel) {
        this.vendor = redeemCodeCouponResultModel.getVendor();
        if (redeemCodeCouponResultModel.getVendor() != null) {
            this.couponCuisines = redeemCodeCouponResultModel.getVendor().getCuisines();
        }
        this.couponName = redeemCodeCouponResultModel.getVendor().getName();
        this.serial = redeemCodeCouponResultModel.getRedeem_code();
    }

    public UserCouponRedeemDataModel(boolean fromWallet, VendorCodeCheckInModel vendorCodeCheckInModel) {
        this.fromWallet = fromWallet;
        this.vendorCodeCheckInModel = vendorCodeCheckInModel;
    }

    public ArrayList<CuisineModel> getCouponCuisines() {
        return couponCuisines;
    }

    public String getCouponName() {
        return couponName;
    }

    public String getSerial() {
        return serial;
    }

    public VendorCodeRedeemModel getVendorCodeRedeemModel() {
        return vendorCodeRedeemModel;
    }

    public void setVendorCodeRedeemModel(VendorCodeRedeemModel vendorCodeRedeemModel) {
        this.vendorCodeRedeemModel = vendorCodeRedeemModel;
    }

    public boolean isFromWallet() {
        return fromWallet;
    }

    public VendorCodeCheckInModel getVendorCodeCheckInModel() {
        return vendorCodeCheckInModel;
    }

    public boolean isWrongCode() {
        return isWrongCode;
    }

    public void setWrongCode(boolean wrongCode) {
        isWrongCode = wrongCode;
    }

    public VendorModel getVendor() {
        return vendor;
    }

    public void updateAfterCheckIn(RedeemCodeCouponResultModel redeemCodeCouponResultModel) {
        this.vendor = redeemCodeCouponResultModel.getVendor();
        if (redeemCodeCouponResultModel.getVendor() != null) {
            this.couponCuisines = redeemCodeCouponResultModel.getVendor().getCuisines();
        }
        this.couponName = redeemCodeCouponResultModel.getVendor().getName();
        this.serial = redeemCodeCouponResultModel.getRedeem_code();
    }
}
