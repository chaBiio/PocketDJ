package com.mouse.lion.pocketdj.ui.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by lionm on 1/18/2018.
 */

public interface LocalMergeSpaceDecoration {
    void getItemOffsets(Rect outRect, int localPosition, View view, RecyclerView parent, RecyclerView.State state);
}
