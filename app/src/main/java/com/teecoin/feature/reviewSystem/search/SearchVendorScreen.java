package com.teecoin.feature.reviewSystem.search;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCReviewBaseFragment;
import com.teecoin.feature.couponSystem.user.catalogues.UserCatalogueListScreen;
import com.teecoin.feature.couponSystem.user.discoverVendor.DetailVendorCategoryScreen;
import com.teecoin.feature.couponSystem.user.discoverVendor.VendorCategoryScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.VendorCategoryModel;
import com.teecoin.model.reviewsystem.SuggestItemModel;
import com.teecoin.model.reviewsystem.SuggestModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.reviewsystem.GetKeySearchRecentRequest;
import com.teecoin.myapi.apirequest.reviewsystem.GetSuggestCouponRequest;
import com.teecoin.myapi.apirequest.reviewsystem.GetSuggestVendorRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;

import static com.teecoin.utils.TCUtils.getLanguageCode;

public class SearchVendorScreen extends TCReviewBaseFragment implements APIResponseListener {

    private static final String TYPE_SEARCH = "TYPE_SEARCH";

    @BindView(R.id.fragment_search_iv_back)
    ImageView ivBack;

    @BindView(R.id.fragment_search_et_search)
    EditText etInput;

    @BindView(R.id.fragment_search_iv_clear)
    ImageView ivClear;

    @BindView(R.id.fragment_search_rcv_suggest)
    TCRecyclerView rcvSuggest;

    @BindView(R.id.view_no_data_tv)
    TextView tvNoData;

    private ArrayList<SuggestItemModel> suggestList;

    private int typeSearch;


    public static SearchVendorScreen getInstance(int typeSearch) {
        SearchVendorScreen screen = new SearchVendorScreen();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE_SEARCH, typeSearch);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onBaseResume() {
        hideHeader();
        hideFooter();
    }

    @Override
    public void onBindView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            typeSearch = bundle.getInt(TYPE_SEARCH);
        }
        initView();

    }

    private void initView() {

        showKeyboard(etInput);

        onClick();

        onListener();

        initSuggestView();

        getKeySearchRecent();

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

                getSuggestSearch(s.toString());

            }
        };

        etInput.addTextChangedListener(textWatcher);

        etInput.setOnEditorActionListener((v, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                String keyword = etInput.getText().toString();

                //if (keyword.length() > 2) {

                hideKeyBoard();

                if (EnumMgr.SearchType.SearchVendor.getValue() == typeSearch) {

                    addFragment(VendorCategoryScreen.getInstance("", "", keyword, false));
                } else if (typeSearch == EnumMgr.SearchType.SearchCouponByCatalogue.getValue()) {
                    Intent intent = new Intent();
                    intent.putExtra(UserCatalogueListScreen.SEARCH_OPTION, keyword);
                    finishWithResult(RESULT_OK, intent);
                } else {
                    sendKeywordToSearch(keyword);
                }
                //}
            }

            return false;
        });
    }

    private void sendKeywordToSearch(String keyword) {

        setFinishedWithResult(true);
        Intent intent = new Intent();
        intent.putExtra(TCConstant.KEYWORD, keyword);
        setFinishedResult(intent);
        popFragment();
    }

    private void initSuggestView() {

        suggestList = new ArrayList<>();

        SearchVendorAdapter adapter = new SearchVendorAdapter(LayoutInflater.from(getActiveActivity()), suggestList,
                (view, item, position, clickType) ->
                {
                    if (typeSearch == EnumMgr.SearchType.SearchVendor.getValue()) {
                        if (TCUtils.isEmpty(item.getId())) {

                            //when click key search recent
                            hideKeyBoard();

                            addFragment(VendorCategoryScreen.getInstance("", "", item.getOption(), false));
                        } else {
                            for (int i = 0; i < getListCategorySort().size(); i++) {

                                if (getListCategorySort().get(i).getId().equals(item.getId())) {

                                    VendorCategoryModel categoryModel = new VendorCategoryModel(item.getId(), item.getCategory(), true);

                                    addFragment(DetailVendorCategoryScreen.getInstance(categoryModel, "", item.getCategory(), item.getOption(), false));
                                }
                            }
                        }
                    } else if (typeSearch == EnumMgr.SearchType.SearchCouponByCatalogue.getValue()) {
                        if (TCUtils.isEmpty(item.getId())) {
                            hideKeyBoard();
                            Intent intent = new Intent();
                            intent.putExtra(UserCatalogueListScreen.SEARCH_OPTION, item.getOption());
                            finishWithResult(RESULT_OK, intent);
                        }
                    } else {

                        sendKeywordToSearch(item.getOption());
                    }
                });

        rcvSuggest.setAdapter(adapter);

    }

    private void getKeySearchRecent() {

        requestApi(new GetKeySearchRecentRequest(this));
    }

    private void getSuggestSearch(String searchInput) {

        if (searchInput.length() > 2) {

            if (EnumMgr.SearchType.SearchVendor.getValue() == typeSearch) {
                new Handler().postDelayed(() ->
                        requestApi(new GetSuggestVendorRequest(searchInput, getCountryCodeModel().getCountry_code(), this)), 500L);
            } else {
                new Handler().postDelayed(() ->
                        requestApi(new GetSuggestCouponRequest(searchInput, getCountryCodeModel().getCountry_code(), this)), 500L);
            }
        } else {

            getKeySearchRecent();
        }
    }

    @SuppressLint("SetTextI18n")
    private void showViewNoData(boolean isNoData) {

        rcvSuggest.setVisibility(isNoData ? View.GONE : View.VISIBLE);

        tvNoData.setVisibility(isNoData ? View.VISIBLE : View.GONE);

        if (isNoData) {
            if (getLanguageCode().equals(EnumMgr.LanguageAppSetting.English.getValue())) {
                tvNoData.setText(Html.fromHtml(String.format(TCUtils.getString(R.string.no_suggest) +
                                "<b><font color = '#B2973F'>&nbsp;%s</font></b>"
                        , etInput.getText().toString())));
            } else {
                tvNoData.setText(Html.fromHtml(String.format("<b><font color = '#B2973F'>%s</font></b>" +
                                TCUtils.getString(R.string.no_suggest)
                        , etInput.getText().toString())));
            }
        }

    }

    private void showBlankData() {

        rcvSuggest.setVisibility(View.GONE);

        tvNoData.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {

        if (requestTarget == ReviewRequestTarget.GET_SUGGEST_VENDOR) {

            ArrayList<SuggestModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();

            if (null != list && list.size() > 0) {

                ArrayList<SuggestItemModel> listItem = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {

                    for (int j = 0; j < list.get(i).getOptions().size(); j++) {

                        listItem.add(new SuggestItemModel(list.get(i).getId(), list.get(i).getOptions().get(j), list.get(i).getName()));
                    }
                }

                suggestList.clear();

                suggestList.addAll(listItem);

                rcvSuggest.onLoadMoreComplete();

                showViewNoData(false);

            } else {
                showViewNoData(true);
            }
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
}
