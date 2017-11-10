package be.howest.nmct.sqlitedemo1.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import be.howest.nmct.sqlitedemo1.R;
import be.howest.nmct.sqlitedemo1.auth.AuthHelper;
import be.howest.nmct.sqlitedemo1.database.DeleteProductsTask;
import be.howest.nmct.sqlitedemo1.viewmodel.Helper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content);
//        if (savedInstanceState == null) {
//            //ADD INITIAL FRAGMENT
//            //showNewProductFragment();
//            //showProductsFragment();
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (AuthHelper.isUserLoggedIn(this)) {
            showProductsFragment();
        } else {
            //showLoginFragment();
            showSignInSignOutActivity();
        }
    }

    private void showSignInSignOutActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_products, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_show_list_product) {
            showProductsFragment();
            return true;
        }

        if (id == R.id.action_new_product) {
            showNewProductFragment();
            return true;
        }

        if (id == R.id.action_delete_list_product){
            Helper.executeAsyncTask(new DeleteProductsTask(this), null);
            if (getFragmentManager().findFragmentByTag("showProductsFragment") != null ){
                ProductsFragment productsFragment = (ProductsFragment) getFragmentManager().findFragmentByTag("showProductsFragment");
                productsFragment.loadProducts();
            }
            return true;
        }

        if (id == R.id.action_logout){
            showSignInSignOutActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showNewProductFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        NewProductFragment newProductFragment = new NewProductFragment();
        fragmentTransaction.replace(R.id.content, newProductFragment, "newProductFragment");
        fragmentTransaction.commit();
    }

    private void showProductsFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ProductsFragment productsFragment = new ProductsFragment();
        fragmentTransaction.replace(R.id.content, productsFragment, "showProductsFragment");
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            //ik zit terug in mijn eerste fragment.
            //gebruiker klikt nogmaals op back --> app afsluiten
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }



}
