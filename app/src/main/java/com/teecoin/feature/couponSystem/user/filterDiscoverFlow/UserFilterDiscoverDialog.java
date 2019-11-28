package com.teecoin.feature.couponSystem.user.filterDiscoverFlow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCBaseBottomSheetDiaLogFragment;
import com.teecoin.feature.couponSystem.user.discoverVendor.DiscoverVendorCatalogueImageAdapter;
import com.teecoin.model.couponsystem.VendorCategoryModel;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCDateUtility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import core.view.RecycleListener;

@SuppressLint("ValidFragment")
public class UserFilterDiscoverDialog extends TCBaseBottomSheetDiaLogFragment {
    @BindView(R.id.view_header_search_coupon_discover_tv_title)
    TextView tv_title;
    @BindView(R.id.view_header_search_coupon_discover_tv_reset)
    TextView tv_reset;
    @BindView(R.id.view_list_catalogue_rcv_image)
    TCRecyclerView rcv_catalogue;

    @BindView(R.id.filter_discover_ll_price_range_one)
    View view_price_range_one;
    @BindView(R.id.filter_discover_ll_price_range_two)
    View view_price_range_two;
    @BindView(R.id.filter_discover_ll_price_range_three)
    View view_price_range_three;
    @BindView(R.id.filter_discover_ll_price_range_four)
    View view_price_range_four;

    @BindView(R.id.filter_discover_ll_distance_1)
    View view_distance_1;
    @BindView(R.id.filter_discover_ll_distance_2)
    View view_distance_2;
    @BindView(R.id.filter_discover_ll_distance_3)
    View view_distance_3;
    @BindView(R.id.filter_discover_ll_distance_4)
    View view_distance_4;

    @BindView(R.id.filter_discover_ll_rating_one_star)
    View view_rating_one_star;
    @BindView(R.id.filter_discover_ll_rating_two_star)
    View view_rating_two_star;
    @BindView(R.id.filter_discover_ll_rating_three_star)
    View view_rating_three_star;
    @BindView(R.id.filter_discover_ll_rating_four_star)
    View view_rating_four_star;
    @BindView(R.id.filter_discover_ll_rating_five_star)
    View view_rating_five_star;

    @BindView(R.id.filter_discover_ll_open_now)
    View view_open_now;
    @BindView(R.id.filter_catalogue_ll_opening_hours)
    View view_opening_hours;
    @BindView(R.id.filter_discover_tv_opening_hour)
    TextView tv_opening_hour;
    @BindView(R.id.filter_discover_rb_new_merchant)
    RadioButton rb_new_merchant;
    @BindView(R.id.filter_discover_rb_near_by)
    RadioButton rb_most_near_by;
    @BindView(R.id.filter_discover_rb_highest_rate)
    RadioButton rb_highest_rate;
    @BindView(R.id.filter_discover_rb_top_rate_experience)
    RadioButton rb_top_rate_experience;
    @BindView(R.id.filter_discover_rb_high_to_low_price)
    RadioButton rb_high_to_low_price;
    @BindView(R.id.filter_discover_rb_low_to_high_price)
    RadioButton rb_low_to_high_price;

    @BindView(R.id.filter_discover_tv_cancel)
    TextView tv_cancel;
    @BindView(R.id.filter_discover_tv_apply)
    TextView tv_apply;

    @BindView(R.id.filter_discover_rg_sort_condition)
    RadioGroup rg_sort_condition;

    @BindView(R.id.filter_discover_tv_distance_title)
    TextView tvDistanceTitle;

    @BindView(R.id.filter_discover_ll_distance_section)
    View vDistanceSection;

    private Context context;
    private DiscoverVendorCatalogueImageAdapter categoryAdapter;
    private ArrayList<VendorCategoryModel> categoryList;
    private FilterDiscoverListener filterCategoryListener;

    private FilterDiscoverModel filterDiscoverModel;
    private String sortCondition;
    private boolean isFromMap;

