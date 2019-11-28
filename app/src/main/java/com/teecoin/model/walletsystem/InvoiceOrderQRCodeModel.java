package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.TeeCoinModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

public class InvoiceOrderQRCodeModel extends TeeCoinModel {

    @SerializedName("invoice")
    @Expose
    private String invoice;

    @SerializedName("invoice_amount")
    @Expose
    private String invoice_amount;

    public String getInvoice() {
        return invoice;
    }

    public String getInvoice_amount() {
        return invoice_amount;
    }


    public boolean validate(String json) {
        JSONObject object = null;
        try {
            object = new JSONObject(json);
            for (Field f : this.getClass().getDeclaredFields()) {
                SerializedName serializedName = f.getAnnotation(SerializedName.class);
                if (serializedName != null) {
                    if (!object.has(serializedName.value())) {
                        return false;
                    }
                }
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}
