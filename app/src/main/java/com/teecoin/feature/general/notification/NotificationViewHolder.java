package com.teecoin.feature.general.notification;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.general.DataPushNotificationModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class NotificationViewHolder extends ItemViewHolder<DataPushNotificationModel> {
    @BindView(R.id.frag_notify_item_ll_parent)
    LinearLayout ll_parent;
    @BindView(R.id.frag_notify_item_iv_icon)
    ImageView iv_icon;
    @BindView(R.id.frag_notify_item_tv_title)
    TextView tv_title;
    @BindView(R.id.frag_notify_item_tv_date)
    TextView tv_date;
    @BindView(R.id.frag_notify_item_tv_message)
    TextView tv_message;

    public NotificationViewHolder(View itemView) {
        super(itemView);
    }
}

