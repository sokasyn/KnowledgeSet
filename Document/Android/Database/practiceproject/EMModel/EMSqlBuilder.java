package com.emin.digit.mobile.android.learning.practiceproject.EMModel;

import android.util.Log;

import com.emin.digit.mobile.android.learning.practiceproject.common.ThisApplication;

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

    //////////////////////////////create table sql start/////////////////////////////////////////////

    public static EMSqlInfo buildCreateTableSql(String tableName , String values){

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

    //////////////////////////////insert sql start/////////////////////////////////////////////

    public static EMSqlInfo buildInsertSql(String tableName, String jsonString){
        Log.i(TAG,"=============buildInsertSql: table name:" + tableName + "jsonString: " + jsonString);
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

    //////////////////////////////delete sql start/////////////////////////////////////////////

    public static ArrayList<EMSqlInfo> buildDeleteSqlWithJSONString(String jsonString) throws JSONException{
        ArrayList<EMSqlInfo> sqlInfoList = new ArrayList<EMSqlInfo>();
        JSONObject obj = new JSONObject(jsonString);
        Iterator tableKeyIterator = obj.keys();
        while (tableKeyIterator.hasNext()){
            EMSqlInfo sqlInfo = null;
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

    //////////////////////////////update sql start/////////////////////////////////////////////

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

    //////////////////////////////select sql start/////////////////////////////////////////////
    private static String getSelectSqlWithTableName(String tableName){
        return new StringBuffer("SELECT * FROM ").append(tableName).toString();
    }
}