package com.teecoin.feature.general.notification;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.general.DataPushNotificationModel;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class NotificationAdapter extends RecycleAdapter<DataPushNotificationModel> {
    NotificationAdapter(LayoutInflater inflater, ArrayList<DataPushNotificationModel> items, RecycleListener<DataPushNotificationModel> listener) {
        super(inflater, items, listener);

    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return NotificationViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_list_notification;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void bindItemView(ItemViewHolder<DataPushNotificationModel> holder, DataPushNotificationModel data, int position) {
        if (holder instanceof NotificationViewHolder) {
            NotificationViewHolder viewHolder = (NotificationViewHolder) holder;
            viewHolder.ll_parent.setBackgroundColor(data.isIs_read() ? TCUtils.getColor(R.color.c_ececec) : TCUtils.getColor(R.color.c_ffffff));
            viewHolder.tv_title.setText(data.getTitle());
            viewHolder.tv_title.setTypeface(viewHolder.tv_title.getTypeface(),
                    data.isIs_read() ? Typeface.NORMAL : Typeface.BOLD);
            viewHolder.tv_message.setText(data.getContent());
            viewHolder.tv_date.setText(TCDateUtility.convertToCurrentTimeZoneDate(data.getCreated(),
                    TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                    TCUtils.getDateFormatByLanguageCode(TCDateUtility.DateFormatDefinition.DD_MM_YYYY_HH_MM)));

            if (!TCUtils.isEmpty(data.getIcon())) {
                Glide.with(getActiveActivity()).load(data.getIcon()).into(viewHolder.iv_icon);
            } else {
                setIcon(data, viewHolder);
            }

        }
    }

    public void updateMarkAll() {
        if (items != null) {
            for (DataPushNotificationModel data : items) {
                data.setIs_read(true);
            }
            notifyDataSetChanged();
        }
    }

    private void setIcon(DataPushNotificationModel data, NotificationViewHolder viewHolder) {
        if (data.getType().equals(EnumMgr.PushNotification.Tip.getValue())) {
                viewHolder.iv_icon.setImageDrawable(TCUtils.getDrawable(R.drawable.ic_gift_gold));
            } else if (data.getType().equals(EnumMgr.PushNotification.Promotion.getValue()) || data.getType().equals(EnumMgr.PushNotification.Referral.getValue())
                    || data.getType().equals(EnumMgr.PushNotification.VendorDetail.getValue()) || data.getType().equals(EnumMgr.PushNotification.VendorCouponList.getValue())
                    || data.getType().equals(EnumMgr.PushNotification.CategoryCatalogue.getValue()) || data.getType().equals(EnumMgr.PushNotification.UserRegister.getValue()) || data.getType().equals(EnumMgr.PushNotification.Event.getValue())) {
                viewHolder.iv_icon.setImageDrawable(TCUtils.getDrawable(R.drawable.ic_notification_gold));
            } else if (data.getType().equals(EnumMgr.PushNotification.Coupon.getValue())
                    || data.getType().equals(EnumMgr.PushNotification.CatalogueCouponByCountry.getValue())) {
                viewHolder.iv_icon.setImageDrawable(TCUtils.getDrawable(R.drawable.ic_gift_coin));
            } else if (data.getType().equals(EnumMgr.PushNotification.CheckInCoupon.getValue())) {
                viewHolder.iv_icon.setImageDrawable(TCUtils.getDrawable(R.drawable.ic_coupon_checkin));
            } else if (data.getType().equals(EnumMgr.PushNotification.TransferMoney.getValue())) {
                viewHolder.iv_icon.setImageDrawable(TCUtils.getDrawable(R.drawable.ic_coin_in));
            } else {
                viewHolder.iv_icon.setImageDrawable(TCUtils.getDrawable(
                        data.getType().equals(EnumMgr.PushNotification.Payment.getValue()) ?
                                R.drawable.ic_coin_in : R.drawable.ic_coin_out));
            }
    }
}
