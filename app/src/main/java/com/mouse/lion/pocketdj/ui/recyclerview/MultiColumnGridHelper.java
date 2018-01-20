package com.mouse.lion.pocketdj.ui.recyclerview;

import android.content.Context;
import android.icu.text.PluralRules;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;

import com.mouse.lion.pocketdj.utils.Logger;

/**
 * Created by lionm on 1/18/2018.
 */

public class MultiColumnGridHelper {

    public interface Callback {
        int getContentsGroupForPosition(int position);
    }

    private static final int MIN_COLUMN_COUNT = 1;
    private static final int DEFAULT_COLUMN_COUNT = 1;
    private static final int INVALID_MAX_COLUMN_COUNT = -1;

    private final Callback CALLBACK;
    private final SparseIntArray contentsGroupColumnCountMap;
    private int confirmedMaxColumnCount = INVALID_MAX_COLUMN_COUNT;

    public MultiColumnGridHelper(@NonNull Callback callback) {
        contentsGroupColumnCountMap = new SparseIntArray();
        CALLBACK = callback;
    }

    /**
     * Be sure to call this method before call {@link MultiColumnGridHelper#setupGrid(Context, RecyclerView)} !
     */
    public void registerContentsGroupWithRequestColumnCount(int contentsGroup, int requestColumnCount) {
        contentsGroupColumnCountMap.put(contentsGroup, requestColumnCount);
    }

    public GridLayoutManager setupGrid(Context context, RecyclerView parent) {
        return setupGrid(context, parent, GridLayoutManager.VERTICAL, false);
    }

    public GridLayoutManager setupGrid(Context context, RecyclerView parent, int orientation, boolean reverseLayout) {
        int[] requestColumnCounts = new int[contentsGroupColumnCountMap.size()];
        for (int i = 0; i < contentsGroupColumnCountMap.size(); ++i) {
            requestColumnCounts[i] = contentsGroupColumnCountMap.valueAt(i);
        }

        confirmedMaxColumnCount = Math.max(findLcmOf(requestColumnCounts), MIN_COLUMN_COUNT);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, confirmedMaxColumnCount, orientation, reverseLayout);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int contentsGroup = CALLBACK.getContentsGroupForPosition(position);
                int columnCount = contentsGroupColumnCountMap.get(contentsGroup, DEFAULT_COLUMN_COUNT);
                return confirmedMaxColumnCount / columnCount;
            }
        });

        parent.setLayoutManager(gridLayoutManager);
        return gridLayoutManager;
    }

    private int findLcmOf(int[] integers) {
        if (integers.length == 0) {
            return 0;
        } else if (integers.length == 1) {
            return integers[0];
        } else {
            int lcm = integers[0];
            for (int i = 1; i < integers.length; ++i) {
                lcm = findLcmOf(lcm, integers[i]);
            }
            return lcm;
        }
    }

    private int findGcdOf(int a, int b) {
        if (a < b) {
            int tmp = a;
            a = b;
            b = tmp;
        }

        int r = -1;
        while (r != 0) {
            r = a % b;
            a = b;
            b = r;
        }

        return a;
    }

    private int findLcmOf(int a, int b) {
        return a * b / findGcdOf(a, b);
    }
}
