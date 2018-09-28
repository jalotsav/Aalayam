package com.jalotsav.aalayam.navgndrwer.patient;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.jalotsav.aalayam.R;
import com.jalotsav.aalayam.adapters.FullScreenImageAdapter;
import com.jalotsav.aalayam.common.AalayamConstants;

public class FullScreenImageViewActivity extends Activity implements AalayamConstants{

	private FullScreenImageAdapter adapter;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lo_fullscreen_image_view);

		viewPager = (ViewPager) findViewById(R.id.pager);
		int position = getIntent().getIntExtra(SLCTD_POSITION, 0);
		ArrayList<String> arrylst_filepath = new ArrayList<String>();
		arrylst_filepath.addAll(getIntent().getStringArrayListExtra(ARRYLST_FILEPATHS_FULLSCRNIMAGE));

		adapter = new FullScreenImageAdapter(FullScreenImageViewActivity.this, arrylst_filepath);

		viewPager.setAdapter(adapter);

		// displaying selected image first
		viewPager.setCurrentItem(position);
	}
}
