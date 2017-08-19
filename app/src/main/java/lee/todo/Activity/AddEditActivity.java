package lee.todo.Activity;

import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import io.realm.Realm;
import lee.todo.Adapter.SimpleNote;
import lee.todo.R;
import lee.todo.Util.CalUtil;
import lee.todo.Util.LogUtil;

public class AddEditActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener,View.OnClickListener {

    private FloatingActionButton button;
    private EditText editTitle;
    private EditText editNote;
    private TextView textTime;
    private TextView reminder;
    private String date=" ";
    private final static String TAG="AddEditActivity";
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);
        initUI();
        realm=Realm.getDefaultInstance();
    }

    private void initUI(){
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        button=(FloatingActionButton)findViewById(R.id.float_buttonIn);
        editTitle=(EditText)findViewById(R.id.titleIn);
        editNote=(EditText)findViewById(R.id.noteIn);
        textTime=(TextView)findViewById(R.id.time_text);
        reminder=(TextView)findViewById(R.id.reminder_text);
        CalUtil cal=new CalUtil();
        textTime.setText("Edited "+cal.getCurrentDate());
        button.setOnClickListener(this);
        reminder.setOnClickListener(this);
    }

    public int getNextID(){
        try{
            Number id=realm.where(SimpleNote.class).max("id");
            if (id!=null){
                return id.intValue()+1;
            }else {
                return 0;
            }
        }catch (ArrayIndexOutOfBoundsException e){
            return 0;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        date=dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        String mdate = "will remind you on date: "+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        reminder.setText(mdate);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reminder.setText("No reminder");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.float_buttonIn:
                realm.beginTransaction();
                SimpleNote aNote=realm.createObject(SimpleNote.class,getNextID());
                //aNote.setId(getNextID());
                aNote.setTitle(editTitle.getText().toString());
                aNote.setNote(editNote.getText().toString());
                CalUtil cal=new CalUtil();
                aNote.setTime(cal.getCurrentDate());
                if (date.equals(" ")) {
                    date="No reminder";
                }
                aNote.setRemindTime(date);
                realm.commitTransaction();
                Toast.makeText(AddEditActivity.this,"saved",Toast.LENGTH_SHORT).show();
                LogUtil.d(TAG,"pickDate"+date);
                CalUtil   calUtil=new CalUtil();
                LogUtil.d(TAG,"CalDate"+calUtil.getCurrentDate());
                finish();
                break;
            case R.id.reminder_text:
                Toast.makeText(AddEditActivity.this,"Notice",Toast.LENGTH_SHORT).show();
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        AddEditActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
                break;
        }
    }
}

