package be.howest.nmct.sqlitedemo1.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;

import be.howest.nmct.sqlitedemo1.R;
import be.howest.nmct.sqlitedemo1.database.Contract;
import be.howest.nmct.sqlitedemo1.database.SaveNewProductToDBTask;
import be.howest.nmct.sqlitedemo1.databinding.FragmentNewProductBinding;
import be.howest.nmct.sqlitedemo1.model.Product;
import be.howest.nmct.sqlitedemo1.viewmodel.NewProductFragmentViewModel;

/**
 * Created by Stijn on 26/09/2016.
 */
public class NewProductFragment extends Fragment {


    private FragmentNewProductBinding binding;


    public NewProductFragment() {
    }

    public static NewProductFragment newInstance() {
        NewProductFragment newProductFragment = new NewProductFragment();
        return newProductFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_new_product,container,false);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_product, container, false);
        NewProductFragmentViewModel newProductFragmentViewModel = new NewProductFragmentViewModel(this.getContext(), binding, this);
        return binding.getRoot();

    }


    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getActivity().getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this.getActivity());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }








}
