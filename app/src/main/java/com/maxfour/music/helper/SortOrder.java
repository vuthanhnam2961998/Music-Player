/*
 * Copyright (C) 2012 Andrew Neal Licensed under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.maxfour.music.helper;

import android.provider.MediaStore;
/**
 *Sắp xếp theo thứ tự
 */
public final class SortOrder {

    /**
     * Lớp này không được khởi tạo
     */
    public SortOrder() {
    }

    /**
     * Nghệ sĩ sắp xếp các mục nhập.
     */
    public interface ArtistSortOrder {
        /* Thứ tự sắp xếp nghệ sĩ A-Z */
        String ARTIST_A_Z = MediaStore.Audio.Artists.DEFAULT_SORT_ORDER;

        /* Từ Z-A */
        String ARTIST_Z_A = ARTIST_A_Z + " DESC";

        /*Nghệ sĩ sắp xếp số lượng bài hát */
        String ARTIST_NUMBER_OF_SONGS = MediaStore.Audio.Artists.NUMBER_OF_TRACKS
                + " DESC";

        /* Số lượng album */
        String ARTIST_NUMBER_OF_ALBUMS = MediaStore.Audio.Artists.NUMBER_OF_ALBUMS
                + " DESC";
    }

    /**
     * Các mục sắp xếp album.
     */
    public interface AlbumSortOrder {
        /*Từ A-Z */
        String ALBUM_A_Z = MediaStore.Audio.Albums.DEFAULT_SORT_ORDER;

        /* Từ Z-A */
        String ALBUM_Z_A = ALBUM_A_Z + " DESC";

        /* Theo số bài hát */
        String ALBUM_NUMBER_OF_SONGS = MediaStore.Audio.Albums.NUMBER_OF_SONGS
                + " DESC";

        /* theo nghệ sĩ */
        String ALBUM_ARTIST = MediaStore.Audio.Artists.DEFAULT_SORT_ORDER
                + ", " + MediaStore.Audio.Albums.DEFAULT_SORT_ORDER;

        /* Theo năm */
        String ALBUM_YEAR = MediaStore.Audio.Media.YEAR + " DESC";
    }

    /**
     * Sắp xếp theo bài hát
     */
    public interface SongSortOrder {
        /* Từ A->Z */
        String SONG_A_Z = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;

        /* Z->A */
        String SONG_Z_A = SONG_A_Z + " DESC";

        /* theo nghệ sĩ */
        String SONG_ARTIST = MediaStore.Audio.Artists.DEFAULT_SORT_ORDER;

        /* theo album */
        String SONG_ALBUM = MediaStore.Audio.Albums.DEFAULT_SORT_ORDER;

        /* theo năm */
        String SONG_YEAR = MediaStore.Audio.Media.YEAR + " DESC";

        /* theo thời gian */
        String SONG_DURATION = MediaStore.Audio.Media.DURATION + " DESC";

        /* Theo ngày */
        String SONG_DATE = MediaStore.Audio.Media.DATE_ADDED + " DESC";
    }

    /**
     * sắp xếp bài hát
     */
    public interface AlbumSongSortOrder {
        /*  A-Z */
        String SONG_A_Z = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;

        /* Z-A */
        String SONG_Z_A = SONG_A_Z + " DESC";

        /* bài hát */
        String SONG_TRACK_LIST = MediaStore.Audio.Media.TRACK + ", "
                + MediaStore.Audio.Media.DEFAULT_SORT_ORDER;

        /* Thời gian */
        String SONG_DURATION = SongSortOrder.SONG_DURATION;
    }

    /**
     * nghệ sĩ
     */
    public interface ArtistSongSortOrder {
        /*  A-Z */
        String SONG_A_Z = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;

        /*  Z-A */
        String SONG_Z_A = SONG_A_Z + " DESC";

        /* album */
        String SONG_ALBUM = MediaStore.Audio.Media.ALBUM;

        /* năm */
        String SONG_YEAR = MediaStore.Audio.Media.YEAR + " DESC";

        /* Artist song sort order duration */
        String SONG_DURATION = MediaStore.Audio.Media.DURATION + " DESC";

        /* ngày */
        String SONG_DATE = MediaStore.Audio.Media.DATE_ADDED + " DESC";
    }

    /**
     * album.
     */
    public interface ArtistAlbumSortOrder {
        /* A-Z */
        String ALBUM_A_Z = MediaStore.Audio.Albums.DEFAULT_SORT_ORDER;

        /*  Z-A */
        String ALBUM_Z_A = ALBUM_A_Z + " DESC";

        /*năm */
        String ALBUM_YEAR = MediaStore.Audio.Media.YEAR
                + " DESC";

        /* năm */
        String ALBUM_YEAR_ASC = MediaStore.Audio.Media.YEAR
                + " ASC";
    }

    /**
     * theo thể loại
     */
    public interface GenreSortOrder {
        /*A-Z */
        String GENRE_A_Z = MediaStore.Audio.Genres.DEFAULT_SORT_ORDER;

        /*Z-A */
        String ALBUM_Z_A = GENRE_A_Z + " DESC";
    }

}
