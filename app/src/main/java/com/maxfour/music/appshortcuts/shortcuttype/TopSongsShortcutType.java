package com.maxfour.music.appshortcuts.shortcuttype;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ShortcutInfo;
import android.os.Build;

import com.maxfour.music.R;
import com.maxfour.music.appshortcuts.AppShortcutIconGenerator;
import com.maxfour.music.appshortcuts.AppShortcutLauncherActivity;
//Chức năng top những bài hát trong danh sách
@TargetApi(Build.VERSION_CODES.N_MR1)
public final class TopSongsShortcutType extends BaseShortcutType {//Bài hát tìm kiếm hàng đầu trong đường đi ngắn nhất
    public TopSongsShortcutType(Context context) {
        super(context);
    }

    public static String getId() {
        return ID_PREFIX + "top_tracks";
    }

    public ShortcutInfo getShortcutInfo() {// Trả về thông tin của shortcut
        return new ShortcutInfo.Builder(context, getId())
                .setShortLabel(context.getString(R.string.app_shortcut_top_songs_short))
                .setLongLabel(context.getString(R.string.my_top_songs))
                .setIcon(AppShortcutIconGenerator.generateThemedIcon(context, R.drawable.ic_app_shortcut_top_songs))
                .setIntent(getPlaySongsIntent(AppShortcutLauncherActivity.SHORTCUT_TYPE_TOP_SONGS))
                .build();
    }
}
