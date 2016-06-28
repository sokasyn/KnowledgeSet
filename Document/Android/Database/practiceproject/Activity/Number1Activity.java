package com.emin.digit.mobile.android.learning.practiceproject;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.emin.digit.mobile.android.learning.practiceproject.EMModel.EMDaoConfig;
import com.emin.digit.mobile.android.learning.practiceproject.EMModel.EMDatabase;
import com.emin.digit.mobile.android.learning.practiceproject.EMModel.EMDatabaseManager;
import com.emin.digit.mobile.android.learning.practiceproject.Model.DatabaseHelper;
import com.emin.digit.mobile.android.learning.practiceproject.Model.DatabaseManager;
import com.emin.digit.mobile.android.learning.practiceproject.common.ThisApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Samson on 16/6/22.
 */
public class Number1Activity extends Activity implements OnClickListener {

    private static final String TAG = Number1Activity.class.getSimpleName();

    private static final String DB_NAME = "fish_1.db";

    //声明五个控件对象
    Button createDatabase=null;
    Button updateDatabase=null;
    Button insert=null;
    Button update=null;
    Button query=null;
    Button delete=null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
        findViews();
    }

    private void findViews() {
        //根据控件ID得到控件
        createDatabase = (Button) this.findViewById(R.id.createDatabase);
        updateDatabase = (Button) this.findViewById(R.id.updateDatabase);
        insert = (Button) this.findViewById(R.id.insert);
        update = (Button) this.findViewById(R.id.update);
        query = (Button) this.findViewById(R.id.query);
        delete = (Button) this.findViewById(R.id.delete);
        //添加监听器
        createDatabase.setOnClickListener(this);
        updateDatabase.setOnClickListener(this);
        insert.setOnClickListener(this);
        update.setOnClickListener(this);
        query.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    // 创建数据库
    private static final String DB_NAME_IN_NUMBER1_ACTIVITY = "number1_1.db";
    private void createDatabaseWithEMLib(){
        Log.i(TAG,"createDatabaseWithEMLib");
        // 构造配置模版
        EMDaoConfig daoConfig = new EMDaoConfig();
        daoConfig.setContext(Number1Activity.this);
        daoConfig.setDbName(DB_NAME_IN_NUMBER1_ACTIVITY);
        daoConfig.setDbVersion(1);
        daoConfig.setTargetDirectory(null);
        EMDatabase database = EMDatabase.create(daoConfig);
        EMDatabaseManager.getInstance().createOrOpenDatabase(daoConfig);
    }


    private static final String TBL_USER = "user";
    private static final String TBL_ACCOUNT = "account";
    private static final String TBL_ADDRESS = "address";

    // 创建数据表
    private void initDatabase(){
        try{
            JSONObject obj = new JSONObject();
            // table:user
            obj.put(TBL_USER,"name,age");

            // table:account
            obj.put(TBL_ACCOUNT,"name,password");

            // table:address
            obj.put(TBL_ADDRESS,"id,pid,name");

            String jsonStr = obj.toString();
            Log.i(TAG,"json string:" + jsonStr);

            EMDatabaseManager.getInstance().createTables(jsonStr);
        }catch (JSONException e){
            Log.e(TAG,"JSONException occurred");
        }
    }

    // 插入表数据
    private void insertData(){

        // 一个json string
        try {
            JSONObject dataObj = new JSONObject();

            // ----- TABLE USER
            // 多条数据存放与array中
            JSONArray userData = new JSONArray();
            JSONObject userObj = new JSONObject();
            userObj.put("name","Kate1");
            userObj.put("age","1");
            userData.put(0,userObj);

            JSONObject userObj2 = new JSONObject();
            userObj2.put("name","Kate2");
            userObj2.put("age","2");
            userData.put(1,userObj2);

            JSONObject userObj3 = new JSONObject();
            userObj3.put("name","Kate3");
            userObj3.put("age","3");
            userData.put(2,userObj3);

            dataObj.put(TBL_USER,userData);


            // ----- TABLE ACCOUNT
            // 单条数据直接一个JSONObject
            JSONObject accountObj = new JSONObject();
            accountObj.put("name","Kate");
            accountObj.put("password","123456");
            dataObj.put(TBL_ACCOUNT,accountObj);

            EMDatabaseManager.getInstance().insertRecords(dataObj.toString());

        }catch (JSONException e){
            Log.e(TAG,e.getMessage());
        }

    }

    // 删除数据
    private void deleteData(){
        Log.i(TAG,"test delete records");

        try {
            JSONObject obj = new JSONObject();

            JSONObject whereObj = new JSONObject();
            whereObj.put("name","Samson");
            obj.put(TBL_USER,whereObj);

            EMDatabaseManager.getInstance().deleteFromTables(obj.toString());
        }catch (JSONException e){
            Log.e(TAG,"!!!!!!" + e.getMessage());
        }


    }

    @Override
    public void onClick(View v) {
        //判断所触发的被监听控件，并执行命令
        switch(v.getId()){
            //创建数据库
            case R.id.createDatabase:
                //创建一个DatabaseHelper对象
//                DatabaseHelper dbHelper1 = new DatabaseHelper(Number1Activity.this, DB_NAME);
                //取得一个只读的数据库对象
//                SQLiteDatabase db1 = dbHelper1.getReadableDatabase();

                createDatabaseWithEMLib();
                break;
            //更新数据库
            case R.id.updateDatabase:
                initDatabase();
//                DatabaseHelper dbHelper2 = new DatabaseHelper(getApplicationContext(), "fish_2.db");
//                SQLiteDatabase db2 = dbHelper2.getReadableDatabase();

//                DatabaseHelper dbHelper2 = new DatabaseHelper(Number1Activity.this, DB_NAME, 2);
//                SQLiteDatabase db2 = dbHelper2.getReadableDatabase();

//                DatabaseManager dbManager = DatabaseManager.getInstance(getApplicationContext());
//                SQLiteDatabase db2 = dbManager.getDatabase();

                break;
            //插入数据
            case R.id.insert:
                Log.i(TAG,"inser event called....");
                insertData();

                /*
                try{
                    JSONObject obj = new JSONObject();
                    obj.putOpt("id", "1");
                    obj.putOpt("name", "cocoa");
                    String jsonStr = obj.toString();
                    DatabaseManager.getInstance().insert("user",jsonStr);
                }catch (JSONException e){
                    Log.e(TAG,"JSONException occured--------");
                    e.printStackTrace();
                }
                */

                /*
                //创建存放数据的ContentValues对象
                ContentValues values = new ContentValues();
                //像ContentValues中存放数据
                values.put("id", 1);
                values.put("name","zhangsan");
                DatabaseHelper dbHelper3 = new DatabaseHelper(Number1Activity.this, DB_NAME);
                SQLiteDatabase db3 = dbHelper3.getWritableDatabase();
                //数据库执行插入命令
                db3.insert("user", null, values);
                */
                break;
            //更新数据信息
            case R.id.update:
                DatabaseHelper dbHelper4 = new DatabaseHelper(ThisApplication.getAppContext());
                SQLiteDatabase db4 = dbHelper4.getReadableDatabase();
                /*
                DatabaseHelper dbHelper4 = new DatabaseHelper(Number1Activity.this, DB_NAME);
                SQLiteDatabase db4 = dbHelper4.getWritableDatabase();
                ContentValues values2 = new ContentValues();
                values2.put("name", "xiaosan");
                db4.update("user", values2, "id=?", new String[]{"1"});
                */
                break;
            //查询信息
            case R.id.query:
                DatabaseHelper dbHelper5 = new DatabaseHelper(Number1Activity.this, DB_NAME);
                SQLiteDatabase db5 = dbHelper5.getReadableDatabase();
                //创建游标对象
                Cursor cursor = db5.query("user", new String[]{"id","name"}, "id=?", new String[]{"1"}, null, null, null, null);
                //利用游标遍历所有数据对象
                while(cursor.moveToNext()){
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    //日志打印输出
                    Log.i(TAG,"query-->"+name);
                }
                break;
            //删除记录
            case R.id.delete:
                deleteData();
                /*
                DatabaseHelper dbHelper6 = new DatabaseHelper(Number1Activity.this,"test_db");
                SQLiteDatabase db6 = dbHelper6.getWritableDatabase();
                db6.delete("user", "id=?", new String[]{"1"});
                */
                break;
            default:
                Log.i(TAG,"error");
                break;
        }
    }
}
