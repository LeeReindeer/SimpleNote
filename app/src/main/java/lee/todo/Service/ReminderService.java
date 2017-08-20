package lee.todo.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.app.NavUtils;
import android.support.v7.app.NotificationCompat;
import android.util.Log;


import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import lee.todo.Activity.MainActivity;
import lee.todo.Activity.UpdateActivity;
import lee.todo.Adapter.SimpleNote;
import lee.todo.R;
import lee.todo.Util.CalUtil;
import lee.todo.Util.LogUtil;

public class ReminderService extends Service {

    private static final String TAG="ReminderService";
    private Realm realm;

    public ReminderService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG,"Service destroy");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        realm=Realm.getDefaultInstance();
        String title="";
        String note="";
        List<SimpleNote>todoLists=findReminder();
        if (todoLists!=null) {
            Notification notification=null;
            if (todoLists.size() == 1) {
                SimpleNote aNote = null;
                for (SimpleNote each : todoLists) {
                    aNote = each;
                }
                title = aNote.getTitle();
                note = aNote.getNote();
                LogUtil.d(TAG, "title" + title);
                LogUtil.d(TAG, "note" + note);

                Intent updataIntent = new Intent(this, UpdateActivity.class);
                updataIntent.putExtra("title", title);
                updataIntent.putExtra("note", note);
                PendingIntent pi = PendingIntent.getActivity(this, 0, updataIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                notification = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setContentTitle("You have one todo today")
                        .setContentText(title)
                        .setContentIntent(pi)
                        .build();

            }else {
                int todoNum=todoLists.size();
                notification = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setContentTitle("You have "+todoNum+" todos today")
                        .build();
            }
            NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            manager.notify(1,notification);
            //startForeground(1, notification);
        }
        LogUtil.d(TAG,"Service created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d(TAG,"Service start");
        return super.onStartCommand(intent, flags, startId);

    }

    private RealmResults<SimpleNote> findReminder(){
        SimpleNote note= null;
        CalUtil calUtil=new CalUtil();
        //List<TodoList>todoLists= DataSupport.where("remindTime = ?",calUtil.getCurrentDate()).find(TodoList.class);
        RealmQuery<SimpleNote> query=realm.where(SimpleNote.class);
        RealmResults<SimpleNote>result=realm.where(SimpleNote.class)
                                            .equalTo("remindTime",calUtil.getCurrentDate())
                                            .findAll();
        return result;
    }
}
