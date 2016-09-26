package com.alxarguello.apps.todocheck.ui.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alxarguello.apps.todocheck.R;
import com.alxarguello.apps.todocheck.obj.TodoItem;
import com.alxarguello.apps.todocheck.ui.actions.ItemEditionInterface;
import com.alxarguello.apps.todocheck.util.Utils;

import java.util.List;


/**
 * Created by alxarguello on 9/22/16.
 */

public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.ViewHolder> {

    private final ItemEditionInterface itemEdition;
    private Context context;
    private List<TodoItem> todoItemList;
    private RecyclerView recyclerView;
    private int deletePosition = RecyclerView.NO_POSITION;

    public CheckListAdapter(Context context, ItemEditionInterface itemEdition) {
        this.context = context;
        this.itemEdition = itemEdition;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View container = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row2, viewGroup, false);
        container.setOnClickListener(onClickItem);
        container.setOnLongClickListener(onLongPressItem);
        return new ViewHolder(container);
    }

    public void updateResults(List<TodoItem> results) {
        todoItemList = results;
    }

    public void add(TodoItem item) {
        int pos = todoItemList.size();
        todoItemList.add(pos, item);
        notifyItemInserted(pos);
    }


    public void notifyItemChanged(TodoItem todoItem) {
        int position = todoItemList.indexOf(todoItem);
        notifyItemChanged(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        TodoItem todoItem = todoItemList.get(position);
        viewHolder.getTitleText().setText(todoItem.getTitle());
        if(todoItem.getDueDate()>0) {
            viewHolder.getDueDateText().setText(context.getString(R.string.due_on) + Utils.getStringDate(todoItem.getDueDate()));
        }
        viewHolder.getCheckBox().setTag(viewHolder);
        viewHolder.getCheckBox().setOnCheckedChangeListener(null);
        if (todoItem.isDone()) {
            viewHolder.getCheckBox().setChecked(true);
        } else {
            viewHolder.getCheckBox().setChecked(false);
        }
        changeTextColor(todoItem, viewHolder);
        viewHolder.getCheckBox().setOnCheckedChangeListener(onCheckedChangeListener);

    }


    @Override
    public int getItemCount() {
        return todoItemList == null ? 0 : todoItemList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleText;
        private final TextView dueDateText;
        private final CheckBox checkBox;

        ViewHolder(View v) {
            super(v);
            titleText = (TextView) v.findViewById(R.id.title_text);
            dueDateText = (TextView) v.findViewById(R.id.dueDate_text);
            checkBox = (CheckBox) v.findViewById(R.id.chkDone);
        }

        TextView getTitleText() {
            return titleText;
        }

        public TextView getDueDateText() {
            return dueDateText;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }
    }


    private View.OnClickListener onClickItem = new View.OnClickListener() {


        @Override
        public void onClick(View view) {
            int adapterPosition = recyclerView.getChildAdapterPosition(view);
            if (adapterPosition != RecyclerView.NO_POSITION) {
                itemEdition.fireEdition(todoItemList.get(adapterPosition));
            }
        }
    };

    private View.OnLongClickListener onLongPressItem = new View.OnLongClickListener() {


        @Override
        public boolean onLongClick(View view) {
            int adapterPosition = recyclerView.getChildAdapterPosition(view);
            if (adapterPosition != RecyclerView.NO_POSITION) {
                deletePosition = adapterPosition;
                showDialogForDeletion();
            }
            return true;
        }
    };

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            Object o = compoundButton.getTag();
            if (o != null && o instanceof ViewHolder) {
                ViewHolder holder = (ViewHolder) o;
                int adapterPosition = recyclerView.getChildAdapterPosition(holder.itemView);
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    TodoItem item = todoItemList.get(adapterPosition);
                    item.setDone(b);
                    itemEdition.fireUpdate(item);
                    changeTextColor(item, holder);
                }
            }
        }
    };

    private void changeTextColor(TodoItem todoItem, ViewHolder viewHolder) {
        int color;
        if (todoItem.isDone()) {
            color = android.R.color.darker_gray;
        } else {
            color = android.R.color.black;
        }
        int themeColor = ContextCompat.getColor(context, color);
        viewHolder.getTitleText().setTextColor(themeColor);
        viewHolder.getDueDateText().setTextColor(themeColor);
    }

    public void showDialogForDeletion() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.deleting);
        builder.setMessage(R.string.sure_delete_item);
        builder.setPositiveButton(android.R.string.ok, clickOkDeleteListener);
        builder.setNegativeButton(android.R.string.cancel, clickCancelDeleteListener);
        builder.show();
    }


    private DialogInterface.OnClickListener clickOkDeleteListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            TodoItem item = todoItemList.get(deletePosition);
            todoItemList.remove(deletePosition);
            notifyItemRemoved(deletePosition);
            itemEdition.fireDeletion(item);
            deletePosition = RecyclerView.NO_POSITION;
        }
    };

    private DialogInterface.OnClickListener clickCancelDeleteListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            deletePosition = RecyclerView.NO_POSITION;
        }
    };

}
