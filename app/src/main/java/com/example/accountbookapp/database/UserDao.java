package com.example.accountbookapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.accountbookapp.User;

public class UserDao {
    private final UserDatabaseHelper dbHelper;

    public UserDao(Context context) {
        dbHelper = new UserDatabaseHelper(context);
    }

    // 添加用户
    public long addUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserDatabaseHelper.COLUMN_USERNAME, user.getUsername());
        values.put(UserDatabaseHelper.COLUMN_PASSWORD, user.getPassword());

        long id = db.insert(UserDatabaseHelper.TABLE_USERS, null, values);
        db.close();
        return id;
    }

    // 验证用户登录
    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + UserDatabaseHelper.TABLE_USERS +
                " WHERE " + UserDatabaseHelper.COLUMN_USERNAME + " = ? AND " +
                UserDatabaseHelper.COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{username, password});
        boolean isValid = cursor.moveToFirst();
        cursor.close();
        db.close();
        return isValid;
    }

    // 根据用户名获取用户 ID
    public int getUserIdByUsername(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " + UserDatabaseHelper.COLUMN_ID + " FROM " + UserDatabaseHelper.TABLE_USERS +
                " WHERE " + UserDatabaseHelper.COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{username});
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_ID));
        }
        cursor.close();
        db.close();
        return userId;
    }
}
