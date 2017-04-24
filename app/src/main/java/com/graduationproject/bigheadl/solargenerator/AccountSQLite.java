package com.graduationproject.bigheadl.solargenerator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lshbi on 4/17/2017.
 */

public class AccountSQLite extends SQLiteOpenHelper {

    private static final String DB_NAME = "UserList";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "User";
    private static final String COL_id = "id";
    private static final String COL_email = "email";
    private static final String COL_password = "password";
    private static final String COL_identify = "identify";

    private static final String TABLE_CREATE = "CREATE TABLE" + TABLE_NAME + "(" +
            COL_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_email + " TEXT NOT NULL, " +
            COL_password + " TEXT NOT NULL, " + COL_identify + " TEXT NOT NULL ); ";

    public AccountSQLite(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }



}
