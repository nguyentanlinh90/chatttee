package com.teecoin.feature.general.mywallet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.feature.walletSystem.convertToTec.ConvertToTecScreen;
import com.teecoin.feature.walletSystem.history.FiatTransactionHistoryScreen;
import com.teecoin.feature.walletSystem.withdraw.WithdrawScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.BalanceModel;
import com.teecoin.model.general.FiatWalletModel;
import com.teecoin.model.walletsystem.ConvertCurrenciesModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.general.GeneralGetShopBalanceRequest;
import com.teecoin.myapi.apirequest.walletsystem.ConvertCurrenciesRequest;
import com.teecoin.myapi.apirequest.walletsystem.shop.ShopFiatTransactionHistoryRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.GeneralRequestTarget;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.ui.TCSwipeRefreshLayout;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;
import core.view.RecycleListener;

public class FiatWalletScreen extends TCWalletBaseFragment implements APIResponseListener {

    @BindView(R.id.frg_fiat_wallet_account_refresh_view)
    TCSwipeRefreshLayout refreshView;
    @BindView(R.id.frg_sgd_wallet_tv_balance)
    TextView tvBalance;

    @BindView(R.id.frg_wallet_v_equivalent)
    View viewEquivalent;
    @BindView(R.id.frg_sgd_wallet_tv_quivalent)
    TextView tvEquivalent;

    @BindView(R.id.view_latest_transaction_rcv_transaction_list)
    TCRecyclerView rcv_transaction;
    private FiatTransactionAdapter adapterUser;
    private ArrayList<FiatWalletModel> listData;

    private BalanceModel balanceModel;

    public static FiatWalletScreen getInstance() {
        return new FiatWalletScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fiat_wallet, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
    }

    @Override
    public void onBindView() {
        registerSingleClick(R.id.frg_fiat_wallet_ll_convert,R.id.frg_fiat_wallet_ll_withdraw,R.id.view_latest_transaction_ll_view_all_transaction);
        getBalance();
        rcv_transaction.setHasFixedSize(true);
        rcv_transaction.setNestedScrollingEnabled(false);
        rcv_transaction.setNextPageIndex(1);
        listData = new ArrayList<>();
        adapterUser = new FiatTransactionAdapter(LayoutInflater.from(getActiveActivity()), listData, new RecycleListener<FiatWalletModel>() {
            @Override
            public void onItemClick(View view, FiatWalletModel item, int position, EnumMgr.ClickType clickType) {
                itemSGDWalletClick(item);

            }
        });
        rcv_transaction.setAdapter(adapterUser);
        getListData();

        refreshData();

    }

    private void getBalance() {
        requestApi(new GeneralGetShopBalanceRequest(GeneralRequestTarget.SHOP_GET_BALANCE, this));
    }

    private void getListData() {
        requestApi(new ShopFiatTransactionHistoryRequest(rcv_transaction.getNextPageIndex(), this));
    }

    @Override
    public void onSingleClick(View v, Object object) {
        super.onSingleClick(v, object);
        switch (v.getId()) {
            case R.id.frg_fiat_wallet_ll_convert:
                if (null != balanceModel)
                    addFragment(ConvertToTecScreen.getInstance());
                break;

            case R.id.frg_fiat_wallet_ll_withdraw:
                if (null != balanceModel)
                    addFragment(WithdrawScreen.getInstance(balanceModel));
                break;

            case R.id.view_latest_transaction_ll_view_all_transaction:
                addFragment(FiatTransactionHistoryScreen.getInstance());
                break;
        }
    }

    @Override
    public void onBaseDestroyView() {
        unregisterSingleClick(R.id.frg_fiat_wallet_ll_convert,R.id.frg_fiat_wallet_ll_withdraw,R.id.view_latest_transaction_ll_view_all_transaction);
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == GeneralRequestTarget.SHOP_GET_BALANCE) {
            balanceModel = (BalanceModel) response.getResult();
            if (null != balanceModel && TCUtils.convertToDouble(balanceModel.getFiat_amount()) > 0) {
                tvBalance.setText(TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, balanceModel.getFiat_amount()));
                requestApi(new ConvertCurrenciesRequest(balanceModel.getCurrency_code(), TCUtils.isProductionMode() ? EnumMgr.Currentcy.TEC.getValue()
                        : EnumMgr.Currentcy.TEECOIN.getValue(), balanceModel.getFiat_amount(), new APIResponseListener() {
                    @Override
                    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                        ConvertCurrenciesModel convertCurrenciesModel = (ConvertCurrenciesModel) response.getResult();
                        if (null != convertCurrenciesModel) {
                            viewEquivalent.setVisibility(View.VISIBLE);
                            String tec = TCUtils.getString(R.string.integrating_with_exchanger);
                            if (!TCUtils.isEmpty(convertCurrenciesModel.getConvertedAmount())) {
                                tec = String.format(TCUtils.getString(R.string.text_parameter_with_tec),
                                        TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, convertCurrenciesModel.getConvertedAmount()));
                            }
                            tvEquivalent.setText(tec);
                        }
                    }

                    @Override
                    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                        viewEquivalent.setVisibility(View.INVISIBLE);
                    }
                }));
            } else {
                tvBalance.setText(TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, "0"));
                viewEquivalent.setVisibility(View.VISIBLE);
                tvEquivalent.setText(String.format(TCUtils.getString(R.string.text_parameter_with_tec),
                        TCUtils.formatMoney(TCConstant.TWO_DECIMAL_FULL_FORMAT, "0")));

            }
        } else if (requestTarget == WalletRequestTarget.SHOP_SGD_TRANSACTION_HISTORY_LIST) {
            if (listData.size() > 0)
                listData.clear();
            ArrayList<FiatWalletModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();
            if (list != null && list.size() > 0) {
                if (list.size() >= 5) {
                    for (int i = 0; i < 5; i++) {
                        listData.add(list.get(i));
                    }
                } else {
                    listData.addAll(list);
                }
                rcv_transaction.onLoadMoreComplete();
            }
        }
        refreshView.setRefreshing(false);
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        if (requestTarget == GeneralRequestTarget.SHOP_GET_BALANCE) {
        } else if (requestTarget == WalletRequestTarget.SHOP_SGD_TRANSACTION_HISTORY_LIST) {
            rcv_transaction.onLoadMoreComplete();
        }
        refreshView.setRefreshing(false);

    }

    private void refreshData() {
        refreshView.setOnRefreshListener(() -> {
            getBalance();
            getListData();

        });
    }
}
