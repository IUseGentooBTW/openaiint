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

    private static final String DATABASE_NAME = "chats.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "chat_history";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String COLUMN_MESSAGE = "message";
    private static final String COLUMN_IS_USER = "is_user";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TIMESTAMP + " TEXT, " +
                COLUMN_MESSAGE + " TEXT, " +
                COLUMN_IS_USER + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addChat(Chat chat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIMESTAMP, chat.getTimestamp());
        values.put(COLUMN_MESSAGE, chat.getMessage());
        values.put(COLUMN_IS_USER, chat.isUser() ? 1 : 0);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<Chat> getChatsByTimestamp(String searchTimestamp) {
        List<Chat> chats = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_TIMESTAMP + " LIKE ?";
        String[] selectionArgs = {searchTimestamp + "%"};

        Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs,
                null, null, COLUMN_TIMESTAMP + " ASC");

        if (cursor.moveToFirst()) {
            do {
                String timestamp = cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP));
                String message = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE));
                boolean isUser = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_USER)) == 1;
                chats.add(new Chat(timestamp, message, isUser));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return chats;
    }
}
