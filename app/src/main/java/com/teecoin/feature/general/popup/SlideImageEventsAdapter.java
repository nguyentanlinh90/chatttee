package com.teecoin.feature.general.popup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.general.EventsModel;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

public class SlideImageEventsAdapter extends PagerAdapter {
    private ArrayList<EventsModel> listData;
    private LayoutInflater inflater;
    private Context context;

    public SlideImageEventsAdapter(Context context, ArrayList<EventsModel> listData) {
        this.context = context;
        this.listData = listData;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.item_list_event_popup, view, false);
        view.addView(imageLayout, 0);
        if (imageLayout != null) {
            ImageView imageView = imageLayout.findViewById(R.id.iv_event);
            TextView tv_description = imageLayout.findViewById(R.id.tv_description);
            EventsModel event = listData.get(position);
            tv_description.setText(event.getDescription());
            Glide.with(context).load(TCUtils.isEmpty(event.getImage().getUrl()) ? TCUtils.getDrawable(R.drawable.ic_logo_chattee) : event.getImage().getUrl()).apply(TCUtils.optionsSquareImage()).into(imageView);
        }
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}
