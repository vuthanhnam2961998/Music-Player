package com.maxfour.music.preferences;

import android.content.Context;
import android.util.AttributeSet;

import com.kabouzeid.appthemehelper.common.prefs.supportv7.ATEDialogPreference;
//Khởi tạo màn hình âm nhạc
public class NowPlayingScreenPreference extends ATEDialogPreference {
    public NowPlayingScreenPreference(Context context) {
        super(context);
    }

    public NowPlayingScreenPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NowPlayingScreenPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NowPlayingScreenPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}