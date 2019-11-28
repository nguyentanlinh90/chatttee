package com.teecoin.feature.reviewSystem.referralDetail;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.model.reviewsystem.ReferralEventModel;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class ReferralEventDialog extends TCBaseDialog {

    private static final String REFERRAL_EVENT = "REFERRAL_EVENT";

    @BindView(R.id.frg_referral_event_iv_banner)
    ImageView iv_banner;

    @BindView(R.id.frg_referral_event_tv_event_info)
    TextView tv_event_info;

    @BindView(R.id.frg_referral_event_tv_event_receive_date)
    TextView tv_event_receive_date;

    private ReferralEventModel referralEventModel;

    public ReferralEventDialog(Context context, ReferralEventModel referralEventModel) {
        super(context);
        this.referralEventModel = referralEventModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_referral_event);
        fillData();
    }

    @Override
    protected void initContentView() {

    }

    @Override
    protected void onViewClick() {
//        ll_bt_cancel.setOnClickListener(v->dismiss());
//        ll_bt_ok.setOnClickListener(v->validate());
    }


    private void fillData() {
        tv_event_info.setText(String.format(TCUtils.getString(R.string.referral_event_information),
                referralEventModel.getStart_date(),
                referralEventModel.getEnd_date(),
                referralEventModel.getReferral_amount()));

        tv_event_receive_date.setText(String.format("you will receive the reward from %s", referralEventModel.getRelease_date()));
    }
}
