package com.emin.digit.mobile.android.learning.practiceproject.EMModel;

import android.util.Log;

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
     * @param jsonArray
     *        数组中的每一个元素都应当是一个以key为表名,value为字段列表的json字符串
     *        即每一个元素对应一张表
     * @return
     */
    public void createTables(JSONArray jsonArray){

    }

    /**
     * 创建数据表
     *
     * @param jsonString
     *        数组中的每一个Key-value键值对都应当是一个以key为表名,value为字段列表的json字符串
     *        每一个key-value对,对应一张表
     * @return
     */
    public void createTables(String jsonString){
            try{
                JSONObject obj = new JSONObject(jsonString);
                Iterator iterator = obj.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    String value = obj.getString(key);
                }
            }catch (JSONException e){

            }


    }


}
