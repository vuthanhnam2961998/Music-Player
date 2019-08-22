package com.maxfour.music.loader;

import android.database.AbstractCursor;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.HashMap;
//Sắp xếp Cursor
public class SortedCursor extends AbstractCursor {
    // con trỏ đến wrap
    private final Cursor mCursor;
    // biến khai báo để lấy các giá trị bên ngoài vào nội bộ
    private List<Integer> mOrderedPositions;
    // biến này chứa id khi không tìm thấy trong con trỏ bên dưới
    private List<String> mMissingValues;
    // biến này chưa vị trí của con trỏ được bổ sung các id không tìm thấy
    private HashMap<String, Integer> mMapCursorPositions;

    /**
     * @param cursor     to wrap @param order      sắp xếp danh sách các id duy nhất theo thứ tự được sắp xếp để hiển thị
     * @param columnName tên cột của id cần tra cứu trong con trỏ bên trong
     */
    public SortedCursor(@NonNull final Cursor cursor, @Nullable final String[] order, final String columnName) {
        mCursor = cursor;
        mMissingValues = buildCursorPositionMapping(order, columnName);
    }

    /**
     * Hàm này điền vào vị trí mOrderedP vị trí với vị trí con trỏ theo thứ tự trên thứ tự được truyền vào
     *
     * @param order thứ tự cuối cùng của con trỏ bên trong
     * @return trả về các id không tìm thấy trong con trỏ bên dưới
     */
    @NonNull
    private List<String> buildCursorPositionMapping(@Nullable final String[] order, final String columnName) {
        List<String> missingValues = new ArrayList<>();

        mOrderedPositions = new ArrayList<>(mCursor.getCount());

        mMapCursorPositions = new HashMap<>(mCursor.getCount());
        final int valueColumnIndex = mCursor.getColumnIndex(columnName);

        if (mCursor.moveToFirst()) {
            // tìm ra vị trí đầu tiên của mỗi id trong con trỏ
            do {
                mMapCursorPositions.put(mCursor.getString(valueColumnIndex), mCursor.getPosition());
            } while (mCursor.moveToNext());

            if (order != null) {
                // tạo các vị trí của con trỏ được chiếu đến bển con trỏ
                // thứ tự sắp xếp bên ngoài
                for (final String value : order) {
                    if (mMapCursorPositions.containsKey(value)) {
                        mOrderedPositions.add(mMapCursorPositions.get(value));
                        mMapCursorPositions.remove(value);
                    } else {
                        missingValues.add(value);
                    }
                }
            }

            mCursor.moveToFirst();
        }

        return missingValues;
    }

    /**
     * @return trả về danh sách id trong con trỏ bên dưới
     */
    public List<String> getMissingValues() {
        return mMissingValues;
    }

    /**
     * @return danh sách các id nằm trong con trỏ bên dưới nhưng lại không nằm trong danh sách đã được sắp xếp
     */
    @NonNull
    public Collection<String> getExtraValues() {
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
