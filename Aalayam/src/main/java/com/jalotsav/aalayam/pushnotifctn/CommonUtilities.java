package com.jalotsav.aalayam.pushnotifctn;

import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.webservice.ServiceHandler;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {
	
	// give your server registration url here
	static ServiceHandler srvc_hndlr = new ServiceHandler();
    static final String SERVER_URL = AalayamConstants.WEBSERVICE_URL;

    // Google project id
    public static final String SENDER_ID = "440328317397";

    /**
     * Tag used on log messages.
     */
    public static final String TAG = AalayamConstants.TAG;

    public static final String DISPLAY_MESSAGE_ACTION =
            "com.androidhive.pushnotifications.DISPLAY_MESSAGE";

    public static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
