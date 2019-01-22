package com.amazonaws.youruserpools.Data;

/**
 * Created by Leandro on 3/14/2017.
 */

public class ChatOption {
    private String action;
    private String text;
    private String replyTo;
    private String id;
    private String value;


    public ChatOption(String action, String text, String replyTo, String id, String value) {
        this.action = action;
        this.text = text;
        this.replyTo = replyTo;
        this.id = id;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAction() {
        return action;
    }

    public String getText() {
        return text;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public String getId() {
        return id;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public void setId(String id) {
        this.id = id;
    }
}
