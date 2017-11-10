package be.howest.nmct.sqlitedemo1.auth;

/**
 * Created by Stijn on 15/10/2016.
 */
public class Contract {
    public static final String ACCOUNT_TYPE = "be.howest.nmct.howestproducts.account";

    //week 6 - uitbreiding
    public static final String CLIENT_ID = "558e8aa64f7bcfd83cc3";
    public static final String CLIENT_SECRET = "b33ca2f585badd054af2625633433ac61ff91b11";
    public static final String REDIRECT_URI = "http://www.howest.be/bad_url";

    public static final String AUTHORISATION_URL = "https://github.com/login/oauth/authorize?client_id=%s&redirect_uri=%s&state=%s";

    public static final String ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
    public static final String ACCESS_TOKEN_BODY = "client_id=%s&client_secret=%s&code=%s&redirect_uri=%s";

    public static final String USER_URL = "https://api.github.com/user";

}
