package com.tongdaxing.xchat_framework.util.util.pref;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * Rule : input key cannot be null.
 * 
 */
public class CommonPref extends YSharedPref {

    private static CommonPref sInst;

    private CommonPref(SharedPreferences preferences){
        super(preferences);
    }

    public synchronized static CommonPref instance(Context applicationContext) {
        if(sInst == null){
            SharedPreferences pref = applicationContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            sInst = new CommonPref(pref);
        }
        return sInst;
    }
}
