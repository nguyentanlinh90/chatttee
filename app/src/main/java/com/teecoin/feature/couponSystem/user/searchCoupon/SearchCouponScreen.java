package com.teecoin.feature.couponSystem.user.searchCoupon;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCReviewBaseFragment;
import com.teecoin.feature.couponSystem.user.catalogues.UserCatalogueListScreen;
import com.teecoin.feature.couponSystem.user.couponCatalogue.UserCouponCatalogueHorizontalAdapter;
import com.teecoin.feature.couponSystem.user.discoverVendor.DisCoverVendorCatalogueNameAdapter;
import com.teecoin.feature.couponSystem.user.discoverVendor.DiscoverVendorCatalogueImageAdapter;
import com.teecoin.feature.couponSystem.user.myCouponDetail.UserMyCouponDetailScreen;
import com.teecoin.feature.reviewSystem.search.SearchVendorAdapter;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.UserCouponCatalogueModel;
import com.teecoin.model.couponsystem.VendorCategoryModel;
import com.teecoin.model.reviewsystem.SuggestItemModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.couponsystem.CouponUserGetCouponCataloguesRequest;
import com.teecoin.myapi.apirequest.reviewsystem.GetKeySearchRecentRequest;
import com.teecoin.myapi.apirequest.reviewsystem.GetSuggestCouponRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.CouponRequestTarget;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.ui.TCSwipeRefreshLayout;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.KeyboardManager;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;


public class SearchCouponScreen extends TCReviewBaseFragment implements APIResponseListener, AppBarLayout.OnOffsetChangedListener {

    @BindView(R.id.fragment_search_view_root)
    View view_root;

    @BindView(R.id.fragment_search_iv_back)
    ImageView ivBack;

    @BindView(R.id.fragment_search_et_search)
    EditText etInput;

    @BindView(R.id.fragment_search_iv_clear)
    ImageView ivClear;

    @BindView(R.id.fragment_search_rcv_suggest)
    TCRecyclerView rcvSuggest;

    @BindView(R.id.fragment_search_view_coordinator_layout)
    View view_data;

    @BindView(R.id.frg_coupon_user_catalogue_refresh_view)
    TCSwipeRefreshLayout refreshView;

    @BindView(R.id.frg_coupon_user_catalogue_appbar_layout)
    AppBarLayout appBar;

    @BindView(R.id.frag_coupon_user_catalogue_ncv_catalogue)
    NestedScrollView scrollView;

    @BindView(R.id.view_coupon_user_catalogue_horizontal_list_rcv_catalogue_list)
    TCRecyclerView rcvCategoryCoupon;

    @BindView(R.id.view_list_catalogue_rcv_image)
    TCRecyclerView rcvCatalogueImage;

    @BindView(R.id.frg_coupon_user_catalogue_rcv_category_name)
    TCRecyclerView rcv_category_name;

    @BindView(R.id.frg_coupon_user_catalogue_view_category_name)
    View view_category_name;

    @BindView(R.id.view_no_data_tv)
    TextView tvNoData;


    private ArrayList<UserCouponCatalogueModel> listCoupon;
    private ArrayList<VendorCategoryModel> vendorCategoryList;

    private DiscoverVendorCatalogueImageAdapter vendorCategoryAdapter;
    private DisCoverVendorCatalogueNameAdapter categoryNameAdapter;

    private ArrayList<SuggestItemModel> suggestList;

    private String keyword = "";

