package com.qun.test.wisdombj.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.qun.test.wisdombj.App;

/**
 * Created by Administrator on 2018/4/4 0004.
 */

public class SPUtil {
    public static void putData(String key, Object value) {
        Class clazz = value.getClass();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(App.sContext);
        SharedPreferences.Editor editor = sp.edit();
        if (clazz == Integer.class) {
            editor.putInt(key, (Integer) value);
        } else if (clazz == String.class) {
            editor.putString(key, (String) value);
        } else if (clazz == Long.class) {
            editor.putLong(key, (Long) value);
        } else if (clazz == Float.class) {
            editor.putFloat(key, (Float) value);
        } else if (clazz == Boolean.class) {
            editor.putBoolean(key, (Boolean) value);
        } else {
            throw new IllegalArgumentException("参数不对");
        }
        editor.apply();
    }

    public static Object getData(String key, Object defaultValue) {
        Class clazz = defaultValue.getClass();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(App.sContext);
        if (clazz == Integer.class) {
            return sp.getInt(key, (Integer) defaultValue);
        } else if (clazz == String.class) {
            return sp.getString(key, (String) defaultValue);
        } else if (clazz == Long.class) {
            return sp.getLong(key, (Long) defaultValue);
        } else if (clazz == Float.class) {
            return sp.getFloat(key, (Float) defaultValue);
        } else if (clazz == Boolean.class) {
            return sp.getBoolean(key, (Boolean) defaultValue);
        } else {
            return null;
        }
    }
}
