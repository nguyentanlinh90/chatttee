package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import static com.teecoin.utils.TCUtils.convertToDouble;

public class PaymentInvoiceSubmitModel implements Serializable {
    @SerializedName("invoice_cash_amount")
    @Expose
    private double invoice_cash_amount;

    @SerializedName("paid_cash_amount")
    @Expose
    private double paid_cash_amount;

    @SerializedName("remain_cash_amount")
    @Expose
    private double remain_cash_amount;

    @SerializedName("currency_code")
    @Expose
    private String currency_code;

    @SerializedName("coinback_cash_amount")
    @Expose
    private String coinback_cash_amount;

    @SerializedName("customer")
    @Expose
    private String customer;

    @SerializedName("status")
    @Expose
    private String status;


    public PaymentInvoiceSubmitModel() {
    }

    public PaymentInvoiceSubmitModel(PaymentInfoModel paymentInfoModel) {
        this.invoice_cash_amount = convertToDouble(paymentInfoModel.getTotal());
        this.paid_cash_amount = convertToDouble(paymentInfoModel.getAmountWallet());
        this.remain_cash_amount = convertToDouble(paymentInfoModel.getInputAmountCash());
        this.currency_code = paymentInfoModel.getCurrency();
    }

    public double getInvoice_cash_amount() {
        return invoice_cash_amount;
    }

    public void setInvoice_cash_amount(double invoice_cash_amount) {
        this.invoice_cash_amount = invoice_cash_amount;
    }

    public double getPaid_cash_amount() {
        return paid_cash_amount;
    }

    public void setPaid_cash_amount(double paid_cash_amount) {
        this.paid_cash_amount = paid_cash_amount;
    }

    public double getRemain_cash_amount() {
        return remain_cash_amount;
    }

    public void setRemain_cash_amount(double remain_cash_amount) {
        this.remain_cash_amount = remain_cash_amount;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public String getCoinback_cash_amount() {
        return coinback_cash_amount;
    }

    public String getCustomer() {
        return customer;
    }

    public String getStatus() {
        return status;
    }
}
