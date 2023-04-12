package com.kay.openaiint;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "chat_history.db";
    private static final String TABLE_NAME = "chat";
    public static final String TIMESTAMP_COLUMN = "timestamp";
    public static final String MESSAGE_COLUMN = "message";
    public static final String USER_COLUMN = "is_user";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + TABLE_NAME + " (" + TIMESTAMP_COLUMN + " TEXT PRIMARY KEY, " +
                MESSAGE_COLUMN + " TEXT, " + USER_COLUMN + " INTEGER)";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement the logic to handle the database upgrade if necessary
    }

    public boolean addChat(Chat chat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIMESTAMP_COLUMN, chat.getTimestamp());
        contentValues.put(MESSAGE_COLUMN, chat.getMessage());
        contentValues.put(USER_COLUMN, chat.isUser() ? 1 : 0);

        long insert = db.insert(TABLE_NAME, null, contentValues);
        return insert != -1;
    }

    public List<Chat> getChatsByTimestamp(String searchTimestamp) {
        List<Chat> chatList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TABLE_NAME + " WHERE " + TIMESTAMP_COLUMN + " LIKE ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, new String[]{searchTimestamp + "%"});

        if (cursor.moveToFirst()) {
            do {
                String timestamp = cursor.getString(cursor.getColumnIndex(TIMESTAMP_COLUMN));
                String message = cursor.getString(cursor.getColumnIndex(MESSAGE_COLUMN));
                boolean isUser = cursor.getInt(cursor.getColumnIndex(USER_COLUMN)) == 1;
                chatList.add(new Chat(timestamp, message, isUser));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return chatList;
    }

    public List<Chat> getAllChats() {
        List<Chat> chatList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                String timestamp = cursor.getString(cursor.getColumnIndex(TIMESTAMP_COLUMN));
                String message = cursor.getString(cursor.getColumnIndex(MESSAGE_COLUMN));
                boolean isUser = cursor.getInt(cursor.getColumnIndex(USER_COLUMN)) == 1;
                chatList.add(new Chat(timestamp, message, isUser));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return chatList;
    }
}
