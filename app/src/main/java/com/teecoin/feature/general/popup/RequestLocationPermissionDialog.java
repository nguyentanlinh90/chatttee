package com.teecoin.feature.general.popup;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.base.TCDecisionListener;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class RequestLocationPermissionDialog extends TCBaseDialog {
    @BindView(R.id.base_notify_tv_content)
    TextView base_notify_tv_content;
    @BindView(R.id.tv_cancel)
    TextView tv_cancel;
    @BindView(R.id.tv_ok)
    TextView tv_ok;
    private TCDecisionListener decisionListener;
    private Object onWhat;

    public RequestLocationPermissionDialog(Context context, TCDecisionListener decisionListener, Object onWhat) {
        super(context);
        this.decisionListener = decisionListener;
        this.onWhat = onWhat;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_request_location);
    }

    @Override
    protected void initContentView() {
        base_notify_tv_content.setText(String.format(TCUtils.getString(R.string.location_service_message), TCUtils.getApplicationName(getContext()), TCUtils.getApplicationName(getContext())));

        setCanceledOnTouchOutside(false);
//        setCancelable(false);
    }

    @Override
    protected void onViewClick() {
        tv_cancel.setOnClickListener(v -> {
            decisionListener.onNegativeButtonClicked(-1, onWhat);
            dismiss();
        });

        tv_ok.setOnClickListener(v -> {
            decisionListener.onPositiveButtonClicked(-1, onWhat);
            dismiss();
        });
    }
}
