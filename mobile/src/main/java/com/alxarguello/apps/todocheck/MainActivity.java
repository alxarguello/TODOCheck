package com.alxarguello.apps.todocheck;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alxarguello.apps.dao.ItemPersistence;
import com.alxarguello.apps.obj.TodoItem;
import com.alxarguello.apps.todocheck.ui.actions.ItemEditionInterface;
import com.alxarguello.apps.todocheck.ui.activities.EditItemActivity;
import com.alxarguello.apps.todocheck.ui.adapters.CheckListAdapter;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemEditionInterface {


    public static final int EDIT_REQUEST_CODE = 100;
    private CheckListAdapter rvAdapter;
    private TextView newItemText;
    private ItemPersistence itemPersistence;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        itemPersistence = ItemPersistence.getInstance();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewItems);
        newItemText = (TextView) findViewById(R.id.etNewItem);
        rvAdapter = new CheckListAdapter(MainActivity.this, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(rvAdapter);
        prepareItemsDataSet();

    }

    public void prepareItemsDataSet() {
        itemPersistence.asyncQuery(TodoItem.class, queryCallback);
    }


    public void addItemClick(View v) {
        String text = newItemText.getText().toString();
        TodoItem item = new TodoItem();
        item.setComment(text);
        itemPersistence.save(item);
        prepareItemsDataSet();
        newItemText.setText("");
    }

    private final QueryTransaction.QueryResultCallback<TodoItem> queryCallback = new QueryTransaction.QueryResultCallback<TodoItem>() {
        @Override
        public void onQueryResult(QueryTransaction transaction, @NonNull CursorResult<TodoItem> tResult) {
            List<TodoItem> todoItemArrayList = tResult.toList();
            rvAdapter.updateResults(todoItemArrayList);
            rvAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void fireEdition(TodoItem item) {
        Intent intent = new Intent(getApplicationContext(), EditItemActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(TodoItem.TODO_ITEM_TAG, item);
        intent.putExtras(bundle);
        startActivityForResult(intent, MainActivity.EDIT_REQUEST_CODE);
    }

    @Override
    public void fireDeletion(TodoItem item) {
        itemPersistence.delete(item);
        prepareItemsDataSet();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        prepareItemsDataSet();

    }
}
