package com.alxarguello.apps.todocheck.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alxarguello.apps.obj.TodoItem;
import com.alxarguello.apps.todocheck.R;
import com.alxarguello.apps.todocheck.ui.actions.ItemEditionInterface;

import java.util.List;


/**
 * Created by alxarguello on 9/22/16.
 */

public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.ViewHolder> {

    private final ItemEditionInterface itemEdition;
    private List<TodoItem> todoItemList;

    public CheckListAdapter(ItemEditionInterface itemEdition, List<TodoItem> todoItemList) {
        this.todoItemList = todoItemList;
        this.itemEdition = itemEdition;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row, viewGroup, false);

        return new ViewHolder(v);
    }

    public void updateResults(List<TodoItem> results) {
        todoItemList = results;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.getTextView().setText(todoItemList.get(position).getComment());
        viewHolder.itemView.setOnLongClickListener(new OnLongPressItem(position));
        viewHolder.itemView.setOnClickListener(new OnClickItem(position));
    }


    @Override
    public int getItemCount() {
        return todoItemList==null?0:todoItemList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        ViewHolder(View v) {
            super(v);

            textView = (TextView) v.findViewById(R.id.text_item_row);
        }

        TextView getTextView() {
            return textView;
        }


    }

    private class OnClickItem implements View.OnClickListener {
        private int position;

        OnClickItem(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            itemEdition.fireEdition(todoItemList.get(position));
        }
    }

    private final class OnLongPressItem implements View.OnLongClickListener {


        private int position;

        OnLongPressItem(int position) {
            this.position = position;
        }

        @Override
        public boolean onLongClick(View view) {
            itemEdition.fireDeletion(todoItemList.get(position));
            return true;
        }
    }

}
