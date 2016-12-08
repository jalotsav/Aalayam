package com.jalotsav.aalayam.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jalotsav.aalayam.R;

public class General_Fnctns implements AalayamConstants{
		
	/**
	 *  Show/Cancel Toast
	 **/
	static Toast toast = null;
	
	public static void showtoastLngthshort(Context context, String message) {
		// TODO Auto-generated method stub
		
		if(toast != null)toast.cancel();
			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
			toast.show();
	}

	public static void showtoastLngthlong(Context context, String message) {
		
		if(toast != null)toast.cancel();
			toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
			toast.show();
	}
	
	public static void cancelToast(){
		if(toast !=null)toast.cancel();
	}
	
	/**
	 *  Log Manager
	 **/
	public static void logManager(int logType, String logMessage){
	
		switch (logType) {
		case VERBOSE:

			Log.v(TAG, logMessage);
			break;
		case DEBUG:
			
			Log.d(TAG, logMessage);
			break;
		case INFO:
			
			Log.i(TAG, logMessage);
			break;
		case WARN:
			
			Log.w(TAG, logMessage);
			break;
		case ERROR:
			
			Log.e(TAG, logMessage);
			break;

		default:
			break;
		}
	}

	/***
	 * Check Internet Connection
	 * Mobile device is connect with Internet or not?
	 * ***/
	public static boolean isNetConnected(Context context){
		// TODO Auto-generated method stub
		/*
		// get Connectivity Manager object to check connection
        ConnectivityManager connec = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
             connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
             connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
             connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            return true;

        } else if (
          connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
          connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

        	return false;
        }
        */
      /*
       * Second Method for check Wifi and Mobile network
       */
		//wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		//mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		//if(wifiInfo.isConnected() || mobileInfo.isConnected())

		/** Support Internet Connection with SAMSUNG devices
		 * */
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		//This for Wifi.
		NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isConnected())
		{
			return true;
		}

