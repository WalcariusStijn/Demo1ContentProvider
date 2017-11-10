package be.howest.nmct.sqlitedemo1.viewmodel;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import be.howest.nmct.sqlitedemo1.database.Contract;
import be.howest.nmct.sqlitedemo1.database.SaveNewProductToDBTask;
import be.howest.nmct.sqlitedemo1.databinding.FragmentNewProductBinding;
import be.howest.nmct.sqlitedemo1.model.Product;
import be.howest.nmct.sqlitedemo1.view.NewProductFragment;
import be.howest.nmct.sqlitedemo1.view.ProductsFragment;


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
        Snackbar.make(binding.getRoot(), "Product saved!", Snackbar.LENGTH_LONG).show();
        newProductFragment.hideKeyboard();
        Log.d("ViewModel", "saved to sqlite");
     }






    private void saveProductToDb() {

        ContentValues values = new ContentValues();
        values.put(Contract.ProductsColumns.COLUMN_PRODUCT_NR, newProduct.getProductnr());
        values.put(Contract.ProductsColumns.COLUMN_PRICE, newProduct.getPrice());
        values.put(Contract.ProductsColumns.COLUMN_PRODUCT_NAME, newProduct.getProductname());
        values.put(Contract.ProductsColumns.COLUMN_QUANTITY, newProduct.getQuantity());
        values.put(Contract.ProductsColumns.COLUMN_REMARK, newProduct.getRemark());

        Helper.executeAsyncTask(new SaveNewProductToDBTask(context), values);
    }

    private void resetProduct() {
        newProduct.setProductname("");
        newProduct.setProductnr(0);
    }


}
