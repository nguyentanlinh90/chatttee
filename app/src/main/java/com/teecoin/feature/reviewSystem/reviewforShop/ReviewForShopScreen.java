package com.teecoin.feature.reviewSystem.reviewforShop;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.teecoin.R;
import com.teecoin.base.TCReviewBaseFragment;
import com.teecoin.feature.reviewSystem.vendor.ViewPagerAdapter;
import com.teecoin.feature.reviewSystem.vendorDetail.VendorDetailScreen;
import com.teecoin.feature.reviewSystem.vendorMap.VendorMapScreen;
import com.teecoin.feature.reviewSystem.vendorReview.VendorReviewScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.reviewsystem.GetVendorIdForShop;
import com.teecoin.model.reviewsystem.VendorDetailModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewShopGetVendorDetailRequest;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewShopGetVendorRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.stellar.StellarResponseListener;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.util.StringTokenizer;

import butterknife.BindView;

public class ReviewForShopScreen extends TCReviewBaseFragment implements StellarResponseListener {

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsing_toolbar;
    @BindView(R.id.frg_coupon_user_my_coupon_detail_toolbar)
    Toolbar toolbar;
    @BindView(R.id.frag_vendor_rl_rating_write_review_for_user)
    View view_info_user;
    //    @BindView(R.id.vendor_detail_shop_ll_info)
//    View view_info_shop;
    @BindView(R.id.frag_vendor_ll_write_review)
    View ll_write_review;
    @BindView(R.id.frag_vendor_rating_bar)
    RatingBar rating_bar;
    @BindView(R.id.frag_vendor_tabs)
    TabLayout tabs;
    @BindView(R.id.frag_vendor_view_pager_tab_menu)
    ViewPager PagerTabMenu;
    @BindView(R.id.frag_vendor_detail_tv_count_reviews)
    TextView tv_count_reviews;
    @BindView(R.id.frag_vendor_detail_iv_image)
    ImageView iv_image;


    private ViewPagerAdapter adapter;
    private AccountModel accountModel;
    private VendorDetailModel detailModel;

    public static ReviewForShopScreen getInstance() {
        ReviewForShopScreen screen = new ReviewForShopScreen();
        Bundle bundle = new Bundle();
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vendor, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        hideHeader();
        showFooter();
        showTabMenuBottom();
    }

    @Override
    public void onBindView() {
        view_info_user.setVisibility(View.GONE);
//        view_info_shop.setVisibility(View.VISIBLE);
        tabs.setupWithViewPager(PagerTabMenu);
        rating_bar.setIsIndicator(true);
        toolbar.setNavigationIcon(null);
        getVendorID();
        onClick();
        accountModel = new AccountModel(RealmController.getInstance().getData(AccountModel.class));
        stellarGetBalance(accountModel.getPublic_key(), true, this);
        checkDeviceTokenExists();
    }

    private void getVendorID() {
        requestApi(new ReviewShopGetVendorRequest(new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                if (response != null) {
                    GetVendorIdForShop data = (GetVendorIdForShop) (response).getResult();
                    if (data.getId() != null && !TCUtils.isEmpty(data.getId())) {
                        getData(data.getId());
                    }
                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                showBaseMessage(errorModel.getErrorMessage());
            }
        }));
    }

    private void getData(String id) {
        requestApi(new ReviewShopGetVendorDetailRequest(id, new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                detailModel = (VendorDetailModel) response.getResult();
                fillData();

                StringTokenizer tokens = new StringTokenizer(detailModel.getLocation(), ",");
                String lat = tokens.nextToken();
                String lng = tokens.nextToken();
                TCConstant.VENDOR_INFO = new String[]{detailModel.getAddress(), lat, lng};
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                showBaseMessage(errorModel.getErrorMessage());
            }
        }));
    }

    private void onClick() {

    }


    private void setupFragmentForViewPager() {
        if (isAdded()) {
            adapter = new ViewPagerAdapter(getChildFragmentManager());
            adapter.addFragment(VendorReviewScreen.newInstance(detailModel,false), "");
            adapter.addFragment(VendorDetailScreen.newInstance(detailModel), "");
            adapter.addFragment(VendorMapScreen.newInstance(detailModel), "");
            PagerTabMenu.setAdapter(adapter);
            PagerTabMenu.setOffscreenPageLimit(TCConstant.VIEW_PAGER_LIMIT_PAGE);

            PagerTabMenu.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    private void fillData() {
        collapsing_toolbar.setTitle("");
//        tv_shop_name.setText(detailModel.getName());
//        tv_shop_rating.setText(String.valueOf(detailModel.getRating()));
//        shop_rating_bar.setRating(detailModel.getRating());
//        tv_shop_review.setText(String.format("(%s %s)", detailModel.getReviewCount(), detailModel.getReviewCount() > 1 ? TCUtils.getString(R.string.text_reviews) : TCUtils.getString(R.string.text_review)).toLowerCase());
        Glide.with(getActiveActivity()).load(TCUtils.isEmpty(detailModel.getFeaturedImage()) ? TCUtils.getDrawable(R.drawable.ic_cover_chattee) : detailModel.getFeaturedImage()).apply(TCUtils.optionsSquareImage()).into(iv_image);
        // todo: check some fields
        setupFragmentForViewPager();
    }

    public void updateLikeItemList(int position) {
        if (adapter != null) {
            VendorReviewScreen vendorReviewScreen = (VendorReviewScreen)
                    adapter.getFragment(VendorReviewScreen.class.getCanonicalName());
            if (vendorReviewScreen != null) {
                vendorReviewScreen.updateItemList(position);
            }
        }
    }

    @Override
    public void onStellarSuccess(Object balance) {
        if (balance instanceof String) {
            accountModel.setBalance((String) balance);
            RealmController.getInstance().updateBalanceAccount(accountModel);
        }
    }

    @Override
    public void onStellarFail(Throwable t) {

    }
}