		//This for Mobile Network.
		NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnected())
		{
			return true;
		}

		//This for Return true else false for Current status.
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected())
		{
			return true;
		}

		return false;
	}

	/***
	 * Hide Soft Keyboard
	 * ***/
	public static void hideSoftKeyboard(Activity activity) {
	    InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}

	/***
	 * String with comma separate to ArrayList
	 * ***/
	public static ArrayList<String> strCommaToArray(String str_with_comma){
		// TODO Auto-generated method stub
		
		ArrayList<String> arrylst = new ArrayList<String>();
		while(str_with_comma.indexOf(",") > 0)
		{
			String id = str_with_comma.substring(0, str_with_comma.indexOf(","));
			str_with_comma = str_with_comma.substring(str_with_comma.indexOf(",") + 1, str_with_comma.length());
			arrylst.add(id);													
		}
		
		// add last element in Array
		if(str_with_comma.trim().length()>0) arrylst.add(str_with_comma);
		return arrylst; 
	}
	
	/***
	 * Get Current Date FIRST TimeStamp
	 * ***/
	public static String getcurrentDateFirstTimestamp() {
		// TODO Auto-generated method stub
		
		String currnt_date_first_timestamp = "";
		
		try {
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			Date current_date_only = new Date();
			String currnt_date_firsttime = dateFormat.format(current_date_only) + " 00:00:01"; 
			
			Date currnt_date_firsttime_datefrmt = new SimpleDateFormat("dd-MMM-yyyy HH::ss").parse(currnt_date_firsttime);
			currnt_date_first_timestamp = String.valueOf(currnt_date_firsttime_datefrmt.getTime()/1000);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		
	    return currnt_date_first_timestamp;
	}
	
	/***
	 * Get Current Date LAST TimeStamp
	 * ***/
	public static String getcurrentDateLastTimestamp() {
		// TODO Auto-generated method stub
		
		String currnt_date_first_timestamp = "";
		
		try {
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			Date current_date_only = new Date();
			String currnt_date_firsttime = dateFormat.format(current_date_only) + " 23:59:59"; 
			
			Date currnt_date_firsttime_datefrmt = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(currnt_date_firsttime);
			currnt_date_first_timestamp = String.valueOf(currnt_date_firsttime_datefrmt.getTime()/1000);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		
	    return currnt_date_first_timestamp;
	}
	
	/***
	 * Get Current TimeStamp
	 * ***/
	public static String getcurrentTimestamp() {
		// TODO Auto-generated method stub
		
	    String currnt_timestamp = String.valueOf(System.currentTimeMillis() / 1000);
	    return currnt_timestamp;
	}
	
	/***
	 * Get Current Date
	 * ***/
	public static String getcurrentDate() {
		// TODO Auto-generated method stub
		
		Calendar clndr = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
	    String currnt_date = dateFormat.format(clndr.getTime());
	    return currnt_date;
	}
	
	/***
	 * Get TimeStamp after CreditPeriod days
	 * ***/
	public static String getAftrCreditperiodTimestamp(int credit_period) {
		// TODO Auto-generated method stub
		
	    long currnt_timestamp = System.currentTimeMillis() / 1000;
	    long crdt_period_milisec = 86400 * credit_period; // 60(ss)*60(mm)*24(hr) = 86400
	    String timestamp_aftr_crdtperiod = String.valueOf(currnt_timestamp + crdt_period_milisec);
	    return timestamp_aftr_crdtperiod;
	}
	
	/***
	 * Convert Timestamp to dd-MMM-yyyy Date format
	 * ***/
	public static String getDateFromTimestamp(long timestamp) {
		// TODO Auto-generated method stub
		
	    Calendar cal = Calendar.getInstance(Locale.ENGLISH);
	    cal.setTimeInMillis(timestamp * 1000);
	    String date = DateFormat.format("dd-MMM-yyyy", cal).toString();
	    return date;
	}
	
	/***
	 * Convert dd-MMM-yyyy Date to TimeStamp format
	 * ***/
	@SuppressWarnings("deprecation")
	public static long getTimestampFromDate(String date) {
		// TODO Auto-generated method stub
		
		Date dt_date = new Date(date);
	    long timestamp = dt_date.getTime()/1000;
	    
	    return timestamp;
	}
	
	/***
	 * Get Previous Date from Given Date
	 * ***/
	public static String getPrvsdateGromGivendate(String date) {
		// TODO Auto-generated method stub
		
	    long timestamp_givendate = getTimestampFromDate(date);
	    long timestamp_prvsdate = timestamp_givendate - 86400;
		String prvs_date = getDateFromTimestamp(timestamp_prvsdate);
	    return prvs_date;
	}
	
	/***
	 * Get Next Date from Given Date
	 * ***/
	public static String get_nextdate_from_givendate(String date) {
		// TODO Auto-generated method stub
		
	    long timestamp_givendate = getTimestampFromDate(date);
	    long timestamp_nextdate = timestamp_givendate + 86400;
		String next_date = getDateFromTimestamp(timestamp_nextdate);
	    return next_date;
	}
	
	/***
	 * Change Date Format
	 * ***/
	public static String changeDateFormate(String actual_frmt, String return_frmt, String current_date) {
		// TODO Auto-generated method stub
		
		SimpleDateFormat date_frmt_actual = new SimpleDateFormat(actual_frmt);
		SimpleDateFormat date_frmt_retrn = new SimpleDateFormat(return_frmt);
		Date temp_date = null;
		
		try {
			temp_date = date_frmt_actual.parse(current_date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		String task_start_date = date_frmt_retrn.format(temp_date);
		
	    return task_start_date;
	}
	
	/***
	 * Show/Hide User Notification
	 * ***/
	public static void showUsrNtfy(Context context, LinearLayout lnrlyot_usr_ntfy, TextView tv_usr_ntfy_msg, int lnrlyot_bckgrnd_color, int ntfy_msg_color, String ntfy_msg){
		// TODO Auto-generated method stub
		
		// Hide Older UserNotification
		lnrlyot_usr_ntfy.setVisibility(View.GONE);
		
		Animation anim_down_in = AnimationUtils.loadAnimation(context, R.anim.trans_up_in);
		lnrlyot_usr_ntfy.startAnimation(anim_down_in);
		lnrlyot_usr_ntfy.setVisibility(View.VISIBLE);
		lnrlyot_usr_ntfy.setBackgroundColor(lnrlyot_bckgrnd_color);
		tv_usr_ntfy_msg.setTextColor(ntfy_msg_color);
		tv_usr_ntfy_msg.setText(ntfy_msg);			
	}
	
	/***
	 * Show/Hide User Notification with AutoHide
	 * ***/
	public static void showUsrNtfyAutoHide(final Context context, final LinearLayout lnrlyot_usr_ntfy, TextView tv_usr_ntfy_msg, int lnrlyot_bckgrnd_color, int ntfy_msg_color, String ntfy_msg, int autoHideMiliSecond){
		// TODO Auto-generated method stub
		
		// Hide Older UserNotification
		lnrlyot_usr_ntfy.setVisibility(View.GONE);
		
		Animation anim_down_in = AnimationUtils.loadAnimation(context, R.anim.trans_up_in);
		lnrlyot_usr_ntfy.startAnimation(anim_down_in);
		lnrlyot_usr_ntfy.setVisibility(View.VISIBLE);
		lnrlyot_usr_ntfy.setBackgroundColor(lnrlyot_bckgrnd_color);
		tv_usr_ntfy_msg.setTextColor(ntfy_msg_color);
		tv_usr_ntfy_msg.setText(ntfy_msg);
		
		Handler mhndlr = new Handler();
		mhndlr.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				hideUsrNtfy(context, lnrlyot_usr_ntfy);
			}
		}, autoHideMiliSecond);
	}
	
	public static void hideUsrNtfy(Context context, LinearLayout lnrlyot_usr_ntfy){
	
		if(lnrlyot_usr_ntfy.getVisibility() == View.VISIBLE){
			
			Animation anim_up_out = AnimationUtils.loadAnimation(context, R.anim.trans_up_out);
			lnrlyot_usr_ntfy.startAnimation(anim_up_out);
			lnrlyot_usr_ntfy.setVisibility(View.GONE);			
		}
	}
	
	/***
	 * Show/Hide Images Download Notification and change Text of Pending Download ImageCount
	 * ***/
	public static void showImgsDwnldNtfy(Context context, LinearLayout lnrlyot_dwnldimgntfy) {
		// TODO Auto-generated method stub
		
		if(lnrlyot_dwnldimgntfy.getVisibility() == View.GONE){

			Animation anim_down_in = AnimationUtils.loadAnimation(context, R.anim.trans_down_in);
			lnrlyot_dwnldimgntfy.startAnimation(anim_down_in);
			lnrlyot_dwnldimgntfy.setVisibility(View.VISIBLE);	
		}
	}
    
	public static void hideImgsDwnldNtfy(Context context, LinearLayout lnrlyot_dwnldimgntfy) {
		// TODO Auto-generated method stub
		
		if(lnrlyot_dwnldimgntfy.getVisibility() == View.VISIBLE){
			
			Animation anim_down_out = AnimationUtils.loadAnimation(context, R.anim.trans_down_out);
			lnrlyot_dwnldimgntfy.startAnimation(anim_down_out);
			lnrlyot_dwnldimgntfy.setVisibility(View.GONE);	
		}
	}
	
	public static void updateTextofImgsDwnldNtfy(Context context, int comeFrom, LinearLayout lnrlyot_dwnldimgntfy, TextView tvImgcnt, TextView tvImgcntlbl, int imgcnt) {
		// TODO Auto-generated method stub
		
		switch (comeFrom) {
		case COME_FROM_TABPTNTIMGS:
			
			if(imgcnt == 1){
				
				tvImgcnt.setText(String.valueOf(imgcnt));
				tvImgcntlbl.setText(context.getResources().getString(R.string.ptnt_image_inqueue));
			}else if(imgcnt > 1){
				
				tvImgcnt.setText(String.valueOf(imgcnt));
				tvImgcntlbl.setText(context.getResources().getString(R.string.ptnt_images_inqueue));
			}else{
				
				tvImgcntlbl.setText(context.getResources().getString(R.string.ptnt_images_inqueue));
			}
			break;
			
		case COME_FROM_TABPTNTCASEIMGS:
			
			if(imgcnt == 1){
				
				tvImgcnt.setText(String.valueOf(imgcnt));
				tvImgcntlbl.setText(context.getResources().getString(R.string.ptnt_case_image_inqueue));
			}else if(imgcnt > 1){
				
				tvImgcnt.setText(String.valueOf(imgcnt));
				tvImgcntlbl.setText(context.getResources().getString(R.string.ptnt_case_images_inqueue));
			}else{
				
				tvImgcntlbl.setText(context.getResources().getString(R.string.ptnt_case_images_inqueue));
			}
			break;

		default:
			break;
		}
	}
	
	/***
	 * Check Email Address Format is valid or not
	 * ***/
	public static boolean isvalidEmailaddrsFrmt(CharSequence target) {
	    if(target == null){
	        return false;
	    }else{
	        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	    }
	}
	
	/***
	 * Convert double value to String with 2digit after decimal point
	 * ***/
	public static String get2DecimalDigitOfDouble(double value){
		
		String cnvrtd_value = "";
		
		//cnvrtd_value = new DecimalFormat("#0.00").format(value);
		//cnvrtd_value = String.format("%.2f", value);
		double double_cnvrtd_value  = Math.round(value * 100) / 100.00;
		cnvrtd_value = new DecimalFormat("#0.00").format(double_cnvrtd_value);
		
		return cnvrtd_value;
	}

	/*
    * Get Two digits after decimal points with thousand comma separator
    * */
	public static String get2DigitDecimal(String reqstdStr, boolean isNeedwithMathRound) {

		String convrtdStr = "0.00";
		try {

			if (TextUtils.isEmpty(reqstdStr))
				return convrtdStr;
			else {
				double reqstdVal = Double.valueOf(reqstdStr);
				if (isNeedwithMathRound)
					reqstdVal = Math.round(reqstdVal * 100) / 100.00;
				convrtdStr = new DecimalFormat("###,##0.00").format(Math.abs(reqstdVal));
				if (reqstdVal < 0)
					convrtdStr = "-" + convrtdStr;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return convrtdStr;
	}
	
	/***
	 * Start Start(Rotate) and complete Sync icon 
	 * ***
	// Start to Rotate Sync ActionBar icon
	public void startSyncIconAnim(Activity getactivity, MenuItem menuitem_sync) {
	     
		menuitem_sync.setActionView(null);
		
		// Attach a rotating Sync ImageView to ActionBar
	     LayoutInflater lyot_inflater = (LayoutInflater) getactivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	     ImageView imgvw_sync = (ImageView) lyot_inflater.inflate(R.layout.lo_actionbar_sync, null);

	     Animation rotation = AnimationUtils.loadAnimation(getactivity, R.anim.rotate_clockwise);
	     rotation.setRepeatCount(Animation.INFINITE);
	     imgvw_sync.startAnimation(rotation);

	     menuitem_sync.setActionView(imgvw_sync);
	 }
	
	// End of Rotate Sync ActionBar icon
	public static void completeSyncIconAnim(MenuItem menuitem_sync) {
		
		 if (menuitem_sync != null && menuitem_sync.getActionView() != null) {
			 menuitem_sync.getActionView().clearAnimation();
			 menuitem_sync.setActionView(null);
		 }
	}
*/	
	/***
	 * Check GPS is Enable or Disable with Return GPS Status
	 * ***/
	public static boolean checkGpsEnbldsblWithRetrnStatus(Context context){
		
		boolean gps_status;
	    LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
	    if (manager.isProviderEnabled( LocationManager.GPS_PROVIDER)){
	    	
	    	gps_status = true;
	    }else{
	    	
	    	gps_status = false;
	    }
	    
	    return gps_status;
	}
	
	/***
	 * Check GPS is Enable or Disable
	 * ***
	public void checkGpsEnblDsbl(Context context){
		
	    LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
	    if (manager.isProviderEnabled( LocationManager.GPS_PROVIDER)){
	    	
//	    	Log.w(TAG, "GPS is Enable");
	    }else{
	    	
//	    	Log.e(TAG, "GPS is Disable");
        	Intent intnt_gpsenble = new Intent(context, Gps_Enable_Ntfy.class);
        	intnt_gpsenble.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	context.startActivity(intnt_gpsenble);
	    }
	}
	
	/***
	 * Check GPS is Enable or GPS mode in "High Accuracy" 
	 * ***
	public void checkGpsStatus(Context context){

		// Get location
		String cmpny_lati_longi = get_latitude_longitude(context);
		if(!cmpny_lati_longi.equals("")){

			if(cmpny_lati_longi.equals("0.0,0.0")){
				
				if(isNetConnected(context)){
					
		        	Intent intnt_gpsenble = new Intent(context, Gps_Enable_ChangeMode.class);
		        	intnt_gpsenble.putExtra("gps_status", "enable");
		        	intnt_gpsenble.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        	context.startActivity(intnt_gpsenble);
		        	
					cmpny_lati_longi = get_latitude_longitude(context);	
				}
			}else{
				
				Log.e("BspTechno_SMS", cmpny_lati_longi);
			}
		}
	}

	// Get Latitude and Longitude
	public String getLatitudeLongitude(Context context) {
		// TODO Auto-generated method stub
		
		String cmpny_lati_longi = "";
		GpsTracker obj_gpstcker = new GpsTracker(context);
		
		// check if GPS enabled		
        if(obj_gpstcker.can_get_location()){
        	
        	double latitude = obj_gpstcker.getLatitude();
        	double longitude = obj_gpstcker.getLongitude();
	        cmpny_lati_longi = String.valueOf(latitude) + "," + String.valueOf(longitude);
        }else{
        	
        	// GPS or Network is not enabled
        	// Ask to user for Open setting Menu
        	Intent intnt_gpsenble = new Intent(context, Gps_Enable_ChangeMode.class);
        	intnt_gpsenble.putExtra("gps_status", "disable");
        	intnt_gpsenble.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	context.startActivity(intnt_gpsenble);
        }
        
        return cmpny_lati_longi;        
	}
*/	
	/***
	 * Check Service is Running or not 
	 * ***/
	public static boolean isServiceRunning(Class<?> serviceClass, Context context) {
	    ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceClass.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}

	/***
	 * convert from bitmap to byte array 
	 * ***/
	public static String encodeTobase64(Bitmap image) {
		Bitmap immagex=image;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		immagex.compress(Bitmap.CompressFormat.JPEG, 70, baos);
		byte[] b = baos.toByteArray();
		String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);

		return imageEncoded;
	}

	/***
	 * convert from bitmap to byte array 
	 * ***/
	public static ArrayList<Integer> getPrimaryColorArray(Context context) {
		
		ArrayList<Integer> arrylst_prmrycolor = new ArrayList<Integer>();
		try {
			arrylst_prmrycolor.add(ContextCompat.getColor(context, R.color.trqs_prmry));
			arrylst_prmrycolor.add(ContextCompat.getColor(context, R.color.blue_prmry));
//		arrylst_prmrycolor.add(ContextCompat.getColor(context, R.color.bluemdnght_prmry));
			arrylst_prmrycolor.add(ContextCompat.getColor(context, R.color.red_prmry));
			arrylst_prmrycolor.add(ContextCompat.getColor(context, R.color.pink_prmry));
			arrylst_prmrycolor.add(ContextCompat.getColor(context, R.color.purple_prmry));
//		arrylst_prmrycolor.add(ContextCompat.getColor(context, R.color.purpledeep_prmry));
			arrylst_prmrycolor.add(ContextCompat.getColor(context, R.color.indigo_prmry));
			arrylst_prmrycolor.add(ContextCompat.getColor(context, R.color.orangedeep_prmry));
			arrylst_prmrycolor.add(ContextCompat.getColor(context, R.color.bluegray_prmry));
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return arrylst_prmrycolor;
	}
 
    /**
     *	getting screen width
     **/
    @SuppressWarnings("deprecation")
	public static int getScreenWidth(Context context) {
    	
        int screenWidth;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
 
        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        screenWidth = point.x;
        return screenWidth;
    }
 
    // Reading file paths from SDCard
    public ArrayList<String> getFilePaths(File imgDirectory, ArrayList<String> arrylstImgname) {
        ArrayList<String> filePaths = new ArrayList<String>();
 
        File directory = imgDirectory;
 
        // check for directory
        if (directory.isDirectory()) {
            // getting list of file paths
            File[] listFiles = directory.listFiles();
 
            // Check for count
            if (listFiles.length > 0) {
 
                // loop through all files
                for (int i = 0; i < listFiles.length; i++) {
 
                    // get file path
                    String filePath = listFiles[i].getAbsolutePath();
 
                    // check for supported file extension
                    if (IsSupportedFile(filePath)) {
                        // Add image path to array list
                    	String fileName = filePath.substring(filePath.lastIndexOf("/") + 1).trim();
                    	if(arrylstImgname.contains(fileName)){

                            filePaths.add(filePath);	
                    	}
                    }
                }
            } else {
                // image directory is empty
            }
        } 
        return filePaths;
    }

	// Check supported file extensions
    private boolean IsSupportedFile(String filePath) {
        String ext = filePath.substring((filePath.lastIndexOf(".") + 1),
                filePath.length());
 
        if (AalayamConstants.FILE_EXTN
                .contains(ext.toLowerCase(Locale.getDefault())))
            return true;
        else
            return false;
    }
 
    /**
     *	Save given Bitmap to Device
     **/
    public static void saveBitmapToDevice(Bitmap btmp, String filePath, String imageName) {
		
    	File file = new File (filePath, imageName);
    	if (file.exists ()) file.delete (); 
    	try {
    	       FileOutputStream out = new FileOutputStream(file);
    	       btmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
    	       out.flush();
    	       out.close();
    	} catch (Exception e) {
    	       e.printStackTrace();
    	}
	}
 
    /**
     *	Rename FileName in Device
     **/
	public static void renameFilenameInDevice(String strFilePath, String fromName, String toName) {
		
		try {
			File filePath = new File(strFilePath);
			File fileFrom = new File(filePath, fromName);
			File fileto = new File(filePath, toName);
			fileFrom.renameTo(fileto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}