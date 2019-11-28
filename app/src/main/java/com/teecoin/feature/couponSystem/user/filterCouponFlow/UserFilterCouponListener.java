package com.teecoin.feature.couponSystem.user.filterCouponFlow;

import com.teecoin.model.couponsystem.VendorCategoryModel;

import java.util.ArrayList;

public interface UserFilterCouponListener {
    void onFilterCoupon(ArrayList<VendorCategoryModel> listCatalogue,FilterCouponModel filterCouponModel);
}
