package com.teecoin.model.walletsystem;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.utils.EnumMgr;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TransactionDetailModel extends RealmObject implements Serializable, Parcelable {
    public static final String PRIMARY_KEY = "transaction_hash";
    public static final String TRANSACTION_ID = "transaction_id";
    public static final String CREATED_COLUMN = "created";
    public static final String TYPE_COLUMN = "type";
    public static final int LIMIT = 10;
    public static final Creator<TransactionDetailModel> CREATOR = new Creator<TransactionDetailModel>() {
        @Override
        public TransactionDetailModel createFromParcel(Parcel in) {
            return new TransactionDetailModel(in);
        }

        @Override
        public TransactionDetailModel[] newArray(int size) {
            return new TransactionDetailModel[size];
        }
    };

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("type")
    @Expose
    private String type;
    @PrimaryKey  //Realm Primary Key
    @SerializedName("transaction_hash")
    @Expose
    private String transaction_hash;
    @SerializedName("transaction_id")
    @Expose
    private String transaction_id;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("invoice")
    @Expose
    private String invoice;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("paging_token")
    @Expose
    private String paging_token;
    @SerializedName("is_coinback_success")
    @Expose
    private boolean is_coinback_success;
    @SerializedName("is_review_success")
    @Expose
    private boolean is_review_success;
    @SerializedName("shop_name")
    @Expose
    private String shop_name;
    @SerializedName("sender")
    @Expose
    private String sender;
    @SerializedName("receiver")
    @Expose
    private String receiver;
    @SerializedName("serial")
    @Expose
    private String serial;

    @SerializedName("icon")
    @Expose
    private String icon;

    public String getStatus() {
        return status;
    }


    public TransactionDetailModel() {
    }

    public TransactionDetailModel(PaymentDetailModel paymentDetailModel) {
        this.url = "";//TODO
        this.type = paymentDetailModel.getType();
        this.transaction_hash = paymentDetailModel.getTransactionHash();
        this.transaction_id = paymentDetailModel.getTransactionId();
        this.source = paymentDetailModel.getSource();
        this.destination = paymentDetailModel.getDestination();
        this.created = paymentDetailModel.getCreated();
        this.invoice = paymentDetailModel.getInvoice();
        this.amount = paymentDetailModel.getAmount();
        this.paging_token = paymentDetailModel.getPagingToken();
        this.is_coinback_success = paymentDetailModel.getCoinbackSuccess();
        this.shop_name = paymentDetailModel.getShopName();
    }

    public TransactionDetailModel(CoinBackResultModel coinBackResultModel) {
        this.url = "";//TODO
        this.type = EnumMgr.TransactionType.CoinBack.getValue();
        this.transaction_hash = coinBackResultModel.getTransactionHash();
        this.transaction_id = coinBackResultModel.getTransactionId();
        this.source = coinBackResultModel.getSource();
        this.destination = coinBackResultModel.getDestination();
        this.created = coinBackResultModel.getCreated();
        this.invoice = coinBackResultModel.getInvoice();
        this.amount = coinBackResultModel.getAmount();
        this.paging_token = coinBackResultModel.getPagingToken();
        this.is_coinback_success = true;
        this.shop_name = coinBackResultModel.getShopName();
    }

    public TransactionDetailModel(TransactionDetailModel clone) {
        this.url = clone.getUrl();
        this.type = clone.getType();
        this.transaction_hash = clone.getTransaction_hash();
        this.transaction_id = clone.getTransaction_id();
        this.source = clone.getSource();
        this.destination = clone.getDestination();
        this.created = clone.getCreated();
        this.invoice = clone.getInvoice();
        this.amount = clone.getAmount();
        this.paging_token = clone.getPaging_token();
        this.is_coinback_success = clone.isCoinbackSuccess();
        this.shop_name = clone.getShop_name();
    }

    public TransactionDetailModel(String transaction_hash) {
        this.transaction_hash = transaction_hash;
    }

    protected TransactionDetailModel(Parcel in) {
        url = in.readString();
        type = in.readString();
        transaction_hash = in.readString();
        transaction_id = in.readString();
        source = in.readString();
        destination = in.readString();
        created = in.readString();
        invoice = in.readString();
        amount = in.readString();
        paging_token = in.readString();
        is_coinback_success = in.readByte() != 0;
        shop_name = in.readString();
    }

    public void updateByHash(String transaction_hash) {
        this.transaction_hash = transaction_hash;

    }

    public void updateRow(TransactionDetailModel transactionDetailModel) {
        this.url = transactionDetailModel.getUrl();
        this.type = transactionDetailModel.getType();
//        this.transaction_hash = transactionDetailModel.getTransaction_hash();
        this.transaction_id = transactionDetailModel.getTransaction_id();
        this.source = transactionDetailModel.getSource();
        this.destination = transactionDetailModel.getDestination();
        this.created = transactionDetailModel.getCreated();
        this.invoice = transactionDetailModel.getInvoice();
        this.amount = transactionDetailModel.getAmount();
        this.paging_token = transactionDetailModel.getPaging_token();
        this.is_coinback_success = transactionDetailModel.isCoinbackSuccess();
        this.is_review_success = transactionDetailModel.isIs_review_success();
        this.shop_name = transactionDetailModel.getShop_name();
    }

    public String getTransactionHash() {
        return transaction_hash;
    }

    public void setTransactionHash(String transaction_hash) {
        this.transaction_hash = transaction_hash;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public String getTransaction_hash() {
        return transaction_hash;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getCreated() {
        return created;
    }

    public String getInvoice() {
        return invoice;
    }

    public String getAmount() {
        return amount;
    }

    public String getPaging_token() {
        return paging_token;
    }

    public boolean isCoinbackSuccess() {
        return is_coinback_success;
    }

    public void setIs_coinback_success(boolean is_coinback_success) {
        this.is_coinback_success = is_coinback_success;
    }

    public String getShop_name() {
        return shop_name;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }


    public boolean isIs_review_success() {
        return is_review_success;
    }

    public void setIs_review_success(boolean is_review_success) {
        this.is_review_success = is_review_success;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(url);
        parcel.writeString(type);
        parcel.writeString(transaction_hash);
        parcel.writeString(transaction_id);
        parcel.writeString(source);
        parcel.writeString(destination);
        parcel.writeString(created);
        parcel.writeString(invoice);
        parcel.writeString(amount);
        parcel.writeString(paging_token);
        parcel.writeByte((byte) (is_coinback_success ? 1 : 0));
        parcel.writeString(shop_name);
        parcel.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void update(TransactionDetailModel transaction) {

        this.status = transaction.getStatus();
        this.url = transaction.getUrl();
        this.type = transaction.getType();
        this.transaction_id = transaction.getTransaction_id();
        this.source = transaction.getSource();
        this.destination = transaction.getDestination();
        this.created = transaction.getCreated();
        this.invoice = transaction.getInvoice();
        this.amount = transaction.getAmount();
        this.paging_token = transaction.getPaging_token();
        this.is_coinback_success = transaction.isCoinbackSuccess();
        this.is_review_success = transaction.isIs_review_success();
        this.shop_name = transaction.getShop_name();
        this.sender = transaction.getSender();
        this.receiver = transaction.getReceiver();
        this.serial = transaction.getSerial();
        this.icon = transaction.getIcon();
    }

    public enum TransactionDetailStatus {
        UNKNOWN,
        PROCESSING,
        SUCCESS,
        FAILED
    }
}
