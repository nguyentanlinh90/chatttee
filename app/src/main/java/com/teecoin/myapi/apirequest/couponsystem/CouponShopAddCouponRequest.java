package com.teecoin.myapi.apirequest.couponsystem;

import android.content.Context;

import com.teecoin.model.couponsystem.ShopAddCouponModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.CouponApiManager;
import com.teecoin.myapi.apirequest.CouponApiRequest;
import com.teecoin.myapi.requesttarget.CouponRequestTarget;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CouponShopAddCouponRequest extends CouponApiRequest {
    private ShopAddCouponModel shopAddCouponModel;

    public CouponShopAddCouponRequest(ShopAddCouponModel shopAddCouponModel, APIResponseListener listener) {
        super(true, CouponRequestTarget.COUPON_SHOP_ADD_COUPON, listener);
        this.shopAddCouponModel = shopAddCouponModel;
    }

    @Override
    public void requestApi(CouponApiManager apiManager, Context context) {
        apiManager.shopAddCoupon(String.format(requestTarget.toString(), getAccountUUID()), getUserToken(),
                request(shopAddCouponModel.getName()), request(shopAddCouponModel.getDiscount_type()), request(shopAddCouponModel.getPercentage()),
                request(shopAddCouponModel.getStart()), request(shopAddCouponModel.getEnd()), request(shopAddCouponModel.getDescription()),
                request(shopAddCouponModel.getTerm()), request(shopAddCouponModel.getHashtags()), request(shopAddCouponModel.getTz()),
                shopAddCouponModel.getBanner(), request(shopAddCouponModel.getCash()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }

    private RequestBody request(String textRequest) {
        return RequestBody.create(MediaType.parse("text/plain"), textRequest);
    }
}
