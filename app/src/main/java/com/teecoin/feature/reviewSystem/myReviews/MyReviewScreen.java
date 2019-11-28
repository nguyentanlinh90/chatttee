package com.teecoin.feature.reviewSystem.myReviews;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teecoin.R;
import com.teecoin.base.TCReviewBaseFragment;
import com.teecoin.model.reviewsystem.MyReviewsResponseModel;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;

public class MyReviewScreen extends TCReviewBaseFragment {
    @BindView(R.id.frg_myreviews_rcv)
    TCRecyclerView rcv;
    private ArrayList<MyReviewsResponseModel> list;
    private MyReviewAdapter adapter;

    public static MyReviewScreen getInstance() {
        return new MyReviewScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_reviews, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        showButtonBackToolbar();
        updateTitleHeader(TCUtils.getString(R.string.text_my_reviews).toUpperCase());
        hideFooter();
    }

    @Override
    public void onBindView() {
        list = new ArrayList<>();
        adapter = new MyReviewAdapter(LayoutInflater.from(getActiveActivity()), list, null);
        rcv.setLayoutManager(new LinearLayoutManager(getActiveActivity()));
        rcv.setAdapter(adapter);
        getData();
    }

    private void getData() {
        for (int i = 0; i < 15; i++) {
            MyReviewsResponseModel model = new MyReviewsResponseModel();
            MyReviewsResponseModel model1 = new MyReviewsResponseModel();

            model.setName("Clan Restaurant");
            model.setImage("https://media-cdn.tripadvisor.com/media/photo-s/07/fb/df/1d/o-an-japanese-restaurant.jpg");
            model.setReview_count("10");
            model.setRating("4");
            list.add(model);
            model1.setName("Kampong Chicken Rice");
            model1.setImage("https://tmbidigitalassetsazure.blob.core.windows.net/secure/RMS/attachments/37/1200x1200/Mint-Patty-Cake_exps140673_CMT2426390C08_17_2b_RMS.jpg");
            model1.setReview_count("19");
            model1.setRating("5");

            list.add(model1);
        }
        rcv.onLoadMoreComplete();
    }

}
