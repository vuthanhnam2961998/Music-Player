package com.maxfour.music.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RSRuntimeException;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.maxfour.music.BuildConfig;
import com.maxfour.music.helper.StackBlur;
import com.maxfour.music.util.ImageUtil;
//Làm mờ
public class BlurTransformation extends BitmapTransformation {
    public static final float DEFAULT_BLUR_RADIUS = 5f;

    private Context context;
    private float blurRadius;
    private int sampling;

    private void init(Builder builder) {//khởi tạo
        this.context = builder.context;
        this.blurRadius = builder.blurRadius;
        this.sampling = builder.sampling;
    }

    private BlurTransformation(Builder builder) {
        super(builder.context);
        init(builder);
    }

    private BlurTransformation(Builder builder, BitmapPool bitmapPool) {
        super(bitmapPool);
        init(builder);
    }

    public static class Builder {
        private Context context;
        private BitmapPool bitmapPool;
        private float blurRadius = DEFAULT_BLUR_RADIUS;
        private int sampling;

        public Builder(@NonNull Context context) {
            this.context = context;
        }

        /**
         * @param    blurRadius   để sử dụng. Phải nằm trong khoảng từ 0 đến 25. Mặc định là 5.
         * @return giống Builder
         */
        public Builder blurRadius(@FloatRange(from = 0.0f, to = 25.0f) float blurRadius) {
            this.blurRadius = blurRadius;
            return this;
        }

        /**
         * @param sampling Phải là công suất 2 hoặc 1 để không lấy mẫu xuống hoặc 0 để tự động phát hiện lấy mẫu. Mặc định là 0.
         * @return giống Builder
         */
        public Builder sampling(int sampling) {
            this.sampling = sampling;
            return this;
        }

        public Builder bitmapPool(BitmapPool bitmapPool) {
            this.bitmapPool = bitmapPool;
            return this;//-> Builder
        }

        public BlurTransformation build() {
            if (bitmapPool != null) {
                return new BlurTransformation(this, bitmapPool);
            }
            return new BlurTransformation(this);
        }
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        int sampling;
        if (this.sampling == 0) {
            sampling = ImageUtil.calculateInSampleSize(toTransform.getWidth(), toTransform.getHeight(), 100);
        } else {
            sampling = this.sampling;
        }

        int width = toTransform.getWidth();
        int height = toTransform.getHeight();
        int scaledWidth = width / sampling;
        int scaledHeight = height / sampling;

        Bitmap out = pool.get(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
        if (out == null) {
            out = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(out);
        canvas.scale(1 / (float) sampling, 1 / (float) sampling);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(toTransform, 0, 0, paint);

        if (Build.VERSION.SDK_INT >= 17) {
            try {
                final RenderScript rs = RenderScript.create(context.getApplicationContext());
                final Allocation input = Allocation.createFromBitmap(rs, out, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
                final Allocation output = Allocation.createTyped(rs, input.getType());
                final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

                script.setRadius(blurRadius);
                script.setInput(input);
                script.forEach(output);

                output.copyTo(out);

                rs.destroy();

                return out;

            } catch (RSRuntimeException e) {
                if (BuildConfig.DEBUG) e.printStackTrace();
            }
        }

        return StackBlur.blur(out, blurRadius);
    }

    @Override
    public String getId() {
        return "BlurTransformation(radius=" + blurRadius + ", sampling=" + sampling + ")";
    }
}
