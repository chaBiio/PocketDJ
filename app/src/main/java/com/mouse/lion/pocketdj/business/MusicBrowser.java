package com.mouse.lion.pocketdj.business;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by lionm on 1/20/2018.
 */

public class MusicBrowser {

    public interface OnArtworkLoaded {
        void onArtworkLoaded(Bitmap bitmap);
    }

    private final Context context;

    public MusicBrowser(Context context) {
        this.context = context;
    }

    public void downloadArtworkInto(ImageView view, String uri) {
        Picasso.with(context).load(uri).into(view);
    }

    public void downloadArtwork(String uri, final OnArtworkLoaded listener) {
        Picasso.with(context).load(uri).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                listener.onArtworkLoaded(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }
}
