package com.mouse.lion.pocketdj.ui.recyclerview;

import android.graphics.Rect;
import android.support.annotation.Px;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by lionm on 1/20/2018.
 */

public class GridItemSpaceDecoration extends RecyclerView.ItemDecoration implements LocalMergeSpaceDecoration {

    @Px private final int SPACE;
    private final int COLUMN_COUNT;
    private final int[] LEFT_SIDE_SPACES;
    private final int[] RIGHT_SIDE_SPACES;
    private final boolean ADD_SPACES_TO_EACH_SIDE_OF_GRID;
    private boolean wasSpaceSizesConfirmed = false;

    public GridItemSpaceDecoration(@Px int space, int columnCount, boolean addSpacesToEachSideOfList) {
        SPACE = space;
        COLUMN_COUNT = columnCount;
        LEFT_SIDE_SPACES = new int[columnCount];
        RIGHT_SIDE_SPACES = new int[columnCount];
        ADD_SPACES_TO_EACH_SIDE_OF_GRID = addSpacesToEachSideOfList;
    }

    private void confirmSpaceSizes(int parentWidth) {
        final int gridWidth = parentWidth / COLUMN_COUNT;
        final int itemWidth = (ADD_SPACES_TO_EACH_SIDE_OF_GRID)
                ? (parentWidth - SPACE * (COLUMN_COUNT + 1)) / COLUMN_COUNT
                : (parentWidth - SPACE * (COLUMN_COUNT - 1)) / COLUMN_COUNT;

        for (int i = 0, left = SPACE, right; i < COLUMN_COUNT / 2; ++i, left = SPACE - right) {
            LEFT_SIDE_SPACES[i] = (i == 0 && !ADD_SPACES_TO_EACH_SIDE_OF_GRID) ? 0 : left;
            RIGHT_SIDE_SPACES[i] = right = gridWidth - LEFT_SIDE_SPACES[i] - itemWidth;
            LEFT_SIDE_SPACES[COLUMN_COUNT - 1 - i] = RIGHT_SIDE_SPACES[i];
            RIGHT_SIDE_SPACES[COLUMN_COUNT - 1 - i] = LEFT_SIDE_SPACES[i];
        }

        if (COLUMN_COUNT % 2 != 0) {
            int i = COLUMN_COUNT / 2;
            LEFT_SIDE_SPACES[i] = RIGHT_SIDE_SPACES[i] = (gridWidth - itemWidth) / 2;
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, int localPosition, View view, RecyclerView parent, RecyclerView.State state) {
        if (!wasSpaceSizesConfirmed) {
            confirmSpaceSizes(parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight());
            wasSpaceSizesConfirmed = true;
        }
        int column = localPosition % COLUMN_COUNT;
        outRect.left = LEFT_SIDE_SPACES[column];
        outRect.right = RIGHT_SIDE_SPACES[column];
        outRect.top = outRect.bottom = SPACE / 2;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        getItemOffsets(outRect, position, view, parent, state);
    }
}
