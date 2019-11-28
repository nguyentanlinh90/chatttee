package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TipDetailModel {
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("transaction_hash")
    @Expose
    private String transactionHash;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
//    @SerializedName("total_tip")
//    @Expose
//    private String totalTip;

    @SerializedName("tip_amount")
    @Expose
    private String tip_amount;

    @SerializedName("images")
    @Expose
    private ArrayList<ImageModel> images = null;

    @SerializedName("tips")
    @Expose
    private ArrayList<Tip> tips = null;

    @SerializedName("vendor")
    @Expose
    private VendorModel vendorModel;


    public String getCreated() {
        return created;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public String getComment() {
        return comment;
    }

    public String getRating() {
        return rating;
    }

    public String getShopName() {
        return shopName;
    }

//    public String getTotalTip() {
//        return totalTip;
//    }

    public String getTip_amount() {
        return tip_amount;
    }

    public ArrayList<ImageModel> getImages() {
        return images;
    }

    public ArrayList<Tip> getTips() {
        return tips;
    }

    public VendorModel getVendorModel() {
        return vendorModel;
    }

    public void setVendorModel(VendorModel vendorModel) {
        this.vendorModel = vendorModel;
    }

    public class Tip {
        @SerializedName("created")
        @Expose
        private String created;
        @SerializedName("transaction_hash")
        @Expose
        private String transactionHash;
        @SerializedName("transaction_id")
        @Expose
        private String transactionId;
        @SerializedName("source")
        @Expose
        private String source;
        @SerializedName("destination")
        @Expose
        private String destination;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("fee_amount")
        @Expose
        private String feeAmount;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("paging_token")
        @Expose
        private String pagingToken;
        @SerializedName("tipped_by")
        @Expose
        private String tippedBy;

        public String getCreated() {
            return created;
        }

        public String getTransactionHash() {
            return transactionHash;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public String getSource() {
            return source;
        }

        public String getDestination() {
            return destination;
        }

        public String getAmount() {
            return amount;
        }

        public String getFeeAmount() {
            return feeAmount;
        }

        public String getStatus() {
            return status;
        }

        public String getPagingToken() {
            return pagingToken;
        }

        public String getTippedBy() {
            return tippedBy;
        }
    }
}
