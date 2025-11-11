package com.tongdaxing.xchat_framework.util.util.pref;

import android.content.Context;
import android.content.SharedPreferences;

public class ObjectPref extends XSharedPref
{
	private static ObjectPref sInst;

	private ObjectPref(SharedPreferences preferences){
		super(preferences);
	}

	public synchronized static ObjectPref instance(Context applicationContext) {
		if(sInst == null){
			SharedPreferences pref = applicationContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
			sInst = new ObjectPref(pref);
		}
		return sInst;
	}

}
