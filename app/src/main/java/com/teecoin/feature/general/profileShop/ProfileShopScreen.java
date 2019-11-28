package com.teecoin.feature.general.profileShop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCCouponBaseFragment;
import com.teecoin.feature.couponSystem.user.discover.FavouriteAndShareListener;
import com.teecoin.feature.general.appflyer.TCAppFlyerTrackingEvent;
import com.teecoin.feature.reviewSystem.userReviewShop.UserReviewShopScreen;
import com.teecoin.feature.reviewSystem.vendor.SlideImageVendorAdapter;
import com.teecoin.feature.reviewSystem.vendor.ViewPagerAdapter;
import com.teecoin.feature.reviewSystem.vendorDetail.VendorDetailScreen;
import com.teecoin.feature.reviewSystem.vendorDetailTabPhotos.VendorDetailTabPhotosScreen;
import com.teecoin.feature.reviewSystem.vendorReview.VendorReviewScreen;
import com.teecoin.feature.reviewSystem.vendorcoupon.VendorCouponScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.Vendor;
import com.teecoin.model.reviewsystem.ImagesVendor;
import com.teecoin.model.reviewsystem.VendorDetailModel;
import com.teecoin.model.reviewsystem.VendorModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.reviewsystem.GetImageVendorRequest;
import com.teecoin.myapi.apirequest.reviewsystem.ReviewShopGetVendorDetailRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.ReviewRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.ui.TCViewPager;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

public class ProfileShopScreen extends TCCouponBaseFragment implements FavouriteAndShareListener, APIResponseListener {

    @BindView(R.id.fra_vendor_view_parent)
    View view_parent;
    @BindView(R.id.view_header_vendor_info)
    AppBarLayout appBar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsing_toolbar;
    @BindView(R.id.frg_coupon_user_my_coupon_detail_toolbar)
    Toolbar toolbar;
    //    @BindView(R.id.vendor_detail_tv_name_shop)
//    TextView tv_name_shop;
    @BindView(R.id.vendor_detail_tv_rating)
    TextView tv_rating;
    @BindView(R.id.vendor_detail_rating_bar)
    MaterialRatingBar rating_bar;
    @BindView(R.id.vendor_detail_tv_review)
    TextView tv_review_count;
    @BindView(R.id.vendor_detail_iv_is_coupon)
    ImageView iv_have_coupon;
    @BindView(R.id.vendor_detail_iv_is_cash)
    ImageView iv_have_cash;


    @BindView(R.id.frag_vendor_ll_share_write_review_vertical)
    View ll_share_write_review_vertical;
    @BindView(R.id.frag_vendor_iv_write_review_vertical)
    ImageView iv_write_review;
    @BindView(R.id.frag_vendor_iv_share_vertical)
    ImageView iv_share;
    @BindView(R.id.frag_vendor_iv_favorite_vertical)
    ImageView iv_favorite;

    @BindView(R.id.frag_vendor_ll_share_write_review_horizontal)
    View ll_share_write_review_horizontal;
    @BindView(R.id.frag_vendor_iv_write_review_horizontal)
    ImageView iv_write_review_horizontal;
    @BindView(R.id.frag_vendor_iv_share_horizontal)
    ImageView iv_share_horizontal;
    @BindView(R.id.frag_vendor_iv_favorite_horizontal)
    ImageView iv_favorite_horizontal;

    @BindView(R.id.frag_vendor_ll_write_review_vertical)
    View vReview ;
    @BindView(R.id.frag_vendor_ll_write_share_vertical)
    View vShare ;
    @BindView(R.id.frag_vendor_ll_write_favorite_vertical)
    View vFavorite ;


