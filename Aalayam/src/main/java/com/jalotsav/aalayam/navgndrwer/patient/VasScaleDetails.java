package com.jalotsav.aalayam.navgndrwer.patient;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jalotsav.aalayam.R;
import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.common.General_Fnctns;
import com.jalotsav.aalayam.service.InternetService;

public class VasScaleDetails extends Activity implements AalayamConstants{

	TextView tvPtntName, tvScaleBefore, tvScaleAfter, tvScaleDate, tvDailyPymnt;
	ImageView imgvwBefore, imgvwAfter;
	Map<String, Integer> mapEmojsDrwbleId;
	Button mBtnOk;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lo_patient_frgmnt_vasscale_details);
		
		tvPtntName = (TextView)findViewById(R.id.tv_ptnt_frgmnt_vasscale_dtls_ptntname);
		tvScaleBefore = (TextView)findViewById(R.id.tv_ptnt_frgmnt_vasscale_dtls_before);
		tvScaleAfter = (TextView)findViewById(R.id.tv_ptnt_frgmnt_vasscale_dtls_after);
		tvScaleDate = (TextView)findViewById(R.id.tv_ptnt_frgmnt_vasscale_dtls_date);
		tvDailyPymnt = (TextView)findViewById(R.id.tv_ptnt_frgmnt_vasscale_dtls_dailypymnt);
		imgvwBefore = (ImageView) findViewById(R.id.imgvw_ptnt_frgmnt_vasscale_dtls_beforeemoji);
		imgvwAfter = (ImageView) findViewById(R.id.imgvw_ptnt_frgmnt_vasscale_dtls_afteremoji);
		mBtnOk = (Button)findViewById(R.id.btn_ptnt_frgmnt_vasscale_dtls_ok);
		
		tvPtntName.setText(getIntent().getStringExtra(PATIENT_NAME_SML));
		tvScaleBefore.setText(String.valueOf(getIntent().getIntExtra(BEFORE_SML, 0)));
		tvScaleAfter.setText(String.valueOf(getIntent().getIntExtra(AFTER_SML, 0)));
		tvScaleDate.setText(getIntent().getStringExtra(ADD_DATE_SML));
		String dailyPymnt = getIntent().getStringExtra(DAILY_PAYMENT);
		String dailyPymntType = getIntent().getStringExtra(DAILY_PAYMENT_TYPE);
		if(!TextUtils.isEmpty(dailyPymnt) && !TextUtils.isEmpty(dailyPymntType))
			tvDailyPymnt.setText(getString(R.string.dailypymnt_with_type_sml, dailyPymnt, dailyPymntType));

		// Check Internet check service is running or not, If not then Start Service
		if(!General_Fnctns.isServiceRunning(InternetService.class, this))
			startService(new Intent(this, InternetService.class));

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
		
		setEmojsVasscale(imgvwBefore, getIntent().getIntExtra(BEFORE_SML, 0));
		setEmojsVasscale(imgvwAfter, getIntent().getIntExtra(AFTER_SML, 0));
		
		mBtnOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				onBackPressed();
			}
		});
	}

	// Set EmoJies to ImageView as per VasScale value
	protected void setEmojsVasscale(ImageView imgvw, int vasScale) {
		// TODO Auto-generated method stub

		switch (vasScale) {
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
}
