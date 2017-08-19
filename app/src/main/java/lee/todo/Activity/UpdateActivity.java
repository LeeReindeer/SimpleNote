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

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import lee.todo.Adapter.SimpleNote;
import lee.todo.R;
import lee.todo.Util.CalUtil;
import lee.todo.Util.LogUtil;

public class UpdateActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener,View.OnClickListener {

    private FloatingActionButton button;
    private EditText editTitle;
    private EditText editNote;
    private TextView textTime;
    private TextView reminder;
    private String title;
    private String note;
    private int id;
    private String date=" ";
    String time;
    String cTime;
    private final static String TAG="UpdateActivity";
    private final static int LASTTIME=1;
    private final static int NOWTIME=2;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);
        realm=Realm.getDefaultInstance();
        initUI();
    }

    private void initUI(){
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
        id=intent.getIntExtra("id",-1);
        LogUtil.d(TAG,"title"+title);
        LogUtil.d(TAG,"note"+note);
        time=getTime(LASTTIME);
        cTime=getTime(NOWTIME);
        LogUtil.d(TAG,"c time"+cTime);
        editTitle.setText(title);
        editNote.setText(note);
        textTime.setText("Edited "+time);
        if (findOne()!=null) {
            reminder.setText("Will remind you on date: " + findOne().getRemindTime());
        }
        if (reminder.getText().toString().equals("Will remind you on date: null")){
            reminder.setText("No reminder");
        }
        button.setOnClickListener(this);
        reminder.setOnClickListener(this);
    }

    private String getTime(int key){
        String time=null;
        switch (key) {
            //saved time
            case LASTTIME:
                try {
                    time=findOne().getTime();
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            //current time
            case NOWTIME:
                CalUtil calUtil=new CalUtil();
                time=calUtil.getCurrentDate();
        }
        return time;
    }

    private SimpleNote findOne(){
        SimpleNote aNote=null;
        try {
            RealmQuery<SimpleNote> query=realm.where(SimpleNote.class);
            aNote=query.equalTo("id",id).findFirst();
        }catch (Exception e){
            e.printStackTrace();
        }
        return aNote;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        date=dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        String mdate = "will remind you on date: "+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        reminder.setText(mdate);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.float_buttonIn:
                final SimpleNote aNote=new SimpleNote();
                String mTitle=editTitle.getText().toString();
                String mNote=editNote.getText().toString();
                aNote.setTime(cTime);
                aNote.setId(id);
                boolean isChanged=!date.equals(" ")||!date.equals(findOne().getRemindTime())
                        ||!mNote.equals(note)||!mTitle.equals(title);

                aNote.setTitle(mTitle);
                aNote.setNote(mNote);
                //更新编辑时间
                if (isChanged){
                    aNote.setRemindTime(date);
                    LogUtil.d(TAG,"save reminder");
                    LogUtil.d(TAG," data Update");
                }
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        //update
                        realm.copyToRealmOrUpdate(aNote);
                    }
                });
                finish();
                break;
            case R.id.reminder_text:
                Toast.makeText(UpdateActivity.this,"Notice",Toast.LENGTH_SHORT).show();
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        UpdateActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
                break;
        }
    }
}
