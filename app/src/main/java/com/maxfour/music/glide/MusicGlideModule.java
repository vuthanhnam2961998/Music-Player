package com.maxfour.music.glide;

import android.content.Context;

import java.io.InputStream;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.module.GlideModule;
import com.maxfour.music.glide.artistimage.ArtistImage;
import com.maxfour.music.glide.artistimage.ArtistImageLoader;
import com.maxfour.music.glide.audiocover.AudioFileCover;
import com.maxfour.music.glide.audiocover.AudioFileCoverLoader;
//Điều chỉnh âm thanh
public class MusicGlideModule implements GlideModule {

  	@Override
  	public void applyOptions(Context context, GlideBuilder builder) {
    }

    @Override
    public void registerComponents(Context context, Glide glide) {//Đăng kí các thành phần
        glide.register(AudioFileCover.class, InputStream.class, new AudioFileCoverLoader.Factory());
        glide.register(ArtistImage.class, InputStream.class, new ArtistImageLoader.Factory());
    }
}
