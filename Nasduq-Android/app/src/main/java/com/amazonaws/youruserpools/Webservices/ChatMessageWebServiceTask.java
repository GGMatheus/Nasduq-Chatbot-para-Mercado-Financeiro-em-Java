package com.amazonaws.youruserpools.Webservices;

import android.app.Activity;
import android.content.ContentValues;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoAccessToken;
import com.amazonaws.youruserpools.Adapters.ChatMessageAdapter;
import com.amazonaws.youruserpools.Adapters.ChatOptionsAdapter;
import com.amazonaws.youruserpools.AppHelper;
import com.amazonaws.youruserpools.Constants;
import com.amazonaws.youruserpools.Data.ChatMessage;
import com.amazonaws.youruserpools.Data.ChatOption;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Leandro on 3/14/2017.
 */

public class ChatMessageWebServiceTask extends WebServiceTask {

    private String mBotContext;
    private ChatOption mChatOption;
    private Activity mActivity;
    private CognitoAccessToken accessToken;
    private RecyclerView chatRecyclerView;
    private ChatOptionsAdapter mChatOptionsAdapter;
    private JSONObject returnedJson = null;
    final private ChatMessageAdapter messageAdapter;

    public ChatMessageWebServiceTask(Activity currentActivity, String botContext, ChatOption chatOption,
                                     RecyclerView chatRecyclerView, ChatOptionsAdapter chatOptionsAdapter) {

        super(currentActivity);
        this.mBotContext = botContext;
        this.mChatOption = chatOption;
        this.mActivity = currentActivity;
        this.mChatOptionsAdapter = chatOptionsAdapter;
        this.chatRecyclerView = chatRecyclerView;
        this.messageAdapter = (ChatMessageAdapter) chatRecyclerView.getAdapter();

        accessToken = AppHelper.getCurrSession().getAccessToken();
        Log.d("[Access token]", accessToken.getJWTToken());
    }

    @Override
    public void showProgress() {
        removeLastMessage(); // remove options cell (if there is any)
        addUserMessage();
        addTypingMessage();
        updateChatLog(true);
    }

    @Override
    public void hideProgress() {
    }

    private boolean hasError() {
        JSONArray messages = returnedJson.optJSONArray("tell");
        JSONArray options = returnedJson.optJSONArray("options");
        if((messages == null) || (options == null)) {
            return true;
        }
        return false;
    }

    @Override
    public void performSuccessfulOperation() {

        Random r = new Random();
        int randomTime = (r.nextInt(10) * 100);

        Timer buttonTimer = new Timer();
        buttonTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                removeLastMessage(); // remove typing indicator cell
                JSONArray messages = returnedJson.optJSONArray("tell");
                JSONArray options = returnedJson.optJSONArray("options");

                for (int i = 0; i < messages.length(); i++) {
                    if (i == messages.length() - 1) {
                        messageAdapter.messages.add(new ChatMessage(messages.optString(i), Constants.BOT_MESSAGE, true));
                    } else {
                        messageAdapter.messages.add(new ChatMessage(messages.optString(i), Constants.BOT_MESSAGE, false));
                    }
                }
                mChatOptionsAdapter.optionsList.clear();
                for (int i = 0; i < options.length(); i++) {

                    JSONObject jsonItem = options.optJSONObject(i);

                    String action = jsonItem.optString("action");
                    String id = jsonItem.optString("id");
                    String text = jsonItem.optString("text");
                    String replyTo = jsonItem.optString("reply_to");
                    String value = jsonItem.optString("value");

                    ChatOption chatOption = new ChatOption(action, text, replyTo, id, value);
                    mChatOptionsAdapter.optionsList.add(chatOption);
                }

                addOptionsCell();
                updateReplyOptions();
                updateChatLog(true);
            }
        }, randomTime);

    }

    @Override
    public boolean performRequest() {

        ContentValues jsonBodyValues = new ContentValues();
        ContentValues optionValues = new ContentValues();
        ContentValues headerValues = new ContentValues();

        optionValues.put(Constants.OPTION_ID_KEY, mChatOption.getId());
        optionValues.put(Constants.OPTION_VALUE_KEY, mChatOption.getValue());

        jsonBodyValues.put(Constants.CONTEXT_KEY, mBotContext);
        jsonBodyValues.put(Constants.OPTION_KEY, String.valueOf(optionValues));

        headerValues.put(Constants.AUTHORIZATION_HEADER_KEY, accessToken.getJWTToken());

        returnedJson = WebServiceUtils.requestJSONObject(
                Constants.API_URL + Constants.CHAT_MESSAGE_RESOURCE,
                WebServiceUtils.METHOD.POST,
                headerValues, null, jsonBodyValues, true);

        // TODO: improve error verification logic
        if (!hasError()) {
            return true;
        }
        else {
            return false;
        }
    }

    private void addTypingMessage() {
        messageAdapter.messages.add(new ChatMessage("", Constants.BOT_MESSAGE, true));
    }

    private void updateChatLog(final boolean smooth) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageAdapter.notifyDataSetChanged();
                if (smooth) {
                    chatRecyclerView.smoothScrollToPosition(messageAdapter.messages.size());
                }
                else {
                    chatRecyclerView.scrollToPosition(messageAdapter.messages.size());
                }
            }
        });
    }

    private void updateReplyOptions() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mChatOptionsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void removeLastMessage() {
        // removing the last message -- could be a options cell or a "bot is typing" indicator cell
        if (messageAdapter.messages.size() > 0) {
            messageAdapter.messages.remove(messageAdapter.messages.size() - 1);
        }
    }

    private void addOptionsCell() {
        // Adding an empty message so that we have a place to put the options view
        messageAdapter.messages.add(new ChatMessage(null, Constants.OPTIONS_MESSAGE, false));
    }

    private void addUserMessage() {
        if (mChatOption.getValue() != null) {
            messageAdapter.messages.add(new ChatMessage(mChatOption.getValue(), Constants.USER_MESSAGE, false));
        }
    }
}
