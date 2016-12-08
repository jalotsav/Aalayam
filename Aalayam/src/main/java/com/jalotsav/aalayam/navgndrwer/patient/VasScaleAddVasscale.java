package com.jalotsav.aalayam.navgndrwer.patient;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.jalotsav.aalayam.R;
import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.common.General_Fnctns;
import com.jalotsav.aalayam.service.InternetService;
import com.jalotsav.aalayam.webservice.ServiceHandler;

public class VasScaleAddVasscale extends ActionBarActivity implements
		AalayamConstants {

	TextView tvPatntName;
	NumberPicker nmbrpckrBefore, nmbrpckrAfter;
	ImageView imgvwBefore, imgvwAfter;
	Button btnAddscale;
	Map<String, Integer> mapEmojsDrwbleId;

	private String comeFor, slctd_ptnt_id;
	private int vas_id, vasBefore, vasAfter;
	
	boolean callSendUpdateAfterAsyncStatus = true;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lo_patient_frgmnt_vasscale_addvasscale);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setElevation(0);

		tvPatntName = (TextView) findViewById(R.id.tv_ptnt_frgmnt_vasscale_addvasscale_patntnm);
		nmbrpckrBefore = (NumberPicker) findViewById(R.id.nmbrpckr_ptnt_frgmnt_vasscale_addvasscale_before);
		nmbrpckrAfter = (NumberPicker) findViewById(R.id.nmbrpckr_ptnt_frgmnt_vasscale_addvasscale_after);
		imgvwBefore = (ImageView) findViewById(R.id.imgvw_ptnt_frgmnt_vasscale_addvasscale_before);
		imgvwAfter = (ImageView) findViewById(R.id.imgvw_ptnt_frgmnt_vasscale_addvasscale_after);
		btnAddscale = (Button) findViewById(R.id.btn_ptnt_frgmnt_vasscale_addvasscale_addscale);

		// Check Internet check service is running or not, If not then Start Service
		if(!General_Fnctns.isServiceRunning(InternetService.class, this))
			startService(new Intent(this, InternetService.class));

		comeFor = getIntent().getStringExtra(COME_FOR);
		slctd_ptnt_id = getIntent().getStringExtra(PT_ID);
		vas_id = getIntent().getIntExtra(VAS_ID, 0);
		vasBefore = getIntent().getIntExtra(BEFORE_SML, 10);
		vasAfter = getIntent().getIntExtra(AFTER_SML, 10);

		if (comeFor.equals(ADD_VASSCALE)) {
			
			actionBar.setTitle(getResources().getString(R.string.add_vasscale));
			btnAddscale.setText(getResources().getString(R.string.add_scale));
		} else if (comeFor.equals(UPDATE_VASSCALE)) {

			actionBar.setTitle(getResources().getString(R.string.update_vasscale));
			btnAddscale.setText(getResources().getString(R.string.update_scale));
		}

		tvPatntName.setText(getIntent().getStringExtra(PATIENT_NAME_SML));

		mapEmojsDrwbleId = new HashMap<String, Integer>();
		mapEmojsDrwbleId.put(EMOJS_00_ID, R.drawable.emojs_00);
		mapEmojsDrwbleId.put(EMOJS_01_ID, R.drawable.emojs_01);
		mapEmojsDrwbleId.put(EMOJS_02_ID, R.drawable.emojs_02);
		mapEmojsDrwbleId.put(EMOJS_03_ID, R.drawable.emojs_03);
		mapEmojsDrwbleId.put(EMOJS_04_ID, R.drawable.emojs_04);
		mapEmojsDrwbleId.put(EMOJS_05_ID, R.drawable.emojs_05);
		mapEmojsDrwbleId.put(EMOJS_06_ID, R.drawable.emojs_06);
		mapEmojsDrwbleId.put(EMOJS_07_ID, R.drawable.emojs_07);
		mapEmojsDrwbleId.put(EMOJS_08_ID, R.drawable.emojs_08);
		mapEmojsDrwbleId.put(EMOJS_09_ID, R.drawable.emojs_09);
		mapEmojsDrwbleId.put(EMOJS_10_ID, R.drawable.emojs_10);

		nmbrpckrBefore.setMaxValue(10);
		nmbrpckrBefore.setMinValue(0);
		nmbrpckrBefore.setWrapSelectorWheel(true);
		nmbrpckrBefore.setValue(vasBefore);
		setNumberPickerTextColor(nmbrpckrBefore,
				ContextCompat.getColor(this, R.color.red_alizarin));
		setDividerColor(nmbrpckrBefore,
				ContextCompat.getColor(this, R.color.red_alizarin));

		nmbrpckrAfter.setMaxValue(10);
		nmbrpckrAfter.setMinValue(0);
		nmbrpckrAfter.setWrapSelectorWheel(true);
		nmbrpckrAfter.setValue(vasAfter);
		setNumberPickerTextColor(nmbrpckrAfter,
				ContextCompat.getColor(this, R.color.trqs_prmry));
		setDividerColor(nmbrpckrAfter,
				ContextCompat.getColor(this, R.color.trqs_prmry));

		// Set EmoJies to ImageView as per VasScale
		getVasscaleSetEmojs(imgvwBefore, vasBefore);
		getVasscaleSetEmojs(imgvwAfter, vasAfter);

		nmbrpckrBefore
				.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

					@Override
					public void onValueChange(NumberPicker picker, int oldVal,
							int newVal) {
						// TODO Auto-generated method stub

						getVasscaleSetEmojs(imgvwBefore, newVal);
					}
				});

		nmbrpckrAfter
				.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

					@Override
					public void onValueChange(NumberPicker picker, int oldVal,
							int newVal) {
						// TODO Auto-generated method stub

						getVasscaleSetEmojs(imgvwAfter, newVal);
					}
				});

		btnAddscale.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (comeFor.equals(ADD_VASSCALE)) {

					confirmSendAddUpdateScale();	
				} else if (comeFor.equals(UPDATE_VASSCALE)) {

					if(nmbrpckrBefore.getValue() == vasBefore
							&& nmbrpckrAfter.getValue() == vasAfter){
						General_Fnctns.showtoastLngthlong(VasScaleAddVasscale.this, "You have to change scale for update");
					}else{
						
						confirmSendAddUpdateScale();
					}
				}
			}
		});
	}

	// Set EmoJies to ImageView as per VasScale value
	protected void getVasscaleSetEmojs(ImageView imgvw, int newVal) {
		// TODO Auto-generated method stub

		switch (newVal) {
		case 0:

			imgvw.setImageResource(mapEmojsDrwbleId.get(EMOJS_00_ID));
			break;
		case 1:

			imgvw.setImageResource(mapEmojsDrwbleId.get(EMOJS_01_ID));
			break;
		case 2:

			imgvw.setImageResource(mapEmojsDrwbleId.get(EMOJS_02_ID));
			break;
		case 3:

			imgvw.setImageResource(mapEmojsDrwbleId.get(EMOJS_03_ID));
			break;
		case 4:

			imgvw.setImageResource(mapEmojsDrwbleId.get(EMOJS_04_ID));
			break;
		case 5:

			imgvw.setImageResource(mapEmojsDrwbleId.get(EMOJS_05_ID));
			break;
		case 6:

			imgvw.setImageResource(mapEmojsDrwbleId.get(EMOJS_06_ID));
			break;
		case 7:

			imgvw.setImageResource(mapEmojsDrwbleId.get(EMOJS_07_ID));
			break;
		case 8:

			imgvw.setImageResource(mapEmojsDrwbleId.get(EMOJS_08_ID));
			break;
		case 9:

			imgvw.setImageResource(mapEmojsDrwbleId.get(EMOJS_09_ID));
			break;
		case 10:

			imgvw.setImageResource(mapEmojsDrwbleId.get(EMOJS_10_ID));
			break;

		default:

			imgvw.setImageResource(mapEmojsDrwbleId.get(EMOJS_10_ID));
			break;
		}
	}

	protected void confirmSendAddUpdateScale() {
		// TODO Auto-generated method stub
		
		String dialog_msg = null;

		final Dialog dialg = new Dialog(VasScaleAddVasscale.this, R.style.Jalotsav_AlertDialog_Lollipopdesign);
		dialg.setContentView(R.layout.lo_alertdlg_2btn_lollipopdsgn);

		TextView tv_title = (TextView) dialg.findViewById(R.id.tv_alrtdlg_lollipopdsgn_title);
		TextView tv_msg = (TextView) dialg.findViewById(R.id.tv_alrtdlg_lollipopdsgn_msg);
		Button btn_negative = (Button) dialg.findViewById(R.id.btn_alrtdlg_lollipopdsgn_negative);
		Button btn_positive = (Button) dialg.findViewById(R.id.btn_alrtdlg_lollipopdsgn_positive);

		if (comeFor.equals(ADD_VASSCALE)) {

			dialog_msg = getAddBeforeAfterScaleSetdialogmsg();

			tv_title.setText(getResources().getString(R.string.add_scale_questnmark));
			tv_msg.setText(dialog_msg);
			btn_negative.setText(getResources().getString(R.string.cancel_sml));
			btn_positive.setText(getResources().getString(R.string.add_sml));	
		} else if (comeFor.equals(UPDATE_VASSCALE)) {

			dialog_msg = getUpdateBeforeAfterScaleSetdialogmsg();

			tv_title.setText(getResources().getString(R.string.update_scale_questnmark));
			tv_msg.setText(dialog_msg);
			btn_negative.setText(getResources().getString(R.string.cancel_sml));
			btn_positive.setText(getResources().getString(R.string.update_sml));
		}

		btn_negative.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialg.dismiss();
			}
		});
		btn_positive.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// Dismiss dialog
				dialg.dismiss();

				if (comeFor.equals(ADD_VASSCALE)) {

					// AsyncTask for Send selected VasSacle to Web-Service
					new SendAddBeforeAfterVasscaleToWebSrve().execute(
							String.valueOf(nmbrpckrBefore.getValue()),
							String.valueOf(nmbrpckrAfter.getValue()));
				} else if (comeFor.equals(UPDATE_VASSCALE)) {

					if(nmbrpckrBefore.getValue() != vasBefore
							&& nmbrpckrAfter.getValue() != vasAfter){
						
						callSendUpdateAfterAsyncStatus = true;
						
						new SendUpdateBeforeVasscaleToWebSrve().execute(String.valueOf(nmbrpckrBefore.getValue()));
					}else if(nmbrpckrBefore.getValue() != vasBefore
							&& nmbrpckrAfter.getValue() == vasAfter){
						
						callSendUpdateAfterAsyncStatus = false;
						
						new SendUpdateBeforeVasscaleToWebSrve().execute(String.valueOf(nmbrpckrBefore.getValue()));
					}else if(nmbrpckrAfter.getValue() != vasAfter
							&& nmbrpckrBefore.getValue() == vasBefore){
						
						callSendUpdateAfterAsyncStatus = false;
						
						new SendUpdateAfterVasscaleToWebSrve().execute(String.valueOf(nmbrpckrAfter.getValue()));
					}
				}
			}
		});
		dialg.show();
	}

	private String getAddBeforeAfterScaleSetdialogmsg() {
		// TODO Auto-generated method stub

		String dialogMsg = getResources().getString(
				R.string.add_scale_dialogmsg)
				+ "\n"
				+ getResources().getString(R.string.beforescale_coln_sml)
				+ " "
				+ nmbrpckrBefore.getValue()
				+ " "
				+ getResources().getString(R.string.and_all_sml)
				+ "\n"
				+ getResources().getString(R.string.afterscale_coln_sml)
				+ " "
				+ nmbrpckrAfter.getValue() + " ?";

		return dialogMsg;
	}

	private String getUpdateBeforeAfterScaleSetdialogmsg() {
		// TODO Auto-generated method stub

		String dialogMsg = getResources().getString(
				R.string.update_scale_dialogmsg)
				+ "\n"
				+ getResources().getString(R.string.beforescale_coln_sml)
				+ " "
				+ nmbrpckrBefore.getValue()
				+ " "
				+ getResources().getString(R.string.and_all_sml)
				+ "\n"
				+ getResources().getString(R.string.afterscale_coln_sml)
				+ " "
				+ nmbrpckrAfter.getValue() + " ?";

		return dialogMsg;
	}

	public static boolean setNumberPickerTextColor(NumberPicker numberPicker,
			int color) {
		final int count = numberPicker.getChildCount();
		for (int i = 0; i < count; i++) {
			View child = numberPicker.getChildAt(i);
			if (child instanceof EditText) {
				try {
					Field selectorWheelPaintField = numberPicker.getClass()
							.getDeclaredField("mSelectorWheelPaint");
					selectorWheelPaintField.setAccessible(true);
					((Paint) selectorWheelPaintField.get(numberPicker))
							.setColor(color);
					((EditText) child).setTextColor(color);
					numberPicker.invalidate();
					return true;
				} catch (NoSuchFieldException e) {
					Log.w(TAG, "setNumberPickerTextColor: " + e);
				} catch (IllegalAccessException e) {
					Log.w(TAG, "setNumberPickerTextColor: " + e);
				} catch (IllegalArgumentException e) {
					Log.w(TAG, "setNumberPickerTextColor: " + e);
				}
			}
		}
		return false;
	}

	private void setDividerColor(NumberPicker picker, int color) {

		java.lang.reflect.Field[] pickerFields = NumberPicker.class
				.getDeclaredFields();
		for (java.lang.reflect.Field pf : pickerFields) {
			if (pf.getName().equals("mSelectionDivider")) {
				pf.setAccessible(true);
				try {
					ColorDrawable colorDrawable = new ColorDrawable(color);
					pf.set(picker, colorDrawable);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (Resources.NotFoundException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}

	public class SendAddBeforeAfterVasscaleToWebSrve extends AsyncTask<String, Void, Void> {

		private ProgressDialog prgrsdlg = new ProgressDialog(
				VasScaleAddVasscale.this);
		String websrvc_response;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			prgrsdlg.setMessage(getResources()
					.getString(R.string.plz_wait_3dot));
			prgrsdlg.setCancelable(false);
			prgrsdlg.show();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			ServiceHandler obj_srvchndlr = new ServiceHandler();

			JSONObject jsnobj = new JSONObject();
			try {
				jsnobj.put(FUNCTION_SMALL, FNCTN_INSERT_VAS_DETAILS);
				jsnobj.put(PT_ID, slctd_ptnt_id);
				jsnobj.put(BEFORE_SML, params[0]);
				jsnobj.put(AFTER_SML, params[1]);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String json = WEBSRVC_JSONKEY + jsnobj.toString();
			General_Fnctns.logManager(DEBUG, json);
			websrvc_response = obj_srvchndlr.post_makeServiceCall(json);
			General_Fnctns.logManager(INFO, websrvc_response);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (prgrsdlg.isShowing())
				prgrsdlg.dismiss();

			if (websrvc_response.equals(ACCESS_DENIED)) {

				// Show UserNotification
				General_Fnctns.showtoastLngthlong(VasScaleAddVasscale.this,
						getResources().getString(R.string.there_seemsprblm_tryagainlater));
			} else {
				try {
					JSONObject websrvc_jsnobj = new JSONObject(websrvc_response);
					String success = websrvc_jsnobj.getString(SUCCESS_SMALL);
					String websrvc_msg = websrvc_jsnobj.getString(MESSAGE_SMALL);
					if (success.equals(TRUE_SMALL)) {

						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								General_Fnctns.showtoastLngthlong(VasScaleAddVasscale.this,
										getResources().getString(R.string.vasscale_added_sucsfly));
							}
						});

						if (!websrvc_msg.equals("null")
								&& websrvc_msg
										.equals(VAS_DETAILS_ADDED_SUCCESSFULLY)) {

							Intent i = new Intent();
							setResult(Activity.RESULT_OK, i);
							finish();
						} else {

							// Show UserNotification
							General_Fnctns
									.showtoastLngthlong(
											VasScaleAddVasscale.this,
											getResources()
													.getString(
															R.string.there_seemsprblm_tryagainlater));
						}
					} else if (success.equals(FALSE_SMALL)) {

						// Show UserNotification
						General_Fnctns
								.showtoastLngthlong(
										VasScaleAddVasscale.this,
										getResources()
												.getString(
														R.string.there_seemsprblm_tryagainlater));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public class SendUpdateBeforeVasscaleToWebSrve extends AsyncTask<String, Void, Void> {

		private ProgressDialog prgrsdlg = new ProgressDialog(
				VasScaleAddVasscale.this);
		String websrvc_response;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			prgrsdlg.setMessage(getResources()
					.getString(R.string.plz_wait_3dot));
			prgrsdlg.setCancelable(false);
			prgrsdlg.show();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			ServiceHandler obj_srvchndlr = new ServiceHandler();

			JSONObject jsnobj = new JSONObject();
			try {
				jsnobj.put(FUNCTION_SMALL, FNCTN_UPDATE_VAS_BEFORE);
				jsnobj.put(VAS_ID, vas_id);
				jsnobj.put(BEFORE_SML, params[0]);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String json = WEBSRVC_JSONKEY + jsnobj.toString();
			General_Fnctns.logManager(DEBUG, json);
			websrvc_response = obj_srvchndlr.post_makeServiceCall(json);
			General_Fnctns.logManager(INFO, websrvc_response);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (prgrsdlg.isShowing())
				prgrsdlg.dismiss();

			if (websrvc_response.equals(ACCESS_DENIED)) {

				// Show UserNotification
				General_Fnctns.showtoastLngthlong(VasScaleAddVasscale.this,
						getResources().getString(R.string.there_seemsprblm_tryagainlater));
			} else {
				try {
					JSONObject websrvc_jsnobj = new JSONObject(websrvc_response);
					String success = websrvc_jsnobj.getString(SUCCESS_SMALL);
					String websrvc_msg = websrvc_jsnobj.getString(MESSAGE_SMALL);
					if (success.equals(TRUE_SMALL)) {

						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								General_Fnctns.showtoastLngthlong(VasScaleAddVasscale.this,
										getResources().getString(R.string.vasscale_before_updated_sucsfly));
							}
						});

						if (!websrvc_msg.equals("null")
								&& websrvc_msg
										.equals(VAS_DETAILS_UPDATED_SUCCESSFULLY)) {

							if(callSendUpdateAfterAsyncStatus){

								new SendUpdateAfterVasscaleToWebSrve().execute(String.valueOf(nmbrpckrAfter.getValue()));
							}else{
								
								Intent i = new Intent();
								setResult(Activity.RESULT_OK, i);
								finish();
							}
						} else {

							// Show UserNotification
							General_Fnctns
									.showtoastLngthlong(
											VasScaleAddVasscale.this,
											getResources()
													.getString(
															R.string.there_seemsprblm_tryagainlater));
						}
					} else if (success.equals(FALSE_SMALL)) {

						// Show UserNotification
						General_Fnctns
								.showtoastLngthlong(
										VasScaleAddVasscale.this,
										getResources()
												.getString(
														R.string.there_seemsprblm_tryagainlater));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public class SendUpdateAfterVasscaleToWebSrve extends AsyncTask<String, Void, Void> {

		private ProgressDialog prgrsdlg = new ProgressDialog(
				VasScaleAddVasscale.this);
		String websrvc_response;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			prgrsdlg.setMessage(getResources()
					.getString(R.string.plz_wait_3dot));
			prgrsdlg.setCancelable(false);
			prgrsdlg.show();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			ServiceHandler obj_srvchndlr = new ServiceHandler();

			JSONObject jsnobj = new JSONObject();
			try {
				jsnobj.put(FUNCTION_SMALL, FNCTN_UPDATE_VAS_AFTER);
				jsnobj.put(VAS_ID, vas_id);
				jsnobj.put(AFTER_SML, params[0]);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String json = WEBSRVC_JSONKEY + jsnobj.toString();
			General_Fnctns.logManager(DEBUG, json);
			websrvc_response = obj_srvchndlr.post_makeServiceCall(json);
			General_Fnctns.logManager(INFO, websrvc_response);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (prgrsdlg.isShowing())
				prgrsdlg.dismiss();

			if (websrvc_response.equals(ACCESS_DENIED)) {

				// Show UserNotification
				General_Fnctns.showtoastLngthlong(VasScaleAddVasscale.this,
						getResources().getString(R.string.there_seemsprblm_tryagainlater));
			} else {
				try {
					JSONObject websrvc_jsnobj = new JSONObject(websrvc_response);
					String success = websrvc_jsnobj.getString(SUCCESS_SMALL);
					String websrvc_msg = websrvc_jsnobj.getString(MESSAGE_SMALL);
					if (success.equals(TRUE_SMALL)) {

						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								General_Fnctns.showtoastLngthlong(VasScaleAddVasscale.this,
										getResources().getString(R.string.vasscale_after_updated_sucsfly));
							}
						});

						if (!websrvc_msg.equals("null")
								&& websrvc_msg
										.equals(VAS_DETAILS_UPDATED_SUCCESSFULLY)) {

							Intent i = new Intent();
							setResult(Activity.RESULT_OK, i);
							finish();
						} else {

							// Show UserNotification
							General_Fnctns
									.showtoastLngthlong(
											VasScaleAddVasscale.this,
											getResources()
													.getString(
															R.string.there_seemsprblm_tryagainlater));
						}
					} else if (success.equals(FALSE_SMALL)) {

						// Show UserNotification
						General_Fnctns
								.showtoastLngthlong(
										VasScaleAddVasscale.this,
										getResources()
												.getString(
														R.string.there_seemsprblm_tryagainlater));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
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
