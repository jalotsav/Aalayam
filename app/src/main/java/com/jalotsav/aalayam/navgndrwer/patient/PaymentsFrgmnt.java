package com.jalotsav.aalayam.navgndrwer.patient;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
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
import com.jalotsav.aalayam.adapters.Listvw_Adptr_PymntHstry;
import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.common.General_Fnctns;
import com.jalotsav.aalayam.common.UserSessionManager;
import com.jalotsav.aalayam.webservice.ServiceHandler;

public class PaymentsFrgmnt extends Fragment implements AalayamConstants, SwipeRefreshLayout.OnRefreshListener {

	static UserSessionManager session;
	
	LinearLayout lnrlyot_usr_ntfy;
	TextView tv_usr_ntfy_msg;
	
	SwipeRefreshLayout swiperfrshlyot, swiperfrshlyot_emptyvw;
	ListView lstvw_pymnthstrylst;
	ArrayAdapter<String> arryadptr_pymnthstrylst;
	ArrayList<String> arrylst_pymnt_id, arrylst_pymntpartclr, arrylst_pymntamnt, arrylst_pymntdate;
	ArrayList<Long> arrylst_pymntdate_timestamp;
	FloatingActionButton fabTotalDueAmnt;
	double totalFees = 0, totalRecvd = 0;

	private String slctd_ptnt_id, slctd_ptnt_name;
	
	boolean currnt_run_onscreen_status = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View rootView = inflater.inflate(R.layout.lo_patient_frgmnt_payments, container, false);

		// Set Actionbar Title
		((NavgnDrawer_Main_Patient) getActivity()).onSectionAttached(NAVDRWER_FRGMNT_PAYMENTS);
		
		session = new UserSessionManager(getActivity());
		
		// Set Current Activity/Fragment is currently running on screen
		currnt_run_onscreen_status = true;

		lnrlyot_usr_ntfy = (LinearLayout)rootView.findViewById(R.id.lnrlyot_user_notify);
		tv_usr_ntfy_msg = (TextView)rootView.findViewById(R.id.tv_user_notify_msg);
		swiperfrshlyot = (SwipeRefreshLayout)rootView.findViewById(R.id.swiperfrshlyot_ptnt_frgmnt_pymntshstry);
		swiperfrshlyot_emptyvw = (SwipeRefreshLayout)rootView.findViewById(R.id.swiperfrshlyot_ptnt_frgmnt_pymntshstry_emptyview);
		lstvw_pymnthstrylst = (ListView)rootView.findViewById(R.id.lstvw_ptnt_frgmnt_pymnts_pymnthstry);
		fabTotalDueAmnt = (FloatingActionButton)rootView.findViewById(R.id.fab_ptnt_frgmnt_pymnts_totaldueamnt);
		
		slctd_ptnt_id = getActivity().getIntent().getStringExtra(PT_ID);
		slctd_ptnt_name = getActivity().getIntent().getStringExtra(PATIENT_NAME_SML);
		
		arrylst_pymnt_id = new ArrayList<String>();
		arrylst_pymntpartclr = new ArrayList<String>();
		arrylst_pymntamnt = new ArrayList<String>();
		arrylst_pymntdate_timestamp = new ArrayList<Long>();
		
		// Set EmptyView to ListView
		lstvw_pymnthstrylst.setEmptyView(swiperfrshlyot_emptyvw);

		// Initialization of SwipeRefreshLayout
		initSwipeRefreshLayout(swiperfrshlyot);
		initSwipeRefreshLayout(swiperfrshlyot_emptyvw);
		
		// Get Payments Details and set to ListView
//		getPymntHstrySetLstvw();

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
		
