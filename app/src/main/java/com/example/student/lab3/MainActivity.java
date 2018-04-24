package com.example.student.lab3;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
    private SimpleCursorAdapter simpleCursorAdapter;
    private ListView listView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.list);

        getLoaderManager().initLoader(0,null,this);
        String[] mapFrom = new String[] { DBHelper.VALUE};
        int[] mapTo = new int[] {R.id.realValue};
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.list_row, null, mapFrom,mapTo,0);
        listView.setAdapter(simpleCursorAdapter);


        button = (Button)findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues contentValues = new ContentValues();
                EditText editText = (EditText)findViewById(R.id.addValue);
                contentValues.put(DBHelper.VALUE, editText.getText().toString());
                Uri newUri = getContentResolver().insert(DBProvider.uri, contentValues);
            }
        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {DBHelper.ID, DBHelper.VALUE};
        CursorLoader cursorLoader = new CursorLoader(this, DBProvider.uri,projection,null,null,null);
        return cursorLoader;

    }



    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        simpleCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        simpleCursorAdapter.swapCursor(null);
    }
}
