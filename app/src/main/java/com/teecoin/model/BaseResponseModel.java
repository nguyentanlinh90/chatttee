package com.teecoin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseResponseModel<T> {
    @SerializedName("result")
    @Expose
    private T result;

    @SerializedName("success")
    @Expose
    private Boolean success;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }


}
