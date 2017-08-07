package lee.todo.Util;

import android.util.Log;

/**
 * @Author lee
 * @Time 6/26/17.
 */

public class LogUtil {
    private final static int NOTHING=6;
    private final static int VERBOSE=1;
    private final static int DEBUG=2;
    private final static int INFO=3;
    private final static int WARN=4;
    private final static int ERROR=5;
    private final static int level=VERBOSE;

    public static void v(String TAG,String msg){
        if (level<=VERBOSE)
            Log.v(TAG,msg);
    }

    public static void d(String TAG,String msg){
        if (level<=DEBUG)
            Log.d(TAG,msg);
    }

    public static void i(String TAG,String msg){
        if (level<=INFO)
            Log.i(TAG,msg);
    }

    public static void w(String TAG,String msg){
        if (level<=WARN)
            Log.w(TAG,msg);
    }

    public static void e(String TAG,String msg){
        if (level<=ERROR)
            Log.e(TAG,msg);
    }
}
