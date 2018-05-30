package com.example.felipe.mibuy4business.Notificacao;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseToken extends FirebaseInstanceIdService {
    private static final String TAG = "tokenDoFire";
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        storeToken(refreshedToken);

    }

    private void storeToken(String token) {
        //saving the token on shared preferences
        SharedPrefManager.getInstance(getApplicationContext()).saveDeviceToken(token);
    }
}
