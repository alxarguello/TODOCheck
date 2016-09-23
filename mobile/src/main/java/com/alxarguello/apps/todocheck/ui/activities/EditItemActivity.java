package com.alxarguello.apps.todocheck.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.alxarguello.apps.obj.TodoItem;
import com.alxarguello.apps.todocheck.R;

/**
 * Created by alxarguello on 9/22/16.
 */
public class EditItemActivity extends AppCompatActivity {

    private EditText editedText;
    private TodoItem itemToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        itemToEdit = getIntent().getParcelableExtra(TodoItem.TODO_ITEM_TAG);
        editedText = (EditText) findViewById(R.id.editedText);
        editedText.setText(itemToEdit.getComment());
    }

    public void onDoneClick(View v) {
        String updatedText = editedText.getText().toString();
        itemToEdit.setComment(updatedText);
        itemToEdit.save();
        finish();
    }


}
