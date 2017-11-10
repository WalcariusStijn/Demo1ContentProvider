package be.howest.nmct.sqlitedemo1.auth;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class AuthHelper {

    private static AccountManager mAccountManager;
    private static AccountAuthenticatorResponse mAccountAuthenticatorResponse;

    public static String getUsername(Context context){
        mAccountManager = AccountManager.get(context);

        Account[] accounts =  mAccountManager.getAccountsByType(Contract.ACCOUNT_TYPE);

        if(accounts.length>0){
            return accounts[0].name;
        }
        else return null;
    }

    public static Boolean isUserLoggedIn(Context context){
        mAccountManager = AccountManager.get(context);
        Account[] accounts =  mAccountManager.getAccountsByType(Contract.ACCOUNT_TYPE);
        if(accounts.length>0){
            return true;
        }
        else return false;

    }

    public static void logUserOff(Context context) {
        mAccountManager = AccountManager.get(context);
        Account[] accounts = mAccountManager.getAccountsByType(Contract.ACCOUNT_TYPE);
        for (int index = 0; index < accounts.length; index++) {
            mAccountManager.removeAccount(accounts[index], null, null,null);
        }

    }

    public static String getAuthorizeUrl(String state) {
        return String.format(Contract.AUTHORISATION_URL, Contract.CLIENT_ID, Contract.REDIRECT_URI, state);
    }

    public static String getAccessToken(String code) throws  Exception{
        String urlParameters = String.format(Contract.ACCESS_TOKEN_BODY, Contract.CLIENT_ID, Contract.CLIENT_SECRET, code, Contract.REDIRECT_URI);
        URL url = new URL(Contract.ACCESS_TOKEN_URL);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
        connection.setUseCaches(false);

        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        wr.write(urlParameters);
        wr.flush();
        wr.close();

        BufferedReader rdr = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = rdr.readLine()) != null)
            sb.append(line);

        rdr.close();
        connection.disconnect();

        Map<String, String> params = getParams(sb.toString());
        String accessToken = params.get("access_token");
        return accessToken;
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

    public static String getUser(String accessToken) throws Exception {

        URL url = new URL(Contract.USER_URL);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Authorization", "token " + accessToken);
        connection.setUseCaches (false);

        BufferedReader rdr = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while((line = rdr.readLine()) != null)
            sb.append(line);

        rdr.close();
        connection.disconnect();

        return sb.toString();

    }

}
