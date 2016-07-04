package com.sokasyn.android.practice.SQLiteProject.Util;

import com.sokasyn.android.practice.SQLiteProject.common.ConstantTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Samson on 16/7/1.
 */
public class BuildTableJSON {

    public static String buildAlterTable(int caseNum) throws JSONException{
        JSONObject alterObj = new JSONObject();
        switch (caseNum){
            case 0:{
                alterObj.put(ConstantTable.TBL_USER,"telNum");
                break;
            }
            case 1:{
                alterObj.put(ConstantTable.TBL_ACCOUNT,"memo varchar(20)");
                break;
            }
        }
        return alterObj.toString();
    }


    public static String buildDropJson(int caseNum) throws JSONException {
        JSONArray tableObj =  new JSONArray();

        switch (caseNum) {
            // 单个表
            case 0: {
                tableObj.put(ConstantTable.TBL_USER);
                break;
            }
            // 两个表
            case 1: {
                tableObj.put(ConstantTable.TBL_ACCOUNT);
                tableObj.put(ConstantTable.TBL_ADDRESS);
                break;
            }
            // 三个表
            // ["USER","ACCOUNT","ADDRESS"]
            case 2:{
                tableObj.put(ConstantTable.TBL_USER);
                tableObj.put(ConstantTable.TBL_ACCOUNT);
                tableObj.put(ConstantTable.TBL_ADDRESS);
                break;
            }
            default:{
                break;
            }
        }
        String jsonString = tableObj.toString();
        System.out.println("drop table json string:" + jsonString);
        return jsonString;
    }
}
