package com.teecoin.feature.couponSystem.user.dailyReward;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.base.TCConfirmListener;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class PopupReciveTec extends TCBaseDialog {
    @BindView(R.id.tv_tec)
    TextView tv_tec;
    @BindView(R.id.tv_congratulation)
    TextView tv_congratulation;
    @BindView(R.id.tv_got_it)
    TextView tv_got_it;
    private String Amount = "0";
    private TCConfirmListener confirmListenerl;

    public PopupReciveTec(Context context, String Amount, TCConfirmListener confirmListenerl) {
        super(context);
        this.Amount = Amount;
        this.confirmListenerl = confirmListenerl;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_daily_reward_tec);
    }

    @Override
    protected void initContentView() {
        tv_tec.setText(String.format(TCUtils.getString(R.string.text_give_tec), TCUtils.formatMoney(TCConstant.SEVEN_DECIMAL_FORMAT, Amount)));
        // tv_congratulation.setText(Html.fromHtml(TCUtils.getString(R.string.daily_give_tec_you_just_won_tec)));
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onViewClick() {
        tv_got_it.setOnClickListener(v -> {
            dismiss();
            confirmListenerl.onConfirmed(1, null);

        });
    }
}
