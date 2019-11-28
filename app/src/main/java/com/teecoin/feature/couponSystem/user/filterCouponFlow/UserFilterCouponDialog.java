package com.teecoin.feature.couponSystem.user.filterCouponFlow;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.teecoin.R;
import com.teecoin.feature.couponSystem.user.discoverVendor.DiscoverVendorCatalogueImageAdapter;
import com.teecoin.model.couponsystem.VendorCategoryModel;
import com.teecoin.ui.RangeSeekBar;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.teecoin.utils.TCUtils.containsItemFilter;
import static com.teecoin.utils.TCUtils.getTextFilterToContains;
import static core.base.BaseApplication.getActiveActivity;

@SuppressLint("ValidFragment")
public class UserFilterCouponDialog extends BottomSheetDialogFragment implements  View.OnClickListener {
    @BindView(R.id.view_header_search_coupon_discover_tv_title)
    TextView tv_title;
    @BindView(R.id.view_header_search_coupon_discover_tv_reset)
    TextView tvResetAll;
    @BindView(R.id.view_list_catalogue_rcv_image)
    TCRecyclerView rcv_catalogue_image;
    @BindView(R.id.dialog_coupon_user_filter_catalogue_cb_ending_soon)
    RadioButton cbEndingSoon;
    @BindView(R.id.dialog_coupon_user_filter_catalogue_cb_latest)
    RadioButton cbLatest;
    @BindView(R.id.dialog_coupon_user_filter_catalogue_cb_popular)
    RadioButton cbPopular;
    @BindView(R.id.dialog_coupon_user_filter_catalogue_cb_recommend_coupon)
    RadioButton cbRecommendCoupon;
    @BindView(R.id.dialog_coupon_user_filter_catalogue_cb_low_to_high)
    RadioButton cbLowToHigh;
    @BindView(R.id.dialog_coupon_user_filter_catalogue_cb_high_to_low)
    RadioButton cbHighToLow;
    @BindView(R.id.dialog_coupon_user_filter_catalogue_et_min_price)
    EditText etMinPrice;
    @BindView(R.id.dialog_coupon_user_filter_catalogue_et_max_price)
    EditText etMaxPrice;
    @BindView(R.id.filter_discover_tv_apply)
    TextView tvApply;
    @BindView(R.id.filter_discover_tv_cancel)
    TextView tvCancel;
    Unbinder unbinder;
    private ArrayList<VendorCategoryModel> mListCatalogue;
    private VendorCategoryModel mCatalogueSelect;
    private UserFilterCouponListener mListener;
    private DiscoverVendorCatalogueImageAdapter imageAdapter;
    private int minValueDiscount = 20;
    private int maxValueDiscount = 80;
    private FilterCouponModel mFilterCouponModel;

    @SuppressLint("ValidFragment")
    public UserFilterCouponDialog(FilterCouponModel filterCouponModel, ArrayList<VendorCategoryModel> listCatalogue, VendorCategoryModel catalogueSelect, UserFilterCouponListener filterCouponListener) {
        this.mListCatalogue = listCatalogue;
        this.mCatalogueSelect = catalogueSelect;
        this.mListener = filterCouponListener;
        this.mFilterCouponModel = filterCouponModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_coupon_user_filter_catalogue, container, false);
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
                // behavior.setPeekHeight(0);//dismiss when scroll to bottom
            }
        });
        init();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetDialog);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        // tv_title.setText(TCUtils.getString(R.string.coupon_filter));
        if (mListCatalogue != null) {
            imageAdapter = new DiscoverVendorCatalogueImageAdapter(LayoutInflater.from(getActiveActivity()), mListCatalogue, (view, item, position, clickType) -> {
                imageAdapter.unSelectedAllItem();
                imageAdapter.setSelectedItem(position);
//            imageAdapter = new UserCouponCatalogueImageAdapter(LayoutInflater.from(getContext()), mListCatalogue, (view, item, position, clickType) -> {
//                if (item.isSelector())
//                    return;
//                imageAdapter.unSelectedAllItem();
//                imageAdapter.setSelectedItem(position);
//                setCatalogueSelect(item);
            });
            rcv_catalogue_image.setAdapter(imageAdapter);
            imageAdapter.setItemSelect(mCatalogueSelect);
        }


        RangeSeekBar<Integer> rangeSeekBarDiscountPercent = new RangeSeekBar<>(getActivity());
        rangeSeekBarDiscountPercent.setRangeValues(0, 100);
        if (mFilterCouponModel != null) {
            cbEndingSoon.setChecked(containsItemFilter(mFilterCouponModel, FilterCouponItem.ENDING_SOON));
            cbLatest.setChecked(containsItemFilter(mFilterCouponModel, FilterCouponItem.LATEST));
            cbPopular.setChecked(containsItemFilter(mFilterCouponModel, FilterCouponItem.POPULAR));
            cbRecommendCoupon.setChecked(containsItemFilter(mFilterCouponModel, FilterCouponItem.RECOMMENDED_COUPON));
            cbLowToHigh.setChecked(containsItemFilter(mFilterCouponModel, FilterCouponItem.LOW_TO_HIGH));
            cbHighToLow.setChecked(containsItemFilter(mFilterCouponModel, FilterCouponItem.HIGH_TO_LOW));

            if (containsItemFilter(mFilterCouponModel, getTextFilterToContains(FilterCouponItem.MIN_PRICE))) {
                etMinPrice.setText(mFilterCouponModel.getMinPrice());
            }
            if (containsItemFilter(mFilterCouponModel, getTextFilterToContains(FilterCouponItem.MAX_PRICE))) {
                etMaxPrice.setText(mFilterCouponModel.getMaxPrice());
            }

            if (containsItemFilter(mFilterCouponModel, getTextFilterToContains(FilterCouponItem.DISCOUNT))) {
                rangeSeekBarDiscountPercent.setSelectedMinValue(Integer.parseInt(mFilterCouponModel.getMinDisCount()));
                rangeSeekBarDiscountPercent.setSelectedMaxValue(Integer.parseInt(mFilterCouponModel.getMaxDisCount()));
            } else {
                rangeSeekBarDiscountPercent.setSelectedMinValue(minValueDiscount);
                rangeSeekBarDiscountPercent.setSelectedMaxValue(maxValueDiscount);
            }

        } else {
            rangeSeekBarDiscountPercent.setSelectedMinValue(minValueDiscount);
            rangeSeekBarDiscountPercent.setSelectedMaxValue(maxValueDiscount);
        }
        rangeSeekBarDiscountPercent.setOnRangeSeekBarChangeListener((bar, minValue, maxValue) -> {
            minValueDiscount = minValue;
            maxValueDiscount = maxValue;
        });

        onListener();
    }

