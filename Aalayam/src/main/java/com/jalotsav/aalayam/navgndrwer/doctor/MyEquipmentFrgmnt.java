package com.jalotsav.aalayam.navgndrwer.doctor;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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

import com.jalotsav.aalayam.R;
import com.jalotsav.aalayam.adapters.Listvw_Adptr_PymntHstry;
import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.common.General_Fnctns;
import com.jalotsav.aalayam.common.UserSessionManager;
import com.jalotsav.aalayam.webservice.ServiceHandler;

public class MyEquipmentFrgmnt extends Fragment implements AalayamConstants, SwipeRefreshLayout.OnRefreshListener  {

	static UserSessionManager session;

	CoordinatorLayout cordntrlyotMain;
	SwipeRefreshLayout swiperfrshlyot, swiperfrshlyot_emptyvw;
	ListView lstvw_pymnthstrylst;
	LinearLayout lnrlyotTotalEqpmnt;
	TextView tvTotalEqpmntAmnt;
	ArrayAdapter<String> arryadptr_pymnthstrylst;
	ArrayList<String> arrylst_eqpmnt_qty, arrylst_eqpmnt_title, arrylst_eqpmnt_amnt, arrylst_eqpmnt_adddate, arrylst_eqpmnt_rate, arrylst_eqpmnt_dscrptn;
	ArrayList<Long> arrylst_eqpmnt_adddate_timestamp;
	double totalEqpmntAmnt = 0;
	
	boolean currnt_run_onscreen_status = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View rootView = inflater.inflate(R.layout.lo_doctor_frgmnt_myeqpmnt, container, false);

		// Set Actionbar Title
		((NavgnDrawer_Main_Doctor) getActivity()).onSectionAttached(NAVDRWER_FRGMNT_MYEQUIPMENT);
		
		session = new UserSessionManager(getActivity());
		
		// Set Current Activity/Fragment is currently running on screen
		currnt_run_onscreen_status = true;

		cordntrlyotMain = (CoordinatorLayout) rootView.findViewById(R.id.cordntrlyot_dctr_frgmnt_eqpmnt);
		swiperfrshlyot = (SwipeRefreshLayout)rootView.findViewById(R.id.swiperfrshlyot_dctr_frgmnt_eqpmnt);
		swiperfrshlyot_emptyvw = (SwipeRefreshLayout)rootView.findViewById(R.id.swiperfrshlyot_dctr_frgmnt_myeqpmnt_emptyview);
		lstvw_pymnthstrylst = (ListView)rootView.findViewById(R.id.lstvw_dctr_frgmnt_myeqpmnt_eqpmnt);
		lnrlyotTotalEqpmnt = (LinearLayout)rootView.findViewById(R.id.lnrlyot_dctr_frgmnt_eqpmnt_totaleqpmnt);
		tvTotalEqpmntAmnt = (TextView)rootView.findViewById(R.id.tv_dctr_frgmnt_eqpmnt_totalamnt);
		
		arrylst_eqpmnt_qty = new ArrayList<String>();
		arrylst_eqpmnt_title = new ArrayList<String>();
		arrylst_eqpmnt_amnt = new ArrayList<String>();
		arrylst_eqpmnt_adddate_timestamp = new ArrayList<Long>();
		arrylst_eqpmnt_rate = new ArrayList<String>();
		arrylst_eqpmnt_dscrptn = new ArrayList<String>();
		
		// Set EmptyView to ListView
		lstvw_pymnthstrylst.setEmptyView(swiperfrshlyot_emptyvw);

		// Initialization of SwipeRefreshLayout
		initSwipeRefreshLayout(swiperfrshlyot);
		initSwipeRefreshLayout(swiperfrshlyot_emptyvw);
		
		// Get Payments Details and set to ListView
//		getPymntHstrySetLstvw();

