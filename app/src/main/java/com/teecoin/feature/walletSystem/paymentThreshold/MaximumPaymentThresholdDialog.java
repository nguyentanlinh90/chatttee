package com.teecoin.feature.walletSystem.paymentThreshold;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class MaximumPaymentThresholdDialog extends TCBaseDialog {

    @BindView(R.id.dialog_input_tv_title)
    TextView tvTitle;

    @BindView(R.id.dialog_input_tv_content)
    TextView tvContent;

    @BindView(R.id.view_input_password_et_input)
    EditText etPass;

    @BindView(R.id.view_input_password_iv_clear_input)
    ImageView ivClear;

    @BindView(R.id.tv_bt_left)
    TextView tvCancel;

    @BindView(R.id.tv_bt_right)
    TextView tvSubmit;


    private MaximumPaymentThresholdListener listener;
    private String maximum;

    MaximumPaymentThresholdDialog(Context context, String maximum, MaximumPaymentThresholdListener listener) {
        super(context);
        this.listener = listener;
        this.maximum = maximum;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input_password);
    }

    @Override
    protected void initContentView() {

        if (getWindow() != null) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
        tvTitle.setText(TCUtils.getString(R.string.text_payment_threshold));

        tvContent.setText(TCUtils.getString(R.string.maximum_amount));

        tvCancel.setText(TCUtils.getString(R.string.text_cancel));

        etPass.setHint(TCUtils.getString(R.string.please_enter_the_maximum_amount));
        etPass.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        etPass.setText(TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FORMAT, maximum));
        TCUtils.checkLimitDecimalPlaces(false, etPass, TCConstant.LIMIT_DECIMAL_TWO, null);
        etPass.setSelection(etPass.getText().length());
    }

    @Override
    protected void onViewClick() {

        tvCancel.setOnClickListener(v -> onCancel());

        ivClear.setOnClickListener(v -> etPass.setText(""));

        tvSubmit.setOnClickListener(v -> finish());

        TCUtils.showHideRemoveIconInEditText(etPass, ivClear);

    }

    private void onCancel() {

        listener.onMaximumThresholdCancel();

        dismiss();
    }

    private void finish() {

        String max = etPass.getText().toString();

        if (!max.isEmpty()) {

            if (!TCUtils.checkLimitDecimalPlaces(max, TCConstant.LIMIT_DECIMAL_TWO)) {

                etPass.setError(TCUtils.getString(R.string.only_two_decimal_is_allowed));
            } else {

                etPass.setError(null);

                listener.onMaximumThresholdFinish(max);

                dismiss();
            }
        } else {

            etPass.setError(TCUtils.getString(R.string.please_enter_maximum));
        }
    }
}
