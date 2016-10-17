package be.howest.nmct.sqlitedemo1.viewmodel;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import be.howest.nmct.sqlitedemo1.BR;
import be.howest.nmct.sqlitedemo1.database.Contract;
import be.howest.nmct.sqlitedemo1.database.ProductsLoader;
import be.howest.nmct.sqlitedemo1.databinding.FragmentProductsBinding;
import be.howest.nmct.sqlitedemo1.model.Product;

/**
 * Created by Stijn on 29/09/2016.
 */
public class ProductsFragmentViewModel extends BaseObservable implements LoaderManager.LoaderCallbacks<Cursor> {


    private FragmentProductsBinding fragmentProductsBinding;
    private Context context;

    @Bindable
    private ObservableList<Product> productsList;  //= new ObservableArrayList<>() ;

    public ProductsFragmentViewModel(FragmentProductsBinding fragmentProductsBinding, Context context) {
        this.fragmentProductsBinding = fragmentProductsBinding;
        this.context = context;
//        this.fragmentProductsBinding.setProductlist(productsList);
    }



    static private <T> void executeAsyncTask(AsyncTask<T, ?, ?> task, T... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new ProductsLoader(context);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //data van Cursor in ObservableArrayList plaatsen
        productsList = new ObservableArrayList<>();
        while (cursor.moveToNext()){
            String[] columns = new String[]{Contract.ProductsColumns.COLUMN_PRODUCT_NR,
                    Contract.ProductsColumns.COLUMN_PRODUCT_NAME,
                    Contract.ProductsColumns.COLUMN_PRICE,
                    Contract.ProductsColumns.COLUMN_QUANTITY,
                    Contract.ProductsColumns.COLUMN_REMARK
            };
            Product product = new Product();
            product.setProductname(cursor.getString(cursor.getColumnIndex(Contract.ProductsColumns.COLUMN_PRODUCT_NAME)));
            product.setProductnr(cursor.getInt(cursor.getColumnIndex(Contract.ProductsColumns.COLUMN_PRODUCT_NR)));
            product.setPrice(cursor.getDouble(cursor.getColumnIndex(Contract.ProductsColumns.COLUMN_PRICE)));
            product.setQuantity(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Contract.ProductsColumns.COLUMN_QUANTITY))));
            product.setRemark(cursor.getString(cursor.getColumnIndex(Contract.ProductsColumns.COLUMN_REMARK)));
            productsList.add(product);
        }
        this.fragmentProductsBinding.setProductlist(productsList);
        notifyPropertyChanged(BR.productlist);

        cursor.close();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
