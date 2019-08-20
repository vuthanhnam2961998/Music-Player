/*
* Copyright (C) 2014 The CyanogenMod Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.maxfour.music.loader;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.maxfour.music.model.Song;
import com.maxfour.music.provider.HistoryStore;
import com.maxfour.music.provider.SongPlayCountStore;

import java.util.ArrayList;
import java.util.List;

public class TopAndRecentlyPlayedSongsLoader {
    public static final int NUMBER_OF_TOP_SONGS = 100;

    @NonNull
    public static List<Song> getRecentlyPlayedSongs(@NonNull Context context) {
        return SongLoader.getSongs(makeRecentSongsCursorAndClearUpDatabase(context));
    }

    @NonNull
    public static List<Song> getTopSongs(@NonNull Context context) {
        return SongLoader.getSongs(makeTopSongsCursorAndClearUpDatabase(context));
    }

    @Nullable
    public static Cursor makeRecentSongsCursorAndClearUpDatabase(@NonNull final Context context) {
        SortedLongCursor retCursor = makeRecentSongsCursorImpl(context);

        // Xóa cơ sở dữ liệu không tìm thấy ids
        if (retCursor != null) {
            List<Long> missingIds = retCursor.getMissingIds();
            if (missingIds != null && missingIds.size() > 0) {
                for (long id : missingIds) {
                    HistoryStore.getInstance(context).removeSongId(id);
                }
            }
        }
        return retCursor;
    }

    @Nullable
    public static Cursor makeTopSongsCursorAndClearUpDatabase(@NonNull final Context context) {
        SortedLongCursor retCursor = makeTopSongsCursorImpl(context);

        // Xóa cơ sở dữ liệu không tìm thấy ids
        if (retCursor != null) {
            List<Long> missingIds = retCursor.getMissingIds();
            if (missingIds != null && missingIds.size() > 0) {
                for (long id : missingIds) {
                    SongPlayCountStore.getInstance(context).removeItem(id);
                }
            }
        }
        return retCursor;
    }

    @Nullable
    private static SortedLongCursor makeRecentSongsCursorImpl(@NonNull final Context context) {
        //đầu tiên nhận id kết quả đứng đầu từ cơ sở dữ liệu nội bộ
        Cursor songs = HistoryStore.getInstance(context).queryRecentIds();

        try {
            return makeSortedCursor(context, songs,
                    songs.getColumnIndex(HistoryStore.RecentStoreColumns.ID));
        } finally {
            if (songs != null) {
                songs.close();
            }
        }
    }

    @Nullable
    private static SortedLongCursor makeTopSongsCursorImpl(@NonNull final Context context) {
        // đầu tiên nhận id kết quả đứng đầu từ cơ sở dữ liệu nội bộ
        Cursor songs = SongPlayCountStore.getInstance(context).getTopPlayedResults(NUMBER_OF_TOP_SONGS);

        try {
            return makeSortedCursor(context, songs,
                    songs.getColumnIndex(SongPlayCountStore.SongPlayCountColumns.ID));
        } finally {
            if (songs != null) {
                songs.close();
            }
        }
    }

    @Nullable
    private static SortedLongCursor makeSortedCursor(@NonNull final Context context, @Nullable final Cursor cursor, final int idColumn) {
        if (cursor != null && cursor.moveToFirst()) {
            // tạo danh sách id để chọn
            StringBuilder selection = new StringBuilder();
            selection.append(BaseColumns._ID);
            selection.append(" IN (");

            //bài hát theo thứ tự của id
            long[] order = new long[cursor.getCount()];

            long id = cursor.getLong(idColumn);
            selection.append(id);
            order[cursor.getPosition()] = id;

            while (cursor.moveToNext()) {
                selection.append(",");

                id = cursor.getLong(idColumn);
                order[cursor.getPosition()] = id;
                selection.append(String.valueOf(id));
            }

            selection.append(")");

            // lấy danh sách các bài hát với dữ lịêu được đưa ra xác nhận lựa chọn
            Cursor songCursor = SongLoader.makeSongCursor(context, selection.toString(), null);
            if (songCursor != null) {
                // trả về TopSongsCursor để xử lý việc sắp xếp thứ tự cho trước
                return new SortedLongCursor(songCursor, order, BaseColumns._ID);
            }
        }

        return null;
    }
}
