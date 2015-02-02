package be.kdg.healthtips.task;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.temboo.Library.Fitbit.Statistics.GetTimeSeriesByDateRange;
import com.temboo.core.TembooException;
import com.temboo.core.TembooSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import be.kdg.healthtips.auth.FitbitTokenManager;
import be.kdg.healthtips.auth.FitbitTokenManager;
import be.kdg.healthtips.session.TembooSessionManager;

/**
 * This Asynctask fetches the steps from the FitBit-API (using temboo)
 */
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class GetStepsATask extends AsyncTask<String, Void, LineData> {
    private FitbitTokenManager tokenManager;

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public GetStepsATask(Context context)
    {
        super();
        this.tokenManager = FitbitTokenManager.getInstance(context);
    }

    /**
     * @param dates The start and end date to fetch data for
     * @return The amount of steps between the given dates
     */
    @Override
    protected LineData doInBackground(String... dates) {
        LineData data = null;

        try {
            TembooSession session = TembooSessionManager.getSession();
            GetTimeSeriesByDateRange getSteps = new GetTimeSeriesByDateRange(session);

            GetTimeSeriesByDateRange.GetTimeSeriesByDateRangeInputSet input = getSteps.newInputSet();
            input.set_EndDate(dates[1]);
            input.set_AccessToken(tokenManager.getFitBitAccesToken());
            input.set_StartDate(dates[0]);
            input.set_AccessTokenSecret(tokenManager.getFitBitAccesTokenSecret());
            input.set_ConsumerSecret(FitbitTokenManager.getConsumerSecret());
            input.set_ResourcePath("activities/log/steps");
            input.set_ConsumerKey(FitbitTokenManager.getConsumerKey());

            GetTimeSeriesByDateRange.GetTimeSeriesByDateRangeResultSet result = getSteps.execute(input);

            JSONObject obj = new JSONObject(result.get_Response());
            JSONArray arr = obj.getJSONArray("activities-log-steps");

            List<Entry> entries = new ArrayList<>();
            for(int i = 0; i < arr.length(); i++)
            {
                JSONObject o = arr.getJSONObject(i);
                String date = o.getString("dateTime");
                int value = o.getInt("value");
            }
        } catch (TembooException | JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
