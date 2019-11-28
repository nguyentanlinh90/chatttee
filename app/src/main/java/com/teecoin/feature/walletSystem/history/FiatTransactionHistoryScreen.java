package com.teecoin.feature.walletSystem.history;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teecoin.R;
import com.teecoin.base.TCWalletBaseFragment;
import com.teecoin.feature.general.mywallet.FiatTransactionAdapter;
import com.teecoin.feature.payment.shopPaymentDetail.ShopPaymentDetailScreen;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.general.FiatWalletModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.walletsystem.shop.ShopFiatTransactionHistoryRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.WalletRequestTarget;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCScreenSize;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

import butterknife.BindView;
import core.view.RecycleListener;

public class FiatTransactionHistoryScreen extends TCWalletBaseFragment implements RecycleListener<FiatWalletModel>, APIResponseListener {
    @BindView(R.id.frg_history_rcv)
    TCRecyclerView rcv_history;

    private ArrayList<FiatWalletModel> listData;

    public static FiatTransactionHistoryScreen getInstance() {
        return new FiatTransactionHistoryScreen();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onBaseResume() {
        super.onBaseResume();
        showHeader();
        updateTitleHeader(TCUtils.getString(R.string.text_history).toUpperCase());
        showButtonBackToolbar();
        showFooter();
        showTabMenuBottom();
    }

    @Override
    public void onBindView() {
        setupRecycle();
    }

    private void setupRecycle() {
        listData = new ArrayList<>();

        rcv_history.setLayoutManager(new LinearLayoutManager(
                getActiveActivity(), LinearLayoutManager.VERTICAL, false));
        FiatTransactionAdapter adapterUser = new FiatTransactionAdapter(LayoutInflater.from(getActiveActivity()), listData, (view, item, position, clickType) -> {
            itemSGDWalletClick(item);
        });
        rcv_history.setAdapter(adapterUser);
        rcv_history.setOnLoadMoreListener(new TCRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (rcv_history.isCanLoadMore()) {
                    getListData();
                }
            }

            @Override
            public boolean shouldOverrideRefresh() {
                return false;
            }
        });
        rcv_history.setNextPageIndex(1);
        getListData();
    }

    private void getListData() {
        requestApi(new ShopFiatTransactionHistoryRequest(rcv_history.getNextPageIndex(), this));
    }

    @Override
    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
        if (requestTarget == WalletRequestTarget.SHOP_SGD_TRANSACTION_HISTORY_LIST) {
            ArrayList<FiatWalletModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();
            if (list != null && list.size() > 0) {
                listData.addAll(list);
                rcv_history.onLoadMoreComplete();
                rcv_history.setLimit(((BaseResultsResponseModel) response.getResult()).getCount());
                if (!TCUtils.isEmpty(((BaseResultsResponseModel) response.getResult()).getNext())) {
                    rcv_history.setNextPageIndex(rcv_history.getNextPageIndex() + 1);
                    rcv_history.setCanLoadMore(true);
                } else {
                    rcv_history.setCanLoadMore(false);
                }

                if (rcv_history.getNextPageIndex() == 2 && TCScreenSize.getHeight(getActiveActivity()) > TCScreenSize.SIZE_LARGE_DEFAULT) {
                    if (rcv_history.isCanLoadMore())
                        getListData();
                }
            }

        }
    }

    @Override
    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
        if (requestTarget == WalletRequestTarget.SHOP_SGD_TRANSACTION_HISTORY_LIST) {
            rcv_history.onLoadMoreComplete();
            rcv_history.setCanLoadMore(false);
        }
    }

    @Override
    public void onItemClick(View view, FiatWalletModel item, int position, EnumMgr.ClickType clickType) {
        addFragment(ShopPaymentDetailScreen.getInstance(item.getTransaction_id()));
    }
}
