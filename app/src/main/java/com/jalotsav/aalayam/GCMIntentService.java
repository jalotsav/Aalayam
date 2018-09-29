package com.jalotsav.aalayam;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.android.gcm.GCMBaseIntentService;
import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.common.UserSessionManager;
import com.jalotsav.aalayam.navgndrwer.doctor.NavgnDrwrDoctor;
import com.jalotsav.aalayam.pushnotifctn.ServerUtilities;

import static com.jalotsav.aalayam.pushnotifctn.CommonUtilities.SENDER_ID;
//import static com.bsptechno.vstock.pushnotifctn.CommonUtilities.displayMessage;

public class GCMIntentService extends GCMBaseIntentService implements AalayamConstants {

	private static final String TAG = "GCMIntentService";
	private static Context _context;
		
    public GCMIntentService() {
        super(SENDER_ID);
    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
    	
        Log.i(TAG, "Device registered: regId = " + registrationId);
//        displayMessage(context, "Your device registred with GCM");
        
        _context = context;
        ServerUtilities.register(context, registrationId);
    }

    /**
     * Method called on device un registred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
//        displayMessage(context, getString(R.string.gcm_unregistered));
        ServerUtilities.unregister(context, registrationId);
    }

    /**
     * Method called on Receiving a new message
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        String pushntfyTitle = intent.getExtras().getString("title");
        String pushntfyMessage = intent.getExtras().getString("message");
//        String newsId = intent.getExtras().getString("news_id");
        String newsId = "";

        // notifies user
//        generateNotification(context, pushntfyTitle, pushntfyMessage, newsId);
    }

    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
//        String message = getString(R.string.gcm_deleted, total);
//        displayMessage(context, message);
        // notifies user
//        generateNotification(context, message);
    }

    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
//        displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
//        displayMessage(context, getString(R.string.gcm_recoverable_error, errorId));
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message with single message.
     */
    @SuppressLint("NewApi")
    private static void generateNotification(Context context, String pushntfyTitle, String pushntfyMessage, String pushntfyEmHelpId) {

        Log.i(TAG, "Single notification");

        int icon = R.drawable.ic_notification;
        long when = System.currentTimeMillis();
        Intent notificationIntent;

        if(TextUtils.isEmpty(pushntfyTitle) || TextUtils.isEmpty(pushntfyMessage)){

            pushntfyTitle = context.getResources().getString(R.string.app_name);
            pushntfyMessage = context.getResources().getString(R.string.pushntfy_ptnt_deflt_message);
            notificationIntent = new Intent(context, Aalayam.class);
        }else{

            notificationIntent = new Intent(context, Aalayam.class);
        }

//        notificationIntent.putExtra(AppConstants.EM_HELP_ID_SML, pushntfyEmHelpId);
        notificationIntent.putExtra(OPEN_FRGMNT_BY_POSTN, NAVDRWER_FRGMNT_NEWS);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        /**
         *  CODE NOT WORK IN ANDROID STUDIO
         // set intent so it does not start a new activity
         notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

         //        PendingIntent intent = PendingIntent.getActivity(_contextPushMessage, 0, notificationIntent, 0);

         Notification notification = new Notification(icon, pushntfyMessage, when);
         notification.setLatestEventInfo(_contextPushMessage, pushntfyTitle, pushntfyMessage, intent);

         // Set Notification background color
         if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
         // only for LolliPop and newer versions
         notification.color = ContextCompat.getColor(_contextPushMessage, R.color.orangedeep_prmry);

         // Heads-up notification like Lollipop
         notification.priority = Notification.PRIORITY_MAX;
         }

         notification.flags |= Notification.FLAG_AUTO_CANCEL;

         // Play default notification sound
         notification.defaults |= Notification.DEFAULT_SOUND;

         //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");

         // Vibrate if vibrate is enabled
         notification.defaults |= Notification.DEFAULT_VIBRATE;
         */
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
/*
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        Notification notification = mBuilder.setSmallIcon(icon).setTicker(pushntfyTitle)
                .setAutoCancel(true)
                .setContentTitle(pushntfyTitle)
                .setContentText(pushntfyMessage)
                .setStyle(new BigTextStyle().bigText(pushntfyMessage))
                .setContentIntent(resultPendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(icon)
                .setColor(ContextCompat.getColor(context, R.color.notification_bg))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(Notification.PRIORITY_HIGH).setVibrate(new long[0])
                .setWhen(when)
                .build();
*/
//    	        .setLargeIcon(BitmapFactory.decodeResource(_contextPushMessage.getResources(), icon))

        /*NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_BUILDER_ID_SINGLE, notification);*/
    }

    @Override
    public void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	                	
    	if(Login_Pswrd.prgrsbr_websrvc != null){
    		Login_Pswrd.prgrsbr_websrvc.setVisibility(View.GONE);
    	}
    	if(_context != null){

			
			// set Login status as True in SharedPreffrences
			UserSessionManager session = new UserSessionManager(_context);
    		session.setLoginStatusTrue();
			
//			Intent intntNvgnDrwrDoctr = new Intent(_context, NavgnDrawer_Main_Doctor.class);
            Intent intntNvgnDrwrDoctr = new Intent(_context, NavgnDrwrDoctor.class);
            intntNvgnDrwrDoctr.putExtra(AalayamConstants.DCTR_EMAILID, Login_Pswrd.apiDctrEmailid);
            intntNvgnDrwrDoctr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intntNvgnDrwrDoctr);
    	}
    }
}
