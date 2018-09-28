package com.jalotsav.aalayam.common;

import android.os.Environment;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public interface AalayamConstants {

	// Tag String
	String TAG = "Jalotsav_Aalayam";
	
	// Log Type
	int VERBOSE = 1;
	int DEBUG = 2;
	int INFO = 3;
	int WARN = 4;
	int ERROR = 5;
	int ASSERT = 6;
	
	// Doctor Email id
	String DCTR_EMAILID = "doctor_emailid";

	// WebSite Name & WebService URL
	String API_ROOT_URL = "http://aalayamrehab.com/hrmasterservices/";
	String WEBSITE_NAME = "http://aalayamrehab.com/hrmasteradmin/";
	String WEBSERVICE_URL = "http://aalayamrehab.com/hrmasterservices/aalayam_client.php/";
	
	// WebService function name
	String FNCTN_CHECK_DOCTOR_EMAIL_LOGIN = "check_doctor_email_login";
	String FNCTN_CHECK_DOCTOR_EMAIL_PASSWORD = "check_doctor_email_password";
	String FNCTN_ACTIVE_PATIENT_BY_DR_ID = "active_patient_by_dr_id";
	String FNCTN_SELECT_NEWS_BY_DR_ID = "select_news_by_dr_id";
	String FNCTN_UPDATE_NEWS_WHO_READ = "update_news_who_read";
	String FNCTN_SELECT_DOCTOR_BY_DR_ID = "select_doctor_by_dr_id";
	String FNCTN_SELECT_PATIENT_BY_PT_ID = "select_patient_by_pt_id";
	String FNCTN_SELECT_PATIENT_DETAILS_BY_PT_ID = "select_patient_details_by_pt_id";
	String FNCTN_SELECT_PAYMENT_BY_PT_ID = "select_payment_by_pt_id";
	String FNCTN_SELECT_PAYMENT_EQUIPMENTS_BY_DR_ID = "select_payment_equipments_by_dr_id";
	String FNCTN_ADD_PATIENT_IMAGE_BY_PT_ID = "add_patient_image_by_pt_id";
	String FNCTN_INSERT_VAS_DETAILS = "insert_vas_details";
	String FNCTN_UPDATE_VAS_BEFORE = "update_vas_before";
	String FNCTN_UPDATE_VAS_AFTER = "update_vas_after";
	String FNCTN_UPDATE_REGISTRATION_ID = "update_registration_id";
	String FNCTN_SELECT_FEEDBACK_BY_DR_PT = "select_feedback_by_dr_pt";
	String FNCTN_INSERT_FEEDBACK = "insert_feedback";
	
	// WebService REQUEST
	String WEBSRVC_JSONKEY = "hr_master_data=";
	String FUNCTION_SMALL = "function";
	String EMAIL_SMALL = "email";
	String DR_ID = "dr_id";
	String PASSWORD_SMALL = "password";
	String PT_ID = "pt_id";
	String BASE_64 = "base_64";
	String BEFORE_SML = "before";
	String AFTER_SML = "after";
	String DAILY_PAYMENT = "daily_payment";
	String DAILY_PAYMENT_TYPE = "daily_payment_type";
	String NEWS_ID = "news_id";
	String WHO_READ_SML = "who_read";
	String REGISTRATION_ID = "registration_id";
	String FEEDBACK_OFFICE_PREMIES = "feedback_office_premies";
	String FEEDBACK_DIAGNOSIS = "feedback_diagnosis";
	String FEEDBACK_TREATMENT = "feedback_treatment";
	String FEEDBACK_DR_ASSISTANCE = "feedback_dr_assistance";
	String FEEDBACK_COMMENTS= "feedback_comments";
	String FEEDBACK_SUGGESTION = "feedback_suggestion";
	
	// WebService RESPONSE
	String ACCESS_DENIED = "Access Denied";
	String SUCCESS_SMALL = "success";
	String TRUE_SMALL = "true";
	String TRUE_FRSTCAPS = "True";
	String FALSE_SMALL = "false";
	String FALSE_FRSTCAPS = "False";
	String MESSAGE_SMALL = "message";
	String FUNCTION_NOT_FOUND = "function not found";
	String DOCTOR_RECORD_FOUND = "doctor record found";
	String DOCTOR_RECORD_NOT_FOUND = "doctor record not found";
	String VALID_PASSWORD = "valid password";
	String INVALID_PASSWORD = "invalid password";
	String PATIENTS_FOUND = "patients found";
	String PATIENTS_NOT_FOUND = "patients not found";
	String NEWS_FOUND = "news found";
	String NEWS_NOT_FOUND = "news not found";
	String DOCTOR_FOUND = "doctor found";
	String DOCTOR_NOT_FOUND = "doctor not found";
	String PATIENT_FOUND = "patient found";
	String PATIENT_NOT_FOUND = "patient not found";
	String DOCTOR_SMALL = "doctor";
	String PATIENTS_SMALL = "patients";
	String VAS_DETAILS_ADDED_SUCCESSFULLY = "vas details added successfully";
	String VAS_DETAILS_UPDATED_SUCCESSFULLY = "vas details updated successfully";
	String PAYMENT_SML = "payment";
	String EQUIPMENTS_SML = "equipments";
	String NEWS_SML = "news";
	String ACTIVE_SML = "Active";
	String INACTIVE_SML = "Inactive";
	String FEEDBACK_NOT_FOUND_DOT = "Feedback not found.";
	
	// WebService RESPONSE PROFILE & DETAILS
	String NAME_FN_SML = "name_fn";
	String NAME_MN_SML = "name_mn";
	String NAME_LN_SML = "name_ln";
	String EMAIL_SML = "email";
	String PASSWORD_SML = "password";
	String PROFILE_IMAGE_SML = "profile_image";
	String CONTACT_NO_SML = "contact_no";
	String CONTACT_NO_1_SML = "contact_no_1";
	String CONTACT_NO_2_SML = "contact_no_2";
	String ADDRESS_SML = "address";
	String AREA_ID_SML = "area_id";
	String BIRTH_DATE_SML = "birth_date";
	String JOIN_DATE_SML = "join_date";
	String QUALIFICATION_SML = "qualification";
	String DESIGNATION_SML = "designation";
	String CASE_DESCRIPPTION_SML = "case_descripption";
	String CASE_IMAGE_SML = "case_image";
	String DIAGNOSIS_SML = "diagnosis";
	String PLAN_SML = "plan";
	String STATUS_SML = "status";
	String REASSESMENT_SML = "reassesment";
	String REASSESMENT_DR_ID_SML = "reassesment_dr_id";
	String IMAGE_SML = "image";
	String VIDEO_SML = "video";
	String FEES_SML = "fees";
	String NO_OF_SETTINGS_SML = "no_of_settings";
	String PATIENT_ADD_DATE_SML = "patient_add_date";
	String RESS_ADD_DATE_SML = "ress_add_date";
	String DR_NAME_FN_SML = "dr_name_fn";
	String DR_NAME_MN_SML = "dr_name_mn";
	String DR_NAME_LN_SML = "dr_name_ln";
	String AREA_SMALL = "area";
	String A_TITLE = "a_title"; // area title
	String LTO_SML = "lto";
	String EXE_SML = "exe";
	String LTO_EXE_SML = "lto_exe";
	String VAS_DETAILS_SML = "vas_details";
	String VAS_ID = "vas_id";
	String PY_ID = "py_id";
	String PARTICULARS_SML = "particulars";
	String AMOUNT_SML = "amount";
	String ADD_DATE_SML = "add_date";
	String MODIFY_DATE_SML = "modify_date";
	
	// WebService RESPONSE Doctor Inventory
	String EQ_ADD_DATE_SML = "eq_add_date";
	String TITLE_SML = "title";
	String QTY_SML = "qty";
	String RATE_SML = "rate";
	String DESCRIPTION_SML = "description";	
	
	// Web Folder Name
	String FORWARD_SLASH = "/";
	String IMAGES_SMALL = "images";
	String PATIENT_SMALL = "patient";
	String CASE_SMALL = "case";
	String PROFILE_SMALL = "profile";
	String VIDEOS_SMALL = "videos";
	
	// Aalayam Directory External storage path
	String AALAYAM = "Aalayam";
	String EXTRNL_STRG_PATH_STRING = Environment.getExternalStorageDirectory().getAbsolutePath();
	File PATH_EXTRNL_STRG = new File(EXTRNL_STRG_PATH_STRING);
	File PATH_IMAGES = new File(EXTRNL_STRG_PATH_STRING + FORWARD_SLASH + AALAYAM + FORWARD_SLASH + IMAGES_SMALL);
    File PATH_IMAGES_DOCTOR = new File(EXTRNL_STRG_PATH_STRING + FORWARD_SLASH + AALAYAM + FORWARD_SLASH + IMAGES_SMALL + FORWARD_SLASH + DOCTOR_SMALL);
    String STRNG_PATH_IMAGES_DOCTOR = EXTRNL_STRG_PATH_STRING + FORWARD_SLASH + AALAYAM + FORWARD_SLASH + IMAGES_SMALL + FORWARD_SLASH + DOCTOR_SMALL;
    File PATH_IMAGES_PATIENT_CASE = new File(EXTRNL_STRG_PATH_STRING + FORWARD_SLASH + AALAYAM + FORWARD_SLASH + IMAGES_SMALL + FORWARD_SLASH + PATIENT_SMALL + FORWARD_SLASH + CASE_SMALL);
    File PATH_IMAGES_PATIENT_IMAGES = new File(EXTRNL_STRG_PATH_STRING + FORWARD_SLASH + AALAYAM + FORWARD_SLASH + IMAGES_SMALL + FORWARD_SLASH + PATIENT_SMALL + FORWARD_SLASH + IMAGES_SMALL);
    String STRNG_PATH_IMAGES_PATIENT_IMAGES = EXTRNL_STRG_PATH_STRING + FORWARD_SLASH + AALAYAM + FORWARD_SLASH + IMAGES_SMALL + FORWARD_SLASH + PATIENT_SMALL + FORWARD_SLASH + IMAGES_SMALL;
    File PATH_IMAGES_PATIENT_PROFILE = new File(EXTRNL_STRG_PATH_STRING + FORWARD_SLASH + AALAYAM + FORWARD_SLASH + IMAGES_SMALL + FORWARD_SLASH + PATIENT_SMALL + FORWARD_SLASH + PROFILE_SMALL);
    String STRNG_PATH_IMAGES_PATIENT_PROFILE = EXTRNL_STRG_PATH_STRING + FORWARD_SLASH + AALAYAM + FORWARD_SLASH + IMAGES_SMALL + FORWARD_SLASH + PATIENT_SMALL + FORWARD_SLASH + PROFILE_SMALL;
    File PATH_IMAGES_PATIENT_VIDEOS = new File(EXTRNL_STRG_PATH_STRING + FORWARD_SLASH + AALAYAM + FORWARD_SLASH + IMAGES_SMALL + FORWARD_SLASH + PATIENT_SMALL + FORWARD_SLASH + VIDEOS_SMALL);

	// Navigation Drawer Used Key
	int NAVDRWER_FRGMNT_DASHBOARD = 0;
	int NAVDRWER_FRGMNT_PATIENTS = 1;
	int NAVDRWER_FRGMNT_NEWS = 2;
	int NAVDRWER_FRGMNT_DISCUSSION = 3;
	int NAVDRWER_FRGMNT_MYEQUIPMENT = 4;
	int NAVDRWER_FRGMNT_PAYMENTS = 5;
	int NAVDRWER_FRGMNT_PROFILE = 6;
	int NAVDRWER_FRGMNT_PATIENTIMAGES = 7;
	int NAVDRWER_FRGMNT_PATIENTVIDEOS = 8;
	int NAVDRWER_FRGMNT_VASSCALE = 9;
	int NAVDRWER_FRGMNT_FEEDBACK = 10;

	// Identifier
	int DRWRITEM_IDENTIFIER_PATIENTS = 51;
	int DRWRITEM_IDENTIFIER_NEWS = 52;

    // VasScale Emojies
    String EMOJS_00_ID = "emojs_00_id";
    String EMOJS_01_ID = "emojs_01_id";
    String EMOJS_02_ID = "emojs_02_id";
    String EMOJS_03_ID = "emojs_03_id";
    String EMOJS_04_ID = "emojs_04_id";
    String EMOJS_05_ID = "emojs_05_id";
    String EMOJS_06_ID = "emojs_06_id";
    String EMOJS_07_ID = "emojs_07_id";
    String EMOJS_08_ID = "emojs_08_id";
    String EMOJS_09_ID = "emojs_09_id";
    String EMOJS_10_ID = "emojs_10_id";

	// Feedback Rating Answer
	String FEDBCK_RATNG_FAIR = "Fair";
	String FEDBCK_RATNG_AVERAGE = "Average";
	String FEDBCK_RATNG_GOOD = "Good";
	String FEDBCK_RATNG_VERYGOOD = "Very good";
	String FEDBCK_RATNG_EXCELLENT = "Excellent";

	// VasScale
	String DAILY_PAYMENT_TYPE_CASH = "Cash";
	String DAILY_PAYMENT_TYPE_CHEQUE = "Cheque";

    // Others
    String ICON_FIRST_CHAR_OFTEXT = "icon_firt_char_oftext";
    String ICON_FIRST_TWODIGIT_OFTEXT = "icon_firt_twodigit_oftext";
    String PATIENT_NAME_SML = "patient_name";
    String COME_FOR = "come_for";
    String ADD_VASSCALE = "add_vasscale";
    String UPDATE_VASSCALE = "update_vasscale";
    String VASSCALE_PREVIOUS_TIMESTAMP = "vasscale_previous_timestamp";
    String COME_FROM_PAYMENTS = "come_from_payments";
    String COME_FROM_INVENTORY = "come_from_inventory";
    int COME_FROM_TABPTNTIMGS = 11;
    int COME_FROM_TABPTNTCASEIMGS = 12;
    String SLCTD_POSITION = "position";
    String ARRYLST_FILEPATHS_FULLSCRNIMAGE = "arrylst_filepaths_fullscreenimage";
    int HIDE_USERNOTIFY_TIME_5K = 5000;
    int HIDE_USERNOTIFY_TIME_10K = 10000;

	// Push Notification
	int NOTIFICATION_BUILDER_ID_SINGLE = 1;
	String OPEN_FRGMNT_BY_POSTN = "open_frgmnt_by_position";

	// Key for onActivityResult
	int REQUESTCODE_SPLASHACTIVITY = 101;
	int REQUEST_APP_PERMISSION = 102;

	// Remote Config keys
	String VASSCALE_PREVIOUS_ADD_ENABLED = "vasscale_previous_add_enabled";

    /** GridView Element **/
    // Number of columns of Grid View
    int NUM_OF_COLUMNS_2 = 2;
    int NUM_OF_COLUMNS_3 = 3;
    // GridView image padding
    int GRID_PADDING_8 = 8; // in dp
    int GRID_PADDING_5 = 5; // in dp
    /** END GridView Element **/
    
    // supported file formats
    List<String> FILE_EXTN = Arrays.asList("jpg", "jpeg", "png");
}
