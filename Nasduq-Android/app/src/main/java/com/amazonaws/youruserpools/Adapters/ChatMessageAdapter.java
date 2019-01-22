package com.amazonaws.youruserpools.Adapters;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amazonaws.youruserpools.CognitoYourUserPoolsDemo.R;
import com.amazonaws.youruserpools.Constants;
import com.amazonaws.youruserpools.Data.ChatMessage;
import com.amazonaws.youruserpools.Fragments.ChatFragment;
import com.amazonaws.youruserpools.Utils.GifImageView;

import java.util.List;
import java.util.Objects;

/**
 * Created by Leandro on 3/2/2017.
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<ChatMessage> messages;
    private ChatFragment fragment;

    public ChatMessageAdapter(ChatFragment fragment, List<ChatMessage> objects) {
        this.fragment = fragment;
        this.messages = objects;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        switch (viewType) {

            case Constants.BOT_MESSAGE:
                v =  LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.view_cell_message_bot, parent, false);
                return new BotMessageViewHolder(v);

            case Constants.USER_MESSAGE:
                v =  LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.view_cell_message_user, parent, false);
                return new UserMessageViewHolder(v);

            case Constants.OPTIONS_MESSAGE:
                v =  LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.view_cell_message_options, parent, false);
                return new OptionsMessageViewHolder(v);

            default:
                throw new RuntimeException("viewType not found for ChatMessageAdapter");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);

        switch (holder.getItemViewType()) {

            case Constants.BOT_MESSAGE:
                BotMessageViewHolder botHolder = (BotMessageViewHolder) holder;
                botHolder.msg.setText(message.getContent());

                if (message.isShowFace()) {
                    botHolder.showFace();
                }
                else {
                    botHolder.hideFace();
                }
                if (Objects.equals(message.getContent(), "")){
                    botHolder.showGif();
                }
                else {
                    botHolder.hideGif();
                }
                break;

            case Constants.USER_MESSAGE:
                UserMessageViewHolder userHolder = (UserMessageViewHolder) holder;
                userHolder.msg.setText(message.getContent());
                break;

            case Constants.OPTIONS_MESSAGE:
                OptionsMessageViewHolder optionsHolder = (OptionsMessageViewHolder) holder;
                optionsHolder.changeViewHeight(fragment.chatRecyclerView);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).messageType;
    }

    public class BotMessageViewHolder
            extends RecyclerView.ViewHolder {

        protected TextView msg;

        public BotMessageViewHolder(View itemView) {
            super(itemView);
            msg = (TextView) itemView.findViewById(R.id.txt_msg);
        }

        public void showFace() {
            ImageView avatar = (ImageView) itemView.findViewById(R.id.nasduq_chat_icon);
            avatar.setVisibility(View.VISIBLE);
        }

        public void hideFace() {
            ImageView avatar = (ImageView) itemView.findViewById(R.id.nasduq_chat_icon);
            avatar.setVisibility(View.GONE);
        }

        public void showGif() {
            GifImageView gifImageView = (GifImageView) itemView.findViewById(R.id.gif_image_view);
            gifImageView.setGifImageResource(R.mipmap.load_dots_grey);
            gifImageView.setVisibility(View.VISIBLE);

            RelativeLayout innerView = (RelativeLayout) itemView.findViewById(R.id.bubble_layout);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) innerView.getLayoutParams();
            lp.bottomMargin = 2 * lp.topMargin;
            innerView.setLayoutParams(lp);
        }

        public void hideGif() {
            GifImageView gifImageView = (GifImageView) itemView.findViewById(R.id.gif_image_view);
            gifImageView.setVisibility(View.GONE);

            RelativeLayout innerView = (RelativeLayout) itemView.findViewById(R.id.bubble_layout);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) innerView.getLayoutParams();
            lp.bottomMargin = 0;
            innerView.setLayoutParams(lp);
        }
    }

    public class UserMessageViewHolder
            extends RecyclerView.ViewHolder {

        protected TextView msg;

        public UserMessageViewHolder(View itemView) {
            super(itemView);
            msg = (TextView) itemView.findViewById(R.id.txt_msg);
        }
    }

    public class OptionsMessageViewHolder
            extends RecyclerView.ViewHolder {

        public OptionsMessageViewHolder(View itemView) {
            super(itemView);

            LinearLayoutManager layoutManager = new LinearLayoutManager(fragment.getActivity(), LinearLayoutManager.HORIZONTAL, true);
            RecyclerView optionsRecyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerview_options_cell);
            optionsRecyclerView.setLayoutManager(layoutManager);
            optionsRecyclerView.setAdapter(fragment.chatOptionsAdapter);
            optionsRecyclerView.addItemDecoration(new SpacesItemDecoration(10));
        }

        public void changeViewHeight(RecyclerView rc) {

            DisplayMetrics displayMetrics = fragment.getContext().getResources().getDisplayMetrics();
            TextView bar = (TextView) fragment.getActivity().findViewById(R.id.chat_title);

            float screenHeight = displayMetrics.heightPixels;
            float barHeight = bar.getHeight();
            float size = (float) rc.computeVerticalScrollExtent();
            float cellHeight = 90 * displayMetrics.density;

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();

            if (screenHeight - barHeight - cellHeight > size) {
                cellHeight = (screenHeight - barHeight - cellHeight) - size;
                params.topMargin = (int) cellHeight;
            }
            else {
                params.topMargin = 0;
            }
            itemView.setLayoutParams(params);
        }
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = 3 * space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
//            if(parent.getChildAdapterPosition(view) == 0)
//                outRect.top = space;
        }
    }
}


