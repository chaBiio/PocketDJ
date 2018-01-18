package com.mouse.lion.pocketdj.ui.adapter;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mouse.lion.pocketdj.R;
import com.mouse.lion.pocketdj.utils.Identifiable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by lionm on 1/18/2018.
 */

public class IconTileAdapter
        extends RecyclerView.Adapter<IconTileAdapter.IconTileViewHolder>
        implements Identifiable {

    public static class IconTile {

        @DrawableRes final int icon;
        @StringRes final int name;

        public IconTile(@DrawableRes int icon, @StringRes int name) {
            this.icon = icon;
            this.name = name;
        }
    }

    static class IconTileViewHolder extends RecyclerView.ViewHolder {

        final TextView name;
        final ImageView icon;

        IconTileViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.tile_name);
            icon = view.findViewById(R.id.tile_icon);
        }
    }

    // immutable list
    private final List<IconTile> ICON_TILES;
    private final int ID;

    public IconTileAdapter(int id, @NonNull IconTile... iconTiles) {
        ICON_TILES = Collections.unmodifiableList(Arrays.asList(iconTiles));
        ID = id;
    }

    @Override
    public IconTileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IconTileViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_icon_tile, parent, false));
    }

    @Override
    public void onBindViewHolder(IconTileViewHolder holder, int position) {
        IconTile iconTile = ICON_TILES.get(position);
        holder.icon.setImageResource(iconTile.icon);
        holder.name.setText(iconTile.name);
    }

    @Override
    public int getItemCount() {
        return ICON_TILES.size();
    }

    @Override
    public int getId() {
        return ID;
    }
}
