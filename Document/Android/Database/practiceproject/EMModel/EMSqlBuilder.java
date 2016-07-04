package com.sokasyn.android.practice.SQLiteProject.EMModel;

import android.util.Log;

import com.sokasyn.android.practice.SQLiteProject.common.DebugLog;
import com.sokasyn.android.practice.SQLiteProject.exception.EMDatabaseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Samson on 16/6/27.
 */
public class EMSqlBuilder {

    private static final String TAG = EMSqlBuilder.class.getSimpleName();

    private String getClassName(){
        String className = this.getClass().getName();
        return className;
    }

    // * * * * * * * * * * Table Level: Build Create SQL * * * * * * * * * * //
    public static EMSqlInfo buildCreateTableSql(String tableName , String values){

        String className = Thread.currentThread().getStackTrace()[1].getClassName();
        DebugLog.methodStart(className);

        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("CREATE TABLE IF NOT EXISTS ");
        sqlBuffer.append(tableName);
        sqlBuffer.append("( ");
        sqlBuffer.append(values);
        sqlBuffer.append(" )");

        EMSqlInfo sqlInfo = new EMSqlInfo();
        sqlInfo.setSql(sqlBuffer.toString());
        return sqlInfo;
    }

    // * * * * * * * * * * Table Level : Build Drop Table SQL * * * * * * * * * * //
    public static EMSqlInfo buildDropTableSqlWithName(String tableName){
        if(tableName.trim().isEmpty() || tableName == null) {
            throw new EMDatabaseException("Table name can not be null");
        }
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("DROP TABLE IF EXISTS ");
        sqlBuffer.append(tableName);
        EMSqlInfo sqlInfo = new EMSqlInfo(sqlBuffer.toString());
        return sqlInfo;
    }

    public static ArrayList<EMSqlInfo> buildDropTableSqlWithJson(String jsonString) throws JSONException{
        JSONArray array = new JSONArray(jsonString);
        ArrayList<EMSqlInfo> sqlInfoList = new ArrayList<EMSqlInfo>();
        for(int i = 0; i < array.length(); i++){
            String tableName = array.getString(i);
            EMSqlInfo sqlInfo = buildDropTableSqlWithName(tableName);
            sqlInfoList.add(sqlInfo);
        }
        DebugLog.i(TAG,"Sql size:" + sqlInfoList.size());
        return sqlInfoList;
    }

    // * * * * * * * * * * Table Level : Build Alter Table SQL * * * * * * * * * * //
    private static String getAlterTableSql(String tableName){
        return "ALTER TABLE " + tableName;
    }

    public static EMSqlInfo buildAlterTableSql(String jsonString) throws JSONException{
//        ALTER TABLE USER ADD COLUMN ADDRESS VARCHAR(20)
        JSONObject alterObj = new JSONObject(jsonString);

        StringBuffer sqlBuffer = new StringBuffer();
        Iterator tableKeyIterator = alterObj.keys();
        while (tableKeyIterator.hasNext()){
            String tableName = (String)tableKeyIterator.next();
            sqlBuffer.append(getAlterTableSql(tableName));
            Object columnObj = alterObj.opt(tableName);
            if(columnObj instanceof JSONArray){



            }else{
                sqlBuffer.append(" ADD COLUMN ");
                sqlBuffer.append(columnObj.toString());
            }
        }
        Log.i(TAG,sqlBuffer.toString());
        return new EMSqlInfo(sqlBuffer.toString());
    }

    public static EMSqlInfo buildAlterTableSqlWithTable(String tableName){
        StringBuffer sqlBuffer = new StringBuffer(getAlterTableSql(tableName));
        return null;
    }

    // * * * * * * * * * *  Records Level: Build Insert SQL * * * * * * * * * * //
    public static EMSqlInfo buildInsertSql(String tableName, String jsonString){
        Log.i(TAG,"BuildInsertSql:table name:" + tableName + "jsonString: " + jsonString);
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("INSERT INTO ");
        sqlBuffer.append(tableName);
        sqlBuffer.append(" (");

        StringBuffer columnStr = new StringBuffer();
        StringBuffer valueStr = new StringBuffer();
        try{
            JSONObject jsonObj = new JSONObject(jsonString);
            Iterator it = jsonObj.keys();

            while (it.hasNext()){
                String key = (String)it.next();
                String value = jsonObj.optString(key);
                columnStr.append(key + ",");
                valueStr.append("'" + value + "'" + ",");
            }
            columnStr.deleteCharAt(columnStr.length() - 1);
            valueStr.deleteCharAt(valueStr.length() - 1);
        }catch (JSONException e){
            Log.e(TAG,"Exception occurred...");
        }

//        insert into staff (staff_id,first_name,last_name,address_id,store_id,username) values(2,'Sam','Hillyer',3,1,'Sam');
        sqlBuffer.append(columnStr);
        sqlBuffer.append(") VALUES (");
        sqlBuffer.append(valueStr);
        sqlBuffer.append(")");

        EMSqlInfo sqlInfo = new EMSqlInfo(sqlBuffer.toString());
        return  sqlInfo;
    }

