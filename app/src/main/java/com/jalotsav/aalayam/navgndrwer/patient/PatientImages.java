package com.jalotsav.aalayam.navgndrwer.patient;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jalotsav.aalayam.R;
import com.jalotsav.aalayam.adapters.SlidingTabLayout;
import com.jalotsav.aalayam.adapters.TabViewPagerAdapter;
import com.jalotsav.aalayam.common.AalayamConstants;

public class PatientImages extends Fragment implements AalayamConstants {

	ViewPager vwpgr_ptntimgs;
	TabViewPagerAdapter tavvwpgr_adapter;
	SlidingTabLayout tabs;
	CharSequence Titles[] = { "Case Images", "Patient Images"};
	int Numboftabs = 2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View rootView = inflater.inflate(
				R.layout.lo_patient_frgmnt_patntimages, container, false);

		// Set Actionbar Title
		((NavgnDrawer_Main_Patient) getActivity()).onSectionAttached(NAVDRWER_FRGMNT_PATIENTIMAGES);

		tavvwpgr_adapter = new TabViewPagerAdapter(getFragmentManager(), Titles, Numboftabs);

		vwpgr_ptntimgs = (ViewPager) rootView.findViewById(R.id.vwpgr_ptnt_frgmnt_ptntimgs);
		vwpgr_ptntimgs.setAdapter(tavvwpgr_adapter);

		tabs = (SlidingTabLayout) rootView.findViewById(R.id.tabs_ptnt_frgmnt_ptntimgs);
		tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true,
										// This makes the tabs Space Evenly in
										// Available width

		// Setting Custom Color for the Scroll bar indicator of the Tab View
		tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
			
			@Override
			public int getIndicatorColor(int position) {
				// Tab ScrollBar Color(Tab underline color)
				return ContextCompat.getColor(getActivity(), R.color.white);
			}
		});

		// Setting the ViewPager For the SlidingTabsLayout
		tabs.setViewPager(vwpgr_ptntimgs);

		return rootView;
	}

}
