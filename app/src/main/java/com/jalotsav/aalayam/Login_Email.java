package com.jalotsav.aalayam;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.common.General_Fnctns;
import com.jalotsav.aalayam.common.UserSessionManager;
import com.jalotsav.aalayam.webservice.ServiceHandler;

// Come from package com.jalotsav.aalayam.navgndrwer.doctor; --> NavgnDrawer_Main_Doctor 
public class Login_Email extends Activity implements AalayamConstants{

	private TextView tv_email_lbl, tv_email_valdtn;
	private EditText et_email;
	private Button btn_next;
	private ProgressBar prgrsbr_websrvc;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lo_login_email);
		
		tv_email_lbl = (TextView)findViewById(R.id.tv_login_email_emaillbl);
		tv_email_valdtn = (TextView)findViewById(R.id.tv_login_email_emailvaldtn);
		et_email = (EditText)findViewById(R.id.et_login_email_dctremail);
		btn_next = (Button)findViewById(R.id.btn_login_email_next);
		prgrsbr_websrvc = (ProgressBar)findViewById(R.id.prgrsbr_login_email_websrvccheckemail);
		
		prgrsbr_websrvc.getIndeterminateDrawable().setColorFilter(
				ContextCompat.getColor(this, R.color.trqs_prmry),
	            android.graphics.PorterDuff.Mode.SRC_IN);
		
		et_email.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
				if(s.toString().trim().length() > 0){
					
					tv_email_lbl.setVisibility(View.VISIBLE);
					emailvaldn_lbl_invsbl();
				}else{
					
					tv_email_lbl.setVisibility(View.INVISIBLE);
				}
			}
		});
		
		et_email.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionid, KeyEvent event) {
				// TODO Auto-generated method stub
				
				if(actionid == EditorInfo.IME_ACTION_DONE){
					
					if(!General_Fnctns.isNetConnected(getApplicationContext())){

						// Show Validation
	            		emailvaldn_lbl_vsbl(getResources().getString(R.string.no_intrnt_cnctn));
					}else{

						if(et_email.getText().toString().trim().length() == 0){
							
							// Show Validation
							emailvaldn_lbl_vsbl(getResources().getString(R.string.plz_entr_email));
						}else if(!General_Fnctns.isvalidEmailaddrsFrmt(et_email.getText().toString())){
							
							// Show UserNotification
							emailvaldn_lbl_vsbl(getResources().getString(R.string.invld_email_adrs_frmt));
						}else{
							
							// Disable click event of Next button
							btn_next.setEnabled(false);
							
							// Hide Validation
							emailvaldn_lbl_invsbl();
							
							// Web-Service AsyncTask for check Email
							new CheckLoginEmailToWebsrvc().execute();
						}
					}
		        }
				return false;
			}
		});
		
		btn_next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(!General_Fnctns.isNetConnected(getApplicationContext())){

					// Show Validation
            		emailvaldn_lbl_vsbl(getResources().getString(R.string.no_intrnt_cnctn));
				}else{
					
					if(et_email.getText().toString().trim().length() == 0){
						
						emailvaldn_lbl_vsbl(getResources().getString(R.string.plz_entr_email));
					}else if(!General_Fnctns.isvalidEmailaddrsFrmt(et_email.getText().toString().trim())){
						
						emailvaldn_lbl_vsbl(getResources().getString(R.string.invld_email_adrs_frmt));
					}
					else{
						
						// Disable click event of Next button
						btn_next.setEnabled(false);
						
						// Hide Validation
						emailvaldn_lbl_invsbl();
						
						// Web-Service AsyncTask for check Email
						new CheckLoginEmailToWebsrvc().execute();
					}
				}
			}
		});
	}

	private void emailvaldn_lbl_vsbl(String str_label) {
		// TODO Auto-generated method stub
		
		tv_email_valdtn.setText(str_label);
		tv_email_valdtn.setVisibility(View.VISIBLE);
	}

	private void emailvaldn_lbl_invsbl() {
		// TODO Auto-generated method stub
		
		tv_email_valdtn.setVisibility(View.INVISIBLE);
	}
	
	public class CheckLoginEmailToWebsrvc extends AsyncTask<Void, Void, Void>{

		String websrvc_response;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			prgrsbr_websrvc.setVisibility(View.VISIBLE);			
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			ServiceHandler obj_srvchndlr = new ServiceHandler();
			
			JSONObject jsnobj = new JSONObject();
			try {
				jsnobj.put(FUNCTION_SMALL, FNCTN_CHECK_DOCTOR_EMAIL_LOGIN);
				jsnobj.put(EMAIL_SMALL, et_email.getText().toString().trim());
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
				
				emailvaldn_lbl_vsbl(getResources().getString(R.string.there_seemsprblm_tryagainlater));
			}else{
				try {
					JSONObject websrvc_jsnobj = new JSONObject(websrvc_response);
					String success = websrvc_jsnobj.getString(SUCCESS_SMALL);
					String websrvc_msg = websrvc_jsnobj.getString(MESSAGE_SMALL);
					if(success.equals(TRUE_SMALL)){
					
						if(!websrvc_msg.equals("null") && websrvc_msg.equals(DOCTOR_RECORD_FOUND)){
							
							String websrvc_email = websrvc_jsnobj.getString(EMAIL_SMALL);
							int websrvc_drid = websrvc_jsnobj.getInt(DR_ID);
							
							UserSessionManager session = new UserSessionManager(Login_Email.this);
							session.setDrId(websrvc_drid);
							
							Intent intnt_pswrd = new Intent(Login_Email.this, Login_Pswrd.class);
							intnt_pswrd.putExtra(DCTR_EMAILID, websrvc_email);
							startActivity(intnt_pswrd);							
						}else{
							
							emailvaldn_lbl_vsbl(getResources().getString(R.string.there_seemsprblm_tryagainlater));
						}
					}else if(success.equals(FALSE_SMALL)){
						
						if(!websrvc_msg.equals("null") && websrvc_msg.equals(FUNCTION_NOT_FOUND)){
							
							emailvaldn_lbl_vsbl(getResources().getString(R.string.there_seemsprblm_tryagainlater));
						}else if(!websrvc_msg.equals("null") && websrvc_msg.equals(DOCTOR_RECORD_NOT_FOUND)){
						
							emailvaldn_lbl_vsbl(getResources().getString(R.string.not_find_alym_acnt));
						}else{

							emailvaldn_lbl_vsbl(getResources().getString(R.string.there_seemsprblm_tryagainlater));	
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(prgrsbr_websrvc.getVisibility() == View.VISIBLE) prgrsbr_websrvc.setVisibility(View.GONE);
						
			btn_next.setEnabled(true);
		}
	}
}
