package com.graduationproject.bigheadl.solargenerator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lshbi on 4/17/2017.
 */

public class AccountSQLite extends SQLiteOpenHelper {

    private static final String DB_NAME = "Report";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "Report";
    private static final String COL_id = "reportID";
    private static final String COL_username = "username";
    private static final String COL_content = "content";
    private static final String COL_enablePicture = "enablePicture";
    private static final String COL_picture = "picture";
    private static final String COL_enableAudio = "enableAudio";
    private static final String COL_audio = "audio";
    private static final String COL_enableVideo = "enableVideo";
    private static final String COL_video = "video";
    private static final String COL_isSync = "isSync";
    private static final String COL_date = "date";

    private static final String TABLE_CREATE = "CREATE TABLE" + TABLE_NAME + "(" +
            COL_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_username + " TEXT NOT NULL , " +
            COL_content + " TEXT NOT NULL, " + COL_enablePicture + " INTEGER, " + COL_picture +
            " BLOB, " + COL_enableAudio + " INTEGER, " + COL_audio + " BLOB, " + COL_enableVideo +
            " INTEGER, " + COL_video + " BLOB, " + COL_isSync + " INTEGER, " + COL_date +
            " TEXT NOT NULL ); ";

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public AccountSQLite(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

}
