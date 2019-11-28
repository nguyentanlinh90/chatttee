package com.teecoin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.R;
import com.teecoin.retrofit.StatusCode;
import com.teecoin.utils.TCUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ErrorModel implements Serializable {
    private static final String STATUS_CODE = "status_code";
    private static final String ERRORS = "errors";
    private static final String ERROR_MESSAGE = "error_message";
    private static final String ERROR_CODE = "error_code";

    private static final String DETAIL = "detail";
    private static final String EMAIL = "email";
    private static final String SOURCE = "source";
    private static final String PAYMENT = "payment";
    private static final String INVOICE = "invoice";
    @SerializedName("status_code")
    @Expose
    private int status_code;

    @SerializedName("error_message")
    @Expose
    private String error_message;

    @SerializedName("error_code")
    @Expose
    private String error_code;

    public ErrorModel(String json) {
        try {
            this.error_code = "";
            this.error_message = "";
            JSONObject object = new JSONObject(json);
            this.status_code = object.getInt(STATUS_CODE);
            if (status_code == StatusCode.BAD_REQUEST.getValue()) {
                convertErrorMessage(object);
            } else if (status_code == StatusCode.SERVICE_UNAVAILABLE.getValue()) {
//                this.error_message = object.getString(DETAIL);
                convertErrorMessage(object);
            } else if (status_code == StatusCode.NOT_FOUND.getValue()) {
//                this.error_message = object.getString(DETAIL);
                convertErrorMessage(object);
            } else if (status_code == StatusCode.FORCE_LOGIN.getValue()) {
                convertErrorMessage(object);
            } else if (status_code == StatusCode.INTERNAL_SERVER_ERROR.getValue()) {
//                convertErrorMessage(object);
                this.error_message = TCUtils.getString(R.string.internal_server_error);
            }
        } catch (JSONException e) {
            this.error_message = "System error! Can not convert json object";
        }
    }

    public ErrorModel(int status_code, String detail) {
        this.error_message = detail;
    }

    private void convertErrorMessage(JSONObject object) {
        try {
            JSONArray errorArray = object.getJSONArray(ERRORS);
            if (errorArray != null && errorArray.length() >= 1) {
                JSONObject errorDetail = errorArray.getJSONObject(0);
                this.error_message = errorDetail.getString(ERROR_MESSAGE);
                this.error_code = errorDetail.getString(ERROR_CODE);
            } else {
                this.error_message = "something's wrong";
            }
        } catch (Exception e) {
            this.error_message = "System error! Can not convert json object";
        }
    }

    public int getStatusCode() {
        return status_code;
    }

    public String getError_code() {
        return error_code;
    }

    public void setStatusCode(int status_code) {
        this.status_code = status_code;
    }

    public String getErrorMessage() {
        return error_message;
    }
}
