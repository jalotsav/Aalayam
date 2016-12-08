package com.jalotsav.aalayam.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bsptechno.libfabbsptechno.FloatingActionButton;
import com.jalotsav.aalayam.R;

/**
 * Created by JALOTSAV Dev. on 19/11/15.
 */
public class NoInternet_Connection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_nointernet_connctn);

        // Register BroadcastReceiver
        registerReceiver(brdcstrcvrFinishNoIntrntActvt, new IntentFilter("intntfltrNoIntrntActvt"));

        FloatingActionButton fabSettings = (FloatingActionButton) findViewById(R.id.fab_nointrnt_connctn_settings);
        fabSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
            }
        });
    }

    // This BroadcastReceiver call from InternetService when Internet is connected
    private final BroadcastReceiver brdcstrcvrFinishNoIntrntActvt = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // finish activity on BroadcastReceiver call
            finish();
        }
    };

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        //android.os.Process.killProcess(android.os.Process.myPid());
        //System.exit(0);
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        // Destroy BroadcastReceiver when activity destroy
        unregisterReceiver(brdcstrcvrFinishNoIntrntActvt);
    }
}
