package com.teecoin.stellar;

import android.view.View;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCApplication;
import com.teecoin.javastellarsdk.stellar.exceptions.DestinationNotFoundException;
import com.teecoin.utils.TCUtils;

import java.util.concurrent.TimeoutException;

import rx.Subscriber;

public class  StellarSubscriber<T> extends Subscriber<T> {

    StellarResponseListener listener;
    private T object;
    private boolean isLoading=true;
    public StellarSubscriber(StellarResponseListener listener) {
        this.listener = listener;
        this.isLoading = true;
    }

    public StellarSubscriber(StellarResponseListener listener,boolean isLoading) {
        this.listener = listener;
        this.isLoading = isLoading;
    }

    @Override
    public void onCompleted() {
        if ( isLoading && TCApplication.getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) TCApplication.getActiveActivity()).hideLoadingDialog();
        }
        if(listener != null){
            listener.onStellarSuccess(this.object);
        }
    }

    @Override
    public void onError(Throwable e) {
        if (TCApplication.getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) TCApplication.getActiveActivity()).hideLoadingDialog();
        }
        if(e instanceof TimeoutException){
            ((TCMainActivity) TCApplication.getActiveActivity()).showAlertDialog(View.NO_ID,
                    TCUtils.getString(R.string.text_warning), TCUtils.getString(R.string.error_request_time_out),
                    TCUtils.getString(R.string.text_ok), null, null);
        }else if(e instanceof DestinationNotFoundException ){
            ((TCMainActivity) TCApplication.getActiveActivity()).showBaseMessage(TCUtils.getString(R.string.error_no_internet_connection));
        }else {
            if (listener != null) {
                listener.onStellarFail(e);
            }
        }
    }

    @Override
    public void onNext(T t) {
        if (isLoading && TCApplication.getActiveActivity() instanceof TCMainActivity) {
            ((TCMainActivity) TCApplication.getActiveActivity()).hideLoadingDialog();
        }
        this.object = null;
        this.object = t;

    }
}
