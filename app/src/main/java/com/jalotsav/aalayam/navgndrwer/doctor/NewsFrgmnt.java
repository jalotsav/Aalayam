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
import android.widget.ListView;

import com.jalotsav.aalayam.R;
import com.jalotsav.aalayam.adapters.Listvw_Adptr_News;
import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.common.General_Fnctns;
import com.jalotsav.aalayam.common.UserSessionManager;
import com.jalotsav.aalayam.webservice.ServiceHandler;

public class NewsFrgmnt extends Fragment implements AalayamConstants, SwipeRefreshLayout.OnRefreshListener {

	static UserSessionManager session;

	CoordinatorLayout cordntrlyotMain;
	SwipeRefreshLayout swiperfrshlyot, swiperfrshlyot_emptyvw;
	ListView lstvw_newslst;
	ArrayList<String> arrylst_news_id, arrylst_news_title, arrylst_news_dscrptn, arrylst_news_forwho, arrylst_news_adddate;
	ArrayList<Long> arrylst_news_adddate_timestamp;
	ArrayList<Boolean> arrylst_news_readstatus;
	
	boolean currnt_run_onscreen_status = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View rootView = inflater.inflate(R.layout.lo_doctor_frgmnt_news, container, false);

		// Set Actionbar Title
		((NavgnDrwrDoctor) getActivity()).onSectionAttached(NAVDRWER_FRGMNT_NEWS);
		
		session = new UserSessionManager(getActivity());
		
		// Set Current Activity/Fragment is currently running on screen
		currnt_run_onscreen_status = true;

		cordntrlyotMain = (CoordinatorLayout) rootView.findViewById(R.id.cordntrlyot_dctr_frgmnt_news);
		swiperfrshlyot = (SwipeRefreshLayout)rootView.findViewById(R.id.swiperfrshlyot_dctr_frgmnt_news);
		swiperfrshlyot_emptyvw = (SwipeRefreshLayout)rootView.findViewById(R.id.swiperfrshlyot_dctr_frgmnt_news_emptyview);
		lstvw_newslst = (ListView)rootView.findViewById(R.id.lstvw_dctr_frgmnt_news_newslst);
				
		arrylst_news_id = new ArrayList<String>();
		arrylst_news_title = new ArrayList<String>();
		arrylst_news_dscrptn = new ArrayList<String>();
		arrylst_news_forwho = new ArrayList<String>();
		arrylst_news_readstatus = new ArrayList<Boolean>();
		arrylst_news_adddate = new ArrayList<String>();
		arrylst_news_adddate_timestamp = new ArrayList<Long>();
		
		// Set EmptyView to ListView
		lstvw_newslst.setEmptyView(swiperfrshlyot_emptyvw);

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

