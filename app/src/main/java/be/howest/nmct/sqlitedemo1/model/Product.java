package be.howest.nmct.sqlitedemo1.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.widget.EditText;

import be.howest.nmct.sqlitedemo1.R;

/**
 * Created by Stijn on 25/09/2016.
 */
public class Product extends BaseObservable {

    private int productnr = 0;
    private String productname = "";
    private int quantity = 0;
    private double price = 0;
    private String remark = "";


    @Bindable
    public int getProductnr() {
        return productnr;
    }

    public void setProductnr(int productnr) {
        this.productnr = productnr;
    }

    @Bindable
    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    @Bindable
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Bindable
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Bindable
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productnr=" + productnr +
                ", productname='" + productname + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", remark='" + remark + '\'' +
                '}';
    }
}
