package com.tongdaxing.xchat_framework.util.util.pref;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Creator: 舒强睿
 * Date:2015/1/12
 * Time:14:13
 * <p/>
 * Description：
 */
public class SettingsPref extends YSharedPref {

    private static SettingsPref instanse;

    private SettingsPref(SharedPreferences preferences){
        super(preferences);
    }

    public SharedPreferences getSharePref() {
        return mPref;
    }

    public synchronized static SettingsPref instance(Context applicationContext) {
        if(instanse == null){
            SharedPreferences pref = applicationContext.getSharedPreferences("SettingsPrefs", Context.MODE_PRIVATE);
            instanse = new SettingsPref(pref);
        }
        return instanse;
    }

    public String getString(String key, String defaultValue) {
        return get(key) == null ? defaultValue : get(key);
    }
}
