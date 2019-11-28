package com.teecoin.feature.reviewSystem.filter;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

@SuppressLint("ValidFragment")
public class FilterSheetDialog extends BottomSheetDialogFragment implements CompoundButton.OnCheckedChangeListener {
    private static final String FIVE_KILOMETERS = "5";
    private static final String TEN_KILOMETERS = "10";
    private static final String TWENTY_KILOMETERS = "20";
    @BindView(R.id.restaurant_rl_search)
    View rl_search;
    @BindView(R.id.restaurant_et_search)
    EditText et_search;
    @BindView(R.id.top_review_filter_ll_from_date)
    View fromDateView;
    @BindView(R.id.top_review_filter_ll_to_date)
    View toDateView;
    TextView tvFromDate;
    //@BindView(R.id.view_filter_calendar_item_tv_date)
    TextView tvToDate;
    @BindView(R.id.top_review_filter_ll_sort_by_date)
    View ll_sort_by_date;
    @BindView(R.id.restaurant_filter_ll_opening_time)
    View ll_opening_time;
    @BindView(R.id.tv_distance)
    TextView tv_distance;
    @BindView(R.id.ll_distance)
    View ll_distance;
    @BindView(R.id.top_review_filter_cb_five_star)
    CheckBox chkBxFiveStar;
    @BindView(R.id.top_review_filter_cb_four_star)
    CheckBox chkBxFourStar;
    @BindView(R.id.top_review_filter_cb_three_star)
    CheckBox chkBxThreeStar;
    @BindView(R.id.top_review_filter_cb_two_star)
    CheckBox chkBxTwoStar;
    @BindView(R.id.top_review_filter_cb_one_star)
    CheckBox chkBxOneStar;
    @BindView(R.id.top_review_filter_cb_near_me)
    CheckBox cb_near_me;
    @BindView(R.id.top_review_filter_cb_new_merchant)
    CheckBox cb_new_merchant;
    @BindView(R.id.top_review_filter_cb_five_km)
    CheckBox cb_five_km;
    @BindView(R.id.top_review_filter_cb_ten_km)
    CheckBox cb_ten_km;
    @BindView(R.id.top_review_filter_cb_twenty_km)
    CheckBox cb_twenty_km;
    @BindView(R.id.top_review_filter_ll_near_me_merchant)
    View ll_near_me_merchant;
    @BindView(R.id.top_review_filter_btn_filter)
    Button btnFilter;
    @BindView(R.id.restaurant_filter_tv_opening_time)
    TextView tv_opening_time;
    @BindView(R.id.restaurant_rl_opening_times)
    View rl_opening_times;
    @BindView(R.id.top_review_filter_cb_sort_date_newest)
    CheckBox cb_sort_date_newest;
    @BindView(R.id.top_review_filter_cb_sort_date_oldest)
    CheckBox cb_sort_date_oldest;
    @BindView(R.id.top_review_filter_cb_sort_rate_newest)
    CheckBox cb_sort_rate_newest;
    @BindView(R.id.top_review_filter_cb_sort_rate_oldest)
    CheckBox cb_sort_rate_oldest;
    FilterListener listener;
    Context context;
    ArrayList<FilterItem> listCuisineType = new ArrayList<>();
    View root;
    private FilterTypeDialog filterType;
    private FilterModel filterModel;
    private BottomSheetBehavior<View> behavior;

