package com.maxfour.music.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.maxfour.music.R;
import com.maxfour.music.model.smartplaylist.AbsSmartPlaylist;
//Xóa hộp thoại danh sách phát thông minh
public class ClearSmartPlaylistDialog extends DialogFragment {

    @NonNull
    public static ClearSmartPlaylistDialog create(AbsSmartPlaylist playlist) {
        ClearSmartPlaylistDialog dialog = new ClearSmartPlaylistDialog();
        Bundle args = new Bundle();
        args.putParcelable("playlist", playlist);//Chèn một giá trị Parcelable vào ánh xạ của Bundle
        // thay thế bất kì giá trị nào của khóa đã cho
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //noinspection unchecked
        final AbsSmartPlaylist playlist = getArguments().getParcelable("playlist"); // trả về đối số liên kết với khóa
        int title = R.string.clear_playlist_title;
        //noinspection ConstantConditions
        CharSequence content = Html.fromHtml(getString(R.string.clear_playlist_x, playlist.name)); //Xóa danh sách

        return new MaterialDialog.Builder(getActivity())
                .title(title)
                .content(content)
                .positiveText(R.string.clear_action)
                .negativeText(android.R.string.cancel)
                .onPositive((dialog, which) -> {
                    if (getActivity() == null) {
                        return;
                    }
                    playlist.clear(getActivity());
                })
                .build();
    }
}
