package com.mouse.lion.pocketdj.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.mouse.lion.pocketdj.R;
import com.mouse.lion.pocketdj.ui.adapter.ArtworkTileAdapter;
import com.mouse.lion.pocketdj.ui.adapter.IconTileAdapter;
import com.mouse.lion.pocketdj.ui.adapter.IdentifiableViewAdapter;
import com.mouse.lion.pocketdj.ui.adapter.MergeAdapter;
import com.mouse.lion.pocketdj.ui.adapter.SongCardAdapter;
import com.mouse.lion.pocketdj.ui.recyclerview.GridItemSpaceDecoration;
import com.mouse.lion.pocketdj.ui.recyclerview.LinearItemSpaceDecoration;
import com.mouse.lion.pocketdj.ui.recyclerview.MergeSpaceDecoration;
import com.mouse.lion.pocketdj.ui.recyclerview.MultiColumnGridHelper;
import com.mouse.lion.pocketdj.utils.Identifiable;
import com.mouse.lion.pocketdj.utils.ViewUtils;

import me.mvdw.recyclerviewmergeadapter.adapter.RecyclerViewMergeAdapter;

public class HomeActivity extends AppCompatActivity {

    private static final int GRID_SINGLE_COLUMN_COUNT = 1;

    // Adapter-id and contents-group
    private static final int CONTENTS_INVALID = -1;
    private static final int CONTENTS_SUBHEADER_LIBRARY = 1;
    private static final int CONTENTS_ICON_TILES = 2;
    private static final int CONTENTS_SUBHEADER_FAVORITE_ALBUMS = 3;
    private static final int CONTENTS_FAVORITE_ALBUMS = 4;
    private static final int CONTENTS_SUBHEADER_FAVORITE_SONGS = 5;
    private static final int CONTENTS_FAVORITE_SONGS = 6;
    private static final int CONTENTS_SUBHEADER_RECENT_ALBUMS = 7;
    private static final int CONTENTS_RECENT_ALBUMS = 8;
    private static final int CONTENTS_SUBHEADER_RECENT_SONGS = 9;
    private static final int CONTENTS_RECENT_SONGS = 10;

