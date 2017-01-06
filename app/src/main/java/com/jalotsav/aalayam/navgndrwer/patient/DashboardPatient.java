package com.jalotsav.aalayam.navgndrwer.patient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jalotsav.aalayam.R;
import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.common.UserSessionManager;

public class DashboardPatient extends Fragment implements AalayamConstants, View.OnClickListener {

	static UserSessionManager session;
	ImageView imgvwProfile, imgvwVasscale, imgvwCaseImages, imgvwPymnts, imgvwFeedback;
//	private String slctd_pt_id;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View rootView = inflater.inflate(R.layout.lo_patient_frgmnt_dashboard, container, false);

		// Set Actionbar Title
		((NavgnDrawer_Main_Patient) getActivity()).onSectionAttached(NAVDRWER_FRGMNT_DASHBOARD);

		session = new UserSessionManager(getActivity());

		imgvwProfile = (ImageView) rootView.findViewById(R.id.imgvw_ptnt_frgmnt_dashbrd_profile);
		imgvwVasscale = (ImageView) rootView.findViewById(R.id.imgvw_ptnt_frgmnt_dashbrd_vasscales);
		imgvwCaseImages = (ImageView) rootView.findViewById(R.id.imgvw_ptnt_frgmnt_dashbrd_caseimages);
		imgvwPymnts = (ImageView) rootView.findViewById(R.id.imgvw_ptnt_frgmnt_dashbrd_pymnts);
		imgvwFeedback = (ImageView) rootView.findViewById(R.id.imgvw_ptnt_frgmnt_dashbrd_feedback);

//		slctd_pt_id = getActivity().getIntent().getStringExtra(PT_ID);

		imgvwProfile.setOnClickListener(this);
		imgvwVasscale.setOnClickListener(this);
		imgvwCaseImages.setOnClickListener(this);
		imgvwPymnts.setOnClickListener(this);
		imgvwFeedback.setOnClickListener(this);

		return rootView;		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.imgvw_ptnt_frgmnt_dashbrd_profile:

				if(session !=null) {
					// Set selected position and IsHome status to Shared preferences
					session.setSlctdPostnDctrNavdrwr(7);
					session.setIsHomeDctrNavdrwr(false);
				}

				Fragment fragmentProfile = new PatientProfileFrgmnt();
				if (fragmentProfile != null) {
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.frame_container_patient, fragmentProfile).commit();
				}
				break;
			case R.id.imgvw_ptnt_frgmnt_dashbrd_vasscales:

				if(session !=null) {
					// Set selected position and IsHome status to Shared preferences
					session.setSlctdPostnDctrNavdrwr(4);
					session.setIsHomeDctrNavdrwr(false);
				}

				Fragment fragmentVasscale = new VasScaleFrgmnt();
				if (fragmentVasscale != null) {
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.frame_container_patient, fragmentVasscale).commit();
				}
				break;
			case R.id.imgvw_ptnt_frgmnt_dashbrd_caseimages:

				if(session !=null) {
					// Set selected position and IsHome status to Shared preferences
					session.setSlctdPostnDctrNavdrwr(2);
					session.setIsHomeDctrNavdrwr(false);
				}

				Fragment fragmentPtntImgs = new PatientImages();
				if (fragmentPtntImgs != null) {
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.frame_container_patient, fragmentPtntImgs).commit();
				}

				break;
			case R.id.imgvw_ptnt_frgmnt_dashbrd_pymnts:

				if(session !=null) {
					// Set selected position and IsHome status to Shared preferences
					session.setSlctdPostnDctrNavdrwr(5);
					session.setIsHomeDctrNavdrwr(false);
				}

				Fragment fragmentPymnts = new PaymentsFrgmnt();
				if (fragmentPymnts != null) {
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.frame_container_patient, fragmentPymnts).commit();
				}

				break;
			case R.id.imgvw_ptnt_frgmnt_dashbrd_feedback:

				if(session !=null) {
					// Set selected position and IsHome status to Shared preferences
					session.setSlctdPostnDctrNavdrwr(6);
					session.setIsHomeDctrNavdrwr(false);
				}

				Fragment fragmentFeedback = new FeedbackFrgmnt();
				if (fragmentFeedback != null) {
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.frame_container_patient, fragmentFeedback).commit();
				}
				break;

			default:
				break;
		}
	}

}