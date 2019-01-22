package com.amazonaws.youruserpools.Webservices;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by Leandro on 3/10/2017.
 */

public class WebServiceUtils {
    private static final String TAG = WebServiceUtils.class.getName();
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 100000;


    public enum METHOD {
        POST
    }

    public static JSONObject requestJSONObject(String serviceUrl, METHOD method,
                                               ContentValues headerValues, boolean hasAuthorization) {
        return requestJSONObject(serviceUrl, method, headerValues, null, null, hasAuthorization);
    }

    public static JSONObject requestJSONObject(String serviceUrl, METHOD method,
                                               ContentValues urlValues, ContentValues bodyValues) {
        return requestJSONObject(serviceUrl, method, null, urlValues, bodyValues, false);
    }
    public static JSONObject requestJSONObject(String serviceUrl, METHOD method,
                                               ContentValues headerValues, ContentValues urlValues,
                                               ContentValues bodyValues, boolean hasAuthorization) {

        HttpURLConnection urlConnection = null;
        try {
            if(urlValues != null) {
                serviceUrl = addParametersToUrl(serviceUrl, urlValues);
            }

            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setRequestMethod(method.toString());
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

//            if(headerValues != null) {
//                Uri.Builder builder  = new Uri.Builder();
//                for(String key: headerValues.keySet()) {
//                    builder.appendQueryParameter(key, headerValues.getAsString(key));
//                }
//                String query = builder.build().getEncodedQuery();
//                OutputStream os = urlConnection.getOutputStream();
//                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//                writer.write(query);
//                writer.flush();
//                writer.close();
//                os.close();
//            }

            if(bodyValues != null) {

                JSONObject jsonObject = new JSONObject();
                for(String key : bodyValues.keySet()) {
                    jsonObject.put(key, bodyValues.getAsString(key));
                }

                if (headerValues != null) {
                    for(String key: headerValues.keySet()) {
                        urlConnection.setRequestProperty(key, headerValues.getAsString(key));
                    }
                }
                String str = jsonObject.toString();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                OutputStreamWriter osw = new OutputStreamWriter(urlConnection.getOutputStream());
                osw.write(str);
                osw.flush();
                osw.close();
            }

            int statusCode = urlConnection.getResponseCode();

            InputStream in;

            if (statusCode == HttpURLConnection.HTTP_OK) {
                in = new BufferedInputStream(urlConnection.getInputStream());
            }
            else {
                in = new BufferedInputStream(urlConnection.getErrorStream());
            }

            return new JSONObject(convertInputStreamToString(in));

        } catch(MalformedURLException e) {
            Log.d(TAG, e.getMessage());
        } catch(SocketTimeoutException e) {
            Log.d(TAG, e.getMessage());
        } catch(IOException e) {
            Log.d(TAG, e.getMessage());
        } catch(JSONException e) {
            Log.d(TAG, e.getMessage());
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    private static String convertInputStreamToString(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String responseText;
        try {
            while((responseText = bufferedReader.readLine()) != null) {
                stringBuilder.append(responseText);
            }
        } catch(IOException e) {
            Log.d(TAG, "IOException in convertInputStreamToString");
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    private static String addParametersToUrl(String url, ContentValues urlValues) {
        StringBuffer stringBuffer = new StringBuffer(url);
        stringBuffer.append("?");
        for (String key : urlValues.keySet()) {
            stringBuffer.append(key);
            stringBuffer.append("=");
            stringBuffer.append(urlValues.getAsString(key));
            stringBuffer.append("&");
        }
        stringBuffer.replace(stringBuffer.length() - 1, stringBuffer.length() - 1, "");
        return stringBuffer.toString();
    }

    public static boolean hasInternetConnection(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager != null &&
                connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }


}


