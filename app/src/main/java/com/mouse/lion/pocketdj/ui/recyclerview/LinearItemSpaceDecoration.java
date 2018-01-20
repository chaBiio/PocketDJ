package com.mouse.lion.pocketdj.ui.recyclerview;

import android.graphics.Rect;
import android.support.annotation.Px;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by lionm on 1/21/2018.
 */

public class LinearItemSpaceDecoration
        extends RecyclerView.ItemDecoration
        implements LocalMergeSpaceDecoration {

    private final Rect SPACES;

    public LinearItemSpaceDecoration(@Px int left, @Px int top, @Px int right, @Px int bottom) {
        SPACES = new Rect(left, top, right, bottom);
    }

    @Override
    public void getItemOffsets(Rect outRect, int localPosition, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(SPACES);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        getItemOffsets(outRect, position, view, parent, state);
    }
}
