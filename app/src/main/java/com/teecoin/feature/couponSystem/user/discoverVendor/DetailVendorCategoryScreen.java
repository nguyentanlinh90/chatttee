package com.teecoin.feature.couponSystem.user.discoverVendor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCCouponBaseFragment;
import com.teecoin.feature.couponSystem.user.discover.FavouriteAndShareListener;
import com.teecoin.feature.couponSystem.user.filterDiscoverFlow.FilterDiscoverListener;
import com.teecoin.feature.couponSystem.user.filterDiscoverFlow.FilterDiscoverModel;
import com.teecoin.feature.couponSystem.user.filterDiscoverFlow.UserFilterDiscoverDialog;
import com.teecoin.feature.reviewSystem.search.SearchVendorAdapter;
import com.teecoin.feature.reviewSystem.searchLocation.NewSearchLocationScreen;
import com.teecoin.feature.reviewSystem.vendor.VendorScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.VendorCategoryModel;
import com.teecoin.model.reviewsystem.SuggestItemModel;
import com.teecoin.model.reviewsystem.VendorModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.reviewsystem.GetVendorListCheckinRequest;
import com.teecoin.myapi.apirequest.reviewsystem.VendorListRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;

public class DetailVendorCategoryScreen extends TCCouponBaseFragment implements FilterDiscoverListener, FavouriteAndShareListener, APIResponseListener, AppBarLayout.OnOffsetChangedListener {
    private static final String SORT_CONDITION = "SortCondition";
    private static final String CATEGORY_MODEL = "CategoryModel";
    private static final String TITLE_CATEGORY = "titleCategory";
    private static final String KEY_WORD = "Keyword";
    private static final String CHECKIN = "checkin";

    @BindView(R.id.view_search_all_iv_back)
    ImageView ivBack;

    @BindView(R.id.view_filter_number_rl_filter)
    View ivFilter;

    @BindView(R.id.frg_coupon_user_catalogue_appbar_layout)
    AppBarLayout appBar;

    @BindView(R.id.view_filter_number_tv_num_filter)
    TextView tvNumFilter;

    @BindView(R.id.fragment_discover_tv_title)
    TextView tvTitleCategory;

    @BindView(R.id.view_list_catalogue_rcv_image)
    TCRecyclerView rcvCategory;

    @BindView(R.id.frag_category_rcv_data)
    TCRecyclerView rcvVendor;

    @BindView(R.id.frg_coupon_user_catalogue_rcv_category_name)
    TCRecyclerView rcv_category_name;

    @BindView(R.id.frg_coupon_user_catalogue_view_category_name)
    View view_category_name;

