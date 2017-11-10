package be.howest.nmct.sqlitedemo1.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class GitHubWebViewClient extends WebViewClient {

    private static final String TAG = "GitHubWebViewClient";
    private static final boolean DEBUG = true;

    private Context context;

    private String state;

    private OnGithubWebviewListener mListener;

    public GitHubWebViewClient(Context context, String state, OnGithubWebviewListener onGithubWebviewListener) {
        this.context = context;
        this.state = state;
        this.mListener = onGithubWebviewListener;
    }

    //onderscheppen van Redirect_uri
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);


        if (DEBUG) Log.v(TAG, "+++ URL page Webview: " + url);

        if (url.startsWith(Contract.REDIRECT_URI)) {
            if (DEBUG) Log.d(TAG, "+++ Ontvangst Authorizationcode: " + url);

            Map<String, String> params = getParams(url.split("\\?")[1]);

            if (!params.get("state").equals(state)) {
                if (DEBUG) Log.d(TAG, "+++ state is invalid");
                throw new RuntimeException("state is invalid");
            }

            String code = params.get("code");
            GetTokenTask getAccessToken = new GetTokenTask(context, view);
            getAccessToken.execute(code);
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false; //altijd deze webviewclient gaan gebruiken om url te overloaden
    }

    public static Map<String, String> getParams(String s) {
        String[] keyValues = s.split("&");
        Map<String, String> params = new HashMap<String, String>(keyValues.length);
        for (String keyValue : keyValues) {
            String[] pair = keyValue.split("=");
            if (pair.length == 2)
                params.put(pair[0], pair[1]);
            else
                params.put(pair[0], null);
        }
        return params;
    }

    public interface OnGithubWebviewListener {
        // TODO: Update argument type and name
        public void resultTokens(String jsonUserInfo, String accessToken, String error);
    }


    class GetTokenTask extends AsyncTask<String, Void, GetTokenTask.Data> {

        class Data {
            String userinfo;
            String accessToken;
            String error;
        }


        private ProgressDialog pDialog;
        private Context mContext;
        private WebView webView;

        private static final String TAG = "GetTokenTask";
        private static final boolean DEBUG = true;

        public GetTokenTask(Context context, WebView view) {
            mContext = context;
            webView = view;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(mContext);
            pDialog.setMessage("Contacting GitHub ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected Data doInBackground(String... strings) {
            return getToken(strings[0]);
        }

        //zie stap 2
        public Data getToken(String code) {
            Data data = new Data();
            try {
                if (DEBUG) Log.d(TAG, "+++ AccessToken Start ");

                data.accessToken = AuthHelper.getAccessToken(code);
                if (DEBUG) Log.d(TAG, "+++ AccessToken received: " + data.accessToken);

                //TokenStore.storeAccessToken(mContext, accessToken);

                if (DEBUG) Log.d(TAG, "+++ UserInfo Request");
                data.userinfo = AuthHelper.getUser(data.accessToken);

            } catch (UnsupportedEncodingException e) {
                Log.e("UnsupportedEncodingEx", "Description: " + e.getMessage());
                data.error = "UnsupportedEncodingEx";
            } catch (IOException e) {
                Log.e("IOException", "Description: " + e.getMessage());
                data.error = "IOException";
                e.printStackTrace();
            } catch (Exception e) {
                Log.e("Exception", "Error converting result " + e.getMessage());
                data.error = "Exception";
            }
            return data;
        }

        @Override
        protected void onPostExecute(Data data) {
            pDialog.dismiss();
            //if (DEBUG) Log.d(TAG, "+++ onPostExecute: " + json);
            webView.loadData(data.userinfo, "application/json", "utf-8");
            //data teruggeven
            mListener.resultTokens(data.userinfo, data.accessToken, data.error);
        }

    }
}
