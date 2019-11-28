package com.teecoin.feature.reviewSystem.vendorReview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.BuildConfig;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCReviewBaseFragment;
import com.teecoin.feature.general.profileShop.ProfileShopScreen;
import com.teecoin.feature.reviewSystem.tip.DialogTipVendor;
import com.teecoin.feature.reviewSystem.tip.TipCallback;
import com.teecoin.feature.reviewSystem.vendor.VendorScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.reviewsystem.GoogleReviewModel;
import com.teecoin.model.reviewsystem.GoogleReviewResponseModel;
import com.teecoin.model.reviewsystem.ReviewVendorDetailModel;
import com.teecoin.model.reviewsystem.VendorDetailModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewGetGoogleReviewRequest;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewShopGetVendorReviewRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.ui.TCSwipeRefreshLayout;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCLog;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;

public class VendorReviewScreen extends TCReviewBaseFragment {
    @BindView(R.id.frg_vendor_review_rcv_list_review)
    TCRecyclerView rcv_list_review;
    @BindView(R.id.frg_vendor_review_srl_list_review)
    TCSwipeRefreshLayout srl_list_review;
    @BindView(R.id.view_header_review_list_iv_back)
    ImageView iv_back;
    @BindView(R.id.view_header_review_list_tv_review_title)
    TextView tv_review_title;
    @BindView(R.id.frg_vendor_review_v_review_header)
    View v_review_header;
    @BindView(R.id.v_back_top_top)
    FrameLayout view_back_to_top;
    @BindView(R.id.view_no_data_tv)
    View view_no_data_tv;



    private VendorDetailModel detailModel;
    private static final String VENDOR_DETAIL_MODEL = "VENDOR_DETAIL_MODEL";
    private static final String SHOW_TOOL_BAR = "SHOW_TOOL_BAR";
    private int pageLoad = 1;
    private ArrayList<ReviewVendorDetailModel> listReviews;
    private VendorReviewListAdapter adapterList;
    private boolean showToolbar;

