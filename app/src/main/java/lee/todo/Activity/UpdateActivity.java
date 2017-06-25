package lee.todo.Activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.List;

import lee.todo.R;
import lee.todo.Adapter.TodoList;
import lee.todo.Util.CalUtil;

public class UpdateActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    private FloatingActionButton button;
    private EditText editTitle;
    private EditText editNote;
    private TextView textTime;
    private TextView reminder;
    private String title;
    private String note;
    private String date=" ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        button=(FloatingActionButton)findViewById(R.id.float_buttonIn);
        editTitle=(EditText)findViewById(R.id.titleIn);
        editNote=(EditText)findViewById(R.id.noteIn);
        textTime=(TextView)findViewById(R.id.time_text);
        reminder=(TextView)findViewById(R.id.reminder_text);
        Intent intent=getIntent();
        title=intent.getStringExtra("title");
        note=intent.getStringExtra("note");
        Log.d("UpdateActivity title",title);
        Log.d("UpdateActivity note",note);
        String time;
        final String cTime;
        time=getTime(1);
        cTime=getTime(2);
        Log.d("Ctime",cTime);
        editTitle.setText(title);
        editNote.setText(note);
        textTime.setText("Edited "+time);
        reminder.setText("will remind you on date: "+findOne().getRemindTime());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoList todo=new TodoList();
                String mtitle=editTitle.getText().toString();
                String mnote=editNote.getText().toString();
                todo.setTime(cTime);
                boolean ischanged=!date.equals(" ")||!date.equals(findOne().getRemindTime())
                        ||!mnote.equals(note)||!mtitle.equals(title);

                todo.setTitle(mtitle);
                todo.setNote(mnote);
                //内容不变，不跟新时间
                if (ischanged){
                    todo.setRemindTime(date);
                    Log.d("UpdateActivity","save reminder");
                    todo.updateAll("title=? and note=?", title, note);
                    Log.d("sql","Update");
                }
                finish();
            }
        });
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(UpdateActivity.this,"Notice",Toast.LENGTH_SHORT).show();
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        UpdateActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
                return false;
            }
        });
    }

    private String getTime(int key){
        String time=null;
        switch (key) {
            //saved time
            case 1:
                List<TodoList> todolists = DataSupport.where("title=?", title).where("note=?", note).find(TodoList.class);
                for (TodoList todo : todolists) {
                    time = todo.getTime();
                }
                break;
            //current time
            case 2:
                CalUtil calUtil=new CalUtil();
                time=calUtil.getCurrentDate();
        }
        return time;
    }

    private TodoList findOne(){
        TodoList todo=null;
        List<TodoList> todolists = DataSupport.where("title=?", title).where("note=?", note).find(TodoList.class);
        for (TodoList e:todolists){
            todo=e;
        }
        return todo;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        date=dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        String mdate = "will remind you on date: "+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        reminder.setText(mdate);
    }
}
