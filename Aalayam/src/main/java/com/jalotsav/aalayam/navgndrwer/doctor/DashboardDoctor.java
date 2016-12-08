package com.jalotsav.aalayam.navgndrwer.doctor;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.jalotsav.aalayam.R;
import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.common.General_Fnctns;
import com.jalotsav.aalayam.common.UserSessionManager;
import com.jalotsav.aalayam.models.GetPatients.MdlGetPatients;
import com.jalotsav.aalayam.models.GetPatients.MdlGetPatientsData;
import com.jalotsav.aalayam.navgndrwer.patient.NavgnDrawer_Main_Patient;
import com.jalotsav.aalayam.retrofitapihelper.RetroAPIAalayam;
import com.jalotsav.aalayam.webservice.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DashboardDoctor extends Fragment implements AalayamConstants, View.OnClickListener {

    static UserSessionManager session;
    ImageView imgvwActivePtnt, imgvwNews, imgvwQrCode, imgvwEqpmnts, imgvwMyPymnts;
    RelativeLayout rltvlyotPtntCount, rltvlyotNewsCount;
    TextView tvPtntCount, tvNewsCount;
    ProgressBar prgrsbrPtntCount, prgrsbrNewsCount;
    CoordinatorLayout cordntrlyotMain;
    ArrayList<String> arrylst_patientnm, arrylst_patient_id;
    SparseArray<String> sprsarry_patient_images, sprsarry_patient_cashimages;

    boolean patntDataLoadStatus = false;

    // For QRCode scanner
    private final static int ACTION_ZXING_SCANNER = 0x0000c0de;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.lo_doctor_frgmnt_dashboard, container, false);

        // Set Actionbar Title
        ((NavgnDrawer_Main_Doctor) getActivity()).onSectionAttached(NAVDRWER_FRGMNT_DASHBOARD);

        session = new UserSessionManager(getActivity());

        imgvwActivePtnt = (ImageView) rootView.findViewById(R.id.imgvw_dctr_frgmnt_dashbrd_activeptnt);
        imgvwNews = (ImageView) rootView.findViewById(R.id.imgvw_dctr_frgmnt_dashbrd_news);
        imgvwQrCode = (ImageView) rootView.findViewById(R.id.imgvw_dctr_frgmnt_dashbrd_qrcode);
        imgvwEqpmnts = (ImageView) rootView.findViewById(R.id.imgvw_dctr_frgmnt_dashbrd_eqpmnts);
        imgvwMyPymnts = (ImageView) rootView.findViewById(R.id.imgvw_dctr_frgmnt_dashbrd_mypymnts);
        rltvlyotPtntCount = (RelativeLayout) rootView.findViewById(R.id.rltvlyot_dctr_frgmnt_dashbrd_ptntcnt);
        rltvlyotNewsCount = (RelativeLayout) rootView.findViewById(R.id.rltvlyot_dctr_frgmnt_dashbrd_newscnt);
        tvPtntCount = (TextView) rootView.findViewById(R.id.tv_dctr_frgmnt_dashbrd_ptntcnt);
        tvNewsCount = (TextView) rootView.findViewById(R.id.tv_dctr_frgmnt_dashbrd_newscnt);
        prgrsbrPtntCount = (ProgressBar) rootView.findViewById(R.id.prgrsbr_dctr_frgmnt_dashbrd_ptntcnt);
        prgrsbrNewsCount = (ProgressBar) rootView.findViewById(R.id.prgrsbr_dctr_frgmnt_dashbrd_newscnt);
        cordntrlyotMain = (CoordinatorLayout) rootView.findViewById(R.id.cordntrlyot_dctr_frgmnt_dashbrd);

        arrylst_patient_id = new ArrayList<String>();
        arrylst_patientnm = new ArrayList<String>();
        sprsarry_patient_images = new SparseArray<String>();
        sprsarry_patient_cashimages = new SparseArray<String>();

        imgvwActivePtnt.setOnClickListener(this);
        imgvwNews.setOnClickListener(this);
        imgvwQrCode.setOnClickListener(this);
        imgvwEqpmnts.setOnClickListener(this);
        imgvwMyPymnts.setOnClickListener(this);

        if (!General_Fnctns.isNetConnected(getActivity())) {

            // Show SnackBar with given message
            showMySnackBar(getResources().getString(R.string.no_intrnt_cnctn));
        } else {

//            new GetActivePatientFromWebSrve().execute();
            getActivePatientAPI();

//            new GetNewsDetailsWebSrve().execute();
            getNewsDetailsAPI();
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.imgvw_dctr_frgmnt_dashbrd_activeptnt:

                if (session != null) {
                    // Set selected position and IsHome status to Shared preferences
                    session.setSlctdPostnDctrNavdrwr(2);
                    session.setIsHomeDctrNavdrwr(false);
                }

                Fragment fragmentPtnt = new MyPatients();
                if (fragmentPtnt != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container_doctor, fragmentPtnt).commit();
                }
                break;
            case R.id.imgvw_dctr_frgmnt_dashbrd_news:

                if (session != null) {
                    // Set selected position and IsHome status to Shared preferences
                    session.setSlctdPostnDctrNavdrwr(3);
                    session.setIsHomeDctrNavdrwr(false);
                }

                Fragment fragmentNews = new NewsFrgmnt();
                if (fragmentNews != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container_doctor, fragmentNews).commit();
                }

                break;
            case R.id.imgvw_dctr_frgmnt_dashbrd_qrcode:

                scanQrcodeGetResult();
                break;
            case R.id.imgvw_dctr_frgmnt_dashbrd_eqpmnts:

                if (session != null) {
                    // Set selected position and IsHome status to Shared preferences
                    session.setSlctdPostnDctrNavdrwr(4);
                    session.setIsHomeDctrNavdrwr(false);
                }

                Fragment fragmentEpmnts = new MyEquipmentFrgmnt();
                if (fragmentEpmnts != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container_doctor, fragmentEpmnts).commit();
                }
                break;
            case R.id.imgvw_dctr_frgmnt_dashbrd_mypymnts:

                if (session != null) {
                    // Set selected position and IsHome status to Shared preferences
                    session.setSlctdPostnDctrNavdrwr(5);
                    session.setIsHomeDctrNavdrwr(false);
                }

                Fragment fragmentPymnts = new MyPaymentsFrgmnt();
                if (fragmentPymnts != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container_doctor, fragmentPymnts).commit();
                }

                break;

            default:
                break;
        }
    }

    private void scanQrcodeGetResult() {

        if (prgrsbrPtntCount.getVisibility() == View.VISIBLE) {

            // Show SnackBar with given message
            showMySnackBar(getResources().getString(R.string.wait_a_momnt));
        } else if (!patntDataLoadStatus) {

            // Show SnackBar with given message
            showMySnackBar(getResources().getString(R.string.plz_load_ptnts_data_from_server));
        } else if (arrylst_patient_id.size() == 0) {

            // Show SnackBar with given message
            showMySnackBar(getResources().getString(R.string.oops_you_have_no_actvptnt));
        } else {

            Intent intent = new Intent(getActivity(), ZBarScannerActivity.class);
            startActivityForResult(intent, ACTION_ZXING_SCANNER);
        }
    }

    private void getActivePatientAPI() {

        prgrsbrPtntCount.setVisibility(View.VISIBLE);

        Retrofit objRetrofit = new Retrofit.Builder()
                .baseUrl(API_ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JSONObject jsnobj = new JSONObject();
        try {
            jsnobj.put(FUNCTION_SMALL, FNCTN_ACTIVE_PATIENT_BY_DR_ID);
            jsnobj.put(DR_ID, String.valueOf(session.getDrId()));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RetroAPIAalayam retroAPI = objRetrofit.create(RetroAPIAalayam.class);
//		Call<MdlGetPatients> callActivePatients = retroAPI.getActivePatients(jsnobj);
        Call<ResponseBody> callActivePatients = retroAPI.getActivePatients(jsnobj);
        callActivePatients.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

				/*if(response.isSuccessful()) {

					String success = response.body().getSuccess();
					String websrvc_msg = response.body().getMessage();
					Log.i(TAG, "Message: " + websrvc_msg);
					ArrayList<MdlGetPatientsData> arrylstPatientsData = response.body().getFirstArray().get(0).getPatientsData();
					String abc = arrylstPatientsData.get(0).getNameFn();
					Log.i(TAG, "Name: " + abc);

					if(success.equals(TRUE_SMALL)){

						if(!TextUtils.isEmpty(websrvc_msg) && websrvc_msg.equals(PATIENTS_FOUND)){

							if(arrylstPatientsData.size() !=0){

								arrylst_patient_id = new ArrayList<>();
								arrylst_patientnm = new ArrayList<>();
								sprsarry_patient_images = new SparseArray<>();
								sprsarry_patient_cashimages = new SparseArray<>();

								int ptntCount = 0;
								for(int i=0; i< jsonArrayFirst.length(); i++){

									jsonArraySecnd = jsonArrayFirst.getJSONArray(i);

									for(int j=0; j<jsonArraySecnd.length(); j++){

										JSONObject jsnobj_dctrprfl = jsonArraySecnd.getJSONObject(j);

										if(jsnobj_dctrprfl.getString(STATUS_SML).equals(ACTIVE_SML)) {

											ptntCount++;
											arrylst_patient_id.add(jsnobj_dctrprfl.getString(PT_ID));
											arrylst_patientnm.add(jsnobj_dctrprfl.getString(NAME_FN_SML) + " " + jsnobj_dctrprfl.getString(NAME_LN_SML));
											sprsarry_patient_images.put(jsnobj_dctrprfl.getInt(PT_ID), jsnobj_dctrprfl.getString(IMAGE_SML));
											sprsarry_patient_cashimages.put(jsnobj_dctrprfl.getInt(PT_ID), jsnobj_dctrprfl.getString(CASE_IMAGE_SML));
										}
									}
								}

								if(ptntCount > 0){

									rltvlyotPtntCount.setVisibility(View.VISIBLE);
									tvPtntCount.setText(String.valueOf(ptntCount));
								}

								// Set Patient data load status True (Used on QrCode)
								patntDataLoadStatus = true;
							}else
								showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
						}else{

							// Show SnackBar with given message
							showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
						}
					}else
						showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
				} else
					showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));

				if(prgrsbrPtntCount.getVisibility() == View.VISIBLE) prgrsbrPtntCount.setVisibility(View.GONE);*/


                try {
                    String websrvc_response = response.body().string();
                    if (websrvc_response.equals(ACCESS_DENIED)) {

                        // Show SnackBar with given message
                        showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
                    } else {
                        JSONObject websrvc_jsnobj = new JSONObject(websrvc_response);
                        String success = websrvc_jsnobj.getString(SUCCESS_SMALL);
                        String websrvc_msg = websrvc_jsnobj.getString(MESSAGE_SMALL);
                        JSONArray jsonArrayFirst = websrvc_jsnobj.getJSONArray(PATIENTS_SMALL);
                        JSONArray jsonArraySecnd = null;
                        if (success.equals(TRUE_SMALL)) {

                            if (!websrvc_msg.equals("null") && websrvc_msg.equals(PATIENTS_FOUND)) {

                                if (jsonArrayFirst.length() != 0) {

                                    arrylst_patient_id = new ArrayList<String>();
                                    arrylst_patientnm = new ArrayList<String>();
                                    sprsarry_patient_images = new SparseArray<String>();
                                    sprsarry_patient_cashimages = new SparseArray<String>();

                                    int ptntCount = 0;
                                    for (int i = 0; i < jsonArrayFirst.length(); i++) {

                                        jsonArraySecnd = jsonArrayFirst.getJSONArray(i);

                                        for (int j = 0; j < jsonArraySecnd.length(); j++) {

                                            JSONObject jsnobj_dctrprfl = jsonArraySecnd.getJSONObject(j);

                                            if (jsnobj_dctrprfl.getString(STATUS_SML).equals(ACTIVE_SML)) {

                                                ptntCount++;
                                                arrylst_patient_id.add(jsnobj_dctrprfl.getString(PT_ID));
                                                arrylst_patientnm.add(jsnobj_dctrprfl.getString(NAME_FN_SML) + " " + jsnobj_dctrprfl.getString(NAME_LN_SML));
                                                sprsarry_patient_images.put(jsnobj_dctrprfl.getInt(PT_ID), jsnobj_dctrprfl.getString(IMAGE_SML));
                                                sprsarry_patient_cashimages.put(jsnobj_dctrprfl.getInt(PT_ID), jsnobj_dctrprfl.getString(CASE_IMAGE_SML));
                                            }
                                        }
                                    }

                                    if (ptntCount > 0) {

                                        rltvlyotPtntCount.setVisibility(View.VISIBLE);
                                        tvPtntCount.setText(String.valueOf(ptntCount));
                                    }

                                    // Set Patient data load status True (Used on QrCode)
                                    patntDataLoadStatus = true;
                                } else {

                                    // Show SnackBar with given message
                                    showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
                                }

                            } else {

                                // Show SnackBar with given message
                                showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
                            }
                        } else if (success.equals(FALSE_SMALL)) {

                            if (!websrvc_msg.equals("null") && websrvc_msg.equals(FUNCTION_NOT_FOUND)) {

                                // Show SnackBar with given message
                                showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
                            } else {

                                // Show SnackBar with given message
                                showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
                            }
                        }
                    }

                    if (prgrsbrPtntCount.getVisibility() == View.VISIBLE)
                        prgrsbrPtntCount.setVisibility(View.GONE);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                t.printStackTrace();
                if (prgrsbrPtntCount.getVisibility() == View.VISIBLE)
                    prgrsbrPtntCount.setVisibility(View.GONE);
                showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
            }
        });
    }

    /*public class GetActivePatientFromWebSrve extends AsyncTask<Void, Void, Void> {

        String websrvc_response;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            prgrsbrPtntCount.setVisibility(View.VISIBLE);
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

            if (websrvc_response.equals(ACCESS_DENIED)) {

                // Show SnackBar with given message
                showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
            } else {
                try {
                    JSONObject websrvc_jsnobj = new JSONObject(websrvc_response);
                    String success = websrvc_jsnobj.getString(SUCCESS_SMALL);
                    String websrvc_msg = websrvc_jsnobj.getString(MESSAGE_SMALL);
                    JSONArray jsonArrayFirst = websrvc_jsnobj.getJSONArray(PATIENTS_SMALL);
                    JSONArray jsonArraySecnd = null;
                    if (success.equals(TRUE_SMALL)) {

                        if (!websrvc_msg.equals("null") && websrvc_msg.equals(PATIENTS_FOUND)) {

                            if (jsonArrayFirst.length() != 0) {

                                arrylst_patient_id = new ArrayList<String>();
                                arrylst_patientnm = new ArrayList<String>();
                                sprsarry_patient_images = new SparseArray<String>();
                                sprsarry_patient_cashimages = new SparseArray<String>();

                                int ptntCount = 0;
                                for (int i = 0; i < jsonArrayFirst.length(); i++) {

                                    jsonArraySecnd = jsonArrayFirst.getJSONArray(i);

                                    for (int j = 0; j < jsonArraySecnd.length(); j++) {

                                        JSONObject jsnobj_dctrprfl = jsonArraySecnd.getJSONObject(j);

                                        if (jsnobj_dctrprfl.getString(STATUS_SML).equals(ACTIVE_SML)) {

                                            ptntCount++;
                                            arrylst_patient_id.add(jsnobj_dctrprfl.getString(PT_ID));
                                            arrylst_patientnm.add(jsnobj_dctrprfl.getString(NAME_FN_SML) + " " + jsnobj_dctrprfl.getString(NAME_LN_SML));
                                            sprsarry_patient_images.put(jsnobj_dctrprfl.getInt(PT_ID), jsnobj_dctrprfl.getString(IMAGE_SML));
                                            sprsarry_patient_cashimages.put(jsnobj_dctrprfl.getInt(PT_ID), jsnobj_dctrprfl.getString(CASE_IMAGE_SML));
                                        }
                                    }
                                }

                                if (ptntCount > 0) {

                                    rltvlyotPtntCount.setVisibility(View.VISIBLE);
                                    tvPtntCount.setText(String.valueOf(ptntCount));
                                }

                                // Set Patient data load status True (Used on QrCode)
                                patntDataLoadStatus = true;
                            } else {

                                // Show SnackBar with given message
                                showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
                            }

                        } else {

                            // Show SnackBar with given message
                            showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
                        }
                    } else if (success.equals(FALSE_SMALL)) {

                        if (!websrvc_msg.equals("null") && websrvc_msg.equals(FUNCTION_NOT_FOUND)) {

                            // Show SnackBar with given message
                            showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
                        } else {

                            // Show SnackBar with given message
                            showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (prgrsbrPtntCount.getVisibility() == View.VISIBLE)
                prgrsbrPtntCount.setVisibility(View.GONE);
        }
    }*/

    private void getNewsDetailsAPI() {

        prgrsbrPtntCount.setVisibility(View.VISIBLE);

        Retrofit objRetrofit = new Retrofit.Builder()
                .baseUrl(API_ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JSONObject jsnobj = new JSONObject();
        try {
            jsnobj.put(FUNCTION_SMALL, FNCTN_SELECT_NEWS_BY_DR_ID);
            jsnobj.put(DR_ID, String.valueOf(session.getDrId()));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RetroAPIAalayam retroAPI = objRetrofit.create(RetroAPIAalayam.class);
//		Call<MdlGetPatients> callActivePatients = retroAPI.getActivePatients(jsnobj);
        Call<ResponseBody> callActivePatients = retroAPI.getActivePatients(jsnobj);
        callActivePatients.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String websrvc_response = response.body().string();
                    if (websrvc_response.equals(ACCESS_DENIED)) {

                        // Show SnackBar with given message
                        showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
                    } else {
                        try {
                            JSONObject websrvc_jsnobj = new JSONObject(websrvc_response);
                            String success = websrvc_jsnobj.getString(SUCCESS_SMALL);
                            String websrvc_msg = websrvc_jsnobj.getString(MESSAGE_SMALL);
                            JSONArray jsonArrayFirst = websrvc_jsnobj.getJSONArray(NEWS_SML);
                            JSONArray jsonArraySecnd = null;
                            if (success.equals(TRUE_SMALL)) {

                                if (!websrvc_msg.equals("null") && websrvc_msg.equals(NEWS_FOUND)) {

                                    if (jsonArrayFirst.length() != 0) {

                                        int unreadNewsCount = 0;
                                        for (int i = 0; i < jsonArrayFirst.length(); i++) {

                                            jsonArraySecnd = jsonArrayFirst.getJSONArray(i);
                                            for (int j = 0; j < jsonArraySecnd.length(); j++) {

                                                JSONObject jsnobj_dctrprfl = jsonArraySecnd.getJSONObject(j);

                                                if (jsnobj_dctrprfl.getString(STATUS_SML).equals(ACTIVE_SML)) {

                                                    String whoReadDrIds = jsnobj_dctrprfl.getString(WHO_READ_SML);
                                                    if (TextUtils.isEmpty(whoReadDrIds)
                                                            || !whoReadDrIds.contains(String.valueOf(session.getDrId()))) {

                                                        unreadNewsCount++;
                                                    }
                                                }
                                            }
                                        }

                                        if (unreadNewsCount > 0) {

                                            rltvlyotNewsCount.setVisibility(View.VISIBLE);
                                            tvNewsCount.setText(String.valueOf(unreadNewsCount));
                                        }
                                    } else {

                                        // Show SnackBar with given message
                                        showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
                                    }
                                } else {

                                    // Show SnackBar with given message
                                    showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
                                }
                            } else if (success.equals(FALSE_SMALL)) {

                                // Show SnackBar with given message
                                showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    if (prgrsbrNewsCount.getVisibility() == View.VISIBLE)
                        prgrsbrNewsCount.setVisibility(View.GONE);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                t.printStackTrace();
                if (prgrsbrPtntCount.getVisibility() == View.VISIBLE)
                    prgrsbrPtntCount.setVisibility(View.GONE);
                showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
            }
        });
    }

    /*public class GetNewsDetailsWebSrve extends AsyncTask<Void, Void, Void> {

        String websrvc_response;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            prgrsbrNewsCount.setVisibility(View.VISIBLE);
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

            if (websrvc_response.equals(ACCESS_DENIED)) {

                // Show SnackBar with given message
                showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
            } else {
                try {
                    JSONObject websrvc_jsnobj = new JSONObject(websrvc_response);
                    String success = websrvc_jsnobj.getString(SUCCESS_SMALL);
                    String websrvc_msg = websrvc_jsnobj.getString(MESSAGE_SMALL);
                    JSONArray jsonArrayFirst = websrvc_jsnobj.getJSONArray(NEWS_SML);
                    JSONArray jsonArraySecnd = null;
                    if (success.equals(TRUE_SMALL)) {

                        if (!websrvc_msg.equals("null") && websrvc_msg.equals(NEWS_FOUND)) {

                            if (jsonArrayFirst.length() != 0) {

                                int unreadNewsCount = 0;
                                for (int i = 0; i < jsonArrayFirst.length(); i++) {

                                    jsonArraySecnd = jsonArrayFirst.getJSONArray(i);
                                    for (int j = 0; j < jsonArraySecnd.length(); j++) {

                                        JSONObject jsnobj_dctrprfl = jsonArraySecnd.getJSONObject(j);

                                        if (jsnobj_dctrprfl.getString(STATUS_SML).equals(ACTIVE_SML)) {

                                            String whoReadDrIds = jsnobj_dctrprfl.getString(WHO_READ_SML);
                                            if (TextUtils.isEmpty(whoReadDrIds)
                                                    || !whoReadDrIds.contains(String.valueOf(session.getDrId()))) {

                                                unreadNewsCount++;
                                            }
                                        }
                                    }
                                }

                                if (unreadNewsCount > 0) {

                                    rltvlyotNewsCount.setVisibility(View.VISIBLE);
                                    tvNewsCount.setText(String.valueOf(unreadNewsCount));
                                }
                            } else {

                                // Show SnackBar with given message
                                showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
                            }
                        } else {

                            // Show SnackBar with given message
                            showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
                        }
                    } else if (success.equals(FALSE_SMALL)) {

                        // Show SnackBar with given message
                        showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (prgrsbrNewsCount.getVisibility() == View.VISIBLE)
                prgrsbrNewsCount.setVisibility(View.GONE);
        }
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTION_ZXING_SCANNER) {

            if (resultCode == Activity.RESULT_OK) {

                String qrcode = data.getStringExtra(ZBarConstants.SCAN_RESULT);
                boolean ptIdMatchStatus = false;
                int postn = 0;

                for (String strArrylstPtid : arrylst_patient_id) {

                    if (qrcode.trim().equals(strArrylstPtid.trim())) {

                        ptIdMatchStatus = true;
                        break;
                    } else {

                        ptIdMatchStatus = false;
                    }
                    postn++;
                }

                if (ptIdMatchStatus) {

                    // Start Company Tab Activity
                    Intent intnt_patient = new Intent(getActivity(), NavgnDrawer_Main_Patient.class);
                    intnt_patient.putExtra(PT_ID, arrylst_patient_id.get(postn));
                    intnt_patient.putExtra(PATIENT_NAME_SML, arrylst_patientnm.get(postn));
                    intnt_patient.putExtra(IMAGE_SML, sprsarry_patient_images.get(Integer.parseInt(arrylst_patient_id.get(postn))).toString());
                    intnt_patient.putExtra(CASE_IMAGE_SML, sprsarry_patient_cashimages.get(Integer.parseInt(arrylst_patient_id.get(postn))).toString());
                    startActivity(intnt_patient);
                } else {

                    // Show SnackBar with given message
                    Snackbar snackbar = Snackbar
                            .make(cordntrlyotMain, getResources().getString(R.string.ptnt_not_match), Snackbar.LENGTH_LONG)
                            .setAction("Retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    scanQrcodeGetResult();

                                }
                            });
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {

                // User press back button
            }
        }
    }

    // Show SnackBar with given message
    private void showMySnackBar(String message) {

        Snackbar.make(cordntrlyotMain, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        switch (item.getItemId()) {
            case R.id.action_refresh:

                patntDataLoadStatus = false;
                if (!General_Fnctns.isNetConnected(getActivity())) {

                    // Show SnackBar with given message
                    showMySnackBar(getResources().getString(R.string.no_intrnt_cnctn));
                } else {

//                    new GetActivePatientFromWebSrve().execute();
                    getActivePatientAPI();

//                    new GetNewsDetailsWebSrve().execute();
                    getNewsDetailsAPI();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}