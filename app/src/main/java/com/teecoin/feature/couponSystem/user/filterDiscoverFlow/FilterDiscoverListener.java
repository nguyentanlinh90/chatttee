package com.teecoin.feature.couponSystem.user.filterDiscoverFlow;

import com.teecoin.model.couponsystem.VendorCategoryModel;

import java.util.ArrayList;

public interface FilterDiscoverListener {
    void onFilter(FilterDiscoverModel filterDiscoverModel, ArrayList<VendorCategoryModel> listCategory);

}
