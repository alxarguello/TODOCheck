package com.alxarguello.apps.todocheck;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.alxarguello.apps.todocheck.dao.ItemPersistence;
import com.alxarguello.apps.todocheck.obj.TodoItem;
import com.alxarguello.apps.todocheck.ui.actions.ItemEditionInterface;
import com.alxarguello.apps.todocheck.ui.actions.RecyclerViewLayoutManager;
import com.alxarguello.apps.todocheck.ui.adapters.CheckListAdapter;
import com.alxarguello.apps.todocheck.util.Utils;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemEditionInterface {


    private CheckListAdapter rvAdapter;
    private View dialogView, emptyView;
    private RecyclerView recyclerView;
    private List<TodoItem> todoItemArrayList;
    private TodoItem currentEditionItem;
    private ItemPersistence itemPersistence;
    private TextView itemTitle, itemIsDoneText;
    private EditText itemNotes, itemDueDate;
    private CheckBox checkBoxDone, checkBoxDate;
    boolean newItem = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        itemPersistence = ItemPersistence.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewItems);
        emptyView = findViewById(R.id.emptView);
        rvAdapter = new CheckListAdapter(this, MainActivity.this);
        recyclerView.setLayoutManager(new RecyclerViewLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(rvAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        prepareItemsDataSet();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(fabClickListener);

    }

    public void prepareItemsDataSet() {
        itemPersistence.asyncQuery(TodoItem.class, queryCallback);
    }

    public void notifyItemAdded(View view, TodoItem item) {
        emptyView.setVisibility(View.GONE);
        rvAdapter.add(item);
        Snackbar snackbar = Snackbar.make(view, item.getTitle(), Snackbar.LENGTH_SHORT)
                .setAction("Action", null);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        snackbar.show();
    }

    public void notifyItemChanged(TodoItem todoItem) {
        rvAdapter.notifyItemChanged(todoItem);
    }

    private final QueryTransaction.QueryResultCallback<TodoItem> queryCallback = new QueryTransaction.QueryResultCallback<TodoItem>() {
        @Override
        public void onQueryResult(QueryTransaction transaction, @NonNull CursorResult<TodoItem> tResult) {
            todoItemArrayList = tResult.toList();
            if (todoItemArrayList == null || todoItemArrayList.isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
            }
            rvAdapter.updateResults(todoItemArrayList);
            rvAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void fireEdition(TodoItem item) {
        showDialogForEdition(item, R.string.edit_item);
    }

    public void showDialogForEdition(TodoItem todoItem, int resourceTitle) {
        currentEditionItem = todoItem;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.getContext();
        builder.setTitle(resourceTitle);
        dialogView = getLayoutInflater().inflate(R.layout.dialog_item_edition, null);
        builder.setView(dialogView);
        setupDialog();
        builder.setPositiveButton(android.R.string.ok, editDialogOkListener);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setOnDismissListener(dialogDismissListener);
        builder.show();
    }


    private void setupDialog() {

        itemTitle = (EditText) dialogView.findViewById(R.id.etItemTitle);
        itemNotes = (EditText) dialogView.findViewById(R.id.etItemNotes);
        itemDueDate = (EditText) dialogView.findViewById(R.id.etItemDueDate);
        itemIsDoneText = (TextView) dialogView.findViewById(R.id.doneText);
        checkBoxDone = (CheckBox) dialogView.findViewById(R.id.checkBoxDone);
        checkBoxDate = (CheckBox) dialogView.findViewById(R.id.checkBoxDate);

        if (currentEditionItem != null && dialogView != null) {
            itemTitle.setText(currentEditionItem.getTitle());
            itemNotes.setText(currentEditionItem.getNotes());
            checkBoxDone.setChecked(currentEditionItem.isDone());
            setItemIsDoneText(currentEditionItem.isDone());
            boolean isDated = (currentEditionItem.getDueDate() > 0);
            checkBoxDate.setChecked(isDated);
            itemDueDate.setVisibility(isDated ? View.VISIBLE : View.GONE);
            if (isDated) {
                itemDueDate.setText(Utils.getStringDate(currentEditionItem.getDueDate()));
            }
        }
        checkBoxDone.setOnCheckedChangeListener(onCheckedDoneChangeListener);
        checkBoxDate.setOnCheckedChangeListener(onCheckedDateChangeListener);
    }

    private void setItemIsDoneText(boolean isDone) {

        itemIsDoneText.setTextColor(ContextCompat.getColor(MainActivity.this, isDone ? R.color.colorAccent : android.R.color.darker_gray));
        itemIsDoneText.setText(getResources().getString(isDone ? R.string.isDone : R.string.isItDone));
    }


    @Override
    public void fireDeletion(TodoItem item) {
        if (todoItemArrayList == null || todoItemArrayList.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
        }
        itemPersistence.delete(item);
    }

    @Override
    public void fireUpdate(TodoItem item) {
        itemPersistence.save(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        prepareItemsDataSet();

    }


    public long retrieveCalendarData() {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, 0, 0);
        return c.getTime().getTime();
    }


    View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showDialogForEdition(null, R.string.add_a_new_item);
        }
    };


    private DialogInterface.OnClickListener editDialogOkListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

            if (currentEditionItem == null) {
                currentEditionItem = new TodoItem();
                newItem = true;
            }
            currentEditionItem.setTitle(itemTitle.getText().toString());
            currentEditionItem.setNotes(itemNotes.getText().toString());
            currentEditionItem.setDone(checkBoxDone.isChecked());

            if (currentEditionItem.isDone()) {
                currentEditionItem.setDoneDate(Calendar.getInstance().getTimeInMillis());
            } else {
                currentEditionItem.setDoneDate(0L);
            }

            if (checkBoxDate.isChecked()) {
                currentEditionItem.setDueDate(retrieveCalendarData());
            } else {
                currentEditionItem.setDueDate(0L);
            }


            itemPersistence.save(currentEditionItem);
            itemTitle = null;
            itemNotes = null;
            itemDueDate = null;
            checkBoxDone = null;
            dialogView = null;
            year = month = day = 0;

        }
    };


    private DialogInterface.OnDismissListener dialogDismissListener = new DialogInterface.OnDismissListener() {

        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            if (newItem) {
                notifyItemAdded(recyclerView, currentEditionItem);
            } else {
                notifyItemChanged(currentEditionItem);
            }
            newItem = false;
            currentEditionItem = null;
        }
    };


    CompoundButton.OnCheckedChangeListener onCheckedDoneChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isDone) {
            setItemIsDoneText(isDone);
        }
    };


    CompoundButton.OnCheckedChangeListener onCheckedDateChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isDated) {
            if (isDated) {
                showDatePicker();
            }
            itemDueDate.setVisibility(isDated ? View.VISIBLE : View.GONE);

        }
    };

    //TODO include hour and min
    private int year, month, day, mHour, mMinute;

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int dialogYear,
                                  int dialogMonthOfYear, int dialogDayOfMonth) {
                year = dialogYear;
                month = dialogMonthOfYear;
                day = dialogDayOfMonth;
                itemDueDate.setText(day + "/" + (month + 1) + "/" + year);
            }
        }, year, month, day);
        datePickerDialog.setOnCancelListener(cancelDateListener);
        datePickerDialog.show();
    }


    private DialogInterface.OnCancelListener cancelDateListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialogInterface) {
            checkBoxDate.setChecked(false);
            year = month = day = 0;
        }
    };


}
