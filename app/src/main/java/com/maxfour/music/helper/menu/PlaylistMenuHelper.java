package com.maxfour.music.helper.menu;

import android.app.Activity;
import android.content.Context;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.maxfour.music.App;
import com.maxfour.music.R;
import com.maxfour.music.dialogs.AddToPlaylistDialog;
import com.maxfour.music.dialogs.DeletePlaylistDialog;
import com.maxfour.music.dialogs.RenamePlaylistDialog;
import com.maxfour.music.helper.MusicPlayerRemote;
import com.maxfour.music.loader.PlaylistSongLoader;
import com.maxfour.music.misc.WeakContextAsyncTask;
import com.maxfour.music.model.AbsCustomPlaylist;
import com.maxfour.music.model.Playlist;
import com.maxfour.music.model.Song;
import com.maxfour.music.util.PlaylistsUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistMenuHelper {
    public static boolean handleMenuClick(@NonNull AppCompatActivity activity, @NonNull final Playlist playlist, @NonNull MenuItem item) {
        switch (item.getItemId()) {
            // chơi nhạc
            case R.id.action_play:
                MusicPlayerRemote.openQueue(new ArrayList<>(getPlaylistSongs(activity, playlist)), 0, true);
                return true;
            // phát bài hát này tiếp theo
            case R.id.action_play_next:
                MusicPlayerRemote.playNext(new ArrayList<>(getPlaylistSongs(activity, playlist)));
                return true;
            //thêm vào danh sách đang phát
            case R.id.action_add_to_current_playing:
                MusicPlayerRemote.enqueue(new ArrayList<>(getPlaylistSongs(activity, playlist)));
                return true;
            // thêm vào danh sách
            case R.id.action_add_to_playlist:
                AddToPlaylistDialog.create(new ArrayList<>(getPlaylistSongs(activity, playlist))).show(activity.getSupportFragmentManager(), "ADD_PLAYLIST");
                return true;
            // đổi tên danh sách bài hát
            case R.id.action_rename_playlist:
                RenamePlaylistDialog.create(playlist.id).show(activity.getSupportFragmentManager(), "RENAME_PLAYLIST");
                return true;
            // xoá danh sách bài hát
            case R.id.action_delete_playlist:
                DeletePlaylistDialog.create(playlist).show(activity.getSupportFragmentManager(), "DELETE_PLAYLIST");
                return true;
             // lưu lại
            case R.id.action_save_playlist:
                new SavePlaylistAsyncTask(activity).execute(playlist);
                return true;
        }
        return false;
    }

    @NonNull
    private static List<? extends Song> getPlaylistSongs(@NonNull Activity activity, Playlist playlist) {
        return playlist instanceof AbsCustomPlaylist ?
                ((AbsCustomPlaylist) playlist).getSongs(activity) :
                PlaylistSongLoader.getPlaylistSongList(activity, playlist.id);
    }


    private static class SavePlaylistAsyncTask extends WeakContextAsyncTask<Playlist, String, String> {
        public SavePlaylistAsyncTask(Context context) {
            super(context);
        }

        //kiểm tra đã lưu thành công hay chưa
        @Override
        protected String doInBackground(Playlist... params) {
            // nếu thành công thì sẽ lưu vào playlist
            try
            {
                return String.format(App.getInstance().getApplicationContext().getString(R.string.saved_playlist_to), PlaylistsUtil.savePlaylist(App.getInstance().getApplicationContext(), params[0]));
            }
            // ngược lại thì sẽ thông báo lưu thất bại
            catch (IOException e)
            {
                e.printStackTrace();
                return String.format(App.getInstance().getApplicationContext().getString(R.string.failed_to_save_playlist), e);
            }
        }

        // thông báo cho người dùng biết là đã lưu thành công hay chưa, phương thức LENGTH_LONG mang lại thời gian đủ lâu cho người dùng đọc hết nội dung thông báo
        @Override
        protected void onPostExecute(String string)
        {
            super.onPostExecute(string);
            Context context = getContext();
            if (context != null) {
                Toast.makeText(context, string, Toast.LENGTH_LONG).show();
            }
        }
    }
}