    @BindView(R.id.frag_vendor_detail_tv_count_reviews)
    TextView tv_count_reviews;
    @BindView(R.id.vendor_detail_tv_name_vendor)
    TextView tv_name_vendor;
    @BindView(R.id.frag_vendor_tabs)
    TabLayout tabs;
    @BindView(R.id.frag_vendor_detail_vp_image)
    TCViewPager VPagerImages;
    @BindView(R.id.frag_vendor_view_pager_tab_menu)
    ViewPager VPagerTabMenu;
    @BindView(R.id.view_custom_tab_menu_vendor_tv_reviews)
    TextView view_custom_tab_menu_vendor_tv_reviews;
    @BindView(R.id.view_custom_tab_menu_vendor_tv_info)
    TextView view_custom_tab_menu_vendor_tv_info;
    @BindView(R.id.view_custom_tab_menu_vendor_tv_coupon)
    TextView view_custom_tab_menu_vendor_tv_coupon;
    @BindView(R.id.view_custom_tab_menu_vendor_tv_photos)
    TextView view_custom_tab_menu_vendor_tv_photos;
    //    @BindView(R.id.frag_vendor_review_circle_page)
//    CircleIndicatorPager circle_page;
    @BindView(R.id.frag_vendor_review_pager_indicator)
    ScrollingPagerIndicator pager_indicator;
    @BindView(R.id.ll_name_vendor)
    LinearLayout vNameVendor;
    @BindView(R.id.tv_get_deals_now)
    TextView tv_get_deals_now;

    private VendorDetailModel detailModel;
    private ViewPagerAdapter adapter;
    private Vendor vendor;
    private ArrayList<ImagesVendor> listImageVendor;
    private SlideImageVendorAdapter imageVendorAdapter;
    private boolean loadPhoto;
    private int pagePhoto=1;
    private  int tabPosition=0;

