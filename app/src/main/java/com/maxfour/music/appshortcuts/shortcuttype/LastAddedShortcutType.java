package com.maxfour.music.appshortcuts.shortcuttype;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ShortcutInfo;
import android.os.Build;

import com.maxfour.music.R;
import com.maxfour.music.appshortcuts.AppShortcutIconGenerator;
import com.maxfour.music.appshortcuts.AppShortcutLauncherActivity;
//Thêm cuối trong danh sách
@TargetApi(Build.VERSION_CODES.N_MR1)
public final class LastAddedShortcutType extends BaseShortcutType {//Thêm cuối
    public LastAddedShortcutType(Context context) {
        super(context);
    }
    public static String getId() {
        return ID_PREFIX + "last_added";
    }
    public ShortcutInfo getShortcutInfo() {
        return new ShortcutInfo.Builder(context, getId())
                .setShortLabel(context.getString(R.string.app_shortcut_last_added_short))
                .setLongLabel(context.getString(R.string.last_added))
                .setIcon(AppShortcutIconGenerator.generateThemedIcon(context, R.drawable.ic_app_shortcut_last_added))
                .setIntent(getPlaySongsIntent(AppShortcutLauncherActivity.SHORTCUT_TYPE_LAST_ADDED))
                .build();
    }
}
