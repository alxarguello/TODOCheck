package com.alxarguello.apps.todocheck.dao;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

/**
 * Created by alxarguello on 9/22/16.
 * <p>
 * This class is intended to be a single point of operations for
 * saving, and updating any item, that way the CRUD operations are not
 * all over the place
 * <p>
 * potentially I can implement the Model instead of using BaseModel
 * of the DBFlow
 */

public class ItemPersistence {

    private static ItemPersistence instance;

    private ItemPersistence() {
    }

    public synchronized static ItemPersistence getInstance() {
        if (instance == null) {
            instance = new ItemPersistence();
        }
        return instance;
    }

    public <T extends BaseModel> void save(T item) {
        item.save();
    }

    public <T extends BaseModel> void asyncQuery(Class<T> clazz, QueryTransaction.QueryResultCallback<T> callback) {
        SQLite.select().from(clazz).async().queryResultCallback(callback).execute();
    }

    public <T extends BaseModel> void delete(T item) {
        item.delete();
    }
}
