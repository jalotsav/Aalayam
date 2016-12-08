package com.jalotsav.aalayam.navgndrwer.patient;

import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jalotsav.aalayam.R;
import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.common.General_Fnctns;
import com.jalotsav.aalayam.common.UserSessionManager;
import com.jalotsav.aalayam.webservice.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by JALOTSAV Dev. on 3/1/16.
 */
public class FeedbackFrgmnt extends Fragment implements AalayamConstants {

    LinearLayout lnrlyotAlreadySubmit;
    ScrollView scrlvwAllFields;
    RatingBar ratbrOfficePrems, ratbrDiagns, ratbrTretmnt, ratbrDoctrAstnc;
    TextView tvRatngOfficePrems, tvRatngDiagns, tvRatngTretmnt, tvRatngDoctrAstnc, tvAlreadySubmit;
    EditText etComments, etSuggedtions;
    ProgressBar prgrsbr_websrvc;
    CoordinatorLayout cordntrlyotMain;
    private String slctd_pt_id;
    MenuItem menuItemSave;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.lo_patient_frgmnt_feedback, container, false);

        // Set Actionbar Title
        ((NavgnDrawer_Main_Patient) getActivity()).onSectionAttached(NAVDRWER_FRGMNT_FEEDBACK);

        slctd_pt_id = getActivity().getIntent().getStringExtra(PT_ID);

        lnrlyotAlreadySubmit = (LinearLayout) rootView.findViewById(R.id.lnrlyot_patnt_frgmnt_feedback_alreadysubmit);
        scrlvwAllFields = (ScrollView) rootView.findViewById(R.id.scrlvw_patnt_frgmnt_feedback_fields);
        ratbrOfficePrems = (RatingBar) rootView.findViewById(R.id.ratngbar_patnt_frgmnt_feedback_officeprems);
        ratbrDiagns = (RatingBar) rootView.findViewById(R.id.ratngbar_patnt_frgmnt_feedback_diagns);
        ratbrTretmnt= (RatingBar) rootView.findViewById(R.id.ratngbar_patnt_frgmnt_feedback_tretmnt);
        ratbrDoctrAstnc = (RatingBar) rootView.findViewById(R.id.ratngbar_patnt_frgmnt_feedback_dctrastnc);
        tvRatngOfficePrems = (TextView) rootView.findViewById(R.id.tv_patnt_frgmnt_feedback_rateofficeprems);
        tvRatngDiagns = (TextView) rootView.findViewById(R.id.tv_patnt_frgmnt_feedback_ratediagns);
        tvRatngTretmnt = (TextView) rootView.findViewById(R.id.tv_patnt_frgmnt_feedback_ratetretmnt);
        tvRatngDoctrAstnc = (TextView) rootView.findViewById(R.id.tv_patnt_frgmnt_feedback_ratedctrastnc);
        tvAlreadySubmit = (TextView) rootView.findViewById(R.id.tv_patnt_frgmnt_feedback_alreadysubmit);
        etComments = (EditText) rootView.findViewById(R.id.et_patnt_frgmnt_feedback_comnts);
        etSuggedtions = (EditText) rootView.findViewById(R.id.et_patnt_frgmnt_feedback_sugtns);
        cordntrlyotMain = (CoordinatorLayout) rootView.findViewById(R.id.cordntrlyot_patnt_frgmnt_feedback);
        prgrsbr_websrvc = (ProgressBar) rootView.findViewById(R.id.prgrbr_patnt_frgmnt_feedback_main);

        setRatingStarColor(ratbrOfficePrems, getRatingStarColorId(5));
        setRatingStarColor(ratbrDiagns, getRatingStarColorId(5));
        setRatingStarColor(ratbrTretmnt, getRatingStarColorId(5));
        setRatingStarColor(ratbrDoctrAstnc, getRatingStarColorId(5));

        tvRatngOfficePrems.setText(getValueFromRating(5));
        tvRatngDiagns.setText(getValueFromRating(5));
        tvRatngTretmnt.setText(getValueFromRating(5));
        tvRatngDoctrAstnc.setText(getValueFromRating(5));

        tvAlreadySubmit.setText(getResources().getString(R.string.already_feedback_onserver));

        // Select Feedback Webservice for check Feedback of selected patient is exist or not on server
        new SelectFeedbackWebSrve().execute();

        ratbrOfficePrems.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                // Get Color id as per given Rating and set to RatingStars
                setRatingStarColor(ratingBar, getRatingStarColorId((int) rating));
                tvRatngOfficePrems.setText(getValueFromRating((int)rating));
            }
        });

        ratbrDiagns.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                // Get Color id as per given Rating and set to RatingStars
                setRatingStarColor(ratingBar, getRatingStarColorId((int) rating));
                tvRatngDiagns.setText(getValueFromRating((int)rating));
            }
        });

        ratbrTretmnt.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                // Get Color id as per given Rating and set to RatingStars
                setRatingStarColor(ratingBar, getRatingStarColorId((int) rating));
                tvRatngTretmnt.setText(getValueFromRating((int)rating));
            }
        });

        ratbrDoctrAstnc.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                // Get Color id as per given Rating and set to RatingStars
                setRatingStarColor(ratingBar, getRatingStarColorId((int) rating));
                tvRatngDoctrAstnc.setText(getValueFromRating((int)rating));
            }
        });

        return rootView;
    }

    private void setRatingStarColor(RatingBar ratingBar, int ratingStarColorId) {

        LayerDrawable startDrwbl = (LayerDrawable) ratingBar.getProgressDrawable();
        startDrwbl.getDrawable(1).setColorFilter(ratingStarColorId, PorterDuff.Mode.SRC_ATOP);
        startDrwbl.getDrawable(2).setColorFilter(ratingStarColorId, PorterDuff.Mode.SRC_ATOP);
    }

    // Get Color id as per given Rating
    private int getRatingStarColorId(int rating) {

        switch (rating){
            case 1:
                return ContextCompat.getColor(getActivity(), R.color.rating_star_1);
            case 2:
                return ContextCompat.getColor(getActivity(), R.color.rating_star_2);
            case 3:
                return ContextCompat.getColor(getActivity(), R.color.rating_star_3);
            case 4:
                return ContextCompat.getColor(getActivity(), R.color.rating_star_4);
            case 5:
                return ContextCompat.getColor(getActivity(), R.color.rating_star_5);
        }
        return ContextCompat.getColor(getActivity(), R.color.rating_star_5);
    }

    private String getValueFromRating(int rating) {

        switch (rating){
            case 0:
                return "";
            case 1:
                return FEDBCK_RATNG_FAIR;
            case 2:
                return FEDBCK_RATNG_AVERAGE;
            case 3:
                return FEDBCK_RATNG_GOOD;
            case 4:
                return FEDBCK_RATNG_VERYGOOD;
            case 5:
                return FEDBCK_RATNG_EXCELLENT;
        }
        return "";
    }

    // Select Feedback Webservice for check Feedback of selected patient is exist or not on server
    public class SelectFeedbackWebSrve extends AsyncTask<Void, Void, Void> {

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

            UserSessionManager session = new UserSessionManager(getActivity());
            JSONObject jsnobj = new JSONObject();
            try {
                jsnobj.put(FUNCTION_SMALL, FNCTN_SELECT_FEEDBACK_BY_DR_PT);
                jsnobj.put(DR_ID, String.valueOf(session.getDrId()));
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
                    if(success.equals(TRUE_SMALL)){

                        // Feedback is already exist on server
                        lnrlyotAlreadySubmit.setVisibility(View.VISIBLE);
                        if(menuItemSave != null)
                            menuItemSave.setVisible(false);
                    }else if(success.equals(FALSE_SMALL)){

                        if(!websrvc_msg.equals("null") && websrvc_msg.equals(FUNCTION_NOT_FOUND)){

                            // Show SnackBar with given message
                            showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
                        } else if(websrvc_msg.equalsIgnoreCase(FEEDBACK_NOT_FOUND_DOT)){

                            lnrlyotAlreadySubmit.setVisibility(View.GONE);
                            scrlvwAllFields.setVisibility(View.VISIBLE);

                            if(menuItemSave != null)
                                menuItemSave.setVisible(true);
                        } else{

                            // Show SnackBar with given message
                            showMySnackBar(getResources().getString(R.string.there_seemsprblm_tryagainlater));
                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if(prgrsbr_websrvc.getVisibility() == View.VISIBLE) prgrsbr_websrvc.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_save, menu);
        menuItemSave = menu.getItem(0);
        menuItemSave.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        switch (item.getItemId()) {
            case R.id.action_save:

                if(!General_Fnctns.isNetConnected(getActivity())){

                    // Show SnackBar with given message
                    showMySnackBar(getResources().getString(R.string.no_intrnt_cnctn));
                }else{

                    // Send Feedback details to server
                    new SendFeedbackToWebSrve().execute(
                            tvRatngOfficePrems.getText().toString(),
                            tvRatngDiagns.getText().toString().trim(),
                            tvRatngTretmnt.getText().toString().trim(),
                            tvRatngDoctrAstnc.getText().toString().trim(),
                            etComments.getText().toString().trim(),
                            etSuggedtions.getText().toString().trim());
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // Show SnackBar with given message
    private void showMySnackBar(String message) {

        Snackbar.make(cordntrlyotMain, message, Snackbar.LENGTH_LONG).show();
    }

    // Send Feedback details to server
    public class SendFeedbackToWebSrve extends AsyncTask<String, Void, Void> {

        String websrvc_response;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            prgrsbr_websrvc.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub

            ServiceHandler obj_srvchndlr = new ServiceHandler();

            UserSessionManager session = new UserSessionManager(getActivity());
            JSONObject jsnobj = new JSONObject();
            try {
                jsnobj.put(FUNCTION_SMALL, FNCTN_INSERT_FEEDBACK);
                jsnobj.put(DR_ID, String.valueOf(session.getDrId()));
                jsnobj.put(PT_ID, slctd_pt_id);
                jsnobj.put(FEEDBACK_OFFICE_PREMIES, params[0]);
                jsnobj.put(FEEDBACK_DIAGNOSIS, params[1]);
                jsnobj.put(FEEDBACK_TREATMENT, params[2]);
                jsnobj.put(FEEDBACK_DR_ASSISTANCE, params[3]);
                jsnobj.put(FEEDBACK_COMMENTS, params[4]);
                jsnobj.put(FEEDBACK_SUGGESTION, params[5]);
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
                    if(success.equals(TRUE_SMALL)){

                        // Show SnackBar with given message
                        showMySnackBar(getResources().getString(R.string.feedback_submit_success));

                        // Hide Menu Item Save
                        if(menuItemSave != null)
                            menuItemSave.setVisible(false);

                        // Update view
                        scrlvwAllFields.setVisibility(View.GONE);
                        lnrlyotAlreadySubmit.setVisibility(View.VISIBLE);
                        tvAlreadySubmit.setText(getResources().getString(R.string.feedback_submit_success));
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

            if(prgrsbr_websrvc.getVisibility() == View.VISIBLE) prgrsbr_websrvc.setVisibility(View.GONE);
        }
    }
}