		lstvw_pymnthstrylst.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				
				// Start Company Tab Activity
				Intent intnt_pymnt = new Intent(getActivity(), PaymentsDetails.class);
				intnt_pymnt.putExtra(PATIENT_NAME_SML, slctd_ptnt_name);
				intnt_pymnt.putExtra(PY_ID, arrylst_pymnt_id.get(position));
				intnt_pymnt.putExtra(AMOUNT_SML, arrylst_pymntamnt.get(position));
				intnt_pymnt.putExtra(PARTICULARS_SML, arrylst_pymntpartclr.get(position));
				intnt_pymnt.putExtra(ADD_DATE_SML, arrylst_pymntdate.get(position));
				startActivity(intnt_pymnt);
			}
		});
		
		fabTotalDueAmnt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				Dialog dlgTotalDueAmnt = new Dialog(getActivity(), R.style.Jalotsav_AlertDialog_Lollipopdesign);
				dlgTotalDueAmnt.setContentView(R.layout.lo_patient_frgmnt_payments_totalduepymnt);
				TextView tvTotalFees = (TextView)dlgTotalDueAmnt.findViewById(R.id.tv_ptnt_frgmnt_pymnts_totaldueamnt_totalpymnt);
				TextView tvRecvdPymnt = (TextView)dlgTotalDueAmnt.findViewById(R.id.tv_ptnt_frgmnt_pymnts_totaldueamnt_recvdpymnt);
				TextView tvDuePymnt = (TextView)dlgTotalDueAmnt.findViewById(R.id.tv_ptnt_frgmnt_pymnts_totaldueamnt_duepymnt);
				
				tvTotalFees.setText(getActivity().getResources().getString(R.string.symbl_rupee) + " " + General_Fnctns.get2DecimalDigitOfDouble(totalFees));
				tvRecvdPymnt.setText(getActivity().getResources().getString(R.string.symbl_rupee) + " " + General_Fnctns.get2DecimalDigitOfDouble(totalRecvd));
				tvDuePymnt.setText(getActivity().getResources().getString(R.string.symbl_rupee) + " " + General_Fnctns.get2DecimalDigitOfDouble(totalFees - totalRecvd));
				
				dlgTotalDueAmnt.show();
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

	// // Get Payments Details and set to ListView
	public void getPymntHstrySetLstvw() {
		// TODO Auto-generated method stub

		try{
			if(currnt_run_onscreen_status){
				
				lstvw_pymnthstrylst.setAdapter(null);
				String pymntAddDate = null;
				
//				arrylst_pymnt_id = new ArrayList<String>();
//				arrylst_pymntpartclr = new ArrayList<String>();
//				arrylst_pymntamnt = new ArrayList<String>();
//				arrylst_pymntdate_timestamp = new ArrayList<Long>();
				arrylst_pymntdate = new ArrayList<String>();
				
				Collections.sort(arrylst_pymntdate_timestamp, Collections.reverseOrder());
				
				for(long str: arrylst_pymntdate_timestamp){
					
					pymntAddDate = General_Fnctns.getDateFromTimestamp(str);
					arrylst_pymntdate.add(pymntAddDate);
				}
				
				Listvw_Adptr_PymntHstry lstvwAdptrPymnthsrty = new Listvw_Adptr_PymntHstry(getActivity(), COME_FROM_PAYMENTS,ICON_FIRST_CHAR_OFTEXT, arrylst_pymntpartclr, arrylst_pymntdate, arrylst_pymnt_id, arrylst_pymntamnt);
				lstvw_pymnthstrylst.setAdapter(lstvwAdptrPymnthsrty);
				
				// Visible TotalDueAmmount FAB
				fabTotalDueAmnt.setVisibility(View.VISIBLE);
			}
		}catch(Exception e){
			General_Fnctns.logManager(ERROR, "Catch  block(GetSetWeb-service Data):" + e.getMessage());
		}
	}
	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

    	if(General_Fnctns.isNetConnected(getActivity())){
    		
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
					JSONArray jsonArrayPatient = websrvc_jsnobj.getJSONArray(PATIENT_SMALL);
					JSONArray jsonArrayFirst = websrvc_jsnobj.getJSONArray(PAYMENT_SML);
					if(success.equals(TRUE_SMALL)){
					
						if(!websrvc_msg.equals("null") && websrvc_msg.equals(PATIENT_FOUND)){
							
							if(jsonArrayPatient.length() !=0){
								
								totalFees = 0;
								
								for(int i=0; i< jsonArrayPatient.length(); i++){
									
									JSONArray jsonArraySecnd = jsonArrayPatient.getJSONArray(i);
									
									for(int j=0; j<jsonArraySecnd.length(); j++){

										JSONObject jsnobj_patient = jsonArraySecnd.getJSONObject(j);
										totalFees += jsnobj_patient.getDouble(FEES_SML);
									}
								}
							}
							
							if(jsonArrayFirst.length() !=0){
								
								arrylst_pymnt_id = new ArrayList<String>();
								arrylst_pymntpartclr = new ArrayList<String>();
								arrylst_pymntamnt = new ArrayList<String>();
								arrylst_pymntdate_timestamp = new ArrayList<Long>();
								totalRecvd = 0;
								
								for(int i=0; i< jsonArrayFirst.length(); i++){
									
									JSONArray jsonArraySecnd = jsonArrayFirst.getJSONArray(i);
									
									for(int j=0; j<jsonArraySecnd.length(); j++){

										JSONObject jsnobj_dctrprfl = jsonArraySecnd.getJSONObject(j);
										
										arrylst_pymnt_id.add(jsnobj_dctrprfl.getString(PY_ID));
										arrylst_pymntamnt.add(jsnobj_dctrprfl.getString(AMOUNT_SML));
										totalRecvd += jsnobj_dctrprfl.getDouble(AMOUNT_SML);
										arrylst_pymntpartclr.add(jsnobj_dctrprfl.getString(PARTICULARS_SML));
										arrylst_pymntdate_timestamp.add(jsnobj_dctrprfl.getLong(ADD_DATE_SML));
									}
								}

								// Get Payments Details and set to ListView
								getPymntHstrySetLstvw();
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
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		// Get Payments Details and set to ListView
//		getPymntHstrySetLstvw();
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
