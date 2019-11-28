package com.teecoin.feature.general.notification;

import android.content.Context;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;

public class FCMHandler {

    public FCMHandler(Context context) {
    }

    public String enableFCM() {
        new Thread(() -> {
            FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        }).start();
        return FirebaseInstanceId.getInstance().getToken();

    }

    public void disableFCM() {
        FirebaseMessaging.getInstance().setAutoInitEnabled(false);
        new Thread(() -> {
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    //https://stackoverflow.com/questions/43193215/firebase-cloud-messaging-handling-logout
}
