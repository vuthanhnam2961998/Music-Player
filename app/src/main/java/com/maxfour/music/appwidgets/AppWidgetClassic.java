package com.maxfour.music.appwidgets;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.kabouzeid.appthemehelper.util.MaterialValueHelper;
import com.maxfour.music.R;
import com.maxfour.music.appwidgets.base.BaseAppWidget;
import com.maxfour.music.glide.SongGlideRequest;
import com.maxfour.music.glide.palette.BitmapPaletteWrapper;
import com.maxfour.music.model.Song;
import com.maxfour.music.service.MusicService;
import com.maxfour.music.ui.activities.MainActivity;
import com.maxfour.music.util.ImageUtil;
//ApWidget Cô điển
public class AppWidgetClassic extends BaseAppWidget {
    public static final String NAME = "app_widget_classic";

    private static AppWidgetClassic mInstance;
    private static int imageSize = 0;
    private static float cardRadius = 0f;
    private Target<BitmapPaletteWrapper> target; // dành cho hủy bỏ

    //synchronized được sử dụng để bảo vệ quyền truy cập vào các tài nguyên được truy cập đồng thời(sử dụng nhiều luồng)
    public static synchronized AppWidgetClassic getInstance() {
        if (mInstance == null) {
            mInstance = new AppWidgetClassic();
        }
        return mInstance;
    }

    /**
     * Khởi tạo các widget đã cho thành trạng thái mặc định, nơi  khởi chạy Music on
     * nhấp chuột và ẩn hành động mặc định nếu dịch vụ không chạy.
     */
    protected void defaultAppWidget(final Context context, final int[] appWidgetIds) {
        final RemoteViews appWidgetView = new RemoteViews(context.getPackageName(), R.layout.app_widget_classic);

        appWidgetView.setViewVisibility(R.id.media_titles, View.INVISIBLE);
        appWidgetView.setImageViewResource(R.id.image, R.drawable.default_album_art);
        appWidgetView.setImageViewBitmap(R.id.button_next, ImageUtil.createBitmap(ImageUtil.getTintedVectorDrawable(context, R.drawable.ic_skip_next_white_24dp, MaterialValueHelper.getSecondaryTextColor(context, true))));
        appWidgetView.setImageViewBitmap(R.id.button_prev, ImageUtil.createBitmap(ImageUtil.getTintedVectorDrawable(context, R.drawable.ic_skip_previous_white_24dp, MaterialValueHelper.getSecondaryTextColor(context, true))));
        appWidgetView.setImageViewBitmap(R.id.button_toggle_play_pause, ImageUtil.createBitmap(ImageUtil.getTintedVectorDrawable(context, R.drawable.ic_play_arrow_white_24dp, MaterialValueHelper.getSecondaryTextColor(context, true))));

        linkButtons(context, appWidgetView);
        pushUpdate(context, appWidgetIds, appWidgetView);
    }

    /**
     *
     * Cập nhật tất cả các trường hợp tiện ích hoạt động bằng cách đẩy các thay đổi
     */
    public void performUpdate(final MusicService service, final int[] appWidgetIds) {
        final RemoteViews appWidgetView = new RemoteViews(service.getPackageName(), R.layout.app_widget_classic);

        final boolean isPlaying = service.isPlaying();
        final Song song = service.getCurrentSong();

        //Đặt tiêu đề và tác phẩm nghệ thuật
        if (TextUtils.isEmpty(song.title) && TextUtils.isEmpty(song.artistName)) {
            appWidgetView.setViewVisibility(R.id.media_titles, View.INVISIBLE);
        } else {
            appWidgetView.setViewVisibility(R.id.media_titles, View.VISIBLE);
            appWidgetView.setTextViewText(R.id.title, song.title);
            appWidgetView.setTextViewText(R.id.text, getSongArtistAndAlbum(song));
        }

        // Liên kết với các nút intents
        linkButtons(service, appWidgetView);

        if (imageSize == 0)
            imageSize = service.getResources().getDimensionPixelSize(R.dimen.app_widget_classic_image_size);
        if (cardRadius == 0f)
            cardRadius = service.getResources().getDimension(R.dimen.app_widget_card_radius);

        // Load the album cover async and push the update on completion
        final Context appContext = service.getApplicationContext();
        service.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (target != null) {
                    Glide.clear(target);
                }
                target = SongGlideRequest.Builder.from(Glide.with(appContext), song)
                        .checkIgnoreMediaStore(appContext)
                        .generatePalette(service).build()
                        .centerCrop()
                        .into(new SimpleTarget<BitmapPaletteWrapper>(imageSize, imageSize) {
                            @Override
                            public void onResourceReady(BitmapPaletteWrapper resource, GlideAnimation<? super BitmapPaletteWrapper> glideAnimation) {
                                Palette palette = resource.getPalette();
                                update(resource.getBitmap(), palette.getVibrantColor(palette.getMutedColor(MaterialValueHelper.getSecondaryTextColor(appContext, true))));
                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                update(null, MaterialValueHelper.getSecondaryTextColor(appContext, true));
                            }

                            private void update(@Nullable Bitmap bitmap, int color) {
                                // Set correct drawable for pause state
                                int playPauseRes = isPlaying ? R.drawable.ic_pause_white_24dp : R.drawable.ic_play_arrow_white_24dp;
                                appWidgetView.setImageViewBitmap(R.id.button_toggle_play_pause, ImageUtil.createBitmap(ImageUtil.getTintedVectorDrawable(service, playPauseRes, color)));

                                // Set prev/next button drawables
                                appWidgetView.setImageViewBitmap(R.id.button_next, ImageUtil.createBitmap(ImageUtil.getTintedVectorDrawable(service, R.drawable.ic_skip_next_white_24dp, color)));
                                appWidgetView.setImageViewBitmap(R.id.button_prev, ImageUtil.createBitmap(ImageUtil.getTintedVectorDrawable(service, R.drawable.ic_skip_previous_white_24dp, color)));

                                final Drawable image = getAlbumArtDrawable(service.getResources(), bitmap);
                                final Bitmap roundedBitmap = createRoundedBitmap(image, imageSize, imageSize, cardRadius, 0, cardRadius, 0);
                                appWidgetView.setImageViewBitmap(R.id.image, roundedBitmap);

                                pushUpdate(appContext, appWidgetIds, appWidgetView);
                            }
                        });
            }
        });
    }

    /**
     * Link up various button actions using {@link PendingIntent}.
     */
    private void linkButtons(final Context context, final RemoteViews views) {
        Intent action;
        PendingIntent pendingIntent;

        final ComponentName serviceName = new ComponentName(context, MusicService.class);

        // Home
        action = new Intent(context, MainActivity.class);
        action.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(context, 0, action, 0);
        views.setOnClickPendingIntent(R.id.image, pendingIntent);
        views.setOnClickPendingIntent(R.id.media_titles, pendingIntent);

        // Previous song
        pendingIntent = buildPendingIntent(context, MusicService.ACTION_REWIND, serviceName);
        views.setOnClickPendingIntent(R.id.button_prev, pendingIntent);

        // Play and pause
        pendingIntent = buildPendingIntent(context, MusicService.ACTION_TOGGLE_PAUSE, serviceName);
        views.setOnClickPendingIntent(R.id.button_toggle_play_pause, pendingIntent);

        // Next song
        pendingIntent = buildPendingIntent(context, MusicService.ACTION_SKIP, serviceName);
        views.setOnClickPendingIntent(R.id.button_next, pendingIntent);
    }
}
