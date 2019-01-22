package com.amazonaws.youruserpools.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amazonaws.youruserpools.Activities.MainFragmentActivity;
import com.amazonaws.youruserpools.CognitoYourUserPoolsDemo.R;
import com.amazonaws.youruserpools.Constants;
import com.amazonaws.youruserpools.Data.ChatOption;
import com.amazonaws.youruserpools.Utils.BackPressEditText;
import com.amazonaws.youruserpools.Webservices.ChatMessageWebServiceTask;

import java.util.ArrayList;

/**
 * Created by Leandro on 3/13/2017.
 */

public class ChatOptionsAdapter extends RecyclerView.Adapter<ChatOptionsAdapter.ChatOptionsViewHolder> {

    protected final RecyclerView chatRecyclerView;
    protected final Activity mActivity;

    public ArrayList<ChatOption> optionsList;

    public ChatOptionsAdapter(ArrayList<ChatOption> optionsList, Activity activity, RecyclerView chatRecyclerView) {
        this.mActivity = activity;
        this.chatRecyclerView = chatRecyclerView;
        this.optionsList = optionsList;
    }

    @Override
    public ChatOptionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_cell_options_item, parent, false);
        ChatOptionsViewHolder viewHolder = new ChatOptionsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChatOptionsAdapter.ChatOptionsViewHolder holder, int position) {

        holder.mText.setText(optionsList.get(position).getText());

        holder.setChatOption(optionsList.get(position));
        holder.setActivity(mActivity);
        holder.setChatMessageAdapter(chatRecyclerView);
        holder.setChatOptionsAdapter(this);
    }

    @Override
    public int getItemCount() {
        return optionsList.size();
    }

    public class ChatOptionsViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        protected TextView mText;
        protected ChatOption mChatOption;
        protected ChatOptionsAdapter mChatOptionsAdapter;
        protected RecyclerView chatRecyclerView;
        protected Activity mActivity;

        public ChatOptionsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mText = (TextView) itemView.findViewById(R.id.user_response_textview);
        }

        public void setChatOption(ChatOption chatOption) {
            mChatOption = chatOption;
        }

        public void setChatOptionsAdapter(ChatOptionsAdapter chatOptionsAdapter) {
            this.mChatOptionsAdapter = chatOptionsAdapter;
        }

        public void setChatMessageAdapter(RecyclerView chatMessageAdapter) {
            this.chatRecyclerView = chatMessageAdapter;
        }

        public void setActivity(Activity activity) {
            this.mActivity = activity;
        }

        @Override
        public void onClick(View v) {

            switch (mChatOption.getAction()) {

                case Constants.CHAT_OPTION_SELECT_CHOICE:
                    sendUserMessageToServer(mChatOption.getText());
                    break;

                case Constants.CHAT_OPTION_TYPE_TEXT:

                    final InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    final BackPressEditText editText = (BackPressEditText) mActivity.findViewById(R.id.user_text_edittext);
                    final Button sendButton = (Button) mActivity.findViewById(R.id.user_text_sendbutton);
                    final RelativeLayout typeTextLayout = (RelativeLayout) mActivity.findViewById(R.id.type_text_layout);

                    typeTextLayout.setVisibility(View.VISIBLE);
                    editText.setActivity((MainFragmentActivity) mActivity);
                    editText.requestFocus();
                    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

                    sendButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sendUserMessageToServer(editText.getText().toString());
                            editText.setText("");
                            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            typeTextLayout.setVisibility(View.GONE);
                        }
                    });
                    break;

                case Constants.CHAT_OPTION_REQUEST_PERMISSION:
                    sendUserMessageToServer(mChatOption.getText());
                    break;

                default:

                    break;
            }
        }

        private void sendUserMessageToServer(String userMessage) {

            mChatOption.setValue(userMessage);
            // sending init message to server
            ChatMessageWebServiceTask responseMessageTask = new ChatMessageWebServiceTask(
                    mActivity,
                    mChatOption.getReplyTo(),
                    mChatOption,
                    chatRecyclerView,
                    mChatOptionsAdapter
            );
            responseMessageTask.execute();

        }
    }
}
