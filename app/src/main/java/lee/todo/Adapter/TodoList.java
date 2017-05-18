package lee.todo.Adapter;

import org.litepal.crud.DataSupport;

import java.util.Calendar;

import lee.todo.Util.CalUtil;

/**
 * @Author lee
 * @Time 5/15/17.
 */

public class TodoList extends DataSupport{
    private String title;
    private String note;
    private int id;
    private  String time;

    public TodoList() {
        CalUtil calUtil=new CalUtil();
        this.time=calUtil.getCurrentDate();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

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
