package com.teecoin.myapi.apirequest.reviewsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GetSuggestVendorRequest extends ReviewApiRequest {

    private String keyWord;
    private String countryCode;

    public GetSuggestVendorRequest(String keyWord, String countryCode, APIResponseListener listener) {
        super(true, ReviewRequestTarget.GET_SUGGEST_VENDOR, listener);
        this.keyWord = keyWord;
        this.countryCode = countryCode;
    }

    @Override
    public void requestApi(ReviewApiManager apiManager, Context context) {
        apiManager.getListSuggest(String.format(requestTarget.toString(), keyWord, countryCode),
                getUserToken()).subscribeOn(Schedulers.newThread()).observeOn(
                AndroidSchedulers.mainThread()).subscribe(
                new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
