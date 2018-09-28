package com.jalotsav.aalayam.models.GetPatients;

import com.google.gson.annotations.SerializedName;
import com.jalotsav.aalayam.common.AalayamConstants;

import java.util.ArrayList;

/**
 * Created by JALOTSAV Dev. on 21/8/16.
 */
public class MdlGetPatients implements AalayamConstants {

    @SerializedName(SUCCESS_SMALL)
    String success;

    @SerializedName(MESSAGE_SMALL)
    String message;

    @SerializedName(PATIENTS_SMALL)
    ArrayList<MdlGetPatientsFirstArray> firstArray;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<MdlGetPatientsFirstArray> getFirstArray() {
        return firstArray;
    }

    public void setFirstArray(ArrayList<MdlGetPatientsFirstArray> firstArray) {
        this.firstArray = firstArray;
    }
}
