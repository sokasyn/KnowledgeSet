package com.emin.digit.mobile.android.learning.practiceproject.Util;

import android.util.Log;

import com.emin.digit.mobile.android.learning.practiceproject.common.ConstantTable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Samson on 16/7/1.
 */
public class BuildDeleteJSON {

    private static final String TAG = BuildJSONData.class.getSimpleName();

    private static boolean isDebug = false;
    /*
     * Delete sql 中的where条件是一个json object 的字符串
     */
    public static String buildDeleteJsonForCase(int caseNum) throws JSONException{
        JSONObject dataObj = new JSONObject();
        switch (caseNum){
            // 单个表,单个条件
            case 0:{
                JSONObject whereObj = new JSONObject();
                whereObj.put("name","Kate0");
                dataObj.put(ConstantTable.TBL_USER,whereObj);
                break;
            }
            // 单个表,两个条件
            case 1 :{
                JSONObject whereObj = new JSONObject();
                whereObj.put("name","Kate1");
                whereObj.put("age","1");
                dataObj.put(ConstantTable.TBL_USER,whereObj);
                break;
            }
            // 空Json对象
            // {}
            case 2 :{
                break;
            }
            // 单个表,无条件 (清空目标表数据)
            // {"USER":{}}
            case 3 :{
                JSONObject whereObj = new JSONObject();
                dataObj.put(ConstantTable.TBL_USER,whereObj);
                break;
            }

            // 多个表在json中,单个条件
            // {"USER":{"name":"Kate4"},"ACCOUNT":{"name":"Sam1"}}
            case 4 :{
                JSONObject userWhereObj = new JSONObject();
                userWhereObj.put("name","Kate4");
                dataObj.put(ConstantTable.TBL_USER,userWhereObj);

                JSONObject accountWhereObj = new JSONObject();
                accountWhereObj.put("name","Sam1");
                dataObj.put(ConstantTable.TBL_ACCOUNT,accountWhereObj);

                break;
            }

            // 多个表在json中,两个条件
            // {"USER":{"name":"Kate5","age":"5"},"ACCOUNT":{"name":"Sam2","password":"2"}}
            case 5 :{
                JSONObject userWhereObj = new JSONObject();
                userWhereObj.put("name","Kate5");
                userWhereObj.put("age","5");
                dataObj.put(ConstantTable.TBL_USER,userWhereObj);

                JSONObject accountWhereObj = new JSONObject();
                accountWhereObj.put("name","Sam2");
                accountWhereObj.put("password","2");
                dataObj.put(ConstantTable.TBL_ACCOUNT,accountWhereObj);
                break;
            }
            // 多个表在json中,三个条件
            case 6 :{
                break;
            }
            // 多个表在json中,多个表,条件混合
            // {"USER":{"name":"Kate7"},"ACCOUNT":{"name":"Sam3","password":"3"},"ADDRESS":{"id":"1","pid":"1","name":"Michael"}}
            case 7 :{
                JSONObject userWhereObj1 = new JSONObject();
                userWhereObj1.put("name","Kate6");
                dataObj.put(ConstantTable.TBL_USER,userWhereObj1);

                JSONObject userWhereObj2 = new JSONObject();
                userWhereObj2.put("name","Kate7");
                dataObj.put(ConstantTable.TBL_USER,userWhereObj2);

                JSONObject accountWhereObj = new JSONObject();
                accountWhereObj.put("name","Sam3");
                accountWhereObj.put("password","3");
                dataObj.put(ConstantTable.TBL_ACCOUNT,accountWhereObj);

                JSONObject addressWhereObj = new JSONObject();
                addressWhereObj.put("id","1");
                addressWhereObj.put("pid","1");
                addressWhereObj.put("name","Michael");
                dataObj.put(ConstantTable.TBL_ADDRESS,addressWhereObj);
                break;
            }
            // 多个表在json中,多个表,都无条件
            case 8 :{
                JSONObject userWhereObj = new JSONObject();

                dataObj.put(ConstantTable.TBL_USER,userWhereObj);

                JSONObject accountWhereObj = new JSONObject();
                dataObj.put(ConstantTable.TBL_ACCOUNT,accountWhereObj);

                JSONObject addressWhereObj = new JSONObject();
                dataObj.put(ConstantTable.TBL_ADDRESS,accountWhereObj);
                break;
            }
        }
        String jsonString = dataObj.toString();
        if(isDebug){
            Log.i(TAG,"Build json string:" + jsonString);
        }
        return jsonString;
    }


    // ********* 构建测试 JSON String **************
    // 针对某个表的数据删除
    // {"name":"Kate8"}
    public static String buildDeleteJsonWithTable(String tableName) throws JSONException{
        JSONObject whereObj =  new JSONObject();
        whereObj.put("name","Kate8");
        String jsonString = whereObj.toString();
        return jsonString;
    }

    private JSONObject getJsonObjet(String key, String value) throws JSONException{
        JSONObject obj = new JSONObject();
        obj.put(key,value);
        return obj;
    }
}
