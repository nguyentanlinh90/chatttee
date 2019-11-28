package com.teecoin.feature.general.popup;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.base.TCConfirmListener;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class DialogConfirmGeneral extends TCBaseDialog {
    @BindView(R.id.general_dialog_tv_title)
    TextView tv_title;
    @BindView(R.id.general_dialog_tv_message)
    TextView tv_message;
    @BindView(R.id.button_cancel_tv_cancel)
    TextView tv_cancel;
    @BindView(R.id.button_ok_tv_ok)
    TextView tv_ok;

    @BindView(R.id.view_cancel)
    View view_cancel;
    @BindView(R.id.view_text)
    TextView view_text;

    private String title = "";
    private String message = "";
    private String buttonLeft = "";
    private String buttonRight = "";
    private TCConfirmListener confirm_listener;

    public DialogConfirmGeneral(Context context, String title, String message, String buttonLeft, String buttonRight, TCConfirmListener confirm_listener) {
        super(context);
        this.title = title;
        this.message = message;
        this.buttonLeft = buttonLeft;
        this.buttonRight = buttonRight;
        this.confirm_listener = confirm_listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm_general);
    }

    @Override
    protected void initContentView() {
        tv_title.setText(title);
        tv_message.setText(message);

        if (TCUtils.isEmpty(buttonLeft)) {
            view_cancel.setVisibility(View.GONE);
            view_text.setVisibility(View.GONE);
        } else {
            tv_cancel.setText(buttonLeft);
        }

        tv_ok.setText(buttonRight);
        setCancelable(false);

    }

    @Override
    protected void onViewClick() {
        tv_cancel.setOnClickListener(v -> dismiss());
        tv_ok.setOnClickListener(v -> {
            if (confirm_listener != null) {
                confirm_listener.onConfirmed(1, null);
            }
            dismiss();
        });
    }
}
