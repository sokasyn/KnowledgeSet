package com.emin.digit.mobile.android.database;


import android.util.Log;

import com.emin.digit.mobile.android.database.exception.EMDatabaseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Samson on 16/7/4.
 *
 * 数据库访问管理器
 */
public class EMDatabaseManager {

    private static final String TAG = EMDatabaseManager.class.getSimpleName();

    private static EMDatabaseManager ourInstance = new EMDatabaseManager();

    public static EMDatabaseManager getInstance() {
        return ourInstance;
    }

    // 数据库
    private EMDatabase mDatabase;

    private EMDatabaseManager() {
    }

    //******************************** Database Create/Open ********************************/
    public EMDatabase createOrOpenDatabase(EMDaoConfig daoConfig){
        try{
            mDatabase = EMSQLiteDatabase.create(daoConfig);
            System.out.println("******  mDatabase" + mDatabase);
        }catch (EMDatabaseException e){
            Log.e(TAG,e.getMessage());
            return null;
        }
        return mDatabase;
    }

    //************************************** Create Table **************************************/

    /**
     * 创建数据表
     *
     * @param jsonString
     *        数组中的每一个Key-value键值对都应当是一个以key为表名,value为字段列表的json字符串
     *        每一个key-value对,对应一张表
     *        格式如:{"user":"name,age",
     *               "account":"name,password",
     *               "address":"id,pid,name"}
     */
    public void createTables(String jsonString){
        Log.i(TAG,"新版本 createTables");
        if(mDatabase == null){
            Log.i(TAG,"db object is null");
            return;
        }else{
            Log.i(TAG,"mDatabase is not null");
        }

        try{
            JSONObject obj = new JSONObject(jsonString);
            Iterator tableKeyIterator = obj.keys();
            while (tableKeyIterator.hasNext()) {
                String tableName = (String) tableKeyIterator.next();
                String values = obj.getString(tableName);
                Log.i(TAG,"key:" + tableName + " values:" + values);
                EMSqlInfo sqlInfo = EMSqlBuilder.buildCreateTableSql(tableName,values);
                Log.i(TAG,"sql:" + sqlInfo.getSql());
//                mDatabase.execSqlInfo(sqlInfo);
                mDatabase.execSql(sqlInfo.getSql());
            }

        }catch (JSONException e){
            Log.e(TAG,"JSONException occurred.......");
        }
    }


    // INSERT
    public void insertWithSQL(String sqlString){

    }

    public void insertWithJSON(String jsonString){

    }

    // DELETE
    public void deleteWithSQL(String sqlString){

    }

    public void deleteWithJSON(String jsonString){

    }

    // UPDATE
    public void updateWithSQL(String sqlString){

    }

    public void updateWithJSON(String jsonString){

    }

    // SELECT
    public void queryWithSQL(String sqlString){

    }

    public void queryWithJSON(String jsonString){

    }



}
