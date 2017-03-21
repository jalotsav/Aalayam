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

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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

import com.bsptechno.libfabbsptechno.FloatingActionButton;
import com.bsptechno.libfabbsptechno.FloatingActionsMenu;
import com.jalotsav.aalayam.R;
import com.jalotsav.aalayam.adapters.GridViewImageAdapter;
import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.common.General_Fnctns;
import com.jalotsav.aalayam.common.UserSessionManager;
import com.jalotsav.aalayam.webservice.ServiceHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class PatientImages_TabPatntImages extends Fragment implements AalayamConstants {

	static UserSessionManager session;
	static Context _CONTEXT;
	
	LinearLayout lnrlyot_usr_ntfy;
	TextView tv_usr_ntfy_msg;
	
	private String slctd_ptnt_id,ptnt_imgs;
	private ArrayList<String> imagePaths = new ArrayList<String>();
    private GridViewImageAdapter adapter;
    private GridView gridView;
    private LinearLayout lnrlyot_noptntimgs, lnrlyot_dwnldimgntfy;
    private TextView tv_dwnldimgcnt, tv_dwnldimgcntlbl;
    private int columnWidth, dwnldImgcnt = 0;
    private FloatingActionsMenu fab_menu;
	private FloatingActionButton fab_mini_addgallery, fab_mini_addcamera;
	private ProgressBar prgrsbr;
	
	ArrayList<String> arrylstPtntImagesname;
	
	// Variables for Capture/Pick image
	private static final int GALLERY_REQUEST = 10;
	private static final int CAMERA_REQUEST = 20;
    private static final int MEDIA_TYPE_IMAGE = 1;
	private Uri slctd_cptur_pic_uri;
	Bitmap slctd_cptur_pic_btmp;
	boolean isCameraRequest;

	private ImageLoader imageLoader;
	private DisplayImageOptions optionsImgLoader;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.lo_patient_frgmnt_patntimages_tab_patntimgs, container, false);

		_CONTEXT = getActivity();
		session = new UserSessionManager(_CONTEXT);
        
		slctd_ptnt_id = getActivity().getIntent().getStringExtra(PT_ID);
        ptnt_imgs = getActivity().getIntent().getStringExtra(IMAGE_SML);

		lnrlyot_usr_ntfy = (LinearLayout)rootView.findViewById(R.id.lnrlyot_user_notify);
		tv_usr_ntfy_msg = (TextView)rootView.findViewById(R.id.tv_user_notify_msg);
        gridView = (GridView) rootView.findViewById(R.id.grdvw_ptnt_frgmnt_ptntimgs_tabptntimgs);
		lnrlyot_noptntimgs = (LinearLayout) rootView.findViewById(R.id.lnrlyot_ptnt_frgmnt_ptntimgs_noptntimgs);
		lnrlyot_dwnldimgntfy = (LinearLayout) rootView.findViewById(R.id.lnrlyot_ptnt_frgmnt_ptntimgs_tabptntimgs_dwnldimgntfy);
        tv_dwnldimgcnt = (TextView) rootView.findViewById(R.id.tv_ptnt_frgmnt_ptntimgs_tabptntimgs_dwnldimgcnt);
        tv_dwnldimgcntlbl = (TextView) rootView.findViewById(R.id.tv_ptnt_frgmnt_ptntimgs_tabptntimgs_dwnldimgcntlbl);
        fab_menu = (FloatingActionsMenu) rootView.findViewById(R.id.fabmenu_ptnt_frgmnt_ptntimgs_tabptntimgs_addimg);
        fab_mini_addgallery = (FloatingActionButton) rootView.findViewById(R.id.fab_ptnt_frgmnt_ptntimgs_tabptntimgs_addgallery);
        fab_mini_addcamera = (FloatingActionButton) rootView.findViewById(R.id.fab_ptnt_frgmnt_ptntimgs_tabptntimgs_addcamera);
        prgrsbr = (ProgressBar) rootView.findViewById(R.id.prgrsbr_ptnt_frgmnt_ptntimgs_tabptntimgs);
		
        // Initializing Grid View
        InitilizeGridLayout();

		// Universal Image Loader Configuration
		imageLoader = ImageLoader.getInstance();
		if (!imageLoader.isInited())
			imageLoader.init(ImageLoaderConfiguration.createDefault(_CONTEXT));
		optionsImgLoader = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).resetViewBeforeLoading(true)
				.build();
        
        arrylstPtntImagesname = new ArrayList<String>();
        arrylstPtntImagesname.addAll(General_Fnctns.strCommaToArray(ptnt_imgs.trim()));
        
        if(arrylstPtntImagesname.isEmpty() || arrylstPtntImagesname.size() == 0){
			
			// Show UserNotification
			General_Fnctns.showUsrNtfyAutoHide(_CONTEXT, lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(_CONTEXT, R.color.red_alizarin), ContextCompat.getColor(_CONTEXT, R.color.white), _CONTEXT.getResources().getString(R.string.no_ptnt_images_onalmsrvr), HIDE_USERNOTIFY_TIME_5K);
        }else{
        	
        	new GetPatientImgsPathAsync().execute();
        }
		
		fab_mini_addgallery.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
            	
				fab_menu.collapse();
				isCameraRequest = false;

				checkStorageCameraPermission();
			}
		});
		fab_mini_addcamera.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				fab_menu.collapse();
				isCameraRequest = true;

				checkStorageCameraPermission();
			}
		});
		
		return rootView;
	}

	// Check Storage & Camera permission for use
	private void checkStorageCameraPermission() {

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

			if (!isCheckSelfPermission())
				requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_APP_PERMISSION);
			else
				launchRequestedActvty();
		} else
			launchRequestedActvty();
	}

	private boolean isCheckSelfPermission(){

		int selfPermsnStorage = ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
		int selfPermsnCamera = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
		return  selfPermsnStorage == PackageManager.PERMISSION_GRANTED && selfPermsnCamera == PackageManager.PERMISSION_GRANTED;
	}

	// Launch Gallery/Camera activity for select/Capture photo
	private void launchRequestedActvty() {

		if(isCameraRequest) {

			// Open Camera for Take Photo
			Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			slctd_cptur_pic_uri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, slctd_cptur_pic_uri);
			startActivityForResult(cameraIntent, CAMERA_REQUEST);
		} else {

			//Directly open Gallery for Choose Photo
			Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(galleryIntent, GALLERY_REQUEST);
		}
	}

	// get capture image URI and store as temp.jpeg
	public Uri getOutputMediaFileUri(int type) {
	    return Uri.fromFile(getOutputMediaFile(type));
	}

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(STRNG_PATH_IMAGES_PATIENT_IMAGES);
 
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
 
                return null;
            }
        }
        // Create a media file name
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "temp.jpeg");
        } else {
            return null;
        }
 
        return mediaFile;
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
	private class GetPatientImgsPathAsync extends AsyncTask<Void, Void, Void>{

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
		        imagePaths = obj_gnrlfnctns.getFilePaths(PATH_IMAGES_PATIENT_IMAGES, arrylstPtntImagesname);
		        
			} catch (Exception e) {	}
	        
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if(imagePaths.size() > 0){

				lnrlyot_noptntimgs.setVisibility(View.GONE);
				
		        // GridView adapter
		        adapter = new GridViewImageAdapter(_CONTEXT, imagePaths, columnWidth);
		 
		        // setting grid view adapter
		        gridView.setAdapter(adapter);
			}else{
				
				/*
				 * NO Image in External devices
				 */				
			}
			
			for( String ptntImgname: arrylstPtntImagesname){
				
				if(!imagePaths.toString().contains(ptntImgname)){
					
					if(General_Fnctns.isNetConnected(_CONTEXT)){

						ServiceHandler srvc_hndlr = new ServiceHandler();
						String url_getimg = srvc_hndlr.getImagesFromUrl(PATIENT_SMALL + FORWARD_SLASH + IMAGES_SMALL, ptntImgname);
												
						// +1 in DownloadImageCount and Update text of Notification
						dwnldImgcnt++;
						General_Fnctns.updateTextofImgsDwnldNtfy(_CONTEXT, COME_FROM_TABPTNTIMGS, lnrlyot_dwnldimgntfy, tv_dwnldimgcnt, tv_dwnldimgcntlbl, dwnldImgcnt);
						
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
		
		String mProfileImageName = "";
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
						
			General_Fnctns.showImgsDwnldNtfy(_CONTEXT, lnrlyot_dwnldimgntfy);
		}

		protected Bitmap doInBackground(String... params) {
			
//			int count;
		    Bitmap bitmap_websrvc = null;
		    try {
		    	
			    URL urlImgWebsrvc = new URL(params[0]);
			    URLConnection urlCnctn = urlImgWebsrvc.openConnection();
			    urlCnctn.connect();
//			    int lenghtOfFile = urlCnctn.getContentLength();
			    mProfileImageName = params[1];
			
		        InputStream in = new BufferedInputStream(urlImgWebsrvc.openStream());
		        bitmap_websrvc = BitmapFactory.decodeStream(in);
		        /*byte data[] = new byte[1024];
                long total = 0;
 
                while ((count = in.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    General_Fnctns.logManager(DEBUG, ""+(int)((total*100)/lenghtOfFile));
                }*/		        
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
				General_Fnctns.updateTextofImgsDwnldNtfy(_CONTEXT, COME_FROM_TABPTNTIMGS, lnrlyot_dwnldimgntfy, tv_dwnldimgcnt, tv_dwnldimgcntlbl, dwnldImgcnt);
				
				if(dwnldImgcnt < 1) {

					General_Fnctns.hideImgsDwnldNtfy(_CONTEXT, lnrlyot_dwnldimgntfy);
					
					// Get Patient Details from Web-service
					new GetPatientDetailsFromWebSrve().execute();
				}
				
				// Store image on External Storage
				storeDoctorImageToDevice(result, mProfileImageName);
				
				new GetPatientImgsPathRefrehGridAsync().execute();
			}
		}   
	}
	
	// Save Web-service getted Image on External Device
	private void storeDoctorImageToDevice(Bitmap bitmap_websrvc_result, String imageName) {

	    File dctrProfileImgPath = PATH_IMAGES_PATIENT_IMAGES;
	    dctrProfileImgPath.mkdirs();
	    
	    String img_name = imageName; // imageName = temp.jpeg already
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
		        imagePaths = obj_gnrlfnctns.getFilePaths(PATH_IMAGES_PATIENT_IMAGES, arrylstPtntImagesname);
			} catch (Exception e) {	}
	        
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if(imagePaths.size() > 0){

				lnrlyot_noptntimgs.setVisibility(View.GONE);
				
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == GALLERY_REQUEST && resultCode == getActivity().RESULT_OK){
			
			//Get Selected Image URI
			slctd_cptur_pic_uri = data.getData();
		
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = _CONTEXT.getContentResolver().query(
                               slctd_cptur_pic_uri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

//            slctd_cptur_pic_btmp = BitmapFactory.decodeFile(filePath);
			slctd_cptur_pic_btmp = imageLoader.loadImageSync("file:///" + filePath);
            
            // Save image to device with temp.jpeg
            General_Fnctns.saveBitmapToDevice(slctd_cptur_pic_btmp, STRNG_PATH_IMAGES_PATIENT_IMAGES, "temp.jpeg");
			
			if(General_Fnctns.isNetConnected(_CONTEXT)){
	            
	            // Upload image to server
	            new ImageUploadTask().execute();
			}else{

				// Show UserNotification
				General_Fnctns.showUsrNtfyAutoHide(_CONTEXT, lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(_CONTEXT, R.color.red_alizarin), ContextCompat.getColor(_CONTEXT, R.color.white), _CONTEXT.getResources().getString(R.string.no_intrnt_cnctn), HIDE_USERNOTIFY_TIME_5K);
			}			
		}else if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK) {
			
			//Get Capture Image URI
//            BitmapFactory.Options options = new BitmapFactory.Options();
            // downsizing image as it throws OutOfMemory Exception for larger images
//            options.inSampleSize = 8;
 
//            slctd_cptur_pic_btmp = BitmapFactory.decodeFile(slctd_cptur_pic_uri.getPath(), options);
			slctd_cptur_pic_btmp = imageLoader.loadImageSync("file:///" + slctd_cptur_pic_uri.getPath());

            // Save image to device with temp.jpeg
            General_Fnctns.saveBitmapToDevice(slctd_cptur_pic_btmp, STRNG_PATH_IMAGES_PATIENT_IMAGES, "temp.jpeg");
			
			if(General_Fnctns.isNetConnected(_CONTEXT)){
	            
	            // Upload image to server
	            new ImageUploadTask().execute();
			}else{

				// Show UserNotification
				General_Fnctns.showUsrNtfyAutoHide(_CONTEXT, lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(_CONTEXT, R.color.red_alizarin), ContextCompat.getColor(_CONTEXT, R.color.white), _CONTEXT.getResources().getString(R.string.no_intrnt_cnctn), HIDE_USERNOTIFY_TIME_5K);
			}
        }else if(requestCode == 0){
        	
        	/*Set Default Image
        	 * if user not capture image from cameras
        	 * if user not pick image from gallery
        	 * if user not crop image*/
//        	Bitmap bitmp_user_img = BitmapFactory.decodeResource(getResources(), R.drawable.ic_navgtn_drwr_cmpny);
//            imgvw_userimg.setImageBitmap(bitmp_user_img);
            
        }
		
	}
	
	// Image Upload to server AsyncTask
	private class ImageUploadTask extends AsyncTask<Void, Void, String> {

		// private ProgressDialog dialog;
		private ProgressDialog dialog = new ProgressDialog(_CONTEXT);

		@Override
		protected void onPreExecute() {
			
			dialog.setMessage("Patient image uploading to server...");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {
			
			String websrvc_response = "";
			
			try {
				
				ServiceHandler obj_srvchndlr = new ServiceHandler();
				
	        	String str[] = new String[1];
	        	
				if(slctd_cptur_pic_btmp != null){

					General_Fnctns.logManager(WARN, "Croped pic not null");
		            str[0] = General_Fnctns.encodeTobase64(slctd_cptur_pic_btmp);	
				}else{

					File file = new File(STRNG_PATH_IMAGES_PATIENT_IMAGES,"temp.jpeg");
					Bitmap btmp = BitmapFactory.decodeFile(file.getAbsolutePath());
					str[0] = General_Fnctns.encodeTobase64(btmp);
				}

				// Convert 1000x1000 croped image to 150x150
//	        	Bitmap imgvw_bitmap = Bitmap.createScaledBitmap(slctd_cptur_pic_btmp, 150, 150, true);
//	            imgvw_userimg.setImageBitmap(imgvw_bitmap);
	            
				JSONObject jsnobj = new JSONObject();
				try {
					jsnobj.put(FUNCTION_SMALL, FNCTN_ADD_PATIENT_IMAGE_BY_PT_ID);
					jsnobj.put(PT_ID, slctd_ptnt_id);
					jsnobj.put(BASE_64, str[0]);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String json = WEBSRVC_JSONKEY + jsnobj.toString();
				websrvc_response = obj_srvchndlr.post_makeServiceCall(json);
				
				/**************************************************************/
/*				ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();
				
				nameValuePairs.add(new BasicNameValuePair("Json=",str[0].toString()));
				
				nameValuePairs.add(new BasicNameValuePair("created_from","Android"));
				Log.e(TAG,"->"+url);
				try {
					if (android.os.Build.VERSION.SDK_INT > 9) {
						StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
						StrictMode.setThreadPolicy(policy);
					}
					
					for(NameValuePair property:nameValuePairs)
					{
						Log.e(property.getName(),"->"+property.getValue());
					}
					
					Log.e(TAG,"Service Call");
					HttpParams httpParameters = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParameters, 600000);
					// http client
					DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
					HttpEntity httpEntity = null;
					HttpResponse httpResponse = null;

						HttpPost httpPost = new HttpPost(url);
						// adding post params
						if (params != null) {
							httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
						}

						httpResponse = httpClient.execute(httpPost);

					httpEntity = httpResponse.getEntity();
					String response = EntityUtils.toString(httpEntity);
					
					Log.e(TAG,"Response->"+ response);
				}catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG,"Cathc Block Image Upload: " + e.getMessage());
				}
*/				
				/****************************************************/
				
				/************************************************************************/
/*				BufferedReader inBuffer = null;
				String response = "";
				try {
					
					HttpClient httpClient = new DefaultHttpClient();
					HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 100000);
					HttpPost request = new HttpPost(url);
		            
		            StringEntity se = new StringEntity(json[0].toString()); 
		            se.setChunked(true);
		            request.addHeader("content-type", "application/x-www-form-urlencoded; multipart/form-data");
					request.setEntity(se);
					
					HttpResponse httpResponse = httpClient.execute(request);
					
					inBuffer = new BufferedReader(new InputStreamReader(
							httpResponse.getEntity().getContent()));
					
					StringBuffer stringBuffer = new StringBuffer("");
					String line = "";
					String newLine = System.getProperty("line.separator");
					while ((line = inBuffer.readLine()) != null) {
						stringBuffer.append(line + newLine);
					}
					inBuffer.close();

					response = stringBuffer.toString();
					Log.d(TAG, "Websrvc_Respns: " + response);
				} catch(Exception e) {
					e.printStackTrace();
					Log.e(TAG, "sh.post_makeServiceCall Catch Block: " +  e.getMessage());
				} finally {
					if (inBuffer != null) {
						try {
							inBuffer.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
*/				
				/********************************************************************/
				/*HttpClient httpClient = new DefaultHttpClient();
				HttpContext localContext = new BasicHttpContext();
				HttpPost httpPost = new HttpPost(webAddressToPost);
				
				//MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
				
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				slctd_cptur_pic_btmp.compress(CompressFormat.JPEG, 100, bos);
				byte[] data = bos.toByteArray();
				//String str = data.toString();
				//Log.i("BspTechno_SMS", str);
				byte[] file = Base64.encode(data, 0);
				Log.i("BspTechno_SMS", file.toString());
				//entity.addPart("uploaded", new StringBody(file));
				
				entity.addPart("someOtherStringToSend", new StringBody("your string here"));
				
				httpPost.setEntity(entity);
				HttpResponse response = httpClient.execute(httpPost,
				  localContext);
				BufferedReader reader = new BufferedReader(
				  new InputStreamReader(
				    response.getEntity().getContent(), "UTF-8"));
				
				String sResponse = reader.readLine();
				return sResponse;*/
				
				/***************************************************************************/
/*				String url = "http://192.168.1.200:1234/PHP_server/SMS/services/web/client_reg.php?function=uploadImage&salesman_id=6&mobile_div_id=e7fcb9cba1564f84";
				File file = new File(Environment.getExternalStorageDirectory(),"crop.jpg");
				BufferedReader inBuffer = null;
				String response_str = "";	
			    HttpClient httpclient = new DefaultHttpClient();

			    HttpPost httppost = new HttpPost(url);

			    String str[] = new String[1];
	            str[0] = encodeTobase64(slctd_cptur_pic_btmp);
	            
	            String json[] = new String[1];
				json[0] = "Json=" + str[0];
			    
			    InputStreamEntity reqEntity = new InputStreamEntity(new FileInputStream(file), -1);
			    reqEntity.setContentType("binary/octet-stream");
			    reqEntity.setChunked(true); // Send in multiple parts if needed
			    httppost.setEntity(reqEntity);
			    HttpResponse response = httpclient.execute(httppost);
				
				inBuffer = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				
				StringBuffer stringBuffer = new StringBuffer("");
				String line = "";
				String newLine = System.getProperty("line.separator");
				while ((line = inBuffer.readLine()) != null) {
					stringBuffer.append(line + newLine);
				}
				inBuffer.close();

				response_str = stringBuffer.toString();
				Log.d(TAG, "Websrvc_Respns: " + response_str);
*/				
			} catch (Exception e) {
				// something went wrong. connection with the server error
			}
			return websrvc_response;
		  }
	
		  @Override
		  protected void onPostExecute(String result) {
				
				if(result.equals(ACCESS_DENIED)){

					// Show UserNotification
		    		General_Fnctns.showUsrNtfyAutoHide(_CONTEXT, lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(_CONTEXT, R.color.red_alizarin), ContextCompat.getColor(_CONTEXT, R.color.white), _CONTEXT.getResources().getString(R.string.there_seemsprblm_tryagainlater), HIDE_USERNOTIFY_TIME_5K);
				}else{
					try {
						JSONObject websrvc_jsnobj = new JSONObject(result);
						String success = websrvc_jsnobj.getString(SUCCESS_SMALL);
						String websrvc_msg = websrvc_jsnobj.getString(MESSAGE_SMALL);
						String imageName = websrvc_jsnobj.getString(IMAGE_SML);
						if(success.equals(TRUE_SMALL)){
						
							if(!websrvc_msg.equals("null") && websrvc_msg.equals("Image")){
								
								// Rename temp.jpeg with Web-service response image name
								General_Fnctns.renameFilenameInDevice(STRNG_PATH_IMAGES_PATIENT_IMAGES, "temp.jpeg", imageName);

								// Show UserNotification
								General_Fnctns.showUsrNtfyAutoHide(_CONTEXT, lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(_CONTEXT, R.color.red_alizarin), ContextCompat.getColor(_CONTEXT, R.color.white), _CONTEXT.getResources().getString(R.string.ptnt_image_uploaded), HIDE_USERNOTIFY_TIME_5K);		
								
								// Add new uploaded image name to array
								arrylstPtntImagesname.add(imageName);
								// Refresh GridView
								new GetPatientImgsPathRefrehGridAsync().execute();
							}else{

								// Show UserNotification
					    		General_Fnctns.showUsrNtfyAutoHide(_CONTEXT, lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(_CONTEXT, R.color.red_alizarin), ContextCompat.getColor(_CONTEXT, R.color.white), _CONTEXT.getResources().getString(R.string.there_seemsprblm_tryagainlater), HIDE_USERNOTIFY_TIME_5K);
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
			  
		   dialog.dismiss();
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
										
										ptnt_imgs = jsnobj_patntprfl.getString(IMAGE_SML);
										
										arrylstPtntImagesname = new ArrayList<String>();
								        arrylstPtntImagesname.addAll(General_Fnctns.strCommaToArray(ptnt_imgs.trim()));
								        
								        if(arrylstPtntImagesname.isEmpty() || arrylstPtntImagesname.size() == 0){
											
											// Show UserNotification
											General_Fnctns.showUsrNtfyAutoHide(_CONTEXT, lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(_CONTEXT, R.color.red_alizarin), ContextCompat.getColor(_CONTEXT, R.color.white), _CONTEXT.getResources().getString(R.string.no_ptnt_images_onalmsrvr), HIDE_USERNOTIFY_TIME_5K);								        	
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

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

		if(requestCode == REQUEST_APP_PERMISSION) {

			if(grantResults.length > 0) {

				if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
					launchRequestedActvty();
				} else
					General_Fnctns.showUsrNtfyAutoHide(_CONTEXT, lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(_CONTEXT, R.color.red_alizarin), ContextCompat.getColor(_CONTEXT, R.color.white), _CONTEXT.getResources().getString(R.string.allow_permtn_gallery_camera), HIDE_USERNOTIFY_TIME_5K);
			}
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}
}
