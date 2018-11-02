package com.jalotsav.aalayamnavjivan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Jalotsav on 11/2/2018.
 */
public class ActvtySplash extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_actvty_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                finish();
                startActivity(new Intent(ActvtySplash.this, ActvtyBookView.class));
            }
        }, 3000);
    }
}
