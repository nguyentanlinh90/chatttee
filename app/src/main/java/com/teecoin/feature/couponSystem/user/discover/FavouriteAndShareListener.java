package com.teecoin.feature.couponSystem.user.discover;

import com.teecoin.model.reviewsystem.VendorModel;

public interface FavouriteAndShareListener {
    void submitFavoriteSuccess(int position, VendorModel vendorModel);
}