    public FilterSheetDialog(Context context, FilterTypeDialog filterType, FilterModel filterModel, FilterListener listener) {
        this.listener = listener;
        this.context = context;
        this.filterType = filterType;
        this.filterModel = filterModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_top_review_filter, container, false);
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
                behavior.setPeekHeight(0);
            }
        });
        init();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetDialog);
    }

    private void init() {
        tvFromDate = fromDateView.findViewById(R.id.view_filter_calendar_item_tv_date);
        tvFromDate.setText(TCDateUtility.getDateBefore(-31));
        tvToDate = toDateView.findViewById(R.id.view_filter_calendar_item_tv_date);
        tvToDate.setText(TCDateUtility.formatDate(TCDateUtility.getCurrentDate(), TCDateUtility.DateFormatDefinition.YYYY_MM_DD_HYPHEN));

        if (filterType == FilterTypeDialog.RestaurantFilter) {
            rl_search.setVisibility(VISIBLE);
            ll_opening_time.setVisibility(VISIBLE);
            ll_near_me_merchant.setVisibility(VISIBLE);
            ll_sort_by_date.setVisibility(GONE);
            rl_opening_times.setOnClickListener(v -> TCDateUtility.openDateTimePicker(TCConstant.DATE_TIME_PICKER_FROM_FILTER, tv_opening_time, context));

            cb_sort_date_newest.setText(TCUtils.getString(R.string.review_filter_sort_date_most_reviews));
            cb_sort_date_oldest.setText(TCUtils.getString(R.string.review_filter_sort_date_lowest_reviews));

            tv_distance.setVisibility(VISIBLE);
            ll_distance.setVisibility(VISIBLE);
        } else {
            rl_search.setVisibility(GONE);
            ll_opening_time.setVisibility(GONE);
            ll_near_me_merchant.setVisibility(GONE);
            ll_sort_by_date.setVisibility(VISIBLE);
            fromDateView.setOnClickListener(v -> openCalendar(tvFromDate));
            toDateView.setOnClickListener(v -> openCalendar(tvToDate));

            tv_distance.setVisibility(GONE);
            ll_distance.setVisibility(GONE);
        }
        fillData();
        btnFilter.setOnClickListener(v -> filter());
        onListener();
    }

    private void onListener() {
        cb_five_km.setOnCheckedChangeListener(this);
        cb_ten_km.setOnCheckedChangeListener(this);
        cb_twenty_km.setOnCheckedChangeListener(this);
        cb_sort_date_oldest.setOnCheckedChangeListener(this);
        cb_sort_date_newest.setOnCheckedChangeListener(this);
        cb_sort_rate_oldest.setOnCheckedChangeListener(this);
        cb_sort_rate_newest.setOnCheckedChangeListener(this);
    }

    private void fillData() {
        if (filterModel != null) {
            if (filterModel.getFilterList().size() > 0) {
                for (int i = 0; i < filterModel.getFilterList().size(); i++) {
                    FilterItem item = filterModel.getFilterList().get(i);
                    if (item.getFilter().equals(FilterModel.Filter.Cuisines)) {
                        listCuisineType.add(new FilterItem(FilterModel.Filter.Cuisines, item.getName()));
                    }
                    if (item.getFilter().equals(FilterModel.Filter.FromDate_ToDate)) {
                        tvFromDate.setText(item.getFromDate());
                        tvToDate.setText(item.getToDate());
                    }

                    if (item.getFilter().equals(FilterModel.Filter.NearMe)) {
                        cb_near_me.setChecked(true);
                    }
                    if (item.getFilter().equals(FilterModel.Filter.OneStar)) {
                        chkBxOneStar.setChecked(true);
                    }
                    if (item.getFilter().equals(FilterModel.Filter.TwoStar)) {
                        chkBxTwoStar.setChecked(true);
                    }
                    if (item.getFilter().equals(FilterModel.Filter.ThreeStar)) {
                        chkBxThreeStar.setChecked(true);
                    }
                    if (item.getFilter().equals(FilterModel.Filter.FourStar)) {
                        chkBxFourStar.setChecked(true);
                    }
                    if (item.getFilter().equals(FilterModel.Filter.FiveStar)) {
                        chkBxFiveStar.setChecked(true);
                    }
                    if (item.getFilter().equals(FilterModel.Filter.SortDate_NewestFirst) || item.getFilter().equals(FilterModel.Filter.SortDate_MostReviews)) {
                        cb_sort_date_newest.setChecked(true);
                    }
                    if (item.getFilter().equals(FilterModel.Filter.SortDate_OldestFirst) || item.getFilter().equals(FilterModel.Filter.SortDate_LowestReviews)) {
                        cb_sort_date_oldest.setChecked(true);
                    }
                    if (item.getFilter().equals(FilterModel.Filter.SortRate_HighestRated)) {
                        cb_sort_rate_newest.setChecked(true);
                    }
                    if (item.getFilter().equals(FilterModel.Filter.SortRate_LowestRated)) {
                        cb_sort_rate_oldest.setChecked(true);
                    }
                    if (item.getFilter().equals(FilterModel.Filter.NewMerchant)) {
                        cb_new_merchant.setChecked(true);
                    }
                    if (item.getFilter().equals(FilterModel.Filter.Keyword)) {
                        et_search.setText(item.getName());
                    }
                    if (item.getFilter().equals(FilterModel.Filter.Distance)) {
                        if (item.getValue() == 5) {
                            cb_five_km.setChecked(true);
                            cb_ten_km.setChecked(false);
                            cb_twenty_km.setChecked(false);
                        } else if (item.getValue() == 10) {
                            cb_five_km.setChecked(false);
                            cb_ten_km.setChecked(true);
                            cb_twenty_km.setChecked(false);
                        } else {
                            cb_five_km.setChecked(false);
                            cb_ten_km.setChecked(false);
                            cb_twenty_km.setChecked(true);
                        }
                    }
                }
            }
        }
    }

    private void openCalendar(TextView tvDate) {
        TCDateUtility.changLocalTimeZonetoLanguageApp(this.context);
        Date date = TCDateUtility.toDate(tvDate.getText().toString(), TCDateUtility.DateFormatDefinition.YYYY_MM_DD_HYPHEN);
        int mYear = TCDateUtility.getYear(date);
        int mMonth = TCDateUtility.getMonth(date);
        int mDay = TCDateUtility.getDayOfMonth(date);
        DatePickerDialog dialog = new DatePickerDialog(context, (view, year, month, dayOfMonth) ->
                tvDate.setText(TCDateUtility.formatDate(year, month, dayOfMonth, TCDateUtility.DateFormatDefinition.YYYY_MM_DD_HYPHEN)),
                mYear, mMonth, mDay);
        dialog.show();
    }

    private void filter() {
        ArrayList<FilterItem> list = new ArrayList<>();
        if (filterType == FilterTypeDialog.RestaurantFilter) {
            if (!TCUtils.isEmpty(et_search.getText().toString())) {
                list.add(new FilterItem(FilterModel.Filter.Keyword, et_search.getText().toString()));
            }
            if (!TCUtils.isEmpty(tv_opening_time.getText().toString())) {
                list.add(new FilterItem(FilterModel.Filter.OpeningTimes, tv_opening_time.getText().toString()));
            }
            getOption();
        }
        if (filterType != FilterTypeDialog.RestaurantFilter) {
            list.add(new FilterItem(FilterModel.Filter.FromDate_ToDate, tvFromDate.getText().toString(), tvToDate.getText().toString()));
        }
        if (cb_sort_date_newest.isChecked()) {
            if (filterType == FilterTypeDialog.RestaurantFilter) {
                list.add(new FilterItem(FilterModel.Filter.SortDate_MostReviews));
            } else {
                list.add(new FilterItem(FilterModel.Filter.SortDate_NewestFirst));
            }
        }
        if (cb_near_me.isChecked()) {
            list.add(new FilterItem(FilterModel.Filter.NearMe));
        }
        if (cb_new_merchant.isChecked()) {
            list.add(new FilterItem(FilterModel.Filter.NewMerchant));
        }
        list.addAll(getStarRating());
        list.addAll(getDistance());

        if (cb_sort_date_oldest.isChecked()) {
            if (filterType == FilterTypeDialog.RestaurantFilter) {
                list.add(new FilterItem(FilterModel.Filter.SortDate_LowestReviews));
            } else {
                list.add(new FilterItem(FilterModel.Filter.SortDate_OldestFirst));
            }
        }
        //--
        if (cb_sort_rate_newest.isChecked()) {
            list.add(new FilterItem(FilterModel.Filter.SortRate_HighestRated));
        }
        if (cb_sort_rate_oldest.isChecked()) {
            list.add(new FilterItem(FilterModel.Filter.SortRate_LowestRated));
        }

//        list.add(new FilterItem(rgSortDate.getCheckedRadioButtonId() == R.id.top_review_filter_rb_sort_date_newest ?
//                FilterModel.Filter.SortDate_MostReviews : FilterModel.Filter.SortDate_LowestReviews));
//        list.add(new FilterItem(rgSortRate.getCheckedRadioButtonId() == R.id.top_review_filter_rb_sort_rate_newest ?
//                FilterModel.Filter.SortRate_HighestRated : FilterModel.Filter.SortRate_LowestRated));

        // todo: filler restaurant with cuisine type
      /*  if(listCuisineType.size()>0){
            for(FilterItem item:listCuisineType){
                list.add(new FilterItem(FilterModel.Filter.Cuisines,item.getName()));
            }
        }*/

        FilterModel filterModel = new FilterModel(list);
        listener.onFilter(filterModel);
        dismiss();
    }

    private ArrayList<FilterItem> getStarRating() {
        ArrayList<FilterItem> list = new ArrayList<>();

        if (chkBxFiveStar.isChecked()) {
            list.add(new FilterItem(FilterModel.Filter.FiveStar));
        }
        if (chkBxFourStar.isChecked()) {
            list.add(new FilterItem(FilterModel.Filter.FourStar));
        }
        if (chkBxThreeStar.isChecked()) {
            list.add(new FilterItem(FilterModel.Filter.ThreeStar));
        }
        if (chkBxTwoStar.isChecked()) {
            list.add(new FilterItem(FilterModel.Filter.TwoStar));
        }
        if (chkBxOneStar.isChecked()) {
            list.add(new FilterItem(FilterModel.Filter.OneStar));
        }
        return list;
    }

    private ArrayList<FilterItem> getOption() {
        ArrayList<FilterItem> list = new ArrayList<>();
        if (cb_near_me.isChecked()) {
            list.add(new FilterItem(FilterModel.Filter.NearMe));
        }
        if (cb_new_merchant.isChecked()) {
            list.add(new FilterItem(FilterModel.Filter.NewMerchant));
        }
        return list;
    }

    private ArrayList<FilterItem> getDistance() {
        ArrayList<FilterItem> list = new ArrayList<>();
        if (cb_five_km.isChecked()) {
            list.add(new FilterItem(FilterModel.Filter.Distance, FIVE_KILOMETERS));
        }
        if (cb_ten_km.isChecked()) {
            list.add(new FilterItem(FilterModel.Filter.Distance, TEN_KILOMETERS));
        }
        if (cb_twenty_km.isChecked()) {
            list.add(new FilterItem(FilterModel.Filter.Distance, TWENTY_KILOMETERS));
        }
        return list;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.top_review_filter_cb_five_km:
                if (isChecked) {
                    cb_ten_km.setChecked(false);
                    cb_twenty_km.setChecked(false);
                }
                break;

            case R.id.top_review_filter_cb_ten_km:
                if (isChecked) {
                    cb_five_km.setChecked(false);
                    cb_twenty_km.setChecked(false);
                }
                break;

            case R.id.top_review_filter_cb_twenty_km:
                if (isChecked) {
                    cb_five_km.setChecked(false);
                    cb_ten_km.setChecked(false);
                }
                break;

            case R.id.top_review_filter_cb_sort_date_newest:
                if (isChecked) {
                    cb_sort_date_oldest.setChecked(false);
                }
                break;

            case R.id.top_review_filter_cb_sort_date_oldest:
                if (isChecked) {
                    cb_sort_date_newest.setChecked(false);
                }
                break;

            case R.id.top_review_filter_cb_sort_rate_newest:
                if (isChecked) {
                    cb_sort_rate_oldest.setChecked(false);
                }
                break;

            case R.id.top_review_filter_cb_sort_rate_oldest:
                if (isChecked) {
                    cb_sort_rate_newest.setChecked(false);
                }
                break;
        }
    }

    public enum FilterTypeDialog {
        RestaurantFilter,
        TopReviewFilter
    }
}
