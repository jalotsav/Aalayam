package com.jalotsav.aalayam.retrofitapihelper;

import com.jalotsav.aalayam.models.GetPatients.MdlGetPatients;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by JALOTSAV Dev. on 21/8/16.
 */
public interface RetroAPIAalayam {

    @FormUrlEncoded
    @POST("aalayam_client.php")
    Call<ResponseBody> getActivePatients(@Field("hr_master_data") JSONObject requestJSON);
//    Call<MdlGetPatients> getActivePatients(@Field("hr_master_data") JSONObject requestJSON);

/*
    @GET("users/1")
    Call<MdlUsers> getUsers();

    // request /get?testArg=...
    @GET("/get")
    Call<MdlUsers> getWithArg(@Query("testArg") String arg);

    // POST form encoded with form field params
    @FormUrlEncoded
    @POST("/post")
    Call<MdlUsers> postWithFormParams(@Field("field1") String field1);

    // POST with a JSON body
    @POST("/post")
    Call<MdlUsers> postWithJson(@Body MdlUsersAddress usersAddress);
*/
}
