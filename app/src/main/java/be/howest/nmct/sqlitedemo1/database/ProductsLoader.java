package be.howest.nmct.sqlitedemo1.database;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import be.howest.nmct.sqlitedemo1.model.Product;
import be.howest.nmct.sqlitedemo1.provider.*;

/**
 * Created by Stijn on 26/09/2016.
 */
public class ProductsLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mData;
    private Context mContext;

    public ProductsLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        }


        if (takeContentChanged() || mData == null) {
            forceLoad();
        }
    }

    @Override
    public Cursor loadInBackground() {
        String[] columns = new String[]{
                Contract.ProductsColumns._ID,
                Contract.ProductsColumns.COLUMN_PRODUCT_NR,
                Contract.ProductsColumns.COLUMN_PRODUCT_NAME,
                Contract.ProductsColumns.COLUMN_QUANTITY,
                Contract.ProductsColumns.COLUMN_PRICE,
                Contract.ProductsColumns.COLUMN_REMARK,
        };

//        DatabaseHelper helper = DatabaseHelper.getInstance(getContext());
//        SQLiteDatabase db = helper.getReadableDatabase();
//        mData = db.query(Contract.ProductsDB.TABLE_NAME,
//                columns,
//                null,
//                null,
//                null,
//                null,
//                Contract.ProductsColumns.COLUMN_PRODUCT_NR + " ASC"
//        );


        mData = getContext().getContentResolver().query(be.howest.nmct.sqlitedemo1.provider.Contract.PRODUCTS_URI,
                columns, null, null, null);

        //door te tellen hoeveel records er zijn, zijn we zeker dat de data binnen gehaald is
        mData.getCount();

        return mData;
    }

    @Override
    public void deliverResult(Cursor cursor) {
        if (isReset()) {
            if (cursor != null) {
                cursor.close();
            }
            return;
        }

        Cursor oldData = mData;
        mData = cursor;

        if (isStarted()) {
            super.deliverResult(cursor);
        }

        if (oldData != null && oldData != cursor && !oldData.isClosed()) {
            oldData.close();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }
}
