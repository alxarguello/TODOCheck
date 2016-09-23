package com.alxarguello.apps.dao;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by alxarguello on 9/22/16.
 */

@Database(name = TODOCheckDatabase.NAME,version = TODOCheckDatabase.VERSION)
public class TODOCheckDatabase {
    public static final String NAME = "todoCheck";
    public static final int VERSION = 1;
}