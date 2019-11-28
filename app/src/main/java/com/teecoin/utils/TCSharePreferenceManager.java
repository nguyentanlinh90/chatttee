package com.teecoin.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.teecoin.base.TCApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class TCSharePreferenceManager {

    private static final String KEY_SHARED_PREFERENCES = TCUtils.getSharedPreferenceKey();

    private static TCSharePreferenceManager instance;
    private final SharedPreferences prefs;

    private TCSharePreferenceManager() {
        prefs = TCApplication.getContext().getSharedPreferences(
                KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static TCSharePreferenceManager getInstance() {
        if (instance == null){
            synchronized (TCSharePreferenceManager.class){
                if (instance == null) {
                    instance = new TCSharePreferenceManager();
                }
            }
        }
        return instance;
    }

    public synchronized boolean clear() {
        if (prefs != null)
            return prefs.edit().clear().commit();
        return false;
    }

    public synchronized void clearByKey(DataKey key) {
        if(prefs!=null){
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove(key.name());
            editor.apply();
        }

    }

    public synchronized boolean clear(DataKey... ignores) {
        if (prefs != null) {
            SharedPreferences.Editor edit = prefs.edit();
            HashMap<DataKey, Object> reserved = new HashMap<>();
            for (DataKey key : ignores) {
                Object data = prefs.getAll().get(key.name());
                reserved.put(key, data);
            }
            boolean cleared = edit.clear().commit();
            if (cleared) {
                for (DataKey key : reserved.keySet()) {
                    Object data = reserved.get(key);
                    if (data instanceof Integer) {
                        edit.putInt(key.name(), (int) data);
                    } else if (data instanceof String) {
                        edit.putString(key.name(), (String) data);
                    } else if (data instanceof Float) {
                        edit.putFloat(key.name(), (float) data);
                    } else if (data instanceof Boolean) {
                        edit.putBoolean(key.name(), (boolean) data);
                    } else if (data instanceof Long) {
                        edit.putLong(key.name(), (long) data);
                    } else if (data instanceof Set) {
                        edit.putStringSet(key.name(), (Set) data);
                    }
                }
                return edit.commit();
            } else
                return false;
        }
        return false;
    }

    public synchronized boolean setLong(DataKey key, long value) {
        if (prefs != null)
            return prefs.edit().putLong(key.name(), value).commit();
        return false;
    }

    public synchronized boolean setStringSet(DataKey key, Set<String> value) {
        if (prefs != null)
            return prefs.edit().putStringSet(key.name(), value).commit();
        return false;
    }

    public synchronized Set<String> getStringSet(DataKey key) {
        if (prefs != null)
            return prefs.getStringSet(key.name(), null);
        return null;
    }


    public synchronized long getLong(DataKey key) {
        if (prefs != null)
            return prefs.getLong(key.name(), 0);
        return 0;
    }

    public synchronized boolean setFloat(DataKey key, float value) {
        if (prefs != null)
            return prefs.edit().putFloat(key.name(), value).commit();
        return false;
    }

    public synchronized float getFloat(DataKey key) {
        if (prefs != null)
            return prefs.getFloat(key.name(), 0f);
        return 0f;
    }

    public synchronized boolean setString(DataKey key, String value) {
        if (prefs != null)
            return prefs.edit().putString(key.name(), value).commit();
        return false;
    }

    public synchronized boolean setString(String key, String value) {
        if (prefs != null)
            return prefs.edit().putString(key, value).commit();
        return false;
    }

    public synchronized String getString(DataKey key) {
        if (prefs != null)
            return prefs.getString(key.name(), "");
        return null;
    }

    public synchronized String getString(String key) {
        if (prefs != null)
            return prefs.getString(key, "");
        return null;
    }

    public synchronized int getInt(DataKey key) {
        if (prefs != null)
            return prefs.getInt(key.name(), 0);
        return 0;
    }

    public synchronized int getCategoryId(DataKey key) {
        if (prefs != null)
            return prefs.getInt(key.name(), -1);
        return -1;
    }

    public synchronized boolean setInt(DataKey key, int value) {
        if (prefs != null)
            return prefs.edit().putInt(key.name(), value).commit();
        return false;
    }

    public synchronized boolean setBoolean(DataKey key, boolean value) {
        if (prefs != null)
            return prefs.edit().putBoolean(key.name(), value).commit();
        return false;
    }

    public synchronized boolean getBoolean(DataKey key) {
        if (prefs != null)
            return prefs.getBoolean(key.name(), false);
        return false;
    }

    public void remove(DataKey key) {
        if (prefs != null)
            prefs.edit().remove(key.toString()).apply();
    }

    public void remove(String key) {
        if (prefs != null)
            prefs.edit().remove(key).apply();
    }


    public void removeKeyLike(String keyLikeString) {
        Map<String, ?> keys = prefs.getAll();
        for (String key : keys.keySet()) {
            if (key.contains(keyLikeString))
                prefs.edit().remove(key).apply();
        }
    }

}
