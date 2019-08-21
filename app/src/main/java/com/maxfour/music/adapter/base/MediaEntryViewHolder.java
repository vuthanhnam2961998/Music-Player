package com.maxfour.music.adapter.base;

import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.maxfour.music.R;

import butterknife.BindView;
import butterknife.ButterKnife;
//baseus là các cơ sở
//ViewHolder mô tả chế độ xem mục và siêu dữ liệu về vị trí của nó trong RecyclerView.
//View.OnClickLister bật chế độ gọi lại được gọi khi nhấp
//View.OnLongClickListener bật chế độ gọi lại được gọi khi nhấp và giữ
public class MediaEntryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    @Nullable //Nó làm rõ rằng phương thức chấp nhận các giá trị null và nếu bạn ghi đè phương thức.
    // Bạn cũng nên chấp nhận các giá trị null.
    @BindView(R.id.image)
    public ImageView image;

    @Nullable
    @BindView(R.id.image_text)
    public TextView imageText;

    @Nullable
    @BindView(R.id.title)
    public TextView title;

    @Nullable
    @BindView(R.id.text)
    public TextView text;

    @Nullable
    @BindView(R.id.menu)
    public View menu;

    @Nullable
    @BindView(R.id.separator)
    public View separator;

    @Nullable
    @BindView(R.id.short_separator)
    public View shortSeparator;

    @Nullable
    @BindView(R.id.drag_view)
    public View dragView;

    @Nullable
    @BindView(R.id.palette_color_container)
    public View paletteColorContainer;

    public MediaEntryViewHolder(View itemView) {
        super(itemView);
        //Không chỉ hỗ trợ cho Activity và Fragment mà Butter Knife còn hỗ trợ đối với cả
        // ViewHolder bằng việc sử dụng câu lệnh bind() trong hàm khởi tạo của ViewHolder
        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    protected void setImageTransitionName(@NonNull String transitionName) {//Chuyển đổi tên hình ảnh
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && image != null) {
            image.setTransitionName(transitionName);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onClick(View v) {

    }
}
