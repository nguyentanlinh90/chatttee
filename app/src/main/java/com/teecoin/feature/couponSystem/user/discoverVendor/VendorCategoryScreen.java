package com.teecoin.feature.couponSystem.user.discoverVendor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCCouponBaseFragment;
import com.teecoin.feature.couponSystem.user.filterDiscoverFlow.FilterDiscoverModel;
import com.teecoin.feature.reviewSystem.search.SearchVendorAdapter;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.VendorCategoryModel;
import com.teecoin.model.reviewsystem.SuggestItemModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.reviewsystem.GetListCheckinRewardByCategory;
import com.teecoin.myapi.apirequest.reviewsystem.VendorByCategoryRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;

public class VendorCategoryScreen extends TCCouponBaseFragment implements APIResponseListener, AppBarLayout.OnOffsetChangedListener {

    private static final String TYPE_PARAM_TO_SORT = "TYPE_PARAM_TO_SORT";
    private static final String TITLE = "Title";
    private static final String KEYWORD = "KeyWord";
    private static final String CHECKIN = "checkin";

    @BindView(R.id.view_filter_number_rl_filter)
    View ivFilter;
    @BindView(R.id.view_filter_number_tv_num_filter)
    TextView tvNumFilter;
    @BindView(R.id.frg_coupon_user_catalogue_appbar_layout)
    AppBarLayout appBar;

    @BindView(R.id.view_search_all_iv_back)
    ImageView ivBack;

    @BindView(R.id.view_search_all_iv_clear)
    ImageView ivClear;

    @BindView(R.id.view_search_all_tv_cancel)
    TextView tvCancel;

    @BindView(R.id.fragment_discover_tv_title)
    TextView tvTitleCategory;

    @BindView(R.id.view_list_catalogue_rcv_image)
    TCRecyclerView rcvCategory;

    @BindView(R.id.frg_coupon_user_catalogue_rcv_category_name)
    TCRecyclerView rcv_category_name;

    @BindView(R.id.frg_coupon_user_catalogue_view_category_name)
    View view_category_name;

    @BindView(R.id.frag_category_rcv_data)
    TCRecyclerView rcvVendorCatalogue;