    public static VendorReviewScreen newInstance(VendorDetailModel detailModel,boolean showToolbar) {// from vendor detail
        VendorReviewScreen screen = new VendorReviewScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(VENDOR_DETAIL_MODEL, detailModel);
        bundle.putSerializable(SHOW_TOOL_BAR, showToolbar);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vendor_review, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        hideHeader();
        v_review_header.setVisibility(BuildConfig.IS_APP_USER&&showToolbar ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onBindView() {
        if (getArguments() != null) {
            detailModel = (VendorDetailModel) getArguments().getSerializable(VENDOR_DETAIL_MODEL);
            showToolbar = getArguments().getBoolean(SHOW_TOOL_BAR);
        }

        v_review_header.setVisibility(BuildConfig.IS_APP_USER&&showToolbar ? View.VISIBLE : View.GONE);
        view_back_to_top.setVisibility(View.GONE);

        setupRecyclerView();
        registerSingleClick(
                R.id.v_back_top_top,R.id.view_header_review_list_iv_back
        );
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.v_back_top_top:
                //nestedScrollToTop(scroll_view);
                rcv_list_review.smoothScrollToPosition(0);
                break;
            case R.id.view_header_review_list_iv_back:
                ((TCMainActivity) getActiveActivity()).handleBackPressed();
                break;
        }
    }

    private void setupRecyclerView() {
        listReviews = new ArrayList<>();
        //for List
        rcv_list_review.setLayoutManager(new LinearLayoutManager(getActiveActivity()));
        adapterList = new VendorReviewListAdapter(LayoutInflater.from(getActiveActivity()),
                listReviews,false,
                (view, item, position, clickType) -> {
                    if (clickType == EnumMgr.ClickType.ReviewList_LikeClicked) {
                        clickLikeAndTip(item,position);
                    }else{
                        if(item.getImages()!=null&&item.getImages().size()>0){
                            addFragment(ReviewDetailScreen.newInstance(item, position,0));
                        }
                    }
                });
        rcv_list_review.setAdapter(adapterList);


        rcv_list_review.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                int  pastVisiblesItems =  ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();


                if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)//  to bottom
                {

                    TCUtils.setMarginRecyclerView(rcv_list_review,0,0,0,120);
                }else{
                    TCUtils.setMarginRecyclerView(rcv_list_review,0,0,0,0);
                }
                if(rcv_list_review.isLoading()||!rcv_list_review.isCanLoadMore())
                    return;

                if(dy > 0) //check for scroll down
                {
                    if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount-3)// near to bottom
                    {
                        getReviews();
                    }

                }
            }
        });

        getReviews();

    }



    private void getReviews() {
        rcv_list_review.setLoading(true);
        requestApi(new ReviewShopGetVendorReviewRequest(detailModel.getId(), pageLoad, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                ArrayList<ReviewVendorDetailModel> list = ((BaseResultsResponseModel<ReviewVendorDetailModel>) response.getResult()).getResults();
                if (list != null && list.size() > 0) {
                    pageLoad++;
                    listReviews.addAll(list);

                    for (ReviewVendorDetailModel reviewVendorDetailModel : listReviews) {
                        reviewVendorDetailModel.setName(detailModel.getName());
                    }
                    if (response.getResult() != null) {
                        if (!TCUtils.isEmpty(((BaseResultsResponseModel) response.getResult()).getReview_count())) {
                            tv_review_title.setText(String.format("%s (%s)", TCUtils.getString(R.string.text_review), ((BaseResultsResponseModel) response.getResult()).getReview_count()));
                        }
                    }
                }
                rcv_list_review.onLoadMoreComplete();
                if(((BaseResultsResponseModel<ReviewVendorDetailModel>)response.getResult()).getNext()!=null){
                    rcv_list_review.setCanLoadMore(true);
                }else{
                    rcv_list_review.setCanLoadMore(false);
                    getGoogleReview();
                }

                rcv_list_review.setLoading(false);
                displayData();
                // setTitle();
            }
            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                rcv_list_review.onLoadMoreComplete();
                rcv_list_review.setCanLoadMore(false);
                rcv_list_review.setLoading(false);
                getGoogleReview();
            }
        }));
    }

    public void updateItemList(int position) {
        if (adapterList != null) {
            adapterList.updateLikeItem(position);
        }
    }
    private void clickLikeAndTip(ReviewVendorDetailModel data,int position){
        boolean tipbefore = false;
        if (!TCUtils.isEmpty(data.getTip_of_user())) {
            float tip = Float.parseFloat(data.getTip_of_user());
            if (tip > 0)
                tipbefore = true;
        }
        if (!TCUtils.isMyselfReview(data.getAuthor().getPublic_key())) {
            showPopupTip(tipbefore,data, position);

        } else {
            ((TCMainActivity) getActiveActivity()).showBaseMessage(TCUtils.getString(R.string.tip_message_yourself));
        }
//        if (!data.isIs_like()) {
//            likeVendor(null, data, position);
//        }


    }
    private void showPopupTip(boolean tipbefore,ReviewVendorDetailModel data ,int position) {
        DialogTipVendor dialogTipVendor = new DialogTipVendor(getActiveActivity(), tipbefore, data.getAuthor().getPublic_key(), data.getId(), new TipCallback() {
            @Override
            public void tipAmount(float amount) {
                if (amount > 0) {
                    if (data.getTip_amount() != null) {
                        float tip_amount = Float.parseFloat(data.getTip_amount());
                        data.setTip_amount((amount + tip_amount) + "");
                    }
                    if (data.getTip_of_user() != null) {
                        float tip_user = Float.parseFloat(data.getTip_of_user());
                        data.setTip_of_user((amount + tip_user) + "");
                    }

                    if (!data.isIs_like()) {
                        if (!TCUtils.isEmpty(data.getLike_count())) {
                            data.setLike_count(String.valueOf(Integer.parseInt(data.getLike_count()) + 1));
                        }
                        data.setIs_like(true);
                    }
                    if(listReviews!=null&&listReviews.size()>0){
                        if(position<listReviews.size()){
                            listReviews.get(position).setTip_amount(data.getTip_amount());
                            listReviews.get(position).setTip_of_user(data.getTip_of_user());
                            listReviews.get(position).setLike_count(data.getLike_count());
                        }
                    }adapterList.notifyItemChanged(position);
                }
                if (!data.isIs_like()) {
                    likeVendor(null, data, position);
                }
            }
        });
        dialogTipVendor.show();
        dialogTipVendor.setCanceledOnTouchOutside(false);
    }

    private void getGoogleReview() {
        if (!TCUtils.isEmpty(detailModel.getPlace_id())) {
            requestApi(new ReviewGetGoogleReviewRequest(detailModel.getId(), new APIResponseListener() {
                @Override
                public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                    GoogleReviewResponseModel googleReviewModel = (GoogleReviewResponseModel) response.getResult();
                    if (googleReviewModel != null && googleReviewModel.getReviews() != null && googleReviewModel.getReviews().size() > 0) {
                        for (GoogleReviewModel google : googleReviewModel.getReviews()) {
                            listReviews.add(new ReviewVendorDetailModel(google));
                        }
                        rcv_list_review.onLoadMoreComplete();
                        if (listReviews.size() > 0) {
                            showGoogleReviewList(listReviews.size());
                        }
                        setTitle();
                    } else {
                        TCLog.d("GET GOOGLE REVIEW null  ");
                    }
                    rcv_list_review.setLoading(false);
                    displayData();
                }

                @Override
                public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                    rcv_list_review.setLoading(false);
                }
            }));
        }
    }

    private void showGoogleReviewList(int googleReviewCount) {
        if (getTopFragment() instanceof VendorScreen) {
            ((VendorScreen) getTopFragment()).showGoogleReviewList(googleReviewCount);
        }
        else if (getTopFragment() instanceof ProfileShopScreen) {
            ((ProfileShopScreen) getTopFragment()).showGoogleReviewList(googleReviewCount);
        }
    }

    private void setTitle() {
        tv_review_title.setText(String.format("%s (%s)", TCUtils.getString(R.string.text_reviews), listReviews.size()));
    }

    @Override
    public void onBaseDestroyView() {
        unregisterSingleClick(
                R.id.v_back_top_top, R.id.view_header_review_list_iv_back

        );
    }

    private void displayData() {
        view_no_data_tv.setVisibility(listReviews != null && listReviews.size() > 0
                ? View.GONE : View.VISIBLE);
    }
}