    public static SearchCouponScreen getInstance() {
        SearchCouponScreen screen = new SearchCouponScreen();
        Bundle bundle = new Bundle();
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_coupon, container, false);
    }

    @Override
    public void onBaseResume() {
        //hideHeader();
        hideHeader();
        showFooter();
        appBar.addOnOffsetChangedListener(this);
    }

    @Override
    public void onBindView() {
        Bundle bundle = getArguments();

        initView();

    }

    @Override
    public void onStop() {
        super.onStop();
        appBar.removeOnOffsetChangedListener(this);
    }

    private void initView() {
        showKeyboard(etInput);
        onClick();

        onListener();

        initSuggestView();

        getKeySearchRecent();

        setupRecycler();
        view_category_name.setAlpha(0);
        refreshData();

    }

    public EditText getEtInput() {
        return etInput;
    }

    private void refreshData() {
        refreshView.setOnRefreshListener(() -> {

            //scrollView.setVisibility(View.GONE);

            // getData("");
            sendKeywordToSearch();

        });
    }

    private void setupRecycler() {
        listCoupon = new ArrayList<>();

        vendorCategoryList = new ArrayList<>();

        UserCouponCatalogueHorizontalAdapter adapterCoupon = new UserCouponCatalogueHorizontalAdapter(getActiveActivity(), LayoutInflater.from(getActiveActivity()),
                listCoupon, true,
                (view, item, position, clickType) -> {
                    if (clickType.equals(EnumMgr.ClickType.ViewAll)) {
                        VendorCategoryModel vendorCategoryModel = new VendorCategoryModel(item.getId(), item.getName(), true);
                        filterCategory(vendorCategoryModel);
                    }
                },
                (view, item, position, clickType) -> {
                    addFragmentForResult(EnumMgr.RequestCode.GOTO_COUPON_USER_MY_COUPON_DETAIL.getValue(), UserMyCouponDetailScreen.newInstance(item));
                });

        rcvCategoryCoupon.setAdapter(adapterCoupon);

        setupVendorCategory();

    }

    private void onClick() {

        ivBack.setOnClickListener(v -> handleBackPressed());

        ivClear.setOnClickListener(v -> {

            etInput.setText("");

            ivClear.setVisibility(View.GONE);

        });


    }

    private void onListener() {

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                ivClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                //keyword=s.toString();
                getSuggestSearch(s.toString());

            }
        };

        etInput.addTextChangedListener(textWatcher);

        etInput.setOnEditorActionListener((v, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                keyword = etInput.getText().toString();
                sendKeywordToSearch();
                //}
            }

            return false;
        });

        view_root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (new KeyboardManager().isKeyboardShown(view_root.getRootView())) {
                    view_data.setVisibility(View.GONE);
                    rcvSuggest.setVisibility(View.VISIBLE);
                } else {
                    view_data.setVisibility(View.VISIBLE);
                    rcvSuggest.setVisibility(View.GONE);
                }
            }
        });
    }

    private void sendKeywordToSearch() {
        etInput.setText(keyword);
        hideKeyBoard();
        requestApi(new CouponUserGetCouponCataloguesRequest(TCUtils.paramsGetCategoryCoupon(getLocation(), getCountryCodeModel().getCountry_code(), "", keyword, TCConstant.TAG_COUPONS), this));

    }

    private void updateUI() {
        rcvSuggest.setVisibility(View.GONE);
        view_data.setVisibility(View.VISIBLE);
        if (listCoupon != null && listCoupon.size() > 0) {
            rcvCategoryCoupon.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.GONE);
            // rcvCategoryCoupon.onLoadMoreComplete();
        } else {
            rcvCategoryCoupon.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }

    }

    private void initSuggestView() {

        suggestList = new ArrayList<>();

        SearchVendorAdapter adapter = new SearchVendorAdapter(LayoutInflater.from(getActiveActivity()), suggestList,
                (view, item, position, clickType) ->
                {
                    keyword = item.getOption();
                    sendKeywordToSearch();
                });

        rcvSuggest.setAdapter(adapter);

    }

    private void getKeySearchRecent() {

        requestApi(new GetKeySearchRecentRequest(this));
    }

    private void getSuggestSearch(String searchInput) {

        if (searchInput.length() > 2) {

            new Handler().postDelayed(() ->
                    requestApi(new GetSuggestCouponRequest(searchInput, getCountryCodeModel().getCountry_code(), this)), 500L);
        } else {

            getKeySearchRecent();
        }
    }


    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {

        if (requestTarget == ReviewRequestTarget.GET_SUGGEST_COUPON) {
            ArrayList<String> list = ((BaseResultsResponseModel) response.getResult()).getResults();

            if (null != list && list.size() > 0) {

                ArrayList<SuggestItemModel> listItem = new ArrayList<>();

                for (int i = 0; i < list.size(); i++) {

                    //when get key search recent: set param id and param categoryId is empty
                    listItem.add(new SuggestItemModel("", list.get(i), ""));
                }

                suggestList.clear();

                suggestList.addAll(listItem);

                rcvSuggest.onLoadMoreComplete();
            }
        } else if (requestTarget == CouponRequestTarget.COUPON_USER_GET_COUPON_CATALOGUES) {
            ArrayList<UserCouponCatalogueModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();

            if (list != null && list.size() > 0) {
                if (listCoupon.size() > 0)
                    listCoupon.clear();
                for (UserCouponCatalogueModel catalogueModel : list) {
                    if (catalogueModel.getCatalogues() != null && catalogueModel.getCatalogues().size() > 0) {
                        listCoupon.add(catalogueModel);
                    }
                }
                rcvCategoryCoupon.onLoadMoreComplete();

            }
            updateUI();
        } else {
            ArrayList<String> list = ((BaseResultsResponseModel) response.getResult()).getResults();

            if (null != list && list.size() > 0) {

                ArrayList<SuggestItemModel> listItem = new ArrayList<>();

                for (int i = 0; i < list.size(); i++) {

                    //when get key search recent: set param id and param categoryId is empty
                    listItem.add(new SuggestItemModel("", list.get(i), ""));
                }

                suggestList.clear();

                suggestList.addAll(listItem);

                rcvSuggest.onLoadMoreComplete();
            }
        }
        refreshView.setRefreshing(false);
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        if (requestTarget == CouponRequestTarget.COUPON_USER_GET_COUPON_CATALOGUES) {
            updateUI();
        }
    }

    private void setupVendorCategory() {
        if (null != getListCategorySort()) {
            vendorCategoryAdapter = new DiscoverVendorCatalogueImageAdapter(LayoutInflater.from(getActiveActivity()), vendorCategoryList, (view, item, position, clickType) -> {
                vendorCategoryAdapter.unSelectedAllItem();
                vendorCategoryAdapter.setSelectedItem(position);
                addFragmentForResult(EnumMgr.RequestCode.GOTO_COUPON_USER_CATALOGUE_LIST.getValue(), UserCatalogueListScreen.getInstance(vendorCategoryList, item, 2,EnumMgr.SortByTypeCoupon.Coupons.getValue()));
            });

            categoryNameAdapter = new DisCoverVendorCatalogueNameAdapter(LayoutInflater.from(getActiveActivity()), vendorCategoryList, (view, item, position, clickType) -> {
                categoryNameAdapter.unSelectedAllItem();
                categoryNameAdapter.setSelectedItem(position);
                addFragmentForResult(EnumMgr.RequestCode.GOTO_COUPON_USER_CATALOGUE_LIST.getValue(), UserCatalogueListScreen.getInstance(vendorCategoryList, item, 2,EnumMgr.SortByTypeCoupon.Coupons.getValue()));

            });

            rcvCatalogueImage.setAdapter(vendorCategoryAdapter);
            rcv_category_name.setAdapter(categoryNameAdapter);
            if (vendorCategoryList != null && vendorCategoryList.size() > 0) {
                vendorCategoryList.clear();
            }
            vendorCategoryList.addAll(getListCategorySort());
            vendorCategoryAdapter.setAllSelector(false);
            categoryNameAdapter.setAllSelector(false);
            rcvCatalogueImage.onLoadMoreComplete();
            rcv_category_name.onLoadMoreComplete();
        }
    }

    private void filterCategory(VendorCategoryModel vendorCategoryModel) {

        if (vendorCategoryModel == null && vendorCategoryList.size() == 0)
            return;
        vendorCategoryAdapter.setAllSelector(false);

        for (VendorCategoryModel couponCategoryModel : vendorCategoryList) {

            if (couponCategoryModel.getId().equals(vendorCategoryModel.getId())) {

                couponCategoryModel.setSelector(true);

                addFragmentForResult(EnumMgr.RequestCode.GOTO_COUPON_USER_CATALOGUE_LIST.getValue(), UserCatalogueListScreen.getInstance(vendorCategoryList, vendorCategoryModel, 2,EnumMgr.SortByTypeCoupon.Coupons.getValue()));

                return;
            }
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        float offsetAlpha = (appBarLayout.getY() / appBarLayout.getTotalScrollRange());

        rcvCatalogueImage.setAlpha(1 - (offsetAlpha * -1));

        if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
            // Collapsed
            view_category_name.setAlpha(1);

        } else if (verticalOffset == 0) {
            // Expanded
            // TCLog.e("Expanded " + verticalOffset);
            view_category_name.setAlpha(0);
        } else {
            // TCLog.e("Somewhere in between " + verticalOffset);
            // Somewhere in between
            if (verticalOffset < -160)
                view_category_name.setAlpha(1 + (offsetAlpha * -1));
            else if (verticalOffset > -160 && verticalOffset < -100) {
                view_category_name.setAlpha(0);
            }
        }

    }
}
