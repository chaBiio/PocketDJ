package com.mouse.lion.pocketdj;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.ThemedSpinnerAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by lionm on 1/15/2018.
 */

public class CacheUtils {

    private static final String STORAGE = "com.mouse.lion.pocketdj.STORAGE";
    private static final String KEY_AUDIO_LIST = "CachedUtils:audioList";
    private static final String KEY_AUDIO_INDEX = "CachedUtils:audioIndex";
    public static final int INVALID_AUDIO_INDEX = -1;
    private SharedPreferences preferences;
    private Context context;

    public CacheUtils(Context context) {
        this.context = context;
    }

    public void cacheAudios(ArrayList<Audio> audioList) {
        preferences = this.context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(audioList);
        editor.putString(KEY_AUDIO_LIST, json);
        editor.apply();
    }

    public ArrayList<Audio> loadAudios() {
        preferences = this.context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString(KEY_AUDIO_LIST, null);
        Type type = new TypeToken<ArrayList<Audio>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public void cacheAudioIndex(int index) {
        preferences = this.context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_AUDIO_INDEX, index);
        editor.apply();
    }

    public int loadAudioIndex() {
        preferences = this.context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        return preferences.getInt(KEY_AUDIO_INDEX, INVALID_AUDIO_INDEX);
    }

    public void clearCachedAudioPlayList() {
        preferences = this.context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
