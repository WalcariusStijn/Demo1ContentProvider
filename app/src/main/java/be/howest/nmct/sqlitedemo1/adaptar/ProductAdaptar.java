package be.howest.nmct.sqlitedemo1.adaptar;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.howest.nmct.sqlitedemo1.R;
import be.howest.nmct.sqlitedemo1.databinding.RowProduct2Binding;
import be.howest.nmct.sqlitedemo1.model.Product;

/**
 * Created by Stijn on 29/09/2016.
 */
public class ProductAdaptar extends RecyclerView.Adapter<ProductAdaptar.Viewholder> {


    private ObservableList<Product> products = null;

    private Context context;

    public ProductAdaptar(ObservableList<Product> products, Context context) {
        this.products = products;
        this.context = context;     //extra erbij gekomen om activity te kunnen starten vanuit viewholder
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        //RowProductBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_product2, parent, false);
        RowProduct2Binding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_product2, parent, false);
        ProductAdaptar.Viewholder vh = new ProductAdaptar.Viewholder(binding);
        return vh;
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        Product product = products.get(position);
        holder.getBinding().setProduct(product);
        holder.getBinding().executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final RowProduct2Binding binding;

        public Viewholder(RowProduct2Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
            //opgelet: niet vergeten!
            binding.getRoot().setOnClickListener(this);
        }

        public RowProduct2Binding getBinding() {
            return binding;
        }

        @Override
        public void onClick(View v) {
            Product product = products.get(getAdapterPosition());

        }

    }
}
