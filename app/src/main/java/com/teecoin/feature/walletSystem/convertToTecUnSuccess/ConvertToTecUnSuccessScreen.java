package com.teecoin.feature.walletSystem.convertToTecUnSuccess;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.feature.walletSystem.convertToTecSuccess.ConvertToTecSuccessScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.walletsystem.ConvertToTecModel;
import com.teecoin.model.walletsystem.ConvertToTecPostModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.walletsystem.ConvertToTecRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class ConvertToTecUnSuccessScreen extends TCWalletBaseFragment {
    private static final String ERROR_MODEL = "ERROR_MODEL";
    @BindView(R.id.frg_transaction_iv_fail)
    ImageView iv_fail;
    @BindView(R.id.frg_payment_unsuccess_tv_title)
    TextView tv_title;
    @BindView(R.id.frg_payment_unsuccess_tv_message)
    TextView tv_message;

    @BindView(R.id.frag_payment_unsuccess_tv_try_again)
    TextView tv_try_again;
    @BindView(R.id.frag_payment_success_tv_cancel)
    TextView tv_cancel;
    private ErrorModel errorMode;
    public static ConvertToTecUnSuccessScreen getInstance(ErrorModel errorMode) {
        ConvertToTecUnSuccessScreen screen = new ConvertToTecUnSuccessScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ERROR_MODEL, errorMode);
        screen.setArguments(bundle);
        return screen;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_unsuccess, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        hideHeader();
        hideFooter();
    }

    @Override
    public void onBindView() {
        super.onBindView();
        Bundle bundle = getArguments();
        if (bundle != null) {
            errorMode = (ErrorModel) bundle.getSerializable(ERROR_MODEL);
        }

        registerSingleClick(R.id.frag_payment_unsuccess_tv_try_again,R.id.frag_payment_success_tv_cancel);
        tv_message.setText(TCUtils.getString(R.string.something_wen_wrong_please_try_again));
        tv_cancel.setText(TCUtils.getString(R.string.back_to_my_wallet));
        updateStatusError(errorMode);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterSingleClick(R.id.frag_payment_unsuccess_tv_try_again,R.id.frag_payment_success_tv_cancel);
    }
    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frag_payment_unsuccess_tv_try_again:
                handleBackPressed();
                break;
            case R.id.frag_payment_success_tv_cancel:
                    // handleBackPressed();
                    openWalletScreen(true,false);
                break;
        }
    }
    @Override
    public void onPostResumeWithResult(int requestCode, int finishedResultCode, Intent finishedResult) {
        if (requestCode == EnumMgr.RequestCode.CONVERT_TO_TEC.getValue() && finishedResultCode == RESULT_OK) {
            onBackResult();
        }

    }
    private void updateStatusError(ErrorModel errorModel){
        if(errorModel!=null){
            tv_message.setText(errorModel.getErrorMessage());
        }
    }

}
