package com.kay.openaiint;

public class Chat {
    public static final String TABLE_NAME = "chat";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_IS_USER = "is_user";

    private String timestamp;
    private String message;
    private boolean isUser;

    public Chat(String timestamp, String message, boolean isUser) {
        this.timestamp = timestamp;
        this.message = message;
        this.isUser = isUser;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public boolean isUser() {
        return isUser;
    }
}
