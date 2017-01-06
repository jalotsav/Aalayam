package com.jalotsav.aalayam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.common.General_Fnctns;
import com.jalotsav.aalayam.navgndrwer.doctor.NavgnDrawer_Main_Doctor;
import com.jalotsav.aalayam.navgndrwer.doctor.NavgnDrwrDoctor;
import com.jalotsav.aalayam.service.InternetService;

import io.fabric.sdk.android.Fabric;

public class Aalayam extends Activity implements AalayamConstants {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.lo_aalayam);

        final int openFrgmntPostn = getIntent().getIntExtra(OPEN_FRGMNT_BY_POSTN, NAVDRWER_FRGMNT_DASHBOARD);

        Handler mhandlr = new Handler();
        mhandlr.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                finish();

                // Check Internet check service is running or not, If not then Start Service
                if (General_Fnctns.isServiceRunning(InternetService.class, Aalayam.this))
                    Log.i(TAG, "InternetCheck Service already Running");
                else {

                    Log.e(TAG, "InternetCheck Service NOT Running, Start Service");
                    startService(new Intent(Aalayam.this, InternetService.class));
                }

//                Intent intntNvgnDrwrDoctr = new Intent(Aalayam.this, NavgnDrawer_Main_Doctor.class);
                Intent intntNvgnDrwrDoctr = new Intent(Aalayam.this, NavgnDrwrDoctor.class);
                intntNvgnDrwrDoctr.putExtra(OPEN_FRGMNT_BY_POSTN, openFrgmntPostn);
                startActivity(intntNvgnDrwrDoctr);
            }
        }, 3000);
    }
}
