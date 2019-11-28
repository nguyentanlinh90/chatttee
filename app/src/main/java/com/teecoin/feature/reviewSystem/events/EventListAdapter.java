package com.teecoin.feature.reviewSystem.events;

import android.view.LayoutInflater;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.EventModel;

import java.util.ArrayList;

import core.view.ItemViewHolder;
import core.view.RecycleAdapter;
import core.view.RecycleListener;

public class EventListAdapter extends RecycleAdapter<EventModel> {


    public EventListAdapter(LayoutInflater inflater, ArrayList<EventModel> items, RecycleListener<EventModel> listener) {
        super(inflater, items, listener);
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @Override
    protected Class<? extends ItemViewHolder> getItemViewHolderClass() {
        return EventViewHolder.class;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected int getItemLayoutResource() {
        return R.layout.item_event;
    }

    @Override
    protected void bindItemView(ItemViewHolder<EventModel> holder, EventModel data, int position) {
        if (holder instanceof EventViewHolder) {
            EventViewHolder viewHolder = (EventViewHolder) holder;
            viewHolder.tv_name.setText(data.getName());
            viewHolder.tv_date.setText(data.getDate());
        }
    }
}
