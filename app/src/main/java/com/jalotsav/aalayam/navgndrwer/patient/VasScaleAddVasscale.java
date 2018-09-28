package com.jalotsav.aalayam.navgndrwer.patient;

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
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jalotsav.aalayam.R;
import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.common.General_Fnctns;
import com.jalotsav.aalayam.service.InternetService;
import com.jalotsav.aalayam.webservice.ServiceHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class VasScaleAddVasscale extends AppCompatActivity implements AalayamConstants {

    CoordinatorLayout mCordntlyot;
    TextView tvPatntName, mTvPrvsVasDate;
    LinearLayout mLnrlyotPrvsVasDate;
    NumberPicker nmbrpckrBefore, nmbrpckrAfter;
    ImageView imgvwBefore, imgvwAfter;
    EditText mEtDailyPymnt;
    RadioGroup mRadioGroupPymntType;
    AppCompatRadioButton mRadiobtnCS, mRadiobtnCH;
    AppCompatButton btnAddscale;
    Map<String, Integer> mapEmojsDrwbleId;

    private String comeFor, slctd_ptnt_id;
    private int vas_id, vasBefore, vasAfter;
    boolean callSendUpdateAfterAsyncStatus = true;
    long mPrvsTimestampVas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_patient_frgmnt_vasscale_addvasscale);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);

        mCordntlyot = (CoordinatorLayout) findViewById(R.id.cordntrlyot_ptnt_frgmnt_vasscale_addvasscale);
        tvPatntName = (TextView) findViewById(R.id.tv_ptnt_frgmnt_vasscale_addvasscale_patntnm);
        mTvPrvsVasDate = (TextView) findViewById(R.id.tv_ptnt_frgmnt_vasscale_addvasscale_prvsvasdate);
        mLnrlyotPrvsVasDate = (LinearLayout) findViewById(R.id.lnrlyot_ptnt_frgmnt_vasscale_addvasscale_prvsdate);
        nmbrpckrBefore = (NumberPicker) findViewById(R.id.nmbrpckr_ptnt_frgmnt_vasscale_addvasscale_before);
        nmbrpckrAfter = (NumberPicker) findViewById(R.id.nmbrpckr_ptnt_frgmnt_vasscale_addvasscale_after);
        imgvwBefore = (ImageView) findViewById(R.id.imgvw_ptnt_frgmnt_vasscale_addvasscale_before);
        imgvwAfter = (ImageView) findViewById(R.id.imgvw_ptnt_frgmnt_vasscale_addvasscale_after);
        mEtDailyPymnt = (EditText) findViewById(R.id.et_ptnt_frgmnt_vasscale_addvasscale_dailypymnt);
        mRadioGroupPymntType = (RadioGroup) findViewById(R.id.radiogrp_ptnt_frgmnt_vasscale_addvasscale_dpymnttype);
        mRadiobtnCS = (AppCompatRadioButton) findViewById(R.id.appcmptradiobtn_ptnt_frgmnt_vasscale_addvasscale_cash);
        mRadiobtnCH = (AppCompatRadioButton) findViewById(R.id.appcmptradiobtn_ptnt_frgmnt_vasscale_addvasscale_cheque);
        btnAddscale = (AppCompatButton) findViewById(R.id.btn_ptnt_frgmnt_vasscale_addvasscale_addscale);

        // Check Internet check service is running or not, If not then Start Service
        if (!General_Fnctns.isServiceRunning(InternetService.class, this))
            startService(new Intent(this, InternetService.class));

        comeFor = getIntent().getStringExtra(COME_FOR);
        slctd_ptnt_id = getIntent().getStringExtra(PT_ID);
        vas_id = getIntent().getIntExtra(VAS_ID, 0);
        vasBefore = getIntent().getIntExtra(BEFORE_SML, 10);
        vasAfter = getIntent().getIntExtra(AFTER_SML, 10);
        mPrvsTimestampVas = getIntent().getLongExtra(VASSCALE_PREVIOUS_TIMESTAMP, 0);

        if (comeFor.equals(ADD_VASSCALE)) {

            actionBar.setTitle(getResources().getString(R.string.add_vasscale));
            btnAddscale.setText(getResources().getString(R.string.add_sml));
            if(mPrvsTimestampVas != 0) { // Visible Previous VasScale Date, if selected

                mLnrlyotPrvsVasDate.setVisibility(View.VISIBLE);
                mTvPrvsVasDate.setText(General_Fnctns.getDateFromTimestamp(mPrvsTimestampVas));
                Snackbar.make(mCordntlyot, R.string.fill_crfly_later_not_edit_msg, Snackbar.LENGTH_LONG).show();
            }
        } else if (comeFor.equals(UPDATE_VASSCALE)) {

            actionBar.setTitle(getResources().getString(R.string.update_vasscale));
            btnAddscale.setText(getResources().getString(R.string.update_sml));

            String dailyPayment = getIntent().getStringExtra(DAILY_PAYMENT);
            String dailyPaymentType = getIntent().getStringExtra(DAILY_PAYMENT_TYPE);
            if (!TextUtils.isEmpty(dailyPayment))
                mEtDailyPymnt.setText(General_Fnctns.get2DigitDecimal(dailyPayment, true));
            if (dailyPaymentType.equals(DAILY_PAYMENT_TYPE_CASH))
                mRadiobtnCS.setChecked(true);
            else if (dailyPaymentType.equals(DAILY_PAYMENT_TYPE_CHEQUE))
                mRadiobtnCH.setChecked(true);
        }

        tvPatntName.setText(getIntent().getStringExtra(PATIENT_NAME_SML));

        mapEmojsDrwbleId = new HashMap<>();
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

                String dailyPayment = mEtDailyPymnt.getText().toString().trim();
                if (TextUtils.isEmpty(dailyPayment) || Double.valueOf(dailyPayment) < 1)
                    mRadiobtnCS.setChecked(true);

                if (comeFor.equals(ADD_VASSCALE))
                    confirmSendAddUpdateScale();
                else if (comeFor.equals(UPDATE_VASSCALE)) {

                    confirmSendAddUpdateScale();

                    /*String currentEntrdDPymnt = General_Fnctns.get2DigitDecimal(mEtDailyPymnt.getText().toString().trim(), true);
                    if (nmbrpckrBefore.getValue() == vasBefore
                            || nmbrpckrAfter.getValue() == vasAfter
                            || currentEntrdDPymnt.equals(dailyPayment)
                            || getSelectedDPaymentType().equals(dailyPymntType))
                        General_Fnctns.showtoastLngthlong(VasScaleAddVasscale.this, "You have to change scale or daily payment for update");
                    else
                        confirmSendAddUpdateScale();*/
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

                String strDailyPymnt = General_Fnctns.get2DigitDecimal(mEtDailyPymnt.getText().toString().trim(), true);
                String slctdDPymntType = getSelectedDPaymentType();

                if (comeFor.equals(ADD_VASSCALE)) {

                    // AsyncTask for Send selected VasSacle to Web-Service
                    new SendAddBeforeAfterVasscaleToWebSrve().execute(
                            String.valueOf(nmbrpckrBefore.getValue()),
                            String.valueOf(nmbrpckrAfter.getValue()),
                            strDailyPymnt, slctdDPymntType);
                } else if (comeFor.equals(UPDATE_VASSCALE)) {

                    if (nmbrpckrBefore.getValue() != vasBefore
                            && nmbrpckrAfter.getValue() != vasAfter) {

                        callSendUpdateAfterAsyncStatus = true;
                        new SendUpdateBeforeVasscaleToWebSrve().execute(String.valueOf(nmbrpckrBefore.getValue()), strDailyPymnt, slctdDPymntType);
                    } else if (nmbrpckrBefore.getValue() != vasBefore
                            && nmbrpckrAfter.getValue() == vasAfter) {

                        callSendUpdateAfterAsyncStatus = false;
                        new SendUpdateBeforeVasscaleToWebSrve().execute(String.valueOf(nmbrpckrBefore.getValue()), strDailyPymnt, slctdDPymntType);
                    } else if (nmbrpckrAfter.getValue() != vasAfter
                            && nmbrpckrBefore.getValue() == vasBefore) {

                        callSendUpdateAfterAsyncStatus = false;
                        new SendUpdateAfterVasscaleToWebSrve().execute(String.valueOf(nmbrpckrAfter.getValue()), strDailyPymnt, slctdDPymntType);
                    } else {

                        callSendUpdateAfterAsyncStatus = true;
                        new SendUpdateBeforeVasscaleToWebSrve().execute(String.valueOf(nmbrpckrBefore.getValue()), strDailyPymnt, slctdDPymntType);
                    }
                }
            }
        });
        dialg.show();
    }

    private String getAddBeforeAfterScaleSetdialogmsg() {
        // TODO Auto-generated method stub

        return getResources().getString(R.string.add_scale_dialogmsg,
                nmbrpckrBefore.getValue(),
                nmbrpckrAfter.getValue(),
                General_Fnctns.get2DigitDecimal(mEtDailyPymnt.getText().toString().trim(), true),
                getSelectedDPaymentType());
    }

    private String getUpdateBeforeAfterScaleSetdialogmsg() {
        // TODO Auto-generated method stub

        return getResources().getString(R.string.update_scale_dialogmsg,
                nmbrpckrBefore.getValue(),
                nmbrpckrAfter.getValue(),
                General_Fnctns.get2DigitDecimal(mEtDailyPymnt.getText().toString().trim(), true),
                getSelectedDPaymentType());
    }

    private String getSelectedDPaymentType() {

        String slctdDPymntType = "";
        int slctdDPymntTypeId = mRadioGroupPymntType.getCheckedRadioButtonId();
        if (slctdDPymntTypeId == mRadiobtnCS.getId())
            slctdDPymntType = DAILY_PAYMENT_TYPE_CASH;
        else if (slctdDPymntTypeId == mRadiobtnCH.getId())
            slctdDPymntType = DAILY_PAYMENT_TYPE_CHEQUE;
        return slctdDPymntType;
    }

    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {
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
                } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
                    Log.e(TAG, "setNumberPickerTextColor: " + e);
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
                } catch (IllegalArgumentException | Resources.NotFoundException | IllegalAccessException e) {
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
                jsnobj.put(DAILY_PAYMENT, params[2]);
                jsnobj.put(DAILY_PAYMENT_TYPE, params[3]);
                jsnobj.put(ADD_DATE_SML, mPrvsTimestampVas != 0 ? mPrvsTimestampVas : General_Fnctns.getcurrentTimestamp());
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

        private ProgressDialog prgrsdlg = new ProgressDialog(VasScaleAddVasscale.this);
        String strDailyPymnt, slctdDPymntType;
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

            strDailyPymnt = params[1];
            slctdDPymntType = params[2];
            ServiceHandler obj_srvchndlr = new ServiceHandler();

            JSONObject jsnobj = new JSONObject();
            try {
                jsnobj.put(FUNCTION_SMALL, FNCTN_UPDATE_VAS_BEFORE);
                jsnobj.put(VAS_ID, vas_id);
                jsnobj.put(BEFORE_SML, params[0]);
                jsnobj.put(DAILY_PAYMENT, strDailyPymnt);
                jsnobj.put(DAILY_PAYMENT_TYPE, slctdDPymntType);
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

                            if (callSendUpdateAfterAsyncStatus) {

                                new SendUpdateAfterVasscaleToWebSrve().execute(String.valueOf(nmbrpckrAfter.getValue()), strDailyPymnt, slctdDPymntType);
                            } else {

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
                jsnobj.put(DAILY_PAYMENT, params[1]);
                jsnobj.put(DAILY_PAYMENT_TYPE, params[2]);
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
