package be.kdg.healthtips.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import be.kdg.healthtips.activity.FitBitAuthActivity;
import be.kdg.healthtips.activity.HomeActivity;
import be.kdg.healthtips.auth.AuthManager;

/**
 * Created by Mathi on 25/01/2015.
 */
public class FinAuthATask extends AsyncTask<AuthManager, Void, Boolean> {

    private Context context;

    public FinAuthATask(Context context)
    {
        super();
        this.context = context;

    }

    @Override
    protected Boolean doInBackground(AuthManager... params) {
        AuthManager man = params[0];
        return man.finalizeOAuth();
    }

    @Override
    protected void onPostExecute(Boolean authorized) {
        if(authorized)
        {
            Intent intent = new Intent(context, HomeActivity.class);
            context.startActivity(intent);
        }
        else
        {
            // TODO ERROR
            CharSequence text = "Unable to login!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }


}