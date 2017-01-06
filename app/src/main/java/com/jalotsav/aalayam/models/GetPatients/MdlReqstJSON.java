package com.jalotsav.aalayam.models.GetPatients;

import com.google.gson.annotations.SerializedName;
import com.jalotsav.aalayam.common.AalayamConstants;

/**
 * Created by JALOTSAV Dev. on 21/8/16.
 */
public class MdlReqstJSON implements AalayamConstants {

    @SerializedName(WEBSRVC_JSONKEY)
    String hr_master_data;

    public MdlReqstJSON(String functionName) {
        this.hr_master_data = functionName;
    }
}
