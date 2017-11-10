package be.howest.nmct.sqlitedemo1.binders;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.databinding.adapters.TextViewBindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.TextView;

import be.howest.nmct.sqlitedemo1.adaptar.ProductAdaptar;
import be.howest.nmct.sqlitedemo1.model.Product;

/**
 * Created by Stijn on 29/09/2016.
 */
public class ProductsBinder {

    @BindingAdapter("items")
    public static void setItems(RecyclerView recyclerView, ObservableList<Product> products) {
        if (products != null) {
            ProductAdaptar adapter = new ProductAdaptar(products, recyclerView.getContext());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }


    @BindingAdapter("android:text")
    public static void setText(EditText view, int value) {
        if (value > 0)
            view.setText(Integer.toString(value));
        else
            view.setText("");
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static int getText1(EditText view) {
        if (!view.getText().toString().isEmpty())
            return Integer.parseInt(view.getText().toString());
        else
            return -1;
    }

    @BindingAdapter("android:text")
    public static void setText(EditText view, double value) {
        if (value > 0) {
            view.setText(Double.toString(value));
        } else
            view.setText("");
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static double getText2(EditText view) {
        if (!view.getText().toString().isEmpty())
            return Double.parseDouble(view.getText().toString());
        else
            return -1;
    }
//
//    @InverseBindingAdapter(attribute = "android:text", event = "android:textAttrChanged")
//    public static void captureTextValue(TextView view, ObservableField<CharSequence> value) {
//        CharSequence newValue = view.getText();
//        CharSequence oldValue = value.get();
//        if (oldValue == null) {
//            value.set(newValue);
//        } else if (!newValue.equals(oldValue)) {
//            value.set(newValue);
//        }
//    }
//    @BindingAdapter(value = {"android:beforeTextChanged", "android:onTextChanged",
//            "android:afterTextChanged", "android:textAttrChanged"},
//            requireAll = false)
//    public static void setTextWatcher(TextView view, final TextViewBindingAdapter.BeforeTextChanged before,
//                                      final TextViewBindingAdapter.OnTextChanged on, final TextViewBindingAdapter.AfterTextChanged after,
//                                      final InverseBindingListener textAttrChanged) {
//
//    }
}
