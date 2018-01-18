package com.mouse.lion.pocketdj.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mouse.lion.pocketdj.utils.Identifiable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import me.mvdw.recyclerviewmergeadapter.adapter.ViewAdapter;

/**
 * Created by lionm on 1/18/2018.
 */

public class IdentifiableViewAdapter extends ViewAdapter implements Identifiable {

    private final int ID;

    public IdentifiableViewAdapter(int id) {
        super();
        ID = id;
    }

    public IdentifiableViewAdapter(int id, @NonNull View view) {
        super(Collections.singletonList(view));
        ID = id;
    }

    public IdentifiableViewAdapter(int id, @NonNull List<View> viewList) {
        super(viewList);
        ID = id;
    }

    @Override
    public int getId() {
        return ID;
    }
}
