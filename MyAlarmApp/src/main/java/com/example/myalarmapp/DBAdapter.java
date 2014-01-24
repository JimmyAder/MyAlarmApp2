package com.example.myalarmapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.util.Date;

/**
 * Created by Jimmy.Ader on 1/23/14.
 */
public class DBAdapter {

    public static final String KEY_ROWID = "id";
    public static final String KEY_SSID = "ssid";
    public static final String KEY_BSSID = "bssid";
    public static final String KEY_FREQUENCY = "frequency";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_LOGDATE = "logDate";

    private static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "MyAlarmAppDB";
    private static final String DATABASE_TABLE = "wifiResults";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
            "create table if not exists "+ DATABASE_TABLE +" (id integer primary key autoincrement, "
            + " SSID VARCHAR, BSSID VARCHAR, frequency int, level int, logDate date);";

    private final Context context;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context context)
    {
        this.context = context;
        DBHelper = new DatabaseHelper(this.context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION );
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.d(TAG, "Upgrading database from version " + String.valueOf(oldVersion) + " to "
                    + String.valueOf(oldVersion) + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }

    }

    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        DBHelper.close();
    }

    public long insertRecord(String SSID, String BSSID, int frequency, int level, String date)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_SSID, SSID);
        initialValues.put(KEY_BSSID, BSSID);
        initialValues.put(KEY_FREQUENCY, frequency);
        initialValues.put(KEY_LEVEL, level);
        initialValues.put(KEY_LOGDATE, date);

        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteRecord(long rowId)
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + String.valueOf(rowId), null) > 0;
    }

    public Cursor getAllRecords()
    {
        return db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_SSID, KEY_BSSID, KEY_FREQUENCY,
                KEY_LEVEL, KEY_LOGDATE}, null, null, null, null, null);
    }

    public Cursor getRecord(long rowId){
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_SSID, KEY_BSSID, KEY_FREQUENCY,
                KEY_LEVEL, KEY_LOGDATE}, KEY_ROWID + "=" + String.valueOf(rowId), null, null, null, null, null);

        if(cursor != null)
            cursor.moveToFirst();

        return cursor;
    }

    public boolean updateRecord(long rowId, String SSID, String BSSID, int frequency, int level, String date)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_SSID, SSID);
        args.put(KEY_BSSID, BSSID);
        args.put(KEY_FREQUENCY, frequency);
        args.put(KEY_LEVEL, level);
        args.put(KEY_LOGDATE, date);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + String.valueOf(rowId), null) > 0;
    }
}
