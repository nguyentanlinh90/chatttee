package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.TeeCoinModel;
import com.teecoin.utils.TCUtils;

import java.util.List;

public class TransactionHistoryListResponseModel extends TeeCoinModel {

    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("next")
    @Expose
    private String next;

    @SerializedName("previous")
    @Expose
    private String previous;

    @SerializedName("results")
    @Expose
    private List<TransactionDetailModel> transactionDetailModelList;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<TransactionDetailModel> getTransactionDetailModelList() {
        return transactionDetailModelList;
    }

    public void setTransactionDetailModelList(List<TransactionDetailModel> transactionDetailModelList) {
        this.transactionDetailModelList = transactionDetailModelList;
    }

    public int getNextPageIndex() {
        /**
         * example: "http://api-dev.tee-coin.com/v2/clients/862b3ea1-904f-4fe9-a96c-942ea1cbc3d2/history/?page=12",
         * get number 12 in last string after "="
         */
        if (!TCUtils.isEmpty(next)) {
            return Integer.valueOf(next.substring(next.lastIndexOf("=") + 1));
        } else if (!TCUtils.isEmpty(previous)) {
            // Log.e("previous 112",previous);
            try {
                // http://api-dev.tee-coin.com/v2/clients/ece3ed23-e7d8-406c-b2d3-d1e46b407603/history/?page=2
                return Integer.valueOf(previous.substring(previous.lastIndexOf("=") + 1)) + 1; //previous page plus 1 equal to current page
            } catch (NumberFormatException ex) {// can't get page in previous
                // http://api-dev.tee-coin.com/v2/clients/ece3ed23-e7d8-406c-b2d3-d1e46b407603/history/

            }

        }
        return 1;//if only have 1 page, return first page
    }

}
