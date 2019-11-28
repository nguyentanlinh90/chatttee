package com.teecoin.feature.reviewSystem.listReviews;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nex3z.flowlayout.FlowLayout;
import com.teecoin.R;
import com.teecoin.base.TCReviewBaseFragment;
import com.teecoin.feature.reviewSystem.filter.FilterItem;
import com.teecoin.feature.reviewSystem.filter.FilterModel;
import com.teecoin.feature.reviewSystem.filter.FilterSelectedView;
import com.teecoin.feature.reviewSystem.vendor.VendorScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.reviewsystem.ListReviewsModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserGetReviewFilterRequest;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewUserGetReviewListRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;

public class ListReviewScreen extends TCReviewBaseFragment {
    @BindView(R.id.frg_review_rcv_list_review)
    TCRecyclerView rcv_list_review;
    @BindView(R.id.tv_list_empty)
    TextView tv_list_empty;
    @BindView(R.id.frg_list_review_fl_filters_selected)
    FlowLayout fl_filters_selected;
    @BindView(R.id.frag_list_review_scroll_view)
    NestedScrollView scroll_view;
    private int pageLoad = 1;
    private ArrayList<ListReviewsModel> listRes;
    private FilterModel filterModel;

    private ListReviewAdapter adapter;

    private boolean continueLoadMore = false;
    private boolean isFilter;

    public static ListReviewScreen getInstance() {
        return new ListReviewScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_reviews, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        showButtonBackToolbar();
        showFilterToolbar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideFilterToolbar();
    }

    @Override
    public void onBindView() {
        updateTitleHeader(TCUtils.getString(R.string.text_reviews).toUpperCase());
        initView();
    }

    private void initView() {
        listRes = new ArrayList<>();
        rcv_list_review.setLayoutManager(new LinearLayoutManager(getActiveActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new ListReviewAdapter(LayoutInflater.from(getActiveActivity()), listRes,
                (view, item, position, clickType) -> reviewClick(item, position, clickType));
        rcv_list_review.setAdapter(adapter);
        rcv_list_review.setNestedScrollingEnabled(false);
        scroll_view.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                //  Log.e(TAG, "Scroll DOWN");
            }
            if (scrollY < oldScrollY) {
                // Log.e(TAG, "Scroll UP");
            }

            if (scrollY == 0) {
                // Log.e(TAG, "TOP SCROLL");
            }

            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                //  Log.e(TAG, "BOTTOM SCROLL");
                if (continueLoadMore) {
                    new Handler().postDelayed(() -> {
                        pageLoad++;
                        if (isFilter) {
                            if (filterModel != null)
                                filterReview(filterModel);
                        } else {
                            getListReviews();
                        }
                    }, 300);
                }
            }
        });
        getListReviews();
    }

    public void filter(FilterModel filters) {
        pageLoad = 1;
        continueLoadMore = true;
        fl_filters_selected.removeAllViews();
        filterModel = filters;
        for (FilterItem filter : filterModel.getFilterList()) {
            FilterSelectedView v = new FilterSelectedView(getActiveActivity(), filter, (filterItem, needToRemoveView) -> {
                filterModel.remove(filter);
                fl_filters_selected.removeView(needToRemoveView);
                filterReview(filterModel);
            });
            fl_filters_selected.addView(v);
        }
        filterReview(filterModel);
    }

    public FilterModel getFilter() {
        return filterModel;
    }

    private void filterReview(FilterModel filterModel) {
        isFilter = true;
        filterModel.removePageUpdate(filterModel.getFilterList());// remove page
        filterModel.getFilterList().add(new FilterItem(FilterModel.Filter.Page, String.valueOf(pageLoad)));// todo updateTo page
        requestApi(new ReviewUserGetReviewFilterRequest(filterModel, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                ArrayList<ListReviewsModel> list = (ArrayList<ListReviewsModel>) ((BaseResultsResponseModel) response.getResult()).getResults();
                boolean hasItem = list != null && list.size() > 0;
                showListView(hasItem);
                if (hasItem) {
                    updateTitleCount(((BaseResultsResponseModel) response.getResult()).getCount());
                    if (isFilter && pageLoad == 1)
                        listRes.clear();
                    listRes.addAll(list);
                } else {
                    updateTitleCount(0);
                }
                rcv_list_review.onLoadMoreComplete();

                continueLoadMore = ((BaseResultsResponseModel) response.getResult()).getNext() != null;
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                updateTitleCount(0);
                listRes.clear();
                showListView(false);
                rcv_list_review.onLoadMoreComplete();
                continueLoadMore = false;
            }
        }));
        if (pageLoad == 1) {
            nestedScrollToTop(scroll_view);
        }
    }

    private void getListReviews() {
        isFilter = false;
        requestApi(new ReviewUserGetReviewListRequest(pageLoad,
                new APIResponseListener() {
                    @Override
                    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                        if (((BaseResultsResponseModel) response.getResult()).getResults() != null) {
                            ArrayList<ListReviewsModel> list = (ArrayList<ListReviewsModel>) ((BaseResultsResponseModel) response.getResult()).getResults();
                            boolean hasItem = list != null && list.size() > 0;
                            showListView(hasItem);
                            if (hasItem) {
                                updateTitleCount(((BaseResultsResponseModel) response.getResult()).getCount());
                                listRes.addAll(list);
                            } else {
                                updateTitleCount(0);
                            }
                        }
                        rcv_list_review.onLoadMoreComplete();
                        //check load more
                        continueLoadMore = ((BaseResultsResponseModel) response.getResult()).getNext() != null;
                    }

                    @Override
                    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                        updateTitleCount(0);
                        showListView(false);
                        rcv_list_review.onLoadMoreComplete();
                        continueLoadMore = false;
                    }
                }));
        if (pageLoad == 1) {
            nestedScrollToTop(scroll_view);
        }
    }

    private void reviewClick(ListReviewsModel data, int position, EnumMgr.ClickType clickType) {
        if (clickType == EnumMgr.ClickType.ReviewList_LikeClicked) {
            likeVendor(data, null, position);// todo: check liked
        } else if (clickType == EnumMgr.ClickType.Review_ShowReviewDetail) {

        } else {
            addFragment(VendorScreen.getInstance(data.getVendor().getId()));
        }
    }

    private void showListView(boolean hasItem) {
        rcv_list_review.setVisibility(hasItem ? View.VISIBLE : View.GONE);
        tv_list_empty.setVisibility(hasItem ? View.GONE : View.VISIBLE);
    }

    public void updateItemList(int position) {
        if (adapter != null) {
            adapter.updateLikeItem(position);
        }
    }

    private void updateTitleCount(int listCount) {
        updateTitleHeader(String.format("%s (%s)", TCUtils.getString(R.string.text_reviews).toUpperCase(), listCount));
    }
}