package com.example.plainolnotes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Date;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBConnection dbConn = new DBConnection(this);

        /*/
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, "New note");
        Uri noteUri = getContentResolver().insert(NotesProvider.CONTENT_URI, values);
        Log.d("MainActivity", "Inserted note " + noteUri.getLastPathSegment());
        //*/

        Cursor resultset = dbConn.getQuery("SELECT * FROM notes");

        String[] fields = {"_id", "noteText", "noteAmount", "noteCreated"};
        SQLOutput.fieldTypes[] types = {SQLOutput.fieldTypes.INT, SQLOutput.fieldTypes.STRING, SQLOutput.fieldTypes.FLOAT, SQLOutput.fieldTypes.DATE};
        SQLOutput.showGridResults(resultset, fields, types, "MainActivity");

        Cursor resultset2 = dbConn.getQuery("SELECT * FROM notes");
        try {
            while (resultset2.moveToNext()) {

            }
        } finally {
            resultset2.close();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
