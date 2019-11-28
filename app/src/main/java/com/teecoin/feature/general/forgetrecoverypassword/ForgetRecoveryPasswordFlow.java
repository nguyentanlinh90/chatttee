package com.teecoin.feature.general.forgetrecoverypassword;

import android.content.Context;

import com.teecoin.TCMainActivity;
import com.teecoin.base.TCDecisionListener;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.general.GeneralGetVerificationCodeRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;

public class ForgetRecoveryPasswordFlow {

    private Context context;
    private InputVerificationCodeListener listener;

    public ForgetRecoveryPasswordFlow(Context context, InputVerificationCodeListener listener) {
        this.context = context;
        this.listener = listener;
        showDialog();
    }

    private void showDialog() {
        new ForgetRecoveryPasswordDialog(this.context, new TCDecisionListener() {
            @Override
            public void onPositiveButtonClicked(int id, Object onWhat) {
                requestGetVerificationCode();
            }

            @Override
            public void onNegativeButtonClicked(int id, Object onWhat) {

            }

            @Override
            public void onNeutralButtonClicked(int id, Object onWhat) {

            }
        }).show();
    }

    private void requestGetVerificationCode() {

        new InputVerificationCodeDialog(ForgetRecoveryPasswordFlow.this.context, listener).show();

        ((TCMainActivity) this.context).requestApi(new GeneralGetVerificationCodeRequest(new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                new InputVerificationCodeDialog(ForgetRecoveryPasswordFlow.this.context, listener).show();
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                ((TCMainActivity) context).showBaseMessage(errorModel.getErrorMessage());
            }
        }));
    }


}
