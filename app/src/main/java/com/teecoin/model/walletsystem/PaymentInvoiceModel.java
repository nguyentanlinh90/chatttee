package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;

public class PaymentInvoiceModel implements Serializable {

    @SerializedName("invoice")
    @Expose
    private String invoice;

    @SerializedName("invoice_amount")
    @Expose
    private double invoice_amount;

    @SerializedName("shop")
    @Expose
    private ShopInvoiceModel shop;

    private double walletBalance;
    private double payment;
    private double teeCoinFee;
    private double paymentAmountConverted;
    private double remainAmount;
    private double totalTeeCoin;
    private String transactionHash;
    private String transactionId;
    private double maxPaymentInput;


    public PaymentInvoiceModel() {
    }

    public PaymentInvoiceModel(String invoiceId, double amount, ShopInvoiceModel shopInvoiceModel) {
        this.invoice = invoiceId;
        this.invoice_amount = amount;
        this.shop = shopInvoiceModel;
    }

    public boolean validate(String json) {
        JSONObject object = null;
        try {
            object = new JSONObject(json);
            for (Field f : this.getClass().getDeclaredFields()) {
                SerializedName serializedName = f.getAnnotation(SerializedName.class);
                if (serializedName != null) {
                    if (f.getType() == ShopInvoiceModel.class && object.has(serializedName.value())) {
                        ShopInvoiceModel shopInvoiceModel = new ShopInvoiceModel();
                        boolean isValid = shopInvoiceModel.validate(object.getString(serializedName.value()));
                        if (!isValid)
                            return false;
                    } else {
                        if (!object.has(serializedName.value())) {
                            return false;
                        }
                    }
                }
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public double getInvoice_amount() {
        return invoice_amount;
    }

    public void setInvoice_amount(double invoice_amount) {
        this.invoice_amount = invoice_amount;
    }

    public ShopInvoiceModel getShop() {
        return shop;
    }

    public void setShop(ShopInvoiceModel shop) {
        this.shop = shop;
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public double getTeeCoinFee() {
        return teeCoinFee;
    }

    public void setTeeCoinFee(double teeCoinFee) {
        this.teeCoinFee = teeCoinFee;
    }

    public double getPaymentAmountConverted() {
        return paymentAmountConverted;
    }

    public void setPaymentAmountConverted(double paymentAmountConverted) {
        this.paymentAmountConverted = paymentAmountConverted;
    }

    public double getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(double remainAmount) {
        this.remainAmount = remainAmount;
    }

    public double getTotalTeeCoin() {
        return totalTeeCoin;
    }

    public void setTotalTeeCoin(double totalTeeCoin) {
        this.totalTeeCoin = totalTeeCoin;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void calculate(double payment) {
        if (payment <= TCConstant.ZERO)
            payment = TCConstant.ZERO;
        this.payment = payment;
        paymentAmountConverted = TCUtils.roundFloor(TCUtils.multiply(this.payment, shop.getEx_tee()), TCConstant.ROUND_SEVEN_DECIMAL);
        teeCoinFee = TCConstant.TEECOIN_FEE;//currently fix the user's fee is 0.01 TEC
        totalTeeCoin = payment + teeCoinFee;
        remainAmount = TCUtils.roundCeil(TCUtils.subtract(invoice_amount, paymentAmountConverted), TCConstant.ROUND_THREE_DECIMAL);
    }

    //Change: User can pay max (Max TEC payment allowed x Amount / Exchange Rate) by TEC
    public void calculateMaxTeeCoin() {
        this.payment = TCUtils.roundFloor(TCUtils.subtract(this.walletBalance, TCConstant.TEECOIN_FEE_RATE) > 0 ?
                        TCUtils.subtract(this.walletBalance, TCConstant.TEECOIN_FEE_RATE) : 0,
                TCConstant.ROUND_FIVE_DECIMAL);
        paymentAmountConverted = TCUtils.roundFloor(TCUtils.multiply(this.payment, shop.getEx_tee()), TCConstant.ROUND_SEVEN_DECIMAL);
        teeCoinFee = TCConstant.TEECOIN_FEE; //currently fix the user's fee is 0.01 TEC
        totalTeeCoin = TCUtils.roundFloor(payment + teeCoinFee, TCConstant.ROUND_SEVEN_DECIMAL);
        remainAmount = TCUtils.roundCeil(TCUtils.subtract(invoice_amount, paymentAmountConverted), TCConstant.ROUND_THREE_DECIMAL);
    }

    public void calculateMaxPaymentAmount() {
        this.payment = TCUtils.roundCeil((TCUtils.divide(this.invoice_amount, this.shop.getEx_tee())) * TCUtils.convertReturnRateToDecimal(shop.getMax_payment_rate()), TCConstant.ROUND_FIVE_DECIMAL);
        teeCoinFee = TCConstant.TEECOIN_FEE; //currently fix the user's fee is 0.01 TEC
        paymentAmountConverted = TCUtils.roundFloor(TCUtils.multiply(this.payment, shop.getEx_tee()), TCConstant.ROUND_SEVEN_DECIMAL);
        totalTeeCoin = payment + teeCoinFee;
        this.remainAmount = TCUtils.roundCeil(TCUtils.subtract(invoice_amount, paymentAmountConverted), TCConstant.ROUND_THREE_DECIMAL);
    }

    public boolean isMaxPaymentAmountOK() {
        if (this.paymentAmountConverted > this.maxPaymentInput) {
            double tempPayment = TCUtils.subtract(this.payment, 0.00001);
            double temp = TCUtils.roundFloor(TCUtils.multiply(tempPayment, shop.getEx_tee()), TCConstant.ROUND_SEVEN_DECIMAL);
            if (temp < this.maxPaymentInput) {
                paymentAmountConverted = TCUtils.roundFloor(TCUtils.multiply(this.payment, shop.getEx_tee()), TCConstant.ROUND_SEVEN_DECIMAL);
                this.remainAmount = 0;
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void setMaxPaymentInput(double maxPaymentInput) {
        this.maxPaymentInput = maxPaymentInput;
    }


    public EnumMgr.CalculationType validatePayment() {
        if (TCUtils.subtract(this.walletBalance, this.teeCoinFee) <= 0) {
            return EnumMgr.CalculationType.NotEnoughTEC;
        } else if (this.payment + this.teeCoinFee > this.walletBalance) {
            return EnumMgr.CalculationType.OverTeeCoin;
        } else if (this.paymentAmountConverted > this.maxPaymentInput && this.maxPaymentInput == this.invoice_amount) {
            calculateMaxPaymentAmount();
            return EnumMgr.CalculationType.OverInvoiceAmount;
        } else if (!isMaxPaymentAmountOK()) {
            calculateMaxPaymentAmount();
            return EnumMgr.CalculationType.OverMaxPaymentAllow;
        } else {
            return EnumMgr.CalculationType.PaymentOK;
        }
    }
}
