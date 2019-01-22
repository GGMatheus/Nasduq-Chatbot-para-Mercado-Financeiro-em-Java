package com.amazonaws.youruserpools.Webservices;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.amazonaws.youruserpools.CognitoYourUserPoolsDemo.R;
import com.amazonaws.youruserpools.Constants;

import org.json.JSONObject;


/**
 * Created by Leandro on 3/10/2017.
 */

public abstract class WebServiceTask extends AsyncTask<Void, Void, Boolean> {

    public static final String TAG = WebServiceTask.class.getName();

    public abstract void showProgress();

    public abstract boolean performRequest();

    public abstract void performSuccessfulOperation();

    public abstract void hideProgress();

    private String mMessage;
    private Context mContext;

    public WebServiceTask(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        showProgress();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if(!WebServiceUtils.hasInternetConnection(mContext)) {
            mMessage = Constants.CONNECTION_MESSAGE;
            return false;
        }
        return performRequest();
    }

    @Override
    protected void onPostExecute(Boolean success) {
        hideProgress();
        if(success) {
            performSuccessfulOperation();
        }
        if(mMessage != null && !mMessage.isEmpty()) {
            Toast.makeText(mContext, mMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCancelled(Boolean aBoolean) {
        hideProgress();
    }

    public boolean hasError(JSONObject obj) {
        if(obj != null) {
            boolean success = obj.optBoolean(Constants.SUCCESS_KEY);

            if (!success) {
                mMessage = mContext.getString(R.string.error_generic_api_failure);
            }
            return !success;
        }
        else {
            mMessage = mContext.getString(R.string.error_generic_api_failure);
            return true;
        }
    }
}
