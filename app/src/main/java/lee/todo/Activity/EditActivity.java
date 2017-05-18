package lee.todo.Activity;

import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import lee.todo.R;
import lee.todo.Adapter.TodoList;
import lee.todo.Util.CalUtil;

public class EditActivity extends BaseActivity {

    private FloatingActionButton button;
    private EditText editTitle;
    private EditText editNote;
    private TextView textTime;

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
        CalUtil cal=new CalUtil();
        textTime.setText("Edited "+cal.getCurrentDate());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoList todo=new TodoList();
                todo.setTitle(editTitle.getText().toString());
                todo.setNote(editNote.getText().toString());
                todo.save();
                Toast.makeText(EditActivity.this,"saved",Toast.LENGTH_SHORT).show();
                finish();
                //saveLater();
            }
        });
    }

}

