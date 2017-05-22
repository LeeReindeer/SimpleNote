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

import org.litepal.crud.DataSupport;

import java.util.List;

import lee.todo.R;
import lee.todo.Adapter.TodoList;
import lee.todo.Util.CalUtil;

public class UpdateActivity extends BaseActivity {

    private FloatingActionButton button;
    private EditText editTitle;
    private EditText editNote;
    private TextView textTime;
    private String title;
    private String note;

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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoList todo=new TodoList();
                todo.setTitle(editTitle.getText().toString());
                todo.setNote(editNote.getText().toString());
                todo.setTime(cTime);
                //内容不变，不跟新时间
                if (editNote.getText().toString().equals(note)&&
                        editTitle.getText().toString().equals(title)) {
                    Log.d("sql","No update");
                }else {
                    todo.updateAll("title=? and note=?", title, note);
                    Log.d("sql","Update");
                }
                finish();
            }
        });
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //// TODO: 5/22/17 long click to add notice
                Toast.makeText(UpdateActivity.this,"Notice",Toast.LENGTH_SHORT).show();
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
}