    public static ProfileShopScreen getInstance() {
        return  new ProfileShopScreen();
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
        vendor = RealmController.getInstance().getData(Vendor.class);
        tabs.setupWithViewPager(VPagerTabMenu);
        toolbar.setNavigationIcon(null);
        vFavorite.setVisibility(View.GONE);
        iv_favorite_horizontal.setVisibility(View.GONE);
        vReview.setVisibility(View.GONE);
        iv_write_review_horizontal.setVisibility(View.GONE);

        initSlideImageVendor();
        ll_share_write_review_horizontal.setVisibility(View.GONE);

        registerSingleClick(R.id.tv_get_deals_now,R.id.frag_vendor_iv_write_review_vertical,R.id.frag_vendor_iv_share_vertical,
                R.id.frg_coupon_user_my_coupon_detail_toolbar,R.id.frag_vendor_iv_favorite_vertical,R.id.frag_vendor_iv_write_review_horizontal,
                R.id.frag_vendor_iv_share_horizontal,R.id.frag_vendor_iv_favorite_horizontal);

        handleAppBarScrolling();
        tv_get_deals_now.setVisibility(View.GONE);
        getData();
        checkDeviceTokenExists();
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frag_vendor_iv_write_review_vertical:
                gotoWriteReview();
                break;
            case R.id.frag_vendor_iv_write_review_horizontal:
                gotoWriteReview();
                break;
            case R.id.frag_vendor_iv_share_vertical:
                getShareVendor(detailModel);
                break;
            case R.id.frag_vendor_iv_share_horizontal:
                getShareVendor(detailModel);
                break;
            case R.id.frag_vendor_iv_favorite_vertical:
                submitFavourite(0, detailModel, this);
                break;
            case R.id.frag_vendor_iv_favorite_horizontal:
                submitFavourite(0, detailModel, this);
                break;
            case R.id.tv_get_deals_now:
                openTabCoupon();
                break;
//            case R.id.frg_coupon_user_my_coupon_detail_toolbar:
//                ((TCMainActivity) getActiveActivity()).handleBackPressed();
//                break;
        }
    }

    @Override
    public void onBaseDestroyView() {
        super.onBaseDestroyView();
        unregisterSingleClick( R.id.frag_vendor_iv_write_review_vertical, R.id.tv_get_deals_now,R.id.frag_vendor_iv_share_vertical,
                R.id.frg_coupon_user_my_coupon_detail_toolbar,R.id.frag_vendor_iv_favorite_vertical,R.id.frag_vendor_iv_write_review_horizontal,
                R.id.frag_vendor_iv_share_horizontal,R.id.frag_vendor_iv_favorite_horizontal);
    }

    private void initSlideImageVendor(){
        listImageVendor = new ArrayList<>();
        imageVendorAdapter = new SlideImageVendorAdapter(getActiveActivity(),listImageVendor);
        VPagerImages.setAdapter(imageVendorAdapter);
        //circle_page.setViewPager(VPagerImages);
        VPagerImages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(loadPhoto){
                    if(listImageVendor!=null&&listImageVendor.size()>0){
                        if(position==listImageVendor.size()-1){
                            pagePhoto+=1;
                            getImageSlideBanner();
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void gotoWriteReview() {
        TCAppFlyerTrackingEvent.getInstance().trackReviewWriteReviewButton();
//        addFragmentForResult(EnumMgr.RequestCode.GOTO_USER_REVIEW_SHOP.getValue(), UserReviewShopScreen.getInstance(detailModel));
//        getReviewStatus();
        addFragmentForResult(EnumMgr.RequestCode.GOTO_USER_REVIEW_SHOP.getValue(),
                UserReviewShopScreen.getInstance(detailModel));
    }


    @Override
    public void onPostResumeWithResult(int requestCode, int finishedResultCode, Intent finishedResult) {
        if (requestCode == EnumMgr.RequestCode.GOTO_USER_REVIEW_SHOP.getValue() && finishedResultCode == RESULT_OK) {
            if(listImageVendor!=null&&listImageVendor.size()>0)
                listImageVendor.clear();
            if (detailModel != null) {//after write a review, back to VendorDetail should increase review count
                //detailModel.setReviewCount(detailModel.getReviewCount() + 1);
            }
            getData();
        }
    }


    private void getData() {
        if (detailModel != null) {
            fillData();

        }  else if (vendor!=null) {
            requestApi(new ReviewShopGetVendorDetailRequest(vendor.getId(),  this));
        }

    }

    private void setupFragmentForViewPager() {
        if (isAdded()) {
            tv_get_deals_now.setVisibility(View.GONE);
            adapter = new ViewPagerAdapter(getChildFragmentManager());
            adapter.addFragment(VendorDetailScreen.newInstance(detailModel), "") ;
            adapter.addFragment(VendorReviewScreen.newInstance(detailModel, false), "");
            adapter.addFragment(VendorDetailTabPhotosScreen.newInstance(detailModel), "");
            adapter.addFragment(VendorCouponScreen.getInstance(detailModel), "");
            VPagerTabMenu.setAdapter(adapter);
            VPagerTabMenu.setOffscreenPageLimit(TCConstant.VIEW_PAGER_LIMIT_PAGE_THREE);
            view_custom_tab_menu_vendor_tv_info.setSelected(true);
            VPagerTabMenu.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    tabPosition =position;
                    view_custom_tab_menu_vendor_tv_info.setSelected(position == 0);
                    view_custom_tab_menu_vendor_tv_reviews.setSelected(position == 1);
                    view_custom_tab_menu_vendor_tv_photos.setSelected(position == 2);
                    view_custom_tab_menu_vendor_tv_coupon.setSelected(position == 3);
                    //tv_get_deals_now.setVisibility(position==0?View.VISIBLE:View.GONE);

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }
    }

    private void fillData() {
        getImageSlideBanner();
        // collapsing_toolbar.setTitle(detailModel.getName());
        collapsing_toolbar.setTitle("");
        tv_name_vendor.setText(detailModel.getName());
        rating_bar.setRating(detailModel.getRating());
        iv_favorite.setSelected(detailModel.isFavorite());
        iv_favorite_horizontal.setSelected(detailModel.isFavorite());
        iv_have_cash.setVisibility(detailModel.isHaveCashVoucher() ? View.VISIBLE : View.GONE);
        iv_have_coupon.setVisibility(detailModel.isHaveCatalogueCoupon() ? View.VISIBLE : View.GONE);
        tv_count_reviews.setText(detailModel.getReviewCount() > 0 ? TCUtils.getString(R.string.text_zero) : String.valueOf(detailModel.getReviewCount()));
        tv_rating.setText(String.valueOf(detailModel.getRating()));
        tv_review_count.setText(String.format("(%d %s)", detailModel.getReviewCount(), TCUtils.getString(TCUtils.checkNumbers(String.valueOf(detailModel.getReviewCount())) ? R.string.text_reviews : R.string.text_review)));

        setupFragmentForViewPager();

    }


    public void showGoogleReviewList(int count) {
        if (count > 0) {
            tv_count_reviews.setText(String.valueOf(count));
        }
    }

    @Override
    public void submitFavoriteSuccess(int position, VendorModel vendorModel) {
        if (detailModel != null) {
            detailModel.setFavorite(!vendorModel.isFavorite());
            iv_favorite.setSelected(detailModel.isFavorite());
            iv_favorite_horizontal.setSelected(detailModel.isFavorite());
        }

    }

    public void openTabCoupon() {
        if (adapter != null && VPagerTabMenu != null) {
            VPagerTabMenu.setCurrentItem(3);
        }
    }
    public void openTabReviews() {
        if (adapter != null && VPagerTabMenu != null) {
            VPagerTabMenu.setCurrentItem(1);
        }
    }

    private void handleAppBarScrolling(){
        appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            //TODO: need to check again
            appBar.post(new Runnable() {
                @Override
                public void run() {
                    //TCLog.d("hung: check=" + String.valueOf(Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()));
                    if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                        // Collapsed
                        vNameVendor.setBackgroundColor(TCUtils.getColor(R.color.c_00000000));
                        tv_name_vendor.setTextColor(TCUtils.getColor(R.color.c_ffffff));

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(60,0,0,0);
                        tv_name_vendor.setLayoutParams(params);
                        tv_rating.setLayoutParams(params);

                        tv_review_count.setTextColor(TCUtils.getColor(R.color.c_ffffff));
                        tv_rating.setTextColor(TCUtils.getColor(R.color.c_ffffff));
                        ll_share_write_review_vertical.setVisibility(View.GONE);
                        ll_share_write_review_horizontal.setVisibility(View.VISIBLE);
                        VPagerImages.setPagingEnabled(false);
                        pager_indicator.setVisibility(View.GONE);
                        // TCLog.e("Collapsed "+verticalOffset);
                    } else if (verticalOffset == 0) {
                        // Expanded
                        // TCLog.e("Expanded "+verticalOffset);
                        vNameVendor.setBackgroundColor(TCUtils.getColor(R.color.c_ffffff));
                        tv_name_vendor.setTextColor(TCUtils.getColor(R.color.c_000000));
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0,0,0,0);
                        tv_name_vendor.setLayoutParams(params);
                        tv_rating.setLayoutParams(params);
                        tv_review_count.setTextColor(TCUtils.getColor(R.color.c_000000));
                        tv_rating.setTextColor(TCUtils.getColor(R.color.c_000000));
                        ll_share_write_review_vertical.setVisibility(View.VISIBLE);
                        ll_share_write_review_horizontal.setVisibility(View.GONE);
                        VPagerImages.setPagingEnabled(true);
                        pager_indicator.setVisibility(View.VISIBLE);

                    } else {
                        //TCLog.e("Somewhere in betvv  ween "+verticalOffset);
                        // Somewhere in between
                    }
                }
            });

        });
    }

    private void getImageSlideBanner(){
        if(detailModel==null)
            return;
        requestApi(new GetImageVendorRequest(detailModel.getId(),String.valueOf(pagePhoto),"0",this) );
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == ReviewRequestTarget.GET_VENDOR_DETAIL) {
            detailModel = (VendorDetailModel) response.getResult();
            fillData();
        } else if (requestTarget == ReviewRequestTarget.GET_LIST_IMAGES_VENDOR) {
            ArrayList<ImagesVendor> list = ((BaseResultsResponseModel) response.getResult()).getResults();
            if(list!=null&&list.size()>0){
                listImageVendor.addAll(list);
                imageVendorAdapter.notifyDataSetChanged();
//                VPagerImages.setAdapter(imageVendorAdapter);
                // circle_page.setViewPager(VPagerImages);
                pager_indicator.attachToPager(VPagerImages);
                loadPhoto =true;
            }else{
                loadPhoto=false;
            }
        }
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        if (requestTarget == ReviewRequestTarget.GET_LIST_IMAGES_VENDOR) {
            loadPhoto=false;
        }
    }
}
