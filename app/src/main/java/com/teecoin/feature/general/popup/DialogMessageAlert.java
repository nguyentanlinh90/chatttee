package com.teecoin.feature.general.popup;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.utils.RippleBackground;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class DialogMessageAlert extends TCBaseDialog {

    @BindView(R.id.view_ripple_background_success)
    View vSuccess;

    @BindView(R.id.rip_coinback_success)
    RippleBackground ripSuccess;

    @BindView(R.id.view_result_icon_fail)
    View vFail;

    @BindView(R.id.base_dialog_success_tv_title)
    TextView tvTitle;

    @BindView(R.id.base_dialog_success_tv_message)
    TextView tvMessage;

    @BindView(R.id.ll_bt_ok)
    View vDone;

    @BindView(R.id.button_ok_tv_ok)
    TextView tvDone;

    private String contentMSG;

    private boolean isSuccess;

    public DialogMessageAlert(Context context, boolean isSuccess, String contentMSG) {
        super(context);

        this.contentMSG = contentMSG;

        this.isSuccess = isSuccess;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.base_dialog_success);
    }

    @Override
    protected void initContentView() {

        setFullScreen(true);

        if (isSuccess) {

            vFail.setVisibility(View.GONE);

            ripSuccess.startRippleAnimation();

            tvDone.setText(TCUtils.getString(R.string.text_done));

        } else {

            vSuccess.setVisibility(View.GONE);

            tvTitle.setText(TCUtils.getString(R.string.coupon_unsuccessful));
            tvTitle.setTextColor(TCUtils.getColor(R.color.c_d0021b));

        }

        tvMessage.setText(contentMSG);
    }

    @Override
    protected void onViewClick() {

        vDone.setOnClickListener(v -> dismiss());

    }
}
