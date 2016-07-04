package com.emin.digit.mobile.android.learning.practiceproject.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Samson on 16/6/23.
 */
public class EMDatabaseManger {

    // 单例
    private static EMDatabaseManger instance = null;

    // SQLiteHelper
    private DatabaseHelper mDatabaseHelper;

    // SQLiteDatabase
    private SQLiteDatabase mSqliteDatabase;

    // Context
    private Context mContext;

    private EMDatabaseManger(Context context){
        this.mContext = context;
        mDatabaseHelper = new DatabaseHelper(context);
    }

    public static synchronized EMDatabaseManger getInstance(Context context){
        if(null == instance){
            instance = new EMDatabaseManger(context);
        }
        return instance;
    }

    public synchronized SQLiteDatabase getDatabase() {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        return db;
    }




    public void openDatabase(){
        mSqliteDatabase = mDatabaseHelper.getReadableDatabase();
    }

    public void openDatabase(String databaseName){


    }

    public void excuteSql(String sql){

    }

    public void createDatabase(){
        /*
        SQLiteDatabase db = openOrCreateDatabase(SDPATH + fileName, null);
 try {
         db.execSQL("create table user(id int, name varchar(20), conversation varchar(50))");
 }catch(SQLException e) {
         System.out.println("SQLException!");
 }
         */


//        SQLiteDatabase db = this.openOrCreateDatabase("test_db.db", Context.MODE_PRIVATE, null);
//        SQLiteDatabase db2 = SQLiteDatabase.openOrCreateDatabase("/data/data/com.test/databases/test_db2.db3", null);
    }


}
