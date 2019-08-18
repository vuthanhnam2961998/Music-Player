package com.maxfour.music.helper.menu;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.maxfour.music.R;
import com.maxfour.music.dialogs.AddToPlaylistDialog;
import com.maxfour.music.dialogs.DeleteSongsDialog;
import com.maxfour.music.dialogs.SongDetailDialog;
import com.maxfour.music.helper.MusicPlayerRemote;
import com.maxfour.music.interfaces.PaletteColorHolder;
import com.maxfour.music.model.Song;
import com.maxfour.music.ui.activities.tageditor.AbsTagEditorActivity;
import com.maxfour.music.ui.activities.tageditor.SongTagEditorActivity;
import com.maxfour.music.util.MusicUtil;
import com.maxfour.music.util.NavigationUtil;
import com.maxfour.music.util.RingtoneManager;

public class SongMenuHelper {
    public static final int MENU_RES = R.menu.menu_item_song;

    public static boolean handleMenuClick(@NonNull FragmentActivity activity, @NonNull Song song, int menuItemId) {
        switch (menuItemId)
        {
            // đặt làm nhạc chuông
            case R.id.action_set_as_ringtone:
                if (RingtoneManager.requiresDialog(activity)) {
                    RingtoneManager.showDialog(activity);
                } else {
                    RingtoneManager ringtoneManager = new RingtoneManager();
                    ringtoneManager.setRingtone(activity, song.id);
                }
                return true;
            // chia sẽ bài hát
            case R.id.action_share:
                activity.startActivity(Intent.createChooser(MusicUtil.createShareSongFileIntent(song, activity), null));
                return true;
            // xoá bài hát khỏi thiết bị
            case R.id.action_delete_from_device:
                DeleteSongsDialog.create(song).show(activity.getSupportFragmentManager(), "DELETE_SONGS");
                return true;
            // thêm vào danh sách nhạc
            case R.id.action_add_to_playlist:
                AddToPlaylistDialog.create(song).show(activity.getSupportFragmentManager(), "ADD_PLAYLIST");
                return true;
            // phát bài hát này tiếp theo
            case R.id.action_play_next:
                MusicPlayerRemote.playNext(song);
                return true;
            // thêm vào danh sách đang phát hiện tại
            case R.id.action_add_to_current_playing:
                MusicPlayerRemote.enqueue(song);
                return true;
             // tag editor
            case R.id.action_tag_editor:
                Intent tagEditorIntent = new Intent(activity, SongTagEditorActivity.class);
                tagEditorIntent.putExtra(AbsTagEditorActivity.EXTRA_ID, song.id);
                // lấy id của bài hát và đưa vào activity tag activity intent để sửa và lưu lại
                if (activity instanceof PaletteColorHolder)
                    tagEditorIntent.putExtra(AbsTagEditorActivity.EXTRA_PALETTE, ((PaletteColorHolder) activity).getPaletteColor());
                activity.startActivity(tagEditorIntent);
                return true;
            // chi tiết bài hát
            case R.id.action_details:
                SongDetailDialog.create(song).show(activity.getSupportFragmentManager(), "SONG_DETAILS");
                return true;
            // đi đến album, sẽ lấy id của album và mở danh mục album này lên
            case R.id.action_go_to_album:
                NavigationUtil.goToAlbum(activity, song.albumId);
                return true;
            // đi đên nghệ sĩ, sẽ lấy id của nghệ sĩ này và mở danh mục nghệ sĩ này lên
            case R.id.action_go_to_artist:
                NavigationUtil.goToArtist(activity, song.artistId);
                return true;
        }
        return false;
    }

    public static abstract class OnClickSongMenu implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        private AppCompatActivity activity;

        // nếu như không có hành động thì sẽ không có gì thay đổi vẫn là activity này
        public OnClickSongMenu(@NonNull AppCompatActivity activity) {
            this.activity = activity;
        }

        // gọi menu item song ra.
        public int getMenuRes() {
            return MENU_RES;
        }

        // menu popup hiện ra và click chọn trên đó
        @Override
        public void onClick(View v) {
            PopupMenu popupMenu = new PopupMenu(activity, v);
            popupMenu.inflate(getMenuRes());
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        // hành động tương ứng việc chọn ở menu  trên sẽ được thực hiện.
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            return handleMenuClick(activity, getSong(), item.getItemId());
        }

        public abstract Song getSong();
    }
}
