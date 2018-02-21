package minkush.com.sqlitedemoapp.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by wingify on 21/02/18.
 */

public class SharePreference {

    public static void saveKeyValueSharePreference(Context context, String s_key, boolean s_value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(s_key,s_value);
        editor.apply();
    }

    public static boolean getKeyValueSharePreference(Context context,String s_key){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean s_value = preferences.getBoolean(s_key, false);

        return s_value;
    }

    public static class SharePrefrenceKeyConstant {

        public  static String s_key_twitter_access_token = "isApiDataHave";

    }
}
