package com.jalotsav.aalayam.navgndrwer.patient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bsptechno.libfabbsptechno.FloatingActionButton;
import com.jalotsav.aalayam.R;
import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.common.General_Fnctns;
import com.jalotsav.aalayam.common.UserSessionManager;
import com.jalotsav.aalayam.webservice.ServiceHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class PatientProfileFrgmnt extends Fragment implements AalayamConstants {
	
	UserSessionManager session;
	
	LinearLayout lnrlyot_usr_ntfy;
	TextView tv_usr_ntfy_msg;
	
	ProgressBar prgrsbrMain, prgrsbrPatntimg;
	ImageView imgvwPatntimg;
	TextView tv_name, tv_address, tv_area, tv_cntcntno1, tv_cntcntno2, tv_plan, tv_noofsitng, tv_fees, tv_alctddctr, tv_reasmntstatus;
	String mPatntName, mAddress, mArea, mCaseDscrptn, mDiagnosis, mCntctno1, mCntctno2, mProfileImageName, mPlan, mNoofsitng, mFees, mAlctdDctr, mReasmntstatus;
	FloatingActionButton fab_casedtls;
	
	private String slctd_pt_id;
	
	boolean currnt_run_onscreen_status = false, isProfileImageSet=false;
	ArrayList<String> arrylstProfileimgFilepath = new ArrayList<String>();

	String strCurrentCallCntctNo = "";
	public static final int REQUEST_USES_PERMISSION = 101;

	private ImageLoader imageLoader;
	private DisplayImageOptions optionsImgLoader;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View rootView = inflater.inflate(R.layout.lo_patient_frgmnt_profile, container, false);

		// Set Actionbar Title
		((NavgnDrawer_Main_Patient) getActivity()).onSectionAttached(NAVDRWER_FRGMNT_PROFILE);
		
		session = new UserSessionManager(getActivity());
		
		// Set Current Activity/Fragment is currently running on screen
		currnt_run_onscreen_status = true;

		lnrlyot_usr_ntfy = (LinearLayout)rootView.findViewById(R.id.lnrlyot_user_notify);
		tv_usr_ntfy_msg = (TextView)rootView.findViewById(R.id.tv_user_notify_msg);
		prgrsbrMain = (ProgressBar)rootView.findViewById(R.id.prgrbr_patnt_frgmnt_profile_main);
		prgrsbrPatntimg = (ProgressBar)rootView.findViewById(R.id.prgrbr_patnt_frgmnt_profile_patntimg);
		imgvwPatntimg = (ImageView)rootView.findViewById(R.id.imgvw_patnt_frgmnt_profile_patntimg);
		tv_name = (TextView)rootView.findViewById(R.id.tv_patnt_frgmnt_profile_patntnm);
		tv_address = (TextView)rootView.findViewById(R.id.tv_patnt_frgmnt_profile_address);
		tv_area = (TextView)rootView.findViewById(R.id.tv_patnt_frgmnt_profile_area);
		tv_cntcntno1 = (TextView)rootView.findViewById(R.id.tv_patnt_frgmnt_profile_contctno1);
		tv_cntcntno2 = (TextView)rootView.findViewById(R.id.tv_patnt_frgmnt_profile_contctno2);
		tv_plan = (TextView)rootView.findViewById(R.id.tv_patnt_frgmnt_profile_plan);
		tv_noofsitng = (TextView)rootView.findViewById(R.id.tv_patnt_frgmnt_profile_noofsitting);
		tv_fees = (TextView)rootView.findViewById(R.id.tv_patnt_frgmnt_profile_fees);
		tv_alctddctr = (TextView)rootView.findViewById(R.id.tv_patnt_frgmnt_profile_alctddctr);
		tv_reasmntstatus = (TextView)rootView.findViewById(R.id.tv_patnt_frgmnt_profile_reasmntstatus);
		fab_casedtls = (FloatingActionButton)rootView.findViewById(R.id.fab_patnt_frgmnt_profile_casedtls);
				
		slctd_pt_id = getActivity().getIntent().getStringExtra(PT_ID);

		imageLoader = ImageLoader.getInstance();
		if (!imageLoader.isInited())
			imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		optionsImgLoader = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).resetViewBeforeLoading(true)
				/*.showImageForEmptyUri(ContextCompat.getDrawable(getActivity(), R.drawable.img_avatar_patient))
				.showImageOnFail(ContextCompat.getDrawable(getActivity(), R.drawable.img_avatar_patient))
				.showImageOnLoading(ContextCompat.getDrawable(getActivity(), R.drawable.img_avatar_patient))*/
				.build();
		
		tv_address.setMovementMethod(new ScrollingMovementMethod());
		tv_area.setMovementMethod(new ScrollingMovementMethod());

		if(!General_Fnctns.isNetConnected(getActivity())){
			
			// Show UserNotification
    		General_Fnctns.showUsrNtfyAutoHide(getActivity(), lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(getActivity(), R.color.red_alizarin), ContextCompat.getColor(getActivity(), R.color.white), getResources().getString(R.string.no_intrnt_cnctn), HIDE_USERNOTIFY_TIME_5K);
		}else{

			new GetPatientProfileFromWebSrve().execute();
			
			// Get Patient Image from External storage or URL
			checkGetPatientImage();
		}

		tv_cntcntno1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(!TextUtils.isEmpty(tv_cntcntno1.getText().toString().trim())){

					// Call from mobile native dialer to given number
					strCurrentCallCntctNo = tv_cntcntno1.getText().toString().trim();
					callTOGivenNumber();
				}
			}
		});

		tv_cntcntno2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(!TextUtils.isEmpty(tv_cntcntno2.getText().toString().trim())){

					// Call from mobile native dialer to given number
					strCurrentCallCntctNo = tv_cntcntno2.getText().toString().trim();
					callTOGivenNumber();
				}
			}
		});

		imgvwPatntimg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(isProfileImageSet){

					if(!arrylstProfileimgFilepath.isEmpty()) {
						// launch full screen activity
						Intent i = new Intent(getActivity(), FullScreenImageViewActivity.class);
						i.putExtra(SLCTD_POSITION, 0);
						i.putStringArrayListExtra(ARRYLST_FILEPATHS_FULLSCRNIMAGE, arrylstProfileimgFilepath);
						getActivity().startActivity(i);
					}
				}
			}
		});
		
		fab_casedtls.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intnt_casedtls = new Intent(getActivity(), PatientProfileFrgmnt_CaseDetails.class);
				intnt_casedtls.putExtra(PT_ID, slctd_pt_id);
				intnt_casedtls.putExtra(CASE_DESCRIPPTION_SML, mCaseDscrptn);
				intnt_casedtls.putExtra(DIAGNOSIS_SML, mDiagnosis);
				startActivity(intnt_casedtls);
			}
		});
		
		return rootView;		
	}
	
	public class GetPatientProfileFromWebSrve extends AsyncTask<Void, Void, Void>{

		String websrvc_response;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			prgrsbrMain.setVisibility(View.VISIBLE);			
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			ServiceHandler obj_srvchndlr = new ServiceHandler();
			
			JSONObject jsnobj = new JSONObject();
			try {
				jsnobj.put(FUNCTION_SMALL, FNCTN_SELECT_PATIENT_BY_PT_ID);
				jsnobj.put(PT_ID, slctd_pt_id);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String json = WEBSRVC_JSONKEY + jsnobj.toString();
			websrvc_response = obj_srvchndlr.post_makeServiceCall(json);
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(websrvc_response.equals(ACCESS_DENIED)){

				// Show UserNotification
	    		General_Fnctns.showUsrNtfyAutoHide(getActivity(), lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(getActivity(), R.color.red_alizarin), ContextCompat.getColor(getActivity(), R.color.white), getResources().getString(R.string.there_seemsprblm_tryagainlater), HIDE_USERNOTIFY_TIME_5K
	    				);
			}else{
				try {
					JSONObject websrvc_jsnobj = new JSONObject(websrvc_response);
					String success = websrvc_jsnobj.getString(SUCCESS_SMALL);
					String websrvc_msg = websrvc_jsnobj.getString(MESSAGE_SMALL);
					JSONArray jsonArrayFirst = websrvc_jsnobj.getJSONArray(PATIENT_SMALL);
					JSONArray jsonArrayAreaFirst = websrvc_jsnobj.getJSONArray(AREA_SMALL);
					if(success.equals(TRUE_SMALL)){
					
						if(!websrvc_msg.equals("null") && websrvc_msg.equals(PATIENT_FOUND)){

							/**
							 * JSONARRAY OF PATIENT DETAILS
							 */
							if(jsonArrayFirst.length() !=0){
								
								for(int i=0; i< jsonArrayFirst.length(); i++){
									
									JSONArray jsonArraySecnd = jsonArrayFirst.getJSONArray(i);
									
									for(int j=0; j<jsonArraySecnd.length(); j++){

										JSONObject jsnobj_patntprfl = jsonArraySecnd.getJSONObject(j);
										
										mPatntName = jsnobj_patntprfl.getString(NAME_FN_SML) + " " + jsnobj_patntprfl.getString(NAME_LN_SML); 
										mAddress = jsnobj_patntprfl.getString(ADDRESS_SML);
//										mArea = jsnobj_patntprfl.getString(AREA_ID_SML);
										mCaseDscrptn = jsnobj_patntprfl.getString(CASE_DESCRIPPTION_SML);
										mDiagnosis = jsnobj_patntprfl.getString(DIAGNOSIS_SML);
										mCntctno1 = jsnobj_patntprfl.getString(CONTACT_NO_1_SML);
										mCntctno2 = jsnobj_patntprfl.getString(CONTACT_NO_2_SML);
										mProfileImageName = jsnobj_patntprfl.getString(PROFILE_IMAGE_SML);
										mPlan = jsnobj_patntprfl.getString(PLAN_SML);
										mNoofsitng = jsnobj_patntprfl.getString(NO_OF_SETTINGS_SML);
										mFees = jsnobj_patntprfl.getString(FEES_SML);
										mAlctdDctr = jsnobj_patntprfl.getString(DR_NAME_FN_SML) + " " + jsnobj_patntprfl.getString(DR_NAME_LN_SML);
										mReasmntstatus = jsnobj_patntprfl.getString(REASSESMENT_SML);
									}
								}
							}else{
								
								// Show UserNotification
					    		General_Fnctns.showUsrNtfyAutoHide(getActivity(), lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(getActivity(), R.color.red_alizarin), ContextCompat.getColor(getActivity(), R.color.white), getResources().getString(R.string.there_seemsprblm_tryagainlater), HIDE_USERNOTIFY_TIME_5K);	
							}
							
							/**
							 * JSONARRAY OF AREA
							 */								
							if(jsonArrayAreaFirst.length() !=0){
								
								for(int i=0; i< jsonArrayAreaFirst.length(); i++){
									
									JSONArray jsonArrayAreaSecnd = jsonArrayAreaFirst.getJSONArray(i);
									
									for(int j=0; j<jsonArrayAreaSecnd.length(); j++){

										JSONObject jsnobj_area = jsonArrayAreaSecnd.getJSONObject(j);
										
										mArea = jsnobj_area.getString(A_TITLE);
									}
								}
							}
							
							// Set all values to TextView
							SetPrflDetailsTextview();	
						}else{
							
							// Show UserNotification
				    		General_Fnctns.showUsrNtfyAutoHide(getActivity(), lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(getActivity(), R.color.red_alizarin), ContextCompat.getColor(getActivity(), R.color.white), getResources().getString(R.string.there_seemsprblm_tryagainlater), HIDE_USERNOTIFY_TIME_5K);
						}
					}else if(success.equals(FALSE_SMALL)){
						
						if(!websrvc_msg.equals("null") && websrvc_msg.equals(FUNCTION_NOT_FOUND)){
							
							// Show UserNotification
				    		General_Fnctns.showUsrNtfyAutoHide(getActivity(), lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(getActivity(), R.color.red_alizarin), ContextCompat.getColor(getActivity(), R.color.white), getResources().getString(R.string.there_seemsprblm_tryagainlater), HIDE_USERNOTIFY_TIME_5K);
						}else{
							
							// Show UserNotification
				    		General_Fnctns.showUsrNtfyAutoHide(getActivity(), lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(getActivity(), R.color.red_alizarin), ContextCompat.getColor(getActivity(), R.color.white), getResources().getString(R.string.there_seemsprblm_tryagainlater), HIDE_USERNOTIFY_TIME_5K);	
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(prgrsbrMain.getVisibility() == View.VISIBLE) prgrsbrMain.setVisibility(View.GONE);
		}
	}

	// Set all values to TextView
	private void SetPrflDetailsTextview() {
		// TODO Auto-generated method stub

		try{
			if(currnt_run_onscreen_status){
				
				tv_name.setText(mPatntName);
				tv_address.setText(mAddress);
				tv_area.setText(mArea);
				tv_cntcntno1.setText(mCntctno1);
				tv_cntcntno2.setText(mCntctno2);
				if(mPlan.equals(LTO_SML)){

					tv_plan.setText("LTO");
				}else if(mPlan.equals(EXE_SML)){

					tv_plan.setText("EXE");
				}else if(mPlan.equals(LTO_EXE_SML)){

					tv_plan.setText("LTO + EXE");
				}
				tv_noofsitng.setText(mNoofsitng);
				tv_fees.setText(mFees);
				tv_alctddctr.setText(mAlctdDctr);
				if(mReasmntstatus.equals(TRUE_FRSTCAPS)){
					
					tv_reasmntstatus.setText("Yes");
				}else if(mReasmntstatus.equals(FALSE_FRSTCAPS)){

					tv_reasmntstatus.setText("No");
				}
				
				// Show CaseDetails FAB
				fab_casedtls.setVisibility(View.VISIBLE);
				
				// Check image to External storage if not available Get from AdminPanel and store to device
				checkGetPatientImage();
			}
		}catch(Exception e){
			General_Fnctns.logManager(ERROR, "Catch  block(GetSetWeb-service Data):" + e.getMessage());
		}
	}

	// Check image to External storage if not available Get from AdminPanel and store to device
	private void checkGetPatientImage() {
		// TODO Auto-generated method stub
		if(mProfileImageName == null || mProfileImageName.equals("")){

			// Set Default image to ImageView
			isProfileImageSet = false;
//        	Bitmap bitmp_user_img = BitmapFactory.decodeResource(getResources(), R.drawable.img_avatar_patient);
//            imgvwPatntimg.setImageBitmap(bitmp_user_img);
			imgvwPatntimg.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.img_avatar_patient));
		}else{
		
			// Get Image from External Storage
			File fileProfileImg = new File(STRNG_PATH_IMAGES_PATIENT_PROFILE + FORWARD_SLASH + mProfileImageName +".jpg");
			if(fileProfileImg.exists()){

				isProfileImageSet = true;
//				Bitmap bitmap_slsmn_img = BitmapFactory.decodeFile(fileProfileImg.getAbsolutePath());
				imgvwPatntimg.setAdjustViewBounds(true);
				imgvwPatntimg.setScaleType(ScaleType.CENTER_CROP);
//				imgvwPatntimg.setImageBitmap(bitmap_slsmn_img);
				imageLoader.displayImage("file:///" + fileProfileImg.getAbsolutePath(), imgvwPatntimg, optionsImgLoader);
				arrylstProfileimgFilepath.add(fileProfileImg.getAbsolutePath());
			}else{
				
				if(General_Fnctns.isNetConnected(getActivity())){

					ServiceHandler srvc_hndlr = new ServiceHandler();
					String url_getimg = srvc_hndlr.getImagesFromUrl(PATIENT_SMALL + FORWARD_SLASH + PROFILE_SMALL, mProfileImageName);
					
					// Get Doctor Profile image from AdminPanel
//					new Get_Image_Websrvc().execute(url_getimg);
					getImageFromWebServerStoreDevice(url_getimg);
				}
			}
		}
	}

	// Get Doctor Profile image from AdminPanel
	private void getImageFromWebServerStoreDevice(String urlGetImage) {

		imageLoader.loadImage(urlGetImage, new SimpleImageLoadingListener(){
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				super.onLoadingComplete(imageUri, view, loadedImage);

				// Set Bitmap to ImageView
				isProfileImageSet = true;
				imgvwPatntimg.setAdjustViewBounds(true);
				imgvwPatntimg.setScaleType(ScaleType.CENTER_CROP);
				// Store image on External Storage
				storePatientImageToDevice(loadedImage);
			}
		});
	}
/*

	// Get Doctor Profile image from AdminPanel
	public class Get_Image_Websrvc extends AsyncTask<String, Void, Bitmap> {
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			prgrsbrPatntimg.setVisibility(View.VISIBLE);			
		}

		protected Bitmap doInBackground(String... urls) {
		    String urldisplay = urls[0];
		    Bitmap bitmap_websrvc = null;
		    try {
		        InputStream in = new java.net.URL(urldisplay).openStream();
		        bitmap_websrvc = BitmapFactory.decodeStream(in);
		    } catch (Exception e) {
		        Log.e(TAG, e.getMessage());
		        e.printStackTrace();
		    }
		    return bitmap_websrvc;
		}

		protected void onPostExecute(Bitmap result) {
			
			//if(pgd.isShowing()) pgd.dismiss();
			if(prgrsbrPatntimg.getVisibility() == View.VISIBLE) prgrsbrPatntimg.setVisibility(View.GONE);
			
			if(result != null){

				// Set Bitmap to ImageView
				isProfileImageSet = true;
				imgvwPatntimg.setAdjustViewBounds(true);
				imgvwPatntimg.setScaleType(ScaleType.CENTER_CROP);
				imgvwPatntimg.setImageBitmap(result);
				
				// Store image on External Storage
				storePatientImageToDevice(result);	
			}
		}   
	}
*/

	// Save Web-service getted Image on External Device
	private void storePatientImageToDevice(Bitmap bitmap_websrvc_result) {

	    File dctrProfileImgPath = PATH_IMAGES_PATIENT_PROFILE;
	    dctrProfileImgPath.mkdirs();
	    
	    String patient_img_name = mProfileImageName +".jpg";
	    File file = new File (dctrProfileImgPath, patient_img_name);
	    if (file.exists ()) file.delete (); 
	    
	    try {
			FileOutputStream out = new FileOutputStream(file);
			bitmap_websrvc_result.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		arrylstProfileimgFilepath.add(file.getAbsolutePath());
	}

	// Call from mobile native dialer to given number
	private void callTOGivenNumber() {
		// TODO Auto-generated method stub

		try {
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
				if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
						!= PackageManager.PERMISSION_GRANTED){

                    /*if(shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)){

                        // Need contact permission UI for user convention.
                        Toast.makeText(this, "Must be need Permission", Toast.LENGTH_LONG).show();
                    }*/
					requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_USES_PERMISSION);
				}else{

					// Show UserNotification
					General_Fnctns.showUsrNtfyAutoHide(getActivity(), lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(getActivity(), R.color.red_alizarin), ContextCompat.getColor(getActivity(), R.color.white), getResources().getString(R.string.wait_a_momnt), HIDE_USERNOTIFY_TIME_5K);

					Intent sIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + strCurrentCallCntctNo));
					sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(sIntent);
				}
			}else{

				// Show UserNotification
				General_Fnctns.showUsrNtfyAutoHide(getActivity(), lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(getActivity(), R.color.red_alizarin), ContextCompat.getColor(getActivity(), R.color.white), getResources().getString(R.string.wait_a_momnt), HIDE_USERNOTIFY_TIME_5K);

				Intent sIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + strCurrentCallCntctNo));
				sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(sIntent);
			}
		} catch (Resources.NotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

		// For Single Permission
		if(requestCode == REQUEST_USES_PERMISSION){
			if(grantResults.length ==1 &&
					grantResults[0] == PackageManager.PERMISSION_GRANTED){

				if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) ==
						PackageManager.PERMISSION_GRANTED){

					// Show UserNotification
					General_Fnctns.showUsrNtfyAutoHide(getActivity(), lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(getActivity(), R.color.red_alizarin), ContextCompat.getColor(getActivity(), R.color.white), getResources().getString(R.string.wait_a_momnt), HIDE_USERNOTIFY_TIME_5K);

					Intent sIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + strCurrentCallCntctNo));
					sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(sIntent);
				}
			}else {

				// Show UserNotification
				General_Fnctns.showUsrNtfyAutoHide(getActivity(), lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(getActivity(), R.color.red_alizarin), ContextCompat.getColor(getActivity(), R.color.white), getResources().getString(R.string.to_call_allow_permtn), HIDE_USERNOTIFY_TIME_5K);
			}
		}else{
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		// Set Current Activity/Fragment is NOT currently running on screen
		currnt_run_onscreen_status = false;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		// Set Current Activity/Fragment is NOT currently running on screen
		currnt_run_onscreen_status = false;
	}	
}
