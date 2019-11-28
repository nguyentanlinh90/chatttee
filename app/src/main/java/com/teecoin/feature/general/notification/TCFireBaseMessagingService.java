package com.teecoin.feature.general.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.feature.general.appflyer.TCAppFlyerTrackingEvent;
import com.teecoin.model.general.PushNotificationModel;
import com.teecoin.utils.TCUtils;

import java.util.Random;


public class TCFireBaseMessagingService extends FirebaseMessagingService {

    public static final String PUSH_NOTIFICATION_MODEL_INTENT = "PUSH_NOTIFICATION_MODEL_INTENT";
    private static final String TEE_COIN_GROUP = "TEE_COIN_GROUP_NOTIFICATION";
    private static int NOTIFICATION_ID = 1;

    //https://github.com/firebase/quickstart-android/tree/master/messaging
    //https://stackoverflow.com/questions/40181654/firebase-fcm-open-activity-and-pass-data-on-notification-click-android

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        PushNotificationModel notificationModel
                = new PushNotificationModel(remoteMessage.getNotification(), remoteMessage.getData());

        if (notificationModel.getNotification() != null) {
            senNotificationByCustomView(notificationModel, remoteMessage.getNotification().getClickAction());

//            if (EnumMgr.PushNotification.TransferMoney.getValue().equals(notificationModel.getData().getType())
//                    || EnumMgr.PushNotification.CheckInCoupon.getValue().equals(notificationModel.getData().getType())) {
//                getActiveActivity().runOnUiThread(TCUtils::loadAndSaveAgainTransactionList);
//            }
        }
    }

    @Override
    public void onNewToken(String token) {
        TCAppFlyerTrackingEvent.getInstance().updateNewTokenToAppFlyer(token);
    }

    private static int getNotificationId() {
        Random rnd = new Random();
        return 100 + rnd.nextInt(9000);
    }

    private void senNotificationByCustomView(PushNotificationModel notificationModel, String click_action) {
        Intent intent = new Intent(this, TCMainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);//this will call onNewIntent when app is visible on screen without calls onCreate()
        intent.addCategory(Intent.CATEGORY_LAUNCHER);//this will call onNewIntent when app is visible on screen without calls onCreate()
        intent.putExtra(PUSH_NOTIFICATION_MODEL_INTENT, notificationModel);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, getNotificationId(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, TCUtils.getString(R.string.Teecoin_notification_channel_id))
                        .setContent(createRemoteView(notificationModel))
//                        .setFullScreenIntent(pendingIntent, true)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_stat_notification)
                        .setAutoCancel(true)
                        //.setPriority(NotificationCompat.PRIORITY_HIGH)
                        //.setGroup(TEE_COIN_GROUP)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                        );

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(TCUtils.getString(R.string.Teecoin_notification_channel_id),
                    TCUtils.getString(R.string.Teecoin_notification_channel_id),
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(getNotificationId(), builder.build());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // group notification support starting from Android 7 (API 24)
//            notificationManager.notify(0, getSummaryNotification());
//        }
    }

    private RemoteViews createRemoteView(PushNotificationModel notificationModel) {
        RemoteViews contentView = new RemoteViews(this.getPackageName(), R.layout.view_custom_notification);
        contentView.setTextViewText(R.id.tv_title, !TCUtils.isEmpty(notificationModel.getNotification().getTitle()) ? notificationModel.getNotification().getTitle() : TCUtils.getString(R.string.flavored_app_name));
        contentView.setTextViewText(R.id.tv_body, !TCUtils.isEmpty(notificationModel.getNotification().getBody()) ? notificationModel.getNotification().getBody() : "New Notification");
        return contentView;
    }

    private Notification getSummaryNotification() {
        Notification summaryNotification =
                new NotificationCompat.Builder(this, TCUtils.getString(R.string.Teecoin_notification_channel_id))
                        .setContentTitle(TCUtils.getString(R.string.text_notifications))
                        .setContentText(TCUtils.getString(R.string.text_notifications))
                        .setSmallIcon(R.drawable.ic_playvideo_white)
                        .setStyle(new NotificationCompat.InboxStyle()
                                .setBigContentTitle(TCUtils.getString(R.string.text_notifications))
                                .setSummaryText(TCUtils.getString(R.string.text_notifications)))
                        .setGroup(TEE_COIN_GROUP)
                        .setGroupSummary(true)
                        .build();
        return summaryNotification;

    }
}
