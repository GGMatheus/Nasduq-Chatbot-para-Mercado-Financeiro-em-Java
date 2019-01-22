package com.amazonaws.youruserpools.Activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.youruserpools.CognitoYourUserPoolsDemo.R;
import com.amazonaws.youruserpools.Constants;
import com.amazonaws.youruserpools.Webservices.WebServiceTask;
import com.amazonaws.youruserpools.Webservices.WebServiceUtils;

import org.json.JSONObject;

/**
 * Created by Leandro on 3/7/2017.
 */

public class RequestInviteActivity extends AppCompatActivity {

    private AlertDialog userDialog;

    private RequestInviteWebServiceTask requestInviteTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_request_invite);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final EditText editText = (EditText) findViewById(R.id.email_placeholder);


        final Button btnRequest = (Button) findViewById(R.id.request_password_button);
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email = editText.getText().toString();

                if (isValidEmail(email)) {
                    requestInviteTask = new RequestInviteWebServiceTask(email);
                    requestInviteTask.execute();
                } else {
                    showDialogMessage("Invalid e-mail", "Please, enter a valid e-mail.");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }



    private void showDialogMessage(String title, String body) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();
                } catch (Exception e) {
                }
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }

    private class RequestInviteWebServiceTask extends WebServiceTask {

        public String mEmail = null;

        public RequestInviteWebServiceTask(String email) {
            super(RequestInviteActivity.this);
            mEmail = email;
        }

        @Override
        public void showProgress() {}

        @Override
        public void hideProgress() {}

        @Override
        public void performSuccessfulOperation() {}

        @Override
        public boolean performRequest() {
            ContentValues contentValues = new ContentValues();
//          User user = RESTServiceApplication.getInstance().getUser();
            contentValues.put(Constants.ORIGIN_KEY, Constants.ORIGIN_VALUE);
            contentValues.put(Constants.ENVIRONMENT_KEY, Constants.ENVIRONMENT_VALUE);
            contentValues.put(Constants.EMAIL_KEY, this.mEmail);

//          ContentValues urlValues = new ContentValues();
//          urlValues.put(Constants.ACCESS_TOKEN, RESTServiceApplication.getInstance().getAccessToken());

            ContentValues urlValues = new ContentValues();

            JSONObject obj = WebServiceUtils.requestJSONObject(
                    Constants.API_URL + Constants.REQUEST_INVITE_RESOURCE,
                    WebServiceUtils.METHOD.POST,
                    urlValues,
                    contentValues);

            if (!hasError(obj)) {
                Intent intent = new Intent(RequestInviteActivity.this,
                                           RequestInviteSuccessActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            }
            return false;
        }
    }

}
