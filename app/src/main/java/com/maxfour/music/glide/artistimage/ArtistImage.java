package com.maxfour.music.glide.artistimage;

import java.util.List;

public class ArtistImage {
    public final String artistName;

    // filePath để có được hình ảnh của nghệ sĩ
    public final List<AlbumCover> albumCovers;

    public ArtistImage(String artistName, final List<AlbumCover> albumCovers) {
        this.artistName = artistName;
        this.albumCovers = albumCovers;
    }

    public String toIdString() {
        StringBuilder id = new StringBuilder(artistName);
        for (AlbumCover albumCover: albumCovers) {
            id.append(albumCover.getYear()).append(albumCover.getFilePath());
        }
        return id.toString();
    }
}
