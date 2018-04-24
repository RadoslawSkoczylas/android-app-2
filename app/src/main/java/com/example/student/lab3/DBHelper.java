package com.example.student.lab3;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    private Context context;

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "db_mobile_phones";
    public static final String TABLE_NAME = "mobile_phones";
    public static final String ID = "_id";
    public static final String VALUE = "value";
    public static final String LABEL = "DBHelper";
    public static final String DB_CREATE = "CREATE TABLE " + TABLE_NAME + "("+ID+" integer primary key autoincrement, "
            + VALUE + " text not null);";
    public static final String DB_DELETE = "DROP TABLE IF EXISTS "+ TABLE_NAME;


    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DB_DELETE);
        onCreate(sqLiteDatabase);
    }
}
