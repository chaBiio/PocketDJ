package com.mouse.lion.pocketdj.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
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
 * Created by lionm on 1/20/2018.
 */

public class ArtworkTileAdapter
        extends RecyclerView.Adapter<ArtworkTileAdapter.ArtworkTileVh>
        implements Identifiable {

    static class ArtworkTileVh extends RecyclerView.ViewHolder {

        final ImageView artwork;
        final ImageView icon;
        final ImageView action;
        final TextView title;
        final TextView extraInfo;
        final View footerBg;

        ArtworkTileVh(View view) {
            super(view);
            artwork = view.findViewById(R.id.artwork);
            icon = view.findViewById(R.id.icon);
            action = view.findViewById(R.id.action);
            title = view.findViewById(R.id.title);
            extraInfo = view.findViewById(R.id.extra_info);
            footerBg = view.findViewById(R.id.footer_bg);
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
    @ColorInt private final int DEFAULT_TILE_FOOTER_COLOR;
    private final MusicBrowser MUSIC_BROWSER;

    public ArtworkTileAdapter(int id, Context context) {
        ID = id;
        MUSIC_BROWSER = new MusicBrowser(context);
        DEFAULT_TILE_FOOTER_COLOR = ContextCompat.getColor(context, R.color.blue);
    }

    @Override
    public ArtworkTileVh onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_artwork_tile_with_action, parent, false);
        return new ArtworkTileVh(view);
    }

    @Override
    public void onBindViewHolder(final ArtworkTileVh holder, int position) {
        int randomIdx = (int) ((DEMO_ARTWORK_URIS.length - 1) * Math.random() + 0.5);
        MUSIC_BROWSER.downloadArtworkInto(holder.artwork, DEMO_ARTWORK_URIS[randomIdx]);
//        MUSIC_BROWSER.downloadArtwork(DEMO_ARTWORK_URIS[randomIdx], new MusicBrowser.OnArtworkLoaded() {
//            @Override
//            public void onArtworkLoaded(Bitmap bitmap) {
//                holder.artwork.setImageBitmap(bitmap);
//                new Palette.Builder(bitmap).generate(new Palette.PaletteAsyncListener() {
//                    @Override
//                    public void onGenerated(Palette palette) {
//                        holder.footerBg.setBackgroundColor(palette.getMutedColor(DEFAULT_TILE_FOOTER_COLOR));
//                    }
//                });
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public int getId() {
        return ID;
    }
}
