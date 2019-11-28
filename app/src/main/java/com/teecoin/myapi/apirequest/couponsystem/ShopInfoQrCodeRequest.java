
package com.teecoin.myapi.apirequest.couponsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.CouponApiManager;
import com.teecoin.myapi.apirequest.CouponApiRequest;
import com.teecoin.myapi.requesttarget.CouponRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShopInfoQrCodeRequest extends CouponApiRequest {

    public ShopInfoQrCodeRequest(APIResponseListener listener) {
        super(true, CouponRequestTarget.COUPON_SHOP_INFO_QR_CODE, listener);
    }

    @Override
    public void requestApi(CouponApiManager apiManager, Context context) {
        apiManager.getShopInfoQrCode(
                String.format(requestTarget.toString(), getAccountUUID()), getUserToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}