    // * * * * * * * * * *  Records Level: Build Delete SQL * * * * * * * * * * //

    public static ArrayList<EMSqlInfo> buildDeleteSqlWithJSONString(String jsonString) throws JSONException{
        if(jsonString.trim().isEmpty()){
            DebugLog.i(TAG,"jsonString is empty");
        }else{
            DebugLog.i(TAG,jsonString);
        }
        ArrayList<EMSqlInfo> sqlInfoList = new ArrayList<EMSqlInfo>();
        JSONObject obj = new JSONObject(jsonString);
        Iterator tableKeyIterator = obj.keys();

        while (tableKeyIterator.hasNext()){
            EMSqlInfo sqlInfo;
            String tableName = (String)tableKeyIterator.next();
            Object object = obj.opt(tableName);
            if(object instanceof JSONObject){
                Log.i(TAG,"instanceof JSONObject");
                JSONObject whereObj = obj.optJSONObject(tableName);
                sqlInfo = buildDeleteSql(tableName,whereObj);
            }else{
                Log.i(TAG,"not instanceof JSONObject");
                sqlInfo = buildDeleteSql(tableName,null);
            }
            sqlInfoList.add(sqlInfo);
        }
        DebugLog.i(TAG,"Sql size:" + sqlInfoList.size());
        return sqlInfoList;
    }


    public static EMSqlInfo buildDeleteSqlWithJSONString(String tableName, String whereArgsJson) throws JSONException{
        Log.i(TAG,"buildDeleteSql: table name: " + tableName);
        EMSqlInfo sqlInfo;
        if(whereArgsJson != null || whereArgsJson.trim() != "" || whereArgsJson != "null"){
            Log.i(TAG,"where not null");
            JSONObject whereObj = new JSONObject(whereArgsJson);
            sqlInfo = buildDeleteSql(tableName,whereObj);
        }else{
            Log.i(TAG,"where null");
            sqlInfo = buildDeleteSql(tableName,null);
        }
        return sqlInfo;
    }

    public static EMSqlInfo buildDeleteSql(String tableName, JSONObject whereArgsJson){
        Log.i(TAG,"== buildDeleteSql: table name: " + tableName);
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("DELETE FROM ");
        sqlBuffer.append(tableName);

//        DELETE from user where name='Kate' and age='1' and ;
        if(whereArgsJson != null && whereArgsJson.length() != 0){
            sqlBuffer.append(" WHERE ");
            Iterator whereKeyIterator = whereArgsJson.keys();
            while (whereKeyIterator.hasNext()){
                String column = (String) whereKeyIterator.next();
                sqlBuffer.append(column);
                sqlBuffer.append("='");
                String value = whereArgsJson.optString(column);
                sqlBuffer.append(value);
                sqlBuffer.append("'");

                sqlBuffer.append(" AND ");
            }
            sqlBuffer.delete(sqlBuffer.length() - 5, sqlBuffer.length()-1);
        }
        EMSqlInfo sqlInfo = new EMSqlInfo(sqlBuffer.toString());
        return  sqlInfo;
    }

    // * * * * * * * * * *  Records Level : Build Update SQL * * * * * * * * * * //
    public static EMSqlInfo buildUpdateSql(String tableName, String updateString){
        Log.i(TAG,"=============buildUpdateSql: table name:" + tableName);
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("UPDATE ");
        sqlBuffer.append(tableName);
        sqlBuffer.append(" ");
        sqlBuffer.append(updateString);

        EMSqlInfo sqlInfo = new EMSqlInfo(sqlBuffer.toString());
        return  sqlInfo;
    }

    // * * * * * * * * * *  Records Level : Build Select SQL * * * * * * * * * * //
    private static String getSelectSqlWithTableName(String tableName){
        return new StringBuffer("SELECT * FROM ").append(tableName).toString();
    }
}
