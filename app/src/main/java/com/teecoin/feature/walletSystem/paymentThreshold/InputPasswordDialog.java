package com.teecoin.feature.walletSystem.paymentThreshold;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.general.GeneralVerifyPasswordRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

import java.util.Objects;

import butterknife.BindView;

public class InputPasswordDialog extends TCBaseDialog implements View.OnFocusChangeListener {

    @BindView(R.id.dialog_input_tv_title)
    TextView tvTitle;

    @BindView(R.id.dialog_input_tv_content)
    TextView tvContent;

    @BindView(R.id.view_input_password_v_input)
    View vInput;

    @BindView(R.id.view_input_password_et_input)
    EditText etPass;

    @BindView(R.id.view_input_password_iv_clear_input)
    ImageView ivClear;

    @BindView(R.id.tv_bt_left)
    TextView tvNo;

    @BindView(R.id.tv_bt_right)
    TextView tvSubmit;

    private Context mContext;

    private int type;

    private InputPasswordListener listener;

    public InputPasswordDialog(Context context, int type, InputPasswordListener listener) {
        super(context);
        this.mContext = context;
        this.type = type;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input_password);
    }


    @Override
    protected void initContentView() {

        TCUtils.showHideRemoveIconInEditText(etPass, ivClear);
        tvNo.setText(TCUtils.getString(R.string.text_cancel));
        if (type == EnumMgr.TypeDialog.ChangePassword.getValue()) {
            //tvTitle.setText(TCUtils.getString(R.string.input_password));
            tvTitle.setText(TCUtils.getString(R.string.current_password));

        } else if (EnumMgr.TypeDialog.InputPassword.getValue() == type) {
            tvTitle.setText(TCUtils.getString(R.string.input_password));
        } else if (EnumMgr.TypeDialog.PaymentThreshold.getValue() == type) {

            tvTitle.setText(TCUtils.getString(R.string.text_payment_threshold));

            tvContent.setText(TCUtils.getString(R.string.maximum_amount));

            etPass.setHint(TCUtils.getString(R.string.please_enter_the_maximum_amount));
        } else if (type == EnumMgr.TypeDialog.ConfirmPaymentGreaterThanPaymentThreshold.getValue()) {

            tvTitle.setText(TCUtils.getString(R.string.input_password));
        }

        etPass.setOnFocusChangeListener(this);
        setCancelable(false);

    }

    @Override
    protected void onViewClick() {
        registerSingleClick(R.id.tv_bt_left, R.id.tv_bt_right);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.tv_bt_left:
                closeDialog();
                break;
            case R.id.tv_bt_right:
                checkPassword();
                break;
        }
    }

    private void checkPassword() {
        String recoveryPass = etPass.getText().toString().trim();
        ((TCMainActivity) mContext).requestApi(new GeneralVerifyPasswordRequest(recoveryPass, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                if (response.getSuccess()) {
                    dismiss();
                    listener.onSubmit(recoveryPass);
                } else {
                    ((TCMainActivity) mContext).showAlertDialog(View.NO_ID,
                            TCUtils.getString(R.string.text_alert),
                            TCUtils.getString(R.string.incorrect_recovery_password),
                            TCUtils.getString(R.string.text_ok),
                            null, null);
                }

            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                ((TCMainActivity) mContext).showBaseMessage(errorModel.getErrorMessage());
            }
        }));
    }

    private void closeDialog() {

        dismiss();

        listener.onCancel();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        switch (v.getId()) {
            case R.id.view_input_password_et_input:
                if (hasFocus) {

                    vInput.setBackground(TCUtils.getDrawable(R.drawable.bg_white_solid_gold_border_5_radius));

                } else {
                    vInput.setBackground(TCUtils.getDrawable(R.drawable.bg_white_solid_gray_border_5_radius));

                }
                break;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Objects.requireNonNull(getWindow()).clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        etPass.requestFocus();
    }
}
