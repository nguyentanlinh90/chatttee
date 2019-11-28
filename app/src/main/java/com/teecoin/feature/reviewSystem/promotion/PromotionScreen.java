package com.teecoin.feature.reviewSystem.promotion;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teecoin.R;
import com.teecoin.base.TCReviewBaseFragment;
import com.teecoin.feature.reviewSystem.reviewforUser.WebViewScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.reviewsystem.PromotionModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserGetPromotionRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.ui.GridSpacingItemDecoration;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;

public class PromotionScreen extends TCReviewBaseFragment {
    @BindView(R.id.frag_promotion_rcv_data)
    TCRecyclerView rcv_data;
    private ArrayList<PromotionModel> listData;
    private PromotionAdapter adapter;

    public static PromotionScreen getInstance() {
        return new PromotionScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_promotion, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        showButtonBackToolbar();
        hideFooter();
        hideFilterToolbar();
        updateTitleHeader(TCUtils.getString(R.string.review_promotion_title));
    }

    @Override
    public void onBindView() {
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        listData = new ArrayList<>();
        adapter = new PromotionAdapter(LayoutInflater.from(getActiveActivity()), listData, (
                view, item, position, clickType) -> addFragment(WebViewScreen.getInstance(item.getDetail_url(), EnumMgr.PushNotification.Promotion.getValue())), true);
        rcv_data.setNestedScrollingEnabled(false);
        rcv_data.setAdapter(adapter);
        int spanCount = TCConstant.COLUMN_TW0_IMAGE; //  columns
        int spacing = 50; // px
        rcv_data.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, true));
        loadData();
    }

    private void loadData() {
        requestApi(new ReviewUserGetPromotionRequest(new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                ArrayList<PromotionModel> list = ((BaseResultsResponseModel<PromotionModel>) response.getResult()).getResults();
                listData.addAll(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                showAlertDialog(View.NO_ID, TCUtils.getString(R.string.text_warning), TCUtils.getString(R.string.this_secret_key_is_not_correct), TCUtils.getString(R.string.text_ok), null, null);
            }
        }));
    }
}
