package com.maxfour.music.appshortcuts.shortcuttype;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.os.Build;
import android.os.Bundle;

import com.maxfour.music.appshortcuts.AppShortcutLauncherActivity;
//Phím tắt cho tất cả các đối tượng
@TargetApi(Build.VERSION_CODES.N_MR1)
public abstract class BaseShortcutType {//Phim tắt cơ sở

    static final String ID_PREFIX = "com.maxfour.music.appshortcuts.id.";

    Context context;

    public BaseShortcutType(Context context) {
        this.context = context;
    }

    static public String getId() {
        return ID_PREFIX + "invalid";
    }

    abstract ShortcutInfo getShortcutInfo();

    /**
     *
     Tạo một ý định sẽ khởi chạy MainActivtiy và ngay lập tức phát
     {@param songs} ở chế độ phát ngẫu nhiên hoặc bình thường
     *
     * @param shortcutType mô tả phím tắt
     * @return
     */
    Intent getPlaySongsIntent(int shortcutType) {
        Intent intent = new Intent(context, AppShortcutLauncherActivity.class);
        intent.setAction(Intent.ACTION_VIEW);

        Bundle b = new Bundle();
        b.putInt(AppShortcutLauncherActivity.KEY_SHORTCUT_TYPE, shortcutType);

        intent.putExtras(b);

        return intent;
    }
}
