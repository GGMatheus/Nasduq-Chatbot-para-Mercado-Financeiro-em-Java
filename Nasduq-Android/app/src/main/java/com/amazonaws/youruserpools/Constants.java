package com.amazonaws.youruserpools;

/**
 * Created by Leandro on 3/11/2017.
 */

public class Constants {

    public static final String API_ENVIRONMENT = "prod";
    public static final String API_URL = "https://rsxdheq8mf.execute-api.us-west-2.amazonaws.com/" + API_ENVIRONMENT;

    // Web service tasks constants
    public static final String CONNECTION_MESSAGE = "No Internet Connection!";
    public static final String SUCCESS_KEY = "success";
    public static final String MESSAGE_KEY = "message";
    public static final int STATUS_ERROR = 400;
    public static final int STATUS_UNAUTHORIZED = 401;

    // Request Password constants
    public static final String ORIGIN_KEY = "origin";
    public static final String ORIGIN_VALUE = "Android";
    public static final String ENVIRONMENT_KEY = "environment";
    public static final String ENVIRONMENT_VALUE = "production";
    public static final String EMAIL_KEY = "email";

    // Chat message constants
    public static final int BOT_MESSAGE = 0;
    public static final int USER_MESSAGE = 1;
    public static final int OPTIONS_MESSAGE = 2;

    public static final String CONTEXT_KEY = "context";
    public static final String OPTION_KEY = "option";
    public static final String OPTION_ID_KEY = "id";
    public static final String OPTION_VALUE_KEY = "value";
    public static final String OPTION_RESPONSE_TO_KEY = "response_to";

    public static final String AUTHORIZATION_HEADER_KEY = "Authorization";

    public static final String REQUEST_INVITE_RESOURCE = "/request-invite";
    public static final String CHAT_MESSAGE_RESOURCE = "/message";

    // Chat options constants
    public static final String CHAT_OPTION_TYPE_TEXT = "type_text";
    public static final String CHAT_OPTION_SELECT_CHOICE = "select_choice";
    public static final String CHAT_OPTION_REQUEST_PERMISSION = "request_permission";

    // Watchlist constants
    public static final String WATCHLIST_TICKER = "ticker";
    public static final String WATCHLIST_PRICE = "price";
    public static final String WATCHLIST_COMPANY_NAME = "company_name";
    public static final String WATCHLIST_CHANGE = "change";
    public static final String WATCHLIST_STOCKS = "stocks";
    public static final String WATCHLIST_CURRENCY = "currency";
    public static final String WATCHLIST_ABS_CHANGE = "abs_change";
    public static final String WATCHLIST_PERIOD_DAILY = "daily";
    public static final String WATCHLIST_PERIOD_WEEKLY = "weekly";
    public static final String WATCHLIST_PERIOD_MONTHLY = "monthly";
    public static final String WATCHLIST_PERIOD_YEARLY = "yearly";
    public static final int WATCHLIST_ITEM = 1;

}