//    private void setCatalogueSelect(UserCouponCatalogueModel catalogueSelect) {
//        imageAdapter.setItemSelect(catalogueSelect);
//    }

    private void onListener() {

        tvResetAll.setOnClickListener(this);
        tvApply.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    private void setCheckBoxToUnSelect(CheckBox... checkBoxes) {
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setChecked(false);
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_header_search_coupon_discover_tv_reset:
                imageAdapter.setItemSelect(mCatalogueSelect);
                cbEndingSoon.setChecked(false);
                cbLatest.setChecked(false);
                cbPopular.setChecked(false);
                cbRecommendCoupon.setChecked(false);
                cbLowToHigh.setChecked(false);
                cbHighToLow.setChecked(false);
                etMinPrice.setText(null);
                etMaxPrice.setText(null);

                break;

            case R.id.filter_discover_tv_apply:
                filterCatalogueCoupon();
                break;
            case R.id.filter_discover_tv_cancel:
                dismiss();
                break;
        }
    }

    private void filterCatalogueCoupon() {
        ArrayList<FilterCouponItem> list = new ArrayList<>();
        FilterCouponModel filterCouponModel = new FilterCouponModel();

        if (cbEndingSoon.isChecked()) {
            list.add(new FilterCouponItem(FilterCouponItem.ENDING_SOON));
        }
        if (cbLatest.isChecked()) {
            list.add(new FilterCouponItem(FilterCouponItem.LATEST));
        }
        if (cbPopular.isChecked()) {
            list.add(new FilterCouponItem(FilterCouponItem.POPULAR));
        }
        if (cbRecommendCoupon.isChecked()) {
            list.add(new FilterCouponItem(FilterCouponItem.RECOMMENDED_COUPON));
        }
        if (cbLowToHigh.isChecked()) {
            list.add(new FilterCouponItem(FilterCouponItem.LOW_TO_HIGH));
        }
        if (cbHighToLow.isChecked()) {
            list.add(new FilterCouponItem(FilterCouponItem.HIGH_TO_LOW));
        }
        if (!TCUtils.isEmpty(etMinPrice.getText().toString())) {
            list.add(new FilterCouponItem(String.format(FilterCouponItem.MIN_PRICE, etMinPrice.getText().toString())));
            filterCouponModel.setMinPrice(etMinPrice.getText().toString());
        }
        if (!TCUtils.isEmpty(etMaxPrice.getText().toString())) {
            if (!TCUtils.isEmpty(etMinPrice.getText().toString())) {
                if (Long.parseLong(etMaxPrice.getText().toString()) >= Long.parseLong(etMinPrice.getText().toString())) {
                    list.add(new FilterCouponItem(String.format(FilterCouponItem.MAX_PRICE, etMaxPrice.getText().toString())));
                    filterCouponModel.setMaxPrice(etMaxPrice.getText().toString());
                } else {
                    Toast.makeText(getContext(), TCUtils.getString(R.string.coupon_filter_please_input_max_cash_bigger_min_cash), Toast.LENGTH_LONG).show();
                    return;
                }
            } else {
                list.add(new FilterCouponItem(String.format(FilterCouponItem.MAX_PRICE, etMaxPrice.getText().toString())));
                filterCouponModel.setMaxPrice(etMaxPrice.getText().toString());
            }
        }

        if (list.size() > 0) {
            filterCouponModel = new FilterCouponModel(list);
        }

        //keep data for filter dialog open again

        if (!TCUtils.isEmpty(etMinPrice.getText().toString())) {
            filterCouponModel.setMinPrice(etMinPrice.getText().toString());
        }
        if (!TCUtils.isEmpty(etMaxPrice.getText().toString())) {
            filterCouponModel.setMaxPrice(etMaxPrice.getText().toString());
        }


        // filterCouponModel.setId(iconAdapter.getItemSelect());
        filterCouponModel.setId(imageAdapter.getItemSelect());
        mListener.onFilterCoupon(mListCatalogue,filterCouponModel);
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
