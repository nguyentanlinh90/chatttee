package com.teecoin.feature.couponSystem.user.myCoupon;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.teecoin.R;
import com.teecoin.feature.couponSystem.user.filterCouponFlow.FilterCouponItem;
import com.teecoin.feature.couponSystem.user.filterCouponFlow.FilterCouponListener;
import com.teecoin.feature.couponSystem.user.filterCouponFlow.FilterCouponModel;
import com.teecoin.ui.RangeSeekBar;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.teecoin.utils.TCUtils.containsItemFilter;
import static com.teecoin.utils.TCUtils.getTextFilterToContains;

@SuppressLint("ValidFragment")
public class UserFilterMyCouponDialog extends BottomSheetDialogFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.dialog_coupon_user_filter_coupon_et_search)
    EditText etSearch;
    @BindView(R.id.dialog_coupon_user_filter_coupon_tv_reset_all)
    TextView tvResetAll;
    @BindView(R.id.dialog_coupon_user_filter_coupon_cb_ending_soon)
    CheckBox cbEndingSoon;
    @BindView(R.id.dialog_coupon_user_filter_coupon_cb_latest)
    CheckBox cbLatest;
    @BindView(R.id.dialog_coupon_user_filter_coupon_cb_buy_one_get_one)
    CheckBox cbBuy1Get1;
    @BindView(R.id.dialog_coupon_user_filter_coupon_cb_free_gift)
    CheckBox cbFreeGift;
    @BindView(R.id.dialog_coupon_user_filter_coupon_cb_discount_percent)
    CheckBox cbDiscountPercent;
    @BindView(R.id.dialog_coupon_user_filter_coupon_sb_discount_percent)
    FrameLayout vDiscountPercent;
    @BindView(R.id.dialog_coupon_user_filter_coupon_cb_cash)
    CheckBox cbCash;
    @BindView(R.id.dialog_coupon_user_filter_coupon_v_cash)
    View vCash;
    @BindView(R.id.dialog_coupon_user_filter_coupon_et_min_cash)
    EditText etMinCash;
    @BindView(R.id.dialog_coupon_user_filter_coupon_et_max_cash)
    EditText etMaxCash;
    @BindView(R.id.dialog_coupon_user_filter_coupon_et_day_remain)
    EditText etDayRemain;
    @BindView(R.id.view_select_time_start_tv_start)
    TextView tvStart;
    @BindView(R.id.view_select_time_start_rl_start)
    View rlStart;
    @BindView(R.id.view_select_time_end_tv_end)
    TextView tvEnd;
    @BindView(R.id.view_select_time_end_rl_end)
    View rlEnd;
    @BindView(R.id.dialog_coupon_user_filter_coupon_tv_bt_done)
    TextView tvBtDone;
    Unbinder unbinder;
    private FilterCouponListener mListener;
    private FilterCouponModel mFilterCouponModel;
    private int minValueDiscount = 20;
    private int maxValueDiscount = 80;

    @SuppressLint("ValidFragment")
    public UserFilterMyCouponDialog(FilterCouponModel filterCouponModel, FilterCouponListener filterCouponListener) {
        this.mListener = filterCouponListener;
        this.mFilterCouponModel = filterCouponModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_coupon_user_filter_coupon, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.getViewTreeObserver().addOnGlobalLayoutListener(() -> {// full views
            BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
            FrameLayout bottomSheet = dialog.findViewById(android.support.design.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                //behavior.setPeekHeight(0); dismiss when scroll to bottom
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
        etSearch.setImeOptions(EditorInfo.IME_ACTION_DONE);
        RangeSeekBar<Integer> rangeSeekBarDiscountPercent = new RangeSeekBar<>(getActivity());
        rangeSeekBarDiscountPercent.setRangeValues(0, 100);
        if (mFilterCouponModel != null) {
            cbEndingSoon.setChecked(containsItemFilter(mFilterCouponModel, FilterCouponItem.ENDING_SOON));
            cbLatest.setChecked(containsItemFilter(mFilterCouponModel, FilterCouponItem.LATEST));
            cbBuy1Get1.setChecked(containsItemFilter(mFilterCouponModel, FilterCouponItem.BUY_1_GET_1));
            cbFreeGift.setChecked(containsItemFilter(mFilterCouponModel, FilterCouponItem.FREE_GIFT));

            cbDiscountPercent.setChecked(containsItemFilter(mFilterCouponModel, getTextFilterToContains(FilterCouponItem.DISCOUNT)));
            if (containsItemFilter(mFilterCouponModel, getTextFilterToContains(FilterCouponItem.DISCOUNT))) {
                vDiscountPercent.setVisibility(View.VISIBLE);
            }
            if (containsItemFilter(mFilterCouponModel, getTextFilterToContains(FilterCouponItem.DISCOUNT))) {
                rangeSeekBarDiscountPercent.setSelectedMinValue(Integer.parseInt(mFilterCouponModel.getMinDisCount()));
                rangeSeekBarDiscountPercent.setSelectedMaxValue(Integer.parseInt(mFilterCouponModel.getMaxDisCount()));
            } else {
                rangeSeekBarDiscountPercent.setSelectedMinValue(minValueDiscount);
                rangeSeekBarDiscountPercent.setSelectedMaxValue(maxValueDiscount);
            }

            cbCash.setChecked(containsItemFilter(mFilterCouponModel, FilterCouponItem.CASH));
            if (containsItemFilter(mFilterCouponModel, FilterCouponItem.CASH)) {
                vCash.setVisibility(View.VISIBLE);
            }
            if (containsItemFilter(mFilterCouponModel, getTextFilterToContains(FilterCouponItem.MIN_CASH))) {
                etMinCash.setText(mFilterCouponModel.getMinCash());
            }
            if (containsItemFilter(mFilterCouponModel, getTextFilterToContains(FilterCouponItem.MAX_CASH))) {
                etMaxCash.setText(mFilterCouponModel.getMaxCash());
            }

            if (containsItemFilter(mFilterCouponModel, getTextFilterToContains(FilterCouponItem.DAY_REMAIN))) {
                etDayRemain.setText(mFilterCouponModel.getDayRemain());
            }

            if (containsItemFilter(mFilterCouponModel, getTextFilterToContains(FilterCouponItem.START))) {
                tvStart.setText(mFilterCouponModel.getStartDay());
            }
            if (containsItemFilter(mFilterCouponModel, getTextFilterToContains(FilterCouponItem.END))) {
                tvEnd.setText(mFilterCouponModel.getEndDay());
            }
        } else {
            rangeSeekBarDiscountPercent.setSelectedMinValue(minValueDiscount);
            rangeSeekBarDiscountPercent.setSelectedMaxValue(maxValueDiscount);
        }
        rangeSeekBarDiscountPercent.setOnRangeSeekBarChangeListener((bar, minValue, maxValue) -> {
            minValueDiscount = minValue;
            maxValueDiscount = maxValue;
        });
        vDiscountPercent.addView(rangeSeekBarDiscountPercent);
        etDayRemain.setOnFocusChangeListener((v, hasFocus) -> etDayRemain.setHint(hasFocus ? "" : "0"));
        cbCash.setText(String.format(TCUtils.getString(R.string.cash_value), TCUtils.getString(R.string.text_usd)));
        onListener();
    }

    private void onListener() {
        cbEndingSoon.setOnCheckedChangeListener(this);
        cbLatest.setOnCheckedChangeListener(this);
        cbBuy1Get1.setOnCheckedChangeListener(this);
        cbFreeGift.setOnCheckedChangeListener(this);
        cbDiscountPercent.setOnCheckedChangeListener(this);
        cbCash.setOnCheckedChangeListener(this);
        rlStart.setOnClickListener(this);
        rlEnd.setOnClickListener(this);
        tvResetAll.setOnClickListener(this);
        tvBtDone.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.dialog_coupon_user_filter_coupon_cb_ending_soon:
                if (isChecked) {
                    cbLatest.setChecked(false);
                }
                break;
            case R.id.dialog_coupon_user_filter_coupon_cb_latest:
                if (isChecked) {
                    cbEndingSoon.setChecked(false);
                }
                break;
            case R.id.dialog_coupon_user_filter_coupon_cb_buy_one_get_one:

                break;
            case R.id.dialog_coupon_user_filter_coupon_cb_free_gift:

                break;
            case R.id.dialog_coupon_user_filter_coupon_cb_discount_percent:
                vDiscountPercent.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                break;
            case R.id.dialog_coupon_user_filter_coupon_cb_cash:
                vCash.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_coupon_user_filter_coupon_tv_reset_all:
                cbEndingSoon.setChecked(false);
                cbLatest.setChecked(false);
                cbBuy1Get1.setChecked(false);
                cbFreeGift.setChecked(false);
                cbDiscountPercent.setChecked(false);
                cbCash.setChecked(false);
                etDayRemain.setText("");
                tvStart.setText("");
                tvEnd.setText("");
                break;
            case R.id.view_select_time_start_rl_start:
                TCDateUtility.openDateTimePicker(TCConstant.DATE_TIME_PICKER_FROM_COUPON, tvStart, getActivity());
                break;
            case R.id.view_select_time_end_rl_end:
                TCDateUtility.openDateTimePicker(TCConstant.DATE_TIME_PICKER_FROM_COUPON, tvEnd, getActivity());
                break;
            case R.id.dialog_coupon_user_filter_coupon_tv_bt_done:
                filterCoupon();
                break;
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void filterCoupon() {
        ArrayList<FilterCouponItem> list = new ArrayList<>();
        FilterCouponModel filterCouponModel = new FilterCouponModel();
        if (!TCUtils.isEmpty(etSearch.getText().toString())) {
            list.add(new FilterCouponItem(String.format(FilterCouponItem.INPUT_TEXT_SEARCH, etSearch.getText().toString())));
        }
        if (cbEndingSoon.isChecked()) {
            list.add(new FilterCouponItem(FilterCouponItem.ENDING_SOON));
        }
        if (cbLatest.isChecked()) {
            list.add(new FilterCouponItem(FilterCouponItem.LATEST));
        }
        if (cbBuy1Get1.isChecked()) {
            list.add(new FilterCouponItem(FilterCouponItem.BUY_1_GET_1));
        }
        if (cbFreeGift.isChecked()) {
            list.add(new FilterCouponItem(FilterCouponItem.FREE_GIFT));
        }
        if (cbDiscountPercent.isChecked()) {
            list.add(new FilterCouponItem(String.format(FilterCouponItem.DISCOUNT, String.valueOf(minValueDiscount))));
            list.add(new FilterCouponItem(String.format(FilterCouponItem.DISCOUNT, String.valueOf(maxValueDiscount))));
        }
        if (cbCash.isChecked()) {
            list.add(new FilterCouponItem(FilterCouponItem.CASH));
            if (!TCUtils.isEmpty(etMinCash.getText().toString())) {
                list.add(new FilterCouponItem(String.format(FilterCouponItem.MIN_CASH, etMinCash.getText().toString())));
                filterCouponModel.setMinCash(etMinCash.getText().toString());
            }
            if (!TCUtils.isEmpty(etMaxCash.getText().toString())) {
                if (!TCUtils.isEmpty(etMinCash.getText().toString())) {
                    if (Long.parseLong(etMaxCash.getText().toString()) >= Long.parseLong(etMinCash.getText().toString())) {
                        list.add(new FilterCouponItem(String.format(FilterCouponItem.MAX_CASH, etMaxCash.getText().toString())));
                        filterCouponModel.setMaxCash(etMaxCash.getText().toString());
                    } else {
                        Toast.makeText(getContext(), TCUtils.getString(R.string.coupon_filter_please_input_max_cash_bigger_min_cash), Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    list.add(new FilterCouponItem(String.format(FilterCouponItem.MAX_CASH, etMaxCash.getText().toString())));
                    filterCouponModel.setMaxCash(etMaxCash.getText().toString());
                }
            }
        }
        if (!TCUtils.isEmpty(etDayRemain.getText().toString())) {
            list.add(new FilterCouponItem(String.format(FilterCouponItem.DAY_REMAIN, etDayRemain.getText().toString())));
            filterCouponModel.setDayRemain(etDayRemain.getText().toString());
        }
        if (!TCUtils.isEmpty(tvStart.getText().toString())) {
            list.add(new FilterCouponItem(String.format(FilterCouponItem.START, TCDateUtility.formatTimeToModel(tvStart.getText().toString()))));
        }
        if (!TCUtils.isEmpty(tvEnd.getText().toString())) {
            list.add(new FilterCouponItem(String.format(FilterCouponItem.END, TCDateUtility.formatTimeToModel(tvEnd.getText().toString()))));
        }

        TCDateUtility.checkAndCompareDate(tvStart, tvEnd, false);

        if (list.size() > 0) {
            filterCouponModel = new FilterCouponModel(list);
        }

        //keep data for filter dialog open again
        if (!TCUtils.isEmpty(etSearch.getText().toString())) {
            filterCouponModel.setTextSearch(etSearch.getText().toString());
        }
        if (cbDiscountPercent.isChecked()) {
            filterCouponModel.setMinDisCount(String.valueOf(minValueDiscount));
            filterCouponModel.setMaxDisCount(String.valueOf(maxValueDiscount));
        }
        if (cbCash.isChecked()) {
            if (!TCUtils.isEmpty(etMinCash.getText().toString())) {
                filterCouponModel.setMinCash(etMinCash.getText().toString());
            }
            if (!TCUtils.isEmpty(etMaxCash.getText().toString())) {
                filterCouponModel.setMaxCash(etMaxCash.getText().toString());
            }
        }
        if (!TCUtils.isEmpty(etDayRemain.getText().toString())) {
            filterCouponModel.setDayRemain(etDayRemain.getText().toString());
        }
        if (!TCUtils.isEmpty(tvStart.getText().toString())) {
            filterCouponModel.setStartDay(tvStart.getText().toString());
        }
        if (!TCUtils.isEmpty(tvEnd.getText().toString())) {
            filterCouponModel.setEndDay(tvEnd.getText().toString());
        }

        mListener.onFilterCoupon(filterCouponModel);
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
