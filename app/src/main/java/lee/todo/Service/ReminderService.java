package lee.todo.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;

public class ReminderService extends Service {

    private static final String TAG="ReminderService";
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
        Log.d(TAG,"Service destroy");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"Service created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"Service start");
        return super.onStartCommand(intent, flags, startId);

    }
}
