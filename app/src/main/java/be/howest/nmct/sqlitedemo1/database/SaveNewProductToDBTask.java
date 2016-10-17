package be.howest.nmct.sqlitedemo1.database;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import be.howest.nmct.sqlitedemo1.provider.*;
import be.howest.nmct.sqlitedemo1.provider.Contract;

/**
 * Created by Stijn on 26/09/2016.
 */
public class SaveNewProductToDBTask extends AsyncTask<ContentValues, Void, Void> {

    private Context mContext;

    public SaveNewProductToDBTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(ContentValues... values) {
//        long insertId = DatabaseHelper.getInstance(mContext).getWritableDatabase().insert(
//                Contract.ProductsDB.TABLE_NAME, Contract.ProductsDB.COLUMN_PRODUCT_NR,values[0]);

        Uri newUri = mContext.getContentResolver().insert(Contract.PRODUCTS_URI, values[0]);

        return (null);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
