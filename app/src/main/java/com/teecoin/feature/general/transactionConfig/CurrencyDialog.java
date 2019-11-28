package com.teecoin.feature.general.transactionConfig;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.teecoin.R;
import com.teecoin.base.TCBaseDialog;
import com.teecoin.base.TCConfirmListener;
import com.teecoin.model.general.CurrencyModel;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;

import java.util.ArrayList;

import butterknife.BindView;
import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class CurrencyDialog extends TCBaseDialog implements RecycleListener<CurrencyModel> {

    @BindView(R.id.dialog_rcv_currency)
    TCRecyclerView rcv_currency;
    @BindView(R.id.iv_close)
    ImageView iv_close;
    private TCConfirmListener confirm_listener;
    private ArrayList<CurrencyModel> currencyList;
    private CurrencyAdapter adapter;

    public CurrencyDialog(Context context, ArrayList<CurrencyModel> currencyList, TCConfirmListener confirm_listener) {
        super(context);
        this.confirm_listener = confirm_listener;
        this.currencyList = currencyList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_currency);
    }

    @Override
    protected void initContentView() {
        adapter = new CurrencyAdapter(LayoutInflater.from(getActiveActivity()), currencyList, this);
        setupRecycler();

    }

    @Override
    protected void onViewClick() {
        iv_close.setOnClickListener(v -> dismiss());
    }

    private void setupRecycler() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActiveActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_currency.setLayoutManager(new LinearLayoutManager(getActiveActivity(), LinearLayoutManager.VERTICAL, false));
        rcv_currency.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, CurrencyModel item, int position, EnumMgr.ClickType clickType) {
        confirm_listener.onConfirmed(position, item);
        dismiss();
    }
}
