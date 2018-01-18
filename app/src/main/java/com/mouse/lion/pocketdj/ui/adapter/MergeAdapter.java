package com.mouse.lion.pocketdj.ui.adapter;

import android.support.annotation.NonNull;
import android.view.View;

import java.util.Arrays;
import java.util.List;

import me.mvdw.recyclerviewmergeadapter.adapter.RecyclerViewMergeAdapter;
import me.mvdw.recyclerviewmergeadapter.adapter.ViewAdapter;

/**
 * Created by lionm on 1/18/2018.
 */

public class MergeAdapter extends RecyclerViewMergeAdapter {

    public MergeAdapter() {
        super();
    }

    public boolean isSingleViewItem(int position) {
        PosSubAdapterInfo posSubAdapterInfo = super.getPosSubAdapterInfoForGlobalPosition(position);
        return posSubAdapterInfo != null &&
                posSubAdapterInfo.getAdapter() instanceof ViewAdapter;
    }
}
