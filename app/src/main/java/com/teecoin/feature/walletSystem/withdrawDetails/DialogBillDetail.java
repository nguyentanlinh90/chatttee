package com.teecoin.feature.walletSystem.withdrawDetails;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;

import butterknife.BindView;

import static core.base.BaseApplication.getActiveActivity;

public class DialogBillDetail extends TCBaseDialog {
    @BindView(R.id.image)
    ImageView image;
    private   String url;
    public DialogBillDetail(Context context,String url) {
        super(context);
        this.url =url;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_image_bill_detail);
    }
    @Override
    protected void initContentView() {

        Glide.with(getActiveActivity()).load(url).into(image);
    }

    @Override
    protected void onViewClick() {

    }
}
