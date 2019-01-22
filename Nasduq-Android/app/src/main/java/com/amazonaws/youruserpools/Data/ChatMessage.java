package com.amazonaws.youruserpools.Data;

/**
 * Created by Leandro on 3/2/2017.
 */

public class ChatMessage {

    private String content;
    public int messageType;
    private boolean showFace;

    public ChatMessage(String content, int messageType, boolean showFace) {
        this.content = content;
        this.messageType = messageType;
        this.showFace = showFace;
    }

    public String getContent() {
        return content;
    }


    public boolean isShowFace() {
        return showFace;
    }
}
