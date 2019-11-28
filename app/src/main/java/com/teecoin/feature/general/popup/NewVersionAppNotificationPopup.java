package com.teecoin.feature.general.popup;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;

import butterknife.BindView;

public class NewVersionAppNotificationPopup extends TCBaseDialog {
    @BindView(R.id.tv_update)
    TextView tv_update;
    private UpdateAppListener closeAppListener;

    public NewVersionAppNotificationPopup(Context context, UpdateAppListener closeAppListener) {
        super(context);
        this.closeAppListener = closeAppListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_new_version_app);
    }

    @Override
    protected void initContentView() {
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onViewClick() {
        tv_update.setOnClickListener(v -> closeAppListener.gotoUpdate());
        dialog.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                closeAppListener.onCloseApp();
                return true;
            }
            return false;
        });
    }
}
