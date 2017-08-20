package lee.todo;

import android.app.Application;

import io.realm.Realm;

/**
 * @Author lee
 * @Time 8/19/17.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}