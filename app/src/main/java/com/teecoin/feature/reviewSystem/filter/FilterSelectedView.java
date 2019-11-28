package com.teecoin.feature.reviewSystem.filter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teecoin.R;

public class FilterSelectedView extends LinearLayout {

    private FilterItem filterItem;

    public FilterSelectedView(Context context, FilterItem filterItem, DeleteFilteredItemListener listener) {
        super(context);
        this.filterItem = filterItem;
        init(context, listener);
    }


    private void init(Context context, DeleteFilteredItemListener listener) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.view_filter_selected_item, this);
        TextView tvName = rootView.findViewById(R.id.view_filter_selected_item_tv_name);
        tvName.setText(filterItem.getName());
        rootView.setOnClickListener(v -> listener.deleteFilterView(filterItem, this));
    }
}
