package be.kdg.healthtips.auth;

import android.content.Context;

import com.temboo.Library.Fitbit.OAuth.InitializeOAuth;
import com.temboo.core.TembooException;
import com.temboo.core.TembooSession;
import com.temboo.Library.Fitbit.OAuth.FinalizeOAuth;
import com.temboo.Library.Fitbit.OAuth.FinalizeOAuth.FinalizeOAuthInputSet;
import com.temboo.Library.Fitbit.OAuth.FinalizeOAuth.FinalizeOAuthResultSet;
import com.temboo.Library.Fitbit.OAuth.InitializeOAuth.InitializeOAuthInputSet;
import com.temboo.Library.Fitbit.OAuth.InitializeOAuth.InitializeOAuthResultSet;


import be.kdg.healthtips.session.TembooSessionManager;

/**
 * This class the fitbit authentication logic using the Temboo API.
 */
public class AuthManager {

    private static AuthManager manager;
    private Context context;
    private boolean authorized = false;
    private String callbackId;
    private String oAuthToken;
    private String callBackUrl;
    private String fitBitAccesToken;
    private String fitBitAccesTokenSecret;

    private AuthManager(Context context)
    {
        this.context = context;
    }

    public static AuthManager getInstance(Context context)
    {
        if (manager == null) manager = new AuthManager(context);
        return manager;
    }

    public String initializeOAuth()
    {
        try {
            TembooSession session = TembooSessionManager.getSession();
            InitializeOAuth initOAauth = new InitializeOAuth(session);


            InitializeOAuthInputSet input = initOAauth.newInputSet();
            input.set_ConsumerKey(FitbitTokenManager.getConsumerKey());
            input.set_ConsumerSecret(FitbitTokenManager.getConsumerSecret());


            InitializeOAuthResultSet results = initOAauth.execute(input);

            callbackId = results.get_CallbackID();
            oAuthToken = results.get_OAuthTokenSecret();
            callBackUrl = results.get_AuthorizationURL();
        }

        catch(TembooException e){
            e.printStackTrace();
            authorized = false;
            fitBitAccesToken = null;
            fitBitAccesTokenSecret = null;
            callbackId = null;
            callBackUrl = null;
            oAuthToken = null;
        }
        return callBackUrl;
    }

    public boolean finalizeOAuth()
    {
        try
        {
            TembooSession session = TembooSessionManager.getSession();
            FinalizeOAuth finOAuth = new FinalizeOAuth(session);

            FinalizeOAuthInputSet input = finOAuth.newInputSet();
            input.set_Timeout(60000);
            input.set_CallbackID(callbackId);
            input.set_OAuthTokenSecret(oAuthToken);
            input.set_ConsumerSecret(FitbitTokenManager.getConsumerSecret());
            input.set_ConsumerKey(FitbitTokenManager.getConsumerKey());

            FinalizeOAuthResultSet results = finOAuth.execute(input);
            authorized = true;
            fitBitAccesToken = results.get_AccessToken();
            fitBitAccesTokenSecret = results.get_AccessTokenSecret();

            FitbitTokenManager tokenManager = FitbitTokenManager.getInstance(context);

            tokenManager.setFitBitAccesToken(fitBitAccesToken);
            tokenManager.setFitBitAccesTokenSecret(fitBitAccesTokenSecret);
        }
        catch (TembooException e)
        {
            e.printStackTrace();
            authorized = false;
            fitBitAccesToken = null;
            fitBitAccesTokenSecret = null;
            callbackId = null;
            callBackUrl = null;
            oAuthToken = null;
        }

        return authorized;
    }
}
