package com.maxfour.music.glide.palette;

import android.graphics.Bitmap;

import androidx.palette.graphics.Palette;
//khởi tạo đối tượng
public class BitmapPaletteWrapper {//Bang mau
    private final Bitmap mBitmap;
    private final Palette mPalette;

    public BitmapPaletteWrapper(Bitmap bitmap, Palette palette) {
        mBitmap = bitmap;
        mPalette = palette;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public Palette getPalette() {
        return mPalette;
    }
}
