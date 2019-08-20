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

import android.database.AbstractCursor;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
    * con trỏ này được bao bởi con trỏ bài hát và cung cấp một danh sách thứ tự các id của con trỏ\
    * Nó bao bọc con trỏ và mô phỏng con trỏ bên trong đang được sắp xếp bằng cách di chuyển đến vị trí thích hợp
 */
public class SortedLongCursor extends AbstractCursor {
    // con trỏ bao bọc
    private final Cursor mCursor;
    // bản đồ của các chỉ số bên ngoài đến các chỉ số bên trong
    private List<Integer> mOrderedPositions;
    // con trỏ này chứa các id không tìm thấy trong con trỏ bên dưới
    private List<Long> mMissingIds;
    // biến này chứa vị trí con trỏ được chiếu đến và sau đó bổ sung id không tìm thấy
    private HashMap<Long, Integer> mMapCursorPositions;

    /**
     * @param cursor     bao bọc
     * @param order      danh sách các id được sắp xếp theo thứ tự để hiển thị
     * @param columnName tên cột của id cần tìm trong con trỏ bên trong
     */
    public SortedLongCursor(final Cursor cursor, final long[] order, final String columnName) {

        mCursor = cursor;
        mMissingIds = buildCursorPositionMapping(order, columnName);
    }

    /*
        *Điền vào các vị trí theo thứ tự được thông qua
        * sắp xếp thứ tự cuối cùng của con trỏ
        * trả về các id không  tìm thấy
     */
    @NonNull
    private List<Long> buildCursorPositionMapping(@Nullable final long[] order, final String columnName) {
        List<Long> missingIds = new ArrayList<>();

        mOrderedPositions = new ArrayList<>(mCursor.getCount());

        mMapCursorPositions = new HashMap<>(mCursor.getCount());
        final int idPosition = mCursor.getColumnIndex(columnName);

        if (mCursor.moveToFirst()) {
           // tìm ra vị trí của tất cả id trong con trỏ
            do {
                mMapCursorPositions.put(mCursor.getLong(idPosition), mCursor.getPosition());
            } while (mCursor.moveToNext());

            //sắp xếp các vị trí trong con trỏ
            // thứ tự sắp xếp bên ngoài
            for (int i = 0; order != null && i < order.length; i++) {
                final long id = order[i];
                if (mMapCursorPositions.containsKey(id)) {
                    mOrderedPositions.add(mMapCursorPositions.get(id));
                    mMapCursorPositions.remove(id);
                } else {
                    missingIds.add(id);
                }
            }

            mCursor.moveToFirst();
        }

        return missingIds;
    }

    /**
     * @return trả về các id không tìm thấy trong con trỏ bên dưới
     */
    public List<Long> getMissingIds() {
        return mMissingIds;
    }

    /**
     * @return trả về danh sách các id nằm trong con trỏ bên dưới nhưng lại không nằm trong con trỏ đã được sắp xếp
     */
    @NonNull
    public Collection<Long> getExtraIds() {
        return mMapCursorPositions.keySet();
    }

    @Override
    public void close() {
        mCursor.close();

        super.close();
    }

    @Override
    public int getCount() {
        return mOrderedPositions.size();
    }

    @Override
    public String[] getColumnNames() {
        return mCursor.getColumnNames();
    }

    @Override
    public String getString(int column) {
        return mCursor.getString(column);
    }

    @Override
    public short getShort(int column) {
        return mCursor.getShort(column);
    }

    @Override
    public int getInt(int column) {
        return mCursor.getInt(column);
    }

    @Override
    public long getLong(int column) {
        return mCursor.getLong(column);
    }

    @Override
    public float getFloat(int column) {
        return mCursor.getFloat(column);
    }

    @Override
    public double getDouble(int column) {
        return mCursor.getDouble(column);
    }

    @Override
    public boolean isNull(int column) {
        return mCursor.isNull(column);
    }

    @Override
    public boolean onMove(int oldPosition, int newPosition) {
        if (newPosition >= 0 && newPosition < getCount()) {
            mCursor.moveToPosition(mOrderedPositions.get(newPosition));
            return true;
        }

        return false;
    }
}
