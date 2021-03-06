package com.tempomena.tokenid;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by HP on 14/04/2018.
 */

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "FCMSharedPref";
    private static final String TAG_TOKEN = "tagtoken";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //this method will save the device token to shared preferences
    public boolean saveDeviceToken(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getDeviceToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_TOKEN, null);
    }
    public boolean saveTokenAdmin(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences("admin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("admin", token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getTokenAdmin(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences("admin", Context.MODE_PRIVATE);
        return  sharedPreferences.getString("admin", null);
    }

    public boolean savePostion(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences("pos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("postion", token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getPostion(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences("pos", Context.MODE_PRIVATE);
        return  sharedPreferences.getString("postion", null);
    }



    public boolean saveCountry(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences("country", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("con", token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getCountry(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences("country", Context.MODE_PRIVATE);
        return  sharedPreferences.getString("con", null);
    }

    public boolean saveCountryArabic(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences("countryy", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("connn", token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getCountryArabic(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences("countryy", Context.MODE_PRIVATE);
        return  sharedPreferences.getString("connn", null);
    }



    public boolean saveCountryId(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences("country_id", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getCountryId(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences("country_id", Context.MODE_PRIVATE);
        return  sharedPreferences.getString("id", null);
    }

    public boolean saveSocialId(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences("social", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("socia", token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getSocialId(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences("social", Context.MODE_PRIVATE);
        return  sharedPreferences.getString("socia", null);
    }
    public boolean saveMyName(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences("name", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getMyName(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences("name", Context.MODE_PRIVATE);
        return  sharedPreferences.getString("name", null);
    }

}
