package be.howest.nmct.sqlitedemo1.viewmodel;

import android.app.LoaderManager;
import android.content.ContentValues;
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
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import be.howest.nmct.sqlitedemo1.BR;
import be.howest.nmct.sqlitedemo1.database.Contract;
import be.howest.nmct.sqlitedemo1.database.DeleteProductTask;
import be.howest.nmct.sqlitedemo1.database.ProductsLoader;
import be.howest.nmct.sqlitedemo1.databinding.FragmentProductsBinding;
import be.howest.nmct.sqlitedemo1.model.Product;

/**
 * Created by Stijn on 29/09/2016.
 */
public class ProductsFragmentViewModel extends BaseObservable implements LoaderManager.LoaderCallbacks<Cursor> {


    private FragmentProductsBinding fragmentProductsBinding;
    private Context context;
    private Cursor mCursor;

    @Bindable
    private ObservableList<Product> productsList;  //= new ObservableArrayList<>() ;

    public ProductsFragmentViewModel(FragmentProductsBinding fragmentProductsBinding, final Context context) {
        this.fragmentProductsBinding = fragmentProductsBinding;
        this.context = context;
        this.fragmentProductsBinding.recyclerviewProducts.setLayoutManager(new LinearLayoutManager(context));
        this.fragmentProductsBinding.recyclerviewProducts.setItemAnimator(new android.support.v7.widget.DefaultItemAnimator());
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                deleteProductFromDB(productsList.get(position));
                productsList.remove(position);
                notifyPropertyChanged(BR.productlist);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(this.fragmentProductsBinding.recyclerviewProducts);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new ProductsLoader(context);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //data van Cursor in ObservableArrayList plaatsen
        mCursor = cursor;
        productsList = new ObservableArrayList<>();

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
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
            } while (cursor.moveToNext());
        }
        this.fragmentProductsBinding.setProductlist(productsList);
        notifyPropertyChanged(BR.productlist);
        //cursor.close();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void deleteProductFromDB(Product p) {
        ContentValues values = new ContentValues();
        values.put(Contract.ProductsColumns.COLUMN_PRODUCT_NR, p.getProductnr());
        Helper.executeAsyncTask(new DeleteProductTask(context), values);
        Snackbar.make(fragmentProductsBinding.getRoot(), "Product deleted out database!", Snackbar.LENGTH_LONG).show();
        //cursor nog niet aangepast
        //mCursor.requery();          //deprecated (todo)

    }


    public void onDestroy() {
        if (!mCursor.isClosed())
            mCursor.close();
    }
}
