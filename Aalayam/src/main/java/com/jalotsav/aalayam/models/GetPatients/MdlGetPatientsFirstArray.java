package com.jalotsav.aalayam.models.GetPatients;

import com.google.gson.annotations.SerializedName;
import com.jalotsav.aalayam.common.AalayamConstants;

import java.util.ArrayList;

/**
 * Created by JALOTSAV Dev. on 22/8/16.
 */
public class MdlGetPatientsFirstArray implements AalayamConstants {

    ArrayList<MdlGetPatientsData> patientsData;

    public ArrayList<MdlGetPatientsData> getPatientsData() {
        return patientsData;
    }

    public void setPatientsData(ArrayList<MdlGetPatientsData> patientsData) {
        this.patientsData = patientsData;
    }
}
