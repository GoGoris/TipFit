package be.kdg.healthtips.task;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;

import com.temboo.Library.Fitbit.Body.GetBodyWeight;
import com.temboo.core.TembooException;
import com.temboo.core.TembooSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import be.kdg.healthtips.activity.LoginActivity;
import be.kdg.healthtips.auth.FitbitTokenManager;
import be.kdg.healthtips.session.TembooSessionManager;

/**
 * Created by school on 5/2/2015.
 */
public class GetWeightATask extends AsyncTask<Date, Void, JSONObject> {
    private FitbitTokenManager tokenManager;
    private Context context;

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public GetWeightATask(Context context) {
        super();
        this.tokenManager = FitbitTokenManager.getInstance(context);
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(Date... params) {
        JSONObject jsonToReturn = null;
        try {
            TembooSession session = TembooSessionManager.getSession();
            GetBodyWeight getBodyWeightChoreo = new GetBodyWeight(session);

            GetBodyWeight.GetBodyWeightInputSet input = getBodyWeightChoreo.newInputSet();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            input.set_Date(sdf.format(params[0]) + "/1m");


            input.set_AccessToken(tokenManager.getFitBitAccesToken());

            input.set_AccessTokenSecret(tokenManager.getFitBitAccesTokenSecret());
            input.set_ConsumerSecret(FitbitTokenManager.getConsumerSecret());
            input.set_ConsumerKey(FitbitTokenManager.getConsumerKey());

            GetBodyWeight.GetBodyWeightResultSet result = getBodyWeightChoreo.execute(input);

            jsonToReturn = new JSONObject(result.get_Response());

        } catch (TembooException e) {
            if (e.getMessage().contains("status code of 401")) {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            }
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonToReturn;
    }
}
