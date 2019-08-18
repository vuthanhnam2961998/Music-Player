package com.maxfour.music;

import android.app.Application;
import android.os.Build;

import com.kabouzeid.appthemehelper.ThemeStore;
import com.maxfour.music.appshortcuts.DynamicShortcutManager;

public class App extends Application {

    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        //giao diện mặc định
        if (!ThemeStore.isConfigured(this, 1)) {
            ThemeStore.editTheme(this)
                    .primaryColorRes(R.color.md_blue_grey_700)
                    .accentColorRes(R.color.md_teal_A700)
                    .commit();
        }

        // thiết lập dynamic shortcut
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            new DynamicShortcutManager(this).initDynamicShortcuts();
        }
    }

    public static App getInstance() {
        return app;
    }
}
