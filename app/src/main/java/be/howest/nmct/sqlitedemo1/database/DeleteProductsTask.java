package be.howest.nmct.sqlitedemo1.database;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import be.howest.nmct.sqlitedemo1.provider.*;

/**
 * Created by stijn on 11/10/2017.
 */

public class DeleteProductsTask extends AsyncTask<ContentValues, Void, Void> {

    private Context mContext;

    public DeleteProductsTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(ContentValues... params) {
        int rows = mContext.getContentResolver().delete(be.howest.nmct.sqlitedemo1.provider.Contract.PRODUCTS_URI, null, null);

        return (null);
    }
}
