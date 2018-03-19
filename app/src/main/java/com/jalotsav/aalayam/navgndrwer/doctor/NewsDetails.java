package com.jalotsav.aalayam.navgndrwer.doctor;

import java.util.Locale;

import com.jalotsav.aalayam.R;
import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.common.General_Fnctns;
import com.jalotsav.aalayam.service.InternetService;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class NewsDetails extends AppCompatActivity implements AalayamConstants {

	private TextView tvTitle, tvDscrptn, tvDate;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lo_doctor_frgmnt_news_details);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setElevation(0);
		
		tvTitle = (TextView) findViewById(R.id.tv_dctr_frgmnt_news_dtls_title);
		tvDscrptn = (TextView) findViewById(R.id.tv_dctr_frgmnt_news_dtls_dscrptn);
		tvDate = (TextView) findViewById(R.id.tv_dctr_frgmnt_news_dtls_date);

		// Check Internet check service is running or not, If not then Start Service
		if(!General_Fnctns.isServiceRunning(InternetService.class, this))
			startService(new Intent(this, InternetService.class));
		
		tvTitle.setText(getIntent().getStringExtra(TITLE_SML));
		String strHtmlPrtclr = Html.fromHtml(getIntent().getStringExtra(DESCRIPTION_SML)).toString();
		tvDscrptn.setText(Html.fromHtml(strHtmlPrtclr).toString().trim());
		tvDate.setText(getIntent().getStringExtra(ADD_DATE_SML));
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
