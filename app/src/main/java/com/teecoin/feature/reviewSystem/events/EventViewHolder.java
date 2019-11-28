package com.teecoin.feature.reviewSystem.events;

import android.view.View;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.model.reviewsystem.EventModel;

import butterknife.BindView;
import core.view.ItemViewHolder;

public class EventViewHolder extends ItemViewHolder<EventModel> {

    @BindView(R.id.item_event_tv_name)
    TextView tv_name;

    @BindView(R.id.item_event_tv_date)
    TextView tv_date;

    public EventViewHolder(View itemView) {
        super(itemView);
    }


}
