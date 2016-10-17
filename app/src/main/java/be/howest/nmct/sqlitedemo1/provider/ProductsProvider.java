package be.howest.nmct.sqlitedemo1.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.HashMap;

import be.howest.nmct.sqlitedemo1.database.DatabaseHelper;

/**
 * Created by Stijn on 10/10/2016.
 */
public class ProductsProvider extends ContentProvider {

    private DatabaseHelper databaseHelper;
    //private static final String PRODUCTS_DB = "Products";

    private static final int PRODUCTS = 1;
    private static final int PRODUCTS_ID = 2;

    private static HashMap<String, String> PRODUCTS_PROJECTION_MAP;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Contract.AUTHORITY, "products", PRODUCTS);
        uriMatcher.addURI(Contract.AUTHORITY, "products/#", PRODUCTS_ID);
    }

    @Override
    public boolean onCreate() {
        databaseHelper = DatabaseHelper.getInstance(getContext());
        PRODUCTS_PROJECTION_MAP = new HashMap<>();
        PRODUCTS_PROJECTION_MAP.put(be.howest.nmct.sqlitedemo1.database.Contract.ProductsColumns._ID, be.howest.nmct.sqlitedemo1.database.Contract.ProductsColumns._ID);
        PRODUCTS_PROJECTION_MAP.put(be.howest.nmct.sqlitedemo1.database.Contract.ProductsColumns.COLUMN_PRODUCT_NAME, be.howest.nmct.sqlitedemo1.database.Contract.ProductsColumns.COLUMN_PRODUCT_NAME);
        PRODUCTS_PROJECTION_MAP.put(be.howest.nmct.sqlitedemo1.database.Contract.ProductsColumns.COLUMN_QUANTITY, be.howest.nmct.sqlitedemo1.database.Contract.ProductsColumns.COLUMN_QUANTITY);
        PRODUCTS_PROJECTION_MAP.put(be.howest.nmct.sqlitedemo1.database.Contract.ProductsColumns.COLUMN_REMARK, be.howest.nmct.sqlitedemo1.database.Contract.ProductsColumns.COLUMN_REMARK);
        PRODUCTS_PROJECTION_MAP.put(be.howest.nmct.sqlitedemo1.database.Contract.ProductsColumns.COLUMN_PRODUCT_NR, be.howest.nmct.sqlitedemo1.database.Contract.ProductsColumns.COLUMN_PRODUCT_NR);
        PRODUCTS_PROJECTION_MAP.put(be.howest.nmct.sqlitedemo1.database.Contract.ProductsColumns.COLUMN_PRICE, be.howest.nmct.sqlitedemo1.database.Contract.ProductsColumns.COLUMN_PRICE);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
            case PRODUCTS:
                queryBuilder.setTables(be.howest.nmct.sqlitedemo1.database.Contract.ProductsDB.TABLE_NAME);
                queryBuilder.setProjectionMap(PRODUCTS_PROJECTION_MAP);
                break;

            case PRODUCTS_ID:
                queryBuilder.setTables(be.howest.nmct.sqlitedemo1.database.Contract.ProductsDB.TABLE_NAME);
                queryBuilder.setProjectionMap(PRODUCTS_PROJECTION_MAP);

                String productid = uri.getPathSegments().get(Contract.PRODUCT_ID_PATH_POSITION);
                DatabaseUtils.concatenateWhere(selection, "( " + be.howest.nmct.sqlitedemo1.database.Contract.ProductsColumns._ID + " = ?" + ")"); //strict genomen haakjes niet nodig
                selectionArgs = DatabaseUtils.appendSelectionArgs(selectionArgs, new String[]{"" + productid});

                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor data = queryBuilder.query(
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        data.getCount();

        data.setNotificationUri(getContext().getContentResolver(), uri);
        return data;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case PRODUCTS:
                return Contract.PRODUCTS_CONTENT_TYPE;
            case PRODUCTS_ID:
                return Contract.PRODUCTS_ITEM_CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case PRODUCTS:
                long newRowId = db.insert(
                        be.howest.nmct.sqlitedemo1.database.Contract.ProductsDB.TABLE_NAME, null, values);
                if (newRowId > 0) {
                    Uri productItemUri = ContentUris.withAppendedId(Contract.PRODUCTS_ITEM_URI, newRowId);
                    getContext().getContentResolver().notifyChange(productItemUri, null);
                    return productItemUri;
                }

                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String finalWhere;
        int count;
        switch (uriMatcher.match(uri)) {
            case PRODUCTS:
                count = db.delete(
                        be.howest.nmct.sqlitedemo1.database.Contract.ProductsDB.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            case PRODUCTS_ID:
                String productItemId = uri.getPathSegments().get(1);
                finalWhere = "Id = " + productItemId;

                if (selection != null) {
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.delete(
                        be.howest.nmct.sqlitedemo1.database.Contract.ProductsDB.TABLE_NAME,
                        finalWhere,
                        selectionArgs
                );
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int count;
        String finalWhere;

        switch (uriMatcher.match(uri)) {
            case PRODUCTS:
                count = db.update(
                        be.howest.nmct.sqlitedemo1.database.Contract.ProductsDB.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;

            case PRODUCTS_ID:
                String productId = uri.getPathSegments().get(1);
                finalWhere = "Id = " + productId;

                if (selection != null) {
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.update(
                        be.howest.nmct.sqlitedemo1.database.Contract.ProductsDB.TABLE_NAME,
                        values,
                        finalWhere,
                        selectionArgs
                );
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }
}

