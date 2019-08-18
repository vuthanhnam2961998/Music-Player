package com.maxfour.music.glide.audiocover;

import android.media.MediaMetadataRetriever;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
//Trình tải
public class AudioFileCoverFetcher implements DataFetcher<InputStream> {
    private final AudioFileCover model;

    private InputStream stream;

    public AudioFileCoverFetcher(AudioFileCover model) {
        this.model = model;
    }

    @Override
    public String getId() {
        // không null
        return String.valueOf(model.filePath);
    }

    @Override
    public InputStream loadData(final Priority priority) throws Exception {

        final MediaMetadataRetriever retriever = new MediaMetadataRetriever();//Lấy dữ liệu đa phương tiện
        try {
            retriever.setDataSource(model.filePath);
            byte[] picture = retriever.getEmbeddedPicture();
            if (picture != null) {
                stream = new ByteArrayInputStream(picture);
            } else {
                stream = AudioFileCoverUtils.fallback(model.filePath);
            }
        } finally {
            retriever.release();
        }
        return stream;
    }

    @Override
    public void cleanup() {
        // đã được dọn sạch trong loadData và ByteArrayInputStream sẽ là GC'd
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException ignore) {
            }
        }
    }

    @Override
    public void cancel() {
        // không hủy.
    }
}
