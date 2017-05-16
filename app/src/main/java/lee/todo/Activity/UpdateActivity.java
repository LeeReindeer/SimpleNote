package lee.todo.Activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import lee.todo.R;
import lee.todo.Adapter.TodoList;

public class UpdateActivity extends BaseActivity {

    private FloatingActionButton button;
    private EditText editTitle;
    private EditText editNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        button=(FloatingActionButton)findViewById(R.id.float_buttonIn);
        editTitle=(EditText)findViewById(R.id.titleIn);
        editNote=(EditText)findViewById(R.id.noteIn);
        Intent intent=getIntent();
        final String title=intent.getStringExtra("title");
        final String note=intent.getStringExtra("note");
        editTitle.setText(title);
        editNote.setText(note);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoList todo=new TodoList();
                todo.setTitle(editTitle.getText().toString());
                todo.setNote(editNote.getText().toString());
                todo.updateAll("title=? and note=?",title,note);
                finish();
            }
        });
    }
}
