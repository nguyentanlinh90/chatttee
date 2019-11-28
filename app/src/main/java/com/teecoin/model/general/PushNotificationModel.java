package com.teecoin.model.general;

import android.os.Bundle;

import com.google.firebase.messaging.RemoteMessage;

import java.io.Serializable;
import java.util.Map;

public class PushNotificationModel implements Serializable {

    private NotificationReceiveModel notification;
    private DataPushNotificationModel data;

    public PushNotificationModel() {

    }

    public PushNotificationModel(RemoteMessage.Notification notification, Map<String, String> data) {
        this.notification = notification != null ?
                new NotificationReceiveModel(notification.getTitle(), notification.getBody()) : null;
        this.data = data != null ? new DataPushNotificationModel(data) : null;
    }

    public PushNotificationModel(Bundle extras) {
        this.data = extras != null && extras.get(DataPushNotificationModel.TAG_TYPE) != null ?
                new DataPushNotificationModel(extras) : null;
    }


    public NotificationReceiveModel getNotification() {
        return notification;
    }

    public void setNotification(NotificationReceiveModel notification) {
        this.notification = notification;
    }

    public DataPushNotificationModel getData() {
        return data;
    }

    public void setData(DataPushNotificationModel data) {
        this.data = data;
    }
}
