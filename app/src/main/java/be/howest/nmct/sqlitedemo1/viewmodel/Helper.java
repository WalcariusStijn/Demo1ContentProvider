package be.howest.nmct.sqlitedemo1.viewmodel;

import android.os.AsyncTask;
import android.os.Build;

/**
 * Created by stijn on 11/10/2017.
 */

public class Helper {

    static public<T> void executeAsyncTask(AsyncTask<T, ?, ?> task, T... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }
}
