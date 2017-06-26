package com.example.donald.testapplication.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Donald on 25.06.2017.
 */
public class DBCustomViews extends SQLiteOpenHelper implements BaseColumns{

    public static final int DB_VERSION = 1;
    public static final String NAME_DB = "images.db";
    public static final String NAME_TABLE = "path";

    public static final String NAME_ID = "_id";
    public static final String NAME_COLLUMN_TITLE = "title";
    public static final String NAME_COLLUMN_DESCRIPTION = "discription";
    public static final String NAME_COLLUMN_TEASER = "teaser";
    public static final String NAME_COLLUMN_NORMAL = "normal";

    private static final String DATABASE_CREATE_SCRIPT = "create table "
            + NAME_TABLE + " ("
            + NAME_ID + " integer primary key, "
            + NAME_COLLUMN_TITLE + " string, "
            + NAME_COLLUMN_TEASER + " string, "
            + NAME_COLLUMN_NORMAL + " string, "
            + NAME_COLLUMN_DESCRIPTION + " string)";

    public DBCustomViews(Context context){
        super(context, NAME_DB, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
