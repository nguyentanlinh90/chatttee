package com.teecoin.feature.walletSystem.history;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;

import com.teecoin.BuildConfig;
import com.teecoin.TCMainActivity;
import com.teecoin.feature.walletSystem.walletUser.TransactionDetailListAdapter;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.walletsystem.TransactionDetailModel;
import com.teecoin.model.walletsystem.TransactionHistoryListResponseModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.walletsystem.WalletGetTransactionHistoryListRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.ui.TCRecyclerView;
import com.teecoin.utils.TCScreenSize;

import java.util.ArrayList;

import core.view.RecycleListener;

import static core.base.BaseApplication.getActiveActivity;

public class LoadHistoryDataProcess {

    private int pageLoad = 0;
    private Context context;
    private TCRecyclerView recyclerView;
    private ArrayList<TransactionDetailModel> transactionList;
    private RecycleListener<TransactionDetailModel> listener;

    public LoadHistoryDataProcess(Context context, TCRecyclerView recyclerView, RecycleListener<TransactionDetailModel> listener) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.listener = listener;
        setupRecycler();
    }

    private void setupRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false));
        transactionList = new ArrayList<>();

        TransactionDetailListAdapter transactionAdapter = new TransactionDetailListAdapter(LayoutInflater.from(context), transactionList, listener);
        recyclerView.setAdapter(transactionAdapter);

        recyclerView.setOnLoadMoreListener(new TCRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(() -> {
                    pageLoad++;
                    fillData();
                }, 500L);
            }

            @Override
            public boolean shouldOverrideRefresh() {
                return false;
            }
        });
        recyclerView.setNextPageIndex(1);
        getHistoryFromServer(false);
    }

    private void getHistoryFromServer(boolean loadPaging) {
        boolean isLocalDBEmpty = RealmController.getInstance().getData(TransactionDetailModel.class) == null;
        ((TCMainActivity) context).requestApi(new WalletGetTransactionHistoryListRequest(recyclerView.getNextPageIndex(),
                new APIResponseListener() {
                    @Override
                    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                        handleData(response, requestTarget, isLocalDBEmpty, loadPaging);
                    }

                    @Override
                    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                        recyclerView.onLoadMoreComplete();
                        recyclerView.setCanLoadMore(false);
                    }
                }));
    }

    private void handleData(BaseResponseModel response, BaseRequestTarget requestTarget, boolean isLocalDBEmpty, boolean loadPaging) {
        boolean isReachLocalRecord = false;
        TransactionHistoryListResponseModel responseModel = (TransactionHistoryListResponseModel) response.getResult();
        recyclerView.onLoadMoreComplete();
        recyclerView.setLimit(responseModel.getCount());
        recyclerView.setNextPageIndex(recyclerView.getNextPageIndex() + 1);
        recyclerView.setCanLoadMore(true);
        if (responseModel.getTransactionDetailModelList() != null && responseModel.getTransactionDetailModelList().size() > 0) {
            for (TransactionDetailModel model : responseModel.getTransactionDetailModelList()) {
                if (!RealmController.getInstance().isExist(TransactionDetailModel.class,
                        TransactionDetailModel.PRIMARY_KEY, model.getTransaction_hash())) {
                    RealmController.getInstance().insertData(model, TransactionDetailModel.PRIMARY_KEY, model.getTransaction_hash());
                } else {
                    RealmController.getInstance().updateTransactionDetail(model);
                    isReachLocalRecord = true;
                }
            }
            if (!isReachLocalRecord && !isLocalDBEmpty && !loadPaging) {
                getHistoryFromServer(loadPaging);
            } else {
                fillData();
            }
        }
        // check recyclerView not scroll on some device with screen size lagre
        if (recyclerView.getNextPageIndex() == 2 && TCScreenSize.getHeight(getActiveActivity()) > TCScreenSize.SIZE_LARGE_DEFAULT) {
            if (recyclerView.isCanLoadMore())
                pageLoad++;
            getHistoryFromServer(true);
        }
    }

    private void fillData() {
        ArrayList<TransactionDetailModel> listData =
                BuildConfig.IS_CHATEE_APP ? RealmController.getInstance().getTransactionListLimited(pageLoad) :
                        RealmController.getInstance().getTransactionListLimitedWithoutTipAndReview(transactionList.size());
        if (listData != null && listData.size() > 0) {
            transactionList.addAll(listData);
            recyclerView.onLoadMoreComplete();
            if (pageLoad == 0 && listData.size() < TransactionDetailModel.LIMIT) {
                pageLoad++;
                if (pageLoad < recyclerView.getNextPageIndex()) {
                    getHistoryFromServer(true);
                } else {
                    recyclerView.onLoadMoreComplete();
                }

            }
        } else {
            getHistoryFromServer(true);
        }
    }

}
