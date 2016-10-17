package be.howest.nmct.sqlitedemo1.view;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.howest.nmct.sqlitedemo1.R;
import be.howest.nmct.sqlitedemo1.databinding.FragmentNewProductBinding;
import be.howest.nmct.sqlitedemo1.databinding.FragmentProductsBinding;
import be.howest.nmct.sqlitedemo1.viewmodel.ProductsFragmentViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragment extends Fragment {

    private FragmentProductsBinding binding;

    private ProductsFragmentViewModel productsFragmentViewModel;

    public ProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_products, container, false);
        binding.recyclerviewProducts.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        binding.recyclerviewProducts.setItemAnimator(new android.support.v7.widget.DefaultItemAnimator());
        productsFragmentViewModel = new ProductsFragmentViewModel(binding, getContext());
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        getLoaderManager().initLoader(0, null, productsFragmentViewModel);       //0: id van loader
    }


}
