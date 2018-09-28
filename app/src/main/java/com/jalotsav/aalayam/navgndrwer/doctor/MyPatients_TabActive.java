package com.jalotsav.aalayam.navgndrwer.doctor;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.jalotsav.aalayam.R;
import com.jalotsav.aalayam.adapters.Listvw_Adptr_Icontext;
import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.common.General_Fnctns;
import com.jalotsav.aalayam.common.UserSessionManager;
import com.jalotsav.aalayam.navgndrwer.patient.NavgnDrawer_Main_Patient;
import com.jalotsav.aalayam.webservice.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MyPatients_TabActive extends Fragment implements AalayamConstants, SearchView.OnQueryTextListener, SearchView.OnCloseListener, SwipeRefreshLayout.OnRefreshListener {

	static UserSessionManager session;

	CoordinatorLayout cordntrlyotMain;
	SwipeRefreshLayout swiperfrshlyot, swiperfrshlyot_emptyvw;
	SearchView search_view;
	ImageView imgvw_qrcode;
	ListView lstvw_patientlst;
//	ArrayAdapter<String> arryadptr_patientlst;
	Listvw_Adptr_Icontext lstvwAdptrIcontext;

	ArrayList<String> arrylst_patientnm, arrylst_patient_id;
	static ArrayList<String> arrylst_patientnm_temp;
	SparseArray<String> sprsarry_patient_images, sprsarry_patient_cashimages;
	
	// For QRCode scanner
	private final static int ACTION_ZXING_SCANNER = 0x0000c0de;
	
	boolean currnt_run_onscreen_status = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	
		View rootView = inflater.inflate(R.layout.lo_doctor_frgmnt_patients, container, false);

		session = new UserSessionManager(getActivity());
		
		// Set Current Activity/Fragment is currently running on screen
		currnt_run_onscreen_status = true;

		cordntrlyotMain = (CoordinatorLayout) rootView.findViewById(R.id.cordntrlyot_dctr_frgmnt_patient);
		swiperfrshlyot = (SwipeRefreshLayout)rootView.findViewById(R.id.swiperfrshlyot_dctr_frgmnt_patient);
		swiperfrshlyot_emptyvw = (SwipeRefreshLayout)rootView.findViewById(R.id.swiperfrshlyot_dctr_frgmnt_patient_emptyview);
		imgvw_qrcode = (ImageView)rootView.findViewById(R.id.imgvw_dctr_frgmnt_patient_qrcode);
		lstvw_patientlst = (ListView)rootView.findViewById(R.id.lstvw_dctr_frgmnt_patient_clientlist);

		SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
		search_view = (SearchView) rootView.findViewById(R.id.srchvw_dctr_frgmnt_patient_search);
		search_view.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
		search_view.setIconifiedByDefault(false);
		search_view.setOnQueryTextListener(this);
		search_view.setOnCloseListener(this);
		  
		//Style of SearchView	    
		int searchPlateId = getResources().getIdentifier("android:id/search_plate", null, null);
		View searchPlate = search_view.findViewById(searchPlateId);   
		if (searchPlate!=null) {
			searchPlate.setBackgroundColor(Color.TRANSPARENT);
            
            //Search Icon
            int searchIconId = searchPlate.getContext().getResources().getIdentifier("android:id/search_mag_icon", null, null);
            ImageView searchIcon = (ImageView) search_view.findViewById(searchIconId);
            searchIcon.setImageResource(R.drawable.ic_search_white);
            
            //Search EditText
            int searchTextId = searchPlate.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            EditText searchEditText = (EditText) searchPlate.findViewById(searchTextId);  
            TextView searchText = (TextView) searchPlate.findViewById(searchTextId);
            if (searchText!=null) {
	            searchText.setTextColor(Color.WHITE);
	            searchText.setHintTextColor(Color.WHITE);
	            searchText.setHint(R.string.search_ptnt);
            }
            
            // set Cursor White color
            try {
                // https://github.com/android/platform_frameworks_base/blob/kitkat-release/core/java/android/widget/TextView.java#L562-564
                Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
                f.setAccessible(true);
                f.set(searchEditText, R.drawable.et_cursor_white);
            } catch (Exception ignored) {
            }
            
            //Search Close Icon
            int searchCloseIconId = searchPlate.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
            ImageView searchCloseIcon = (ImageView) search_view.findViewById(searchCloseIconId);
            searchCloseIcon.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
    	}
		
		arrylst_patient_id = new ArrayList<String>();
		arrylst_patientnm = new ArrayList<String>();
		arrylst_patientnm_temp = new ArrayList<String>();
		sprsarry_patient_images = new SparseArray<String>();
		sprsarry_patient_cashimages = new SparseArray<String>();
		
		lstvwAdptrIcontext = new Listvw_Adptr_Icontext(getActivity(), ICON_FIRST_CHAR_OFTEXT, arrylst_patientnm_temp);
		// Set EmptyView to ListView
		lstvw_patientlst.setEmptyView(swiperfrshlyot_emptyvw);

		// Initialization of SwipeRefreshLayout
		initSwipeRefreshLayout(swiperfrshlyot);
		initSwipeRefreshLayout(swiperfrshlyot_emptyvw);
		
		// Get Patient Id&Name as in List and Only Patient Name to Listview
//		getPatientIdNameSetLstvw();

		if(!General_Fnctns.isNetConnected(getActivity())){

			// Show SnackBar with given message
			showMySnackBar(getResources().getString(R.string.no_intrnt_cnctn));
		}else{

			swiperfrshlyot.post(new Runnable() {
			    @Override
			    public void run() {

			    	swiperfrshlyot.setRefreshing(true);
			    	swiperfrshlyot_emptyvw.setRefreshing(true);

					new GetActivePatientFromWebSrve().execute();
			    }
			});
		}
		
		lstvw_patientlst.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				
				// Start Company Tab Activity
				Intent intnt_patient = new Intent(getActivity(), NavgnDrawer_Main_Patient.class);
				intnt_patient.putExtra(PT_ID, arrylst_patient_id.get(position));
				intnt_patient.putExtra(PATIENT_NAME_SML, arrylst_patientnm.get(position));
				intnt_patient.putExtra(IMAGE_SML, sprsarry_patient_images.get(Integer.parseInt(arrylst_patient_id.get(position))).toString());
				intnt_patient.putExtra(CASE_IMAGE_SML, sprsarry_patient_cashimages.get(Integer.parseInt(arrylst_patient_id.get(position))).toString());
				startActivity(intnt_patient);
			}
		});
		
		imgvw_qrcode.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(getActivity(), ZBarScannerActivity.class);
				startActivityForResult(intent, ACTION_ZXING_SCANNER);
			}
		});
		
		return rootView;
	}

	@Override
	public boolean onClose() {
		// TODO Auto-generated method stub
		
		lstvwAdptrIcontext.getFilter().filter("");
		return false;
	}

	@Override
	public boolean onQueryTextChange(String query) {
		// TODO Auto-generated method stub
		
		if(lstvwAdptrIcontext !=null || lstvwAdptrIcontext.getCount() > 0){

			lstvwAdptrIcontext.getFilter().filter(query);	
		}
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		
		if(lstvwAdptrIcontext !=null || lstvwAdptrIcontext.getCount() > 0){

			lstvwAdptrIcontext.getFilter().filter(query);	
		}
		return false;
	}

	// Initialization of SwipeRefreshLayout
    private void initSwipeRefreshLayout(SwipeRefreshLayout swprefrhlyot) {

    	swprefrhlyot.setOnRefreshListener(this);
    	swprefrhlyot.setColorSchemeColors(
				ContextCompat.getColor(getActivity(), R.color.green_speech),
				ContextCompat.getColor(getActivity(), R.color.red),
				ContextCompat.getColor(getActivity(), R.color.blue_belize),
				ContextCompat.getColor(getActivity(), R.color.yellow_sunflwr));
    }

	// Get Patient Id&Name as in List and Only Client Name to ListView
	public void getPatientIdNameSetLstvw() {
		// TODO Auto-generated method stub
		
		try{
			if(currnt_run_onscreen_status){

				lstvw_patientlst.setAdapter(null);
				
//				arrylst_patientnm = new ArrayList<String>();
//				arrylst_patient_id = new ArrayList<String>();
								
				lstvwAdptrIcontext = new Listvw_Adptr_Icontext(getActivity(), ICON_FIRST_CHAR_OFTEXT, arrylst_patientnm_temp);
				
				// Set Patient Name array to ListView
				/*arryadptr_patientlst = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrylst_patientnm){
					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
					    View view = super.getView(position, convertView, parent);
					    TextView lstvw_text = (TextView) view.findViewById(android.R.id.text1);
					    lstvw_text.setTextColor(Color.BLACK);
					    return view;
					  }
				};*/
				lstvw_patientlst.setAdapter(lstvwAdptrIcontext);
			}
		}catch(Exception e){
			General_Fnctns.logManager(ERROR, "Catch  block(GetSetWeb-service Data):" + e.getMessage());
		}
	}
	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

    	if(General_Fnctns.isNetConnected(getActivity())){
    		
    		// Set Current Activity/Fragment is currently running on screen
    		currnt_run_onscreen_status = true;
    		new GetActivePatientFromWebSrve().execute();
    	}else{

    		swiperfrshlyot.setRefreshing(false);
    		swiperfrshlyot_emptyvw.setRefreshing(false);

			// Show SnackBar with given message
			showMySnackBar(getResources().getString(R.string.no_intrnt_cnctn));
    	}
	}
	
	public class GetActivePatientFromWebSrve extends AsyncTask<Void, Void, Void>{

		String websrvc_response;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
//			prgrsbrMain.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			ServiceHandler obj_srvchndlr = new ServiceHandler();
			
			JSONObject jsnobj = new JSONObject();
			try {
				jsnobj.put(FUNCTION_SMALL, FNCTN_ACTIVE_PATIENT_BY_DR_ID);
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

				// Show SnackBar with given message
				showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
			}else{
				try {
					JSONObject websrvc_jsnobj = new JSONObject(websrvc_response);
					String success = websrvc_jsnobj.getString(SUCCESS_SMALL);
					String websrvc_msg = websrvc_jsnobj.getString(MESSAGE_SMALL);
					JSONArray jsonArrayFirst = websrvc_jsnobj.getJSONArray(PATIENTS_SMALL);
					if(success.equals(TRUE_SMALL)){
					
						if(!websrvc_msg.equals("null") && websrvc_msg.equals(PATIENTS_FOUND)){
							
							if(jsonArrayFirst.length() !=0){
								
								arrylst_patient_id = new ArrayList<String>();
								arrylst_patientnm = new ArrayList<String>();
								arrylst_patientnm_temp = new ArrayList<String>();
								sprsarry_patient_images = new SparseArray<String>();
								sprsarry_patient_cashimages = new SparseArray<String>();
								
								for(int i=0; i< jsonArrayFirst.length(); i++){
									
									JSONArray jsonArraySecnd = jsonArrayFirst.getJSONArray(i);
									
									for(int j=0; j<jsonArraySecnd.length(); j++){

										JSONObject jsnobj_dctrprfl = jsonArraySecnd.getJSONObject(j);

										if(jsnobj_dctrprfl.getString(REASSESMENT_SML).equals(FALSE_FRSTCAPS)
												&& jsnobj_dctrprfl.getString(STATUS_SML).equals(ACTIVE_SML)){

											arrylst_patient_id.add(jsnobj_dctrprfl.getString(PT_ID));
											arrylst_patientnm.add(jsnobj_dctrprfl.getString(NAME_FN_SML) + " " + jsnobj_dctrprfl.getString(NAME_LN_SML));
											sprsarry_patient_images.put(jsnobj_dctrprfl.getInt(PT_ID), jsnobj_dctrprfl.getString(IMAGE_SML));
											sprsarry_patient_cashimages.put(jsnobj_dctrprfl.getInt(PT_ID), jsnobj_dctrprfl.getString(CASE_IMAGE_SML));
										}
									}
								}
								
								arrylst_patientnm_temp = arrylst_patientnm;
								// Get Patient id, Name and Set to ListView
								getPatientIdNameSetLstvw();
							}else{

								// Show SnackBar with given message
								showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
							}
														
						}else{

							// Show SnackBar with given message
							showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
						}
					}else if(success.equals(FALSE_SMALL)){
						
						if(!websrvc_msg.equals("null") && websrvc_msg.equals(FUNCTION_NOT_FOUND)){

							// Show SnackBar with given message
							showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
						}else{

							// Show SnackBar with given message
							showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
//			if(prgrsbrMain.getVisibility() == View.VISIBLE) prgrsbrMain.setVisibility(View.GONE);
        	
        	swiperfrshlyot.setRefreshing(false);
        	swiperfrshlyot_emptyvw.setRefreshing(false);
		}
	}	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == ACTION_ZXING_SCANNER) {
			
			if (resultCode == Activity.RESULT_OK) {

				String qrcode = data.getStringExtra(ZBarConstants.SCAN_RESULT);
//				String format = data.getStringExtra("SCAN_RESULT_FORMAT");
				boolean ptIdMatchStatus = false;
				int postn = 0;
				
				for(String strArrylstPtid: arrylst_patient_id){
					
					if(qrcode.trim().equals(strArrylstPtid.trim())){
						
						ptIdMatchStatus = true;
						break;
					}else{
						
						ptIdMatchStatus = false;
					}
					postn++;
				}
				
				if(ptIdMatchStatus){
					
					// Start Company Tab Activity
					Intent intnt_patient = new Intent(getActivity(), NavgnDrawer_Main_Patient.class);
					intnt_patient.putExtra(PT_ID, arrylst_patient_id.get(postn));
					intnt_patient.putExtra(PATIENT_NAME_SML, arrylst_patientnm.get(postn));
					intnt_patient.putExtra(IMAGE_SML, sprsarry_patient_images.get(Integer.parseInt(arrylst_patient_id.get(postn))).toString());
					intnt_patient.putExtra(CASE_IMAGE_SML, sprsarry_patient_cashimages.get(Integer.parseInt(arrylst_patient_id.get(postn))).toString());
					startActivity(intnt_patient);
				}else{

					// Show SnackBar with given message
					showMySnackBar(getResources().getString(R.string.ptnt_not_match));
				}
	        } else if(resultCode == Activity.RESULT_CANCELED) {
				
				// User press back button
	        }
		}
	}

	// Show SnackBar with given message
	private void showMySnackBar(String message) {

		Snackbar.make(cordntrlyotMain, message, Snackbar.LENGTH_LONG).show();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
    	
    	// Get Patient id, Name and Set to ListView
//    	getPatientIdNameSetLstvw();
    	
    	// Change SearchView Focus to FALSE
    	search_view.setFocusable(false);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		// Set Current Activity/Fragment is NOT currently running on screen
		currnt_run_onscreen_status = false;
				
		if (swiperfrshlyot!=null) {
			swiperfrshlyot.setRefreshing(false);
			swiperfrshlyot.destroyDrawingCache();
			swiperfrshlyot.clearAnimation();
	    }
	
		if (swiperfrshlyot_emptyvw!=null) {
			swiperfrshlyot_emptyvw.setRefreshing(false);
			swiperfrshlyot_emptyvw.destroyDrawingCache();
			swiperfrshlyot_emptyvw.clearAnimation();
	    }
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		// Set Current Activity/Fragment is NOT currently running on screen
		currnt_run_onscreen_status = false;
	}	
}
