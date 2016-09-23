package com.alxarguello.apps.obj;

import android.os.Parcel;
import android.os.Parcelable;

import com.alxarguello.apps.dao.TODOCheckDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by alxarguello on 9/22/16.
 */
@Table(database = TODOCheckDatabase.class)
public class TodoItem extends BaseModel implements Parcelable {

    public static final String TODO_ITEM_TAG = "todo-tag";

    public TodoItem() {

    }

    private TodoItem(Parcel in) {
        id = in.readInt();
        comment = in.readString();
        done = in.readInt() == 1 ? true : false;
        dueDate = in.readLong();
        doneDate = in.readLong();
    }

    @PrimaryKey(autoincrement = true)
    private int id;

    @Column
    @Unique(onUniqueConflict = ConflictAction.FAIL)
    private String comment;
    @Column
    private boolean done;
    @Column
    private long dueDate;
    @Column
    private long doneDate;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public long getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(long doneDate) {
        this.doneDate = doneDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(comment);
        parcel.writeInt(done ? 1 : 0);
        parcel.writeLong(dueDate);
        parcel.writeLong(doneDate);

    }

    public static final Parcelable.Creator<TodoItem> CREATOR = new Parcelable.Creator<TodoItem>() {
        public TodoItem createFromParcel(Parcel in) {
            return new TodoItem(in);
        }

        public TodoItem[] newArray(int size) {
            return new TodoItem[size];
        }
    };


}