    public UserFilterDiscoverDialog(Context context, boolean isFromMap, ArrayList<VendorCategoryModel> listCategories, FilterDiscoverModel filterDiscoverModel, String sortCondition, FilterDiscoverListener filterCategoryListener) {
        this.context = context;
        this.isFromMap = isFromMap;
        this.categoryList = listCategories;
        this.filterDiscoverModel = filterDiscoverModel;
        this.sortCondition = sortCondition;
        this.filterCategoryListener = filterCategoryListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_filter_discover, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
                FrameLayout bottomSheet = dialog.findViewById(android.support.design.R.id.design_bottom_sheet);
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        initView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    private void initView() {

        tvDistanceTitle.setVisibility(isFromMap ? View.GONE : View.VISIBLE);
        vDistanceSection.setVisibility(isFromMap ? View.GONE : View.VISIBLE);

        categoryAdapter = new DiscoverVendorCatalogueImageAdapter(LayoutInflater.from(getContext()), categoryList, new RecycleListener<VendorCategoryModel>() {
            @Override
            public void onItemClick(View view, VendorCategoryModel item, int position, EnumMgr.ClickType clickType) {
                categoryAdapter.unSelectedAllItem();
                categoryAdapter.setSelectedItem(position);
                filterDiscoverModel.setCategoryModel(item);
            }
        });
        rcv_catalogue.setAdapter(categoryAdapter);
        init();
        onClickView();
    }

    private void init() {
        //TCLog.e("sortCondition "+sortCondition);
        if (filterDiscoverModel != null) {
            //   TCLog.e("not null ");
            view_price_range_one.setSelected(filterDiscoverModel.getPrice_range().equals(FilterDiscoverModel.TAG_PRICE_RANGE_ONE));
            view_price_range_two.setSelected(filterDiscoverModel.getPrice_range().equals(FilterDiscoverModel.TAG_PRICE_RANGE_TWO));
            view_price_range_three.setSelected(filterDiscoverModel.getPrice_range().equals(FilterDiscoverModel.TAG_PRICE_RANGE_THREE));
            view_price_range_four.setSelected(filterDiscoverModel.getPrice_range().equals(FilterDiscoverModel.TAG_PRICE_RANGE_FOUR));

            view_distance_1.setSelected(filterDiscoverModel.getDistance().equals(FilterDiscoverModel.TAG_DISTANCE_HAFT_KM));
            view_distance_2.setSelected(filterDiscoverModel.getDistance().equals(FilterDiscoverModel.TAG_DISTANCE_ONE_KM));
            view_distance_3.setSelected(filterDiscoverModel.getDistance().equals(FilterDiscoverModel.TAG_DISTANCE_ONE_POINT_FIVE_KM));
            view_distance_4.setSelected(filterDiscoverModel.getDistance().equals(FilterDiscoverModel.TAG_DISTANCE_TWO_KM));

            String[] ratings = filterDiscoverModel.getRatings().split("&");
            if (ratings.length > 0) {
                for (String r : ratings) {
                    if (checkRating(r, FilterDiscoverModel.TAG_ONE_STAR)) {
                        view_rating_one_star.setSelected(true);
                    } else if (checkRating(r, FilterDiscoverModel.TAG_TWO_STAR)) {
                        view_rating_two_star.setSelected(true);
                    } else if (checkRating(r, FilterDiscoverModel.TAG_THREE_STAR)) {
                        view_rating_three_star.setSelected(true);
                    } else if (checkRating(r, FilterDiscoverModel.TAG_FOUR_STAR)) {
                        view_rating_four_star.setSelected(true);
                    } else if (checkRating(r, FilterDiscoverModel.TAG_FIVE_STAR)) {
                        view_rating_five_star.setSelected(true);
                    }
                }
            }

            String selectedSortCondition = filterDiscoverModel.getSort_condition();
            if (selectedSortCondition.equals(EnumMgr.SortCondition.NearMe.getValue())) {
                rb_most_near_by.setChecked(true);
            } else if (selectedSortCondition.equals(EnumMgr.SortCondition.NewMerchant.getValue())) {
                rb_new_merchant.setChecked(true);
            } else if (selectedSortCondition.equals(EnumMgr.SortCondition.HighestRating.getValue())) {
                rb_highest_rate.setChecked(true);
            } else if (selectedSortCondition.equals(EnumMgr.SortCondition.TopRatedExperiences.getValue())) {
                rb_top_rate_experience.setChecked(true);
            } else if (selectedSortCondition.equals(EnumMgr.SortCondition.HighToLowPrice.getValue())) {
                rb_high_to_low_price.setChecked(true);
            } else if (selectedSortCondition.equals(EnumMgr.SortCondition.LowToHighPrice.getValue())) {
                rb_low_to_high_price.setChecked(true);
            }

            if (filterDiscoverModel.getFilterOpenTimeType() != null) {
                if (filterDiscoverModel.getFilterOpenTimeType().equals(FilterDiscoverModel.FilterOpenTimeType.OpenNow)) {
                    view_open_now.setSelected(true);
                } else if (filterDiscoverModel.getFilterOpenTimeType().equals(FilterDiscoverModel.FilterOpenTimeType.SpecificOpeningTime)) {
                    view_opening_hours.setSelected(true);
                    tv_opening_hour.setText(
                            TCDateUtility.formatDate(
                                    TCDateUtility.toDate(filterDiscoverModel.getOpen_date(), TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z),
                                    TCDateUtility.DateFormatDefinition.DD_MM_YYYY_HH_MM_SS));
                }
            }
        }
    }

    private boolean checkRating(String ratingString, String star) {
        return ratingString.contains(star);
    }

    private void onClickView() {
        tv_reset.setOnClickListener(v -> resetFilter());
        tv_cancel.setOnClickListener(v -> dismiss());
        tv_apply.setOnClickListener(v -> {
            filData();
        });

        // GROUP PRICE RANGE
        view_price_range_one.setOnClickListener(v -> {
            if (view_price_range_one.isSelected()) {
                view_price_range_one.setSelected(false);
                filterDiscoverModel.setPrice_range("");
            } else {
                filterDiscoverModel.setPrice_range("");
                view_price_range_one.setSelected(true);
                deselectView(view_price_range_two, view_price_range_three, view_price_range_four);
            }
        });
        view_price_range_two.setOnClickListener(v -> {
            if (view_price_range_two.isSelected()) {
                view_price_range_two.setSelected(false);
                filterDiscoverModel.setPrice_range("");
            } else {
                filterDiscoverModel.setPrice_range("");
                view_price_range_two.setSelected(true);
                deselectView(view_price_range_one, view_price_range_three, view_price_range_four);
            }
        });
        view_price_range_three.setOnClickListener(v -> {
            if (view_price_range_three.isSelected()) {
                view_price_range_three.setSelected(false);
                filterDiscoverModel.setPrice_range("");
            } else {
                filterDiscoverModel.setPrice_range("");
                view_price_range_three.setSelected(true);
                deselectView(view_price_range_one, view_price_range_two, view_price_range_four);
            }
        });
        view_price_range_four.setOnClickListener(v -> {
            if (view_price_range_four.isSelected()) {
                view_price_range_four.setSelected(false);
                filterDiscoverModel.setPrice_range("");
            } else {
                filterDiscoverModel.setPrice_range("");
                view_price_range_four.setSelected(true);
                deselectView(view_price_range_one, view_price_range_two, view_price_range_three);
            }
        });

        // GROUP DISTANCE
        view_distance_1.setOnClickListener(v -> {
            if (view_distance_1.isSelected()) {
                view_distance_1.setSelected(false);
            } else {
                view_distance_1.setSelected(true);
                deselectView(view_distance_2, view_distance_3, view_distance_4);
            }
        });
        view_distance_2.setOnClickListener(v -> {
            if (view_distance_2.isSelected()) {
                filterDiscoverModel.setDistance("");
                view_distance_2.setSelected(false);
            } else {
                view_distance_2.setSelected(true);
                deselectView(view_distance_1, view_distance_3, view_distance_4);
            }
        });
        view_distance_3.setOnClickListener(v -> {
            if (view_distance_3.isSelected()) {
                view_distance_3.setSelected(false);
            } else {
                view_distance_3.setSelected(true);
                deselectView(view_distance_1, view_distance_2, view_distance_4);
            }
        });
        view_distance_4.setOnClickListener(v -> {
            if (view_distance_4.isSelected()) {
                view_distance_4.setSelected(false);
            } else {
                filterDiscoverModel.setDistance(FilterDiscoverModel.TAG_DISTANCE_TWO_KM);
                view_distance_4.setSelected(true);
                deselectView(view_distance_1, view_distance_2, view_distance_3);
            }
        });

        // GROUP RATING
        view_rating_one_star.setOnClickListener(v -> v.setSelected(!v.isSelected()));
        view_rating_two_star.setOnClickListener(v -> v.setSelected(!v.isSelected()));
        view_rating_three_star.setOnClickListener(v -> v.setSelected(!v.isSelected()));
        view_rating_four_star.setOnClickListener(v -> v.setSelected(!v.isSelected()));
        view_rating_five_star.setOnClickListener(v -> v.setSelected(!v.isSelected()));

        // GROUP OPENING TIME
        view_open_now.setOnClickListener(v -> {
            if (view_open_now.isSelected()) {
                view_open_now.setSelected(false);
            } else {
                view_open_now.setSelected(true);
                view_opening_hours.setSelected(false);
            }
        });
        view_opening_hours.setOnClickListener(v -> {
            if (view_opening_hours.isSelected()) {
                view_opening_hours.setSelected(false);
            } else {
                view_open_now.setSelected(false);
                view_opening_hours.setSelected(true);
                TCDateUtility.openDateTimePicker(TCConstant.DATE_TIME_PICKER_FROM_FILTER, tv_opening_hour, context);
            }
        });
    }

    private void filData() {
        if (view_price_range_one.isSelected())
            filterDiscoverModel.setPrice_range(FilterDiscoverModel.TAG_PRICE_RANGE_ONE);
        if (view_price_range_two.isSelected())
            filterDiscoverModel.setPrice_range(FilterDiscoverModel.TAG_PRICE_RANGE_TWO);
        if (view_price_range_three.isSelected())
            filterDiscoverModel.setPrice_range(FilterDiscoverModel.TAG_PRICE_RANGE_THREE);
        if (view_price_range_four.isSelected())
            filterDiscoverModel.setPrice_range(FilterDiscoverModel.TAG_PRICE_RANGE_FOUR);

        filterDiscoverModel.setDistance("");
        if (view_distance_1.isSelected())
            filterDiscoverModel.setDistance(FilterDiscoverModel.TAG_DISTANCE_HAFT_KM);
        if (view_distance_2.isSelected())
            filterDiscoverModel.setDistance(FilterDiscoverModel.TAG_DISTANCE_ONE_KM);
        if (view_distance_3.isSelected())
            filterDiscoverModel.setDistance(FilterDiscoverModel.TAG_DISTANCE_ONE_POINT_FIVE_KM);
        if (view_distance_4.isSelected())
            filterDiscoverModel.setDistance(FilterDiscoverModel.TAG_DISTANCE_TWO_KM);

        StringBuilder rating = new StringBuilder();
        if (view_rating_one_star.isSelected())
            rating.append(String.format(FilterDiscoverModel.TAG_RATING, FilterDiscoverModel.TAG_ONE_STAR));
        if (view_rating_two_star.isSelected())
            rating.append(String.format(FilterDiscoverModel.TAG_RATING, FilterDiscoverModel.TAG_TWO_STAR));
        if (view_rating_three_star.isSelected())
            rating.append(String.format(FilterDiscoverModel.TAG_RATING, FilterDiscoverModel.TAG_THREE_STAR));
        if (view_rating_four_star.isSelected())
            rating.append(String.format(FilterDiscoverModel.TAG_RATING, FilterDiscoverModel.TAG_FOUR_STAR));
        if (view_rating_five_star.isSelected())
            rating.append(String.format(FilterDiscoverModel.TAG_RATING, FilterDiscoverModel.TAG_FIVE_STAR));

        filterDiscoverModel.setRatings(rating.toString());

        filterDiscoverModel.setSort_condition(getSortCondition());

        if (view_opening_hours.isSelected()) {
            filterDiscoverModel.setOpen_date(TCDateUtility.formatDate(
                    TCDateUtility.toDate(tv_opening_hour.getText().toString(),
                            TCDateUtility.DateFormatDefinition.DD_MM_YYYY_HH_MM_SS)
                    , TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z));
            filterDiscoverModel.setFilterOpenTimeType(FilterDiscoverModel.FilterOpenTimeType.SpecificOpeningTime);
        } else if (view_open_now.isSelected()) {
            filterDiscoverModel.setOpen_date(TCDateUtility.formatDate(TCDateUtility.getCurrentDate(),
                    TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z));
            filterDiscoverModel.setFilterOpenTimeType(FilterDiscoverModel.FilterOpenTimeType.OpenNow);
        } else {
            filterDiscoverModel.setFilterOpenTimeType(FilterDiscoverModel.FilterOpenTimeType.None);
            filterDiscoverModel.setOpen_date("");
        }

        filterCategoryListener.onFilter(filterDiscoverModel, categoryList);
        dismiss();
    }

    private void resetFilter() {
        deselectView(view_price_range_one,
                view_price_range_two,
                view_price_range_three,
                view_price_range_four);

        deselectView(view_distance_1,
                view_distance_2,
                view_distance_3,
                view_distance_4);

        deselectView(view_rating_one_star,
                view_rating_two_star,
                view_rating_three_star,
                view_rating_four_star, view_rating_five_star);

        deselectView(view_open_now,
                view_opening_hours);


        rb_new_merchant.setChecked(false);
        rb_most_near_by.setChecked(false);
        rb_highest_rate.setChecked(false);
        rb_top_rate_experience.setChecked(false);
        rb_high_to_low_price.setChecked(false);
        rb_low_to_high_price.setChecked(false);

        rg_sort_condition.clearCheck();
        if (filterDiscoverModel != null)
            filterDiscoverModel.clearCondition();

    }

    private String getSortCondition() {
        int checkedRadioButtonId = rg_sort_condition.getCheckedRadioButtonId();
        switch (checkedRadioButtonId) {
            case R.id.filter_discover_rb_near_by:
                return EnumMgr.SortCondition.NearMe.getValue();
            case R.id.filter_discover_rb_new_merchant:
                return EnumMgr.SortCondition.NewMerchant.getValue();
            case R.id.filter_discover_rb_highest_rate:
                return EnumMgr.SortCondition.HighestRating.getValue();
            case R.id.filter_discover_rb_top_rate_experience:
                return EnumMgr.SortCondition.TopRatedExperiences.getValue();
            case R.id.filter_discover_rb_high_to_low_price:
                return EnumMgr.SortCondition.HighToLowPrice.getValue();
            case R.id.filter_discover_rb_low_to_high_price:
                return EnumMgr.SortCondition.LowToHighPrice.getValue();
            default:
                return "";
        }
    }
}
