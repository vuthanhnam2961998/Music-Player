package com.maxfour.music.glide.audiocover;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.Artwork;
//Sử dụng tập tin âm thanh
public class AudioFileCoverUtils {

    public static final String[] FALLBACKS = {"cover.jpg", "album.jpg", "folder.jpg", "cover.png", "album.png", "folder.png"};


    public static InputStream fallback(String path) throws FileNotFoundException {
        // Phương pháp 1: sử dụng nghệ thuật album có độ phân giải cao được nhúng nếu có
        try {
            MP3File mp3File = new MP3File(path);
            if (mp3File.hasID3v2Tag()) {
                Artwork art = mp3File.getTag().getFirstArtwork();
                if (art != null) {
                    byte[] imageData = art.getBinaryData();
                    return new ByteArrayInputStream(imageData);
                }
            }
            // Nếu có bất kỳ trường hợp ngoại lệ nào, bỏ qua chúng và tiếp tục phương thức dự phòng khác
        } catch (ReadOnlyFileException ignored) {
        } catch (InvalidAudioFrameException ignored) {
        } catch (TagException ignored) {
        } catch (IOException ignored) {
        }

        // Cách 2: tìm album art trong các tệp bên ngoài
        final File parent = new File(path).getParentFile();
        for (String fallback : FALLBACKS) {
            File cover = new File(parent, fallback);
            if (cover.exists()) {
                return new FileInputStream(cover);
            }
        }
        return null;
    }
}