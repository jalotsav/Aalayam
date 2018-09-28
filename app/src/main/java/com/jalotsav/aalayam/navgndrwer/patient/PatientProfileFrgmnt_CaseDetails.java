package com.jalotsav.aalayam.navgndrwer.patient;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jalotsav.aalayam.R;
import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.common.General_Fnctns;
import com.jalotsav.aalayam.service.InternetService;
import com.jalotsav.aalayam.webservice.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PatientProfileFrgmnt_CaseDetails extends AppCompatActivity implements AalayamConstants{

	CoordinatorLayout cordntrlyotMain;
	ProgressBar prgrsbrMain;
	TextView tv_caseDscrptn, tv_diagns;
	String caseDscrptn, diagns;
	private String slctd_pt_id;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lo_patient_frgmnt_profile_casedtls);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		cordntrlyotMain = (CoordinatorLayout) findViewById(R.id.cordntrlyot_patnt_frgmnt_profile_casedtls);
		prgrsbrMain = (ProgressBar) findViewById(R.id.prgrbr_patnt_frgmnt_profile_casedtls_main);
		tv_caseDscrptn = (TextView) findViewById(R.id.tv_patnt_frgmnt_profile_casedtls_dscrptn);
		tv_diagns = (TextView) findViewById(R.id.tv_patnt_frgmnt_profile_casedtls_diagns);
		
		caseDscrptn = getIntent().getStringExtra(CASE_DESCRIPPTION_SML);
		diagns = getIntent().getStringExtra(DIAGNOSIS_SML);
		slctd_pt_id = getIntent().getStringExtra(PT_ID);

		// Check Internet check service is running or not, If not then Start Service
		/*if(!General_Fnctns.isServiceRunning(InternetService.class, this))
			startService(new Intent(this, InternetService.class));*/

		//  Update TextView text of CaseDescription & Diagnosis
		updateUI();

		if(TextUtils.isEmpty(caseDscrptn)
				|| TextUtils.isEmpty(diagns)){

			if(!General_Fnctns.isNetConnected(this)){

				// Show SnackBar with given message
				showMySnackBar(getResources().getString(R.string.no_intrnt_cnctn));
			}else{

				new GetPatientProfileFromWebSrve().execute();
			}
		}
	}

	private void updateUI() {

		if(!TextUtils.isEmpty(caseDscrptn)) {
			caseDscrptn = Html.fromHtml(caseDscrptn).toString();
			tv_caseDscrptn.setText(Html.fromHtml(caseDscrptn).toString().trim());
		}else
			tv_caseDscrptn.setText(getResources().getString(R.string.no_details_avalbl));

		if(!TextUtils.isEmpty(diagns)){
			diagns = Html.fromHtml(diagns).toString();
			tv_diagns.setText(Html.fromHtml(diagns).toString().trim());
		}else
			tv_diagns.setText(getResources().getString(R.string.no_details_avalbl));
	}

	public class GetPatientProfileFromWebSrve extends AsyncTask<Void, Void, Void> {

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

				// Show SnackBar with given message
				showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
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

										caseDscrptn = jsnobj_patntprfl.getString(CASE_DESCRIPPTION_SML);
										diagns = jsnobj_patntprfl.getString(DIAGNOSIS_SML);

										//  Update TextView text of CaseDescription & Diagnosis
										updateUI();
									}
								}
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

			if(prgrsbrMain.getVisibility() == View.VISIBLE) prgrsbrMain.setVisibility(View.GONE);
		}
	}

	// Show SnackBar with given message
	private void showMySnackBar(String message) {

		Snackbar.make(cordntrlyotMain, message, Snackbar.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.aalayam, menu);
		menu.getItem(0).setVisible(false);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case android.R.id.home:

				onBackPressed();
				break;

			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}
