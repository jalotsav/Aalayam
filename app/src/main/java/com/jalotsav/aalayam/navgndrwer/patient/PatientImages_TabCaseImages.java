package com.jalotsav.aalayam.navgndrwer.patient;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jalotsav.aalayam.R;
import com.jalotsav.aalayam.adapters.GridViewImageAdapter;
import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.common.General_Fnctns;
import com.jalotsav.aalayam.common.UserSessionManager;
import com.jalotsav.aalayam.webservice.ServiceHandler;

public class PatientImages_TabCaseImages extends Fragment implements AalayamConstants {

	static UserSessionManager session;
	static Context _CONTEXT;
	
	LinearLayout lnrlyot_usr_ntfy;
	TextView tv_usr_ntfy_msg;
	
	private String slctd_ptnt_id, ptnt_case_imgs;
	private ArrayList<String> imagePaths = new ArrayList<String>();
    private GridViewImageAdapter adapter;
    private GridView gridView;
    private LinearLayout lnrlyot_noptntcaseimgs, lnrlyot_dwnldimgntfy;
    private TextView tv_dwnldimgcnt, tv_dwnldimgcntlbl;
    private int columnWidth, dwnldImgcnt = 0;
	private ProgressBar prgrsbr;
	
	ArrayList<String> arrylstPtntCaseImagesname;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.lo_patient_frgmnt_patntimages_tab_caseimgs,
				container, false);

		_CONTEXT = getActivity();
		session = new UserSessionManager(_CONTEXT);

		slctd_ptnt_id = getActivity().getIntent().getStringExtra(PT_ID);
        ptnt_case_imgs = getActivity().getIntent().getStringExtra(CASE_IMAGE_SML);

		lnrlyot_usr_ntfy = (LinearLayout)rootView.findViewById(R.id.lnrlyot_user_notify);
		tv_usr_ntfy_msg = (TextView)rootView.findViewById(R.id.tv_user_notify_msg);
        gridView = (GridView) rootView.findViewById(R.id.grdvw_ptnt_frgmnt_ptntimgs_tabptntcaseimgs);
		lnrlyot_noptntcaseimgs = (LinearLayout) rootView.findViewById(R.id.lnrlyot_ptnt_frgmnt_ptntimgs_noptntcaseimgs);
		lnrlyot_dwnldimgntfy = (LinearLayout) rootView.findViewById(R.id.lnrlyot_ptnt_frgmnt_ptntimgs_tabptntcaseimgs_dwnldimgntfy);
        tv_dwnldimgcnt = (TextView) rootView.findViewById(R.id.tv_ptnt_frgmnt_ptntimgs_tabptntcaseimgs_dwnldimgcnt);
        tv_dwnldimgcntlbl = (TextView) rootView.findViewById(R.id.tv_ptnt_frgmnt_ptntimgs_tabptntcaseimgs_dwnldimgcntlbl);
        prgrsbr = (ProgressBar) rootView.findViewById(R.id.prgrsbr_ptnt_frgmnt_ptntimgs_tabptntcaseimgs);
		
        // Initializing Grid View
        InitilizeGridLayout();
        
        arrylstPtntCaseImagesname = new ArrayList<String>();
        arrylstPtntCaseImagesname.addAll(General_Fnctns.strCommaToArray(ptnt_case_imgs.trim()));
        
        if(arrylstPtntCaseImagesname.isEmpty() || arrylstPtntCaseImagesname.size() == 0){
    		
			// Show UserNotification
			General_Fnctns.showUsrNtfyAutoHide(_CONTEXT, lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(_CONTEXT, R.color.red_alizarin), ContextCompat.getColor(_CONTEXT, R.color.white), _CONTEXT.getResources().getString(R.string.no_ptnt_case_images_onalmsrvr), HIDE_USERNOTIFY_TIME_10K);
        }else{
        	
        	new GetPatientCaseImgsPathAsync().execute();
        }

		return rootView;
	}

	// Initializing Grid View
    private void InitilizeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                AalayamConstants.GRID_PADDING_5, r.getDisplayMetrics());
 
        columnWidth = (int) ((General_Fnctns.getScreenWidth(_CONTEXT) - ((AalayamConstants.NUM_OF_COLUMNS_2 + 1) * padding)) / AalayamConstants.NUM_OF_COLUMNS_2);
 
        gridView.setNumColumns(AalayamConstants.NUM_OF_COLUMNS_2);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding, (int) padding);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);
    }

    // Get Directory paths which are contain Image and set to GridView
	private class GetPatientCaseImgsPathAsync extends AsyncTask<Void, Void, Void>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
	        // Show ProgressBar
			prgrsbr.setVisibility(View.VISIBLE);
			
			imagePaths = new ArrayList<>();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			try {

				General_Fnctns obj_gnrlfnctns = new General_Fnctns();
		        imagePaths = obj_gnrlfnctns.getFilePaths(PATH_IMAGES_PATIENT_CASE, arrylstPtntCaseImagesname);
			} catch (Exception e) {	e.printStackTrace(); }
	        
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if(imagePaths.size() > 0){

				lnrlyot_noptntcaseimgs.setVisibility(View.GONE);
				
		        // GridView adapter
		        adapter = new GridViewImageAdapter(_CONTEXT, imagePaths, columnWidth);
		 
		        // setting grid view adapter
		        gridView.setAdapter(adapter);
			}else{
				
				/*
				 * NO Image in External devices
				 */				
			}
			
			for( String ptntImgname: arrylstPtntCaseImagesname){
				
				if(!imagePaths.toString().contains(ptntImgname)){
					
					if(General_Fnctns.isNetConnected(_CONTEXT)){

						ServiceHandler srvc_hndlr = new ServiceHandler();
						String url_getimg = srvc_hndlr.getImagesFromUrl(PATIENT_SMALL + FORWARD_SLASH + CASE_SMALL, ptntImgname);
						
						// +1 in DownloadImageCount and Update text of Notification
						dwnldImgcnt++;
						General_Fnctns.updateTextofImgsDwnldNtfy(_CONTEXT, COME_FROM_TABPTNTCASEIMGS, lnrlyot_dwnldimgntfy, tv_dwnldimgcnt, tv_dwnldimgcntlbl, dwnldImgcnt);
						
						// Get Doctor Profile image from AdminPanel
						new GetImageFromWebsrvc().execute(url_getimg, ptntImgname);
					}else{

						// Show UserNotification
						General_Fnctns.showUsrNtfyAutoHide(_CONTEXT, lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(_CONTEXT, R.color.red_alizarin), ContextCompat.getColor(_CONTEXT, R.color.white), _CONTEXT.getResources().getString(R.string.no_intrnt_cnctn), HIDE_USERNOTIFY_TIME_5K);
					}
				}
			}
			
			if(dwnldImgcnt < 1) {
				
				// Get Patient Details from Web-service
				new GetPatientDetailsFromWebSrve().execute();
			}
			
	        // Hide ProgressBar
			prgrsbr.setVisibility(View.GONE);
		}
    }

	// Get Doctor Profile image from AdminPanel
	private class GetImageFromWebsrvc extends AsyncTask<String, Void, Bitmap> {
		
		String mImageName = "";
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
						
			General_Fnctns.showImgsDwnldNtfy(_CONTEXT, lnrlyot_dwnldimgntfy);
		}

		protected Bitmap doInBackground(String... params) {
			
		    Bitmap bitmap_websrvc = null;
		    try {
		    	
			    URL urlImgWebsrvc = new URL(params[0]);
			    URLConnection urlCnctn = urlImgWebsrvc.openConnection();
			    urlCnctn.connect();
			    mImageName = params[1];
			
		        InputStream in = new BufferedInputStream(urlImgWebsrvc.openStream());
		        bitmap_websrvc = BitmapFactory.decodeStream(in);		        
		    } catch (Exception e) {
		    	
		        General_Fnctns.logManager(ERROR, e.getMessage());
		        e.printStackTrace();
		    }
		    return bitmap_websrvc;
		}

		protected void onPostExecute(Bitmap result) {
			
			if(result != null){
				
				// -1 in DownloadImageCount and Update text of Notification
				dwnldImgcnt--;
				General_Fnctns.updateTextofImgsDwnldNtfy(_CONTEXT, COME_FROM_TABPTNTCASEIMGS, lnrlyot_dwnldimgntfy, tv_dwnldimgcnt, tv_dwnldimgcntlbl, dwnldImgcnt);
				
				if(dwnldImgcnt < 1) {
					
					General_Fnctns.hideImgsDwnldNtfy(_CONTEXT, lnrlyot_dwnldimgntfy);
					
					// Get Patient Details from Web-service
					new GetPatientDetailsFromWebSrve().execute();
				}
				
				// Store image on External Storage
				storeDoctorImageToDevice(result, mImageName);
				
				new GetPatientImgsPathRefrehGridAsync().execute();
			}
		}   
	}
	
	// Save Web-service getted Image on External Device
	private void storeDoctorImageToDevice(Bitmap bitmap_websrvc_result, String imageName) {

	    File dctrProfileImgPath = PATH_IMAGES_PATIENT_CASE;
	    dctrProfileImgPath.mkdirs();
	    
	    String img_name = imageName; // imageName = temp.jpg already
//	    String img_name = imageName +".jpg";
	    
	    File file = new File (dctrProfileImgPath, img_name);
	    if (file.exists ()) file.delete (); 
	    
	    try {
			FileOutputStream out = new FileOutputStream(file);
			bitmap_websrvc_result.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	        General_Fnctns.logManager(ERROR, "CtchBlk(storePatienImage) " + e.getMessage());
	    }
	}

    // Get Directory paths which are contain Image and Refresh to GridView
	private class GetPatientImgsPathRefrehGridAsync extends AsyncTask<Void, Void, Void>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
	        // Show ProgressBar
			prgrsbr.setVisibility(View.VISIBLE);
			
			imagePaths = new ArrayList<String>();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			try {

				General_Fnctns obj_gnrlfnctns = new General_Fnctns();
		        imagePaths = obj_gnrlfnctns.getFilePaths(PATH_IMAGES_PATIENT_CASE, arrylstPtntCaseImagesname);
			} catch (Exception e) {	e.printStackTrace(); }
	        
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if(imagePaths.size() > 0){

				lnrlyot_noptntcaseimgs.setVisibility(View.GONE);
				
		        // GridView adapter
		        adapter = new GridViewImageAdapter(_CONTEXT, imagePaths, columnWidth);
		 
		        // setting grid view adapter
		        gridView.setAdapter(adapter);
			}else{
				
				/*
				 * NO Image in External devices
				 */				
			}
			
	        // Hide ProgressBar
			prgrsbr.setVisibility(View.GONE);
		}
    }
	
	// Get Patient Details from Web-service
	private class GetPatientDetailsFromWebSrve extends AsyncTask<Void, Void, Void>{

		String websrvc_response;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			ServiceHandler obj_srvchndlr = new ServiceHandler();
			
			JSONObject jsnobj = new JSONObject();
			try {
				jsnobj.put(FUNCTION_SMALL, FNCTN_SELECT_PATIENT_DETAILS_BY_PT_ID);
				jsnobj.put(PT_ID, slctd_ptnt_id);
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
	    		General_Fnctns.showUsrNtfyAutoHide(_CONTEXT, lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(_CONTEXT, R.color.red_alizarin), ContextCompat.getColor(_CONTEXT, R.color.white), _CONTEXT.getResources().getString(R.string.there_seemsprblm_tryagainlater), HIDE_USERNOTIFY_TIME_5K);
			}else{
				try {
					JSONObject websrvc_jsnobj = new JSONObject(websrvc_response);
					String success = websrvc_jsnobj.getString(SUCCESS_SMALL);
					String websrvc_msg = websrvc_jsnobj.getString(MESSAGE_SMALL);
					JSONArray jsonArrayFirst = websrvc_jsnobj.getJSONArray(PATIENT_SMALL);
					if(success.equals(TRUE_SMALL)){
					
						if(!websrvc_msg.equals("null") && websrvc_msg.equals(PATIENT_FOUND)){
							
							if(jsonArrayFirst.length() !=0){
								
								for(int i=0; i< jsonArrayFirst.length(); i++){
									
									JSONArray jsonArraySecnd = jsonArrayFirst.getJSONArray(i);
									
									for(int j=0; j<jsonArraySecnd.length(); j++){

										JSONObject jsnobj_patntprfl = jsonArraySecnd.getJSONObject(j);
										
										ptnt_case_imgs = jsnobj_patntprfl.getString(CASE_IMAGE_SML);
										
										arrylstPtntCaseImagesname = new ArrayList<String>();
								        arrylstPtntCaseImagesname.addAll(General_Fnctns.strCommaToArray(ptnt_case_imgs.trim()));
								        
								        if(arrylstPtntCaseImagesname.isEmpty() || arrylstPtntCaseImagesname.size() == 0){
											
											// Show UserNotification
											General_Fnctns.showUsrNtfyAutoHide(_CONTEXT, lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(_CONTEXT, R.color.red_alizarin), ContextCompat.getColor(_CONTEXT, R.color.white), _CONTEXT.getResources().getString(R.string.no_ptnt_case_images_onalmsrvr), HIDE_USERNOTIFY_TIME_5K);								        	
								        }else{
								        	
								        	new GetPatientImgsPathRefrehGridAsync().execute();
								        }
									}
								}
							}	
						}
					}else if(success.equals(FALSE_SMALL)){

						// Show UserNotification
			    		General_Fnctns.showUsrNtfyAutoHide(_CONTEXT, lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(_CONTEXT, R.color.red_alizarin), ContextCompat.getColor(_CONTEXT, R.color.white), _CONTEXT.getResources().getString(R.string.there_seemsprblm_tryagainlater), HIDE_USERNOTIFY_TIME_5K);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}   
}
