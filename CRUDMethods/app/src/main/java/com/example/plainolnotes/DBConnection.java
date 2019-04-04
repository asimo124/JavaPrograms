package com.example.plainolnotes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @class DBConnection - code to execute and select SQLite Queries
 * @author alex hawley 2017.05.02
 */

public class DBConnection {

    public SQLiteDatabase database = null;

    public DBConnection(Context context) {
        DBOpenHelper helper = new DBOpenHelper(context);
        database = helper.getWritableDatabase();
    }

    /**
     * get current instance of SQLite Database
     * @return SQLiteDatabase
     */
    public SQLiteDatabase getDatabase() {

        return database;
    }

    /**
     * execute insert/update/delete queries
     * @param query
     */
    public void execQuery(String query) {

        database.execSQL(query);
    }

    /**
     * execute insert/update/delete queries with query params
     * @param query
     */
    public void execQuery(String query, Object[] args) {

        database.execSQL(query, args);
    }

    /**
     * run select query from query string
     * @param query
     * @return Cursor
     */
    public Cursor getQuery(String query) {

        String[] blankArgs = {};
        return database.rawQuery(query, blankArgs);
    }
}
