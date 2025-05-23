package com.example.accountbookapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecordDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "accounting.db";
    //private static final int DATABASE_VERSION = 1;
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_RECORDS = "records";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_DATE = "date";

    // SQL 语句创建表
    private static final String CREATE_TABLE_RECORDS = "CREATE TABLE " +
            TABLE_RECORDS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_USER_ID + " INTEGER NOT NULL," +
            COLUMN_AMOUNT + " REAL NOT NULL," +
            COLUMN_TYPE + " TEXT NOT NULL," +
            COLUMN_CATEGORY + " TEXT," +
            COLUMN_NOTE + " TEXT," +
            COLUMN_DATE + " INTEGER NOT NULL" +
            ")";

    public RecordDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RECORDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);
//        onCreate(db);
        if (oldVersion < 2) {
            // 添加user_id列
            db.execSQL("ALTER TABLE " + TABLE_RECORDS + " ADD COLUMN " + COLUMN_USER_ID + " INTEGER NOT NULL DEFAULT 0");
            // 如果需要，可以在这里迁移数据
        }
    }
}  