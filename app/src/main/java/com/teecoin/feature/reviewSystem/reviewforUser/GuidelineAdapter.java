package com.teecoin.feature.reviewSystem.reviewforUser;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.model.general.GuidelineModel;
import com.teecoin.utils.TCUtils;

import java.util.List;

public class GuidelineAdapter extends PagerAdapter {
    private List<GuidelineModel> listData;
    private LayoutInflater inflater;
    private Context context;
//    private int screenWidth;
//    private int screenHeight;

    public GuidelineAdapter(Context context, List<GuidelineModel> listData) {
        this.context = context;
        this.listData = listData;
        inflater = LayoutInflater.from(context);
//        screenWidth = TCScreenSize.getWidth(getActiveActivity());
//        screenHeight = TCScreenSize.getHeight(getActiveActivity());
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
        View layout = inflater.inflate(R.layout.item_tutorial, view, false);
        if (layout != null) {
            GuidelineModel guidelineModel = listData.get(position);
            ImageView imageView = layout.findViewById(R.id.id_tutorial_iv_image);

            /*LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(screenWidth, (screenWidth * 9) / 15);
            lp.setMargins(0, (int) (screenHeight * 0.1), 0, 0);
            imageView.setLayoutParams(lp);*/

            TextView textViewTitle = layout.findViewById(R.id.id_tutorial_tv_title);
            TextView textViewContent = layout.findViewById(R.id.id_tutorial_tv_content);

            Glide.with(context).load(TCUtils.isEmpty(guidelineModel.getImage()) ? TCUtils.getDrawable(R.drawable.ic_logo_chattee) : guidelineModel.getImage()).apply(TCUtils.optionsSquareImage()).into(imageView);
            textViewTitle.setText(Html.fromHtml(guidelineModel.getTitle()));
            textViewContent.setText(Html.fromHtml(guidelineModel.getContent()));

        }
        view.addView(layout, 0);
        return layout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}

