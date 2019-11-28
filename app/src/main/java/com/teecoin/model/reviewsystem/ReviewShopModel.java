package com.teecoin.model.reviewsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ReviewShopModel implements Serializable {
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("vendor_id")
    @Expose
    private String vendor_id;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("image_ids")
    @Expose
    private ArrayList<String> image_ids;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("xdr")
    @Expose
    private String xdr;
    @SerializedName("transaction_hash")
    @Expose
    private String transaction_hash;

    @SerializedName("transaction_id")
    @Expose
    private String transaction_id;

    @SerializedName("share_instagram")
    @Expose
    private boolean share_instagram;

    @SerializedName("share_facebook")
    @Expose
    private boolean share_facebook;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("visit_types")
    @Expose
    private ArrayList<String> visit_types;

    @SerializedName("visit_date")
    @Expose
    private String visit_date;


    public ReviewShopModel(String comment, String vendor_id, String transaction_id, String rating, ArrayList<String> image_ids, String source, String destination, String amount, boolean share_instagram, boolean share_facebook, String title, ArrayList<String> visitType, String visitDate) {
        this.comment = comment;
        this.vendor_id = vendor_id;
        this.transaction_id=transaction_id;
        this.rating = rating;
        this.image_ids = image_ids;
        this.source = source;
        this.destination = destination;
        this.amount = amount;
        this.share_instagram =share_instagram;
        this.share_facebook=share_facebook;
        this.title = title;
        this.visit_types = visitType;
        this.visit_date = visit_date;

    }

    public ReviewShopModel(String comment, String transaction_id, String rating, ArrayList<String> image_ids, String source, String destination, String amount, boolean share_instagram, boolean share_facebook, String title, ArrayList<String> visitType, String visitDate) {
        this.comment = comment;
        this.transaction_id=transaction_id;
        this.rating = rating;
        this.image_ids = image_ids;
        this.source = source;
        this.destination = destination;
        this.amount = amount;
        this.share_instagram =share_instagram;
        this.share_facebook=share_facebook;
        this.title = title;
        this.visit_types = visitType;
        this.visit_date = visit_date;
    }

    public String getComment() {
        return comment;
    }

    public String getRating() {
        return rating;
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

    public void setXdr(String xdr) {
        this.xdr = xdr;
    }

    public void setTransaction_hash(String transaction_hash) {
        this.transaction_hash = transaction_hash;
    }


}
