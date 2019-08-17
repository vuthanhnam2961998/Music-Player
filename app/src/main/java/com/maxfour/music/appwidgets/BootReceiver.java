package com.maxfour.music.appwidgets;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.maxfour.music.service.MusicService;

public class BootReceiver extends BroadcastReceiver {//Bộ thu khởi động
    @Override
    public void onReceive(final Context context, Intent intent) {
        final AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);

        // Bắt đầu dịch vụ âm nhạc nếu có bất kỳ vật dụng hiện có nào
        if (widgetManager.getAppWidgetIds(new ComponentName(context, AppWidgetBig.class)).length > 0 ||
                widgetManager.getAppWidgetIds(new ComponentName(context, AppWidgetClassic.class)).length > 0 ||
                widgetManager.getAppWidgetIds(new ComponentName(context, AppWidgetSmall.class)).length > 0 ||
                widgetManager.getAppWidgetIds(new ComponentName(context, AppWidgetCard.class)).length > 0) {
            final Intent serviceIntent = new Intent(context, MusicService.class);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) { //không được phép trên Oreo
                context.startService(serviceIntent);
            }
        }
    }
}
