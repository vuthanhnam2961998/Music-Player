package com.maxfour.music.glide.palette;

import android.widget.ImageView;

import com.bumptech.glide.request.target.ImageViewTarget;
//Bảng màu mục tiêu
public class BitmapPaletteTarget extends ImageViewTarget<BitmapPaletteWrapper> {
    public BitmapPaletteTarget(ImageView view) {
        super(view);
    }

    @Override
    protected void setResource(BitmapPaletteWrapper bitmapPaletteWrapper) {
        view.setImageBitmap(bitmapPaletteWrapper.getBitmap());
    }
}
