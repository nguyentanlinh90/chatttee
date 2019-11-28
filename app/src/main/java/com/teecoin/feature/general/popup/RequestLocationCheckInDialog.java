package com.teecoin.feature.general.popup;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.feature.walletSystem.paymentThreshold.YesNoListener;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class RequestLocationCheckInDialog extends TCBaseDialog {
    @BindView(R.id.base_notify_tv_title)
    TextView base_notify_tv_title;

    @BindView(R.id.base_notify_tv_content)
    TextView base_notify_tv_content;

    @BindView(R.id.tv_cancel)
    TextView tv_cancel;

    @BindView(R.id.tv_ok)
    TextView tv_ok;

    private YesNoListener listener;

    public RequestLocationCheckInDialog(Context context, YesNoListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_request_location);
    }

    @Override
    protected void initContentView() {
        base_notify_tv_title.setText(String.format(TCUtils.getString(R.string.location_service_check_in_title),
                TCUtils.getApplicationName(getContext())));

        base_notify_tv_content.setText(String.format(TCUtils.getString(R.string.location_service_check_in_message),
                TCUtils.getApplicationName(getContext()), TCUtils.getApplicationName(getContext())));
    }

    @Override
    protected void onViewClick() {
        tv_cancel.setOnClickListener(v -> dismiss());

        tv_ok.setOnClickListener(v -> {
            listener.onSubmit();
            dismiss();
        });
    }
}
