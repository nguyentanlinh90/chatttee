package com.teecoin.feature.reviewSystem.referralDetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.teecoin.R;
import com.teecoin.base.TCReviewBaseFragment;
import com.teecoin.feature.general.appflyer.TCAppFlyerTrackingEvent;
import com.teecoin.feature.reviewSystem.referralInfo.ReferralInfoScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.reviewsystem.ReferralHistoryItemModel;
import com.teecoin.model.reviewsystem.ReferralInformationModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserGetReferralInformationRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;
import core.view.RecycleListener;

public class ReferralDetailScreen extends TCReviewBaseFragment implements APIResponseListener, RecycleListener<ReferralHistoryItemModel> {

    @BindView(R.id.frg_referral_detail_tv_referral_code)
    TextView tv_referral_code;

    @BindView(R.id.frg_referral_detail_rcv_referral_detail)
    TCRecyclerView rcv_referral_detail;

    @BindView(R.id.frg_referral_detail_tv_total_reward)
    TextView tv_total_reward;

    private ArrayList<ReferralHistoryItemModel> historyItemModels;
    private ReferralInformationModel model;

    public static ReferralDetailScreen getInstance() {
        return new ReferralDetailScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_referral_detail, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        showButtonBackToolbar();
        showFooter();
        showTabMenuBottom();
        updateTitleHeader(TCUtils.getString(R.string.account_refer_a_friend));
        updateTitleRightHeader(TCUtils.getString(R.string.referral_Info));
        setOnClickListenerForTitleRightHeader(v -> {
            if (model != null && model.getSummary() != null) {
                addFragment(ReferralInfoScreen.getInstance(model.getSummary().getEvent_url()));
            }
        });
    }

    @Override
    public void onBindView() {
        fillData();
        initClickEvent();
    }

    private void fillData() {
//        AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);
        historyItemModels = new ArrayList<>();
        ReferralDetailListAdapter adapter = new ReferralDetailListAdapter(getLayoutInflater(), historyItemModels, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActiveActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_referral_detail.setLayoutManager(layoutManager);
        rcv_referral_detail.setAdapter(adapter);

        getReferralInformation(1);
        rcv_referral_detail.setOnLoadMoreListener(new TCRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getReferralInformation(rcv_referral_detail.getNextPageIndex());
            }

            @Override
            public boolean shouldOverrideRefresh() {
                return false;
            }
        });
    }

    private void getReferralInformation(int pageIndex) {
        requestApi(new ReviewUserGetReferralInformationRequest(pageIndex, this));
    }

    private void initClickEvent() {
        registerSingleClick(R.id.frg_referral_detail_rl_copy, R.id.frg_referral_detail_rl_share);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frg_referral_detail_rl_copy:
                TCAppFlyerTrackingEvent.getInstance().trackAccountReferralReferralCodeCopy();
                TCUtils.copyStringToClipboard(tv_referral_code.getText().toString());
                Toast.makeText(getActiveActivity(), R.string.referral_code_copied, Toast.LENGTH_LONG).show();
                break;
            case R.id.frg_referral_detail_rl_share:
                TCAppFlyerTrackingEvent.getInstance().trackAccountReferralReferralCodeShareSNS();
                shareContent();
                break;
        }
    }

    @Override
    public void onBaseDestroyView() {
        super.onBaseDestroyView();
        unregisterSingleClick(R.id.frg_referral_detail_rl_copy, R.id.frg_referral_detail_rl_share);
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == ReviewRequestTarget.GET_REFERRAL_INFORMATION||requestTarget == ReviewRequestTarget.GET_REFERRAL_INFORMATION_FOR_SHOP) {
            model = (ReferralInformationModel) response.getResult();
            if (model.getSummary() != null) {
                tv_referral_code.setText(model.getSummary().getReferral_code());
                tv_total_reward.setText(
                        String.format(TCUtils.getString(R.string.text_parameter_with_tec),
                                TCUtils.formatMoney(TCConstant.FIVE_DECIMAL_FORMAT, model.getSummary().getAmount())
                        ));
            }

            if (model.getHistory() != null && model.getHistory().getReferralHistoryItems() != null) {
                historyItemModels.addAll(model.getHistory().getReferralHistoryItems());
                rcv_referral_detail.onLoadMoreComplete();
                rcv_referral_detail.setLimit(model.getHistory().getCount());
                rcv_referral_detail.setNextPageIndex(model.getHistory().getNextPageIndex());
            }
        }
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

    }

    @Override
    public void onItemClick(View view, ReferralHistoryItemModel item, int position, EnumMgr.ClickType clickType) {
//        ReferralEventDialog eventDialog = new ReferralEventDialog(getActiveActivity(), new ReferralEventModel());
//        eventDialog.show();
    }

    private void shareContent() {
        if (model != null && model.getSummary() != null) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, TCUtils.getString(R.string.share_title));
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, model.getSummary().getShare_content());
            startActivity(Intent.createChooser(sharingIntent, TCUtils.getString(R.string.share_title)));
        }
    }
}
// {root}/v4/clients/{uuid}/referral_information/?page=1