package com.teecoin.feature.reviewSystem.vendorDetail;

import android.view.LayoutInflater;
import android.view.View;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.HoursVendorModel;
import com.teecoin.model.reviewsystem.OpenHoursVendorModel;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class TimesOpenAdapter extends RecycleAdapter<OpenHoursVendorModel> {

    public TimesOpenAdapter(LayoutInflater inflater, ArrayList<OpenHoursVendorModel> items, RecycleListener<OpenHoursVendorModel> listener) {
        super(inflater, items, listener);
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return TimesOpenAdapterViewHolder.class;
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_day_open_time;
    }

    @Override
    protected void bindItemView(ItemViewHolder<OpenHoursVendorModel> holder, OpenHoursVendorModel data, int position) {
        if (holder instanceof TimesOpenAdapterViewHolder) {

            TimesOpenAdapterViewHolder viewHolder = (TimesOpenAdapterViewHolder) holder;

            viewHolder.tv_title.setText(data.getDay());

            viewHolder.tv_title.setTextColor(TCUtils.getColor(
                    items.size() == 1 ? R.color.c_b2973f :
                            TCDateUtility.isCurrentDayOfWeek(position) ?
                                    R.color.c_b2973f : R.color.c_000000));

//            if (items.size() == 1) { // if list is not show full
//                viewHolder.tv_title.setTextColor(TCUtils.getColor(R.color.c_b2973f));
//
//            } else {
//                if (TCDateUtility.isCurrentDayOfWeek(position)) {
//
//                    viewHolder.tv_title.setTextColor(TCUtils.getColor(R.color.c_b2973f));
//
//                }else {
//
//                    viewHolder.tv_title.setTextColor(TCUtils.getColor(R.color.c_000000));
//                }
//            }

            if (data.getHours() != null) {

                ArrayList<HoursVendorModel> listHours = data.getHours();

                TimesOpenHorizontalAdapter adapter = new TimesOpenHorizontalAdapter(
                        LayoutInflater.from(getActiveActivity()), items, listHours, TCDateUtility.isCurrentDayOfWeek(position),
                        (view, item, pos, clickType) -> {

                        });
                viewHolder.rcv_day.setAdapter(adapter);

                if (listHours.size() == 0) {

                    viewHolder.tv_close.setVisibility(View.VISIBLE);

                    if (items.size() == 1) { // if list is not show full

                        // viewHolder.tv_close.setTextColor(TCUtils.getColor(R.color.c_b2973f));
                        viewHolder.tv_close.setTextColor(TCUtils.getColor(R.color.c_d0021b));

                    } else {

                        if (TCDateUtility.isCurrentDayOfWeek(position)) {

                            // viewHolder.tv_close.setTextColor(TCUtils.getColor(R.color.c_b2973f));
                            viewHolder.tv_close.setTextColor(TCUtils.getColor(R.color.c_d0021b));
                        }
                    }
                }else{
                    viewHolder.tv_close.setVisibility(View.GONE);
                }
            }
        }
    }
}