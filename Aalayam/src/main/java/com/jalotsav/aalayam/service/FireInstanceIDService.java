package com.jalotsav.aalayam.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.jalotsav.aalayam.common.AalayamConstants;

/**
 * Created by JALOTSAV Dev. on 29/8/16.
 */
public class FireInstanceIDService  extends FirebaseInstanceIdService implements AalayamConstants {

    @Override
    public void onTokenRefresh() {

        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "onTokenRefresh: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
//        sendRegistrationToServer(refreshedToken);
    }

    /*private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }*/
}
