package be.howest.nmct.sqlitedemo1.provider;

import android.net.Uri;

/**
 * Created by Stijn on 10/10/2016.
 */
public class Contract {

    public static final String AUTHORITY = "be.howest.nmct.HowestProducts";

    //CONTENT-URIS
    public static final Uri PRODUCTS_URI = Uri.parse("content://" + AUTHORITY + "/products");
    public static final Uri PRODUCTS_ITEM_URI = Uri.parse("content://" + AUTHORITY + "/products/");

    //MIME-TYPES
    public static final String PRODUCTS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.howest.product";
    public static final String PRODUCTS_ITEM_CONTENT_TYPE = "vnd.android.cursor.item/vnd.howest.product";

    public static final int PRODUCT_ID_PATH_POSITION = 1;
}
