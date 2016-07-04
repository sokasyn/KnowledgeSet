package com.emin.digit.mobile.android.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.emin.digit.mobile.android.database.exception.EMDatabaseException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Samson on 16/7/4.
 */
public class EMSQLiteDatabase implements EMDatabase {

    // DebugLog
    private static final String TAG = EMSQLiteDatabase.class.getSimpleName();

    private static HashMap<String, EMSQLiteDatabase> daoMap = new HashMap<String, EMSQLiteDatabase>();

    private SQLiteDatabase database;  // SQLiteDatabase对象

    private EMDaoConfig config; // 数据库配置对象

    /**
     * 通过数据库配置,创建EMDatabase
     *
     * @param daoConfig 数据库配置实例
     * @return
     */
    public static EMSQLiteDatabase create(EMDaoConfig daoConfig) {
        Log.i(TAG,"EMDatabase create constructor with EMDaoConfig Directly");
        return getInstance(daoConfig);
    }

    // 私有构造方法
    private EMSQLiteDatabase(EMDaoConfig config){

        if (config == null) {
            Log.e(TAG,"EMDatabase NULL parameter exception");
            throw new EMDatabaseException("daoConfig is null");
        }
        if (config.getContext() == null) {
            Log.e(TAG,"EMDatabase NULL parameter exception");
            throw new EMDatabaseException("android context is null");
        }
        if (config.getTargetDirectory() != null && config.getTargetDirectory().trim().length() > 0) {
            Log.i(TAG,"EMDatabase target directory is setted, will create database at sdcard");
            this.database = createDbFileOnSDCard(config.getTargetDirectory(), config.getDbName());
        } else {
            Log.i(TAG,"EMDatabase created with config setting");
            this.database = new SqliteDbHelper(config.getContext().getApplicationContext(),
                    config.getDbName(), config.getDbVersion()).getWritableDatabase();
        }
        this.config = config;
    }

    /**
     * 线程同步方式获取单例通过数据库配置获取数据库对象
     *
     * @param daoConfig
     * @return
     */
    private synchronized static EMSQLiteDatabase getInstance(EMDaoConfig daoConfig){
        Log.i(TAG,"EMDatabase getInstance==========");
        EMSQLiteDatabase dao = daoMap.get(daoConfig.getDbName());
        if(dao == null){
            Log.i(TAG,"EMDatabase is null,Get instance of EMDatabase==================");
            dao = new EMSQLiteDatabase(daoConfig);
            daoMap.put(daoConfig.getDbName(),dao);
        }
        return dao;
    }

    /**
     * 在SD卡的指定目录上创建文件
     *
     * @param sdcardPath
     * @param dbfilename
     * @return
     */
    private SQLiteDatabase createDbFileOnSDCard(String sdcardPath, String dbfilename) {
        File dbf = new File(sdcardPath, dbfilename);
        if (!dbf.exists()) {
            try {
                if (dbf.createNewFile()) {
                    return SQLiteDatabase.openOrCreateDatabase(dbf, null);
                }
            } catch (IOException ioex) {
                Log.e(TAG,"createDbFileOnSDCard IOException occurred");
                throw new EMDatabaseException("数据库文件创建失败", ioex);
            }
        } else {
            return SQLiteDatabase.openOrCreateDatabase(dbf, null);
        }
        return null;
    }

    /**
     * 执行sqlInfo
     *
     * @param sqlInfo
     * @return
     */
    public void execSqlInfo(EMSqlInfo sqlInfo){
        debugSql(sqlInfo.getSql());
        Log.i(TAG,"sqlInfo.sql :" + sqlInfo.getSql());
        database.execSQL(sqlInfo.getSql());
        Log.i(TAG,"execSqlInfo succeed......" );
    }

    /**
     * 执行sql
     *
     * @param sql 原生SQL的字符串
     * @return
     */
    public void execSQL(String sql){
        database.beginTransaction();
        try{
            database.execSQL(sql);
            database.setTransactionSuccessful();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            database.endTransaction();
        }

    }


    public void execSql(String sqlString){
        Log.i(TAG,"= = = = = = = = = = 新版本 execSQL");
        database.execSQL(sqlString);
        Log.i(TAG,"= = = = = = = = = = 新版本 End");
    }

    public Cursor queryWithSqlInfo(EMSqlInfo sqlInfo){
        return database.rawQuery(sqlInfo.getSql(),null);
    }

    class SqliteDbHelper extends SQLiteOpenHelper {

//        private DbUpdateListener mDbUpdateListener;

        //三个不同参数的构造函数
        //带全部参数的构造函数，此构造函数必不可少
        public SqliteDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            Log.i(TAG,"Constructor four params");
        }

        //带三个参数的构造函数，调用的是带所有参数的构造函数
        public SqliteDbHelper(Context context,String name,int version){
            this(context, name, null, version);
            Log.i(TAG,"Constructor three params");
        }

        /*
        public SqliteDbHelper(Context context, String name, int version, DbUpdateListener dbUpdateListener) {
            super(context, name, null, version);
            DebugLog.i(TAG,"EMDatabase SqliteDbHelper constructor");
            this.mDbUpdateListener = dbUpdateListener;
        }*/

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG,"EMDatabase SqliteDbHelper onCreate");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(TAG,"EMDatabase SqliteDbHelper onUpgrade");
            /*
            if (mDbUpdateListener != null) {
                mDbUpdateListener.onUpgrade(db, oldVersion, newVersion);
            } else { // 清空所有的数据信息
                dropDb();
            }*/
        }
    }

    private void debugSql(String sql) {
        if (config != null && config.isDebug()) {
            Log.d("Debug SQL", " Executed sql:" + sql);
        }
    }
}
