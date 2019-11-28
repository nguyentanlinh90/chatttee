package com.teecoin.feature.general.popup;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.base.TCConfirmListener;
import com.teecoin.model.general.EventsModel;
import com.teecoin.model.general.PushNotificationModel;
import com.teecoin.utils.EnumMgr;

import butterknife.BindView;

public class PopupNotification extends TCBaseDialog {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_image)
    ImageView iv_image;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.ll_bt_cancel)
    View bt_cancel;
    @BindView(R.id.ll_bt_read_more)
    View bt_read_more;
    private PushNotificationModel pushNotificationModel;
    private EventsModel eventModel;
    private TCConfirmListener confirmListener;

    public PopupNotification(Context context, PushNotificationModel pushNotificationModel, TCConfirmListener confirmListener) {
        super(context);
        this.pushNotificationModel = pushNotificationModel;
        this.confirmListener = confirmListener;

    }

    public PopupNotification(Context context, EventsModel eventModel, TCConfirmListener confirmListener) {
        super(context);
        this.eventModel = eventModel;
        this.confirmListener = confirmListener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_notification);
    }

    @Override
    protected void initContentView() {
        if (pushNotificationModel != null) {
            if (pushNotificationModel.getNotification() != null) {
                tv_title.setText(pushNotificationModel.getNotification().getTitle());
                tv_content.setText(pushNotificationModel.getNotification().getBody());
            }
        } else if (eventModel != null) {
            tv_title.setText(eventModel.getName());
            tv_content.setText(eventModel.getDescription());
            if (eventModel.getImage() != null) {
                if (eventModel.getImage().getUrl() != null) {
                    Glide.with(getContext()).load(eventModel.getImage().getUrl()).into(iv_image);
                }
            }
        }

    }

    @Override
    protected void onViewClick() {
        bt_cancel.setOnClickListener(v -> dismiss());
        bt_read_more.setOnClickListener(v -> dismiss());
        bt_read_more.setOnClickListener(v -> {
            dismiss();
            if (pushNotificationModel != null) {
                confirmListener.onConfirmed(Integer.parseInt(EnumMgr.PushNotification.Referral.getValue()), pushNotificationModel);
            } else {
                confirmListener.onConfirmed(Integer.parseInt(EnumMgr.PushNotification.Referral.getValue()), eventModel);
            }

        });
    }

}
