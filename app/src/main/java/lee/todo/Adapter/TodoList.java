package lee.todo.Adapter;

import org.litepal.crud.DataSupport;

/**
 * @Author lee
 * @Time 5/15/17.
 */

public class TodoList extends DataSupport{
    private String title;
    private String note;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
