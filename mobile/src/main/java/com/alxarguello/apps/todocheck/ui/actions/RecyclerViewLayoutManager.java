package com.alxarguello.apps.todocheck.ui.actions;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by alxarguello on 9/23/16.
 */

public class RecyclerViewLayoutManager extends LinearLayoutManager {


    public RecyclerViewLayoutManager(Context context) {
        super(context);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return true;
    }
}
