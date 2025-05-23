package com.example.accountbookapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.accountbookapp.Record;
import com.example.accountbookapp.database.UserDao;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecordDao {
    private final RecordDatabaseHelper dbHelper;
    private final UserDao userDao;
    private final Context context;

    public RecordDao(Context context) {
        this.context = context;
        dbHelper = new RecordDatabaseHelper(context);
        userDao = new UserDao(context);
    }

    private int getCurrentUserId() {
        // 获取当前登录用户的用户名
        android.content.SharedPreferences prefs = context.getSharedPreferences("UserPrefs", android.content.Context.MODE_PRIVATE);
        String username = prefs.getString("username", null);
        if (username != null) {
            return userDao.getUserIdByUsername(username);
        }
        return -1;
    }

    // 添加记录
    public long addRecord(Record record) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RecordDatabaseHelper.COLUMN_USER_ID, getCurrentUserId());
        values.put(RecordDatabaseHelper.COLUMN_AMOUNT, record.getAmount());
        values.put(RecordDatabaseHelper.COLUMN_TYPE, record.getType());
        values.put(RecordDatabaseHelper.COLUMN_CATEGORY, record.getCategory());
        values.put(RecordDatabaseHelper.COLUMN_NOTE, record.getNote());
        values.put(RecordDatabaseHelper.COLUMN_DATE, record.getDate().getTime());

        long id = db.insert(RecordDatabaseHelper.TABLE_RECORDS, null, values);
        db.close();
        return id;
    }

    // 更新记录
    public int updateRecord(Record record) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RecordDatabaseHelper.COLUMN_USER_ID, getCurrentUserId());
        values.put(RecordDatabaseHelper.COLUMN_AMOUNT, record.getAmount());
        values.put(RecordDatabaseHelper.COLUMN_TYPE, record.getType());
        values.put(RecordDatabaseHelper.COLUMN_CATEGORY, record.getCategory());
        values.put(RecordDatabaseHelper.COLUMN_NOTE, record.getNote());
        values.put(RecordDatabaseHelper.COLUMN_DATE, record.getDate().getTime());

        int rowsAffected = db.update(
                RecordDatabaseHelper.TABLE_RECORDS,
                values,
                RecordDatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(record.getId()),String.valueOf(getCurrentUserId())}
        );
        db.close();
        return rowsAffected;
    }

    // 删除记录
    public int deleteRecord(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsAffected = db.delete(
                RecordDatabaseHelper.TABLE_RECORDS,
                RecordDatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id), String.valueOf(getCurrentUserId())}
        );
        db.close();
        return rowsAffected;
    }


    // 获取所有记录
    public List<Record> getAllRecords() {
        List<Record> records = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + RecordDatabaseHelper.TABLE_RECORDS +
                " WHERE " + RecordDatabaseHelper.COLUMN_USER_ID + " = ?" +
                " ORDER BY " + RecordDatabaseHelper.COLUMN_DATE + " DESC";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(getCurrentUserId())});

        if (cursor.moveToFirst()) {
            do {
                Record record = new Record();
                record.setId(cursor.getInt(cursor.getColumnIndex(RecordDatabaseHelper.COLUMN_ID)));
                record.setAmount(cursor.getDouble(cursor.getColumnIndex(RecordDatabaseHelper.COLUMN_AMOUNT)));
                record.setType(cursor.getString(cursor.getColumnIndex(RecordDatabaseHelper.COLUMN_TYPE)));
                record.setCategory(cursor.getString(cursor.getColumnIndex(RecordDatabaseHelper.COLUMN_CATEGORY)));
                record.setNote(cursor.getString(cursor.getColumnIndex(RecordDatabaseHelper.COLUMN_NOTE)));
                record.setDate(new Date(cursor.getLong(cursor.getColumnIndex(RecordDatabaseHelper.COLUMN_DATE))));
                records.add(record);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return records;
    }

    // 获取特定时间段的记录
    public List<Record> getRecordsByDateRange(Date startDate, Date endDate) {
        List<Record> records = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + RecordDatabaseHelper.TABLE_RECORDS +
                " WHERE "  + RecordDatabaseHelper.COLUMN_USER_ID + " = ? AND " +
                RecordDatabaseHelper.COLUMN_DATE + " >= ? AND " +
                RecordDatabaseHelper.COLUMN_DATE + " <= ?" +
                " ORDER BY " + RecordDatabaseHelper.COLUMN_DATE + " DESC";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{
                String.valueOf(getCurrentUserId()),
                String.valueOf(startDate.getTime()),
                String.valueOf(endDate.getTime())
        });

        if (cursor.moveToFirst()) {
            do {
                Record record = new Record();
                record.setId(cursor.getInt(cursor.getColumnIndex(RecordDatabaseHelper.COLUMN_ID)));
                record.setAmount(cursor.getDouble(cursor.getColumnIndex(RecordDatabaseHelper.COLUMN_AMOUNT)));
                record.setType(cursor.getString(cursor.getColumnIndex(RecordDatabaseHelper.COLUMN_TYPE)));
                record.setCategory(cursor.getString(cursor.getColumnIndex(RecordDatabaseHelper.COLUMN_CATEGORY)));
                record.setNote(cursor.getString(cursor.getColumnIndex(RecordDatabaseHelper.COLUMN_NOTE)));
                record.setDate(new Date(cursor.getLong(cursor.getColumnIndex(RecordDatabaseHelper.COLUMN_DATE))));
                records.add(record);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return records;
    }

    // 获取本日记录
    public List<Record> getTodayRecords() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date endDate = calendar.getTime();

        return getRecordsByDateRange(startDate, endDate);
    }

    // 获取本周记录
    public List<Record> getThisWeekRecords() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 6);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date endDate = calendar.getTime();

        return getRecordsByDateRange(startDate, endDate);
    }

    // 获取本月记录
    public List<Record> getThisMonthRecords() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date endDate = calendar.getTime();

        return getRecordsByDateRange(startDate, endDate);
    }

    // 获取本年记录
    public List<Record> getThisYearRecords() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date endDate = calendar.getTime();

        return getRecordsByDateRange(startDate, endDate);
    }
}  