    private MergeAdapter mergeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewUtils.setToolBarTitleFont(this, toolbar, R.font.roboto_mono_medium);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        RecyclerView grid = findViewById(R.id.grid);
        setupRecyclerView(grid);
    }

    private void setupRecyclerView(RecyclerView parent) {
        mergeAdapter = new MergeAdapter();
        MergeSpaceDecoration mergeSpaceDecoration = new MergeSpaceDecoration(mergeAdapter);
        MultiColumnGridHelper multiColumnGridHelper = new MultiColumnGridHelper(new MultiColumnGridHelper.Callback() {
            @Override
            public int getContentsGroupForPosition(int position) {
                RecyclerViewMergeAdapter.PosSubAdapterInfo posSubAdapterInfo =
                        mergeAdapter.getPosSubAdapterInfoForGlobalPosition(position);
                if (posSubAdapterInfo != null) {
                    RecyclerView.Adapter adapter = posSubAdapterInfo.getAdapter();
                    // adapter-id == contents-group
                    if (adapter instanceof Identifiable) {
                        return ((Identifiable) adapter).getId();
                    }
                }

                return CONTENTS_INVALID;
            }
        });

        // TODO; confirm this integer automatically
        final int columnsForIconTiles = 3;
        multiColumnGridHelper.registerContentsGroupWithRequestColumnCount(CONTENTS_SUBHEADER_LIBRARY, GRID_SINGLE_COLUMN_COUNT);
        multiColumnGridHelper.registerContentsGroupWithRequestColumnCount(CONTENTS_ICON_TILES, columnsForIconTiles);
        multiColumnGridHelper.registerContentsGroupWithRequestColumnCount(CONTENTS_SUBHEADER_FAVORITE_ALBUMS, GRID_SINGLE_COLUMN_COUNT);
        multiColumnGridHelper.registerContentsGroupWithRequestColumnCount(CONTENTS_FAVORITE_ALBUMS, 2);
        multiColumnGridHelper.registerContentsGroupWithRequestColumnCount(CONTENTS_SUBHEADER_FAVORITE_SONGS, GRID_SINGLE_COLUMN_COUNT);
        multiColumnGridHelper.registerContentsGroupWithRequestColumnCount(CONTENTS_FAVORITE_SONGS, GRID_SINGLE_COLUMN_COUNT);
        multiColumnGridHelper.registerContentsGroupWithRequestColumnCount(CONTENTS_SUBHEADER_RECENT_ALBUMS, GRID_SINGLE_COLUMN_COUNT);
        multiColumnGridHelper.registerContentsGroupWithRequestColumnCount(CONTENTS_RECENT_ALBUMS, 2);
        multiColumnGridHelper.registerContentsGroupWithRequestColumnCount(CONTENTS_SUBHEADER_RECENT_SONGS, GRID_SINGLE_COLUMN_COUNT);
        multiColumnGridHelper.registerContentsGroupWithRequestColumnCount(CONTENTS_RECENT_SONGS, GRID_SINGLE_COLUMN_COUNT);
        multiColumnGridHelper.setupGrid(this, parent);

        // 'Library' section
        View subheader = getLayoutInflater().inflate(R.layout.item_subheader, parent, false);
        ((TextView) subheader.findViewById(R.id.subheader_title)).setText(R.string.subheader_title_library);
        mergeAdapter.addAdapter(new IdentifiableViewAdapter(CONTENTS_SUBHEADER_LIBRARY, subheader));

        IconTileAdapter iconTileAdapter = new IconTileAdapter(CONTENTS_ICON_TILES,
                new IconTileAdapter.IconTile(R.drawable.ic_audiotrack, R.string.tile_name_songs),
                new IconTileAdapter.IconTile(R.drawable.ic_album, R.string.tile_name_albums),
                new IconTileAdapter.IconTile(R.drawable.ic_headset, R.string.tile_name_artists),
                new IconTileAdapter.IconTile(R.drawable.ic_layers, R.string.tile_name_genres),
                new IconTileAdapter.IconTile(R.drawable.ic_library_music, R.string.tile_name_playlists),
                new IconTileAdapter.IconTile(R.drawable.ic_settings_input_antenna, R.string.tile_name_streaming));

        mergeAdapter.addAdapter(iconTileAdapter);
        mergeSpaceDecoration.addLocalDecoration(iconTileAdapter, new GridItemSpaceDecoration(
                getResources().getDimensionPixelSize(R.dimen.s_icon_tile_home),
                columnsForIconTiles, false));

        // 'Favorite - Albums - ' section
        subheader = getLayoutInflater().inflate(R.layout.item_subheader, parent, false);
        ((TextView) subheader.findViewById(R.id.subheader_title)).setText(R.string.subheader_title_favorite_albums);
        mergeAdapter.addAdapter(new IdentifiableViewAdapter(CONTENTS_SUBHEADER_FAVORITE_ALBUMS, subheader));
        ArtworkTileAdapter artworkTileAdapter =  new ArtworkTileAdapter(CONTENTS_FAVORITE_ALBUMS, this);
        mergeAdapter.addAdapter(artworkTileAdapter);
        mergeSpaceDecoration.addLocalDecoration(artworkTileAdapter, new GridItemSpaceDecoration(
                getResources().getDimensionPixelSize(R.dimen.s_artwork_tile_home),
                2, true));


        // 'Favorite - Songs - ' section
        subheader = getLayoutInflater().inflate(R.layout.item_subheader, parent, false);
        ((TextView) subheader.findViewById(R.id.subheader_title)).setText(R.string.subheader_title_favorite_songs);
        mergeAdapter.addAdapter(new IdentifiableViewAdapter(CONTENTS_SUBHEADER_FAVORITE_SONGS, subheader));
        SongCardAdapter songCardAdapter = new SongCardAdapter(CONTENTS_FAVORITE_SONGS, this);
        mergeAdapter.addAdapter(songCardAdapter);
        int space = getResources().getDimensionPixelSize(R.dimen.s_song_card_each_side);
        mergeSpaceDecoration.addLocalDecoration(songCardAdapter, new LinearItemSpaceDecoration(space, 0, space, 0));

        // 'Recent - Albums - ' section
        subheader = getLayoutInflater().inflate(R.layout.item_subheader, parent, false);
        ((TextView) subheader.findViewById(R.id.subheader_title)).setText(R.string.subheader_title_recent_albums);
        mergeAdapter.addAdapter(new IdentifiableViewAdapter(CONTENTS_SUBHEADER_RECENT_ALBUMS, subheader));
        artworkTileAdapter =  new ArtworkTileAdapter(CONTENTS_RECENT_ALBUMS, this);
        mergeAdapter.addAdapter(artworkTileAdapter);
        mergeSpaceDecoration.addLocalDecoration(artworkTileAdapter, new GridItemSpaceDecoration(
                getResources().getDimensionPixelSize(R.dimen.s_artwork_tile_home),
                2, true));


        // 'Favorite - Songs - ' section
        subheader = getLayoutInflater().inflate(R.layout.item_subheader, parent, false);
        ((TextView) subheader.findViewById(R.id.subheader_title)).setText(R.string.subheader_title_recent_songs);
        mergeAdapter.addAdapter(new IdentifiableViewAdapter(CONTENTS_SUBHEADER_RECENT_SONGS, subheader));
        songCardAdapter = new SongCardAdapter(CONTENTS_RECENT_SONGS, this);
        mergeAdapter.addAdapter(songCardAdapter);
        mergeSpaceDecoration.addLocalDecoration(songCardAdapter, new LinearItemSpaceDecoration(space, 0, space, 0));

        parent.setAdapter(mergeAdapter);
        parent.addItemDecoration(mergeSpaceDecoration);
    }
}
