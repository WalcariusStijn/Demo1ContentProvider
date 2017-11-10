package be.howest.nmct.sqlitedemo1.view;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.OnAccountsUpdateListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.UUID;

import be.howest.nmct.sqlitedemo1.R;
import be.howest.nmct.sqlitedemo1.auth.AuthHelper;
import be.howest.nmct.sqlitedemo1.auth.Contract;
import be.howest.nmct.sqlitedemo1.auth.GitHubWebViewClient;

public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener, GitHubWebViewClient.OnGithubWebviewListener {

    private static final String TAG = "LoginActivity";

    /* Keys for persisting instance variables in savedInstanceState */
    private static final String KEY_IS_RESOLVING = "is_resolving";
    private static final String KEY_SHOULD_RESOLVE = "should_resolve";


    /* View to display current status (signed-in, signed-out, disconnected, etc) */
    private TextView mStatus;

    // [START resolution_variables]
    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;

    private AppCompatEditText editTextUseremail;
    private AppCompatEditText editTextPassword;
    private LinearLayout linearLayout;
    private String token = "";

    private AccountManager mAccountManager;
    private AccountAuthenticatorResponse mAccountAuthenticatorResponse;
    private String mFullName;
    private String mEmail;

    //listener voor AccountManager -> update UI
    private OnAccountsUpdateListener onAccountsUpdateListener;

    //Week 6
    private WebView webView;
    private String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.loginButton).setOnClickListener(this);
        findViewById(R.id.logoutButton).setOnClickListener(this);

        if (savedInstanceState != null) {
            mIsResolving = savedInstanceState.getBoolean(KEY_IS_RESOLVING);
            mShouldResolve = savedInstanceState.getBoolean(KEY_SHOULD_RESOLVE);
        }

        //Week 6: login-activity laat toe om in te loggen via Github account
        state = UUID.randomUUID().toString();
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        GitHubWebViewClient gitHubWebViewClient = new GitHubWebViewClient(this, state, this);
        webView.setWebViewClient(gitHubWebViewClient);


        mAccountManager = AccountManager.get(this);
        mAccountAuthenticatorResponse = this.getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
        if (mAccountAuthenticatorResponse != null) {
            mAccountAuthenticatorResponse.onRequestContinued();
        }

        //UI aanpassen
        updateUI(AuthHelper.isUserLoggedIn(this));
    }


    @Override
    protected void onResume() {
        super.onResume();
        onAccountsUpdateListener = new OnAccountsUpdateListener() {
            @Override
            public void onAccountsUpdated(Account[] accounts) {
                updateUI(AuthHelper.isUserLoggedIn(LoginActivity.this));
            }
        };

        mAccountManager.addOnAccountsUpdatedListener(onAccountsUpdateListener, null, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (onAccountsUpdateListener != null)
            mAccountManager.removeOnAccountsUpdatedListener(onAccountsUpdateListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                onSignInClicked();
                break;
            case R.id.logoutButton:
                onSignOut();
                break;
        }
    }

    //Week 6: NIEUW -> we verwijzen de gebruiker door naar Github
    //logica zit in GithubWebViewClient
    private void onSignInClicked() {
//        mEmail = editTextUseremail.getText().toString();
//        String passwordString = editTextPassword.getText().toString();
//        addAccount(mEmail);
        webView.loadUrl(AuthHelper.getAuthorizeUrl(state));
    }


    private void onSignOut() {
        AuthHelper.logUserOff(this);

        Snackbar snackbar = Snackbar
                .make(linearLayout, "Logged out successfully", Snackbar.LENGTH_LONG)
                .setAction("Close app?", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.finishAffinity(LoginActivity.this);
                    }
                });
        snackbar.show();

    }


    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            findViewById(R.id.loginButton).setVisibility(View.GONE);
            findViewById(R.id.webView).setVisibility(View.GONE);
            findViewById(R.id.logoutButton).setVisibility(View.VISIBLE);
        } else {
            // Show signed-out message and clear email field
            findViewById(R.id.loginButton).setVisibility(View.VISIBLE);
            findViewById(R.id.webView).setVisibility(View.VISIBLE);
            findViewById(R.id.logoutButton).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_RESOLVING, mIsResolving);
        outState.putBoolean(KEY_SHOULD_RESOLVE, mShouldResolve);
    }

    //Week 6: username & accesstoken binnen
    private void addAccount(String userNameString, String accesstoken) {
        Account[] accountsByType = mAccountManager.getAccountsByType(Contract.ACCOUNT_TYPE);
        Account account;
        if (accountsByType.length == 0) {
            // nog geen account aanwezig
            account = new Account(userNameString, Contract.ACCOUNT_TYPE);
            mAccountManager.addAccountExplicitly(account, null, null);
        } else if (!userNameString.equals(accountsByType[0].name)) {
            // er bestaat reeds een account met andere naam
            mAccountManager.removeAccount(accountsByType[0], this, null, null);
            account = new Account("test", Contract.ACCOUNT_TYPE);
            mAccountManager.addAccountExplicitly(account, null, null);
        } else {
            // account met de zelfde username terug gevonden
            account = accountsByType[0];
        }

        //accesstoken aan account hangen
        if (accesstoken != null)
            mAccountManager.setAuthToken(account, "access_token", accesstoken);

        Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, userNameString);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Contract.ACCOUNT_TYPE);

        if (mAccountAuthenticatorResponse != null) {
            Bundle bundle = intent.getExtras();
            bundle.putString(AccountManager.KEY_ACCOUNT_NAME, userNameString);
            bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, Contract.ACCOUNT_TYPE);
            mAccountAuthenticatorResponse.onResult(bundle);
        }

        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //als user niet meer ingelogd is, en de gebruiker drukt op back-button ~> app laten afsluiten
        if (!AuthHelper.isUserLoggedIn(this)) {
            ActivityCompat.finishAffinity(LoginActivity.this);
        }
    }

    //Week 6: luisteren naar GithubWebViewClient
    @Override
    public void resultTokens(String jsonUserInfo, String accessToken, String error) {
        if (jsonUserInfo != null && accessToken != null)
            addAccount(jsonUserInfo, accessToken);
    }
}

