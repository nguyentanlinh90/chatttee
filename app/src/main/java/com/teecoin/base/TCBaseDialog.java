package com.teecoin.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teecoin.R;

import core.base.BaseDialog;

public abstract class TCBaseDialog extends BaseDialog {
    private TextView tv_title_header;
    private RelativeLayout rl_header;
    protected Dialog dialog;

    public TCBaseDialog(Context context) {
        super(context);
        dialog = this;
    }

    public TCBaseDialog(Activity activity) {
        super(activity);
        dialog = this;
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = getLayoutInflater().inflate(layoutResID, null);
        //  view = getLayoutInflater().inflate(layoutResID, null);
//        unbinder = ButterKnife.bind(this, view);
        setupToolbar(view);
        super.setContentView(view);
        initContentView();
        onViewClick();
    }



    protected abstract void initContentView();

    protected abstract void onViewClick();

    private void setupToolbar(View view) {
        rl_header = view.findViewById(R.id.activity_main_rl_header);
        tv_title_header = view.findViewById(R.id.activity_main_tv_middle_title);
        registerSingleClick(R.id.activity_main_iv_bt_back);
        hideToolbar();// default dialog small screen, not tollbar
    }

    public void updateTitleBar(String text) {
        if (tv_title_header != null) {
            tv_title_header.setText(text);
        }
    }

    public void hideToolbar() {
        if (rl_header != null) {
            rl_header.setVisibility(View.GONE);
        }
    }

    public void showToolbar() {
        if (rl_header != null) {
            rl_header.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.activity_main_iv_bt_back:
                dismiss();
                break;
        }
    }

    public void setFullScreen(boolean toolbar) {
        if (toolbar) {
            showToolbar();
        }
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;// with
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;// height
        window.setAttributes(lp);
    }

}
