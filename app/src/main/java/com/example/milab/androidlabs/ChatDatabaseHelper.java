package com.example.milab.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by milab on 2017-10-09.
 */

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "newDatabase";
    public static final int VERSION_NUM = 1;
    public final static String KEY_ID = "id";
    public final static String KEY_MESSAGE = "text_message";
    public final static String TABLE_NAME = "Message";
    public ChatDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
    String CREATE_MESSAGE_TABLE ="CREATE TABLE " + TABLE_NAME +" ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + KEY_MESSAGE + " TEXT" + ");";
        db.execSQL(CREATE_MESSAGE_TABLE);
        Log.i("ChatDatabaseHelper", "Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop tble is eists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //create new table
        onCreate(db);
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + "newVersion=" + newVersion);
    }

}
