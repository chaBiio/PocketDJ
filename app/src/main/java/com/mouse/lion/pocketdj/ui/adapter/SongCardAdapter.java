package com.mouse.lion.pocketdj.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mouse.lion.pocketdj.R;
import com.mouse.lion.pocketdj.business.MusicBrowser;
import com.mouse.lion.pocketdj.utils.Identifiable;

/**
 * Created by lionm on 1/21/2018.
 */

public class SongCardAdapter
        extends RecyclerView.Adapter<SongCardAdapter.SongCardVh>
        implements Identifiable {

    static class SongCardVh extends RecyclerView.ViewHolder {

        final ImageView artwork;
        final ImageView action;
        final TextView title;
        final TextView extraInfo;

        SongCardVh(View view) {
            super(view);
            artwork = view.findViewById(R.id.artwork);
            action = view.findViewById(R.id.action);
            title = view.findViewById(R.id.title);
            extraInfo = view.findViewById(R.id.extra_info);
        }
    }

    private static final String[] DEMO_ARTWORK_URIS = new String[] {
            "https://www.billboard.com/files/styles/900_wide/public/media/Green-Day-American-Idiot-album-covers-billboard-1000x1000.jpg",
            "https://www.billboard.com/files/styles/900_wide/public/media/Taylor-Swift-1989-album-covers-billboard-1000x1000.jpg",
            "https://www.billboard.com/files/styles/900_wide/public/media/Ohio-Players-Honey-album-covers-billboard-1000x1000.jpg",
            "https://www.billboard.com/files/styles/900_wide/public/media/M-I-A-Kala-album-covers-billboard-1000x1000.jpg",
            "https://www.billboard.com/files/styles/900_wide/public/media/Lucinda-Williams-Car-Wheels-on-a-Gravel-Road-album-covers-billboard-1000x1000.jpg",
            "https://www.billboard.com/files/styles/900_wide/public/media/Bruce-Springsteen-Born-in-the-USA-album-covers-billboard-1000x1000.jpg",
            "https://www.wired.com/images_blogs/underwire/2010/06/lps_2b.jpg,",
            "https://www.wired.com/images_blogs/underwire/2010/06/lps_4a.jpg",
            "https://www.billboard.com/files/styles/900_wide/public/media/Big-Baby-DRAM-Cover-2016-billboard-1240.jpg",
            "https://www.billboard.com/files/styles/900_wide/public/media/Kings-of-Leon-Walls-album-2016-billboard-1240.jpg",
            "https://www.billboard.com/files/styles/900_wide/public/media/Deftones-gore-album-art-2016-a-billboard-1240.jpg",
            "https://www.billboard.com/files/styles/900_wide/public/media/Weeknd-Starboy-2016-billboard-1240.jpg"
    };

    private final int ID;
    private final MusicBrowser MUSIC_BROWSER;

    public SongCardAdapter(int id, Context context) {
        ID = id;
        MUSIC_BROWSER = new MusicBrowser(context);
    }

    @Override
    public SongCardVh onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song_card_with_action, parent, false);
        return new SongCardVh(view);
    }

    @Override
    public void onBindViewHolder(SongCardVh holder, int position) {
        int randomIdx = (int) ((DEMO_ARTWORK_URIS.length - 1) * Math.random() + 0.5);
        MUSIC_BROWSER.downloadArtworkInto(holder.artwork, DEMO_ARTWORK_URIS[randomIdx]);
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    @Override
    public int getId() {
        return ID;
    }
}
