package com.maxfour.music.adapter.album;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.bumptech.glide.Glide;
import com.kabouzeid.appthemehelper.util.ColorUtil;
import com.kabouzeid.appthemehelper.util.MaterialValueHelper;
import com.maxfour.music.R;
import com.maxfour.music.adapter.base.AbsMultiSelectAdapter;
import com.maxfour.music.adapter.base.MediaEntryViewHolder;
import com.maxfour.music.glide.MusicColoredTarget;
import com.maxfour.music.glide.SongGlideRequest;
import com.maxfour.music.helper.SortOrder;
import com.maxfour.music.helper.menu.SongsMenuHelper;
import com.maxfour.music.interfaces.CabHolder;
import com.maxfour.music.model.Album;
import com.maxfour.music.model.Song;
import com.maxfour.music.util.MusicUtil;
import com.maxfour.music.util.NavigationUtil;
import com.maxfour.music.util.PreferenceUtil;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.List;

public  class AlbumAdapter extends AbsMultiSelectAdapter<AlbumAdapter.ViewHolder, Album> implements FastScrollRecyclerView.SectionedAdapter {

    protected final AppCompatActivity activity;
    protected List<Album> dataSet;

    protected int itemLayoutRes;

    protected boolean usePalette = false;

    public AlbumAdapter(@NonNull AppCompatActivity activity, List<Album> dataSet, @LayoutRes int itemLayoutRes, boolean usePalette, @Nullable CabHolder cabHolder) {
        super(activity, cabHolder, R.menu.menu_media_selection); //Chuyen doi Adapter
        this.activity = activity;
        this.dataSet = dataSet;
        this.itemLayoutRes = itemLayoutRes;
        this.usePalette = usePalette;

        setHasStableIds(true);
    }

    public void usePalette(boolean usePalette)//Bang mau
    {
        this.usePalette = usePalette;
        notifyDataSetChanged();
    }

    public void swapDataSet(List<Album> dataSet) { //Chuyen doi Du Lieu
        this.dataSet = dataSet;
        notifyDataSetChanged(); //Chi hoat dong them,xoa,sua,chen vao Adapter
    }

    public List<Album> getDataSet() {
        return dataSet;
    } //Dua du lieu vao album

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//Chuyen doi tu xml sang View java code
        View view = LayoutInflater.from(activity).inflate(itemLayoutRes, parent, false);
        return createViewHolder(view, viewType);
    }

    protected ViewHolder createViewHolder(View view, int viewType) {
        return new ViewHolder(view);
    }//Tao view

    protected String getAlbumTitle(Album album) {
        return album.getTitle();
    }//Lay ten Album

    protected String getAlbumText(Album album) {
        return MusicUtil.buildInfoString(//Xay dung
            album.getArtistName(),
            MusicUtil.getSongCountString(activity, album.songs.size())
        );
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) { //Rang Buoc
        final Album album = dataSet.get(position);

        final boolean isChecked = isChecked(album);
        holder.itemView.setActivated(isChecked);

        if (holder.getAdapterPosition() == getItemCount() - 1) {
            if (holder.shortSeparator != null) {
                holder.shortSeparator.setVisibility(View.GONE);//An View hoan toan va thay the boi view khac
            }
        }
        else {
            if (holder.shortSeparator != null) {
                holder.shortSeparator.setVisibility(View.VISIBLE);//Hien View bi an len tren man hinh

            }
        }
//Sua doi
        if (holder.title != null) {
            holder.title.setText(getAlbumTitle(album));
        }
        if (holder.text != null) {
            holder.text.setText(getAlbumText(album));
        }

        loadAlbumCover(album, holder);
    }

    protected void setColors(int color, ViewHolder holder) {//Mau sac
        if (holder.paletteColorContainer != null) {
            holder.paletteColorContainer.setBackgroundColor(color);
            if (holder.title != null) {
                holder.title.setTextColor(MaterialValueHelper.getPrimaryTextColor(activity, ColorUtil.isColorLight(color)));
            }
            if (holder.text != null) {
                holder.text.setTextColor(MaterialValueHelper.getSecondaryTextColor(activity, ColorUtil.isColorLight(color)));
            }
        }
    }

    protected void loadAlbumCover(Album album, final ViewHolder holder) {
        if (holder.image == null) return;

        SongGlideRequest.Builder.from(Glide.with(activity), album.safeGetFirstSong())
                .checkIgnoreMediaStore(activity)
                .generatePalette(activity).build()
                .into(new MusicColoredTarget(holder.image) {
                    @Override
                    public void onLoadCleared(Drawable placeholder) {//Cai mau
                        super.onLoadCleared(placeholder);
                        setColors(getDefaultFooterColor(), holder);
                    }

                    @Override
                    public void onColorReady(int color) {//Bat bang mau
                        if (usePalette)
                            setColors(color, holder);
                        else
                            setColors(getDefaultFooterColor(), holder);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }//Them hinh

    @Override
    public long getItemId(int position) {
        return dataSet.get(position).getId();
    }//Them Id

    @Override
    protected Album getIdentifier(int position) {
        return dataSet.get(position);
    }//Ten album

    @Override
    protected String getName(Album album) {
        return album.getTitle();
    }//Ten

    @Override
    protected void onMultipleItemAction(@NonNull MenuItem menuItem, @NonNull List<Album> selection) {
        SongsMenuHelper.handleMenuClick(activity, getSongList(selection), menuItem.getItemId());
    }

    @NonNull
    private List<Song> getSongList(@NonNull List<Album> albums) {
        final List<Song> songs = new ArrayList<>();
        for (Album album : albums) {  //Lay du lieu ra tu album
            songs.addAll(album.songs); //Ađ
        }
        return songs;
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        @Nullable String sectionName = null;
        switch (PreferenceUtil.getInstance(activity).getAlbumSortOrder()) {
            case SortOrder.AlbumSortOrder.ALBUM_A_Z:
            case SortOrder.AlbumSortOrder.ALBUM_Z_A:
                sectionName = dataSet.get(position).getTitle();
                break;
            case SortOrder.AlbumSortOrder.ALBUM_ARTIST:
                sectionName = dataSet.get(position).getArtistName();
                break;
            case SortOrder.AlbumSortOrder.ALBUM_YEAR:
                return MusicUtil.getYearString(dataSet.get(position).getYear());
        }

        return MusicUtil.getSectionName(sectionName);
    }

    public class ViewHolder extends MediaEntryViewHolder {

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            setImageTransitionName(activity.getString(R.string.transition_album_art));
            if (menu != null) {
                menu.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            if (isInQuickSelectMode()) {
                toggleChecked(getAdapterPosition());
            } else {
                Pair[] albumPairs = new Pair[]{
                        Pair.create(image,
                                activity.getResources().getString(R.string.transition_album_art)
                        )};
                NavigationUtil.goToAlbum(activity, dataSet.get(getAdapterPosition()).getId(), albumPairs);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            toggleChecked(getAdapterPosition()); //Kiem tra toggle co mo hay chua
            return true;
        }
    }
}
