package com.jalotsav.aalayam.navgndrwer.patient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jalotsav.aalayam.R;

/**
 * Created by JALOTSAV Dev. on 20/11/15.
 */
public class PatientVideosFrgmnt extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.lo_patient_frgmnt_patntvideos, container, false);

        return rootView;
    }
}
