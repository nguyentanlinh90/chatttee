package com.teecoin.feature.reviewSystem.events;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teecoin.R;
import com.teecoin.base.TCReviewBaseFragment;
import com.teecoin.feature.reviewSystem.referralDetail.ReferralDetailScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.reviewsystem.EventModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserGetReferralInformationRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;
import core.view.RecycleListener;

public class EventScreen extends TCReviewBaseFragment implements APIResponseListener, RecycleListener<EventModel> {


    @BindView(R.id.frg_referral_detail_rcv_event_list)
    TCRecyclerView rcv_event_list;

    public static EventScreen getInstance() {
        return new EventScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        showButtonBackToolbar();
        hideFooter();
        showMenuNextBottom();
        updateTitleHeader(TCUtils.getString(R.string.events));
    }

    @Override
    public void onBindView() {
        fillData();
        initClickEvent();
    }

    private void fillData() {
        ArrayList<EventModel> eventModels = new ArrayList<EventModel>();
        eventModels.add(new EventModel("Referral event", "2018/01/01"));
        EventListAdapter adapter = new EventListAdapter(getLayoutInflater(), eventModels, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActiveActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_event_list.setLayoutManager(layoutManager);
        rcv_event_list.setAdapter(adapter);
    }

    private void initClickEvent() {
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == ReviewRequestTarget.GET_REFERRAL_INFORMATION) {
            addFragment(ReferralDetailScreen.getInstance());
        }
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

    }

    @Override
    public void onItemClick(View view, EventModel item, int position, EnumMgr.ClickType clickType) {
//        requestApi(null, true, RequestTarget.GET_REFERRAL_INFORMATION, this);//TODO: need to check again parameter null
        requestApi(new ReviewUserGetReferralInformationRequest(1, this));
    }
}
