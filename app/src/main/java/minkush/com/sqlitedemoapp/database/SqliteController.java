package minkush.com.sqlitedemoapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wingify on 20/02/18.
 */

public class SqliteController extends SQLiteOpenHelper {
    private static final String LOGCAT = null;
    private static final String TABLE_COUNTRY = "country";
    public static final String COLUMN_COUNTRY_ID = "country_id";
    public static final String COLUMN_COUNTRY_NAME = "country_name";
    public static final String COLUMN_COUNTRY_CAPITAL = "country_capital";
    public static final String COLUMN_COUNTRY_REGION = "country_region";
    public static final String COLUMN_COUNTRY_NATIVENAME = "country_nativename";


    public SqliteController(Context applicationcontext) {
        super(applicationcontext, "androidsqlite.db", null, 1);
        Log.d(LOGCAT, "Created");
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE " + TABLE_COUNTRY + " ( " + COLUMN_COUNTRY_ID + " INTEGER PRIMARY KEY, " + COLUMN_COUNTRY_NAME + " TEXT, "
                + COLUMN_COUNTRY_CAPITAL + " TEXT, " + COLUMN_COUNTRY_REGION + " TEXT, " + COLUMN_COUNTRY_NATIVENAME + " TEXT ) ";
        database.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS " + TABLE_COUNTRY;
        database.execSQL(query);
        onCreate(database);
    }

    public void insertStudent(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COUNTRY_NAME, queryValues.get(COLUMN_COUNTRY_NAME));
        values.put(COLUMN_COUNTRY_CAPITAL, queryValues.get(COLUMN_COUNTRY_CAPITAL));
        values.put(COLUMN_COUNTRY_REGION, queryValues.get(COLUMN_COUNTRY_REGION));
        values.put(COLUMN_COUNTRY_NATIVENAME, queryValues.get(COLUMN_COUNTRY_NATIVENAME));
        database.insert(TABLE_COUNTRY, null, values);
        database.close();
    }

    public int updateStudent(String countryId, HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COUNTRY_NAME, queryValues.get(COLUMN_COUNTRY_NAME));
        values.put(COLUMN_COUNTRY_CAPITAL, queryValues.get(COLUMN_COUNTRY_CAPITAL));
        values.put(COLUMN_COUNTRY_REGION, queryValues.get(COLUMN_COUNTRY_REGION));
        values.put(COLUMN_COUNTRY_NATIVENAME, queryValues.get(COLUMN_COUNTRY_NATIVENAME));
        return database.update(TABLE_COUNTRY, values, COLUMN_COUNTRY_ID + " = ?", new String[]{countryId});
    }

    public void deleteStudent(String countryId) {
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM " + TABLE_COUNTRY + " where " + COLUMN_COUNTRY_ID + "='" + countryId + "'";
        database.execSQL(deleteQuery);
    }


    public ArrayList<HashMap<String, String>> getAllCountries() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM " + TABLE_COUNTRY;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(COLUMN_COUNTRY_ID, cursor.getString(0));
                map.put(COLUMN_COUNTRY_NAME, cursor.getString(1));
                map.put(COLUMN_COUNTRY_CAPITAL, cursor.getString(2));
                map.put(COLUMN_COUNTRY_REGION, cursor.getString(3));
                map.put(COLUMN_COUNTRY_NATIVENAME, cursor.getString(4));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        return wordList;
    }

}