		if(!General_Fnctns.isNetConnected(getActivity())){

			// Show SnackBar with given message
			showMySnackBar(getResources().getString(R.string.no_intrnt_cnctn));
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
				Intent intnt_pymnt = new Intent(getActivity(), MyEquipmentDetails.class);
				intnt_pymnt.putExtra(QTY_SML, arrylst_eqpmnt_qty.get(position));
				intnt_pymnt.putExtra(AMOUNT_SML, arrylst_eqpmnt_amnt.get(position));
				intnt_pymnt.putExtra(TITLE_SML, arrylst_eqpmnt_title.get(position));
				intnt_pymnt.putExtra(ADD_DATE_SML, arrylst_eqpmnt_adddate.get(position));
				intnt_pymnt.putExtra(RATE_SML, arrylst_eqpmnt_rate.get(position));
				intnt_pymnt.putExtra(DESCRIPTION_SML, arrylst_eqpmnt_dscrptn.get(position));
				startActivity(intnt_pymnt);
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
				
//				arrylst_eqpmnt_qty = new ArrayList<String>();
//				arrylst_eqpmnt_title = new ArrayList<String>();
//				arrylst_eqpmnt_amnt = new ArrayList<String>();
//				arrylst_eqpmnt_adddate_timestamp = new ArrayList<Long>();
//				arrylst_eqpmnt_rate = new ArrayList<String>();
//				arrylst_eqpmnt_dscrptn = new ArrayList<String>();
				arrylst_eqpmnt_adddate = new ArrayList<String>();
				
				Collections.sort(arrylst_eqpmnt_adddate_timestamp, Collections.reverseOrder());
				
				for(long str: arrylst_eqpmnt_adddate_timestamp){
					
					pymntAddDate = General_Fnctns.getDateFromTimestamp(str);
					arrylst_eqpmnt_adddate.add(pymntAddDate);
				}
				
				Listvw_Adptr_PymntHstry lstvwAdptrPymnthsrty = new Listvw_Adptr_PymntHstry(getActivity(), COME_FROM_INVENTORY, ICON_FIRST_CHAR_OFTEXT, arrylst_eqpmnt_title, arrylst_eqpmnt_adddate, arrylst_eqpmnt_qty, arrylst_eqpmnt_amnt);
				lstvw_pymnthstrylst.setAdapter(lstvwAdptrPymnthsrty);
				
				// Visible Total Equipment
				lnrlyotTotalEqpmnt.setVisibility(View.VISIBLE);
				tvTotalEqpmntAmnt.setText(getActivity().getResources().getString(R.string.symbl_rupee) + " " + General_Fnctns.get2DecimalDigitOfDouble(totalEqpmntAmnt));
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

			// Show SnackBar with given message
			showMySnackBar(getResources().getString(R.string.no_intrnt_cnctn));
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
				jsnobj.put(FUNCTION_SMALL, FNCTN_SELECT_PAYMENT_EQUIPMENTS_BY_DR_ID);
				jsnobj.put(DR_ID, session.getDrId());
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
					JSONArray jsonArrayFirst = websrvc_jsnobj.getJSONArray(EQUIPMENTS_SML);
					if(success.equals(TRUE_SMALL)){
					
						if(!websrvc_msg.equals("null") && websrvc_msg.equals(DOCTOR_FOUND)){
							
							if(jsonArrayFirst.length() !=0){
								
								arrylst_eqpmnt_qty = new ArrayList<String>();
								arrylst_eqpmnt_title = new ArrayList<String>();
								arrylst_eqpmnt_amnt = new ArrayList<String>();
								arrylst_eqpmnt_adddate_timestamp = new ArrayList<Long>();
								arrylst_eqpmnt_rate = new ArrayList<String>();
								arrylst_eqpmnt_dscrptn = new ArrayList<String>();
								totalEqpmntAmnt = 0;
								
								for(int i=0; i< jsonArrayFirst.length(); i++){
									
									JSONArray jsonArraySecnd = jsonArrayFirst.getJSONArray(i);
									
									for(int j=0; j<jsonArraySecnd.length(); j++){

										JSONObject jsnobj_dctrprfl = jsonArraySecnd.getJSONObject(j);
										
										arrylst_eqpmnt_qty.add(jsnobj_dctrprfl.getString(QTY_SML));
										arrylst_eqpmnt_amnt.add(jsnobj_dctrprfl.getString(AMOUNT_SML));
										totalEqpmntAmnt += jsnobj_dctrprfl.getDouble(AMOUNT_SML);
										arrylst_eqpmnt_title.add(jsnobj_dctrprfl.getString(TITLE_SML));
										arrylst_eqpmnt_adddate_timestamp.add(General_Fnctns.getTimestampFromDate(jsnobj_dctrprfl.getString(EQ_ADD_DATE_SML)));
										arrylst_eqpmnt_rate.add(jsnobj_dctrprfl.getString(RATE_SML));
										arrylst_eqpmnt_dscrptn.add(jsnobj_dctrprfl.getString(DESCRIPTION_SML));
									}
								}

								// Get Payments Details and set to ListView
								getPymntHstrySetLstvw();
							}else{

								// Show SnackBar with given message
								showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
							}
														
						}else{

							// Show SnackBar with given message
							showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
						}
					}else if(success.equals(FALSE_SMALL)){

						// Show SnackBar with given message
						showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
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

	// Show SnackBar with given message
	private void showMySnackBar(String message) {

		Snackbar.make(cordntrlyotMain, message, Snackbar.LENGTH_LONG).show();
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