					new GetNewsDetailsWebSrve().execute();
			    }
			});
		}
		
		lstvw_newslst.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				
				if(arrylst_news_readstatus.get(position) == false){

			    	if(General_Fnctns.isNetConnected(getActivity())){
			    		
			    		// Send ReadStatus true of selected News
			    		new SendNewsReadStatusWebSrve().execute(arrylst_news_id.get(position));

						arrylst_news_readstatus.set(position, true);				
						Listvw_Adptr_News lstvwAdptrNews = new Listvw_Adptr_News(getActivity(), arrylst_news_title, arrylst_news_adddate, arrylst_news_forwho, arrylst_news_readstatus);
						lstvw_newslst.setAdapter(lstvwAdptrNews);
			    	}	
				}
				
				// Start Company Tab Activity
				Intent intnt_newsdtls = new Intent(getActivity(), NewsDetails.class);
				intnt_newsdtls.putExtra(TITLE_SML, arrylst_news_title.get(position));
				intnt_newsdtls.putExtra(DESCRIPTION_SML, arrylst_news_dscrptn.get(position));
				intnt_newsdtls.putExtra(ADD_DATE_SML, arrylst_news_adddate.get(position));
				startActivity(intnt_newsdtls);
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

	// Get News Details and set to ListView
	public void getNewsDetailsSetLstvw() {
		// TODO Auto-generated method stub

		try{
			if(currnt_run_onscreen_status){
				
				lstvw_newslst.setAdapter(null);
				String pymntAddDate = null;

				arrylst_news_adddate = new ArrayList<String>();
				
//				Collections.sort(arrylst_news_adddate_timestamp, Collections.reverseOrder());
				Collections.reverse(arrylst_news_id);
				Collections.reverse(arrylst_news_title);
				Collections.reverse(arrylst_news_dscrptn);
				Collections.reverse(arrylst_news_forwho);
				Collections.reverse(arrylst_news_readstatus);
				Collections.reverse(arrylst_news_adddate_timestamp);
				
				for(long str: arrylst_news_adddate_timestamp){
					
					pymntAddDate = General_Fnctns.getDateFromTimestamp(str);
					arrylst_news_adddate.add(pymntAddDate);
				}				
				
				Listvw_Adptr_News lstvwAdptrNews = new Listvw_Adptr_News(getActivity(), arrylst_news_title, arrylst_news_adddate, arrylst_news_forwho, arrylst_news_readstatus);
				lstvw_newslst.setAdapter(lstvwAdptrNews);
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
    		new GetNewsDetailsWebSrve().execute();
    	}else{

    		swiperfrshlyot.setRefreshing(false);
    		swiperfrshlyot_emptyvw.setRefreshing(false);

			// Show SnackBar with given message
			showMySnackBar(getResources().getString(R.string.no_intrnt_cnctn));
    	}
	}
	
	public class GetNewsDetailsWebSrve extends AsyncTask<Void, Void, Void>{

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
				jsnobj.put(FUNCTION_SMALL, FNCTN_SELECT_NEWS_BY_DR_ID);
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
				showMySnackBar(getResources().getString(R.string.no_intrnt_cnctn));
			}else{
				try {
					JSONObject websrvc_jsnobj = new JSONObject(websrvc_response);
					String success = websrvc_jsnobj.getString(SUCCESS_SMALL);
					String websrvc_msg = websrvc_jsnobj.getString(MESSAGE_SMALL);
					JSONArray jsonArrayFirst = websrvc_jsnobj.getJSONArray(NEWS_SML);
					if(success.equals(TRUE_SMALL)){
					
						if(!websrvc_msg.equals("null") && websrvc_msg.equals(NEWS_FOUND)){
							
							if(jsonArrayFirst.length() !=0){
								
								arrylst_news_id = new ArrayList<String>();
								arrylst_news_title = new ArrayList<String>();
								arrylst_news_dscrptn = new ArrayList<String>();
								arrylst_news_forwho = new ArrayList<String>();
								arrylst_news_readstatus = new ArrayList<Boolean>();
								arrylst_news_adddate_timestamp = new ArrayList<Long>();
								
								for(int i=0; i< jsonArrayFirst.length(); i++){
									
									JSONArray jsonArraySecnd = jsonArrayFirst.getJSONArray(i);
									
									for(int j=0; j<jsonArraySecnd.length(); j++){

										JSONObject jsnobj_dctrprfl = jsonArraySecnd.getJSONObject(j);
										
										arrylst_news_id.add(jsnobj_dctrprfl.getString(NEWS_ID));
										arrylst_news_title.add(jsnobj_dctrprfl.getString(TITLE_SML));
										arrylst_news_dscrptn.add(jsnobj_dctrprfl.getString(DESCRIPTION_SML));
										
										// GetSet This News for Who (only this doctor or ALL) 
										String dr_id = jsnobj_dctrprfl.getString(DR_ID); 
										if(dr_id.equals("") || dr_id.length() ==0){
											
											arrylst_news_forwho.add("ALL");
										}else{

											if(dr_id.substring(0,1).equals(",")) dr_id = dr_id.substring(1);
											String forwho = "";
											ArrayList<String> arrylst = General_Fnctns.strCommaToArray(dr_id);
											for(String s: arrylst){
												
												if(s.trim().equals(String.valueOf(session.getDrId()))) {
													
													forwho = "ME";
													break;
												}
												else forwho =  "ALL";			
											}
											
											arrylst_news_forwho.add(forwho);
										}
										
										// GETSET News is already Read or not
										String whoRead = jsnobj_dctrprfl.getString(WHO_READ_SML); 
										if(whoRead.equals("") || whoRead.length() ==0){
											arrylst_news_readstatus.add(false);
										}else{

											if(whoRead.substring(0,1).equals(",")) whoRead = whoRead.substring(1);
											boolean readStaus = false;
											ArrayList<String> arrylst_strtoarray = General_Fnctns.strCommaToArray(whoRead);
											for(String str: arrylst_strtoarray){
												
												if(str.trim().equals(String.valueOf(session.getDrId()))) {
													
													readStaus = true;
													break;
												}
												else readStaus =  false;			
											}
											
											arrylst_news_readstatus.add(readStaus);
										}
										
										arrylst_news_adddate_timestamp.add(jsnobj_dctrprfl.getLong(ADD_DATE_SML));
									}
								}

								// Get News Details and set to ListView
								getNewsDetailsSetLstvw();
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
	
	// Send ReadStatus true of selected News
	public class SendNewsReadStatusWebSrve extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String slctdNewsid = params[0];
			
			ServiceHandler obj_srvchndlr = new ServiceHandler();
			
			JSONObject jsnobj = new JSONObject();
			try {
				jsnobj.put(FUNCTION_SMALL, FNCTN_UPDATE_NEWS_WHO_READ);
				jsnobj.put(NEWS_ID, slctdNewsid);
				jsnobj.put(WHO_READ_SML, session.getDrId());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String json = WEBSRVC_JSONKEY + jsnobj.toString();
			General_Fnctns.logManager(INFO, json);
			String websrvc_response = obj_srvchndlr.post_makeServiceCall(json);
			General_Fnctns.logManager(DEBUG, websrvc_response);
			
			return websrvc_response;
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
