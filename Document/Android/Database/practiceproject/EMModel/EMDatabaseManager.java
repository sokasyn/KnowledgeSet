package com.emin.digit.mobile.android.learning.practiceproject.EMModel;

import android.database.Cursor;
import android.util.Log;

import com.emin.digit.mobile.android.learning.practiceproject.exception.EMDatabaseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
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

    //******************************** Database Create/Open ********************************/

    public EMDatabase createOrOpenDatabase(EMDaoConfig daoConfig){
        try{
            db = EMDatabase.create(daoConfig);
        }catch (EMDatabaseException e){
            Log.e(TAG,e.getMessage());
            return null;
        }
        return db;
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
     * @return
     */
    public void createTables(String jsonString){
        if(db == null){
            Log.i(TAG,"db object is null");
            return;
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
                db.execSqlInfo(sqlInfo);
            }
        }catch (JSONException e){
            Log.e(TAG,"JSONException occurred.......");
        }
    }

    //************************************** Drop Tables **************************************/

    /**
     * 删除所有数据表
     */
    public void dropAllTables() {
        EMSqlInfo sqlInfo = new EMSqlInfo();
        String sqlStr = "SELECT name FROM sqlite_master WHERE type ='table' AND name != 'sqlite_sequence'";
        Cursor cursor = db.queryWithSqlInfo(new EMSqlInfo(sqlStr));
        if (cursor != null) {
            while (cursor.moveToNext()) {
                db.execSQL("DROP TABLE " + cursor.getString(0));
            }
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
    }

    public void dropTable(String tableName){

    }

    //************************************** Drop Tables **************************************/
    public void updateTable(){

    }

    //************************************** Insert Records **************************************/

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


    //************************************** Delete Record **************************************/

    /*
    * 删除数据记录,可删除多个表中的数据
    * @param jsonString
    *        key为数据表的名称,value为删除数据的条件的JSON对象字符串
    *        格式形如:
    *        {"TABLE1":{"ID":100},
    *         "TABLE2":{"ID":2,"NAME":"ABC"}
    *         "TABLE3":null}
    *
    *         删除TABLE1中ID为100的数据；
    *         删除TABLE2中ID为2且NAME为ABC的数据;
    *         删除TABLE3所有的数据
    */
    public void deleteWithJsonString(String jsonString){
        Log.i(TAG,"deleteRords jsonString:" + jsonString);
        if(db == null){
            Log.i(TAG,"db object is null");
            return;
        }
        try{
            ArrayList<EMSqlInfo> arrayList = EMSqlBuilder.buildDeleteSqlWithJSONString(jsonString);
            for(int i = 0 ; i < arrayList.size(); i++){
                db.execSqlInfo(arrayList.get(i));
            }

        }catch (JSONException e){
            Log.e(TAG,"!!!!!!!!!!!!!" + e.getMessage());
        }
    }

    /*
    * 删除单个表中的满足条件的数据记录
    * @param tableName 目标数据表
    * @param whereJsonString 条件JSON格式字符串
    *        key为数据表的名称,value为删除数据的条件的JSON对象字符串
    *        如DELETE FROM TABLE_NAME WHERE COLUMN_NAME_1 =100 AND COLUMN_NAME_2='ABC' ;
    *        参数 whereJsonString 则为 {"COLUMN_NAME_1":100,"COLUMN_NAME_2":"ABC"}
    *
    */
    public void deleteFromTable(String tableName, String whereArgsJson){
        Log.i(TAG,"deleteFromTable whereJsonString:" + whereArgsJson);
        if(db == null){
            Log.i(TAG,"db object is null");
            return;
        }
        try{
            EMSqlInfo sqlInfo = EMSqlBuilder.buildDeleteSqlWithJSONString(tableName,whereArgsJson);
            db.execSqlInfo(sqlInfo);
        }catch (JSONException e){
            Log.e(TAG,"!!!!!!!!!!!!!" + e.getMessage());
        }
    }

    public void deleteFromTable(String tableName){
        EMSqlInfo sqlInfo = EMSqlBuilder.buildDeleteSql(tableName,null);
        Log.i(TAG,"SQL :" + sqlInfo.getSql());
        db.execSqlInfo(sqlInfo);

    }

    public void deleteWithSQL(String sqlStatement){
        db.execSqlInfo(new EMSqlInfo(sqlStatement));
    }

    //************************************** Update record **************************************/
    /*
    * 更新记录，可更新多个表中的记录
    * @param updateJsonString
    *        key为数据表的名称,value为更新的更新sql中的set即where语句
    *
    */
    public void updateRecords(String updateJsonString){
        Log.i(TAG,"updateWithJsonString updateJsonString:" + updateJsonString);
        try{
            JSONObject updateObj = new JSONObject(updateJsonString);

            Iterator tableKeyIterator = updateObj.keys();
            while (tableKeyIterator.hasNext()){
                String tableName = (String) tableKeyIterator.next();
                String updateStr = updateObj.optString(tableName);
                updateRecord(tableName,updateStr);
            }
        }catch (JSONException e){
            Log.e(TAG,"!!!!!!!!!!!!!" + e.getMessage());
        }
    }

    /*
    * 更新单个表中的记录
    * @param tableName 表名
    * @param updateStr 更新sql中的set和where语句
    *
    */
    public void updateRecord(String tableName,String updateStr){
        Log.i(TAG,"updateRecord :" + updateStr);
        EMSqlInfo sqlInfo = EMSqlBuilder.buildUpdateSql(tableName,updateStr);
        Log.i(TAG,"SQL :" + sqlInfo.getSql());
        db.execSqlInfo(sqlInfo);
    }

    //************************************** Query Records **************************************/

    public String query() throws JSONException{
        Log.i(TAG,"query=======");
        String sqlStr = "SELECT * FROM USER";
        EMSqlInfo sqlInfo = new EMSqlInfo(sqlStr);
        Cursor cursor = db.queryWithSqlInfo(sqlInfo);

        JSONArray jsonArray = new JSONArray();
        while (cursor.moveToNext()){
            int columnCount = cursor.getColumnCount();
            Log.i(TAG,"column count:" + columnCount);
            String[] column = cursor.getColumnNames();
            for(String columnName : column){
                Log.i(TAG,"column name:" + columnName);
            }

            JSONObject rowDataObj = new JSONObject();
            for(int i = 0 ; i < columnCount ; i++){
                String columnName = cursor.getColumnName(i);
                String columnValue = cursor.getString(i);
                rowDataObj.put(columnName,columnValue);
            }
            jsonArray.put(rowDataObj);
        }

        if(cursor != null){
            cursor.close();
            cursor = null;
        }

        return jsonArray.toString();
    }



}
