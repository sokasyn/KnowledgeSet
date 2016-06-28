package com.emin.digit.mobile.android.learning.practiceproject.EMModel;

import android.util.Log;

import com.emin.digit.mobile.android.learning.practiceproject.common.ThisApplication;
import com.emin.digit.mobile.android.learning.practiceproject.exception.EMDatabaseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Samson on 16/6/27.
 */
public class EMDatabaseManager {

    private static final String TAG = EMDatabaseManager.class.getSimpleName();

    private static EMDatabaseManager ourInstance = new EMDatabaseManager();

    public static EMDatabaseManager getInstance() {
        return ourInstance;
    }

    private EMDatabase db;

    private EMDatabaseManager() {
    }

    public EMDatabase createOrOpenDatabase(EMDaoConfig daoConfig){
        try{
            db = EMDatabase.create(daoConfig);
        }catch (EMDatabaseException e){
            Log.e(TAG,e.getMessage());
            return null;
        }
        return db;
    }

    /**
     * 创建数据表
     *
     * @param jsonString
     *        数组中的每一个Key-value键值对都应当是一个以key为表名,value为字段列表的json字符串
     *        每一个key-value对,对应一张表
     *        格式如:{"user":"name,age",
     *               "account":"name,password",
     *               "address":"id,pid,name"}
     * @return
     */
    public void createTables(String jsonString){
        if(db == null){
            Log.i(TAG,"db object is null");
            return;
        }
        try{
            JSONObject obj = new JSONObject(jsonString);
            Iterator iterator = obj.keys();
            int i = 0;
            while (iterator.hasNext()) {
                String tableName = (String) iterator.next();
                String values = obj.getString(tableName);
                Log.i(TAG,"key:" + tableName + " values:" + values);
                EMSqlInfo sqlInfo = EMSqlBuilder.buildCreateTableSql(tableName,values);
                Log.i(TAG,"sql:" + sqlInfo.getSql());
                db.execSqlInfo(sqlInfo);
            }
        }catch (JSONException e){
            Log.e(TAG,"JSONException occurred.......");
        }
    }

    /*
     * 插入数据记录
     * @param jsonString
     *        key为数据表的名称,value为所要插入数据记录,每一条记录是一个单独的JSON对象
     *        如果插入一行,可以构建value为单独的JSON对象,也可以包装在JSON数组里；
     *        如果插入多行,可以构建value为JSON数组,也可以构建多个重复的key
     *
     */
    public void insertRecords(String jsonString){
        Log.i(TAG,"json string:" + jsonString);

        if(db == null){
            Log.i(TAG,"db object is null");
            return;
        }
        try{
            JSONObject obj = new JSONObject(jsonString);

            // table
            Iterator tableKeyIterator = obj.keys();
            while(tableKeyIterator.hasNext()){
                String tableName = (String ) tableKeyIterator.next();
                Object value = obj.opt(tableName);
                Log.i(TAG,"value class:" + value.getClass().toString());
                if(value instanceof JSONArray){
                    Log.i(TAG,"instance of JSONArray:" + value.toString());
                    /*
                    JSONArray dataArray = (JSONArray) value;

                    for(int i = 0 ; i < dataArray.length() ; i++){
                        JSONObject dataObj = (JSONObject) dataArray.get(i);
                        insertRecord(tableName,dataObj.toString());
                    }*/

                    insertRecords(tableName,value.toString());
                }
                if(value instanceof JSONObject){
                    Log.i(TAG,"instance of JSONObject :" + value.toString());
                    insertRecord(tableName,value.toString());
                }
            }
        }catch (JSONException e){
            Log.e(TAG,"!!!!!!!!!!!!!" + e.getMessage());
        }

    }

    public void insertRecords(String tableName ,String jsonArrayStr){
        Log.i(TAG,"jsonArray string:" + jsonArrayStr);
        if(db == null){
            Log.i(TAG,"db object is null");
            return;
        }
        try{
            JSONArray dataArray = new JSONArray(jsonArrayStr);
            for(int i = 0 ; i < dataArray.length() ; i++ ){
                JSONObject dataObj = (JSONObject) dataArray.get(i);
                insertRecord(tableName,dataObj.toString());
            }
        }catch (JSONException e){
            Log.e(TAG,"!!!!!!!!!!!!!" + e.getMessage());
        }
    }

    /*
     * 插入一条数据记录
     * @param jsonString
     *        key为数据表的名称,value为所要插入一行数据的JSON对象
     *
     */
    public void insertRecord(String tableName ,String jsonObjectStr){
        EMSqlInfo sqlInfo = EMSqlBuilder.buildInsertSql(tableName,jsonObjectStr);
        Log.i(TAG,"SQL :" + sqlInfo.getSql());
        db.execSqlInfo(sqlInfo);
    }


    public void deleteFromTables(String jsonString){
        Log.i(TAG,"deleteRords jsonString:" + jsonString);
        if(db == null){
            Log.i(TAG,"db object is null");
            return;
        }
        try{
            JSONObject obj = new JSONObject(jsonString);

            Iterator tableKeyIterator = obj.keys();
            while (tableKeyIterator.hasNext()){
                String tableName = (String)tableKeyIterator.next();
                JSONObject whereObj = obj.optJSONObject(tableName);
                deleteFromTable(tableName,whereObj.toString());
            }


        }catch (JSONException e){
            Log.e(TAG,"!!!!!!!!!!!!!" + e.getMessage());
        }
    }

    public void deleteFromTable(String tableName, String whereJsonString){
        Log.i(TAG,"deleteFromTable whereJsonString:" + whereJsonString);
        if(db == null){
            Log.i(TAG,"db object is null");
            return;
        }
        try{
            JSONObject whereObj = new JSONObject(whereJsonString);
            EMSqlInfo sqlInfo = EMSqlBuilder.buildDeleteSql(tableName,whereObj);

            Log.i(TAG,"SQL :" + sqlInfo.getSql());
//            db.execSqlInfo(sqlInfo);
        }catch (JSONException e){
            Log.e(TAG,"!!!!!!!!!!!!!" + e.getMessage());
        }
    }

    public void update(){

    }

    public void query(){

    }

    public void clearDatabase(){

    }
}
