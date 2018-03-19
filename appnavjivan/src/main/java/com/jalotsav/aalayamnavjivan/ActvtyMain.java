package com.jalotsav.aalayamnavjivan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Jalotsav on 1/16/18.
 */
public class ActvtyMain extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_actvty_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                finish();
                startActivity(new Intent(ActvtyMain.this, ActvtyBookView.class));
            }
        }, 3000);
    }
}
