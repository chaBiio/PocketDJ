package com.mouse.lion.pocketdj.ui.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.mouse.lion.pocketdj.utils.Identifiable;

import me.mvdw.recyclerviewmergeadapter.adapter.RecyclerViewMergeAdapter;

/**
 * Created by lionm on 1/18/2018.
 */

public class MergeSpaceDecoration extends RecyclerView.ItemDecoration {

    private final RecyclerViewMergeAdapter pairedMergeAdapter;
    private final SparseArray<LocalMergeSpaceDecoration> localMergeSpaceDecorationMap;

    public MergeSpaceDecoration(RecyclerViewMergeAdapter pairedMergeAdapter) {
        this.pairedMergeAdapter = pairedMergeAdapter;
        localMergeSpaceDecorationMap = new SparseArray<>();
    }

    public void addLocalDecoration(Identifiable localAdapter, LocalMergeSpaceDecoration decoration) {
        localMergeSpaceDecorationMap.put(localAdapter.getId(), decoration);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int globalPosition = parent.getChildAdapterPosition(view);
        RecyclerViewMergeAdapter.PosSubAdapterInfo posSubAdapterInfo =
                pairedMergeAdapter.getPosSubAdapterInfoForGlobalPosition(globalPosition);
        if (posSubAdapterInfo != null) {
            RecyclerView.Adapter localAdapter = posSubAdapterInfo.getAdapter();
            if (localAdapter instanceof Identifiable) {
                LocalMergeSpaceDecoration localDecoration =
                        localMergeSpaceDecorationMap.get(((Identifiable) localAdapter).getId());
                if (localDecoration != null) {
                    localDecoration.getItemOffsets(outRect, posSubAdapterInfo.posInSubAdapter, view, parent, state);
                }
            } else {
                // should not be reached
                throw new IllegalStateException("local adapter must implements " + Identifiable.class.getSimpleName());
            }
        }
    }
}
