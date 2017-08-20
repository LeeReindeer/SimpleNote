package lee.todo.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
    private ImageView imageView;
    private String date=" ";
    private final static String TAG="AddEditActivity";
    private Realm realm;
    private String imagePath="";

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
        imageView=(ImageView)findViewById(R.id.image_view);
        CalUtil cal=new CalUtil();
        textTime.setText("Edited "+cal.getCurrentDate());
        button.setOnClickListener(this);
        reminder.setOnClickListener(this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else {
                    Toast.makeText(this,"Permissions deny",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
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
        //save();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.image_item){
            if (ActivityCompat.checkSelfPermission(AddEditActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(AddEditActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }else {
                openAlbum();
            }
        }
        return true;
    }


    public void openAlbum(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==0&&resultCode==RESULT_OK){
            if (Build.VERSION.SDK_INT>=19){
                handleImageAfterKitkat(data);
            }else {
                handleImageBeforeKitKat(data);
            }
        }
    }

    @TargetApi(19)
    public void handleImageAfterKitkat(Intent data){
        Uri uri=data.getData();
        String imagePath=null;
        //解析uri
        //document类型
        if (DocumentsContract.isDocumentUri(this,uri)){
            String docId=DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];
                String selection= MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
            //content类型
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            imagePath=getImagePath(uri,null);
            //file类型
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            imagePath=uri.getPath();
        }
        displayImage(imagePath);
    }

    public void handleImageBeforeKitKat(Intent data){
        Uri uri=data.getData();
        String imagePath=getImagePath(uri,null);
        displayImage(imagePath);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.float_buttonIn:
                save();
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

    private void save(){
        realm.beginTransaction();
        SimpleNote aNote=realm.createObject(SimpleNote.class,getNextID());
        //aNote.setId(getNextID());
        aNote.setTitle(editTitle.getText().toString());
        aNote.setNote(editNote.getText().toString());
        if (editNote.getText().toString().isEmpty()){
            realm.cancelTransaction();
            return;
        }
        aNote.setImagePath(imagePath);
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
    }

    public void displayImage(String imagePath){
        if (imagePath!=null){
            this.imagePath=imagePath;
            Glide.with(this)
                 .load(imagePath)
                 .into(imageView);
        }else {
            Toast.makeText(this,"Failed to get image path",Toast.LENGTH_SHORT).show();
        }
    }

    public String getImagePath(Uri uri,String selection){
        String path=null;
        //通过uri和selection来获取真实路径
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if (cursor!=null){
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
}

