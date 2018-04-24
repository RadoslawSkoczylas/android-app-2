package com.example.student.lab3;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
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
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater menuInflater = actionMode.getMenuInflater();
                menuInflater.inflate(R.menu.context_menu,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.delete_menu:
                        long selected[] = listView.getCheckedItemIds();
                        for(int i = 0; i < selected.length; i++){
                            getContentResolver().delete(
                                    ContentUris.withAppendedId(DBProvider.URI, selected[i]),
                                    null,
                                    null);
                        }
                        return true;
                }
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });

        button = (Button)findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues contentValues = new ContentValues();
                EditText editText = (EditText)findViewById(R.id.addValue);
                if(!editText.getText().toString().equals("")){
                    contentValues.put(DBHelper.VALUE, editText.getText().toString());
                    Uri newUri = getContentResolver().insert(DBProvider.URI, contentValues);
                }

            }
        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {DBHelper.ID, DBHelper.VALUE};
        CursorLoader cursorLoader = new CursorLoader(this, DBProvider.URI,projection,null,null,null);
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
