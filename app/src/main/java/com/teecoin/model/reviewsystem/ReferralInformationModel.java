package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.TeeCoinModel;
import com.teecoin.utils.TCUtils;

import java.io.Serializable;
import java.util.ArrayList;

public class ReferralInformationModel extends TeeCoinModel implements Serializable {

    @SerializedName("summary")
    @Expose
    private ReferralSummary summary;

    @SerializedName("history")
    @Expose
    private ReferralHistory history;


    public ReferralSummary getSummary() {
        return summary;
    }

    public ReferralHistory getHistory() {
        return history;
    }

    public class ReferralSummary implements Serializable {
        @SerializedName("event_url")
        @Expose
        private String event_url;

        @SerializedName("amount")
        @Expose
        private String amount;

        @SerializedName("referral_code")
        @Expose
        private String referral_code;

        @SerializedName("share_content")
        @Expose
        private String share_content;

        public String getShare_content() {
            return share_content;
        }

        public String getEvent_url() {
            return event_url;
        }

        public String getAmount() {
            return amount;
        }

        public String getReferral_code() {
            return referral_code;
        }
    }

    public class ReferralHistory implements Serializable {
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
        private ArrayList<ReferralHistoryItemModel> referralHistoryItems;


        public ArrayList<ReferralHistoryItemModel> getReferralHistoryItems() {
            return referralHistoryItems;
        }

        public int getCount() {
            return count;
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

}
