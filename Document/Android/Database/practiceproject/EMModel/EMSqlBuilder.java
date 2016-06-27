package com.emin.digit.mobile.android.learning.practiceproject.EMModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Samson on 16/6/27.
 */
public class EMSqlBuilder {

    public EMSqlInfo buildCreateTableSql(String tableName , String values){
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("CREATE TABLE ");
        sqlStr.append(tableName);
        sqlStr.append(" VALUES( ");
        sqlStr.append(values);
        sqlStr.append(" )");
        return new EMSqlInfo();
    }

    public EMSqlInfo buildInsertSql(){
        EMSqlInfo sqlInfo = null;

        return  sqlInfo;
    }

    public EMSqlInfo buildUpdateSql(){
        EMSqlInfo sqlInfo = null;

        return  sqlInfo;
    }
}
