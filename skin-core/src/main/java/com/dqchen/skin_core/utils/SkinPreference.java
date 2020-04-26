package com.dqchen.skin_core.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 保存皮肤包的路径
 */
public class SkinPreference {

    private static final String SKIN_SHARD = "skin_shard";
    private static final String SKIN_PATH = "skin_path";
    private final SharedPreferences mPref;
    private static SkinPreference ourInstance;

    private SkinPreference(Context context) {
        mPref = context.getSharedPreferences(SKIN_SHARD, Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        if (null == ourInstance) {
            synchronized (SkinPreference.class) {
                if (null == ourInstance) {
                    ourInstance = new SkinPreference(context.getApplicationContext());
                }
            }
        }
    }

    public static SkinPreference getInstance() {
        return ourInstance;
    }

    public void setSkin(String path) {
        mPref.edit().putString(SKIN_PATH, path).apply();
    }

    public String getSkin() {
        return mPref.getString(SKIN_PATH, null);
    }
}
