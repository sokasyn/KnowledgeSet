package com.emin.digit.mobile.android.learning.practiceproject.EMModel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.emin.digit.mobile.android.learning.practiceproject.exception.EMDatabaseException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Samson on 16/6/24.
 */
public class EMDatabase {

    // Log
    private static final String TAG = EMDatabase.class.getSimpleName();

    private static HashMap<String, EMDatabase> daoMap = new HashMap<String, EMDatabase>();

    private SQLiteDatabase database;

    private EMDaoConfig config;

    /**
     * 创建EMDatabase
     *
     * @param daoConfig
     * @return
     */
    public static EMDatabase create(EMDaoConfig daoConfig) {
        Log.i(TAG,"EMDatabase create constructor with EMDaoConfig Directly");
        return getInstance(daoConfig);
    }

    // 私有构造方法
    private EMDatabase(EMDaoConfig config){

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

    // 私有获取单例对象
    private synchronized static EMDatabase getInstance(EMDaoConfig daoConfig){
        Log.i(TAG,"EMDatabase getInstance==========");
        EMDatabase dao = daoMap.get(daoConfig.getDbName());
        if(dao == null){
            Log.i(TAG,"EMDatabase is null,Get new instance of EMDatabase==================");
            dao = new EMDatabase(daoConfig);
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

    public void execSqlInfo(EMSqlInfo sqlInfo){
        database.execSQL(sqlInfo.getSql());
        Log.i(TAG,"execSqlInfo succeed......" );
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
            Log.i(TAG,"EMDatabase SqliteDbHelper constructor");
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
