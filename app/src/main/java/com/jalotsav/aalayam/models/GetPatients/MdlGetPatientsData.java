package com.jalotsav.aalayam.models.GetPatients;

import com.google.gson.annotations.SerializedName;
import com.jalotsav.aalayam.common.AalayamConstants;

/**
 * Created by JALOTSAV Dev. on 21/8/16.
 */
public class MdlGetPatientsData implements AalayamConstants {

    @SerializedName(PT_ID)
    String ptId;

    @SerializedName(NAME_FN_SML)
    String nameFn;

    @SerializedName(NAME_LN_SML)
    String nameLn;

    @SerializedName(IMAGE_SML)
    String image;

    @SerializedName(CASE_IMAGE_SML)
    String caseImage;

    public String getPtId() {
        return ptId;
    }

    public void setPtId(String ptId) {
        this.ptId = ptId;
    }

    public String getNameFn() {
        return nameFn;
    }

    public void setNameFn(String nameFn) {
        this.nameFn = nameFn;
    }

    public String getNameLn() {
        return nameLn;
    }

    public void setNameLn(String nameLn) {
        this.nameLn = nameLn;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCaseImage() {
        return caseImage;
    }

    public void setCaseImage(String caseImage) {
        this.caseImage = caseImage;
    }
}
