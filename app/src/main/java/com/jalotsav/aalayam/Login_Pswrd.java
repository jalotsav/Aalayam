package com.jalotsav.aalayam;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.google.android.gcm.GCMRegistrar;
import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.common.General_Fnctns;
import com.jalotsav.aalayam.common.UserSessionManager;
import com.jalotsav.aalayam.navgndrwer.doctor.NavgnDrawer_Main_Doctor;
import com.jalotsav.aalayam.navgndrwer.doctor.NavgnDrwrDoctor;
import com.jalotsav.aalayam.pushnotifctn.ServerUtilities;
import com.jalotsav.aalayam.pushnotifctn.WakeLocker;
import com.jalotsav.aalayam.webservice.ServiceHandler;

import org.json.JSONException;
import org.json.JSONObject;

import static com.jalotsav.aalayam.pushnotifctn.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.jalotsav.aalayam.pushnotifctn.CommonUtilities.SENDER_ID;

//  Come from package com.jalotsav.aalayam; --> Login_Email
public class Login_Pswrd extends Activity implements AalayamConstants{

	private UserSessionManager session;
	
	private TextView tv_dctr_email, tv_pswrd_lbl, tv_pswrd_valdtn, tv_frgt_pswrd;
	private EditText et_pswrd;
	private Button btn_next;
	public static ProgressBar prgrsbr_websrvc;
	public static String apiDctrEmailid = "";

