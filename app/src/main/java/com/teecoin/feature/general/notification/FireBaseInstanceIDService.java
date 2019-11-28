package com.teecoin.feature.general.notification;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FireBaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "TC FirebaseIIDService";

    @Override
    public void onTokenRefresh() {

//Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

//Displaying token on logcat
        //  TCLog.e("Refreshed token: " + refreshedToken);
    }
}
// ON FIREBASE
// Server key: AAAA7W2twnw:APA91bEvD604uVgN6jBrzS4f7-KjdgPe4X9UQDWio9Ouaix8iv8-ocux3xiZ6xOsIcdSYXF8qMt197EnaYkDqt6CXSQqyMi7yc_BJClPvRoIhBgf8VoRe0N3LISRE_vRldmN1ZnqlrAtrwDTLcFKSg0ciRa-bdIyhA
//
// Legacy server key : AIzaSyDDmpVHjWXXdw1PTPUnAFXeRmdmVrKBKyQ
// sender id: 1019747353212

// token: c8FZCuM-kJw:APA91bG6DalESQsTVLYGv472Etkb6NiJ7oiFvYN8LZS1RMvUqNjrNTZbPCWIVLWISZV3VLQ4QNnjlYenGLmHvwud1cZBdCiqOSqjpHfwuOwX_8BQiPGEOf1FoGeNXvAt95Xx-zRYPSZmFl6crbrmo5iAw9oKPNYNkQ