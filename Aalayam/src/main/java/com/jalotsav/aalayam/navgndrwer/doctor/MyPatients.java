package com.jalotsav.aalayam.navgndrwer.doctor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jalotsav.aalayam.R;
import com.jalotsav.aalayam.adapters.SlidingTabLayout;
import com.jalotsav.aalayam.adapters.TabViewPagerAdapterMyPatients;
import com.jalotsav.aalayam.common.AalayamConstants;

public class MyPatients extends Fragment implements AalayamConstants {

	ViewPager vwpgr_ptntimgs;
	TabViewPagerAdapterMyPatients tavvwpgr_adapter;
	SlidingTabLayout tabs;
	CharSequence Titles[] = { "Active", "Reassessment" };
	int Numboftabs = 2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View rootView = inflater.inflate(R.layout.lo_doctor_frgmnt_mypatients, container, false);

		// Set Actionbar Title
		((NavgnDrawer_Main_Doctor) getActivity()).onSectionAttached(NAVDRWER_FRGMNT_PATIENTS);

		tavvwpgr_adapter = new TabViewPagerAdapterMyPatients(getFragmentManager(), Titles, Numboftabs);

		vwpgr_ptntimgs = (ViewPager) rootView.findViewById(R.id.vwpgr_dctr_frgmnt_mypatients);
		vwpgr_ptntimgs.setAdapter(tavvwpgr_adapter);

		tabs = (SlidingTabLayout) rootView.findViewById(R.id.tabs_dctr_frgmnt_mypatients);
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
