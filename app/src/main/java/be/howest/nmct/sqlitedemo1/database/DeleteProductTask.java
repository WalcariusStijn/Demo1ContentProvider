package be.howest.nmct.sqlitedemo1.database;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Debug;
import android.util.Log;


/**
 * Created by stijn on 11/10/2017.
 */

public class DeleteProductTask extends AsyncTask<ContentValues, Void, Void> {

    private Context mContext;

    public DeleteProductTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(ContentValues... params) {
        ContentValues contentValues = params[0];
        String productnr = contentValues.getAsString(Contract.ProductsColumns.COLUMN_PRODUCT_NR);
        String[] selectionArgs = new String[]{productnr};
        int rows = mContext.getContentResolver().delete(be.howest.nmct.sqlitedemo1.provider.Contract.PRODUCTS_URI, Contract.ProductsColumns.COLUMN_PRODUCT_NR +"=?", selectionArgs);
        Log.d("Delete product","Aantal verwijderde rijen: " + rows);
        return (null);
    }
}