    @BindView(R.id.floating_action_button)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.frag_discover_nested_scroll_view)
    NestedScrollView nestedScroll;

    @BindView(R.id.view_search_all_et_input)
    EditText etInput;

    @BindView(R.id.view_search_all_iv_clear)
    ImageView ivClear;

    @BindView(R.id.view_search_all_tv_cancel)
    TextView tvCancel;

    @BindView(R.id.view_no_data_tv)
    TextView tvNoData;

    @BindView(R.id.fragment_vendor_catalogue_rcv_suggest)
    TCRecyclerView rcvSuggest;

    private ArrayList<SuggestItemModel> suggestList;

    private ArrayList<VendorCategoryModel> categoryList;

    private ArrayList<VendorModel> listVendor;
    private ArrayList<VendorModel> listVendorCheckin;

    private VendorCategoryModel categorySelectModel;

    private DiscoverVendorCatalogueImageAdapter categoryAdapter;


    private DisCoverVendorCatalogueNameAdapter categoryNameAdapter;

    private DetailVendorCategoryAdapter detailVendorCategoryAdapter;

    private FilterDiscoverModel filterDiscoverModel;

    private String sortCondition = EnumMgr.SortCondition.NearMe.getValue();

    private String titleCategory;

    private String keyword;

    private boolean checkin;

    public static DetailVendorCategoryScreen getInstance(VendorCategoryModel categoryModel, String sortCondition, String titleCategory, String keyword,boolean checkin) {
        DetailVendorCategoryScreen screen = new DetailVendorCategoryScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CATEGORY_MODEL, categoryModel);
        bundle.putString(SORT_CONDITION, sortCondition);
        bundle.putString(TITLE_CATEGORY, titleCategory);
        bundle.putSerializable(KEY_WORD, keyword);
        bundle.putBoolean(CHECKIN, checkin);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vendor_catalogue, container, false);
    }

    @Override
    public void onBaseResume() {
        hideHeader();
        showFooter();
        showTabMenuBottom();
        if (null != filterDiscoverModel) filterDiscoverModel.setDistance("");
        appBar.addOnOffsetChangedListener(this);
    }
    @Override
    public void onStop() {
        super.onStop();
        appBar.removeOnOffsetChangedListener(this);
    }
    @Override
    public void onBindView() {
        Bundle bundle = getArguments();
        if (bundle != null) {

            titleCategory = bundle.getString(TITLE_CATEGORY);

            categorySelectModel = (VendorCategoryModel) bundle.getSerializable(CATEGORY_MODEL);

            sortCondition = bundle.getString(SORT_CONDITION);

            keyword = bundle.getString(KEY_WORD);

            checkin = bundle.getBoolean(CHECKIN);

        }

        setupRecyclerView();

        initSuggestView();

        initView();

        getListVendor(1);

    }

    private void setupRecyclerView() {

        categoryList = getListCategorySort();

        categoryAdapter = new DiscoverVendorCatalogueImageAdapter(LayoutInflater.from(getActiveActivity()), categoryList, (view, item, position, clickType) -> {

            categoryAdapter.unSelectedAllItem();
            categoryAdapter.setAllSelector(false);
            categoryAdapter.setSelectedItem(position);

            tvTitleCategory.setText(item.getName());

            categorySelectModel = item;
            sortData();

        });

        categoryNameAdapter = new DisCoverVendorCatalogueNameAdapter(LayoutInflater.from(getActiveActivity()), categoryList, (view, item, position, clickType) -> {
            categoryNameAdapter.unSelectedAllItem();
            categoryNameAdapter.setSelectedItem(position);
            categoryAdapter.unSelectedAllItem();
            categoryAdapter.setAllSelector(false);
            categoryAdapter.setSelectedItem(position);
            tvTitleCategory.setText(item.getName());
            categorySelectModel = item;
            sortData();

        });

        // categoryAdapter.unSelectedAllItem();

        rcvCategory.setAdapter(categoryAdapter);
        rcv_category_name.setAdapter(categoryNameAdapter);
        rcv_category_name.onLoadMoreComplete();

        listVendor = new ArrayList<>();
        listVendorCheckin = new ArrayList<>();

        detailVendorCategoryAdapter = new DetailVendorCategoryAdapter(LayoutInflater.from(getActiveActivity()), checkin?listVendorCheckin:listVendor,checkin, (view, item, position, clickType) -> {
            if (clickType == EnumMgr.ClickType.Vendor_Favourite) {

                submitFavourite(position, item, this);

            } else if (clickType == EnumMgr.ClickType.Vendor_Share) {
                getShareVendor(item);

            } else if (clickType == EnumMgr.ClickType.Vendor_WriteReview) {
                writeReview(item);

            } else {
                addFragment(VendorScreen.getInstance(item.getId()));
            }
        });

        rcvVendor.setAdapter(detailVendorCategoryAdapter);

        rcvVendor.setNextPageIndex(1);

        nestedScroll.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {

                if (rcvVendor.getNextPageIndex() > 1)
                    new Handler().postDelayed(() -> {
                        getListVendor(rcvVendor.getNextPageIndex());
                    }, 300);
            }
        });
    }

    private void sortData() {

        listVendor.clear();

        rcvVendor.setNextPageIndex(1);

        getListVendor(rcvVendor.getNextPageIndex());
    }

    private void initView() {

        ivFilter.setVisibility(View.VISIBLE);

        filterDiscoverModel = new FilterDiscoverModel();
        view_category_name.setAlpha( 0);
        if (categorySelectModel != null) {

            filterDiscoverModel.setCategoryModel(categorySelectModel);

            for (int i = 0; i < categoryList.size(); i++) {

                if (categorySelectModel.getId().equals(categoryList.get(i).getId())) {

                    categoryAdapter.setSelectedItem(i);

                    rcvCategory.scrollToPosition(i);

                    break;
                }
            }
        }
        if (!TCUtils.isEmpty(sortCondition)) {
            if (sortCondition.equals(EnumMgr.ParamToSortCondition.TopRate.getValue())) {

                filterDiscoverModel.setSort_condition(EnumMgr.SortCondition.TopRatedExperiences.getValue());
            } else {

                filterDiscoverModel.setSort_condition(EnumMgr.SortCondition.NearMe.getValue());
            }
        } else {

            sortCondition = EnumMgr.SortCondition.NearMe.getValue();

            filterDiscoverModel.setSort_condition(EnumMgr.SortCondition.NearMe.getValue());
        }

        tvTitleCategory.setText(titleCategory);

        if (!TCUtils.isEmpty(keyword)) {

            etInput.setText(keyword);

            ivClear.setVisibility(View.VISIBLE);

        }
        TCUtils.setNumberFilter(filterDiscoverModel, categorySelectModel, tvNumFilter);

        onListener();

        onClick();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void onListener() {

        TCUtils.scrollOnTouchListener(nestedScroll, rcvSuggest);

        TCUtils.etSearchTextChangedListener(EnumMgr.SearchType.SearchVendor.getValue(), etInput, ivClear, tvCancel, suggestList, rcvSuggest);

        TCUtils.etOnFocusChangeListener(etInput, tvCancel, suggestList, rcvSuggest);

        etInput.setOnEditorActionListener((v, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                String textSearch = etInput.getText().toString();

               // if (textSearch.length() > 2) {

                    hideKeyBoard();

                    addFragment(VendorCategoryScreen.getInstance("", "", textSearch,checkin));

                //}
            }

            return false;
        });
    }

    private void initSuggestView() {

        suggestList = new ArrayList<>();

        SearchVendorAdapter adapter = new SearchVendorAdapter(LayoutInflater.from(getActiveActivity()), suggestList,
                (view, item, position, clickType) ->
                {
                    if (TCUtils.isEmpty(item.getId())) {
                        //when click key search recent
                        hideKeyBoard();

                        addFragment(VendorCategoryScreen.getInstance("", "", item.getOption(),checkin));
                    } else {
                        for (int i = 0; i < getListCategorySort().size(); i++) {

                            if (getListCategorySort().get(i).getId().equals(item.getId())) {

                                VendorCategoryModel categoryModel = new VendorCategoryModel(item.getId(), item.getCategory(), true);

                                filterDiscoverModel.setCategoryModel(categoryModel);

                                filterDiscoverModel.setKeyword(item.getOption());

                                listVendor.clear();
                                listVendorCheckin.clear();

                                getListVendor(1);

                            }
                        }
                    }

                });

        rcvSuggest.setAdapter(adapter);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void onClick() {

//        ivBack.setOnClickListener(v ->
//        {
//            if (rcvSuggest.getVisibility() == View.VISIBLE) {
//                rcvSuggest.setVisibility(View.GONE);
//            } else {
//                finishWithResult(RESULT_OK, new Intent());
//            }
//        });
//
//        ivFilter.setOnClickListener(v -> showFilter());
//
//        floatingActionButton.setOnClickListener(v ->
//
//        {
//            filterDiscoverModel.setKeyword(etInput.getText().toString());
//            addFragment(NewSearchLocationScreen.getInstance(filterDiscoverModel, categorySelectModel));
//        });
//
//        ivClear.setOnClickListener(v -> {
//
//            etInput.setText("");
//
//            ivClear.setVisibility(View.GONE);
//
//            filterDiscoverModel.setKeyword("");
//
//            TCUtils.getKeySearchRecent(tvCancel, suggestList, rcvSuggest);
//
//        });
//
//        tvCancel.setOnClickListener(v -> {
//
//            tvCancel.setVisibility(View.GONE);
//
//            rcvSuggest.setVisibility(View.GONE);
//
//        });

        registerSingleClick(ivBack, ivFilter, floatingActionButton, ivClear, tvCancel);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.view_search_all_iv_back:
                if (rcvSuggest.getVisibility() == View.VISIBLE) {
                    rcvSuggest.setVisibility(View.GONE);
                } else {
                    finishWithResult(RESULT_OK, new Intent());
                }
                break;
            case R.id.view_filter_number_rl_filter:
                showFilter();
                break;
            case R.id.floating_action_button:
                filterDiscoverModel.setKeyword(etInput.getText().toString());
                addFragment(NewSearchLocationScreen.getInstance(filterDiscoverModel, categorySelectModel));
                break;
            case R.id.view_search_all_iv_clear:
                etInput.setText("");
                ivClear.setVisibility(View.GONE);
                filterDiscoverModel.setKeyword("");
                TCUtils.getKeySearchRecent(tvCancel, suggestList, rcvSuggest);
                break;
            case R.id.view_search_all_tv_cancel:
                tvCancel.setVisibility(View.GONE);
                rcvSuggest.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onBaseDestroyView() {
        super.onBaseDestroyView();
        registerSingleClick(ivBack, ivFilter, floatingActionButton, ivClear, tvCancel);
    }

    private void getListVendor(int pageIndex) {

        filterDiscoverModel.setKeyword(etInput.getText().toString());

        if(checkin){
            requestApi(new GetVendorListCheckinRequest(TCUtils.paramsGetVendorList(pageIndex, TCConstant.PAGE_SIZE_10, getLocation(), null,
                    getCountryCodeModel().getCountry_code(), filterDiscoverModel.getKeyword(),
                    filterDiscoverModel.getDistance(), filterDiscoverModel.getOpen_date(), categorySelectModel.getId(),
                    filterDiscoverModel.getPrice_range(), filterDiscoverModel.getSort_condition(),
                    filterDiscoverModel.getRatings()), this));
        }else{
            requestApi(new VendorListRequest(TCUtils.paramsGetVendorList(pageIndex, TCConstant.PAGE_SIZE_10, getLocation(), null,
                    getCountryCodeModel().getCountry_code(), filterDiscoverModel.getKeyword(),
                    filterDiscoverModel.getDistance(), filterDiscoverModel.getOpen_date(), categorySelectModel.getId(),
                    filterDiscoverModel.getPrice_range(), filterDiscoverModel.getSort_condition(),
                    filterDiscoverModel.getRatings()), this));
        }


    }

    private void showFilter() {

        UserFilterDiscoverDialog filterCategories = new UserFilterDiscoverDialog(getActiveActivity(), false,
                categoryList, filterDiscoverModel, sortCondition, this);
        filterCategories.show(getChildFragmentManager(), "FilterCategoriesDialog");
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == ReviewRequestTarget.GET_VENDOR_LIST) {
            ArrayList<VendorModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();
            if (list != null & list.size() > 0) {

                listVendor.addAll(list);

                rcvVendor.onLoadMoreComplete();

                rcvVendor.setLimit(((BaseResultsResponseModel) response.getResult()).getCount());

                TCUtils.setNetPageIndexRecycleVew(response, rcvVendor);

                showViewNoData(false);

            } else {
                showViewNoData(true);
            }

        } else if (requestTarget == ReviewRequestTarget.GET_LIST_CHECKIN_REWARDS) {
            ArrayList<VendorModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();
            if (list != null & list.size() > 0) {

                listVendorCheckin.addAll(list);

                rcvVendor.onLoadMoreComplete();

                rcvVendor.setLimit(((BaseResultsResponseModel) response.getResult()).getCount());

                TCUtils.setNetPageIndexRecycleVew(response, rcvVendor);

                showViewNoData(false);

            } else {
                showViewNoData(true);
            }

        }

    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        showViewNoData(true);
    }

    @Override
    public void onFilter(FilterDiscoverModel filterDiscoverModel, ArrayList<VendorCategoryModel> listCategory) {
        categorySelectModel = filterDiscoverModel.getCategoryModel();
        categoryList = new ArrayList<>();
        categoryList.addAll(listCategory);
        rcvCategory.onLoadMoreComplete();

        if (listVendor != null && listVendor.size() > 0) {
            listVendor.clear();
            rcvVendor.onLoadMoreComplete();
        }
        TCUtils.setNumberFilter(filterDiscoverModel, categorySelectModel, tvNumFilter);
        getListVendor(1);
    }

    @Override
    public void submitFavoriteSuccess(int position, VendorModel vendorModel) {
        detailVendorCategoryAdapter.setStateIconFavorite(position, !vendorModel.isFavorite());
    }

    private void showViewNoData(boolean isNoData) {

        rcvVendor.setVisibility(isNoData ? View.GONE : View.VISIBLE);

        tvNoData.setVisibility(isNoData ? View.VISIBLE : View.GONE);

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        handelScrollingCategory(appBarLayout,verticalOffset,rcvCategory,view_category_name);

    }
}
