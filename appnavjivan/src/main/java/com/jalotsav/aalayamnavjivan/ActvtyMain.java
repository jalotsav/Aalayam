package com.jalotsav.aalayamnavjivan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jalotsav.aalayamnavjivan.common.AppConstants;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jalotsav on 1/16/18.
 */
public class ActvtyMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_actvty_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.appcmptbtn_actvty_main_lang_gujarati, R.id.appcmptbtn_actvty_main_lang_english})
    public void onClickView(View view) {

        switch (view.getId()) {
            case R.id.appcmptbtn_actvty_main_lang_gujarati:

                startActivity(new Intent(this, ActvtyBookView.class)
                        .putExtra(AppConstants.PUT_EXTRA_LANGUAGE_NAME, AppConstants.LANGUAGE_SHORTCODE_GUJARATI));
                break;
            case R.id.appcmptbtn_actvty_main_lang_english:

                startActivity(new Intent(this, ActvtyBookView.class)
                        .putExtra(AppConstants.PUT_EXTRA_LANGUAGE_NAME, AppConstants.LANGUAGE_SHORTCODE_ENGLISH));
                break;
        }
    }
}
