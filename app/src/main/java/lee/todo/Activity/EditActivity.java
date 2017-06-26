package lee.todo.Activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.w3c.dom.Text;

import java.util.Calendar;

import lee.todo.R;
import lee.todo.Adapter.TodoList;
import lee.todo.Util.CalUtil;
import lee.todo.Util.LogUtil;

public class EditActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    private FloatingActionButton button;
    private EditText editTitle;
    private EditText editNote;
    private TextView textTime;
    private TextView reminder;
    private String date=" ";
    private final static String TAG="EditActivity";

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

        CalUtil cal=new CalUtil();
        textTime.setText("Edited "+cal.getCurrentDate());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoList todo=new TodoList();
                todo.setTitle(editTitle.getText().toString());
                todo.setNote(editNote.getText().toString());
                CalUtil cal=new CalUtil();
                todo.setTime(cal.getCurrentDate());
                if (!date.equals(" ")) {
                    LogUtil.d(TAG,"save reminder");
                    todo.setRemindTime(date);
                }
                todo.save();
                Toast.makeText(EditActivity.this,"saved",Toast.LENGTH_SHORT).show();
                finish();
                //saveLater();
            }
        });

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //// TODO: 5/22/17 long click to add notice
                Toast.makeText(EditActivity.this,"Notice",Toast.LENGTH_SHORT).show();
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        EditActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
                return false;
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        date=dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        String mdate = "will remind you on date: "+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        reminder.setText(mdate);
    }
}

