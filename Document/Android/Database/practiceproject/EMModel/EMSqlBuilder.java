package com.emin.digit.mobile.android.learning.practiceproject.EMModel;

import android.util.Log;

import com.emin.digit.mobile.android.learning.practiceproject.common.ThisApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Samson on 16/6/27.
 */
public class EMSqlBuilder {

    private static final String TAG = EMSqlBuilder.class.getSimpleName();

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

    public static EMSqlInfo buildDeleteSql(String tableName, JSONObject whereArgsJson){
        Log.i(TAG,"=============buildDeleteSql: table name:" + tableName);
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("DELETE FROM ");
        sqlBuffer.append(tableName);

//        DELETE from user where name='Kate' and age='1' and ;
        if(whereArgsJson != null){
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
            sqlBuffer.delete(sqlBuffer.length()-5, sqlBuffer.length()-1);

        }
        EMSqlInfo sqlInfo = new EMSqlInfo(sqlBuffer.toString());
        return  sqlInfo;
    }

    public EMSqlInfo buildUpdateSql(){
        EMSqlInfo sqlInfo = null;

        return  sqlInfo;
    }
}
