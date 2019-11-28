package com.teecoin.feature.general.forgetrecoverypassword;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.general.GeneralCheckVerificationCodeRequest;
import com.teecoin.myapi.apirequest.general.GeneralGetVerificationCodeRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCLog;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class InputVerificationCodeDialog extends TCBaseDialog implements View.OnFocusChangeListener {

    @BindView(R.id.view_input_password_v_input)
    View vInput;

    @BindView(R.id.tv_bt_left)
    TextView tvCancel;

    @BindView(R.id.tv_bt_right)
    TextView tvSubmit;

    @BindView(R.id.view_input_password_iv_lock_input)
    ImageView ivLock;

    @BindView(R.id.view_input_password_et_input)
    EditText et_verification_code;

    @BindView(R.id.view_input_password_iv_clear_input)
    ImageView ivClearVerificationCode;

    @BindView(R.id.dialog_input_verification_code_tv_error_msg)
    TextView tv_error_msg;

    @BindView(R.id.dialog_input_verification_code_tv_resend_count_down)
    TextView tv_resend_count_down;

    @BindView(R.id.dialog_input_verification_code_tv_resend)
    TextView tv_resend;


    private Context context;
    private InputVerificationCodeListener listener;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning;

    InputVerificationCodeDialog(Context context, InputVerificationCodeListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input_verification_code);
    }

    @Override
    protected void initContentView() {

        ivLock.setVisibility(View.GONE);

        tvCancel.setText(TCUtils.getString(R.string.text_cancel));

        tvSubmit.setText(TCUtils.getString(R.string.submit));

        setFullScreen(true);

        countDownTimer();

        et_verification_code.setHint(TCUtils.getString(R.string.please_enter_verification_code));

        TCUtils.showHideRemoveIconInEditText(et_verification_code, ivClearVerificationCode);
        TCUtils.editTextTextChange(et_verification_code, ivClearVerificationCode, tv_error_msg, vInput, null);
        ///TCUtils.editTextTextChange(et_verification_code, ivClearVerificationCode, vInput, null);
        et_verification_code.setOnFocusChangeListener(this);
    }

    @Override
    protected void onViewClick() {

        registerSingleClick(tvCancel, tvSubmit, tv_resend);

//        tvCancel.setOnClickListener(v -> closeDialog());
//
//        tvSubmit.setOnClickListener(v -> checkVerificationCode());
//
//        tv_resend.setOnClickListener(v -> {
//
//            countDownTimer();
//
//            requestGetVerificationCode();
//        });
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.tv_bt_left:
                closeDialog();
                break;
            case R.id.tv_bt_right:
                checkVerificationCode();
                break;
            case R.id.dialog_input_verification_code_tv_resend:
                countDownTimer();
                requestGetVerificationCode();
                break;
        }
    }

    private void closeDialog() {
        isTimerRunning = false;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        dismiss();
    }

    private void checkVerificationCode() {

        String verificationCode = et_verification_code.getText().toString().trim();

        if (TCUtils.isEmpty(verificationCode)) {

            tv_error_msg.setVisibility(View.VISIBLE);
            tv_error_msg.setText(TCUtils.getString(R.string.verify_phone_number_pls_input_verification_code));
            TCUtils.viewInputsetError(vInput);
            return;
        }

        tv_error_msg.setVisibility(View.GONE);

        ((TCMainActivity) this.context)
                .requestApi(new GeneralCheckVerificationCodeRequest(verificationCode, new APIResponseListener() {
                    @Override
                    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                        listener.onValidVerificationCodeSuccess(verificationCode);
                        dismiss();
                    }

                    @Override
                    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                        tv_error_msg.setVisibility(View.VISIBLE);
                        tv_error_msg.setText(errorModel.getErrorMessage());
                        TCUtils.viewInputsetError(vInput);
                    }
                }));
    }

    private void countDownTimer() {

        String resendString = TCUtils.getString(R.string.verify_phone_number_resend_in);

        tv_resend_count_down.setVisibility(View.VISIBLE);

        tv_resend.setVisibility(View.GONE);

        if (countDownTimer != null)

            countDownTimer = null;
        isTimerRunning = true;
        countDownTimer = new CountDownTimer(TCConstant.TEN_SECOND_IN_MILLISECOND, TCConstant.ONE_SECOND_IN_MILLISECOND) {
            public void onTick(long millisUntilFinished) {
                if (isTimerRunning) {
                    TCLog.d("hung Tick onTick" + millisUntilFinished);
                    tv_resend_count_down.setText(String.format(resendString, millisUntilFinished / 1000));
                }
            }

            public void onFinish() {
                if (isTimerRunning) {
                    TCLog.d("hung Tick onFinish");
                    tv_resend_count_down.setVisibility(View.GONE);
                    tv_resend.setVisibility(View.VISIBLE);
                }
            }
        }.start();
    }

    private void requestGetVerificationCode() {
        ((TCMainActivity) this.context).requestApi(new GeneralGetVerificationCodeRequest(new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {

                tv_error_msg.setVisibility(View.GONE);

            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

                tv_error_msg.setVisibility(View.VISIBLE);
                tv_error_msg.setText(errorModel.getErrorMessage());

            }
        }));
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {

            vInput.setBackground(TCUtils.getDrawable(R.drawable.bg_white_solid_gold_border_5_radius));

        } else {

            vInput.setBackground(TCUtils.getDrawable(R.drawable.bg_white_solid_gray_border_5_radius));

        }
    }
}
