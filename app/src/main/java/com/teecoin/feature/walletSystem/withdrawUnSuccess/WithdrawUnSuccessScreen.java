package com.teecoin.feature.walletSystem.withdrawUnSuccess;

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
import com.teecoin.feature.walletSystem.withdrawSuccess.WithdrawSuccessScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.BalanceModel;
import com.teecoin.model.walletsystem.WithdrawModel;
import com.teecoin.model.walletsystem.WithdrawPostModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.walletsystem.WithdrawRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class WithdrawUnSuccessScreen  extends TCWalletBaseFragment {
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
    private ErrorModel errorModel;
    public static WithdrawUnSuccessScreen getInstance(ErrorModel errorModel) {
        WithdrawUnSuccessScreen screen = new WithdrawUnSuccessScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ERROR_MODEL, errorModel);
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
        Bundle bundle =getArguments();
        if(bundle!=null){
            errorModel =(ErrorModel) bundle.getSerializable(ERROR_MODEL);
            if(errorModel!=null){
                tv_message.setText(errorModel.getErrorMessage());
            }
        }

        tv_cancel.setText(TCUtils.getString(R.string.back_to_my_wallet));
        registerSingleClick(R.id.frag_payment_unsuccess_tv_try_again,R.id.frag_payment_success_tv_cancel);
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
}
