package be.howest.nmct.sqlitedemo1.viewmodel;

import android.content.ContentValues;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.util.Log;

import be.howest.nmct.sqlitedemo1.database.Contract;
import be.howest.nmct.sqlitedemo1.database.SaveNewProductToDBTask;
import be.howest.nmct.sqlitedemo1.databinding.FragmentNewProductBinding;
import be.howest.nmct.sqlitedemo1.model.Product;
import be.howest.nmct.sqlitedemo1.view.NewProductFragment;
import be.howest.nmct.sqlitedemo1.view.ProductsFragment;

/**
 * Created by Stijn on 26/09/2016.
 */
public class NewProductFragmentViewModel extends BaseObservable {

    private FragmentNewProductBinding binding;
    private NewProductFragment newProductFragment;

    @Bindable
    private Product newProduct = new Product();

    private Context context;

    public NewProductFragmentViewModel(Context context, FragmentNewProductBinding fragmentNewProductBinding, NewProductFragment newProductFragment) {
        this.context = context;
        this.binding = fragmentNewProductBinding;
        this.binding.setNewproduct(newProduct);
        this.binding.setViewmodel(this);
        this.newProductFragment = newProductFragment;
    }

    public void saveNewProduct() {
        //opslaan van het nieuwe product
        Log.d("ViewModel", "saving to sqlite");
        saveProductToDb();
        resetProduct();
        newProductFragment.showSnackbar("Product saved!");
        newProductFragment.hideKeyboard();
        Log.d("ViewModel", "saved to sqlite");
        //Snackbar.make(context., "Product saved into database!", Snackbar.LENGTH_LONG).show();
    }


/*
    public View.OnClickListener saveProduct(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("ViewModel","saving to sqlite");
                Snackbar.make(null, "Product student: ", Snackbar.LENGTH_LONG).show();
            }
        };
    }
*/


    private void saveProductToDb() {

        ContentValues values = new ContentValues();
        values.put(Contract.ProductsColumns.COLUMN_PRODUCT_NR, newProduct.getProductnr());
        values.put(Contract.ProductsColumns.COLUMN_PRICE, newProduct.getPrice());
        values.put(Contract.ProductsColumns.COLUMN_PRODUCT_NAME, newProduct.getProductname());
        values.put(Contract.ProductsColumns.COLUMN_QUANTITY, newProduct.getQuantity());
        values.put(Contract.ProductsColumns.COLUMN_REMARK, newProduct.getRemark());

        executeAsyncTask(new SaveNewProductToDBTask(context), values);
    }

    private void resetProduct() {
        newProduct.setProductname("");
        newProduct.setProductnr(0);
    }

    static private <T> void executeAsyncTask(AsyncTask<T, ?, ?> task, T... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }
}
