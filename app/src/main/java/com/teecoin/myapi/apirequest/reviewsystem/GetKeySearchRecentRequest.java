package com.teecoin.myapi.apirequest.reviewsystem;

import android.content.Context;

import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.MyApiSubscribe;
import com.teecoin.myapi.apimanager.ReviewApiManager;
import com.teecoin.myapi.apirequest.ReviewApiRequest;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GetKeySearchRecentRequest extends ReviewApiRequest {

    public GetKeySearchRecentRequest(APIResponseListener listener) {
        super(false, ReviewRequestTarget.GET_KEY_SEARCH_RECENT_LIST, listener);
    }

    @Override
    public void requestApi(ReviewApiManager apiManager, Context context) {
        apiManager.getListKeySearchRecent(String.format(requestTarget.toString(), getAccountUUID()),
                getUserToken()).subscribeOn(Schedulers.newThread()).observeOn(
                AndroidSchedulers.mainThread()).subscribe(
                new MyApiSubscribe<Object>(context, listener, requestTarget));
    }
}
