package com.teecoin.model.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReadNotificationRequestModel  {
    @SerializedName("notification_id")
    @Expose
    private String notification_id;
    @SerializedName("mark_all")
    @Expose
    private boolean mark_all;

    public ReadNotificationRequestModel() {
    }

    public ReadNotificationRequestModel(String notification_id) {
        this.notification_id = notification_id;
    }
    public ReadNotificationRequestModel(boolean mark_all) {
        this.mark_all = mark_all;
    }

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public boolean isMark_all() {
        return mark_all;
    }

    public void setMark_all(boolean mark_all) {
        this.mark_all = mark_all;
    }

    @Override
    public String toString() {
        return "ReadNotificationRequestModel{" +
                "notification_id='" + notification_id + '\'' +
                ", mark_all=" + mark_all +
                '}';
    }
}
