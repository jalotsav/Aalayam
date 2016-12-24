package com.jalotsav.aalayam.navgndrwer.doctor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jalotsav.aalayam.R;
import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.common.General_Fnctns;
import com.jalotsav.aalayam.common.UserSessionManager;
import com.jalotsav.aalayam.service.InternetService;
import com.jalotsav.aalayam.webservice.ServiceHandler;
import com.jalotsav.imageviewshape.RoundedImageView;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by Jalotsav on 12/22/2016.
 */

public class NavgnDrwrDoctor extends AppCompatActivity implements AalayamConstants {

    static UserSessionManager session;
    Toolbar mToolbar;
    Drawer mDrawer;
    PrimaryDrawerItem mPrmryDrwrItemDashbrd, mPrmryDrwrItemMyPtnts, mPrmryDrwrItemNews, mPrmryDrwrItemMyEqpmnt, mPrmryDrwrItemMyPymnt;
    SecondaryDrawerItem mScndryDrwrItemDctrProfl;
    CharSequence mToolbarTitle;
    RoundedImageView mImgvwHeaderImage;
    TextView mTvHeaderName, mTvHeaderEmail;

    String mDoctrName, mDoctrEmail, mProfileImageName;
    Bitmap mDoctrImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_nvgtndrwr_activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_nvgtndrwr_actvtymain);
        setSupportActionBar(mToolbar);
        mToolbarTitle = mToolbar.getTitle();

        session = new UserSessionManager(this);
        // Set selected position to Shared preferences
        session.setSlctdPostnDctrNavdrwr(1);

        if(session.checkLogin()) {

            // Check Internet check service is running or not, If not then Start Service
            if(General_Fnctns.isServiceRunning(InternetService.class, this))
                Log.i(TAG, "InternetCheck Service already Running");
            else {
                Log.e(TAG, "InternetCheck Service NOT Running, Start Service");
                startService(new Intent(this, InternetService.class));
            }

            mPrmryDrwrItemDashbrd = new PrimaryDrawerItem().withName(getString(R.string.dashboard_sml))
                    .withIcon(GoogleMaterial.Icon.gmd_dashboard);
            mPrmryDrwrItemMyPtnts = new PrimaryDrawerItem().withName(getString(R.string.my_ptnts_sml))
                /*.withBadge("0")*/.withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700))
                    .withIdentifier(DRWRITEM_IDENTIFIER_PATIENTS)
                    .withIcon(GoogleMaterial.Icon.gmd_accessible);
            mPrmryDrwrItemNews = new PrimaryDrawerItem().withName(getString(R.string.news_sml))
                    .withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700))
                    .withIdentifier(DRWRITEM_IDENTIFIER_NEWS)
                    .withIcon(GoogleMaterial.Icon.gmd_event_note);
            mPrmryDrwrItemMyEqpmnt = new PrimaryDrawerItem().withName(getString(R.string.my_eqpmnt_sml))
                    .withIcon(GoogleMaterial.Icon.gmd_storage);
            mPrmryDrwrItemMyPymnt = new PrimaryDrawerItem().withName(getString(R.string.my_pymnts_sml))
                    .withIcon(GoogleMaterial.Icon.gmd_account_balance_wallet);
            mScndryDrwrItemDctrProfl = new SecondaryDrawerItem().withName(getString(R.string.my_profile_sml)).withIcon(GoogleMaterial.Icon.gmd_account_box);

            mDrawer = new DrawerBuilder()
                    .withActivity(this)
                    .withToolbar(mToolbar)
                    .withHeader(R.layout.lo_nvgtndrwr_header)
                    .withStickyFooter(R.layout.lo_nvgtndrwr_footer)
                    .addDrawerItems(
                            mPrmryDrwrItemDashbrd,
                            mPrmryDrwrItemMyPtnts,
                            mPrmryDrwrItemNews,
                            mPrmryDrwrItemMyEqpmnt,
                            mPrmryDrwrItemMyPymnt,
                            new DividerDrawerItem(),
                            mScndryDrwrItemDctrProfl)
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            session.setSlctdPostnDctrNavdrwr(position);
                            switch (position) {
                                case 1:
                                    session.setIsHomeDctrNavdrwr(true);
                                    ft.replace(R.id.framecntnr_nvgtndrwr_actvtymain, new DashboardDoctor());
                                    ft.commit();
                                    break;
                                case 2:
                                    session.setIsHomeDctrNavdrwr(false);
                                    ft.replace(R.id.framecntnr_nvgtndrwr_actvtymain, new MyPatients());
                                    ft.commit();
                                    break;
                                case 3:
                                    session.setIsHomeDctrNavdrwr(false);
                                    ft.replace(R.id.framecntnr_nvgtndrwr_actvtymain, new NewsFrgmnt());
                                    ft.commit();
                                    break;
                                case 4:
                                    session.setIsHomeDctrNavdrwr(false);
                                    ft.replace(R.id.framecntnr_nvgtndrwr_actvtymain, new MyEquipmentFrgmnt());
                                    ft.commit();
                                    break;
                                case 5:
                                    session.setIsHomeDctrNavdrwr(false);
                                    ft.replace(R.id.framecntnr_nvgtndrwr_actvtymain, new MyPaymentsFrgmnt());
                                    ft.commit();
                                    break;
                                case 7:
                                    session.setIsHomeDctrNavdrwr(false);
                                    ft.replace(R.id.framecntnr_nvgtndrwr_actvtymain, new DoctorProfileFrgmnt());
                                    ft.commit();
                                    break;
                            }
                            return false;
                        }
                    })
                    .build();

            mImgvwHeaderImage = (RoundedImageView) mDrawer.getHeader().findViewById(R.id.roundimgvw_nvgtndrwr_header_profile);
            mTvHeaderName = (TextView) mDrawer.getHeader().findViewById(R.id.tv_nvgtndrwr_header_name);
            mTvHeaderEmail = (TextView) mDrawer.getHeader().findViewById(R.id.tv_nvgtndrwr_header_email);

            mDoctrName = getString(R.string.loading_3dots_sml);
            mDoctrEmail = session.getEmail();
            mDoctrImage = BitmapFactory.decodeResource(getResources(), R.drawable.person_bg_trqs);

            mTvHeaderName.setText(mDoctrName);
            mTvHeaderEmail.setText(mDoctrEmail);

            // Get profile icon from server using Picasso library
            /*DrawerImageLoader.init(new AbstractDrawerImageLoader() {
                @Override
                public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                    super.set(imageView, uri, placeholder);
                    Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
                }

                @Override
                public void cancel(ImageView imageView) {
                    super.cancel(imageView);
                }
            });*/

            // Directly call DashBoard Fragment rather than Header
            int openFrgmntPostn = getIntent().getIntExtra(OPEN_FRGMNT_BY_POSTN, NAVDRWER_FRGMNT_DASHBOARD);
            if(openFrgmntPostn == NAVDRWER_FRGMNT_DASHBOARD)
                mDrawer.setSelection(mPrmryDrwrItemDashbrd);
            else if(openFrgmntPostn == NAVDRWER_FRGMNT_NEWS){

                // Set selected position to Shared preferences
                session.setSlctdPostnDctrNavdrwr(3);
                mDrawer.setSelection(mPrmryDrwrItemNews);
            }

            if(General_Fnctns.isNetConnected(this)) {

                new GetDoctorProfileFromWebSrve().execute();
                // Get Doctor Image from External storage or URL
                checkGetDoctorImage();
            }
        }
    }

    public void onSectionAttached(int navDrwrFrgmnt) {
        switch (navDrwrFrgmnt) {
            case NAVDRWER_FRGMNT_DASHBOARD:
                mToolbarTitle = getString(R.string.dashboard_sml);
                break;
            case NAVDRWER_FRGMNT_PATIENTS:
                mToolbarTitle = getString(R.string.my_ptnts_sml);
                break;
            case NAVDRWER_FRGMNT_NEWS:
                mToolbarTitle = getString(R.string.news_sml);
                break;
            case NAVDRWER_FRGMNT_DISCUSSION:
//                app_title = navgndrwer_menu_titles[3];
                break;
            case NAVDRWER_FRGMNT_MYEQUIPMENT:
                mToolbarTitle = getString(R.string.my_eqpmnt_sml);
                break;
            case NAVDRWER_FRGMNT_PAYMENTS:
                mToolbarTitle = getString(R.string.my_pymnts_sml);
                break;
            case NAVDRWER_FRGMNT_PROFILE:
                mToolbarTitle = getString(R.string.my_profile_sml);
                break;
        }
        setActiobarTitle();
    }

    private void setActiobarTitle() {
        mToolbar.setTitle(mToolbarTitle);
    }

    public void updateBadge(int identifier, StringHolder badgeCount) {
        mDrawer.updateBadge(identifier, badgeCount);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));

    }

    public class GetDoctorProfileFromWebSrve extends AsyncTask<Void, Void, Void> {

        String websrvc_response;

        @Override
        protected Void doInBackground(Void... params) {

            ServiceHandler obj_srvchndlr = new ServiceHandler();

            JSONObject jsnobj = new JSONObject();
            try {
                jsnobj.put(FUNCTION_SMALL, FNCTN_SELECT_DOCTOR_BY_DR_ID);
                jsnobj.put(DR_ID, String.valueOf(session.getDrId()));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String json = WEBSRVC_JSONKEY + jsnobj.toString();
            websrvc_response = obj_srvchndlr.post_makeServiceCall(json);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(websrvc_response.equals(ACCESS_DENIED)){

                // There are some server problem
            }else{
                try {
                    JSONObject websrvc_jsnobj = new JSONObject(websrvc_response);
                    String success = websrvc_jsnobj.getString(SUCCESS_SMALL);
                    String websrvc_msg = websrvc_jsnobj.getString(MESSAGE_SMALL);
                    JSONArray jsonArrayFirst = websrvc_jsnobj.getJSONArray(DOCTOR_SMALL);
                    if(success.equals(TRUE_SMALL)){

                        if(!websrvc_msg.equals("null") && websrvc_msg.equals(DOCTOR_FOUND)){

                            if(jsonArrayFirst.length() !=0){

                                for(int i=0; i< jsonArrayFirst.length(); i++){

                                    JSONArray jsonArraySecnd = jsonArrayFirst.getJSONArray(i);

                                    for(int j=0; j<jsonArraySecnd.length(); j++){

                                        JSONObject jsnobj_dctrprfl = jsonArraySecnd.getJSONObject(j);

                                        mDoctrName = jsnobj_dctrprfl.getString(NAME_FN_SML) + " " + jsnobj_dctrprfl.getString(NAME_LN_SML);
                                        mDoctrEmail = jsnobj_dctrprfl.getString(EMAIL_SML);
                                        mProfileImageName = jsnobj_dctrprfl.getString(PROFILE_IMAGE_SML);

                                        mTvHeaderName.setText(mDoctrName);
                                        mTvHeaderEmail.setText(mDoctrEmail);
                                        // Check image to External storage if not available Get from AdminPanel and store to device
                                        checkGetDoctorImage();
                                    }
                                }
                            }
                        }
                    }else if(success.equals(FALSE_SMALL)){
                        // There are some server problem
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Check image to External storage if not available Get from AdminPanel and store to device
    private void checkGetDoctorImage() {

        if(TextUtils.isEmpty(mProfileImageName)) mImgvwHeaderImage.setImageBitmap(mDoctrImage);
        else {

            // Get Image from External Storage
            File fileImg = new File(STRNG_PATH_IMAGES_DOCTOR + FORWARD_SLASH + mProfileImageName +".jpg");
            if(fileImg.exists()){

                mDoctrImage = BitmapFactory.decodeFile(fileImg.getAbsolutePath());
                mImgvwHeaderImage.setImageBitmap(mDoctrImage);
            }else{

                if(General_Fnctns.isNetConnected(this)){

                    ServiceHandler srvcHndlr = new ServiceHandler();
                    String urlGetImg = srvcHndlr.getImagesFromUrl(DOCTOR_SMALL, mProfileImageName);

                    // Get Doctor Profile image from AdminPanel
                    new GetImageWebsrvc().execute(urlGetImg);
                }
            }
        }
    }

    // Get Doctor Profile image from AdminPanel
    public class GetImageWebsrvc extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bitmap_websrvc = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap_websrvc = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
            return bitmap_websrvc;
        }

        protected void onPostExecute(Bitmap result) {

            if(result != null){

                mDoctrImage = result;
                mImgvwHeaderImage.setImageBitmap(mDoctrImage);

                // Store image on External Storage
                storeDoctorImageToDevice(result);
            }
        }
    }

    // Save Web-service getted Image on External Device
    private void storeDoctorImageToDevice(Bitmap bitmap_websrvc_result) {

        File dctrProfileImgPath = PATH_IMAGES_DOCTOR;
        dctrProfileImgPath.mkdirs();

        String dctr_img_name = mProfileImageName +".jpg";
        File file = new File (dctrProfileImgPath, dctr_img_name);
        if (file.exists ()) file.delete ();

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap_websrvc_result.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        if(session !=null) {

            if(mDrawer.isDrawerOpen())
                mDrawer.closeDrawer();
            else if(session.getIsHomeDctrNavdrwr())
                super.onBackPressed();
            else {

                session.setSlctdPostnDctrNavdrwr(1);
                mDrawer.setSelection(mPrmryDrwrItemDashbrd);
            }
        } else
            super.onBackPressed();
    }
}
