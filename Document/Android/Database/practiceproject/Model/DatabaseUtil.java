package com.emin.digit.mobile.android.learning.practiceproject.Model;

/**
 * Created by Samson on 16/6/22.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseUtil{

    private static final String TAG = DatabaseUtil.class.getSimpleName();

    /**
     * Database Name
     */
    private static final String DATABASE_NAME ="student_database";

    /**
     * Database Version
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Table Name
     */
    private static final String DATABASE_TABLE ="tb_student";

    /**
     * Table columns
     */
    public static final String KEY_NAME ="name";
    public static final String KEY_GRADE ="grade";
    public static final String KEY_ROWID ="_id";

    /**
     * Database creation sql statement
     */
    private static final String CREATE_STUDENT_TABLE =
            "create table"+ DATABASE_TABLE +"("+ KEY_ROWID +"integer primary key autoincrement,"
                    + KEY_NAME +"text not null,"+ KEY_GRADE +"text not null);";

    /**
     * Context
     */
    private final Context mCtx;

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Inner private class. Database Helper class for creating and updating database.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        /**
         * onCreate method is called for the 1st time when database doesn't exists.
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG,"Creating DataBase:"+ CREATE_STUDENT_TABLE);
            db.execSQL(CREATE_STUDENT_TABLE);
        }
        /**
         * onUpgrade method is called when database version changes.
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG,"Upgrading database from version"+ oldVersion +"to"
                    + newVersion);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public DatabaseUtil(Context ctx) {
        this.mCtx = ctx;
    }
    /**
     * This method is used for creating/opening connection
     * @return instance of DatabaseUtil
     * @throws SQLException
     */
    public DatabaseUtil open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    /**
     * This method is used for closing the connection.
     */
    public void close() {
        mDbHelper.close();
    }

    /**
     * This method is used to create/insert new record Student record.
     * @param name
     * @param grade
     * @return long
     */
    public long createStudent(String name, String grade) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_GRADE, grade);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }
    /**
     * This method will delete Student record.
     * @param rowId
     * @return boolean
     */
    public boolean deleteStudent(long rowId) {
        return mDb.delete(DATABASE_TABLE, KEY_ROWID +"="+ rowId, null) > 0;
    }

    /**
     * This method will return Cursor holding all the Student records.
     * @return Cursor
     */
    public Cursor fetchAllStudents() {
        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME,
                KEY_GRADE}, null, null, null, null, null);
    }

    /**
     * This method will return Cursor holding the specific Student record.
     * @param id
     * @return Cursor
     * @throws SQLException
     */
    public Cursor fetchStudent(long id) throws SQLException {
        Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_NAME, KEY_GRADE}, KEY_ROWID +"="+ id, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * This method will update Student record.
     * @param id
     * @param name
     * @param standard
     * @return boolean
     */
    public boolean updateStudent(int id, String name, String standard) {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_GRADE, standard);
        return mDb.update(DATABASE_TABLE, args, KEY_ROWID +"="+ id, null) > 0;
    }
}