	// AsyncTask
	AsyncTask<Void, Void, Void> mRegisterTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lo_login_pwd);
		
		session = new UserSessionManager(Login_Pswrd.this);
		
		tv_dctr_email = (TextView)findViewById(R.id.tv_login_pswrd_dctremail);
		tv_pswrd_lbl = (TextView)findViewById(R.id.tv_login_pswrd_pswrdlbl);
		tv_pswrd_valdtn = (TextView)findViewById(R.id.tv_login_pswrd_pswrdvaldtn);
		tv_frgt_pswrd = (TextView)findViewById(R.id.tv_login_pswrd_frgtpswrd);
		et_pswrd = (EditText)findViewById(R.id.et_login_pswrd_dctrpswrd);
		btn_next = (Button)findViewById(R.id.btn_login_pswrd_next);
		prgrsbr_websrvc = (ProgressBar)findViewById(R.id.prgrsbr_login_pswrd_websrvccheckpswrd);
		
		prgrsbr_websrvc.getIndeterminateDrawable().setColorFilter(
				ContextCompat.getColor(this, R.color.trqs_prmry),
	            android.graphics.PorterDuff.Mode.SRC_IN);
		
		tv_dctr_email.setText(getIntent().getStringExtra(DCTR_EMAILID));
		
		et_pswrd.addTextChangedListener(new TextWatcher() {
			
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
					
					tv_pswrd_lbl.setVisibility(View.VISIBLE);
					pswrdvaldn_lbl_invsbl();
				}else{
					
					tv_pswrd_lbl.setVisibility(View.INVISIBLE);
				}
			}
		});
		
		et_pswrd.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionid, KeyEvent event) {
				// TODO Auto-generated method stub
				
				if(actionid == EditorInfo.IME_ACTION_DONE){
					
					if(!General_Fnctns.isNetConnected(getApplicationContext())){

						// Show Validation
						pswrdvaldn_lbl_vsbl(getResources().getString(R.string.no_intrnt_cnctn));
					}else{

						if(et_pswrd.getText().toString().trim().length() == 0){
							
							// Show Validation
							pswrdvaldn_lbl_vsbl(getResources().getString(R.string.plz_entr_pswrd));
						}else{
							
							// Disable click event of Next button
							btn_next.setEnabled(false);
							
							// Hide Validation
							pswrdvaldn_lbl_invsbl();
							
							// Web-Service AsyncTask for check Password
							new CheckLoginPswrdToWebsrvc().execute();
						}
					}
		        }
				return false;
			}
		});
		
		tv_frgt_pswrd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				showAlertDlgbx();
			}
		});
		
		btn_next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(!General_Fnctns.isNetConnected(getApplicationContext())){

					// Show Validation
					pswrdvaldn_lbl_vsbl(getResources().getString(R.string.no_intrnt_cnctn));
				}else{					
					
					if(et_pswrd.getText().toString().trim().length() == 0){
						
						pswrdvaldn_lbl_vsbl(getResources().getString(R.string.plz_entr_pswrd));
					}else{
						
						// Disable click event of Next button
						btn_next.setEnabled(false);
						
						// Hide Validation
						pswrdvaldn_lbl_invsbl();
						
						// Web-Service AsyncTask for check Password
						new CheckLoginPswrdToWebsrvc().execute();
					}
				}
			}
		});
	}

	protected void showAlertDlgbx() {
		// TODO Auto-generated method stub
				
		final Dialog dialg = new Dialog(Login_Pswrd.this, R.style.Jalotsav_AlertDialog_Lollipopdesign);
		dialg.setContentView(R.layout.lo_alertdlg_1btn_lollipopdsgn);
		
		TextView tv_title = (TextView)dialg.findViewById(R.id.tv_alrtdlg_1btn_lollipopdsgn_title);
		TextView tv_msg = (TextView)dialg.findViewById(R.id.tv_alrtdlg_1btn_lollipopdsgn_msg);
		Button btn_positive = (Button)dialg.findViewById(R.id.btn_alrtdlg_1btn_lollipopdsgn_positive);
		
		tv_title.setText(getResources().getString(R.string.frgt_pswrd_qstn));
		tv_msg.setText(getResources().getString(R.string.plz_cntct_your_admin));
		btn_positive.setText(getResources().getString(R.string.ok_caps));
		btn_positive.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// Dismiss dialog
				dialg.dismiss();
			}
		});
		dialg.show();
	}

	private void pswrdvaldn_lbl_vsbl(String str_label) {
		// TODO Auto-generated method stub
		
		tv_pswrd_valdtn.setText(str_label);
		tv_pswrd_valdtn.setVisibility(View.VISIBLE);
	}

	private void pswrdvaldn_lbl_invsbl() {
		// TODO Auto-generated method stub
		
		tv_pswrd_valdtn.setVisibility(View.INVISIBLE);
	}
	
	public class CheckLoginPswrdToWebsrvc extends AsyncTask<Void, Void, Void>{

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
				jsnobj.put(FUNCTION_SMALL, FNCTN_CHECK_DOCTOR_EMAIL_PASSWORD);
				jsnobj.put(DR_ID, session.getDrId());
				jsnobj.put(PASSWORD_SMALL, et_pswrd.getText().toString().trim());
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
				
				pswrdvaldn_lbl_vsbl(getResources().getString(R.string.there_seemsprblm_tryagainlater));
				
				// Hide ProgressBar and Enable Next Button
				hidePrgrsbrEnblNextBtn();
			}else{
				try {
					JSONObject websrvc_jsnobj = new JSONObject(websrvc_response);
					String success = websrvc_jsnobj.getString(SUCCESS_SMALL);
					String websrvc_msg = websrvc_jsnobj.getString(MESSAGE_SMALL);
					if(success.equals(TRUE_SMALL)){
					
						if(!websrvc_msg.equals("null") && websrvc_msg.equals(VALID_PASSWORD)){
							
							apiDctrEmailid = websrvc_jsnobj.getString(EMAIL_SMALL);
							int websrvc_drid = websrvc_jsnobj.getInt(DR_ID);
							
							session.setEmail(apiDctrEmailid);
							session.setDrId(websrvc_drid);
							
							// requestToGCM();

							// Hide ProgressBar
							prgrsbr_websrvc.setVisibility(View.GONE);

							// set Login status as True in SharedPreffrences
							session.setLoginStatusTrue();

							// Intent intntNvgnDrwrDoctr = new Intent(Login_Pswrd.this, NavgnDrawer_Main_Doctor.class);
							Intent intntNvgnDrwrDoctr = new Intent(Login_Pswrd.this, NavgnDrwrDoctor.class);
							intntNvgnDrwrDoctr.putExtra(DCTR_EMAILID, apiDctrEmailid);
							intntNvgnDrwrDoctr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
							startActivity(intntNvgnDrwrDoctr);
						}else{
							
							pswrdvaldn_lbl_vsbl(getResources().getString(R.string.there_seemsprblm_tryagainlater));
							
							// Hide ProgressBar and Enable Next Button
							hidePrgrsbrEnblNextBtn();
						}
					}else if(success.equals(FALSE_SMALL)){
						
						if(!websrvc_msg.equals("null") && websrvc_msg.equals(FUNCTION_NOT_FOUND)){
							
							pswrdvaldn_lbl_vsbl(getResources().getString(R.string.there_seemsprblm_tryagainlater));
						}else if(!websrvc_msg.equals("null") && websrvc_msg.equals(DOCTOR_RECORD_NOT_FOUND)){
						
							pswrdvaldn_lbl_vsbl(getResources().getString(R.string.not_find_alym_acnt));
						}else if(!websrvc_msg.equals("null") && websrvc_msg.equals(INVALID_PASSWORD)){
						
							pswrdvaldn_lbl_vsbl(getResources().getString(R.string.pswrd_notmatch));
						}else{

							pswrdvaldn_lbl_vsbl(getResources().getString(R.string.there_seemsprblm_tryagainlater));	
						}
						
						// Hide ProgressBar and Enable Next Button
						hidePrgrsbrEnblNextBtn();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
					// Hide ProgressBar and Enable Next Button
					hidePrgrsbrEnblNextBtn();
				}
			}
		}

		// Hide ProgressBar and Enable Next Button
		private void hidePrgrsbrEnblNextBtn() {
			// TODO Auto-generated method stub
			
			if(prgrsbr_websrvc.getVisibility() == View.VISIBLE) prgrsbr_websrvc.setVisibility(View.GONE);
						
			btn_next.setEnabled(true);
		}
	}

	// Request to GCM for register device on server for PushNotification
	private void requestToGCM() {
		// TODO Auto-generated method stub
		
		// Show ProgressBar
		prgrsbr_websrvc.setVisibility(View.VISIBLE);
				
		Log.i(TAG, "request to GCM");

		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);

		registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));

		// Get GCM registration id
		final String regId = GCMRegistrar.getRegistrationId(this);

		// Check if regid already presents
		if (regId.equals("")) {

			// Registration is not present, register now with GCM
			GCMRegistrar.register(this, SENDER_ID);
		} else {

			// Device is already registered on GCM
			/*if (GCMRegistrar.isRegisteredOnServer(this)) {

				// Skips registration.
				Toast.makeText(getApplicationContext(),"Already registered with GCM", Toast.LENGTH_LONG).show();
			} else {*/

				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						
						// Register on our server
						// On server creates a new user
						ServerUtilities.register(context, regId);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;

						// Hide ProgressBar
						prgrsbr_websrvc.setVisibility(View.GONE);
						
						// set Login status as True in SharedPreffrences
						session.setLoginStatusTrue();
						
//						Intent intntNvgnDrwrDoctr = new Intent(Login_Pswrd.this, NavgnDrawer_Main_Doctor.class);
						Intent intntNvgnDrwrDoctr = new Intent(Login_Pswrd.this, NavgnDrwrDoctor.class);
						intntNvgnDrwrDoctr.putExtra(DCTR_EMAILID, apiDctrEmailid);
						intntNvgnDrwrDoctr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
						startActivity(intntNvgnDrwrDoctr);
					}
				};
				mRegisterTask.execute(null, null, null);
			//}
		}
	}

	/**
	 * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			
//			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());

			/**
			 * Take appropriate action on this message depending upon your app
			 * requirement For now i am just displaying it on the screen
			 * */

			// Releasing wake lock
			WakeLocker.release();
		}
	};

	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegisterReceiverError", "> " + e.getMessage());
		}
		super.onDestroy();
	}

}
