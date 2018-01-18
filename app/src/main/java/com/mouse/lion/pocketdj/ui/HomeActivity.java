package com.mouse.lion.pocketdj.ui;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mouse.lion.pocketdj.R;
import com.mouse.lion.pocketdj.ui.adapter.IconTileAdapter;
import com.mouse.lion.pocketdj.ui.adapter.IdentifiableViewAdapter;
import com.mouse.lion.pocketdj.ui.adapter.MergeAdapter;
import com.mouse.lion.pocketdj.ui.recyclerview.LocalMergeSpaceDecoration;
import com.mouse.lion.pocketdj.ui.recyclerview.MergeSpaceDecoration;
import com.mouse.lion.pocketdj.utils.Identifiable;
import com.mouse.lion.pocketdj.utils.ViewUtils;

public class HomeActivity extends AppCompatActivity {

    private static final int MAX_GRID_SPAN_COUNT = 3;
    private static final int MIN_GRID_SPAN_COUNT = 1;

    private static final int ADP_ID_SUBHEADER_LIBRARY = 1;
    private static final int ADP_ID_ICON_TILES = 2;

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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, MAX_GRID_SPAN_COUNT);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mergeAdapter.isSingleViewItem(position)
                        ? MAX_GRID_SPAN_COUNT : MIN_GRID_SPAN_COUNT;
            }
        });
        parent.setLayoutManager(gridLayoutManager);

        // 'Library' section
        View subheader = getLayoutInflater().inflate(R.layout.item_grid_subheader, parent, false);
        ((TextView) subheader.findViewById(R.id.subheader_title)).setText(R.string.subheader_title_library);
        mergeAdapter.addAdapter(new IdentifiableViewAdapter(ADP_ID_SUBHEADER_LIBRARY, subheader));

        IconTileAdapter iconTileAdapter = new IconTileAdapter(ADP_ID_ICON_TILES,
                new IconTileAdapter.IconTile(R.drawable.ic_audiotrack, R.string.tile_name_songs),
                new IconTileAdapter.IconTile(R.drawable.ic_album, R.string.tile_name_albums),
                new IconTileAdapter.IconTile(R.drawable.ic_headset, R.string.tile_name_artists),
                new IconTileAdapter.IconTile(R.drawable.ic_layers, R.string.tile_name_genres),
                new IconTileAdapter.IconTile(R.drawable.ic_library_music, R.string.tile_name_playlists),
                new IconTileAdapter.IconTile(R.drawable.ic_settings_input_antenna, R.string.tile_name_streaming));
        mergeAdapter.addAdapter(iconTileAdapter);
        final int spaceBtwTiles = getResources().getDimensionPixelSize(R.dimen.s_icon_tile_home);
        final int spaceBtwTilesDiv2 = spaceBtwTiles / 2;
        mergeSpaceDecoration.addLocalDecoration(iconTileAdapter, new LocalMergeSpaceDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, int localPosition, View view, RecyclerView parent, RecyclerView.State state) {
                int mod = localPosition % MAX_GRID_SPAN_COUNT;
                outRect.top = outRect.bottom = spaceBtwTilesDiv2;
                if (mod == 0) {
                    outRect.left = spaceBtwTiles;
                    outRect.right = spaceBtwTilesDiv2;
                } else if (mod == MAX_GRID_SPAN_COUNT - 1) {
                    outRect.left = spaceBtwTilesDiv2;
                    outRect.right = spaceBtwTiles;
                } else {
                    outRect.left = outRect.right = spaceBtwTilesDiv2;
                }
            }
        });

        parent.setAdapter(mergeAdapter);
        parent.addItemDecoration(mergeSpaceDecoration);
    }
}
