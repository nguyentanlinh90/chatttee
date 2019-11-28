package com.teecoin.feature.reviewSystem.vendorDetail;

import android.view.LayoutInflater;
import android.view.View;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.HoursVendorModel;
import com.teecoin.model.reviewsystem.OpenHoursVendorModel;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

public class TimesOpenHorizontalAdapter extends RecycleAdapter<HoursVendorModel> {

    private boolean isCurrentDayOfWeek;

    private ArrayList<OpenHoursVendorModel> listDay;

    TimesOpenHorizontalAdapter(LayoutInflater inflater, ArrayList<OpenHoursVendorModel> listDay, ArrayList<HoursVendorModel> items, boolean isCurrentDayOfWeek, RecycleListener<HoursVendorModel> listener) {
        super(inflater, items, listener);

        this.isCurrentDayOfWeek = isCurrentDayOfWeek;

        this.listDay = listDay;
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return TimesOpenHorizontalViewHolder.class;
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_times;
    }

    @Override
    protected void bindItemView(ItemViewHolder<HoursVendorModel> holder, HoursVendorModel data, int position) {

        if (holder instanceof TimesOpenHorizontalViewHolder) {

            TimesOpenHorizontalViewHolder viewHolder = (TimesOpenHorizontalViewHolder) holder;

            if (data.getOpen_time() != null && data.getClose_time() != null) {

                viewHolder.tv_time_open.setText(String.format("%s - %s", data.getOpen_time(), data.getClose_time()));

                viewHolder.tv_time_open.setTextColor(
                        TCUtils.getColor(listDay.size() == 1 ?
                                R.color.c_b2973f :
                                isCurrentDayOfWeek ? R.color.c_b2973f : R.color.c_9698a2));

//                if (listDay.size() == 1) {// // if list is not show full
//
//                    viewHolder.tv_time_open.setTextColor(TCUtils.getColor(R.color.c_b2973f));
//
//                } else {
//
//                    if (isCurrentDayOfWeek) {
//
//                        viewHolder.tv_time_open.setTextColor(TCUtils.getColor(R.color.c_b2973f));
//
//                    } else {
//
//                        viewHolder.tv_time_open.setTextColor(TCUtils.getColor(R.color.c_2d2d2d));
//                    }
//                }
            }

            if ((data.getOpen_time().equals(TCUtils.getString(R.string.text_time_zero)) && data.getClose_time().equals(TCUtils.getString(R.string.text_time_zero)))
                    || (data.getOpen_time().equals(data.getClose_time()))) {
                viewHolder.tv_time_full.setVisibility(View.VISIBLE);
                viewHolder.tv_time_open.setVisibility(View.GONE);
                if (listDay.size() == 1) {// if list is not show full
                    viewHolder.tv_time_open.setTextColor(TCUtils.getColor(R.color.c_b2973f));
                } else {
                    viewHolder.tv_time_full.setTextColor(TCUtils.getColor(
                            isCurrentDayOfWeek ? R.color.c_b2973f : R.color.c_9698a2));
//                    if (isCurrentDayOfWeek) {
//
//                        viewHolder.tv_time_full.setTextColor(TCUtils.getColor(R.color.c_b2973f));
//
//                    } else {
//
//                        viewHolder.tv_time_full.setTextColor(TCUtils.getColor(R.color.c_2d2d2d));
//
//                    }
                }
            }
        }
    }
}