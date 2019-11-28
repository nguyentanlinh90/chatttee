package com.teecoin.feature.walletSystem.purchase;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.feature.walletSystem.confirmPurchase.ConfirmPurchaseScreen;
import com.teecoin.utils.TCDateUtility;
import com.teecoin.utils.TCUtils;

import butterknife.BindView;

public class PurchaseScreen extends TCWalletBaseFragment {

    @BindView(R.id.frg_new_purchase_ll_from_date)
    View ll_from_date;

    @BindView(R.id.frg_new_purchase_ll_to_date)
    View ll_to_date;

    @BindView(R.id.frg_new_purchase_tv_search)
    View tv_search;

    @BindView(R.id.frg_new_purchase_ll_search_result)
    View ll_search_result;

    TextView tv_from_date;
    TextView tv_to_date;


    public static PurchaseScreen getInstance() {
        return new PurchaseScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_purchase, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        updateTitleHeader(TCUtils.getString(R.string.new_purchase_upper_case));
        showButtonBackToolbar();
        showMenuNextBottom();
        if (ll_search_result.getVisibility() == View.VISIBLE) {
            showFooter();
            showButtonNext();
        } else {
            hideFooter();
        }
        hideButtonDone();
    }

    @Override
    public void onBindView() {
        initView();
        fillData();
        initClickEvent();
    }

    private void initView() {
        tv_from_date = ll_from_date.findViewById(R.id.view_filter_calendar_item_tv_date);
        tv_to_date = ll_to_date.findViewById(R.id.view_filter_calendar_item_tv_date);
        ll_search_result.setVisibility(View.GONE);
    }

    private void fillData() {

    }

    private void initClickEvent() {
        ll_from_date.setOnClickListener(v -> openCalendar(tv_from_date));
        ll_to_date.setOnClickListener(v -> openCalendar(tv_to_date));
        tv_search.setOnClickListener(v -> search());
    }

    private void search() {
        showFooter();
        showButtonNext();
        ll_search_result.setVisibility(View.VISIBLE);
    }

    public boolean validate() {
        addFragment(ConfirmPurchaseScreen.getInstance());
        return true;
    }

    private void openCalendar(TextView tvDate) {
        DatePickerDialog dialog = new DatePickerDialog(getActiveActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tvDate.setText(TCDateUtility.formatDate(year, month, dayOfMonth, TCDateUtility.DateFormatDefinition.DD_MM_YYYY));
            }
        }, TCDateUtility.getCurrentYear(), TCDateUtility.getCurrentMonth(), TCDateUtility.getCurrentDayOfMonth());
        dialog.show();
    }
}
