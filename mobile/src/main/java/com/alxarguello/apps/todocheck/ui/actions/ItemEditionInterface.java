package com.alxarguello.apps.todocheck.ui.actions;

import com.alxarguello.apps.todocheck.obj.TodoItem;

/**
 * Created by alxarguello on 9/22/16.
 */

public interface ItemEditionInterface {

    void fireEdition(TodoItem item);

    void fireDeletion(TodoItem item);

    void fireUpdate(TodoItem item);
}
