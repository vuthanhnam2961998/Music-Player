package com.maxfour.music.appshortcuts;

import android.content.Context;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.TypedValue;

import androidx.annotation.RequiresApi;
import androidx.core.graphics.drawable.IconCompat;

import com.kabouzeid.appthemehelper.ThemeStore;
import com.maxfour.music.R;
import com.maxfour.music.util.ImageUtil;
import com.maxfour.music.util.PreferenceUtil;
//Trình tạo biểu tượng lối tắt ứng dụng
@RequiresApi(Build.VERSION_CODES.N_MR1)
public final class AppShortcutIconGenerator {

    public static Icon generateThemedIcon(Context context, int iconId) {//Tạo biểu tượng theo chủ đề
        if (PreferenceUtil.getInstance(context).coloredAppShortcuts()) {//getInstance(context) trả về đối tượng chữ kí đã được chỉ định
            return generateUserThemedIcon(context, iconId).toIcon();//Tạo biểu tượng người dùng
        } else {
            return generateDefaultThemedIcon(context, iconId).toIcon();//Tạo biểu tượng mặc định
        }
    }

    private static IconCompat generateDefaultThemedIcon(Context context, int iconId) {
        // Trả lại Biểu tượng của iconId với màu mặc định
        return generateThemedIcon(context, iconId,
                context.getColor(R.color.app_shortcut_default_foreground),
                context.getColor(R.color.app_shortcut_default_background)
        );
    }

    private static IconCompat generateUserThemedIcon(Context context, int iconId) {
        // Nhận màu nền từ chủ đề bối cảnh
        final TypedValue typedColorBackground = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.colorBackground, typedColorBackground, true);

        // Trả lại một Biểu tượng của iconId với các màu đó
        return generateThemedIcon(context, iconId,
                ThemeStore.accentColor(context),
                typedColorBackground.data
        );
    }

    private static IconCompat generateThemedIcon(Context context, int iconId, int foregroundColor, int backgroundColor) {
        // Nhận và tô màu cho các bản vẽ nền trước và nền
        Drawable vectorDrawable = ImageUtil.getTintedVectorDrawable(context, iconId, foregroundColor);
        Drawable backgroundDrawable = ImageUtil.getTintedVectorDrawable(context, R.drawable.ic_app_shortcut_background, backgroundColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AdaptiveIconDrawable adaptiveIconDrawable = new AdaptiveIconDrawable(backgroundDrawable, vectorDrawable);
            return IconCompat.createWithAdaptiveBitmap(ImageUtil.createBitmap(adaptiveIconDrawable));
        } else {
            // kết hợp cả 2 lại với nhau
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{backgroundDrawable, vectorDrawable});

            //Quay trở lại như một Icon
            return IconCompat.createWithBitmap(ImageUtil.createBitmap(layerDrawable));
        }
    }
}