    @BindView(R.id.floating_action_button)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.frag_discover_nested_scroll_view)
    NestedScrollView nestedScroll;

    @BindView(R.id.view_search_all_et_input)
    EditText etInput;

    @BindView(R.id.view_no_data_tv)
    TextView tvNoData;

    @BindView(R.id.fragment_vendor_catalogue_rcv_suggest)
    TCRecyclerView rcvSuggest;

    private ArrayList<SuggestItemModel> suggestList;

    private ArrayList<com.teecoin.model.reviewsystem.VendorCategoryModel> listVendorCatalogue;
    private ArrayList<com.teecoin.model.reviewsystem.VendorCategoryModel> listVendorCheckinCatalogue;

    private DiscoverVendorCatalogueImageAdapter categoryAdapter;

    private DisCoverVendorCatalogueNameAdapter categoryNameAdapter;

    private String sortCondition = EnumMgr.SortCondition.NearMe.getValue();
    private String titleCategory = "";
    private String keyword = "";

    private VendorCategoryAdapter vendorCategoryAdapter;
    private VendorCategoryModel vendorCategoryModel;

    private FilterDiscoverModel filterDiscoverModel;
    private ArrayList<VendorCategoryModel> listCatalogueToSort;

    private boolean checkin;

    public static VendorCategoryScreen getInstance(String typeSort, String title, String keyword, boolean checkin) {
        VendorCategoryScreen screen = new VendorCategoryScreen();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE_PARAM_TO_SORT, typeSort);
        bundle.putString(TITLE, title);
        bundle.putString(KEYWORD, keyword);
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

            titleCategory = bundle.getString(TITLE);

            sortCondition = bundle.getString(TYPE_PARAM_TO_SORT);

            keyword = bundle.getString(KEYWORD);
            checkin = bundle.getBoolean(CHECKIN);

        }
        filterDiscoverModel = new FilterDiscoverModel();
        if (getVendorCategorySelected() != null) {
            filterDiscoverModel.setCategoryModel(getVendorCategorySelected());
        }
        filterDiscoverModel.setSort_condition(EnumMgr.SortCondition.NearMe.getValue());
        TCUtils.setNumberFilter(filterDiscoverModel, getVendorCategorySelected(), tvNumFilter);
        setupRecyclerView();

        initSuggestView();

        intView();

    }

    private void intView() {

        if (!TCUtils.isEmpty(keyword)) {

            categoryAdapter.unSelectedAllItem();

            etInput.setText(keyword);

            ivClear.setVisibility(View.VISIBLE);
        }

        onListener();

        onClick();
        ivFilter.setVisibility(View.GONE);
        floatingActionButton.setVisibility(View.GONE);
        tvTitleCategory.setText(TCUtils.isEmpty(titleCategory) ? TCUtils.getString(R.string.discover_experiences) : titleCategory);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void onListener() {

        TCUtils.scrollOnTouchListener(nestedScroll, rcvSuggest);

        TCUtils.etSearchTextChangedListener(EnumMgr.SearchType.SearchVendor.getValue(), etInput, ivClear, tvCancel, suggestList, rcvSuggest);

        TCUtils.etOnFocusChangeListener(etInput, tvCancel, suggestList, rcvSuggest);

//        TCUtils.nestedOnScrollChangeListener(nestedScroll, vBackToTop);

        etInput.setOnEditorActionListener((v, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                //if (textSearch.length() > 2) {

                new Handler().postDelayed(() -> rcvSuggest.setVisibility(View.GONE), 2000L);

                keyword = etInput.getText().toString();

                getVendor();

                //  }
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

                        addFragment(VendorCategoryScreen.getInstance("", "", item.getOption(), checkin));
                    } else {
                        for (int i = 0; i < getListCategorySort().size(); i++) {

                            if (getListCategorySort().get(i).getId().equals(item.getId())) {

                                VendorCategoryModel categoryModel = new VendorCategoryModel(item.getId(), item.getCategory(), true);

                                addFragment(DetailVendorCategoryScreen.getInstance(categoryModel, "", item.getCategory(), item.getOption(), checkin));

                            }
                        }
                    }

                });

        rcvSuggest.setAdapter(adapter);

    }

    private void onClick() {
        registerSingleClick(ivClear, tvCancel, ivBack);
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.view_search_all_iv_clear:
                etInput.setText("");
                ivClear.setVisibility(View.GONE);
                TCUtils.getKeySearchRecent(tvCancel, suggestList, rcvSuggest);
                break;
            case R.id.view_search_all_tv_cancel:
                tvCancel.setVisibility(View.GONE);
                rcvSuggest.setVisibility(View.GONE);
                break;
            case R.id.view_search_all_iv_back:
                handleBackPressed();
                break;
        }
    }

    private void setupRecyclerView() {
        listCatalogueToSort = getListCategorySort();
        categoryAdapter = new DiscoverVendorCatalogueImageAdapter(LayoutInflater.from(getActiveActivity()), listCatalogueToSort, (view, item, position, clickType) -> {
            if (item.isSelector())
                return;

            categoryAdapter.unSelectedAllItem();

            categoryAdapter.setSelectedItem(position);

            tvTitleCategory.setText(item.getName());
            addFragment(DetailVendorCategoryScreen.getInstance(item, sortCondition, item.getName(), keyword, checkin));
        });

        rcvCategory.setAdapter(categoryAdapter);
        rcvCategory.onLoadMoreComplete();

        categoryNameAdapter = new DisCoverVendorCatalogueNameAdapter(LayoutInflater.from(getActiveActivity()), listCatalogueToSort, (view, item, position, clickType) -> {
            if (item.isSelector())
                return;

            categoryAdapter.unSelectedAllItem();

            categoryAdapter.setSelectedItem(position);

            tvTitleCategory.setText(item.getName());
            addFragment(DetailVendorCategoryScreen.getInstance(item, sortCondition, item.getName(), keyword, checkin));
        });
        rcv_category_name.setAdapter(categoryNameAdapter);
        rcv_category_name.onLoadMoreComplete();

        listVendorCatalogue = new ArrayList<>();
        listVendorCheckinCatalogue = new ArrayList<>();
        vendorCategoryAdapter = new VendorCategoryAdapter(LayoutInflater.from(getActiveActivity()), checkin ? listVendorCheckinCatalogue : listVendorCatalogue, checkin,
                (view, item, position, clickType) -> {
                    if (clickType == EnumMgr.ClickType.ViewAll) {
                        fillCategory(item);
                    }
                }, null);

        rcvVendorCatalogue.setAdapter(vendorCategoryAdapter);
        TCUtils.setNumberFilter(filterDiscoverModel, getVendorCategorySelected(), tvNumFilter);
        getVendor();
    }


    private void getVendor() {
        hideKeyBoard();
        if (checkin) {
            requestApi(new GetListCheckinRewardByCategory(TCUtils.paramsGetVendorList(1, TCConstant.PAGE_SIZE_10, getLocation(), null, getCountryCodeModel().getCountry_code(),
                    keyword, "", "", "", "",
                    TextUtils.isEmpty(sortCondition) ? "" : sortCondition, ""), this));

        } else {
            requestApi(new VendorByCategoryRequest(TCUtils.paramsGetVendorList(1, TCConstant.PAGE_SIZE_10, getLocation(), null, getCountryCodeModel().getCountry_code(),
                    keyword, "", "", "", "",
                    TextUtils.isEmpty(sortCondition) ? "" : sortCondition, ""), this));
        }
    }

    private void fillCategory(com.teecoin.model.reviewsystem.VendorCategoryModel vendorCategoryModel) {

        if (getListCategorySort().size() == 0)
            return;

        categoryAdapter.setAllSelector(false);

        for (VendorCategoryModel couponCategoryModel : getListCategorySort()) {
            if (couponCategoryModel.getId().equals(vendorCategoryModel.getId())) {
                couponCategoryModel.setSelector(true);
                addFragment(DetailVendorCategoryScreen.getInstance(couponCategoryModel, sortCondition, couponCategoryModel.getName(), keyword, checkin));
            }
        }
    }

    private void showViewNoData(boolean isShow) {

        tvNoData.setVisibility(isShow ? View.VISIBLE : View.GONE);

        rcvVendorCatalogue.setVisibility(isShow ? View.GONE : View.VISIBLE);

        rcvSuggest.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == ReviewRequestTarget.GET_VENDOR_LIST_BY_CATEGORY) {
            ArrayList<com.teecoin.model.reviewsystem.VendorCategoryModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();
            if (listVendorCatalogue.size() > 0) listVendorCatalogue.clear();

            if (list.size() > 0) {

                for (int i = 0; i < list.size(); i++) {

                    if (list.get(i).getVendors().size() == 0) {

                        list.remove(i);
                    }
                }
                listVendorCatalogue.addAll(list);

                rcvVendorCatalogue.onLoadMoreComplete();
            }
            showViewNoData(listVendorCatalogue.size() < 1);
        } else if (requestTarget == ReviewRequestTarget.GET_LIST_CHECKIN_REWARDS_BY_CATEGORY) {
            ArrayList<com.teecoin.model.reviewsystem.VendorCategoryModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();
            if (listVendorCheckinCatalogue.size() > 0) listVendorCheckinCatalogue.clear();
            if (list.size() > 0) {

                for (int i = 0; i < list.size(); i++) {

                    if (list.get(i).getVendors().size() == 0) {

                        list.remove(i);
                    }
                }
                listVendorCheckinCatalogue.addAll(list);

                rcvVendorCatalogue.onLoadMoreComplete();
            }
            showViewNoData(listVendorCheckinCatalogue.size() < 1);
        }
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        showViewNoData(true);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        handelScrollingCategory(appBarLayout, verticalOffset, rcvCategory, view_category_name);
    }
}
