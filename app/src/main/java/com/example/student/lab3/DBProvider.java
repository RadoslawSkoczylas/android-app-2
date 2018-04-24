package com.example.student.lab3;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class DBProvider extends ContentProvider {

    private DBHelper dbHelper;
    private static final String IDENTIFIER = "com.example.db_mobile_phones.DBProvider";
    public static final Uri uri = Uri.parse("content://"+IDENTIFIER+"/"+DBHelper.TABLE_NAME);
    private static final int TABLE = 1;
    private static final int CHOSEN_ROW = 2;
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        URI_MATCHER.addURI(IDENTIFIER, DBHelper.TABLE_NAME, TABLE);
        URI_MATCHER.addURI(IDENTIFIER, DBHelper.TABLE_NAME+"/#", CHOSEN_ROW);
    }
    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = null;

        switch (uriType){
            case TABLE:
                cursor = sqLiteDatabase.query(
                        false,
                        DBHelper.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder,
                        null,
                        null
                        );
                break;
            case CHOSEN_ROW:

                cursor = sqLiteDatabase.query(
                        false,
                        DBHelper.TABLE_NAME,
                        projection,
                        addIdToSelection(selection,uri),
                        selectionArgs,
                        null,
                        null,
                        sortOrder,
                        null,
                        null

                );
                break;
            default:
                throw new IllegalArgumentException("Nieznane uri: "+uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }
    private String addIdToSelection(String selection, Uri uri){
        if(selection != null && !selection.equals("")){
            selection = selection + " and " + DBHelper.ID+"="+uri.getLastPathSegment();
        }else {
            selection = DBHelper.ID+"="+uri.getLastPathSegment();
        }
        return selection;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        long idOfAdded = 0;

        switch(uriType){
            case TABLE:
                idOfAdded = sqLiteDatabase.insert(DBHelper.TABLE_NAME, null, contentValues);

                break;

            default:
                throw new IllegalArgumentException("Nieznane URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);

        return Uri.parse(DBHelper.TABLE_NAME+"/"+idOfAdded);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int numberOfDeleted = 0;
        switch (uriType){
            case TABLE:
                numberOfDeleted = sqLiteDatabase.delete(DBHelper.TABLE_NAME,selection,selectionArgs);
                break;
            case CHOSEN_ROW:
                numberOfDeleted = sqLiteDatabase.delete(DBHelper.TABLE_NAME,addIdToSelection(selection,uri),selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Nieznane uri: "+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return numberOfDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int numberOfUpdated = 0;
        switch(uriType){
            case TABLE:
                numberOfUpdated = sqLiteDatabase.update(DBHelper.TABLE_NAME,contentValues,selection,selectionArgs);

                break;
            case CHOSEN_ROW:
                numberOfUpdated = sqLiteDatabase.update(DBHelper.TABLE_NAME,contentValues,addIdToSelection(selection,uri),selectionArgs);

                break;
            default:
                throw new IllegalArgumentException("Nieznane uri: "+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return numberOfUpdated;
    }
}
