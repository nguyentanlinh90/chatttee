package com.teecoin.feature.couponSystem.user.recommendCoupons;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCCouponBaseFragment;
import com.teecoin.feature.couponSystem.user.catalogues.UserCatalogueListScreen;
import com.teecoin.feature.couponSystem.user.discoverVendor.DiscoverVendorCatalogueImageAdapter;
import com.teecoin.feature.reviewSystem.search.SearchVendorAdapter;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.UserCouponCatalogueModel;
import com.teecoin.model.couponsystem.VendorCategoryModel;
import com.teecoin.model.reviewsystem.SuggestItemModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.couponsystem.CouponUserGetCouponCataloguesRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;


public class RecommendCouponsScreen extends TCCouponBaseFragment {

    @BindView(R.id.fragment_recommend_coupons_categories_nested_scroll_view)
    NestedScrollView nestedScroll;

    @BindView(R.id.view_search_all_iv_back)
    ImageView ivBack;

    @BindView(R.id.view_search_all_et_input)
    EditText etInput;

    @BindView(R.id.view_search_all_iv_clear)
    ImageView ivClear;

    @BindView(R.id.view_search_all_tv_cancel)
    TextView tvCancel;

    @BindView(R.id.view_filter_number_rl_filter)
    View iv_filter;

    @BindView(R.id.view_no_data_tv)
    TextView tvNoData;

    @BindView(R.id.v_back_top_top)
    FrameLayout vBackToTop;

    @BindView(R.id.fragment_recommend_coupons_categories_rcv_sort_catalogue)
    TCRecyclerView rcvSortCatalogue;

    @BindView(R.id.fragment_recommend_coupons_categories_rcv_categories)
    TCRecyclerView rcvRecommendCoupons;

    @BindView(R.id.fragment_recommend_coupons_categories_rcv_suggest)
    TCRecyclerView rcvSuggest;
    String keyword = "";
    private ArrayList<SuggestItemModel> suggestList;
    private DiscoverVendorCatalogueImageAdapter sortCatalogueAdapter;
    private RecommendCouponsCategoriesAdapter recommendCouponsCategoriesAdapter;
    private ArrayList<UserCouponCatalogueModel> listRecommendCoupons;

    public static RecommendCouponsScreen getInstance() {
        return new RecommendCouponsScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recommend_coupons_categories, container, false);
    }

    @Override
    public void onBaseResume() {
        hideHeader();
        showFooter();
        showTabMenuBottom();
    }

    @Override
    public void onBindView() {
        iv_filter.setVisibility(View.GONE);
        initSuggestView();
        initList();
        onClick();
        onListener();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void onListener() {
        TCUtils.scrollOnTouchListener(nestedScroll, rcvSuggest);
        TCUtils.etSearchTextChangedListener(EnumMgr.SearchType.SearchCoupon.getValue(), etInput, ivClear, tvCancel, suggestList, rcvSuggest);
        TCUtils.etOnFocusChangeListener(etInput, tvCancel, suggestList, rcvSuggest);
        TCUtils.nestedOnScrollChangeListener(nestedScroll, vBackToTop);

        etInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String textSearch = etInput.getText().toString();
                if (textSearch.length() > 2) {
                    hideKeyBoard();
                    keyword = textSearch;
                    getData();
                }
            }
            return false;
        });
    }

    private void initSuggestView() {
        suggestList = new ArrayList<>();
        SearchVendorAdapter adapter = new SearchVendorAdapter(LayoutInflater.from(getActiveActivity()), suggestList,
                (view, item, position, clickType) -> {
                    hideKeyBoard();
                    keyword = item.getOption();
                    etInput.setText(item.getOption());
                    getData();
                });
        rcvSuggest.setAdapter(adapter);
    }

    private void onClick() {
        ivBack.setOnClickListener(v -> handleBackPressed());
        ivClear.setOnClickListener(v -> {
            etInput.setText("");
            ivClear.setVisibility(View.GONE);
            TCUtils.getKeySearchRecent(tvCancel, suggestList, rcvSuggest);
        });
        tvCancel.setOnClickListener(v -> {
            tvCancel.setVisibility(View.GONE);
            rcvSuggest.setVisibility(View.GONE);
        });
        vBackToTop.setOnClickListener(v -> nestedScrollToTop(nestedScroll));
    }

    private void initList() {
        sortCatalogueAdapter = new DiscoverVendorCatalogueImageAdapter(LayoutInflater.from(getActiveActivity()), getListCategorySort(), (view, item, position, clickType) -> {
            sortCatalogueAdapter.unSelectedAllItem();
            sortCatalogueAdapter.setSelectedItem(position);
            addFragmentForResult(EnumMgr.RequestCode.GOTO_COUPON_USER_CATALOGUE_LIST.getValue(), UserCatalogueListScreen.getInstance(getListCategorySort(), item, 1, EnumMgr.SortByTypeCoupon.Coupons.getValue()));

        });

        rcvSortCatalogue.setAdapter(sortCatalogueAdapter);
        listRecommendCoupons = new ArrayList<>();
        recommendCouponsCategoriesAdapter = new RecommendCouponsCategoriesAdapter(LayoutInflater.from(getActiveActivity()), listRecommendCoupons, (view, item, position, clickType) -> {
            if (clickType == EnumMgr.ClickType.ViewAll) {
                VendorCategoryModel vendorCategoryModel = new VendorCategoryModel(item.getId(), item.getName(), true);
                filterCategory(vendorCategoryModel);
            }
        });
        rcvRecommendCoupons.setAdapter(recommendCouponsCategoriesAdapter);
        getData();

    }

    private void getData() {

        requestApi(new CouponUserGetCouponCataloguesRequest(TCUtils.paramsGetCategoryCoupon(
                getLatLngCurrent(), getCountryCodeModel().getCountry_code(),
                EnumMgr.ParamToSortCondition.RecommendCouponCategory.getValue(), keyword, ""), new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                ArrayList<UserCouponCatalogueModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();
                if (listRecommendCoupons.size() > 0) listRecommendCoupons.clear();
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getCatalogues().size() == 0) {
                            list.remove(i);
                        }
                    }
                    listRecommendCoupons.addAll(list);
                    rcvRecommendCoupons.onLoadMoreComplete();
                }
                showViewNoData(listRecommendCoupons.size() < 1);
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                showViewNoData(true);
            }
        }));

    }

    private void filterCategory(VendorCategoryModel vendorCategoryModel) {
        if (vendorCategoryModel == null && getListCategorySort().size() == 0)
            return;
        sortCatalogueAdapter.setAllSelector(false);
        for (VendorCategoryModel couponCategoryModel : getListCategorySort()) {
            if (couponCategoryModel.getId().equals(vendorCategoryModel.getId())) {
                couponCategoryModel.setSelector(true);
                addFragmentForResult(EnumMgr.RequestCode.GOTO_COUPON_USER_CATALOGUE_LIST.getValue(), UserCatalogueListScreen.getInstance(getListCategorySort(), vendorCategoryModel, 1, EnumMgr.SortByTypeCoupon.Coupons.getValue()));
                return;
            }
        }
    }

    private void showViewNoData(boolean isShow) {
        tvNoData.setVisibility(isShow ? View.VISIBLE : View.GONE);
        rcvRecommendCoupons.setVisibility(isShow ? View.GONE : View.VISIBLE);
        showSuggestView(false);
    }

    private void showSuggestView(boolean isShow) {
        rcvSuggest.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void reloadPriceCoupon() {
        if (null != recommendCouponsCategoriesAdapter)
            recommendCouponsCategoriesAdapter.notifyDataSetChanged();
    }

}
