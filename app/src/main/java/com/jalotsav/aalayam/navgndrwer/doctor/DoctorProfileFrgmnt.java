package com.jalotsav.aalayam.navgndrwer.doctor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.jalotsav.aalayam.R;
import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.common.General_Fnctns;
import com.jalotsav.aalayam.common.UserSessionManager;
import com.jalotsav.aalayam.navgndrwer.patient.FullScreenImageViewActivity;
import com.jalotsav.aalayam.webservice.ServiceHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class DoctorProfileFrgmnt extends Fragment implements AalayamConstants {
	
	UserSessionManager session;
	
	LinearLayout lnrlyot_usr_ntfy;
	TextView tv_usr_ntfy_msg;
	
	ProgressBar prgrsbrMain, prgrsbrDctrimg;
	ImageView imgvwDctrimg;
	TextView tv_name, tv_address, tv_area, tv_cntcntno, tv_email, tv_birthdate, tv_joindate, tv_qualifctn, tv_desgntn;
	String mDoctrName, mAddress, mArea = "", mCntctno, mEmail, mProfileImageName, mBirhtdate, mJoindate, mQualifctn, mDesgntn;
	
	boolean currnt_run_onscreen_status = false, isProfileImageSet=false;
	ArrayList<String> arrylstProfileimgFilepath = new ArrayList<String>();

	private ImageLoader imageLoader;
	private DisplayImageOptions optionsImgLoader;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View rootView = inflater.inflate(R.layout.lo_doctor_frgmnt_profile, container, false);

		// Set Actionbar Title
		((NavgnDrwrDoctor) getActivity()).onSectionAttached(NAVDRWER_FRGMNT_PROFILE);
		
		session = new UserSessionManager(getActivity());
		
		// Set Current Activity/Fragment is currently running on screen
		currnt_run_onscreen_status = true;

		lnrlyot_usr_ntfy = (LinearLayout)rootView.findViewById(R.id.lnrlyot_user_notify);
		tv_usr_ntfy_msg = (TextView)rootView.findViewById(R.id.tv_user_notify_msg);
		prgrsbrMain = (ProgressBar)rootView.findViewById(R.id.prgrbr_dctr_frgmnt_profile_main);
		prgrsbrDctrimg = (ProgressBar)rootView.findViewById(R.id.prgrbr_dctr_frgmnt_profile_dctrimg);
		imgvwDctrimg = (ImageView)rootView.findViewById(R.id.imgvw_dctr_frgmnt_profile_dctrimg);
		tv_name = (TextView)rootView.findViewById(R.id.tv_dctr_frgmnt_profile_dctrnm);
		tv_address = (TextView)rootView.findViewById(R.id.tv_dctr_frgmnt_profile_address);
		tv_area = (TextView)rootView.findViewById(R.id.tv_dctr_frgmnt_profile_area);
		tv_cntcntno = (TextView)rootView.findViewById(R.id.tv_dctr_frgmnt_profile_contctno);
		tv_email = (TextView)rootView.findViewById(R.id.tv_dctr_frgmnt_profile_email);
		tv_birthdate = (TextView)rootView.findViewById(R.id.tv_dctr_frgmnt_profile_birthdate);
		tv_joindate = (TextView)rootView.findViewById(R.id.tv_dctr_frgmnt_profile_joindate);
		tv_qualifctn = (TextView)rootView.findViewById(R.id.tv_dctr_frgmnt_profile_qualifctn);
		tv_desgntn = (TextView)rootView.findViewById(R.id.tv_dctr_frgmnt_profile_desgntn);

		imageLoader = ImageLoader.getInstance();
		if (!imageLoader.isInited())
			imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		optionsImgLoader = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).resetViewBeforeLoading(true)
				/*.showImageForEmptyUri(ContextCompat.getDrawable(getActivity(), R.drawable.img_avatar_doctor))
				.showImageOnFail(ContextCompat.getDrawable(getActivity(), R.drawable.img_avatar_doctor))
				.showImageOnLoading(ContextCompat.getDrawable(getActivity(), R.drawable.img_avatar_doctor))*/
				.build();
		
		tv_address.setMovementMethod(new ScrollingMovementMethod());
		tv_area.setMovementMethod(new ScrollingMovementMethod());

		if(!General_Fnctns.isNetConnected(getActivity())){
			
			// Show UserNotification
    		General_Fnctns.showUsrNtfyAutoHide(getActivity(), lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(getActivity(), R.color.red_alizarin), ContextCompat.getColor(getActivity(), R.color.white), getResources().getString(R.string.no_intrnt_cnctn), HIDE_USERNOTIFY_TIME_5K);
		}else{

			new GetDoctorProfileFromWebSrve().execute();
			
			// Get Doctor Image from External storage or URL
			checkGetDoctorImage();
		}

		imgvwDctrimg.setOnClickListener(new View.OnClickListener() {
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
		
		return rootView;		
	}
	
	public class GetDoctorProfileFromWebSrve extends AsyncTask<Void, Void, Void>{

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
				jsnobj.put(FUNCTION_SMALL, FNCTN_SELECT_DOCTOR_BY_DR_ID);
				jsnobj.put(DR_ID, String.valueOf(session.getDrId()));
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
	    		General_Fnctns.showUsrNtfyAutoHide(getActivity(), lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(getActivity(), R.color.red_alizarin), ContextCompat.getColor(getActivity(), R.color.white), getResources().getString(R.string.there_seemsprblm_tryagainlater), HIDE_USERNOTIFY_TIME_5K);
			}else{
				try {
					JSONObject websrvc_jsnobj = new JSONObject(websrvc_response);
					String success = websrvc_jsnobj.getString(SUCCESS_SMALL);
					String websrvc_msg = websrvc_jsnobj.getString(MESSAGE_SMALL);
					JSONArray jsonArrayFirst = websrvc_jsnobj.getJSONArray(DOCTOR_SMALL);
					JSONArray jsonArrayAreaFirst = websrvc_jsnobj.getJSONArray(AREA_SMALL);
					if(success.equals(TRUE_SMALL)){
					
						if(!websrvc_msg.equals("null") && websrvc_msg.equals(DOCTOR_FOUND)){
							
							if(jsonArrayFirst.length() !=0){
								
								for(int i=0; i< jsonArrayFirst.length(); i++){
									
									JSONArray jsonArraySecnd = jsonArrayFirst.getJSONArray(i);
									
									for(int j=0; j<jsonArraySecnd.length(); j++){

										JSONObject jsnobj_dctrprfl = jsonArraySecnd.getJSONObject(j);
										
										mDoctrName = jsnobj_dctrprfl.getString(NAME_FN_SML) + " " + jsnobj_dctrprfl.getString(NAME_LN_SML); 
										mAddress = jsnobj_dctrprfl.getString(ADDRESS_SML);
//										mArea = jsnobj_dctrprfl.getString(AREA_ID_SML);
										mCntctno = jsnobj_dctrprfl.getString(CONTACT_NO_SML);
										mEmail = jsnobj_dctrprfl.getString(EMAIL_SML);
										mProfileImageName = jsnobj_dctrprfl.getString(PROFILE_IMAGE_SML);
										mBirhtdate = jsnobj_dctrprfl.getString(BIRTH_DATE_SML);
										mJoindate = jsnobj_dctrprfl.getString(JOIN_DATE_SML);
										mQualifctn = jsnobj_dctrprfl.getString(QUALIFICATION_SML);
										mDesgntn = jsnobj_dctrprfl.getString(DESIGNATION_SML);
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
										mArea += jsnobj_area.getString(A_TITLE) + ", ";
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

				tv_name.setText(mDoctrName);
				tv_address.setText(mAddress);
				if(!TextUtils.isEmpty(mArea)){

					if(mArea.length() > 2)
						tv_area.setText(mArea.substring(0, mArea.length() - 2));
					else
						tv_area.setText(mArea);
				}
				tv_cntcntno.setText(mCntctno);
				tv_email.setText(mEmail);
				tv_birthdate.setText(mBirhtdate);
				tv_joindate.setText(mJoindate);
				tv_qualifctn.setText(mQualifctn);
				tv_desgntn.setText(mDesgntn);
				
				// Check image to External storage if not available Get from AdminPanel and store to device
				checkGetDoctorImage();
			}
		}catch(Exception e){
			General_Fnctns.logManager(ERROR, "Catch  block(GetSetWeb-service Data):" + e.getMessage());
		}
	}

	// Check image to External storage if not available Get from AdminPanel and store to device
	private void checkGetDoctorImage() {
		// TODO Auto-generated method stub
		if(mProfileImageName == null || mProfileImageName.equals("")){

			// Set Default image to ImageView
			isProfileImageSet = false;
//        	Bitmap bitmp_user_img = BitmapFactory.decodeResource(getResources(), R.drawable.img_avatar_doctor);
//            imgvwDctrimg.setImageBitmap(bitmp_user_img);
			imgvwDctrimg.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.img_avatar_doctor));
		}else{
		
			// Get Image from External Storage
			File fileProfileImg = new File(STRNG_PATH_IMAGES_DOCTOR + FORWARD_SLASH + mProfileImageName +".jpg");
			if(fileProfileImg.exists()){

				isProfileImageSet = true;
//				Bitmap bitmap_slsmn_img = BitmapFactory.decodeFile(fileProfileImg.getAbsolutePath());
				imgvwDctrimg.setAdjustViewBounds(true);
				imgvwDctrimg.setScaleType(ScaleType.CENTER_CROP);
//				imgvwDctrimg.setImageBitmap(bitmap_slsmn_img);
				imageLoader.displayImage("file:///" + fileProfileImg.getAbsolutePath(), imgvwDctrimg, optionsImgLoader);
				arrylstProfileimgFilepath.add(fileProfileImg.getAbsolutePath());
			}else{
				
				if(General_Fnctns.isNetConnected(getActivity())){

					ServiceHandler srvc_hndlr = new ServiceHandler();
					String url_getimg = srvc_hndlr.getImagesFromUrl(DOCTOR_SMALL, mProfileImageName);
					
					// Get Doctor Profile image from AdminPanel
//					new GetImageFromWebsrvc().execute(url_getimg);
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
				imgvwDctrimg.setAdjustViewBounds(true);
				imgvwDctrimg.setScaleType(ScaleType.CENTER_CROP);
				// Store image on External Storage
				storeDoctorImageToDevice(loadedImage);
			}
		});
	}
/*

	// Get Doctor Profile image from AdminPanel
	public class GetImageFromWebsrvc extends AsyncTask<String, Void, Bitmap> {
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			prgrsbrDctrimg.setVisibility(View.VISIBLE);		
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
			if(prgrsbrDctrimg.getVisibility() == View.VISIBLE) prgrsbrDctrimg.setVisibility(View.GONE);
			
			if(result != null){

				// Set Bitmap to ImageView
				isProfileImageSet = true;
				imgvwDctrimg.setAdjustViewBounds(true);
				imgvwDctrimg.setScaleType(ScaleType.CENTER_CROP);
				imgvwDctrimg.setImageBitmap(result);
				
				// Store image on External Storage
				storeDoctorImageToDevice(result);	
			}
		}   
	}
*/

	// Save Web-service getted Image on External Device
	private void storeDoctorImageToDevice(Bitmap bitmap_websrvc_result) {

	    File dctrProfileImgPath = PATH_IMAGES_DOCTOR;
	    dctrProfileImgPath.mkdirs();
	    
	    String dctr_img_name = mProfileImageName +".jpg";
	    File file = new File (dctrProfileImgPath, dctr_img_name);
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
