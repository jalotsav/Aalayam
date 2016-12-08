package com.jalotsav.aalayam.common;

import com.jalotsav.aalayam.Login_Email;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserSessionManager {

	// Shared Preferences reference
    SharedPreferences pref;
    
    // Editor reference for Shared preferences
    Editor editor;
    
    // Context
    Context _context;
    
    // Shared pref mode
    int PRIVATE_MODE = 0;
    
    // Sharedpref file name
    private static String PREFER_NAME;
    
    // All Shared Preferences Keys
    // Is User Login or not (make variable public to access from outside)
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    
    // Dr_Id
    public static final String KEY_DR_ID = "dr_id";
    
    // Doctor Email
    public static final String KEY_EMAIL = "email";
    
    // Selected Position of Doctor NavigationDrawer
    public static final String KEY_SLCTD_POSTN_DCTR_NAVDRWR = "slctd_postn_dctr_navdrwr";

    // Doctor Email
    public static final String KEY_ISHOME_DCTR_NAVDRWR = "is_home_dctr_navdrwr";
    
    // Constructor
    public UserSessionManager(Context context){
        
    	this._context = context;
        PREFER_NAME = _context.getPackageName() + "_shrdprfrnc";
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }
    
    // Create login session
    // Store DrId to SharedPreferences
    public void setDrId(int dr_id){
        
        editor.putInt(KEY_DR_ID, dr_id);
        editor.commit();
    }
        
    // Store Doctor Email to SharedPreferences
    public void setEmail(String email){
        
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }
    
	// Store Login Status to SharedPreferences
	public void setLoginStatusTrue(){

    	// Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);
	    editor.commit();
	}
    
	// Store Selected Position of Doctor NavigationDrawer to SharedPreferences
	public void setSlctdPostnDctrNavdrwr(int slctd_postn_dctr_navdrwr){
	    
	    editor.putInt(KEY_SLCTD_POSTN_DCTR_NAVDRWR, slctd_postn_dctr_navdrwr);
	    editor.commit();
	}

    // Store Is Home status of Doctor NavigationDrawer to SharedPreferences
    public void setIsHomeDctrNavdrwr(boolean is_home_dctr_navdrwr){

        editor.putBoolean(KEY_ISHOME_DCTR_NAVDRWR, is_home_dctr_navdrwr);
        editor.commit();
    }
	
    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     * */
    public boolean checkLogin(){
        // Check login status
        if(!this.isUserLoggedIn()){
            
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, Login_Email.class);
            /*
            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            */
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            // Staring Login Activity
            _context.startActivity(i);
            
            return false;
        }
        return true;
    }
    
    public int getDrId(){
    	
    	int dr_id = pref.getInt(KEY_DR_ID, 0);
    	return dr_id;
    }
    
    public String getEmail(){
    	
    	String email = pref.getString(KEY_EMAIL, "");
    	return email;
    }
    
    public int getSlctdPostnDctrNavdrwr(){
    	
    	int slctd_postn_dctr_navdrwr = pref.getInt(KEY_SLCTD_POSTN_DCTR_NAVDRWR, 1);
    	return slctd_postn_dctr_navdrwr;
    }

    public boolean getIsHomeDctrNavdrwr(){

        boolean ishome_dctr_navdrwr = pref.getBoolean(KEY_ISHOME_DCTR_NAVDRWR, true);
        return ishome_dctr_navdrwr;
    }
    
    /**
     * Clear session details
     * */
    public void logoutUser(){
        
        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();
        
        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, Login_Email.class);
        
        /*// Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    } 
    
    // Check for login
    public boolean isUserLoggedIn(){
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}
