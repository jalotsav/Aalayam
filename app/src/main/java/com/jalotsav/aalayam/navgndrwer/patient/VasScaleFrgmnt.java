package com.jalotsav.aalayam.navgndrwer.patient;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bsptechno.libfabbsptechno.FloatingActionButton;
import com.jalotsav.aalayam.R;
import com.jalotsav.aalayam.adapters.Listvw_Adptr_Icontext;
import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.common.General_Fnctns;
import com.jalotsav.aalayam.common.UserSessionManager;
import com.jalotsav.aalayam.webservice.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class VasScaleFrgmnt extends Fragment implements AalayamConstants, SwipeRefreshLayout.OnRefreshListener  {

	static UserSessionManager session;

	LinearLayout lnrlyot_usr_ntfy;
	TextView tv_usr_ntfy_msg;

	SwipeRefreshLayout swiperfrshlyot, swiperfrshlyot_emptyvw;
	ListView lstvw_vasscalelst;
	FloatingActionButton fab_addvasscale;
	ArrayList<String> arrylst_vasscaledate, arrylst_vasdailypymnt, arrylst_vasdailypymnt_type;
	ArrayList<Integer> arrylst_vasscale_id, arrylst_vasbefore, arrylst_vasafter;
	ArrayList<Long> arrylst_vasscaledate_timestamp;

	private String slctd_ptnt_id, slctd_ptnt_name;
	
	boolean currnt_run_onscreen_status = false;
	
	private static final int ADD_VASSCALE_REQUEST = 10;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View rootView = inflater.inflate(R.layout.lo_patient_frgmnt_vasscale, container, false);

		// Set Actionbar Title
		((NavgnDrawer_Main_Patient) getActivity()).onSectionAttached(NAVDRWER_FRGMNT_VASSCALE);
		
		session = new UserSessionManager(getActivity());
		
		// Set Current Activity/Fragment is currently running on screen
		currnt_run_onscreen_status = true;

		lnrlyot_usr_ntfy = (LinearLayout)rootView.findViewById(R.id.lnrlyot_user_notify);
		tv_usr_ntfy_msg = (TextView)rootView.findViewById(R.id.tv_user_notify_msg);
		swiperfrshlyot = (SwipeRefreshLayout)rootView.findViewById(R.id.swiperfrshlyot_ptnt_frgmnt_vasscale);
		swiperfrshlyot_emptyvw = (SwipeRefreshLayout)rootView.findViewById(R.id.swiperfrshlyot_ptnt_frgmnt_vasscale_emptyview);
		lstvw_vasscalelst = (ListView)rootView.findViewById(R.id.lstvw_ptnt_frgmnt_vasscale_vaslist);
		fab_addvasscale = (FloatingActionButton)rootView.findViewById(R.id.fab_ptnt_frgmnt_vasscale_addvasscale);
		
		slctd_ptnt_id = getActivity().getIntent().getStringExtra(PT_ID);
		slctd_ptnt_name = getActivity().getIntent().getStringExtra(PATIENT_NAME_SML);
		
		arrylst_vasscale_id = new ArrayList<>();
		arrylst_vasbefore = new ArrayList<>();
		arrylst_vasafter = new ArrayList<>();
		arrylst_vasscaledate_timestamp = new ArrayList<>();
		arrylst_vasdailypymnt = new ArrayList<>();
		arrylst_vasdailypymnt_type = new ArrayList<>();

		// Set EmptyView to ListView
		lstvw_vasscalelst.setEmptyView(swiperfrshlyot_emptyvw);

		// Initialization of SwipeRefreshLayout
		initSwipeRefreshLayout(swiperfrshlyot);
		initSwipeRefreshLayout(swiperfrshlyot_emptyvw);
		
		// Get VasScale Details as in List and Only VasScale date to ListView
//		getVasscaleIdDateSetLstvw();

		if(!General_Fnctns.isNetConnected(getActivity())){

			// Show UserNotification
			General_Fnctns.showUsrNtfyAutoHide(getActivity(), lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(getActivity(), R.color.red_alizarin), ContextCompat.getColor(getActivity(), R.color.white), getResources().getString(R.string.no_intrnt_cnctn), HIDE_USERNOTIFY_TIME_5K);
		}else{

			swiperfrshlyot.post(new Runnable() {
			    @Override
			    public void run() {

			    	swiperfrshlyot.setRefreshing(true);
			    	swiperfrshlyot_emptyvw.setRefreshing(true);

					new GetPatientDetailsWebSrve().execute();
			    }
			});
		}
		
		lstvw_vasscalelst.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				
				if(arrylst_vasscaledate.get(position).equals(General_Fnctns.getcurrentDate())){
					
					Intent intnt_updatescale = new Intent(getActivity(), VasScaleAddVasscale.class);
					intnt_updatescale.putExtra(COME_FOR, UPDATE_VASSCALE);
					intnt_updatescale.putExtra(PT_ID, slctd_ptnt_id.trim());
					intnt_updatescale.putExtra(PATIENT_NAME_SML, slctd_ptnt_name.trim());
					intnt_updatescale.putExtra(VAS_ID, (int)arrylst_vasscale_id.get(position));
					intnt_updatescale.putExtra(BEFORE_SML, (int)arrylst_vasbefore.get(position));
					intnt_updatescale.putExtra(AFTER_SML, (int)arrylst_vasafter.get(position));
					intnt_updatescale.putExtra(DAILY_PAYMENT, arrylst_vasdailypymnt.get(position));
					intnt_updatescale.putExtra(DAILY_PAYMENT_TYPE, arrylst_vasdailypymnt_type.get(position));
					startActivityForResult(intnt_updatescale, ADD_VASSCALE_REQUEST);
				}else{
					
					Intent intnt_vasscale = new Intent(getActivity(), VasScaleDetails.class);
					intnt_vasscale.putExtra(PATIENT_NAME_SML, slctd_ptnt_name);
					intnt_vasscale.putExtra(BEFORE_SML, arrylst_vasbefore.get(position));
					intnt_vasscale.putExtra(AFTER_SML, arrylst_vasafter.get(position));
					intnt_vasscale.putExtra(ADD_DATE_SML, arrylst_vasscaledate.get(position));
					intnt_vasscale.putExtra(DAILY_PAYMENT, arrylst_vasdailypymnt.get(position));
					intnt_vasscale.putExtra(DAILY_PAYMENT_TYPE, arrylst_vasdailypymnt_type.get(position));
					startActivity(intnt_vasscale);
				}
			}
		});
		
		fab_addvasscale.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intnt_addscale = new Intent(getActivity(), VasScaleAddVasscale.class);
				intnt_addscale.putExtra(COME_FOR, ADD_VASSCALE);
				intnt_addscale.putExtra(PT_ID, slctd_ptnt_id.trim());
				intnt_addscale.putExtra(PATIENT_NAME_SML, slctd_ptnt_name.trim());
				startActivityForResult(intnt_addscale, ADD_VASSCALE_REQUEST);
			}
		});
		
		return rootView;
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

	// Get VasScale Details as in List and Only VasScale date to ListView
	public void getVasscaleIdDateSetLstvw() {
		// TODO Auto-generated method stub

		try{
			if(currnt_run_onscreen_status){
				
				lstvw_vasscalelst.setAdapter(null);
				arrylst_vasscaledate = new ArrayList<>();
				
				Collections.reverse(arrylst_vasscale_id);
				Collections.reverse(arrylst_vasbefore);
				Collections.reverse(arrylst_vasafter);
				Collections.reverse(arrylst_vasscaledate_timestamp);
				Collections.reverse(arrylst_vasdailypymnt);
				Collections.reverse(arrylst_vasdailypymnt_type);

				for(long str: arrylst_vasscaledate_timestamp){
					
					arrylst_vasscaledate.add(General_Fnctns.getDateFromTimestamp(str));
				}
						
				Listvw_Adptr_Icontext lstvwAdptrIcontext = new Listvw_Adptr_Icontext(getActivity(), ICON_FIRST_TWODIGIT_OFTEXT, arrylst_vasscaledate);
				lstvw_vasscalelst.setAdapter(lstvwAdptrIcontext);
				
				if(arrylst_vasscaledate.isEmpty()){
					
					fab_addvasscale.setVisibility(View.VISIBLE);
				}else if(arrylst_vasscaledate.get(0).equals(General_Fnctns.getcurrentDate())){
					
					fab_addvasscale.setVisibility(View.GONE);
				}else{
					
					fab_addvasscale.setVisibility(View.VISIBLE);
				}
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
    		new GetPatientDetailsWebSrve().execute();
    	}else{

    		swiperfrshlyot.setRefreshing(false);
    		swiperfrshlyot_emptyvw.setRefreshing(false);

			// Show UserNotification
			General_Fnctns.showUsrNtfyAutoHide(getActivity(), lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(getActivity(), R.color.red_alizarin), ContextCompat.getColor(getActivity(), R.color.white), getResources().getString(R.string.no_intrnt_cnctn), HIDE_USERNOTIFY_TIME_5K);
    	}
	}
	
	public class GetPatientDetailsWebSrve extends AsyncTask<Void, Void, Void>{

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
				jsnobj.put(FUNCTION_SMALL, FNCTN_SELECT_PATIENT_BY_PT_ID);
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
	    		General_Fnctns.showUsrNtfyAutoHide(getActivity(), lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(getActivity(), R.color.red_alizarin), ContextCompat.getColor(getActivity(), R.color.white), getResources().getString(R.string.there_seemsprblm_tryagainlater), HIDE_USERNOTIFY_TIME_5K);
			}else{
				try {
					JSONObject websrvc_jsnobj = new JSONObject(websrvc_response);
					String success = websrvc_jsnobj.getString(SUCCESS_SMALL);
					String websrvc_msg = websrvc_jsnobj.getString(MESSAGE_SMALL);
					JSONArray jsonArrayFirst = websrvc_jsnobj.getJSONArray(VAS_DETAILS_SML);
					if(success.equals(TRUE_SMALL)){
					
						if(!websrvc_msg.equals("null") && websrvc_msg.equals(PATIENT_FOUND)){
							
							if(jsonArrayFirst.length() !=0){
								
								arrylst_vasscale_id = new ArrayList<>();
								arrylst_vasbefore = new ArrayList<>();
								arrylst_vasafter = new ArrayList<>();
								arrylst_vasscaledate_timestamp = new ArrayList<>();
								arrylst_vasdailypymnt= new ArrayList<>();
								arrylst_vasdailypymnt_type= new ArrayList<>();

								for(int i=0; i< jsonArrayFirst.length(); i++){
									
									JSONArray jsonArraySecnd = jsonArrayFirst.getJSONArray(i);
									
									for(int j=0; j<jsonArraySecnd.length(); j++){

										JSONObject jsnobj_dctrprfl = jsonArraySecnd.getJSONObject(j);
										
										arrylst_vasscale_id.add(jsnobj_dctrprfl.getInt(VAS_ID));
										arrylst_vasbefore.add(jsnobj_dctrprfl.getInt(BEFORE_SML));
										arrylst_vasafter.add(jsnobj_dctrprfl.getInt(AFTER_SML));
										arrylst_vasscaledate_timestamp.add(jsnobj_dctrprfl.getLong(ADD_DATE_SML));
										arrylst_vasdailypymnt.add(jsnobj_dctrprfl.getString(DAILY_PAYMENT));
										arrylst_vasdailypymnt_type.add(jsnobj_dctrprfl.getString(DAILY_PAYMENT_TYPE));
									}
								}
								
								// Get VasScale Details as in List and Only VasScale date to ListView
								getVasscaleIdDateSetLstvw();
							}else{

								// Show UserNotification
					    		General_Fnctns.showUsrNtfyAutoHide(getActivity(), lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(getActivity(), R.color.red_alizarin), ContextCompat.getColor(getActivity(), R.color.white), getResources().getString(R.string.there_seemsprblm_tryagainlater), HIDE_USERNOTIFY_TIME_5K);
							}
														
						}else{

							// Show UserNotification
				    		General_Fnctns.showUsrNtfyAutoHide(getActivity(), lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(getActivity(), R.color.red_alizarin), ContextCompat.getColor(getActivity(), R.color.white), getResources().getString(R.string.there_seemsprblm_tryagainlater), HIDE_USERNOTIFY_TIME_5K);
						}
					}else if(success.equals(FALSE_SMALL)){

						// Show UserNotification
			    		General_Fnctns.showUsrNtfyAutoHide(getActivity(), lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(getActivity(), R.color.red_alizarin), ContextCompat.getColor(getActivity(), R.color.white), getResources().getString(R.string.there_seemsprblm_tryagainlater), HIDE_USERNOTIFY_TIME_5K);
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
		super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
    		
    		// Set Current Activity/Fragment is currently running on screen
    		currnt_run_onscreen_status = true;
        	
            if(requestCode == ADD_VASSCALE_REQUEST){
            	
            	fab_addvasscale.setVisibility(View.GONE);

        		if(!General_Fnctns.isNetConnected(getActivity())){

        			// Show UserNotification
        			General_Fnctns.showUsrNtfyAutoHide(getActivity(), lnrlyot_usr_ntfy, tv_usr_ntfy_msg, ContextCompat.getColor(getActivity(), R.color.red_alizarin), ContextCompat.getColor(getActivity(), R.color.white), getResources().getString(R.string.no_intrnt_cnctn), HIDE_USERNOTIFY_TIME_5K);
        		}else{

        			swiperfrshlyot.post(new Runnable() {
        			    @Override
        			    public void run() {

        			    	swiperfrshlyot.setRefreshing(true);
        			    	swiperfrshlyot_emptyvw.setRefreshing(true);

        					new GetPatientDetailsWebSrve().execute();
        			    }
        			});
        		}
            }
        }
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
    	
    	// Get VasScale Details as in List and Only VasScale date to ListView
//    	getVasscaleIdDateSetLstvw();
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
