package com.jalotsav.aalayam.navgndrwer.patient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jalotsav.aalayam.R;
import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.common.General_Fnctns;
import com.jalotsav.aalayam.service.InternetService;

public class PaymentsDetails extends ActionBarActivity implements AalayamConstants{

	LinearLayout lnrylot_ptntnm;
	private TextView tvPtntName, tvPymntId, tvAmnt, tvPrtclr, tvDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lo_patient_frgmnt_payments_details);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setElevation(0);
		
		lnrylot_ptntnm = (LinearLayout) findViewById(R.id.lntlyot_ptnt_frgmnt_pymnts_dtls_patntnm);
		tvPtntName = (TextView) findViewById(R.id.tv_ptnt_frgmnt_pymnts_dtls_patntnm);
		tvPymntId = (TextView) findViewById(R.id.tv_ptnt_frgmnt_pymnts_dtls_pymntid);
		tvAmnt = (TextView) findViewById(R.id.tv_ptnt_frgmnt_pymnts_dtls_pymntamount);
		tvDate = (TextView) findViewById(R.id.tv_ptnt_frgmnt_pymnts_dtls_pymntdate);
		tvPrtclr = (TextView) findViewById(R.id.tv_ptnt_frgmnt_pymnts_dtls_pymntprtclr);

		// Check Internet check service is running or not, If not then Start Service
		if(!General_Fnctns.isServiceRunning(InternetService.class, this))
			startService(new Intent(this, InternetService.class));
		
		String ptntnm = getIntent().getStringExtra(PATIENT_NAME_SML);
		if(ptntnm.length() == 0) lnrylot_ptntnm.setVisibility(View.GONE);
		else tvPtntName.setText(ptntnm);
		tvPymntId.setText(getIntent().getStringExtra(PY_ID));
		tvAmnt.setText(getResources().getString(R.string.symbl_rupee) + " " + getIntent().getStringExtra(AMOUNT_SML));
		tvDate.setText(getIntent().getStringExtra(ADD_DATE_SML));
		String strHtmlPrtclr = Html.fromHtml(getIntent().getStringExtra(PARTICULARS_SML)).toString();
		tvPrtclr.setText(Html.fromHtml(strHtmlPrtclr).toString().trim());
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
