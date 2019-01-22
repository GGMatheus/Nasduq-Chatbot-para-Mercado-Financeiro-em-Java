package com.amazonaws.youruserpools.Webservices;

import android.app.Activity;
import android.content.ContentValues;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoAccessToken;
import com.amazonaws.youruserpools.Adapters.WatchlistAdapter;
import com.amazonaws.youruserpools.AppHelper;
import com.amazonaws.youruserpools.CognitoYourUserPoolsDemo.R;
import com.amazonaws.youruserpools.Constants;
import com.amazonaws.youruserpools.Data.WatchlistItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Leandro on 3/19/2017.
 */

public class WatchlistWebServiceTask extends WebServiceTask {
    private RecyclerView watchlistRecyclerview;

    private CognitoAccessToken accessToken;
    private JSONObject returnedJson = null;
    private WatchlistAdapter watchlistAdapter;
    private Activity activity;
    private String period;
    private ProgressBar progressBar;
    private View inflatedView;

    public WatchlistWebServiceTask(Activity currentActivity, RecyclerView watchlistRecyclerview, WatchlistAdapter watchlistAdapter, View inflatedView) {
        super(currentActivity);
        this.activity = currentActivity;
        this.watchlistAdapter = watchlistAdapter;
        this.watchlistRecyclerview = watchlistRecyclerview;
        this.inflatedView = inflatedView;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    @Override
    public void performSuccessfulOperation() {

        JSONArray stocks = returnedJson.optJSONArray(Constants.WATCHLIST_STOCKS);


        watchlistAdapter.watchlistItems.clear();
        for (int i = 0; i < stocks.length(); i++) {
            JSONObject wlItemJson = stocks.optJSONObject(i);
            WatchlistItem wl = new WatchlistItem(
                    wlItemJson.optString(Constants.WATCHLIST_TICKER),
                    wlItemJson.optString(Constants.WATCHLIST_COMPANY_NAME),
                    wlItemJson.optDouble(Constants.WATCHLIST_PRICE),
                    wlItemJson.optDouble(Constants.WATCHLIST_CHANGE),
                    wlItemJson.optString(Constants.WATCHLIST_CURRENCY),
                    wlItemJson.optDouble(Constants.WATCHLIST_ABS_CHANGE),
                    Constants.WATCHLIST_ITEM);
            watchlistAdapter.watchlistItems.add(wl);
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                watchlistAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void showProgress() {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                      WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        progressBar = (ProgressBar) inflatedView.findViewById(R.id.watchlist_progressbar);
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideProgress() {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        progressBar = (ProgressBar) inflatedView.findViewById(R.id.watchlist_progressbar);
        progressBar.setVisibility(View.GONE);

    }

    @Override
  public boolean performRequest() {

        accessToken = AppHelper.getCurrSession().getAccessToken();
        ContentValues jsonBodyValues = new ContentValues();
        ContentValues optionValues = new ContentValues();
        ContentValues headerValues = new ContentValues();

        // this.period is accessible here, should pass that through jsonBodyValues when the HTTP endpoint is ready.
        // jsonBodyValues.put(Constants.PERIOD_KEY, period);

        optionValues.put(Constants.OPTION_ID_KEY, "");
        optionValues.put(Constants.OPTION_VALUE_KEY, "");

        jsonBodyValues.put(Constants.CONTEXT_KEY, "init");
        jsonBodyValues.put(Constants.OPTION_KEY, String.valueOf(optionValues));

        headerValues.put(Constants.AUTHORIZATION_HEADER_KEY, accessToken.getJWTToken());

        returnedJson = WebServiceUtils.requestJSONObject(
                Constants.API_URL + Constants.CHAT_MESSAGE_RESOURCE,
                WebServiceUtils.METHOD.POST,
                headerValues, null, jsonBodyValues, true);

        // TODO: this is just temporary while the API isn't implementing this endpoint

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            JSONArray stocks = new JSONArray();

            JSONObject stock_1 = new JSONObject();
            JSONObject stock_2 = new JSONObject();
            JSONObject stock_3 = new JSONObject();
            JSONObject stock_4 = new JSONObject();
            JSONObject stock_5 = new JSONObject();
            JSONObject stock_6 = new JSONObject();

            stock_1.put(Constants.WATCHLIST_TICKER, "AAPL");
            stock_1.put(Constants.WATCHLIST_PRICE, 100);
            stock_1.put(Constants.WATCHLIST_COMPANY_NAME, "Apple Inc.");
            stock_1.put(Constants.WATCHLIST_CHANGE, -1);
            stock_1.put(Constants.WATCHLIST_CURRENCY, "CAD");
            stock_1.put(Constants.WATCHLIST_ABS_CHANGE, 10000.55);

            stock_2.put(Constants.WATCHLIST_TICKER, "TSLA");
            stock_2.put(Constants.WATCHLIST_PRICE, 200);
            stock_2.put(Constants.WATCHLIST_COMPANY_NAME, "Tesla Motors Inc.");
            stock_2.put(Constants.WATCHLIST_CHANGE, 10);
            stock_2.put(Constants.WATCHLIST_CURRENCY, "AUD");
            stock_2.put(Constants.WATCHLIST_ABS_CHANGE, 10000.55);

            stock_3.put(Constants.WATCHLIST_TICKER, "GOOG");
            stock_3.put(Constants.WATCHLIST_PRICE, 600);
            stock_3.put(Constants.WATCHLIST_COMPANY_NAME, "Alphabet Inc.");
            stock_3.put(Constants.WATCHLIST_CHANGE, 2);
            stock_3.put(Constants.WATCHLIST_CURRENCY, "EUR");
            stock_3.put(Constants.WATCHLIST_ABS_CHANGE, 10000.55);

            stock_4.put(Constants.WATCHLIST_TICKER, "BRK-A");
            stock_4.put(Constants.WATCHLIST_PRICE, 258000.52);
            stock_4.put(Constants.WATCHLIST_COMPANY_NAME, "Berkshire Hathaway Inc. Class A");
            stock_4.put(Constants.WATCHLIST_CHANGE, 0.37);
            stock_4.put(Constants.WATCHLIST_CURRENCY, "USD");
            stock_4.put(Constants.WATCHLIST_ABS_CHANGE, 952.00);

            stock_5.put(Constants.WATCHLIST_TICKER, "FORD");
            stock_5.put(Constants.WATCHLIST_PRICE, 350);
            stock_5.put(Constants.WATCHLIST_COMPANY_NAME, "Ford Inc.");
            stock_5.put(Constants.WATCHLIST_CHANGE, -5);
            stock_5.put(Constants.WATCHLIST_CURRENCY, "USD");
            stock_5.put(Constants.WATCHLIST_ABS_CHANGE, 10000.55);

            stock_6.put(Constants.WATCHLIST_TICKER, "MON");
            stock_6.put(Constants.WATCHLIST_PRICE, 120);
            stock_6.put(Constants.WATCHLIST_COMPANY_NAME, "Monsanto Company");
            stock_6.put(Constants.WATCHLIST_CHANGE, 3);
            stock_6.put(Constants.WATCHLIST_CURRENCY, "BRL");
            stock_6.put(Constants.WATCHLIST_ABS_CHANGE, 10000.55);


            stocks.put(stock_1);
            stocks.put(stock_2);
            stocks.put(stock_3);
            stocks.put(stock_4);
            stocks.put(stock_5);
            stocks.put(stock_6);

            jsonObject.put("stocks", stocks);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            returnedJson = jsonObject;
        }

        // TODO: improve error verification logic
        if (returnedJson != null) {
            return true;
        } else {
            return false;
        }
    }
}
